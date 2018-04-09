package gnu.chu.anjelica.almacen;

import gnu.chu.controles.CComboBox;
import gnu.chu.controles.Cgrid;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventana;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * <p>Título: MvtosAlma </p>
 * <p>Descripción: Clase con rutinas para calcular mvtos valorados de almacen
 * @see gnu.chu.anjelica.almacen.conmvpr</p>
 * @see gnu.chu.anjelica.margenes.colizona
 * <p>Copyright: Copyright (c) 2005-2016
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
 * <p>Empresa: miSL</p>
 * @author chuchiP
 * @version 2.0 (2014-05-29)
 */
public class MvtosAlma
{
  private boolean incluyeHora=false; // Incluir hora a la hora de buscar mvtos.
  private boolean swIgnComprasSinValor=false;
  private int numDecimalesCosto=2;
  private boolean swIncSalDep=false;// Incluir Salidas de depositos.
  private boolean swVerSalDep=false;// Ver Salidas de depositos.
  private boolean swIncInvDep=false;// Incluir Inventarios de depositos.
  private boolean swVerInvDep=false;// Ver Inventarios de depositos.
  private boolean swIncAcum=true;
  private int cliCodi=0;
  private boolean swUsaDocumentos=false; // Usa documentos para buscar los mvtos.
  private String tiposVert="";
  private boolean swDesglInd=false; // Indica si los mvtos. se deben desglosar por individuos
  private double incCosto=0;
  private String accesoEmp;
  private Date dateFin;
  private boolean swCompraNoValor,swDespNoValor;
  private boolean swIgnRegul=false; // Ignorar reg. De secciones no suyas
  private boolean swIgnAllRegul=false; // Ignorar TODAS las regularizaciones.
  private boolean swIgnDespFecha=false; // Ignorar despieces de ult.fecha
  boolean swSoloInv=false; // Solo busca registros en inventarios
  boolean incInvFinal=false; // Por defecto no se incluye inventario de ult. fecha
  boolean incInvInicial=false; // Por defecto no se incluye inventario Inicial (solo para tabla mvtos)
  private String msgStock="";
  private int deoCodiLim=0; // Numero despiece a partir del cual no considerar mas para calculos costos (incluido)
  private int sbeCodi; // Subempresa de Cliente.
  private String rutCodi=null; // Ruta de Cliente.
  private int numProd,numProdInv=0; // Numero para el numero de veces q aparece el prod. en el statement
  String feulst=""; // Fecha Ultimo Stock
  private PreparedStatement pStmt=null,pSInv=null;

    public void setpSInv(PreparedStatement pSInv) {
        this.pSInv = pSInv;
    }
  private boolean swVerVenta,swVerCompra,swVerDesEnt,swVerDespSal,swVerRegul;
  private boolean swVerPrecios=true;
  private double costoFijo=-1; 
  double impVenta,impSal=0;
  double kgSal,kgEnt=0,kgVen=0,kgCompra=0,kgRegul=0;
  
  double impEnt=0;
  double impGana,impCompra,impEntDes,impSalDes,impRegul;
  double kgSalDes=0;
  double kgEntDes=0;
  private ventana padre;
  private EntornoUsuario EU;
  private boolean swAjusteCostos=false; 
  private String msgLog="",msgDesp="",msgCompra="";
  private boolean cancelarConsulta=false;
  private double preStk,canStk;
  private int unSal,uniStk,unEnt,unVent,unCompra,unSalDes, unEntDes,unRegul;
  private boolean swValDesp=false; // Ignorar para costos entradas a almacen de despieces con precio = 0
  private boolean swIgnDespIgu=false;
  private int almCodi=0;
  private int proNumind=0;
  private int proLote=0,empCodi=0,ejercLote=0;
  private String serieLote=null;
  private boolean swFecDocumento=false;
  private boolean swSerieX=false; // Incluir Albaranes de Serie X (traspaso entre almacenes)
  private boolean swPermCostosNegat=true; // Permitir costos en negativo al calcular costos.

  private boolean resetCostoStkNeg=true; // Si el stock es negativo, resetea costo.

//  /**
//   * Indica si debe ignorar para costos los despieces cuyo
//   * producto de entrada y salida sean iguales.
//   * Por defecto es false
//   * @param swIgnDespIgu
//   * 
//   */
//  public void setIgnDespIgu(boolean swIgnDespIg)
//  {
//     this.swIgnDespIgu= swIgnDespIg;
//  }
//  public boolean getIgnDespIgu()
//  {
//     return swIgnDespIgu;
//  }
//  /**
//   * Establece si se debe ignorar en los despieces la fecha superior
//   * Para cuando se esta valorando los despieces en si, y no se quiere que se autovaloren.
//   * Actualmente solo usado por el programa de regeneración de costes.
//   * @param swIgnDespFecha
//   */
//  public void setIgnorarDespFecha(boolean swIgnDespFecha)
//  {
//    this.swIgnDespFecha=swIgnDespFecha;
//  }
//  public boolean getIgnorarDespFecha()
//  {
//    return this.swIgnDespFecha;
//  }
  /**
   * Indica si se deben ignorar para costos los mvtos entrada de despiece sin valorar
   * @default false
   * @param valoraDes 
   */
  public void setIgnDespSinValor(boolean valoraDes)
  {
      swValDesp=valoraDes;
  }
  public boolean getIgnDespSinValor()
  {
      return swValDesp;
  }
  /**
   * Incluir serie X. Esta es la serie de albaranes de venta
   * donde estan incluidos los traspasos entre almacenes
   * @param serieX true=si
   */
  public void setIncluyeSerieX(boolean serieX)
  {
      swSerieX=serieX;
  }
  public boolean getIncluyeSerieX()
  {
      return swSerieX;
  }
  public void setEjerLote(int ejercLote)
  {
      this.ejercLote=ejercLote;
  }
  public void setSerieLote(String serieLote)
  {
      if (serieLote==null)
        this.serieLote=null;
      else
      {
          if (serieLote.trim().equals("") )
              this.serieLote=null;
           else
              this.serieLote=serieLote;
      }
  }
  /**
   * Indica si las fechas mandadas para limitar la consulta son las del documento
   * o las del mvto. Por defecto son las del Mvto.
   * En el caso de que se este buscando usando los documentos, la fecha de compra SIEMPRE se hara
   * a traves de la de documento.
   * @param swFecDocumento 
   */
  public void setFechasDocumento(boolean swFecDocumento)
  {
      this.swFecDocumento=swFecDocumento;
  }
  
//  public void setIntervaloVenta(Date fechaInicio,Date fechaFinal, DatosTabla dt) throws SQLException
//  {
//      
//  }
  public void setLote(int lote)
  {
      proLote=lote;
  }
  public int getLote()
  {
      return proLote;
  }
  /**
   * Indica si los mvtos se deben desglosar por individuos.
   * @param desglIndiv 
   */
  public void setDesglIndiv(boolean desglIndiv)
  {
      this.swDesglInd =desglIndiv;
  }
  public void setIndividuo(int individuo)
  {
//      if (individuo>0)
//        setDesglIndiv(true);
      proNumind=individuo;
  }
  public int getIndividuo()
  {
      return proNumind;
  }
  public void setSoloInventario(boolean swSoloInv)
  {
      this.swSoloInv=swSoloInv;
  }
  public void setAlmacen(int almacen)
  {
      almCodi=almacen;
  }
  public int getAlmacen()
  {
      return almCodi;
  }
  
 
  public void setIncrementarCosto(double incrementoCosto)
  {
    incCosto=incrementoCosto;
  }
  public double getIncrementarCosto()
  {
    return incCosto;
  }
  public void setEmpresa(int empresa)
  {
      empCodi=empresa;
  }
  public int getEmpresa()
  {
      return empCodi;
  }
  public int getSbeCodi() {
        return sbeCodi;
  }

  public void setSbeCodi(int sbeCodi) {
        this.sbeCodi = sbeCodi;
  }
  public void setRutCodi(String rutCodi) {
        this.rutCodi = rutCodi;
  }
  /**
   * Indica si debe ignorar vertederos de otras secciones 
   * al buscar los movimientos
   * @param ignRegul 
   */
  public void setIgnoraRegular(boolean ignRegul)
  {
       swIgnRegul=ignRegul;
  }
  public void setIgnoraTodasRegular(boolean ignRegul)
  {
       swIgnAllRegul=ignRegul;
  }
  /**
   * Indica si se buscaran los movimientos desglosandolo. 
   * Es decir buscando en albaranes de venta, compra, desp,  etc, o se buscara en la tabla mvtos.
   * Por defecto se busca en Mvtos
   * @dprecated
   * @return true si se se busca Desglosado (Albaranes/Despieces)
   */
  public boolean getMvtoDesgl()
  {
      return swUsaDocumentos;
  }
  /**
   * Si se manda true la busqueda de mvtos se hara a traves de los albaranes y despieces (Documentos)
   * Si se manda false la busqueda de mvtos. se hara leyendo la tabla d mvtos (lo que es el valor por defecto)
   * @param usaDocum 
   */
  public void setUsaDocumentos(boolean usaDocum)
  {
       this.swUsaDocumentos=usaDocum;
  }
   /**
   * Indica si se buscaran los movimientos desglosandolo. 
   * Es decir buscando en albaranes de venta, compra, desp,  etc, o se buscara en la tabla mvtos.
   * Por defecto se busca en Mvtos  
   * @return true si se se busca Desglosado (Albaranes/Despieces)
   */
  public boolean getUsaDocumentos()
  {
      return swUsaDocumentos;
  }

  /**
   * Si se manda true la busqueda de mvtos se hara a traves de los albaranes y despieces
   * Si se manda false la busqueda de mvtos. se hara leyendo la tabla d mvtos.
   * @deprecated  setUsaDocumentos
   * @see setUsaDocumentos
   * @param mvtoDesgl  true o false
   */
  public void setMvtoDesgl(boolean mvtoDesgl)
  {
      this.swUsaDocumentos=mvtoDesgl;
  }
  /**
   * Usar mvtos(true) o documentos(false) para las consultas. 
   * @see setFechasDocumento() para especificar si las fechas seran limitadas por fecha doc. o fecha Mvto.
   * 
   * @param useMvtos  true o false
   */
  public void setUseMvtos(boolean useMvtos)
  {
      this.swUsaDocumentos=!useMvtos;
  }
  /**
   * Devuelve Sentencia SQL a ejecutar para buscar los movimientos.
   * @param fecIni Fecha Inico . Formato 'dd-MM-yyyy
   * @param fecFin Fecha Final. Formato 'dd-MM-yyyy
   * @param proCodi Producto. Si el producto es -1 pone un interrogante para utilizarlo
   *        como PreparedStatement
   * @return String con la sentencia sql
   * @throws java.text.ParseException
   */
  public String getSqlMvt(String fecIni, String fecFin, int proCodi) throws ParseException
  {
    if (swUsaDocumentos)
        return getSqlMvtAnt(fecIni,fecFin,proCodi);    
    return getSqlMvtNuevo(fecIni,fecFin,proCodi);
  }
  /**
   * Devuelve el inventario de un producto en una fecha determinada. Null si no encuentra registros
   * Lo devuelve en un hasmap donde el campo indice sera 'kilos','importe','unidades' (en ese orden)
   * @param dt DatosTabla
   * @param proCodi Producto
   * @param fecInv Fecha Inventario
   * @return HashMap
   * @throws SQLException 
   */
  public static HashMap<String, Double> getDatosInventario(DatosTabla dt,int proCodi,Date fecInv) throws SQLException
  {
       dt.select("select sum(rgs_kilos) as kilos,sum(rgs_kilos*rgs_prregu) as importe, sum(rgs_canti) as unidades "
        + " FROM v_inventar r  WHERE  r.pro_codi =  "+proCodi+
         " AND r.rgs_fecha::date = '" + Formatear.getFechaDB(fecInv) +"'");
       
       HashMap<String,Double> hm=new HashMap();
       
       
       hm.put("kilos", dt.getDouble("kilos",true));
       hm.put("importe",dt.getDouble("importe",true));
       hm.put("unidades",dt.getDouble("unidades",true));
       return hm;
  }
  /**
   * SQL Para calcular costos (preparestatement) sobre un producto.
   * 
   * @param fecIni Fecha Inicial (Inventario)  Formato: dd-MM-yyyy
   * @return 
   */
  private String getSqlMvtCostos(String fecIni)  {
    return "SELECT 0 as orden,mvt_tipdoc as sel, mvt_tipo as tipmov,  "
        + " mvt_time as fecmov,"
        + "  mvt_serdoc as serie,pro_numlot as  lote,"
        + " mvt_canti as canti,mvt_prec as precio,pro_indlot as numind,"
        + " mvt_cliprv as cliCodi,mvt_numdoc  as numalb,pro_ejelot as ejenume, "
        + " 1 as empcodi,'0' as pro_codori "
        + ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi "
        + ", mvt_unid as unidades,1 as div_codi,alm_codi,mvt_serdoc as avc_serie,mvt_ejedoc as ejedoc "
        +" ,mvt_fecdoc as fecdoc "
        + ", 'N' as avc_depos "
        + " from mvtosalm where "
        + "   mvt_canti <> 0 "
        + " AND pro_codi = ? "
        + " AND mvt_time::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') "
        + " and mvt_time <=  ? "
        + " union all "
        + " select 1 as orden,'RE' as sel,'=' as tipmov,"
        + " r.rgs_fecha as fecmov,"
        + "  r.pro_serie as serie,r.pro_nupar as  lote,"
        + " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "
        + " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"
        + " r.emp_codi  as empcodi,r.pro_codi as pro_codori"
        + ", '' as repCodi,'' as zonCodi,sbe_codi as sbe_codi,'' as rut_codi "
        + ", rgs_canti as unidades, 1 as div_codi,alm_codi,'.' as avc_serie,r.eje_nume as ejedoc "
        + " ,rgs_fecha as fecdoc, "
        + " 'N' as avc_depos "
        + " FROM v_inventar r  WHERE  r.pro_codi = ? "
        + " AND r.rgs_fecha::date = TO_DATE('" + fecIni + "','dd-MM-yyyy') "
        + " ORDER BY 4,1,3 desc"; // FECHA,orden y tipo
  }
  /**
   * 
   * @param fecIni  Formato: dd-MM-yyyy
   * @param fecFin  Formato: dd-MM-yyyy
   * @param proCodi
   * @return
   * @throws ParseException 
   */
  private String getSqlMvtNuevo(String fecIni, String fecFin, int proCodi) throws ParseException
  {
    numProd=0;
    this.dateFin=Formatear.getDate(fecFin,"dd-MM-yyyy");          
    String sql="";
    if (! swSoloInv)
    {
       numProd++;
       sql+="SELECT 0 as orden,mvt_tipdoc as sel, mvt_tipo as tipmov,  "+
            " mvt_time as fecmov,"+
            "  pro_serlot as serie,pro_numlot as  lote,"+
            " mvt_canti as canti,mvt_prec as precio,pro_indlot as numind,"+
            " mvt_cliprv as cliCodi,mvt_numdoc  as numalb,pro_ejelot as ejenume, "+           
            " 1 as empcodi,pro_codi as pro_codori "+
            ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi, "+
            " mvt_unid as unidades,1 as div_codi,alm_codi,mvt_serdoc as avc_serie,mvt_ejedoc as ejedoc, "+
            " mvt_fecdoc as fecdoc "+
            ", 'N' as avc_depos,mvt_prenet as precioneto "+
             " from mvtosalm where "+
             "  mvt_canti <> 0 "+           
            (almCodi==0?"":" and alm_codi = "+almCodi)+
            (ejercLote==0?"":" and pro_ejelot = "+ejercLote)+
            (serieLote==null?"":" and pro_serlot = '"+serieLote+"'")+
            (proLote==0?"":" and pro_numlot  = "+proLote)+
            (proNumind==0?"":" and pro_indlot = "+proNumind)+
            (!swSerieX?" and mvt_tipdoc !='X'":"")+
            (proCodi==0?"":" AND pro_codi = " + (proCodi==-1?"?":proCodi)) +
            (swFecDocumento? (" AND mvt_fecdoc >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+           
            " and mvt_fecdoc <= TO_DATE('"+fecFin+"','dd-MM-yyyy')"):
            " AND mvt_time::date >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+           
            " and mvt_time::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy"+
             (incluyeHora?"hh:mm:ss":""+"') ")); 
    }
    
    
    if (swIncSalDep && ! swSoloInv)
    {
        numProd++; 
        sql+= (sql.equals("")?"":" UNION ALL ")+
            "SELECT 1 as orden,'P' as sel, '-' as tipmov,  "+
            " avs_fecha as fecmov,"+
            "  avs_serlot as serie,avs_numpar as  lote,"+
            " avs_canti as canti,0 as precio,avs_numind as numind,"+
            " cli_codi as cliCodi,avc_nume  as numalb,avs_ejelot as ejenume, "+           
            " 1 as empcodi,pro_codi as pro_codori "+
            ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi, "+
            " avs_numuni as unidades,1 as div_codi, 1 as alm_codi,avc_serie as avc_serie,"
            + "avc_ano as ejedoc, "+
            " avs_fecha as fecdoc "+
            ", 'N' as avc_depos,0 as precioneto "+
             " from v_albvenserv where avs_fecha >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
            " and avs_fecha <= TO_DATE('"+fecFin+"','dd-MM-yyyy') "+
            (ejercLote==0?"":" and avs_ejelot = "+ejercLote)+
            (serieLote==null?"":" and avs_serlot = '"+serieLote+"'")+
            (proLote==0?"":" and avs_numpar  = "+proLote)+
            (proNumind==0?"":" and avs_numind = "+proNumind)+
            (proCodi==0?"":" AND pro_codi = " + (proCodi==-1?"?":proCodi)) ;
    }
    if (swIncInvDep)
    {
        numProd++; 
        sql+= (sql.equals("")?"":" UNION ALL ")+
            " select 3 as orden,'i' as sel,'=' as tipmov,"
             + "r.ind_fecha as fecmov,"+
           "  r.pro_serie as serie,r.pro_nupar as  lote,"+
           " r.ind_kilos as canti,0 as precio,r.pro_numind as numind, "+
           " 0 as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
           " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
           ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi "+
           ", ind_numuni as unidades, 1 as div_codi,alm_codi,'.' as avc_serie,0 as ejedoc"+
           " ,r.ind_fecha as fecdoc, "+
           " 'N' as avc_depos,0 as precioneto  "+
           " FROM invdepos r  WHERE "+         
           " ind_kilos <> 0 "+
//            " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
           (almCodi==0?"":" and alm_codi = "+almCodi)+
           (ejercLote==0?"":" and r.eje_nume = "+ejercLote)+
           (serieLote==null?"":" and r.pro_serie = '"+serieLote+"'")+
           (proLote==0?"":" and r.pro_nupar  = "+proLote)+
           (proNumind==0?"":" and r.pro_numind = "+proNumind)+
           (empCodi==0?"":" and r.emp_codi = "+empCodi)+
           (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
           (proCodi==0?"":" AND r.pro_codi = " + (proCodi==-1?"?":proCodi))  +
            (swSoloInv?" AND r.ind_fecha = TO_DATE('" + fecIni + "','dd-MM-yyyy') ":
            " AND r.ind_fecha >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
            " and r.ind_fecha <"+(incInvFinal?"=":"")+
            " TO_DATE('"+fecFin+"','dd-MM-yyyy') ");
    }
    if (! incInvFinal || swSoloInv || incInvInicial)
    { 
         numProd++; 
         sql+= (sql.equals("")?"":" UNION ALL ")+
           " select 2 as orden,'RE' as sel,'=' as tipmov,"
             + "r.rgs_fecha as fecmov,"+
           "  r.pro_serie as serie,r.pro_nupar as  lote,"+
           " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
           " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
           " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
           ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi "+
           ", rgs_canti as unidades, 1 as div_codi,alm_codi,'.' as avc_serie,0 as ejedoc"+
           " ,r.rgs_fecha as fecdoc, "+
           " 'N' as avc_depos,rgs_prregu as precioneto  "+
           " FROM v_inventar r  WHERE "+         
           " rgs_kilos <> 0 "+
//            " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
           (almCodi==0?"":" and alm_codi = "+almCodi)+
           (ejercLote==0?"":" and r.eje_nume = "+ejercLote)+
           (serieLote==null?"":" and r.pro_serie = '"+serieLote+"'")+
           (proLote==0?"":" and r.pro_nupar  = "+proLote)+
           (proNumind==0?"":" and r.pro_numind = "+proNumind)+
           (empCodi==0?"":" and r.emp_codi = "+empCodi)+
           (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
           (proCodi==0?"":" AND r.pro_codi = " + (proCodi==-1?"?":proCodi))  +
            (swSoloInv?" AND r.rgs_fecha::date = TO_DATE('" + fecIni + "','dd-MM-yyyy') ":
            " AND r.rgs_fecha::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
            " and r.rgs_fecha::date <"+(incInvFinal?"=":"")+
            " TO_DATE('"+fecFin+"','dd-MM-yyyy') ");
    }
    sql+= " ORDER BY 4,3,1 desc"; // FECHA,  tipo
    return sql;      
  }
  /**
   * Devuelve la SQL para buscar los mvtos, buscando en los docuemtos.
   * @param fecIni. Formato: dd-MM-yyyy
   * @param fecFin Formato: dd-MM-yyyy
   * @param proCodi
   * @return
   * @throws ParseException 
   */
  private String getSqlMvtAnt(String fecIni, String fecFin, int proCodi) throws ParseException
  {
    numProd=1;
    this.dateFin=Formatear.getDate(fecFin,"dd-MM-yyyy");
    

    numProd=5;
    String sql="";
    if (! swSoloInv)
    {
      if (! swDesglInd && proNumind==0)
       sql+="SELECT 1 as orden,'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
        " c.acc_nume as  lote,"+
        " l.acl_canti as canti,l.acl_prcom as precio,0 as numind,"+
        " c.prv_codi as cliCodi,  c.acc_nume as numalb, "+
        " c.acc_ano as ejeNume,c.emp_codi as empCodi,l.pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,c.sbe_codi,'' as rut_codi, acl_numcaj as unidades,1 as div_codi, "+
        " l.alm_codi,'.' as avc_serie,c.acc_ano as ejedoc "+
        ", c.acc_fecrec as fecdoc "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " FROM v_albacoc c,v_albacol l " + //,v_albcompar i "+
        " where c.emp_codi = l.emp_codi "+
        " AND c.acc_serie = l.acc_serie "+
        " AND c.acc_nume = l.acc_nume "+
        " and c.acc_ano = l.acc_ano "+
        " and l.acl_canti <> 0 "+
        (empCodi==0?"":" and c.emp_codi = "+empCodi)+
        (almCodi==0?"":" and l.alm_codi = "+almCodi)+
        (proLote==0?"":" and c.acc_nume = "+proLote)+
        (ejercLote==0?"":" and c.acc_ano = "+ejercLote)+
        (serieLote==null?"":" and c.acc_serie = '"+serieLote+"'")+
        (proCodi==0?"":" AND l.pro_codi = "+(proCodi==-1?"?":proCodi)) +
        " AND c.acc_fecrec >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
        " and c.acc_fecrec <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
      else
         sql+="SELECT 1 as orden,'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
        " c.acc_nume as  lote,"+
        " c.acp_canti as canti,c.acl_prcom as precio,acp_numind as numind,"+
        " c.prv_codi as cliCodi,  c.acc_nume as numalb, "+
        " c.acc_ano as ejeNume,c.emp_codi as empCodi,c.pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,sbe_codi,'' as rut_codi,acp_canind as unidades,1 as div_codi, "+
        " c.alm_codi,'.' as avc_serie,c.acc_ano as ejedoc "+
        ", c.acc_fecrec as fecdoc "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " FROM v_compras as c "+
        " where c.acp_canti <> 0 "+
        (proNumind==0?"":" and acp_numind = "+proNumind)+
        (empCodi==0?"":" and c.emp_codi = "+empCodi)+
        (almCodi==0?"":" and c.alm_codi = "+almCodi)+
        (proLote==0?"":" and c.acc_nume = "+proLote)+
        (ejercLote==0?"":" and c.acc_ano = "+ejercLote)+
        (serieLote==null?"":" and c.acc_serie = '"+serieLote+"'")+
        (proCodi==0?"":" AND c.pro_codi = "+(proCodi==-1?"?":proCodi))+
        " AND c.acc_fecrec >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
        " and c.acc_fecrec <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
    sql+=" UNION all"; // Albaranes de Venta
    String condAlb=  " where avl_canti <> 0 "+
          (proLote==0?"":" and avp_numpar  = "+proLote)+
          (proNumind==0?"":" and avp_numind = "+proNumind)+
          (ejercLote==0?"":" and c.avp_ejelot = "+ejercLote)+
          (serieLote==null?"":" and c.avp_serlot = '"+serieLote+"'")+
          (empCodi==0?"":" and c.emp_codi = "+empCodi)+
          (proCodi==0?"":" AND pro_codi = "+(proCodi==-1?"?":proCodi)) +
          (swFecDocumento?
                 " AND avc_fecalb >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
                 " and avc_fecalb <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ":    
                 " AND avl_fecalt::date >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
                 " and avl_fecalt::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ");

    sql+= " select 2 as orden,'VE' as sel,'-' as tipmov,c.avl_fecalt as fecmov,"+
        "  avp_serlot  as serie,avp_numpar as  lote,"+
        " avp_canti  as canti,avl_prbase as precio,avp_numind as numind,"+
        " c.cli_codi as cliCodi,avc_nume as numalb, "+
        " avp_ejelot as ejeNume,avp_emplot as empcodi,pro_codi as pro_codori, "+
        " rep_codi as repCodi,zon_codi as zonCodi "+
        ",c.sbe_codi,'' as rut_codi, avp_numuni as unidades, c.div_codi,c.alm_codori as alm_codi,"
        + "avc_serie,avc_ano as ejedoc "+
        ", avc_fecalb as fecdoc "+
        ", alm_codori, alm_coddes,avc_depos "+
        "  from  v_albventa_detalle as c "+
        " left join  clientes as cl on cl.cli_codi = c.cli_codi "+
         condAlb+
         (almCodi!=0 && swSerieX?" AND (alm_codori="+almCodi+" or alm_coddes="+almCodi+")":"")+
         (almCodi!=0 && ! swSerieX ?" and c.alm_coddes = "+almCodi: "")+
        // Si no se deben incluir los traspasos, quito los albaranes de serie X
        (! swSerieX?" and c.avc_serie != 'X'":"");

      sql+=" UNION all "+ // Cabecera de despieces (Salidas de almacen)
        " select 2 as orden,'DS' as sel,"+
        " '-' as tipmov ,"+
        " deo_tiempo as fecmov,"+
        "  deo_serlot as serie,pro_lote as  lote,"+
        " deo_kilos as canti,deo_prcost as precio,pro_numind as numind,"+
        " 0 as cliCodi,deo_codi as numalb,deo_ejelot as ejeNume," +
        " deo_emplot as empcodi,pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi  "+
        ", 1 as unidades,1 as div_codi,deo_almori as alm_codi,'.' as avc_serie,eje_nume as ejedoc "+
        ", deo_fecha as fecdoc "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " from  v_despori where "+        
        "  deo_kilos <> 0 "+ 
        (proCodi==0?"":" and pro_codi = " + (proCodi==-1?"?":proCodi))  +
        (almCodi==0?"":" and deo_almori = "+almCodi)+
        (proLote==0?"":" and pro_lote  = "+proLote)+
        (proNumind==0?"":" and pro_numind = "+proNumind)+
        (ejercLote==0?"":" and deo_ejelot = "+ejercLote)+
        (serieLote==null?"":" and deo_serlot = '"+serieLote+"'")+
        (empCodi==0?"":" and emp_codi = "+empCodi)+        
        (accesoEmp==null || empCodi!=0?"":" and emp_codi in ("+accesoEmp+")")+
        " and "+(swFecDocumento?"  deo_fecha  ":
          "  deo_tiempo::date  ")+
          " between TO_DATE('" + fecIni + "','dd-MM-yyyy') and TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
      sql+=" UNION all "+ // Salida de Despieces (Entradas a almacen)
       " select 1 as orden,'DE' as sel, '+' as tipmov,l.def_tiempo as fecmov,"+
       "  l.def_serlot as serie,l.pro_lote as  lote,"+
       " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind,"+
       " 0 as cliCodi,l.deo_codi  as numalb,l.def_ejelot as ejenume, "+
       " l.def_emplot as empcodi,l.pro_codi as pro_codori "+
       ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi "+
       ", l.def_numpie as unidades,1 as div_codi,alm_codi,'.' as avc_serie,c.eje_nume as ejedoc "+
       ", deo_fecha as fecdoc "+
       ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
       " from  desporig c,v_despfin l where "+
       "  c.eje_nume = l.eje_nume "+
       " and c.deo_codi = l.deo_codi "+
       " and l.def_kilos <> 0 "+
       (almCodi==0?"":" and alm_codi = "+almCodi)+
       (proLote==0?"":" and l.pro_lote  = "+proLote)+
       (proNumind==0?"":" and l.pro_numind = "+proNumind)+
       (ejercLote==0?"":" and def_ejelot = "+ejercLote)+
       (serieLote==null?"":" and def_serlot = '"+serieLote+"'")+
//       (empCodi==0?"":" and c.emp_codi = "+empCodi)+
       (proCodi==0?"":" AND l.pro_codi = " + (proCodi==-1?"?":proCodi))  +
        " and "+(swFecDocumento?"  deo_fecha ":
          " l.def_tiempo::date ")+
       " between TO_DATE('" + fecIni + "','dd-MM-yyyy') and TO_DATE('"+fecFin+"','dd-MM-yyyy') ";   
     sql+=" UNION all "+ // Regularizaciones.
        " select 1 as orden,'RE' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"+
        "  r.pro_serie as serie,r.pro_nupar as  lote,"+
        " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
        " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
        " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
        ", rep_codi as repCodi,zon_codi as zonCodi,r.sbe_codi as sbe_codi, cl.rut_codi as rut_codi "+
        ", rgs_canti as unidades, 1 as div_codi,alm_codi,'.' as avc_serie,r.eje_nume as ejedoc "+
        ", r.rgs_fecha as fecdoc "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " FROM v_regstock r left join  clientes as cl on cl.cli_codi = r.rgs_cliprv "        
        + "WHERE "+       
        " rgs_kilos <> 0 "+
        ( incInvFinal?"": " and tir_afestk != '='")+ // Sin Incluir Inventarios      
 //        " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
        (almCodi==0?"":" and alm_codi = "+almCodi)+
        (proLote==0?"":" and r.pro_nupar  = "+proLote)+
        (proNumind==0?"":" and r.pro_numind = "+proNumind)+
        (ejercLote==0?"":" and eje_nume = "+ejercLote)+
        (serieLote==null?"":" and pro_serie = '"+serieLote+"'")+
        (empCodi==0?"":" and r.emp_codi = "+empCodi)+
        (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
        (proCodi==0?"":" AND r.pro_codi = " + (proCodi==-1?"?":proCodi))  +
         " AND r.rgs_fecha::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
         " and r.rgs_fecha::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";

    }
    if (! incInvFinal || swSoloInv )
    { // No incluir inventario final.
         numProd++;
         if (! swSoloInv)
             sql+=" UNION all "; // Regularizaciones Inc. Inventario
         else
         {
            numProd=1;
            sql="";
         }
         sql+=" select 1 as orden,'RE' as sel,'=' as tipmov,r.rgs_fecha as fecmov,"+
           "  r.pro_serie as serie,r.pro_nupar as  lote,"+
           " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
           " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
           " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
           ", '' as repCodi,'' as zonCodi,0 as sbe_codi,'' as rut_codi "+
           ", rgs_canti as unidades, 1 as div_codi,alm_codi,'.' as avc_serie,r.eje_nume as ejedoc "+
           ", r.rgs_fecha as fecdoc "+
           ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
           " FROM v_inventar r WHERE "+        
//           " tir_afestk = '='"+ // Solo Inventarios
           "  rgs_kilos <> 0 "+
//            " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
           (almCodi==0?"":" and alm_codi = "+almCodi)+
           (proLote==0?"":" and r.pro_nupar  = "+proLote)+
           (proNumind==0?"":" and r.pro_numind = "+proNumind)+
           (ejercLote==0?"":" and eje_nume = "+ejercLote)+
           (serieLote==null?"":" and pro_serie = '"+serieLote+"'")+
           (empCodi==0?"":" and r.emp_codi = "+empCodi)+
           (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
           (proCodi==0?"":" AND r.pro_codi = " + (proCodi==-1?"?":proCodi))  +
            (swSoloInv?" AND r.rgs_fecha::date = TO_DATE('" + fecIni + "','dd-MM-yyyy') ":
            " AND r.rgs_fecha::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
            " and r.rgs_fecha::date <"+(incInvFinal?"=":"")+
            " TO_DATE('"+fecFin+"','dd-MM-yyyy') ");
    }
    sql+= " ORDER BY 4,1,3 desc"; // FECHA,orden y tipo
    return sql;
  }
  /**
   * Incluir fecha inventario final
   * Por defecto es false
   * @param incInvFinal
   */
  public void setIncUltFechaInv(boolean incInvFinal)
  {
      this.incInvFinal=incInvFinal;
  }
  /**
   * Incluir Salidas de albaranes de deposito (Defecto: false)
   * @param incSalidaDep 
   */
  public void setIncSalidaDep(boolean incSalidaDep)
  {
      swIncSalDep=incSalidaDep;
  }
  /**
   * Especifica si se deben ver las salidas  de deposito (Defecto: false)
   * @param verSalidaDep 
   */
  public void setVerSalidaDep(boolean verSalidaDep)
  {
      swVerSalDep=verSalidaDep;
  }
  /**
   * Incluir inventarios de deposito(Defecto: false)
   * @param incInventDep 
   */
  public void setIncInventDep(boolean incInventDep)
  {
      swIncInvDep=incInventDep;
  }
  /**
   * Especifica si se deben ver los Inventarios   de deposito (Defecto: false)
   * @param verInventDep 
   */
  public void setVerInventDep(boolean verInventDep)
  {
      swVerInvDep=verInventDep;
  }
  /**
   * Incluir Inventarios Inicial. Por defecto es false.
   * @param incInvInicial 
   */
  public void setIncIniFechaInv(boolean incInvInicial)
  {
      this.incInvInicial=incInvInicial;
  }
  /**
   * Busca Acumulados mvtos. 
   * El importe calculado es el Neto (con dto pp, etc).Columna mvt_prenet
   * @param fecIni
   * @param fecFin
   * @param almCodi
   * @param proCodi
   * @param proLote
   * @param dt
   * @return
   * @throws SQLException
   * @throws ParseException 
   */
  public boolean getAcumuladosMvtos( String fecIni, String fecFin,
            int almCodi,int proCodi,int proLote, DatosTabla dt) throws SQLException,ParseException
  {
       resetAcumulados();
       resetAcumMvtos();
       setLote(proLote);
       String s = "SELECT mvt_tipdoc,mvt_tipo, sum(mvt_canti) as canti,sum(mvt_unid) as unid, "+
            " sum(mvt_canti* mvt_prenet)  as importe "+
             " from mvtosalm where "+
             "   mvt_canti <> 0 "+
             " and mvt_serdoc!='X' "+
            (almCodi==0?"":" and alm_codi = "+almCodi)+
            (proLote==0?"":" and pro_numlot  = "+proLote)+            
            " AND pro_codi = " + (proCodi==-1?"?":proCodi) +
            " AND mvt_time::date >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
            " and mvt_time::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') "+
            " group by mvt_tipdoc,mvt_tipo";
       if (!dt.select(s))
           return false;
       double canti,unid,importe;
       boolean tipEnt;
       do
       {
           canti=dt.getDouble("canti",true);
           unid=dt.getDouble("unid",true);
           importe = dt.getDouble("importe",true);
           tipEnt = dt.getString("mvt_tipo").equals("E");
           if (tipEnt)
           {
               kgEnt+=canti;
               unEnt+=unid;
               impEnt=importe;
           }
           else
           {
               kgSal+=canti;
               unSal+=unid;
               impSal+=importe;
           }
           switch (dt.getString("mvt_tipdoc"))
           {
               case "C":
                   kgCompra+=canti;
                   unCompra+=unid;
                   impCompra+=importe;
                   break;
               case "V":
                   kgVen+=canti;
                   unVent+=unid;
                   impVenta+=importe; 
                   break;
                case "d":  
                   kgEntDes+=canti;
                   unEntDes+=unid;
                   impEntDes+=importe;
                   break;
                case "D":
                   kgSalDes+=canti;
                   unSalDes+=unid;
                   impSalDes+=importe;
                   break;
                 case "R":
                   kgRegul+=tipEnt?canti:canti*-1;
                   unRegul+=tipEnt?unid:unid*-1;
                   impRegul+=tipEnt?importe:importe*-1;
                   break;
           }
       } while (dt.next());
       return !(kgEnt==0 && kgSal==0);
  }
  
    public boolean isIncluyeHora() {
        return incluyeHora;
    }

    public void setIncluyeHora(boolean incluyeHora) {
        this.incluyeHora = incluyeHora;
    }
    /**
     * Establece si las compras con precio 0 se deben ignorar para costos. 
     * Por defecto es false.
     * @param ignComprasSinValor 
     */
    public void setIgnComprasSinValor(boolean ignComprasSinValor)
    {
        swIgnComprasSinValor=ignComprasSinValor;
    }
    /**
     * Devuelve si las compras con precio 0 se deben ignorar para costos. 
     * 
     * @return 
     */
    public boolean getIgnComprasSinValor()
    {
        return swIgnComprasSinValor;
    }
    public void setNumeroDeciamlesCosto(int numDecimal)
    {
        numDecimalesCosto=numDecimal;
    }
  /**
   * Define la cadena para limitar accesos a las empresas.
   * @param acessoEmp
   * 
   */
  public void setAccesoEmp(String acessoEmp)
  {
      this.accesoEmp=acessoEmp;
  }
  /**
   * Devuelve el costo de un producto en una fecha.
   * @param proCodi
   * @param timeMvt
   * @param dtCon1
   * @param dtStat
   * @return
   * @throws SQLException
   * @throws java.text.ParseException 
   */
  public boolean getCostoRefInFecha(int proCodi,Timestamp timeMvt,DatosTabla dtCon1, DatosTabla dtStat) throws SQLException, java.text.ParseException      
  {
      resetAcumulados();
      pStmt.setInt(1, proCodi);
      pStmt.setTimestamp(2, timeMvt);
      pStmt.setInt(3, proCodi);
      ResultSet rs=pStmt.executeQuery();
       if (!rs.next())
            return false;
      return calculaMvtos(rs,dtCon1, dtStat,null,null,null,0);
  }
  /**
   * 
   * @param feulin Fecha Ultimo Inventario
   * @param fecIni Fecha Inicio Periodo
   * @param fecFin Fecha Final Periodo
   * @param proCodi Producto
   * @param zonCodi Zona . Null si no se restringe por ninguna
   * @param repCodi  Representante Null si no se restringe por ninguna.
   * @param proLote Lote
   * @param dtCon1  DatosTabla principal
   * @param dtStat  DatosTabla Auxilar (no se usa si jt == null)
   * @param jt Cgrid donde poner los datos de lineas.
   * @return 
   * @throws java.sql.SQLException
   * @throws java.text.ParseException
   */
    public boolean calculaMvtos(String feulin, String fecIni, String fecFin,
            int proCodi, String zonCodi, String repCodi, int proLote,
            DatosTabla dtCon1, DatosTabla dtStat, Cgrid jt) throws SQLException, java.text.ParseException {
        resetAcumulados();
        String fefi;
        String s;

        fefi = Formatear.sumaDias(fecIni, "dd-MM-yyyy", -1); // Le quito un dia a la fecha Inicial
        if (Formatear.comparaFechas(Formatear.getDate(feulin, "dd-MM-yyyy"),
                Formatear.getDate(fecIni, "dd-MM-yyyy")) < 0) {
            s = getSqlMvt(feulin, fefi, proCodi);
            calculaMvtos(dtCon1, dtStat, s, null, null, null, proCodi);
        }
        // resetAcumulados();
        if (costoFijo>0)
            preStk=costoFijo;
        setLote(proLote);
        s = getSqlMvt(fecIni, fecFin, proCodi);

        return calculaMvtos(dtCon1, dtStat, s, jt, zonCodi, repCodi, proCodi);
    }
    public void iniciarMvtos(Date fecInv, Date fecIni, Date fecFin,
            DatosTabla dtCon1) throws SQLException, ParseException
    {
        if (fecInv==null)
        {// Busca Fecha Ult. Inventario anterior a la fecha Inicial
            fecInv=ActualStkPart.getDateUltInv(fecIni,dtCon1);
        }
        iniciarMvtos(Formatear.getFecha(fecInv, "dd-MM-yyyy"),Formatear.getFecha(fecIni, "dd-MM-yyyy"),
            Formatear.getFecha(fecFin, "dd-MM-yyyy"),
            dtCon1);
    }
    /**
     * Iniciar movimientos.
     * Funcion  a llamar cuando se van a calcular Movimientos sobre diferentes
     * productos pero con las mismas condiciones.
     * LLamar despues a
     *  calculaMvtos(int proCodi,DatosTabla dtCon1,DatosTabla dtStat,String zonCodi, String repCodi)
     * @deprecated usar iniciarMvtos(Date, Date, DatosTabla)
     * @param fecInv Fecha Inventario (Formato dd-MM-yyyy)
     * @param fecIni Fevcha Inicial (Formato dd-MM-yyyy)
     * @param fecFin Fecha Final ((Formato dd-MM-yyyy))
     * @param dtCon1 DatosTabla principal
     * @throws SQLException
     * @throws ParseException
     */
    void iniciarMvtos(String fecInv, String fecIni, String fecFin,
            DatosTabla dtCon1) throws SQLException, ParseException {

        String fefi;
        boolean soloInv=swSoloInv;
        boolean incUltFecInv=incInvFinal;
        tiposVert=pdmotregu.getTiposRegul(dtCon1,"V%");
        if (fecInv.equals(fecIni))
        {
            pSInv=null;
        }
        else
        {           
            fefi = Formatear.sumaDias(fecIni, "dd-MM-yyyy", swFecDocumento?0:-1);
            if (fecInv.equals(fefi))
            {
                setIncUltFechaInv(true);
                setSoloInventario(true);
            }
            String s = getSqlMvt(fecInv, fefi, -1);
           
            numProdInv=numProd;
            pSInv = dtCon1.getPreparedStatement(s);
        }
       
        setSoloInventario(soloInv);
        setIncUltFechaInv(incUltFecInv);
//        setIncUltFechaInv(incInv);
        iniciarMvtos(fecIni, fecFin, dtCon1);
    }

  /**
   * Funcion  a llamar cuando se van a calcular Movimientos sobre diferentes 
   * productos pero con las mismas condiciones.
   * LLamar despues a calculaMvtos(int proCodi,DatosTabla dtCon1,DatosTabla dtStat,String zonCodi, String repCodi)
   * @param fecIni Fecha ultimo inventario (o de Inicio)
   * @param fecFin Fecha en la que calcular costo y kilos
   * @param dtCon1 DatosTabla principal
   * @throws SQLException
   * @throws java.text.ParseException
   */
    public void iniciarMvtos(String fecIni,String fecFin,   DatosTabla dtCon1) throws SQLException,ParseException {
        String s = getSqlMvt(fecIni, fecFin, -1);
        pStmt=dtCon1.getPreparedStatement(s);
    }
    public void iniciarMvtos(Date fecIni,Date fecFin,   DatosTabla dtCon1) throws SQLException,ParseException {
        String s = getSqlMvt(Formatear.getFecha(fecIni, "dd-MM-yyyy"), 
            Formatear.getFecha(fecFin,"dd-MM-yyyy"), -1);
        pStmt=dtCon1.getPreparedStatement(s);
    }

    /**
     * Inicia consulta sobre mvtos, usado para calcular costo en una fecha determinada.
     * @param fecIni String con formato dd-MM-yyyy
     * @param dtCon1
     * @throws SQLException
     * @throws ParseException 
     */
    public void iniciarMvtos(String fecIni,  DatosTabla dtCon1) throws SQLException,ParseException {
        String s = getSqlMvtCostos(fecIni);
        pStmt=dtCon1.getPreparedStatement(s);
    }
    /**
     * Establece el cliente sobre el que se deben calcular las ganancias.
     * 
     * @param cliCodi 
     */
    public void setCliente(int cliCodi)
    {
        this.cliCodi=cliCodi;
    }
    /**
     * Debera haberse llamado primero a la funcion iniciarMvtos
     * @param dtCon1
     * @param dtStat
     * @param zonCodi
     * @param repCodi
     * @return  true si encuentra mvtos. False en caso contrario
     * @see iniciarMvtos(String fecini,String fecfin, DatosTabla dt)
     * @param proCodi Producto
     * @throws SQLException
     */
    public boolean calculaMvtos(int proCodi,DatosTabla dtCon1,DatosTabla dtStat,String zonCodi, String repCodi) throws SQLException
    {
        if (pSInv!=null)
        {
          if (numProdInv==0)
              numProdInv=numProd;
          for (int n=1;n<=numProdInv;n++)
          {
                pSInv.setInt(n, proCodi);
          }
          resetAcumulados();
          ResultSet rs=pSInv.executeQuery();
          swIncAcum=false;
          if (rs.next())
            calculaMvtos(rs,dtCon1,dtStat,null,null,null,proCodi);
          feulst=null;
          swIncAcum=true;
        }
        else
            resetAcumulados();
        for (int n=1;n<=numProd;n++)
        {
            pStmt.setInt(n, proCodi);
        }
        ResultSet rs=pStmt.executeQuery();
        if (!rs.next())
            return false;
        return calculaMvtos(rs,dtCon1,dtStat,null,zonCodi,repCodi,proCodi);
    }
  public boolean calculaMvtos(DatosTabla dt,DatosTabla dtStat,String s,Cgrid jt,String zonCodi,
       String repCodi,int proCodi) throws SQLException
  {
    if (! dt.select(s))
        return false;

    calculaMvtos(null,dt,dtStat,jt,zonCodi,repCodi,proCodi);
    return true;
  }
  /**
   * Devuelve variable que indica si ha habido compras sin valorar. O sea con precio = 0
   * @return true si hay compras con costo 0
   */
  public boolean getCompraSinValor()
  {
      return swCompraNoValor;
  }
  /**
   * Devuelve variable que indica si ha habido despieces sin valorar. O sea con costo = 0
   * @return true si hay despieces con costo 0
   */
  public boolean getDespiecesSinValor()
  {
      return swDespNoValor;
  }
  /**
   * Devuelve variable si ha habido entradas de despieces o compras sin valorar (no incluye las regularizaciones)
   * 
   * @return true si hay entradas sin valorar
   */
  public boolean getEntradaSinValor()
  {
     return swDespNoValor ||  swCompraNoValor;
  }
  public void setDespieceLimite(int despLim)
  {
      this.deoCodiLim=despLim;
  }
  public int getDespieceLimite()
  {
      return this.deoCodiLim;
  }
 
/**
 * Calcula Mvtos sobre la select puesta en la variable 's'
 * Actualiza las variables preStk (@see getPrecioStock)
 * canStk (@see getKilosStock) y  uniStk  (@see getUnidStock)
 * @param rs ResultSet para usar en datosTabla
 * @param dt DatosTabla principal. leera los mvtos.
 * @param dtStat DatosTabla Auxiliar
 * @param jt Grid donde meter los datos. Si NULL no se metera nada
 * @param zonCodi Zona Null si no se debe tener en cuenta
 * @param repCodi  Zona/Credito Null si no debe tenerse en cuenta
 * @param proCodi Producto a tratar.
 * @return true si hay datos y no se ha cancelado.
 * @throws java.sql.SQLException
 */
  public boolean calculaMvtos(ResultSet rs,DatosTabla dt,DatosTabla dtStat,Cgrid jt,String zonCodi,
       String repCodi,int proCodi) throws SQLException
  {
//    boolean ignDesp;
    boolean swErr=true;
    String tipMov;
    String cliNomb;
    double precio;
//    double kg;
//    boolean swPas1;
    double canti;
    int unid;
    char sel;
    resetAcumMvtos();   
    boolean swNegat=false;
    boolean swIgnMvto=false;
    boolean swTraspAlm;
    swCompraNoValor=false;
    swDespNoValor=false;
    if (rs!=null)
        dt.setResultSet(rs);

    // Inicializando variables.
    if (jt!=null)
       jt.removeAllDatos();
    swDespNoValor=false;
    swCompraNoValor=false;
    String ref="";
    Double cant;
    double cantiInd=0;
    HashMap<String,Double> ht = new HashMap();
    PreparedStatement psCli=null;
    boolean swDiscr=false;
    String DT_zonCodi="",DT_repCodi="",DT_rutCodi=null;
    int DT_sbeCodi=0;
    if ( (repCodi!=null ||  zonCodi!=null || sbeCodi!=0 || cliCodi!=0 || rutCodi!=null))
    {
        swDiscr=true;
        if (!swUsaDocumentos )
        { // Va por movimientos pero quiero discr. Zona/Repr o Seccion.
            psCli= dtStat.getPreparedStatement("select * from v_cliente where cli_codi = ?");       
        }
    }
    // Comienzo del bucle sobre mvtos o documentos.
    boolean swIgnCosto;
    do
    {
        if (cancelarConsulta)
           return false;
        swTraspAlm=false; // No es traspasos entre almacenes
        swIgnCosto=false;
        canti = 0;
        unid=0;
        precio =preStk;
        tipMov=dt.getString("tipmov");
        if (! swUsaDocumentos)
        {               
            if  (tipMov.equals("E"))
                tipMov="+";
            if  (tipMov.equals("S"))
                tipMov="-";
        }
        
       
        boolean avcSerieX=dt.getString("avc_serie").equals("X");
        sel=dt.getString("sel").charAt(0);
        if (dt.getString("sel").equals("DE"))
          sel='d';
        if (swDesglInd)
        {
            ref = proCodi + "|" + dt.getInt("ejenume", true) + "|"
                             + dt.getInt("empcodi", true) + "|"
                             + dt.getString("serie") + "|"
                             + dt.getInt("lote", true) + "|"
                             + dt.getInt("numind", true);
            cant =  ht.get(ref);
            if (cant==null)
                cantiInd=0;
            else
                cantiInd=cant;
        }
       
        if (tipMov.equals("+"))
        { // Se pone aqui por que al tratar los inventarios se pondra tipmov= '+'
            if (sel=='C')
            {
               if (swIncAcum)
               {
                    kgCompra+=dt.getDouble("canti"); // Ignoramos los Inventarios
                    unCompra+=dt.getInt("unidades");
                    if (dt.getDouble("precio")==0)
                    {
                        if (swIgnComprasSinValor)
                            swIgnCosto=true;
                        msgLog+="Compra con Precio 0 " +
                              ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                        msgCompra+="Precio 0 " +
                              ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                        swCompraNoValor=true;
                    }
                    impCompra+=dt.getDouble("precio")*dt.getDouble("canti");
               }
            }
            if (sel=='V') 
            {
                if ( !avcSerieX && swIncAcum)              
                { // Albaran de Abono
                    kgVen -= dt.getDouble("canti");
                    impVenta -= dt.getDouble("canti", true) * dt.getDouble("precio", true);
                    unVent -= dt.getDouble("unidades");
                }
            }
            
            if (!avcSerieX || almCodi!=0) // No es serie X o trata un solo almacen
            {
                kgEnt+=dt.getDouble("canti");
                unEnt+=dt.getInt("unidades");
                impEnt+=dt.getDouble("precio")*dt.getDouble("canti");
            }
           
        }
        if (sel=='R' && ! tipMov.equals("="))
        {
          if (tipMov.equals("+"))
          {
            if (swIncAcum)
            {
              kgRegul += dt.getDouble("canti",true);
              unRegul +=dt.getInt("unidades",true);
              impRegul+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
            }
          }
          else
          {
              if (swIncAcum)
              {
                kgRegul -= dt.getDouble("canti",true);
                unRegul -=dt.getInt("unidades",true);
                impRegul-=dt.getDouble("canti",true)*dt.getDouble("precio",true);
              }
          }
        }
        if (tipMov.equals("="))
        {
            if (feulst!=null && !feulst.equals(dt.getFecha("fecmov")))
            {
              feulst = dt.getFecha("fecmov");
              precio = dt.getDouble("precio") ;
              canStk=0;
              uniStk=0;
              cantiInd=0;
              canti = dt.getDouble("canti");
              unid= dt.getInt("unidades");
            }
            else
              tipMov = "+";
        }
        
        if (avcSerieX && almCodi==0)
        {// No influye en stock
              canti=canStk;
              precio=preStk;
              unid=uniStk;    
              swIgnMvto=true;
        }
        if (tipMov.equals("+"))
        { // Trato entradas a almacen
//            kg = dt.getDouble("canti");
            cantiInd += dt.getDouble("canti");
//            cj = dt.getInt("unidades");

            if (sel == 'd')
            { // Despiece de salida (Entrada a almacen) 
                if (dt.getDouble("precio") == 0)
                {
                    swDespNoValor=true;
                    if(swValDesp)
                        swIgnCosto = true; // Ignorar costos de despiece
                }
            }
            canti = canStk + dt.getDouble("canti");
            unid = uniStk + dt.getInt("unidades");
            if ( swIgnCosto || avcSerieX  || (swIgnAllRegul && sel == 'R') )
                precio = preStk; // No trato para costos los traspasos entre almacenes y/o ignoro regularizaciones.
            else
            {
                if ((canStk < -0.1 || canti < -0.1) && resetCostoStkNeg)
                { // Cant. anterior y/o actual en Negativo
                    msgLog += "Atencion Stock en Negativo: " + Formatear.redondea(canStk, 2)
                        + ":  Fecha: " + dt.getFecha("fecmov") + " Prod: " + proCodi + " \n";
                    if (swErr == false)
                    {
                        swErr = true;
                        msgBox("Stock en Negativo: " + Formatear.redondea(canStk, 2)
                            + ":  Fecha: " + dt.getFecha("fecmov"));
                    } else
                    {
                        mensajeErr(" Stock en Negativo: " + Formatear.redondea(canStk, 2)
                            + ": EN Fecha: " + dt.getFecha("fecmov"));
                    }
                    precio = dt.getDouble("precio");
                } else
                {
                    precio = (preStk * canStk)
                        + (dt.getDouble("canti") * dt.getDouble("precio"));
                    if ((precio >= 0.01 || precio <= -0.01) && (canti >= 0.01 || canti <= -0.01))
                        precio = precio / canti;
                    else
                        precio = preStk;
                }
                if (!swPermCostosNegat)
                {
                    if (precio < 0)
                        precio = 0;
                }
            }
        }

        if (tipMov.equals("-"))
        { // Es una salida, traspaso entre almacenes o Regularizacion
            if (swDiscr)
            {
                if (psCli != null)
                {
                    psCli.setInt(1, dt.getInt("cliCodi",true));
                    ResultSet rsCli = psCli.executeQuery();
                    if (!rsCli.next())
                    {
                        DT_zonCodi = null;
                        DT_repCodi = null;
                        DT_sbeCodi = 0;
                        DT_rutCodi= null;
                    } else
                    {
                        DT_zonCodi = rsCli.getString("zon_codi").toUpperCase();
                        DT_repCodi = rsCli.getString("rep_codi").toUpperCase();
                        DT_sbeCodi = rsCli.getInt("sbe_codi");
                        DT_rutCodi= rsCli.getString("rut_codi");
                    }
                } else
                {
                    DT_zonCodi = dt.getString("zonCodi", true).toUpperCase();
                    DT_repCodi = dt.getString("repCodi", true).toUpperCase();
                    DT_sbeCodi = dt.getInt("sbe_codi");
                    DT_rutCodi= dt.getString("rut_codi");
                }
            }
            cantiInd -= dt.getDouble("canti");
            swIgnMvto = false;
            if (sel == 'R')
            {
                if ( swIgnAllRegul || (swIgnRegul && swDiscr && DT_zonCodi == null))
                { // Ignorar Regularizaciones                       
                    swIgnMvto = true;
                }
            }
            if (sel == 'V' || sel == 'R' && !swIgnMvto)
            { // Albaran de venta o Regularizacion              
                if (DT_zonCodi != null && zonCodi != null)
                {
                    if (!DT_zonCodi.matches(zonCodi))
                        swIgnMvto = true;
                }
                if (DT_repCodi != null && repCodi != null)
                {
                    if (!DT_repCodi.matches(repCodi))
                        swIgnMvto = true;
                }
                if (DT_sbeCodi != 0 && sbeCodi != 0 && DT_sbeCodi != sbeCodi)
                {
                    swIgnMvto = true;
                }
                if (DT_rutCodi != null && rutCodi != null && ! DT_rutCodi.equals(rutCodi))
                {
                    swIgnMvto = true;
                }
                if (dt.getInt("cliCodi",true) != 0 && cliCodi != 0 && cliCodi != dt.getInt("cliCodi",true))
                {
                    swIgnMvto = true;
                }
            }

            if ((sel == 'V' || sel == 'R') && !swIgnMvto)
            { // Tener solo en cuenta Ventas y Regularizaciones.
                if (sel == 'V' && dt.getInt("div_codi") <= 0 && !isRootAV())
                    isRootAV(); // no lo muestro xq es albaran oculto.
                else
                {
                    if (sel == 'V')
                    {
                        if (swUsaDocumentos)
                        {
                            if (almCodi != 0 && dt.getInt("alm_coddes") != almCodi && dt.getInt("alm_codori") != almCodi)
                                continue; // No lo trato.
                        }
                        if (!avcSerieX)
                        {
                            if (swIncAcum)
                            {
                                kgVen += dt.getDouble("canti");
                                impVenta += dt.getDouble("canti", true) * dt.getDouble("precio", true);
                                unVent += dt.getDouble("unidades");
                                if (preStk == 0)
                                    msgLog += "Venta con precio stock = 0 "
                                        + " EN Fecha: " + dt.getFecha("fecmov") + ": Producto: " + proCodi + " \n";
                                impGana += dt.getDouble("canti", true)
                                    * (dt.getDouble("precio", true) - preStk);
                            }
                        }
                    } else
                    { // Es regularizacion
                        if (!swIgnMvto)
                        {
                            if (swIncAcum)
                                impGana += dt.getDouble("canti", true) * (dt.getDouble("precio", true) - preStk);
                        }
                    }
                }
            }
            if (almCodi != 0 && avcSerieX && swUsaDocumentos && dt.getInt("alm_coddes") == almCodi)
            {
                canti = canStk + dt.getDouble("canti", true);
                unid = uniStk + dt.getInt("unidades");
            } else
            {
                canti = canStk - dt.getDouble("canti", true);
                unid = uniStk - dt.getInt("unidades");
            }
         } // Fin de negativo
        
         if (swDesglInd)
            ht.put(ref, cantiInd);
        preStk=precio;
        canStk=canti;
        uniStk = unid;
        if (canStk<-0.1 && !swNegat)
        {
            if (! tipMov.equals("+") &&  sel !='R' )
            { // Si es mvto. de Inventario no lo considero error.
                msgStock+="Producto: "+proCodi+": Fecha: "+ dt.getFecha("fecmov")+ ": Kilos: "+
                    Formatear.format(canStk,"---,--9.99")+"\n";
                swNegat=true;
            }
        }
        else
          swNegat=false;
        if (sel=='d')
        {
          if (swIncAcum)
          {
            kgEntDes += dt.getDouble("canti",true);
            unEntDes +=dt.getInt("unidades",true);
            impEntDes+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
          }
        }
        if (sel=='D')
        {            
          if (swIncAcum)
          {
            kgSalDes += dt.getDouble("canti",true);
            unSalDes +=dt.getInt("unidades",true);
            impSalDes+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
          }
        }
      
        if (jt!=null && !swIgnMvto)
        {  // Meter datos para cuando la consulta es en pantalla y detallada en un grid
          if (dt.getString("avc_serie").equals("X") && ! swSerieX )
              continue;
          if (sel == 'V'&& ! swVerVenta)
            continue;
          if (sel=='C' && ! swVerCompra)
            continue;
          if (sel=='d' && !swVerDesEnt)
            continue;
          if (sel=='D' && !swVerDespSal)
            continue;
          if (sel=='R' && !swVerRegul)
            continue;
          if (sel=='i' && !swVerInvDep)
            continue; // No ver Inv. Deposito
          if (sel=='P' && !swVerSalDep)
            continue; // No ver salidas de depositos.
          if (sel=='V' && dt.getInt("div_codi")<=0 && ! isRootAV())
            continue;
          
          // Introduce Valores en ArrayList (mostrar en pantalla)
          ArrayList v = new ArrayList();
          v.add(dt.getTimeStamp("fecmov"));
          if (tipMov.equals("+") || tipMov.equals("="))
          {
            if (sel=='D')
               v.add(dt.getDouble("canti") * -1);
            else
              v.add(dt.getString("canti"));
            v.add("");
          }
          else
          { // Salida
            if (swTraspAlm && swUsaDocumentos )
                v.add(dt.getString("canti"));
            else
                v.add("");
            v.add(dt.getString("canti"));
          }
          if (dt.getString("tipmov").equals("="))
          {
              if (sel=='i')
                v.add("ID");
             else
                v.add("IN");
          }
          else
          {
             if(dt.getString("avc_serie").equals("X"))
                 v.add("TA");
             else
                v.add(dt.getString("sel"));
          }
          v.add(!swVerPrecios?"":Formatear.redondea(dt.getDouble("precio"),numDecimalesCosto ));
          v.add(uniStk);
          v.add(canStk);
          v.add(!swVerPrecios?"":Formatear.redondea(preStk+incCosto,numDecimalesCosto) );
          v.add(!swVerPrecios?"":impGana);
          v.add(dt.getString("ejedoc")+"-"+dt.getString("avc_serie")+"-"+dt.getString("numalb"));
          v.add(dt.getDate("fecdoc"));
          cliNomb="";
          if (sel=='V')
          {
             if (dtStat.select("select cli_nomb from clientes"+
                           " where cli_codi="+dt.getInt("cliCodi",true)))
             {
                
                String depos="";
                cliNomb=dtStat.getString("cli_nomb");
                if (dt.getString("avc_depos").equals("D"))
                { // Busco albaran entregado.
                 
                  if ( dtStat.select ("SELECT avs_nume FROM albvenseri "
                        + " WHERE avs_serlot = '" + dt.getString("serie")+ "'"
                        + " and avs_numpar = " + dt.getString("lote")
                        + " and avs_ejelot = " + dt.getString("ejenume")
                        + " and avs_emplot = " + dt.getString("empcodi")
                        + " and avs_numind = " + dt.getString("numind") ))
                    depos="D:"+dtStat.getInt("avs_nume")+" ";
                  else
                    depos="*DEP* ";
                }
                cliNomb = depos+
                     cliNomb;
                
             }
          }
          if (sel=='C'||  sel=='D' || sel=='d' )
          {
            if (dtStat.select("select prv_nomb from v_proveedo"+
                           " where prv_codi="+dt.getInt("cliCodi",true)))
                cliNomb =  dtStat.getString("prv_nomb");
          }
          
          if (sel=='R')
              v.add(dt.getString("zonCodi",true));
          else
              v.add(dt.getInt("cliCodi",true)+" "+cliNomb);
          
          v.add(dt.getString("ejenume")+"-"+
                  dt.getString("serie")+"-"+
                  dt.getString("lote")+"-"+dt.getString("numind") );
          if (sel=='V')
            v.add(!swVerPrecios?"":dt.getDouble("precio")-preStk);
          else
            v.add("");
          if (swUsaDocumentos)
          {
            if ( dt.getInt("alm_codori")==0)
                v.add(dt.getInt("alm_codi"));
            else 
                  v.add(dt.getInt("alm_codori")+"->"+dt.getInt("alm_coddes",true));
          }
          else
              v.add(dt.getInt("alm_codi"));
//          v.add(dt.getInt("alm_codi"));
          jt.addLinea(v);
        }
      } while (dt.next());
    
      return true;
  }

  public void setCancelarConsulta(boolean cancConsulta)
  {
      cancelarConsulta=cancConsulta;
  }
  public boolean isCancelarConsulta()
  {
       return cancelarConsulta;
  }
  void resetAcumMvtos()
  {
    kgEnt=kgSal=0;
    unEnt=unSal=unVent=unSalDes=unEntDes=unCompra=unRegul=0;
    impEnt=impSal=0;
    kgVen=kgCompra=kgRegul=0;
    impVenta=0;
    impGana=0;
    kgSalDes=0;
    kgEntDes=impEntDes=impSalDes=impCompra=impRegul=0;
  }
  public void resetAcumulados()
  {
      swIncAcum=true;
      preStk=0;
      canStk=0;
      uniStk=0;
      feulst="";
  }
  /**
   * Devuelve el precio de stock. (o sea, el precio q tiene el producto
   * en la fecha indicada).
   * @return Precio stock.  (por kilo)
   */
  public double getPrecioStock()
  {
      return preStk;
  }
  public double getKilosStock()
  {
       return  canStk;
  }
  public double getImporteEntrada()
  {
      return impEnt;
  }
  public double getKilosEntrada()
  {
       return  kgEnt;
  }
   public double getUnidadesEntrada()
  {
       return  unEnt;
  }
   public double getImporteCompra()
  {
      return impCompra;
  }
  public double getKilosCompra()
  {
       return  kgCompra;
  }
   public double getUnidadesCompra()
  {
       return  unCompra;
  }
  public double getImporteRegul()
  {
      return impRegul;
  }
  public double getKilosRegul()
  {
       return  kgRegul;
  }
   public double getUnidadesRegul()
  {
       return  unRegul;
  }
  public double getUnidadesVenta()
  {
       return  unVent;
  }
  public double getKilosSalida()
  {
      return kgSal;
  }
  public double getUnidadesSalida()
  {
      return unSal;
  }
  public double getImporteSalida()
  {
      return impSal;
  }
  public int getUnidStock()
  {
    return     uniStk;
  }
  public double getKilosVenta()
  {
      return kgVen;
  }
  
  public double getImporteVenta()
  {
      return impVenta;
  }
  public double getImpGana()
  {
      return impGana;
  }
  public double getKilosSalDesp()
  {
      return kgSalDes;
  }
  public double getKilosEntDesp()
  {
      return kgEntDes;
  }
  public double getImporteEntDesp()
  {
      return impEntDes;
  }
  public int getUnidadesEntDesp()
  {
      return unEntDes;
  }
  public double getImporteSalDesp()
  {
      return impSalDes;
  }
  public int getUnidadesSalDesp()
  {
      return unSalDes;
  }
  /**
   * Indica si debe resetear costos en caso de que el stock se quede en negativo
   * Por defecto es true.
   * @param aceptaNegativos 
   */
  public void setResetCostoStkNeg(boolean aceptaNegativos)
  {
       resetCostoStkNeg=aceptaNegativos;
  }
  public boolean isResetCostoStkNeg()
  {
       return resetCostoStkNeg;
  }
  private void msgBox(String msg)
  {
      if (padre!=null)
         padre.msgBox(msg);
      msgLog+=msg;
  }
  public String getMsgLog()
  {
      return msgLog;
  }
  public String getMsgDesp()
  {
      return msgDesp;
  }
  public String getMsgCompra()
  {
    return msgCompra;
  }
  
  public void resetMensajes()
  {
      msgLog="";
      msgDesp="";
      msgCompra="";
      msgStock="";
  }
   public String getMsgStock()
  {
      return msgStock;
  }
  public void resetMsgLog()
  {
      msgLog="";
  }
  private void mensajeErr(String msg)
  {
      if (padre!=null)
          padre.mensajeErr(msg,false);
  }
  public void setPadre(ventana papa)
  {
     padre=papa;
     if (papa!=null)
         EU=papa.EU;
  }
//  /**
//   * Establece si se deben sumar los ajustes de costos
//   * @param incAjusteCostos 
//   */
//  public void setIncAjusteCostos(boolean incAjusteCostos)
//  {
//      swAjusteCostos=incAjusteCostos;
//  }
//  public boolean getIncAjusteCostos()
//  {
//      return swAjusteCostos;
//  }
  /**
   * Establece la variable del entorno usuario
   * @param eu 
   */
  public void setEntornoUsuario(EntornoUsuario eu)
  {
    EU=eu;
  }
  boolean isRootAV()
  {
      if (EU==null)
          return false;
      return EU.isRootAV();
  }
          
    /**
     * @return the swVerVenta
     */
    public boolean isVerVenta() {
        return swVerVenta;
    }

    /**
     * @return the swVerCompra
     */
    public boolean isVerCompra() {
        return swVerCompra;
    }

    /**
     * @return the swVerDesEnt
     */
    public boolean isVerDesEnt() {
        return swVerDesEnt;
    }

    /**
     * @return the swVerDespSal
     */
    public boolean isVerDespSal() {
        return swVerDespSal;
    }

    /**
     * @return the swVerRegul
     */
    public boolean isVerRegul() {
        return swVerRegul;
    }

    /**
     * @param swVerRegul the swVerRegul to set
     */
    public void setVerRegul(boolean swVerRegul) {
        this.swVerRegul = swVerRegul;
    }

   
    /**
     * @param swVerVenta the swVerVenta to set
     */
    public void setVerVenta(boolean swVerVenta) {
        this.swVerVenta = swVerVenta;
    }
    /**
     * Establece costo fijo a considerado como inicial para fechas introducidas
     * @param costoFijo  <0 No tener en cuenta.
     *                   
     */
    public void setCostoFijo(double costoFijo)
    {
        this.costoFijo=costoFijo;
    }
    /**
     * Establece si mostrara los precios en el grid.
     * @param verPrecios 
     */
    public void setVerPrecios(boolean verPrecios)
    {
        swVerPrecios=verPrecios;
    }
    /**
     * @param swVerCompra the swVerCompra to set
     */
    public void setVerCompra(boolean swVerCompra) {
        this.swVerCompra = swVerCompra;
    }
    public boolean isPermCostosNegat() {
        return swPermCostosNegat;
    }
    /**
     * Permite Costos negativos en calculos de costos.
     * Por defecto si que lo permite en casos de error en stocks.
     * 
     * @param swPermNegat 
     */
    public void setPermCostosNegat(boolean swPermNegat) {
        this.swPermCostosNegat = swPermNegat;
    }
    /**
     * @param swVerDesEnt the swVerDesEnt to set
     */
    public void setVerDesEnt(boolean swVerDesEnt) {
        this.swVerDesEnt = swVerDesEnt;
    }

    /**
     * @param swVerDespSal the swVerDespSal to set
     */
    public void setVerDespSal(boolean swVerDespSal) {
        this.swVerDespSal = swVerDespSal;
    }
     /**
     * Carga el combo mandado con las fechas de inventario disponibles. Hasta un maximo de 24 Entradas
     *
     * @param dt DatosTabla
     * @param empCodi Empresa
     * @param ejeNume Ejercicio
     * @param feulinE ComboBox a llenar.
     * 
     * @return Fecha ultimo inventario
     * @throws SQLException
     */
     public static String  llenaComboFecInv(DatosTabla dt, int empCodi, int ejeNume, CComboBox feulinE
            ) throws SQLException {
            return llenaComboFecInv(dt,empCodi,ejeNume,feulinE,24);
     }
     /**
      * Devuelve la ultima fecha inventario inferior o igual a la mandada.
      * @param dt Datostabla
      * @param fecmax . Fecha maxima de inventario
      * @param empCodi Empresa. 0 = TODAS
      * @return Fecha Ultimo Inventario. Null si no hay ninguna
      * @throws SQLException 
      */
     public static Date getFechaUltInv(DatosTabla dt, Date fecmax,int empCodi) throws SQLException
     {
         String s = "select max(rgs_fecha) as cci_feccon from v_inventar as r  " +               
                "  where rgs_fecha <= '"+Formatear.getFechaDB(fecmax)+"'"+
                (empCodi==0?"": " and  r.emp_codi = " + empCodi);
         if (!dt.select(s))
             return null;
         return dt.getDate("cci_feccon");
     }
    /**
     * Carga el combo mandado con las fechas de inventario disponibles.
     *
     * @param dt DatosTabla
     * @param empCodi Empresa
     * @param ejeNume Ejercicio
     * @param feulinE ComboBox a llenar.
     * @param maxElemen numero maximo de elementos a cargar en el combo. (0=todos)
     * @return Fecha ultimo inventario
     * @throws SQLException
     */
    public static String  llenaComboFecInv(DatosTabla dt, int empCodi, int ejeNume, CComboBox feulinE
            ,int maxElemen) throws SQLException 
    {
        feulinE.removeAllItems();
        String feulin;
        String s = "select distinct(rgs_fecha) as fechas from v_inventar as r  "
                + (empCodi==0?"": "where r.emp_codi = " + empCodi)                            
                + " order by fechas desc ";
        if (dt.select(s))
        {
            return llenaComboFechas(dt,feulinE,maxElemen);
        }
        else 
        {
            feulin = "01-01-" + ejeNume; // Buscamos desde el principio del año.
            feulinE.addItem(feulin);
        }
        feulinE.setText(feulin);
        return feulin;
    }
    public static String  llenaComboFecInvControl(DatosTabla dt, int empCodi, CComboBox feulinE ,int maxElemen) throws SQLException 
    {      
        feulinE.removeAllItems();
        String s="SELECT DISTINCT(cci_feccon) as fechas FROM coninvcab "
            + (empCodi==0?"": "where emp_codi = " + empCodi)             
            + " order by fechas desc";
       
        if (dt.select(s))
            return llenaComboFechas(dt,feulinE,maxElemen);
        return null;
    }
    static String llenaComboFechas(DatosTabla dt, CComboBox feulinE, int maxElemen) throws SQLException 
    {
        String feulin;
        int nMax = 1;
        feulin = dt.getFecha("fechas", "dd-MM-yyyy");
        do
        {
            feulinE.addItem(dt.getFecha("fechas", "dd-MM-yyyy"), dt.getFecha("fechas", "dd-MM-yyyy"));
            nMax++;
            if (nMax > maxElemen && maxElemen > 0)
                break;
        } while (dt.next());
        return feulin ;
    }
  
    /**
     * Comprueba si un individuo ha tenido salidas a partir de una fecha
     * @param dt
     * @param proCodi Codigo de procuto. Si es cero se ignorara
     * @param empCod
     * @param ejeNume
     * @param serie
     * @param numpar
     * @param numInd . Si es cero se ignorara.
     * @param kilos a buscar. Si es cero se ignorara
     * @param fecha Fecha en formato 'dd-MM-yyyy'
     * @throws SQLException 
     * @return 1 SI tiene albarane de venta    2 si tiene despieces de salida.
     *         0 Si no tiene movimientos.
     */
    public static int  hasMvtosSalida(DatosTabla dt, int proCodi,int empCod,int ejeNume,String serie, int numpar,
            int numInd,double kilos,String fecha) throws SQLException
    {
          String s="select p.avp_ejelot from v_albvenpar as p,v_albavec as c where   avp_ejelot="+ejeNume+
                   " and avp_emplot = "+empCod+
                   " and avp_serlot ='"+serie+"'"+
                   " and avp_numpar ="+numpar+
                   (proCodi==0?"":" and pro_codi = "+proCodi)+
                  (numInd==0?"":" and avp_numind = "+numInd)+
                  (kilos!=0?" and avp_canti ="+kilos:"")+
                  " and c.avc_ano =p.avc_ano "+
                  " and c.avc_nume = p.avc_nume "+
                  " and c.emp_codi = p.emp_codi "+
                  " and c.avc_serie = p.avc_serie "+
                  " and avc_fecalb >= to_date('"+fecha+"','dd-MM-yyyy')";
          if (dt.select(s))
              return 1;
           s="select deo_codi from v_despori where   deo_ejelot ="+ejeNume+
                   " and deo_emplot = "+empCod+
                   " and deo_serlot ='"+serie+"'"+
                   " and pro_lote    ="+numpar+
                   (proCodi==0?"":" and pro_codi = "+proCodi)+
                   (numInd==0?"":" and pro_numind ="+numInd)+
                   (kilos!=0?" and deo_kilos ="+kilos:"")+
                   " and deo_fecha >= to_date('"+fecha+"','dd-MM-yyyy')";
           if (dt.select(s))
              return 2;
           return 0;
    }
    /**
     * Comprueba si un individuo ha tenido Entradas a partir de una fecha
     * @param dt
     * @param proCodi Codigo de procuto. Si es cero se ignorara
     * @param empCod
     * @param ejeNume
     * @param serie
     * @param numpar
     * @param numind . Si es cero se ignorara.
     * @param kilos a buscar. Si es cero se ignorara
     * @param fecha Fecha en formato 'dd-MM-yyyy'. NULL si se quiere ignorar
     * @throws SQLException 
     * @return 1 SI tiene despiece salida   2 si tiene albaran de compra
     *         0 Si no tiene movimientos.
     */
    public static int  hasMvtosEntrada(DatosTabla dt, int proCodi,int empCod,int ejeNume,String serie, int numpar,
            int numInd,double kilos,String fecha) throws SQLException
    {
          String s="select pro_lote from v_despfin a where   def_ejelot="+ejeNume+
                   " and def_emplot = "+empCod+
                   " and def_serlot ='"+serie+"'"+
                   " and pro_lote ="+numpar+
                   (proCodi==0?"":" and pro_codi = "+proCodi)+
                  (numInd==0?"":" and pro_numind = "+numInd)+
                  (kilos!=0?" and def_kilos ="+kilos:"")+
                  (fecha==null?"":" and def_tiempo >= to_date('"+fecha+"','dd-MM-yyyy')");
          if (dt.select(s))
              return 1;
           s="select c.acc_serie from v_albcompar as i, v_albacoc as c where   i.acc_ano ="+ejeNume+
                   " and i.emp_codi = "+empCod+
                   " and i.acc_serie ='"+serie+"'"+
                   " and i.acc_nume    ="+numpar+
                   (proCodi==0?"":" and i.pro_codi = "+proCodi)+
                   (numInd==0?"":" and i.acp_numind ="+numInd)+
                   (kilos!=0?" and i.acp_canti ="+kilos:"")+
                   (fecha==null?"":" and c.acc_fecrec >= to_date('"+fecha+"','dd-MM-yyyy')")+
                   " and i.emp_codi = c.emp_codi "+
                   " and i.acc_serie = c.acc_serie "+
                   " and i.acc_ano = c. acc_ano "+
                   " and i.acc_nume = c.acc_nume";
           if (dt.select(s))
              return 2;
           return 0;
    }
}
