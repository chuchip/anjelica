package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Titulo: utildesp </p>
 * <p>Descripcion: Serie de Funciones con Utilidades para mant. de Despiece
 * Localiza datos de trazabilidad de  un individuo en particular.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.compras.pdalbco2;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
import org.apache.log4j.Logger;

public class utildesp
{
 
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
  private Date fecCadPrvE,fecCompraE,fecDespE,fecProdE;
  
 // private int deoNumdes=0;
  public boolean swDesp; // Indica si el individuo salio de un despiece
  private String msgAviso;
  private Date deoFecval;
  public String fecrepL;
  public String fecrecepE;
  public String nacidoE,cebadoE,despiezadoE,ntrazaE,conservarE,sacrificadoE;
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
  public boolean cambio=true;
  public int prvCodi=0;
  private int acp_painac,acp_engpai,acp_paisac;
  private int mat_codi,sde_codi;
  private String mat_nrgsa,sde_nrgsa,acp_paisacNomb;
// Variables Cabecera de Despiece
  int ejeNume, empCodi, numDesp;
  int almDest, almOrig;
  DatosTabla dtAdd;
  int grdNume=0;
  EntornoUsuario EU;
  actStkPart stkPart;
  etiqueta etiq;

  Logger loger = Logger.getLogger(utildesp.class.getName());
  public int prvCompra;
  private boolean swSaltaDespInt=false;
  private boolean swAlbInt=false;
  private int  ejeLotY,proLoteY,  empLotY ;

  public utildesp()
  {

  }

  public void setSaltaDespInt(boolean saltaDespInt)
  {
    swSaltaDespInt = saltaDespInt;
  }

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
   * @param dt1
   * @param dt2
   * @param EU
   * @return
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
   * @throws ParseException
   * @return boolean
   */
    
    public boolean busDatInd(String serie, int proCodi, int empLot,
            int ejeLot, int proLote,
            int proIndi,
            int almCodi, DatosTabla dtCon1, DatosTabla dtStat,
            EntornoUsuario EU) throws SQLException {
        setBuscaCompra(false);
        return buscaDatosIndiv(serie, proCodi, empLot, ejeLot, proLote, proIndi, almCodi, dtCon1, dtStat, EU);
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
   * @return boolean true si encuentra los datos de compra.
   */
    private boolean buscaDatosIndiv(String serie, int proCodi, int empLot,
            int ejeLot, int proLote,
            int proIndi,
            int almCodi, DatosTabla dtCon1, DatosTabla dtStat,
            EntornoUsuario EU) throws SQLException {
        prvCodi = 0;
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
    ntrazaE = "****";
    sacrificadoE = "****";
    despiezadoE = null;
    nacidoE = null;
    cebadoE = null;
    fecSacrE = null;
    
    feccadE=null;
    fecCadDesp=null;
    deoCodi=0;
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
    boolean swTidCodi;
    s = "SELECT * FROM v_empresa WHERE emp_codi = " + empLot;
    if (!dtCon1.select(s))
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
        nacidoE="----";
        return false;
      }
      // Primero busco en Compras el Individuo mandado.
      s = "select p.*,c.acc_fecrec,c.prv_codi,c.acc_serie,c.acc_nume,"+
          " l.acl_prcom+c.acc_impokg as prcom  "+
         " from  v_albcompar p,v_albacoc c,v_albacol as l " +
          " WHERE p.emp_codi = " + empLot +
          " and p.acc_ano = " + ejeLot +
          " and p.acc_serie = '" + serie + "'" +
          " and p.acc_nume = " + proLote +
          " and "+(proIndi==0?"l.pro_codi ="+proCodi
          :" p.acp_numind = " + proIndi )+
          " and p.pro_codi = " + proCodi +
          (almCodi!=0?" and l.alm_codi = "+almCodi:"")+
          " and p.emp_codi = c.emp_codi " +
          " and p.acc_ano = c.acc_ano " +
          " and p.acc_serie = c.acc_serie " +
          " and p.acc_nume = c.acc_nume "+
          " and c.emp_codi = l.emp_codi " +
          " and c.acc_ano = l.acc_ano " +
          " and c.acc_serie = l.acc_serie " +
          " and c.acc_nume = l.acc_nume "+
          " and p.acl_nulin = l.acl_nulin ";

      dtRes=dtStat.select(s);
      if (!dtRes )
      {
        // No encontrado individuo  en compras, lo busco en salidas de despieces
        s = "SELECT df.*,de.tid_codi FROM v_despfin as df,desporig as de "+
            " WHERE df.pro_codi = " + proCodi +
            " and df.def_ejelot = " + ejeLot +
            " and df.def_serlot = '" + serie + "'" +
            " and df.pro_lote = " + proLote +
            " and "+(proIndi==0?"df.pro_codi= "+proCodi
            :" df.pro_numind = " + proIndi)+
            (almCodi!=0?" and de.deo_almdes = "+almCodi:"")+
            " and df.eje_nume = de.eje_nume "+
            " and df.deo_codi = de.deo_codi "+
            " order by df.def_tiempo desc";
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
                        + (EU == null ? EU.em_cod + "-" : "") + dtCon1.getInt("eje_nume")
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
                feccadE = dtCon1.getFecha("def_feccad", "dd-MM-yyyy");
            if (fecCadDesp==null && dtCon1.getObject("def_feccad") != null)
                fecCadDesp=dtCon1.getDate("def_feccad");
            swDesp = true;
        }
      }
      else
      { // Encontrado en Compras.
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
            feccadE=dtStat.getFecha("acp_feccad","dd-MM-yyyy");
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
          acp_painac=dtStat.getInt("acp_painac");
          s = "SELECT pai_nomb FROM v_paises WHERE pai_codi = " +
              dtStat.getInt("acp_painac");
          if (!dtCon1.select(s))
          {
            msgAviso = "No encontrado PAIS NACIMIENTO: " +
                dtStat.getInt("acp_painac");
            nacidoE = "";
          }
          else
            nacidoE = dtCon1.getString("pai_nomb");
          acp_engpai=dtStat.getInt("acp_engpai");
          s = "SELECT pai_nomb FROM v_paises WHERE pai_codi = " +
              dtStat.getInt("acp_engpai");
          if (!dtCon1.select(s))
          {
            msgAviso = "No encontrado PAIS CEBADO: " +
                dtStat.getInt("acp_painac");
            cebadoE = "";
          }
          else
            cebadoE = dtCon1.getString("pai_nomb");
          mat_codi = dtStat.getInt("mat_codi");
          s = "SELECT mat_nrgsa,pai_codi FROM v_matadero m WHERE m.mat_codi = " +
              dtStat.getInt("mat_codi");
          if (!dtCon1.select(s))
          {
            msgAviso = "No encontrado MATADERO: " +
                dtStat.getInt("mat_codi");
            sacrificadoE = "";
          }
          else
          {
            sacrificadoE =dtCon1.getString("mat_nrgsa");
            mat_nrgsa=sacrificadoE;
            s = "select pai_nomb from v_paises where pai_codi = " + dtCon1.getInt("pai_codi");
            if (dtCon1.select(s))
              sacrificadoE = dtCon1.getString("pai_nomb") + "-" + sacrificadoE;
          }
          acp_paisac = dtStat.getInt("acp_paisac");
          s = "select pai_nomb from v_paises where pai_codi = " + acp_paisac;
          if (dtCon1.select(s))
            acp_paisacNomb = dtCon1.getString("pai_nomb");
          sde_codi=dtStat.getInt("sde_codi");
          if (swDesp)
            s = "select pai_nomb from v_paises where pai_codi = " + paiEmp;
          else
            s = "SELECT pai_codi,sde_nrgsa FROM v_saladesp m " +
                " WHERE  m.sde_codi = " + dtStat.getInt("sde_codi");
          if (!dtCon1.select(s))
          {
            msgAviso = "No encontrado Sala de Despiece: " +
                dtStat.getInt("mat_codi");
            despiezadoE = "";
          }
          else
          {
            if (!swDesp)
            {
              despiezadoE =dtCon1.getString("sde_nrgsa");
              sde_nrgsa=despiezadoE;
              s = "select pai_nomb from v_paises where pai_codi = " + dtCon1.getInt("pai_codi");
              if (dtCon1.select(s))
                despiezadoE=dtCon1.getString("pai_nomb") + "-" +despiezadoE;
            }
            else
              despiezadoE = dtCon1.getString("pai_nomb") + "-" +empRGSA;
          }
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
                   ntrazaE=pdalbco2.getRandomCrotal(ntrazaE);
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
    * @param fecDesp Fecha Despiece
    * @param dt Datos Tabla para buscar nombre pais desp.
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
    String s = "select pai_nomb from v_paises where pai_codi = " + paiEmp;
    if (!dt.select(s))
    {
      msgAviso = "No encontrado PAIS de Despiece: " + paiEmp;
      despiezadoE = "";
      return;
    }
    despiezadoE = dt.getString("pai_nomb") + "-" + empRGSA;
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
  public String getConservar()
  {
      return conservarE;
  }
  public void actualConservar(int codProd, DatosTabla dtCon1) throws SQLException
  {
    conservarE=gnu.chu.anjelica.pad.MantArticulos.getStrConservar(codProd,dtCon1);
  }
  /**
   * Devuelve fecha congelado
   * @param codProd
   * @param fecProd Fecha Produccion
   * @param dtCon1
   * @return string "" si el producto no es congelado
   *
   * @throws SQLException
   */
  public static String getFechaCongelado(int codProd, java.util.Date fecProd, DatosTabla dt) throws SQLException
  {
     if (fecProd==null)
         return "";
     Boolean b=gnu.chu.anjelica.pad.MantArticulos.isCongelado(codProd,dt);
     if (b==null)
         return "";
     if (!b)
         return "";
     java.util.Date fecCong=Formatear.sumaMesDate(fecProd, dt.getInt("pro_cadcong"));
     return "Cad.Cong:"+ Formatear.getFecha(fecCong, "dd-MM-yy");
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
   * @throws ParseException
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

//  public static String buscaPeso(DatosTabla dt,int ejeLot,int empLot,
//                                 String serLot,int numLot,int numind,int proCodi) throws SQLException
//  {
//    return buscaPeso(dt,ejeLot,empLot,serLot,numLot,numind,proCodi,0);
//  }
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
    String s = "SELECT * FROM V_STKPART WHERE " +
        " EJE_NUME= " + ejeLot +
        " AND EMP_CODI= " + empLot +
        " AND PRO_SERIE='" + serLot + "'" +
        " AND pro_nupar= " + numLot +
        " and pro_numind= " + numind +
        " and pro_codi= " + proCodi+
        (almCodi!=0?" and alm_codi = "+almCodi:"");
    if (!dt.select(s))
    {
      // Compruebo si el producto tiene control stock por individuo
      s = "SELECT pro_coinst,pro_stock as stp_kilact,1 as stp_unact "+
          " FROM V_ARTICULO WHERE pro_codi = " +proCodi;
      if (!dt.select(s))
        return new StkPartid(StkPartid.ARTIC_NOT_FOUND);
      if (dt.getInt("pro_coinst") == 0)
        return new StkPartid(StkPartid.ARTIC_SIN_CONTROL_INDIV);        // SIN CONTROL individual devolvemos el STOCK
      return new StkPartid(StkPartid.INDIV_NOT_FOUND); // "NO encontrado Partida para estos valores";
    }
    if (! incBloq &&  dt.getInt("stk_block")!=0)
         return new StkPartid(StkPartid.INDIV_LOCK,dt.getDouble("stp_kilact"),dt.getInt("stp_unact")) ; //"Individuo Bloqueado";
    return new StkPartid(StkPartid.INDIV_OK,dt.getDouble("stp_kilact"),dt.getInt("stp_unact")) ;
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
  public void setStkPart(actStkPart stkPart)
  {
    this.stkPart=stkPart;
  }

  public actStkPart getStkPart()
  {
    return this.stkPart;
  }

  public void iniciar(DatosTabla dtAdd,int ejeNume,int empCodi,int almDest,int almOrig,EntornoUsuario EU)
  {
    this.dtAdd=dtAdd;
    this.ejeNume=ejeNume;
    this.empCodi=empCodi;
    this.almDest=almDest;
    this.almOrig=almOrig;
    this.numDesp=0;
    this.EU=EU;
    if (stkPart==null)
      stkPart=new actStkPart(dtAdd,EU.em_cod);
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
  *  @see setRepiteIndiv
   * @return
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
 * @throws Exception
 */
  public int guardaLinDesp(int ejeLot,int empLot,String serLot,int numLot,int nInd,
                    int numDesp,int proCodi,double kilos,int numPie,
                    String feccad, int defOrden,int uniCaj,double precCost,
                    int defCerra) throws Exception
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
 public int getAccNume()
 {
     return accNume;
 }
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
  * Eso influye solo para  costos, si el despice no es de costo, ignorara el costo.
  * Por defecto se buscara el ultimo despiece de cualquier tipo
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
   * Devuelve una cadena con el codigo de barras para el grafico.
   * @param ejeLot
   * @param empLot
   * @param serLot
   * @param proLote
   * @param proCodi
   * @param proNumind
   * @param deoKilos
   * @return 
   */
  public static String getCodBarras(String ejeLot,int empLot,String serLot,int proLote,int proCodi,
          int proNumind,double deoKilos)
  {
      return ejeLot.substring(2)
                + (proLote > 9999 ? Formatear.format(empLot, "9")
                : Formatear.format(empLot, "99"))
                + serLot
                + (proLote > 9999 ? Formatear.format(proLote, "99999")
                : Formatear.format(proLote, "9999"))
                + Formatear.format(proCodi, "99999")
                + Formatear.format(proNumind, "999")
                + Formatear.format(deoKilos, "999.99");
      
  }
 /**
  * Imprime etiqueta
  * @param TIPOETIQ Tipo etiqueta. Segun tabla etiquetas
  * @param dtStat DatosTabla para buscar tipo etiqueta
  * @param proCodi Codigo Articulo
  * @param nombArt Nombre Articulo
  * @param empCodi Empresa
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
  * @throws Exception
  */
 public void imprEtiq(int TIPOETIQ, DatosTabla dtStat,
                      int proCodi, String nombArt, int empCodi, int nuloge, String ejloge,
                      String seloge, int numInd,
                      double kilos, String fecDesp, java.util.Date fecProd, String fecCad,
                      java.util.Date fecSacr,java.util.Date cadProdDate) throws  Exception
    {
        actualConservar(proCodi, dtStat);
        if (etiq == null) {
            etiq = new etiqueta(EU);
        }
        etiq.setTipoEtiq(dtStat, EU.em_cod, TIPOETIQ);
        String codBarras = getCodBarras(ejloge,empCodi,seloge,nuloge,proCodi,numInd,kilos);
      
        etiq.setFechaCongelado(getFechaCongelado(proCodi, fecProd, dtStat));    
        etiq.iniciar(codBarras, ejloge + "/"
                + Formatear.format(empCodi, "9") + "/"
                + seloge + "/"
                + nuloge + "/"
                + numInd,
                "" + proCodi, nombArt,
                nacidoE, cebadoE, despiezadoE,
                ntrazaE, kilos + "Kg",
                conservarE, sacrificadoE,
                (etiq.getFechaCongelado().equals("")?"F.Prod: ":"F.Cong: ") + (fecProd == null ? fecDesp
                : Formatear.getFecha(fecProd, "dd-MM-yyyy")),//"F. Desp: " + fecDesp,
                etiq.getFechaCongelado().equals("")?"Fec.Cad":"Cad.Fresco", 
                fecProd,
                fecCad,etiq.getFechaCongelado().equals("")?fecSacr:null);
        etiq.setDatMatadero(datmat);
        etiq.setDirEmpresa(diremp);
        etiq.setLogotipo(logo);
        
        etiq.listarDefec();
    }


    public int getAcpPainac() {
        return acp_painac;
    }

    public int getMatCodi() {
        return mat_codi;
    }

    public int getSdeCodi() {
        return sde_codi;
    }

    public int getAcpEngpai() {
        return acp_engpai;
    }

    public int getAcpPaisac() {
        return acp_paisac;
    }

    public String getNumCrot() {
        return ntrazaE;
    }

    public String getMatNrgsa() {
        return mat_nrgsa;
    }

    public String getSdeNrgsa() {
        return sde_nrgsa;
    }
    public String getAcpPaisacNomb()
   {
     return acp_paisacNomb;
   }
   public java.util.Date getFecSacrif()
   {
     return fecSacrE;
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
   public java.util.Date getFecCaduc()
   {
        try {
            return Formatear.getDate(feccadE, "dd-MM-yyyy");
        } catch (ParseException ex) {
            return null;
        }
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
    public Date getFecCadPrv() {
        return fecCadPrvE;
    }
     /**
    * Devuelve la fecha de Compra del Proveedor.
    * @return date con fecha Compra
    */
    public Date getFecCompra() {
        return fecCompraE;
    }
     public Date getFecDesp() {
        return fecDespE;
    }
   public Date getFechaProduccion()
   {
       if (fecProdE!=null)
         return fecProdE;
       if (fecDespE!=null)
         return fecDespE;
       return fecCompraE;
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
    * @param repiteInd >0 : Número de Repeticiones permitidas
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
        return cebadoE;
    }
}
