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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * <p>Título: MvtosAlma </p>
 * <p>Descripción: Clase con rutinas para calcular mvtos valorados de almacen
 * @see gnu.chu.anjelica.almacen.conmvpr</p>
 * @see gnu.chu.anjelica.margenes.colizona
 * <p>Copyright: Copyright (c) 2005-2011
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
 * @version 1.0
 */
public class MvtosAlma
{
  private String tiposVert="";
  private boolean swDesglInd=false;
  private double incCosto=0;
  private String accesoEmp;
  private Date dateFin;
  private boolean swCompraNoValor,swDespNoValor;
  private boolean swIgnVert=false;
  private boolean swIgnDespFecha=false; // Ignorar despieces de ult.fecha
  boolean swSoloInv=false; // Solo busca registros en inventarios
  boolean incInvFinal=false; // Por defecto no se incluye inventario de ult. fecha
  private String msgStock="";
  private int deoCodiLim=0; // Numero despiece a partir del cual no considerar mas para calculos costos (incluido)
  private int sbeCodi; // Subempresa de Cliente.
  private int numProd,numProdInv=0; // Numero para el numero de veces q aparece el prod. en el statement
  String feulst=""; // Fecha Ultimo Stock
  private PreparedStatement pStmt=null,pSInv=null;

    public void setpSInv(PreparedStatement pSInv) {
        this.pSInv = pSInv;
    }
  private boolean swVerVenta,swVerCompra,swVerDesEnt,swVerDespSal,swVerRegul;
  private double costoFijo=-1; 
  double prVen=0;
  double kgEnt=0,kgVen=0,kgCompra=0,kgRegul=0;
  double prEnt=0;
  double impGana,impCompra,impEntDes,impSalDes,impRegul;
  double kgSalDes=0;
  double kgEntDes=0;
  private ventana padre;
  private EntornoUsuario EU;
  private String msgLog="",msgDesp="",msgCompra="";
  private boolean cancelarConsulta=false;
  private double preStk,canStk;
  private int uniStk,unEnt,unVent,unCompra,unSalDes, unEntDes,unRegul;
  private boolean swDespSalida=false,swValDesp=false;
  private boolean swIgnDespIgu=false;
  private int almCodi=0;
  private int proNumind=0;
  private int proLote=0,empCodi=0;
  private boolean swSerieX=false; // Incluir Albaranes de Serie X (traspaso entre almacenes)
  private boolean swPermCostosNegat=true; // Permitir costos en negativo al calcular costos.

  private boolean resetCostoStkNeg=true; // Si el stock es negativo, resetea costo.

  /**
   * Indica si debe ignorar para costos los despieces cuyo
   * producto de entrada y salida sean iguales.
   * Por defecto es false
   * @param swIgnDespIgu
   * @return
   */
  public void setIgnDespIgu(boolean swIgnDespIg)
  {
     this.swIgnDespIgu= swIgnDespIg;
  }
  public boolean getIgnDespIgu()
  {
     return swIgnDespIgu;
  }
  /**
   * Establece si se debe ignorar en los despieces la fecha superior
   * Para cuando se esta valorando los despieces en si, y no se quiere que se autovaloren.
   * Actualmente solo usado por el programa de regeneración de costes.
   * @param swIgnDespFecha
   */
  public void setIgnorarDespFecha(boolean swIgnDespFecha)
  {
    this.swIgnDespFecha=swIgnDespFecha;
  }
  public boolean getIgnorarDespFecha()
  {
    return this.swIgnDespFecha;
  }
  /**
   * Indica si los despieces de entrada deben considerarse como salidas en negativo
   * @default false
   * @param valoraDes 
   */
  public void setValorarDesp(boolean valoraDes)
  {
      swValDesp=valoraDes;
  }
  public boolean getValorarDesp()
  {
      return swValDesp;
  }
  /**
   * Incluir serie X. Esta es la serie de albaranes de venta
   * donde estan incluidos los traspasos entre almacenes
   * @param serieX true=si
   */
  public void setSerieX(boolean serieX)
  {
      swSerieX=serieX;
  }
  public boolean getSerieX()
  {
      return swSerieX;
  }
  
  public void setLote(int lote)
  {
      proLote=lote;
  }
  public int getLote()
  {
      return proLote;
  }
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
  public void setDespSalida(boolean despSalida)
  {
      swDespSalida=despSalida;
  }
  public boolean getDespSalida()
  {
      return swDespSalida;
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
  /**
   * Indica si debe ignorar vertederos al buscar los movimientos
   * @param ignVert 
   */
  public void setIgnoraVert(boolean ignVert)
  {
       swIgnVert=ignVert;
  }
  /**
   * Devuelve Sentencia SQL a ejecutar para buscar los movimientos.
   * @param fecIni Fecha Inico
   * @param fecFin Fecha Final
   * @param proCodi Producto. Si el producto es -1 pone un interrogante para utilizarlo
   *        como PreparedStatement
   * @return String con la sentencia sql
   */
  public String getSqlMvt(String fecIni, String fecFin, int proCodi) throws ParseException
  {
    numProd=1;
    this.dateFin=Formatear.getDate(fecFin,"dd-MM-yyyy");
    
    if (swDespSalida) // Solo saca despieces de salida.
      return  " select 1 as orden,'DE' as sel, '+' as tipmov,'' as fecmov,"+
          "  l.def_serlot as serie,l.pro_lote as  lote,"+
          " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind,"+
          " 0 as cliCodi,l.deo_codi  as numalb,l.def_ejelot as ejenume, "+
          " l.def_emplot as empcodi,'0' as pro_codori "+
          ", '' as repCodi,'' as zonCodi,0 as sbe_codi "+
          ", l.def_numpie as unidades,0 as div_codi,alm_codi,'.' as avc_serie "+
           ", 0 as alm_codori,0 as alm_coddes, 'N' as avc_depos "+
          " from  v_despfin l where "+
          " l.def_kilos <> 0 "+
          (almCodi==0?"":" and alm_codi = "+almCodi)+
          (proLote==0?"":" and l.pro_lote  = "+proLote)+
          (proNumind==0?"":" and l.pro_numind = "+proNumind)+
          " AND l.pro_codi = " + (proCodi==-1?"?":proCodi) +
          (empCodi==0?"":" and l.emp_codi = "+empCodi)+
          " ORDER BY 3,2 desc"; // FECHA y tipo
    numProd=6;
    String sql="";
    if (! swSoloInv)
    {
      if (! swDesglInd && proNumind==0)
       sql+="SELECT 1 as orden,'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
        " c.acc_nume as  lote,"+
        " l.acl_canti as canti,l.acl_prcom as precio,0 as numind,"+
        " c.prv_codi as cliCodi,  c.acc_nume as numalb, "+
        " c.acc_ano as ejeNume,c.emp_codi as empCodi,l.pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,sbe_codi,acl_numcaj as unidades,0 as div_codi, "+
        " l.alm_codi,'.' as avc_serie "+
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
        " AND l.pro_codi = "+(proCodi==-1?"?":proCodi) +
        " AND c.acc_fecrec >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
        " and c.acc_fecrec <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
      else
         sql+="SELECT 1 as orden,'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
        " c.acc_nume as  lote,"+
        " i.acp_canti as canti,l.acl_prcom as precio,acp_numind as numind,"+
        " c.prv_codi as cliCodi,  c.acc_nume as numalb, "+
        " c.acc_ano as ejeNume,c.emp_codi as empCodi,l.pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,sbe_codi,acp_canind as unidades,0 as div_codi, "+
        " l.alm_codi,'.' as avc_serie "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " FROM v_albacoc c,v_albacol l,v_albcompar i "+
        " where c.emp_codi = l.emp_codi "+
        " AND c.acc_serie = l.acc_serie "+
        " AND c.acc_nume = l.acc_nume "+
        " and c.acc_ano = l.acc_ano "+
        " and c.emp_codi = i.emp_codi "+
        " AND c.acc_serie = i.acc_serie "+
        " AND c.acc_nume = i.acc_nume "+
        " and c.acc_ano = i.acc_ano "+
        " and l.acl_nulin = i.acl_nulin "+
        " and i.acp_canti <> 0 "+
        (proNumind==0?"":" and acp_numind = "+proNumind)+
        (empCodi==0?"":" and c.emp_codi = "+empCodi)+
        (almCodi==0?"":" and l.alm_codi = "+almCodi)+
        (proLote==0?"":" and c.acc_nume = "+proLote)+
        " AND l.pro_codi = "+(proCodi==-1?"?":proCodi) +
        " AND c.acc_fecrec >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
        " and c.acc_fecrec <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
    sql+=" UNION all"; // Albaranes de Venta
    String condAlb=  " where c.emp_codi = l.emp_codi "+
          " AND c.avc_serie = l.avc_serie "+
          " AND c.avc_nume = l.avc_nume "+
          " and c.avc_ano = l.avc_ano "+
          " and l.avl_canti <> 0 "+
          " and i.emp_codi = l.emp_codi " +
          " AND i.avc_serie = l.avc_serie " +
          " AND i.avc_nume = l.avc_nume " +
          " and i.avc_ano = l.avc_ano " +
          " and i.avl_numlin = l.avl_numlin " +
          (proLote==0?"":" and i.avp_numpar  = "+proLote)+
          (proNumind==0?"":" and i.avp_numind = "+proNumind)+
          (empCodi==0?"":" and c.emp_codi = "+empCodi)+
          " AND l.pro_codi = "+(proCodi==-1?"?":proCodi) +
          " AND l.avl_fecalt::date >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
          " and l.avl_fecalt::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";

    sql+= " select 2 as orden,'VE' as sel,'-' as tipmov,l.avl_fecalt as fecmov,"+
        "  i.avp_serlot  as serie,i.avp_numpar as  lote,"+
        " i.avp_canti  as canti,l.avl_prbase as precio,i.avp_numind as numind,"+
        " c.cli_codi as cliCodi,c.avc_nume as numalb, "+
        " i.avp_ejelot as ejeNume,i.avp_emplot as empcodi,l.pro_codi as pro_codori, "+
        " rep_codi as repCodi,zon_codi as zonCodi "+
        ",c.sbe_codi, avp_numuni as unidades, c.div_codi, l.alm_codi,c.avc_serie "+
        ", alm_codori, alm_coddes,c.avc_depos "+
        "  from v_albavel l, v_albvenpar i, v_albavec c "+
        " left join  clientes as cl on cl.cli_codi = c.cli_codi "+
         condAlb+
//         " and avl_fecalt >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+ // Solo albaranes con fecha mvto. Superior o Igual a Inicial
          // Si almacen !=0 e incluir serie X
         (almCodi!=0 && swSerieX?" AND (alm_codori="+almCodi+" or alm_coddes="+almCodi+")":"")+
         // Si almacen !=0 y NO  incluir serie X. No tiene mucho sentido...
        (almCodi!=0 && ! swSerieX ?" and l.alm_codi = "+almCodi: "")+
        // Si no se deben incluir los traspasos, quito los albaranes de serie X
        (! swSerieX?" and c.avc_serie != 'X'":"")+
        " UNION ALL "+
        " select 2 as orden,'VE' as sel,'-' as tipmov,c.avc_fecalb as fecmov,"+
        "  i.avp_serlot  as serie,i.avp_numpar as  lote,"+
        " i.avp_canti  as canti,l.avl_prbase as precio,i.avp_numind as numind,"+
        " c.cli_codi as cliCodi,c.avc_nume as numalb, "+
        " i.avp_ejelot as ejeNume,i.avp_emplot as empcodi,l.pro_codi as pro_codori, "+
        " rep_codi as repCodi,zon_codi as zonCodi "+
        ",c.sbe_codi, avp_numuni as unidades, c.div_codi, l.alm_codi,c.avc_serie "+
        ", alm_codori, alm_coddes,c.avc_depos "+
        "  from v_albavel l, v_albvenpar i, v_albavec c "+
        " left join  clientes as cl on cl.cli_codi = c.cli_codi "+
         condAlb+
         " and avl_fecalt::date < TO_DATE('"+fecIni+"','dd-MM-yyyy') "+ // Solo albaranes con fecha mvto. Inferior a Inicial
          // Si almacen !=0 e incluir serie X
         (almCodi!=0 && swSerieX?" AND (alm_codori="+almCodi+" or alm_coddes="+almCodi+")":"")+
         // Si almacen !=0 y NO  incluir serie X. No tiene mucho sentido...
        (almCodi!=0 && ! swSerieX ?" and l.alm_codi = "+almCodi: "")+
        // Si no se deben incluir los traspasos, quito los albaranes de serie X
        (! swSerieX?" and c.avc_serie != 'X'":"");
//        " and c.avc_serie "+(swSerieX?"":"!")+"='X'";
      sql+=" UNION all "+ // Despieces (Salidas de Stock)
        " select 2 as orden,'DS' as sel,'"+
         (swValDesp?"+":"-")+"' as tipmov ,"+
         " deo_tiempo as fecmov,"+
        "  deo_serlot as serie,pro_lote as  lote,"+
        " deo_kilos as canti,deo_prcost as precio,pro_numind as numind,"+
        " 0 as cliCodi,deo_codi as numalb,deo_ejelot as ejeNume," +
        " deo_emplot as empcodi,pro_codi as pro_codori "+
        ", '' as repCodi,'' as zonCodi,0 as sbe_codi "+
        ", 1 as unidades,0 as div_codi,deo_almori as alm_codi,'.' as avc_serie "+
        ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
        " from  v_despori where "+
        "  pro_codi = " + (proCodi==-1?"?":proCodi)  +
        " and deo_kilos <> 0 "+ 
        (almCodi==0?"":" and deo_almori = "+almCodi)+
        (proLote==0?"":" and pro_lote  = "+proLote)+
        (proNumind==0?"":" and pro_numind = "+proNumind)+
        (empCodi==0?"":" and emp_codi = "+empCodi)+
        (accesoEmp==null || empCodi!=0?"":" and emp_codi in ("+accesoEmp+")")+
        " AND deo_tiempo::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
        " and deo_tiempo::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
      sql+=" UNION all "+ // Despieces (Entradas)
       " select 1 as orden,'DE' as sel, '+' as tipmov,l.def_tiempo as fecmov,"+
       "  l.def_serlot as serie,l.pro_lote as  lote,"+
       " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind,"+
       " 0 as cliCodi,l.deo_codi  as numalb,l.def_ejelot as ejenume, "+
       " l.def_emplot as empcodi,0 as pro_codori "+
       ", '' as repCodi,'' as zonCodi,0 as sbe_codi "+
       ", l.def_numpie as unidades,0 as div_codi,alm_codi,'.' as avc_serie "+
       ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
       " from  desporig c,v_despfin l where "+
       "  c.eje_nume = l.eje_nume "+
       " and c.deo_codi = l.deo_codi "+
       " and l.def_kilos <> 0 "+
       (almCodi==0?"":" and alm_codi = "+almCodi)+
       (proLote==0?"":" and l.pro_lote  = "+proLote)+
       (proNumind==0?"":" and l.pro_numind = "+proNumind)+
//       (empCodi==0?"":" and c.emp_codi = "+empCodi)+
       " AND l.pro_codi = " + (proCodi==-1?"?":proCodi)  +
       " AND l.def_tiempo::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
       " and l.def_tiempo::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
    sql+=" UNION all "+ // Regularizaciones.
       " select 1 as orden,'RE' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"+
       "  r.pro_serie as serie,r.pro_nupar as  lote,"+
       " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
       " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
       " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
       ", tir_tipo as repCodi,m.tir_nomb as zonCodi,0 as sbe_codi "+
       ", rgs_canti as unidades, 0 as div_codi,alm_codi,'.' as avc_serie "+
       ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
       " FROM v_regstock r, v_motregu m WHERE "+
       " m.tir_codi = r.tir_codi "+
       " and rgs_kilos <> 0 "+
       ( incInvFinal?"": " and tir_afestk != '='")+ // Sin Incluir Inventarios
       (swIgnVert?" and tir_tipo NOT like 'V%'":"")+
        " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
       (almCodi==0?"":" and alm_codi = "+almCodi)+
       (proLote==0?"":" and r.pro_nupar  = "+proLote)+
       (proNumind==0?"":" and r.pro_numind = "+proNumind)+
       (empCodi==0?"":" and r.emp_codi = "+empCodi)+
       (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
        " AND r.pro_codi = " + (proCodi==-1?"?":proCodi)  +
        " AND r.rgs_fecha::date >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
        " and r.rgs_fecha::date <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
    }
    if (! incInvFinal || swSoloInv)
    { // No incluir inventario final.
         numProd++;
         if (! swSoloInv)
             sql+=" UNION all "; // Regularizaciones Inc. Inventario
         else
         {
            numProd=1;
            sql="";
         }
         sql+=" select 1 as orden,'RE' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"+
           "  r.pro_serie as serie,r.pro_nupar as  lote,"+
           " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
           " rgs_recprv as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
           " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
           ", tir_tipo as repCodi,m.tir_nomb as zonCodi,0 as sbe_codi "+
           ", rgs_canti as unidades, 0 as div_codi,alm_codi,'.' as avc_serie "+
           ", 0 as alm_codori,0 as alm_coddes,'N' as avc_depos "+
           " FROM v_regstock r, v_motregu m WHERE "+
           " m.tir_codi = r.tir_codi "+
           " and tir_afestk = '='"+ // Solo Inventarios
           " and rgs_kilos <> 0 "+
            " and rgs_trasp != 0 "+ // Tienen q estar traspasados.
           (almCodi==0?"":" and alm_codi = "+almCodi)+
           (proLote==0?"":" and r.pro_nupar  = "+proLote)+
           (proNumind==0?"":" and r.pro_numind = "+proNumind)+
           (empCodi==0?"":" and r.emp_codi = "+empCodi)+
           (accesoEmp==null || empCodi!=0?"":" and r.emp_codi in ("+accesoEmp+")")+
            " AND r.pro_codi = " + (proCodi==-1?"?":proCodi)  +
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
   * Define la cadena para limitar accesos a las empresas.
   * @param acessoEmp
   * @return 
   */
  public void setAccesoEmp(String acessoEmp)
  {
      this.accesoEmp=acessoEmp;
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
    /**
     * Iniciar movimientos.
     * Funcion  a llamar cuando se van a calcular Movimientos sobre diferentes
     * productos pero con las mismas condiciones.
     * LLamar despues a
     *  calculaMvtos(int proCodi,DatosTabla dtCon1,DatosTabla dtStat,String zonCodi, String repCodi)
     * @param fecInv Fecha Inventario (Formato dd-MM-yyyy)
     * @param fecIni Fevcha Inicial (Formato dd-MM-yyyy)
     * @param fecFin Fecha Final ((Formato dd-MM-yyyy))
     * @param dtCon1 DatosTabla principal
     * @throws SQLException
     * @throws ParseException
     */
    public void iniciarMvtos(String fecInv, String fecIni, String fecFin,
            DatosTabla dtCon1) throws SQLException, ParseException {

        String fefi=fecIni;
        boolean soloInv=swSoloInv;
        boolean incUltFecInv=incInvFinal;
        tiposVert=pdmotregu.getTiposRegul(dtCon1,"V%");
        if (fecInv.equals(fecIni))
        {
            pSInv=null;
        }
        else
        {
           
            fefi = Formatear.sumaDias(fecIni, "dd-MM-yyyy", -1);
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
   * @param fecInv Fecha ultimo inventario (o de Inicio)
   * @param fecMvto Fecha en la que calcular costo y kilos
   * @param dtCon1 DatosTabla principal
   * @throws SQLException
   * @throws java.text.ParseException
   */
    public void iniciarMvtos(String fecIni,String fecFin,   DatosTabla dtCon1) throws SQLException,ParseException {
        String s = getSqlMvt(fecIni, fecFin, -1);
        pStmt=dtCon1.getPreparedStatement(s);
    }

    /**
     * Debera haberse llamado primero a la funcion iniciarMvtos
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
          if (rs.next())
            calculaMvtos(rs,dtCon1,dtStat,null,null,null,proCodi);
          feulst=null;
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
    boolean ignDesp=false;
    boolean swErr=true;
    String tipMov;
    String cliNomb;
    double precio;
    double kg;
    boolean swPas1;
    double canti;
    int unid,cj;
    char sel;
    kgEnt=0;
    unEnt=unVent=unSalDes=unEntDes=unCompra=unRegul=0;
    prEnt=0;
    kgVen=kgCompra=kgRegul=0;
    prVen=0;
    impGana=0;
    kgSalDes=0;
    kgEntDes=impEntDes=impSalDes=impCompra=impRegul=0;
    boolean swNegat=false;
    boolean swIgnVenta=false;
    boolean swTraspAlm;
    swCompraNoValor=false;
    swDespNoValor=false;
    if (rs!=null)
        dt.setResultSet(rs);

    // Inicializando variables.
    if (jt!=null)
       jt.removeAllDatos();
    String ref="";
    Double cant;
    double cantiInd=0;
    HashMap<String,Double> ht = new HashMap();
    do
    {
        if (cancelarConsulta)
           return false;
        swTraspAlm=false;
        canti = 0;
        unid=0;
        precio =preStk;
        tipMov=dt.getString("tipmov");
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
               kgCompra+=dt.getDouble("canti"); // Ignoramos los Inventarios
               unCompra+=dt.getInt("unidades");
               if (dt.getDouble("precio")==0)
               {
                   msgLog+="Compra con Precio 0 " +
                         ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                   msgCompra+="Precio 0 " +
                         ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                   swCompraNoValor=true;
               }
               impCompra+=dt.getDouble("precio")*dt.getDouble("canti");
            }
            kgEnt+=dt.getDouble("canti"); // Ignoramos los Inventarios
            unEnt+=dt.getInt("unidades");
            prEnt+=dt.getDouble("precio")*dt.getDouble("canti");
        }
        if (sel=='R' && ! tipMov.equals("="))
        {
          if (tipMov.equals("+"))
          {
             kgRegul += dt.getDouble("canti",true);
             unRegul +=dt.getInt("unidades",true);
             impRegul+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
          }
          else
          {
             kgRegul -= dt.getDouble("canti",true);
             unRegul -=dt.getInt("unidades",true);
             impRegul-=dt.getDouble("canti",true)*dt.getDouble("precio",true);
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

        if (tipMov.equals("+"))
        { // Entrada.
          kg=dt.getDouble("canti");
          cantiInd+=dt.getDouble("canti");
          cj=dt.getInt("unidades");
          if (sel=='D')
          {
            kg = kg * -1;
            cj = cj * -1;
          }

          canti = canStk + kg;
          unid = uniStk + cj;
          swPas1=false;
          if (sel=='d')
          { // Despiece de salida (Entrada a almacen)            
            ignDesp=false;
            if (deoCodiLim!=0 && dt.getInt("numalb")==deoCodiLim)
                ignDesp=true;
            swPas1=ignDesp;
            if (swIgnDespFecha && Formatear.comparaFechas(dt.getDate("fecmov"), dateFin)>=0)
                swPas1=true;
            if (! swPas1  && ((dt.getInt("pro_codori") == proCodi &&  ! swValDesp && swIgnDespIgu) ||
                dt.getDouble("precio") == 0))
            {
               if (dt.getDouble("precio") == 0)
               {
                   msgLog+="Despiece de entrada ignorado por tener Precio 0 " +
                         ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                   msgDesp+="Fecha "+dt.getFecha("fecmov")+" Kilos: "+kg+"\n";
                   swDespNoValor=true;
               }
               else
                   msgLog+="Despiece de entrada ignorado por tener mismo prod. entrada y salida " +
                         ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
              swPas1=true; // Marco para que pase de tener en cuenta este mvto. para precio
            }
          }
          if (swPas1==false)
          {
              if ((canStk < -0.1 || canti < -0.1 ) && resetCostoStkNeg )
              { // Cant. anterior y/o actual en Negativo
//                 if (sel!='R')
//                 {
                 msgLog+="Atencion Stock en Negativo: " + Formatear.redondea(canStk,2) +
                         ":  Fecha: " + dt.getFecha("fecmov")+" Prod: "+ proCodi+" \n";
                if (swErr == false)
                {
                  swErr = true;
                  msgBox("Stock en Negativo: " + Formatear.redondea(canStk,2)+
                         ":  Fecha: " + dt.getFecha("fecmov"));
                }
                else
                  mensajeErr(" Stock en Negativo: " + Formatear.redondea(canStk,2) +
                             ": EN Fecha: " + dt.getFecha("fecmov"));
                precio = dt.getDouble("precio");
//                }
              } 
              else
              {
                precio = (preStk * canStk) +
                    (kg * dt.getDouble("precio"));
                if ((precio >= 0.01 || precio <= -0.01) && (canti >= 0.01 || canti <= -0.01))
                  precio = precio / canti;
                else
                  precio=preStk;
              }
              if (!swPermCostosNegat)
              {
                if (precio<0)
                  precio=0;
              }
            }
        }

        if (tipMov.equals("-"))
        { // Es una salida, traspaso entre almacenes o Regularizacion
          cantiInd-= dt.getDouble("canti");
          swIgnVenta=false;
          if (sel=='V')
          { // Albaran de ventas.
            if (zonCodi!= null)
            {
              if (!dt.getString("zonCodi").toUpperCase().matches(zonCodi))
                  swIgnVenta=true;
            }
            if (repCodi != null)
            {
              if (!dt.getString("repCodi").toUpperCase().matches(repCodi))
                  swIgnVenta=true;
            }
            if (sbeCodi!=0 && dt.getInt("sbe_codi")!=sbeCodi)
                swIgnVenta=true;
          }

          if ((sel=='V' || sel=='R') && !swIgnVenta)
          { // Tener solo en cuenta Ventas y Regularizaciones.
            if (sel=='V' && dt.getInt("div_codi")<=0 && ! isRootAV())
              isRootAV(); // no lo muestro xq es albaran oculto.
            else
            {
              if (sel=='V')
              {
                  if (almCodi!=0 && dt.getInt("alm_coddes")!=almCodi && dt.getInt("alm_codori")!=almCodi )
                        continue; // No lo trato.
                  if (dt.getString("avc_serie").equals("X"))
                  { // Traspaso entre almacenes
//                    if (almCodi!=0 && dt.getInt("alm_coddes")!=almCodi && dt.getInt("alm_codori")!=almCodi )
//                        continue; // No lo trato.
                    swTraspAlm=true;
                  }
                  if (!swTraspAlm )
                  {
                    kgVen += dt.getDouble("canti");
                    prVen += dt.getDouble("canti", true) * dt.getDouble("precio", true);
                    unVent += dt.getDouble("unidades");
                    if (preStk==0)
                      msgLog+="Venta con precio stock = 0 "+
                               " EN Fecha: " + dt.getFecha("fecmov")+": Producto: "+proCodi+" \n";
                    impGana += dt.getDouble("canti", true) *
                    (dt.getDouble("precio", true) - preStk);
                  }
              }
              else
              { // Es regularizacion
                  if (dt.getString("repCodi").equals("VP") && dt.getInt("cliCodi")!=4 && dt.getInt("cliCodi")!=0)
                  { // Vertedero de proveedor y NO es NO reclamada ni PENDIENTE. No influye en Ganancias
                     msgLog+="Vertedero reclamado a proveedor "+dt.getDouble("canti", true)+
                                   " : EN Fecha: " + dt.getFecha("fecmov")+": Producto: "+proCodi+" \n";
                  }
                  else
                    impGana += dt.getDouble("canti", true) * (dt.getDouble("precio", true) - preStk);
              }
            }
          }
          if (swTraspAlm && almCodi==0)
          {
              canti=canStk;
              preStk=precio;
              uniStk=unid;
          // No influye en stock
          }
          else
          {
               if (swTraspAlm && almCodi != 0 &&   dt.getInt("alm_coddes")==almCodi )
               {
                   canti = canStk + dt.getDouble("canti",true);
                   unid = uniStk +  dt.getInt("unidades");
               }
               else
               {
//            if ( almCodi==0 || dt.getInt("alm_coddes")==almCodi )
//          {
                canti = canStk - dt.getDouble("canti",true);
                unid = uniStk - dt.getInt("unidades");
              }
//          }
          }
         }
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
          kgEntDes += dt.getDouble("canti",true);
          unEntDes +=dt.getInt("unidades",true);
          impEntDes+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
        }
        if (sel=='D')
        {
          kgSalDes += dt.getDouble("canti",true);
          unSalDes +=dt.getInt("unidades",true);
          impSalDes+=dt.getDouble("canti",true)*dt.getDouble("precio",true);
        }
      
        if (jt!=null && !swIgnVenta)
        {  // Meter datos para cuando la consulta es en pantalla y detallada en un grid
          if (sel == 'V' && ! swVerVenta)
            continue;
          if (sel=='C' && ! swVerCompra)
            continue;
          if (sel=='d' && !swVerDesEnt)
              continue;
          if (sel=='D' && !swVerDespSal)
          continue;
          if (sel=='R' && !swVerRegul)
            continue;
          if (sel=='V' && dt.getInt("div_codi")<=0 && ! isRootAV())
            continue;
          ArrayList v = new ArrayList();
          v.add(dt.getDate("fecmov"));
          if (tipMov.equals("+") || tipMov.equals("="))
          {
            if (sel=='D')
               v.add(Formatear.format(dt.getDouble("canti") * -1,
                                           "---,--9.99"));
            else
              v.add(Formatear.format(dt.getString("canti"),
                                            "---,--9.99"));
            v.add("");
          }
          else
          { // Salida
            if (swTraspAlm)
                 v.add(Formatear.format(dt.getString("canti"),
                                        "---,--9.99"));
            else
                v.add("");
            v.add(Formatear.format(dt.getString("canti"),
                                        "---,--9.99"));
          }
          if (dt.getString("tipmov").equals("="))
             v.add("IN");
          else
          {
             if(swTraspAlm)
                 v.add("TA");
             else
                v.add(dt.getString("sel"));
          }
          v.add(Formatear.format(dt.getString("precio"),
                                      "---,--9.99"));
          v.add(Formatear.format(uniStk, "---,--9"));
          v.add(Formatear.format(canStk, "---,--9.99"));
          v.add(Formatear.format(preStk+incCosto, "---,--9.99"));
          v.add(Formatear.format(impGana, "---,--9.99"));
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
                cliNomb = "("+dt.getString("avc_serie")+dt.getString("numalb")+
                     ")"+depos+
                     cliNomb;
                
             }
          }
          if (sel=='C')
          {
            if (dtStat.select("select prv_nomb from v_proveedo"+
                           " where prv_codi="+dt.getInt("cliCodi")))
            cliNomb = "("+dt.getString("numalb")+") "+ dtStat.getString("prv_nomb");
          }
          if (sel=='D' || sel=='d')
            cliNomb="N. Desp: "+dt.getString("numalb");
          if (sel=='R')
               v.add(dt.getString("zonCodi",true));
          else
            v.add(""+dt.getInt("cliCodi",true)+" "+cliNomb);
          v.add(dt.getString("ejenume")+"-"+dt.getString("empcodi")+"-"+
                  dt.getString("serie")+
                     dt.getString("lote")+"-"+dt.getString("numind") );
          if (sel=='V')
            v.add(Formatear.format(dt.getDouble("precio")-preStk,"---9.99"));
          else
            v.add("");
          v.add(dt.getInt("alm_codi"));
          jt.addLinea(v);
        }
      } while (dt.next());
      if (swDesglInd)
      {
        Iterator<String> pr = ht.keySet().iterator();
        while (pr.hasNext()) 
        {
              ref =  pr.next();
              cantiInd =  ht.get(ref);
//              if (cantiInd!=0)
//                System.out.println(ref+"|"+cantiInd);
        }
      }
                //System.out.println(ref+"|"+canti);
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
  public void resetAcumulados()
  {
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
      return prEnt;
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
 
  public int getUnidStock()
  {
    return     uniStk;
  }
  public double getKilosVenta()
  {
      return kgVen;
  }
  /**
   * @deprecated usar getImporteVenta
   * @return
   */
  public double getPrecioVenta()
  {
      return prVen;
  }
  public double getImporteVenta()
  {
      return prVen;
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
            ,int maxElemen) throws SQLException {

        String feulin;
        String s = "select distinct(rgs_fecha) as cci_feccon from v_regstock as r,v_motregu  as m "
                + " where r.emp_codi = " + empCodi
                + " and r.tir_codi = m.tir_codi "
                + " and M.tir_afestk='=' "
                + " order by cci_feccon desc ";

        if (dt.select(s)) {
            int nMax=1;
            feulin = dt.getFecha("cci_feccon", "dd-MM-yyyy");
            do {
                feulinE.addItem(dt.getFecha("cci_feccon", "dd-MM-yyyy"), dt.getFecha("cci_feccon", "dd-MM-yyyy"));
                nMax++;
                if (nMax>maxElemen && maxElemen > 0)
                    break;
            } while (dt.next());
        } else {
            feulin = "01-01-" + ejeNume; // Buscamos desde el principio del año.
            feulinE.addItem(feulin);
        }
        feulinE.setText(feulin);
        return feulin;
    }
    /**
     * Comprueba si un individuo ha tenido salidas a partir de una fecha
     * @param dt
     * @param proCodi Codigo de procuto. Si es cero se ignorara
     * @param empCod
     * @param ejeNume
     * @param serie
     * @param numpar
     * @param numind . Si es cero se ignorara.
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
