package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.camposdb.empPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.log4j.Logger;
/**
 *
 * <p>Título: actStkPart</p>
 * <p>Descripción: Clase encargada de actualizar el stock actual de TODOS los productos.
*  A traves de sus funciones se sumaran y restaran kilos a los tabla stkpart y articulos (acumulados)
*  </p>
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.1
 */
public class ActualStkPart
{
  private boolean actStkAutomatico=true; 
//  private boolean controlInv=false; // Busca stock sobre Control Inventario
  boolean checkUnid=true; // Indica si debe comprobar que no haya mas de una unidad en stock Partidas
  MvtosAlma mvtos=null;
  private HashMap<Integer,Double>  difProd=new HashMap() ;
  private double precAcu,cantiAcu,precStk;
  private boolean acepNeg=true;
  static Logger logger = Logger.getLogger(ventana.class.getName());
  private String camara;
  private int posFin;
  private DatosTabla dtAdd;
  private int empCodi=1;
  private double kilStk;
  private int unidStk;
  private double kilAlmac;
  private int unidAlmac;
  private boolean excep = true;
  public final static int CREAR_PREGUNTAR = 2;
  public final static int CREAR_NO = 0;
  public final static int CREAR_SI = 1;
  private ventana padre;
  private String usuario;
  private String strAccesos;
  
  public ActualStkPart(DatosTabla dtAdd, int empCodi)
  {
    this.dtAdd = dtAdd;
    this.empCodi = empCodi;
    this.usuario=dtAdd.getConexion().getUsuario();
    try
    {
        strAccesos=empPanel.getStringAccesos(dtAdd, usuario);
    } catch (SQLException ex)
    {
        java.util.logging.Logger.getLogger(ActualStkPart.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  /**
   * Inicia la clase.
   * @param dtAdd DatosTabla DatosTabla para select y Updates.
   * @param empCodi int Empresa sobre la que trabajaremos
   */
  public void iniciar(DatosTabla dtAdd,int empCodi) 
  {
    this.dtAdd = dtAdd;
    this.empCodi = empCodi;
    this.usuario=dtAdd.getConexion().getUsuario();
    try
    {
        strAccesos=empPanel.getStringAccesos(dtAdd, usuario);
    } catch (SQLException ex)
    {
        java.util.logging.Logger.getLogger(ActualStkPart.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void setEmpresa(int empCodi)
  {
    this.empCodi = empCodi;
  }

  public int getEmpresa()
  {
    return this.empCodi;
  }

  public boolean sumar(int ejeNume, String serie, int lote,
                       int numind, int proCodi,
                       int almCodi, double kilos, int unidades) throws SQLException
  {
    return sumar(ejeNume, serie, lote, numind, proCodi, almCodi, kilos,
                 unidades, null, CREAR_NO, 0, null);
  }
  /**
   * Suma los kilos mandados al individuo en cuestion
   * @param ejeNume
   * @param serie
   * @param lote
   * @param numind
   * @param proCodi
   * @param almCodi
   * @param kilos
   * @param unidades
   * @param fecCre Fecha en formato dd-MM-yyyy
   * @return true si se creo registro. False si habia q crear registro y se dijo de no crear.
   * @throws SQLException 
   */
  public boolean sumar(int ejeNume, String serie, int lote,
                       int numind, int proCodi,
                       int almCodi, double kilos, int unidades, Date fecCre) throws
      SQLException
  {
    return sumar(ejeNume, serie, lote, numind, proCodi, almCodi, kilos,
                 unidades, fecCre, CREAR_NO, 0, null);
  }

  /**
   * Indica si se debe lanzar una exception en caso de que se intente crear un movimiento
   * y no exista el registro de origen. <br>
   * Por defecto es 'true'
   *
   * @param excep boolean
   */
  public void setException(boolean excep)
  {
    this.excep = excep;
  }
  /**
   * Suma los kilos y unidades mandados a stock-partidas.
   * @deprecated
   * @param ejeNume
   * @param serie
   * @param lote
   * @param numind
   * @param proCodi
   * @param almCodi
   * @param kilos
   * @param unidades
   * @param fecMvto Fecha en formato dd-MM-yyyy
   * @param creaReg
   * @param prvCodi
   * @param fecCaduc
   * @return
   * @throws java.sql.SQLException 
   */
  public boolean sumar(int ejeNume, String serie, int lote, int numind,
                       int proCodi,
                       int almCodi, double kilos, int unidades, Date fecMvto,
                       int creaReg,
                       int prvCodi, java.util.Date fecCaduc) throws java.sql.SQLException
  {
    return sumar(ejeNume, serie, lote, numind, proCodi, almCodi, kilos,
                 unidades, fecMvto, creaReg, prvCodi, fecCaduc, excep,false);

  }
  
  public void setActStkAutomatico(boolean actStkAutomatico )
  {
      this.actStkAutomatico=actStkAutomatico;
  }
   public boolean getActStkAutomatico( )
  {
      return this.actStkAutomatico;
  }
  /**
   *  @deprecated 
   * Funcion encargada de sumar kilos y unidades al stock.
   * Actualiza las siguientes tablas:<br>
   * v_stkpart Para los kilos y unidades de ese individuo.<br>
   * v_stkpart Para los kilos y unidades del producto en el almacen mandado.<br>
   * v_articulo para el acumulados de ese producto.<br>
   *
   * @param ejeNume int Ejercicio del Lote
   * @param serie String Serie del Lote
   * @param lote int Numero de Lote
   * @param numind int Numero de Individuo
   * @param proCodi int Codigo de Producto
   * @param almCodi int Codigo de Almacen
   * @param kilos double Kilos a sumar
   * @param unidades int Unidades a Restar
   * @param fecMvto String Fecha de Movimiento (formato dd-MM-yyyy)
   * @param creaReg int CREAR_SI SÃ­,CREAR_NO No (dar error si no existe) CREAR_PREGUNTAR Preguntar.
   * @param prvCodi int Proveedor al que se le compro el producto
   * @param fecCaduc java.util.Date Fecha de Caducidad del Individuo
   * @param excep boolean Lanzar una exception si se deberia insertar un registro y creaReg
   * es false.
   * @param actual Indica si las cantidades son a poner (Inventario) no a sumar o restar.
   * @throws SQLException En caso error de base de datos.
   * @return true si todo va bien. False en caso de no generar el movimiento por intervencion
   * del usuario.
   */
  public boolean sumar(int ejeNume, String serie, int lote, int numind,
                       int proCodi,
                       int almCodi, double kilos, int unidades, Date fecMvto,
                       int creaReg,
                       int prvCodi, java.util.Date fecCaduc, boolean excep,boolean actual) throws
      java.sql.SQLException
  {
    if (actStkAutomatico)
        return true;
    return suma_regen(ejeNume,serie,lote,numind,proCodi,almCodi,kilos, unidades,fecMvto,creaReg,prvCodi,fecCaduc,excep,actual);
   }
  private boolean suma_regen(int ejeNume, String serie, int lote, int numind,
                       int proCodi,
                       int almCodi, double kilos, int unidades, Date fecMvto,
                       int creaReg,
                       int prvCodi, java.util.Date fecCaduc, boolean excep,boolean actual) throws
      java.sql.SQLException
  {
      
    int res;
    String s1;
    String stp_fefici=fecMvto == null ? Fecha.getFechaSys("dd-MM-yyyy") : Formatear.getFecha(fecMvto,"dd-MM-yyyy");
    String s = "UPDATE  stockpart set stp_kilact = "+(actual?"(":"stp_kilact + (")+kilos+")"+
        " ,stp_unact= "+(actual?"(":" stp_unact+ (")+unidades+")"+
        " ,stp_fefici = to_date('"+stp_fefici+"','dd-MM-yyyy')"  +
        " WHERE emp_codi = " + empCodi +
        " and eje_nume = " + ejeNume +
        " and pro_Serie = '" + serie + "'" +
        " and pro_nupar = " + lote +
        " and pro_numind = " + numind +
        " and pro_codi = " + proCodi +
        " and alm_codi = " + almCodi;
    if (dtAdd.executeUpdate(s)==0)
    {
      // Quiza el producto NO Tiene CONTROL DE STOCK POR Individuo
      s1 = "SELECT pro_coinst  from v_articulo where pro_codi = " + proCodi +
//          " and emp_codi = " + empCodi +
          " and pro_coinst != 0";
      if (dtAdd.select(s1) || actual)
      { // Pues si que tiene control de stock.
        String msgErr = "NO encontrado Registro en Stock para Producto:" +
            proCodi + " Lote: " + empCodi + "-" + ejeNume + "/" + serie + lote +
            " Ind: " + numind;

        if (creaReg == CREAR_PREGUNTAR)
        {
          res = mensajes.mensajePreguntar(msgErr + "\nContinuar?");
          if (res != mensajes.YES)
          {
            mensajes.mensajeAviso("Creacion de Regularizacion  ... ANULADO");
            return false;
          }
        }
        if (creaReg == CREAR_NO)
        {
          if (excep)
            throw new java.sql.SQLWarning(msgErr + "\n" + s);
          else
          {
            mensajes.mensajeUrgente("Creacion de Regularizacion ... ANULADO");
            return false;
          }
        }
        kilStk = kilos;
        unidStk = unidades;
        dtAdd.addNew("stockpart");
        dtAdd.setDato("eje_nume", ejeNume);
        dtAdd.setDato("emp_codi", empCodi);
        dtAdd.setDato("pro_serie", serie);
        dtAdd.setDato("stp_tiplot", "P");
        dtAdd.setDato("pro_nupar", lote);
        // dtAdd.setDato("stk_nlipar", numind);
        dtAdd.setDato("pro_codi", proCodi);
        dtAdd.setDato("pro_numind", numind);
        dtAdd.setDato("alm_codi", almCodi);
        dtAdd.setDato("stp_unini", unidStk);
        if (fecMvto==null)
        {
            dtAdd.setDato("stp_feccre","current_timestamp");
            dtAdd.setDato("stp_fefici","current_timestamp" );
        }
        else
        {
            dtAdd.setDato("stp_feccre",      fecMvto );
            dtAdd.setDato("stp_fefici",     fecMvto);
        }
        dtAdd.setDato("stp_kilini", kilStk);
        dtAdd.setDato("stp_kilact", kilStk);
        dtAdd.setDato("stp_unact", unidStk);
        if (prvCodi > 0)
          dtAdd.setDato("prv_codi", prvCodi);
        if (fecCaduc != null)
          dtAdd.setDato("stp_feccad", fecCaduc);
        dtAdd.update();
      }
    }
    else
    {
      if (checkUnid)
      {
          // Primero compruebo si el producto es vendible
        if (MantArticulos.getTipoProd(proCodi, dtAdd).equals(MantArticulos.TIPO_VENDIBLE))
        { // Si es vendible compruebo si tiene mas de un individuo.
            s="select stp_unact from v_stkpart "+
                " WHERE emp_codi = " + empCodi +
                " and eje_nume = " + ejeNume +
                " and pro_Serie = '" + serie + "'" +
                " and pro_nupar = " + lote +
                " and pro_numind = " + numind +
                " and pro_codi = " + proCodi +
                " and alm_codi = " + almCodi+
                " and stp_unact > 1";
            if (dtAdd.select(s))
            {
                   Logger.getLogger(ActualStkPart.class.getName()).error("Stock Partidas con mas de un individuo\n"+s+"\n"+ventana.getCurrentStackTrace());
                   HashMap ht=new HashMap();  
                   ht.put("%a",almCodi);
                   ht.put("%p",proCodi);
                   ht.put("%i",ejeNume+serie+lote+"-"+numind);
                   Principal.guardaMens(dtAdd, "A1",ht,"","");
            }
        }
      }
    }
    if (actual)
      return true;
    return true; //actAcum(proCodi, almCodi, kilos, unidades, fecMvto,false);
  }
  /**
   * Actualizar acumulados (Productos y registro general en stockPartidas)
   * @param proCodi
   * @param almCodi
   * @param kilos
   * @param unidades
   * @param fecMvto
   * @return
   * @throws java.sql.SQLException 
   */
//  public boolean actAcum(int proCodi, int almCodi, double kilos, int unidades,
//                         String fecMvto) throws java.sql.SQLException
//  {
//    return actAcum(proCodi,almCodi,kilos,unidades,fecMvto,false);
//  }

  /**
   * Actualizo Stock sobre el producto y almacen para la empresa dada.
   * Actualiza la tabla v_stkpart con los datos mandados y los acumulados
   * de la tabla productos
   * 
   * @param proCodi int
   * @param almCodi int
   * @param kilos double Kilos a sumar
   * @param unidades int Unidades a sumar
   * @param fecMvto String
   * @param actual Indica si no es acumular sino actualizar a la cantidad mandada
   *               True es Machacar. False es actualizar (suma a lo ya existente)
   * @throws SQLException Error en DB
   * @return boolean
   */
  private void actAcum(int proCodi, int almCodi, double kilos, int unidades,
                         Date fecMvto,boolean actual) throws java.sql.SQLException
  {


    String fefici=fecMvto == null ? Fecha.getFechaSys("dd-MM-yyyy") : Formatear.getFecha(fecMvto,"dd-MM-yyyy");
    String s="UPDATE actstkpart SET stp_kilact = "+(actual?"(":"stp_kilact + (")+kilos+")"+
        " ,stp_unact= "+(actual?"(":"stp_unact + (")+unidades +")"+
        " ,stp_fefici = to_date('"+fefici+"','dd-MM-yyyy')"+
            " WHERE pro_codi = " + proCodi +
            " and alm_codi = " + almCodi ;
    if (dtAdd.executeUpdate(s)==0)
    { // No lo ha modificado. Creo el registro
      kilAlmac = kilos;
      unidAlmac = unidades;
      dtAdd.addNew("actstkpart"); 
      dtAdd.setDato("pro_codi", proCodi);
      dtAdd.setDato("alm_codi", almCodi);
      if (fecMvto == null )
      {
          dtAdd.setDato("stp_feccre","current_timestamp");
          dtAdd.setDato("stp_fefici", "current_timestamp");
      }
      else
      {
        dtAdd.setDato("stp_feccre",  fecMvto);
        dtAdd.setDato("stp_fefici", fecMvto);
      }
      dtAdd.setDato("stp_kilact", kilAlmac);
      dtAdd.setDato("stp_unact", unidAlmac);
      dtAdd.update();
    }
  }
/**
 * @deprecated
 * @param ejeNume
 * @param serie
 * @param lote
 * @param numind
 * @param proCodi
 * @param almCodi
 * @param kilos
 * @param unidades
 * @param fecMvto
 * @return
 * @throws SQLException 
 */
  public boolean restar(int ejeNume, String serie, int lote,
                        int numind, int proCodi,
                        int almCodi, double kilos, int unidades, Date fecMvto) throws   SQLException
  {

    return sumar(ejeNume, serie, lote, numind, proCodi, almCodi, kilos * -1,
                 unidades * -1, fecMvto, CREAR_NO, 0, null, true,false);
  }
/**
 * @deprecated
 * @param ejeNume
 * @param serie
 * @param lote
 * @param numind
 * @param proCodi
 * @param almCodi
 * @param kilos
 * @param unidades
 * @return
 * @throws SQLException 
 */
  public boolean restar(int ejeNume, String serie, int lote,
                        int numind, int proCodi,
                        int almCodi, double kilos, int unidades) throws  SQLException
  {
    return restar(ejeNume, serie, lote, numind, proCodi, almCodi, kilos,
                  unidades, null);
  }
  /**
   * Carga las variables unidStk y kilStk con los kilos actuales en la tabla de stok-partidas
   * @param empCodi int
   * @param ejeNume int
   * @param serie String
   * @param lote int
   * @param numind int
   * @param proCodi int
   * @param almCodi int
   * @throws SQLException
   * @return boolean false si no encontrado registro en stock partidas.
   */
  public boolean verKilos(int empCodi, int ejeNume, String serie, int lote,
                          int numind, int proCodi,
                          int almCodi) throws java.sql.SQLException
  {
    return verKilos(dtAdd, empCodi, ejeNume, serie, lote, numind, proCodi,
                    almCodi);

  }
  /**
   * @deprecated usar la funcion sin mandar empresa.
   * @param dt
   * @param empCodi
   * @param ejeNume
   * @param serie
   * @param lote
   * @param numInd
   * @param proCodi
   * @param almCodi
   * @return
   * @throws java.sql.SQLException 
   */
   public boolean verKilos(DatosTabla dt, int empCodi, int ejeNume, String serie,
                          int lote,
                          int numInd, int proCodi,
                          int almCodi) throws java.sql.SQLException
   {
       return verKilos(dt,ejeNume,serie,lote,numInd,proCodi,almCodi);
   }
  /**
   * Carga las variables unidStk y kilStk con los kilos actuales en la tabla de stok-partidas
   * @param dt DatosTabla
   * @param empCodi int
   * @param ejeNume int
   * @param serie String
   * @param lote int
   * @param numind int
   * @param proCodi int
   * @param almCodi int
   * @throws SQLException
   * @return boolean false si no encontrado registro en stock partidas.
   */
  public boolean verKilos(DatosTabla dt,  int ejeNume, String serie,
                          int lote,
                          int numind, int proCodi,
                          int almCodi) throws java.sql.SQLException
  {
    String s = "SELECT * FROM v_stkpart where emp_codi = " + empCodi +
        " and eje_nume = " + ejeNume +
        " and pro_serie = '" + serie + "'" +
        " and pro_nupar = " + lote +
        " and pro_numind = " + numind +
        " and pro_codi = " + proCodi +
        " and alm_codi = " + almCodi;

    if (!dt.select(s))
    {
      unidStk = 0;
      kilStk = 0;
      return false;
    }
    kilStk = dt.getDouble("stp_kilact");
    unidStk = dt.getInt("stp_unact");
    return true;
  }

  public double getKilosStk()
  {
    return kilStk;
  }

  public int getUnidStk()
  {
    return unidStk;
  }

  public int anuStkPart(int proCodi, int ejeLot, String serLot, int numLot,
                        int nInd, int almCodi, double kilos, int unid) throws
      SQLException
  {
    return anuStkPart(proCodi, ejeLot, empCodi, serLot, numLot, nInd, almCodi,
                      kilos, unid);
  }
  public static boolean isBloqueado(DatosTabla dt,int proCodi, int ejeLot, int empLot, String serLot,
                        int numLot,
                        int nInd, int almCodi) throws SQLException
  {
      return dt.select("SELECT * FROM v_stkpart WHERE stk_block != 0 "+
        " AND eje_nume = " + ejeLot +
        " AND emp_codi =  " + empLot +
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot +
        " and pro_numind = " + nInd +
        " and pro_codi = " + proCodi +
        " and alm_codi = " + almCodi);
              
  }
  /**
   * Establece un registro de stock partidas como bloqueado
   * @param proCodi
   * @param ejeLot
   * @param empLot
   * @param serLot
   * @param numLot
   * @param nInd
   * @param almCodi
   * @param bloquea true -> lo bloquea. False lo desbloquea
   * @return false si no encuentra el registro.
   * @throws SQLException
   */
   public boolean setBloqueo(int proCodi, int ejeLot, int empLot, String serLot,
                        int numLot,
                        int nInd, int almCodi, boolean bloquea) throws SQLException
  {
     String s="UPDATE stockpart set stk_block ="+(bloquea?-1:0)+
        " WHERE eje_nume = " + ejeLot +
        " AND emp_codi =  " + empLot +
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot +
        " and pro_numind = " + nInd +
        " and pro_codi = " + proCodi +
        " and alm_codi = " + almCodi;
     int nRowAf=dtAdd.executeUpdate(s);
     return nRowAf!=0;
  }
   /**
    * Establece el stock de un individuo a los kilos y unidades mandados.
    * @param proCodi
    * @param ejeLot
    * @param serLot
    * @param numLot
    * @param nInd
    * @param almCodi
    * @param kilos
    * @param unid
    * @return int 1 Registro NO existia y lo ha creado.<BR>
   *  0 SI ACTUALIZA LOS KILOS.
   * 
    * @throws SQLException
    */
  public void ponerStock  (int proCodi, int ejeLot, int empLot, String serLot,
                        int numLot,
                        int nInd, int almCodi, double kilos, int unid) throws SQLException
  {
    sumar(ejeLot,serLot,numLot,nInd,proCodi,almCodi,kilos,unid,null,CREAR_SI,0,null,false,true);
  }
  /**
   *
   * Anular el Stock creado. Esta rutina difiere de sumar en que el caso
   * de que el registro quede con kilos = 0 y unidades = 0 en vez de actualizar la
   * tabla 'v_stkpart'  BORRA el registro (tambien actualizando acumulados)
   * @deprecated
   * @param proCodi int
   * @param ejeLot int
   * @param empLot int
   * @param serLot String
   * @param numLot int
   * @param nInd int
   * @param almCodi int
   * @param kilos double
   * @param unid int
   * @throws SQLException
   * @return int 2 Si EL REGISTRO HA SIDO BORRADO.<BR>
   * 1 SI ACTUALIZA LOS KILOS. 
   * 0 SI NO ENCUENTRA EL REGISTRO en stock Partidas
   * 3 Si el producto no tiene control stock o es de comentario
   */
  public int anuStkPart(int proCodi, int ejeLot, int empLot, String serLot,
                        int numLot,
                        int nInd, int almCodi, double kilos, int unid) throws SQLException
  {
    if (actStkAutomatico) return 2;
    String s = "SELECT * FROM v_stkpart WHERE eje_nume = " + ejeLot +
        " AND emp_codi =  " + empLot +
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot +
        " and pro_numind = " + nInd +
        " and pro_codi = " + proCodi +
        " and alm_codi = " + almCodi;

    if (!dtAdd.select(s, true))
    {
      String s1 = "SELECT pro_coinst,pro_tiplot  from v_articulo where pro_codi = " + proCodi;
//          " and emp_codi = " + empCodi +
         
      if (!dtAdd.select(s1))
        return 1; // No encontrado articulo
      else
      {
        if (dtAdd.getInt("pro_coinst")==0)
            return 3; // No tiene control por individuos. Todo bien.
        if (! dtAdd.getString("pro_tiplot").equals("V"))
            return 3;
        else
        {
            Logger.getLogger(ActualStkPart.class.getName()).error("Sin stock Partidas\n"+s+"\n"+ventana.getCurrentStackTrace());
            HashMap ht=new HashMap();  
            ht.put("%a",almCodi);
            ht.put("%p",proCodi);
            ht.put("%i",ejeLot+serLot+numLot+"-"+nInd);
            Principal.guardaMens(dtAdd, "A2",ht,"",""); 
            return 1;
        }
      }
    }
    int unAnt= dtAdd.getInt("stp_unact");
    double kilAnt=dtAdd.getDouble("stp_kilact");
    unid = unAnt - unid;
    kilos = kilAnt - kilos;
    if (unid == 0 && kilos == 0)
    {
      dtAdd.executeUpdate("DELETE FROM v_stkpart WHERE " + dtAdd.getCondWhere());
//      actAcum(proCodi, almCodi, kilAnt*-1, unAnt*-1, null);
      return 2;
    }
    dtAdd.edit(dtAdd.getCondWhere());
    dtAdd.setDato("stp_unact", unid);
    dtAdd.setDato("stp_kilact", kilos);
    dtAdd.update();
//    actAcum(proCodi, almCodi, kilAnt*-1, unAnt*-1, null);
    return 1;
  }
  /**
   * @deprecated
   * Cambia una referencia por otra en StockPartidas
   * @param dt
   * @param proCodi
   * @param proCodAnt
   * @param ejeLot
   * @param serLot
   * @param numLot
   * @param nInd
   * @param almCodi
   * @return
   * @throws SQLException 
   */
  private int cambiaProd(DatosTabla dt,int proCodi, int proCodAnt, int ejeLot,
                        String serLot, int numLot,
                        int nInd, int almCodi) throws SQLException
  {
    return cambiaProd(dt,proCodi,proCodAnt,ejeLot,empCodi,serLot,numLot,nInd,almCodi);
  }
  /**
   *  @deprecated
   * Rutina utilizada para sustituir un producto por otro en movimientos de stock.
   *
   * @param dt DatosTabla
   * @param proCodi int
   * @param proCodAnt int
   * @param ejeLot int
   * @param empLot int
   * @param serLot String
   * @param numLot int
   * @param nInd int
   * @param almCodi int
   * @throws SQLException
   * @return int
   */
  private int cambiaProd(DatosTabla dt,int proCodi, int proCodAnt, int ejeLot, int empLot,
                        String serLot, int numLot,
                        int nInd, int almCodi) throws SQLException
  {
    String s = "SELECT *  FROM v_stkpart "+
        " WHERE eje_nume = " + ejeLot+
        " AND emp_codi =  " + empLot +
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot +
        " and pro_numind = " +nInd +
        " and pro_codi = " + proCodAnt +
        " and alm_codi = " + almCodi;
    if (dt.select(s))
    {
//      actAcum(proCodAnt,almCodi,dt.getDouble("stp_kilact")*-1,dt.getInt("stp_unact")*-1,null);
//      actAcum(proCodi,almCodi,dt.getDouble("stp_kilact"),dt.getInt("stp_unact"),null);
      s = "UPDATE v_stkpart SET pro_codi = " + proCodi +
          " WHERE eje_nume = " + ejeLot +
          " AND emp_codi =  " + empLot +
          " and pro_serie = '" + serLot + "'" +
          " and pro_nupar = " + numLot +
          " and pro_numind = " + nInd +
          " and pro_codi = " + proCodAnt +
          " and alm_codi = " + almCodi;
      return dtAdd.executeUpdate(s);
    }
    return 0;
  }
  /**
   * Comprueba si hay suficiente stock para crear un movimiento.
   *
   * @param dt DatosTabla
   * @param proCodi int
   * @param ejeLot int
   * @param empLot int
   * @param serLot String
   * @param numLot int
   * @param nInd int
   * @param almCodi int
   * @param kilos int
   * @param unids int
   * @throws SQLException
   * @return boolean TRUE si hay stock en kilos y unidades suficientes.
   */
  public static boolean checkStock(DatosTabla dt, int proCodi, int ejeLot,int empLot,
                            String serLot, int numLot,
                            int nInd, int almCodi, double kilos, int unids) throws SQLException
  {
    if (! checkIndiv(dt,proCodi,ejeLot,empLot,serLot,numLot,nInd,almCodi,false))
        return false;
    if ( Formatear.redondea(kilos,3) > Formatear.redondea(dt.getDouble("stp_kilact"),3) && kilos != 0)
      return false;
    return unids <= dt.getDouble("stp_unact") || unids == 0;
  }
  
  /**
   * Comprueba si existe el registro en Stock-Partidas.
   * @param dt
   * @param proCodi
   * @param ejeLot
   * @param serLot
   * @param numLot
   * @param nInd
   * @param almCodi
   * @return false si no existe
   * @throws SQLException 
   */
  public static boolean checkIndiv(DatosTabla dt, int proCodi, int ejeLot,
                            String serLot, int numLot,
                            int nInd) throws SQLException
  {
     return checkIndiv(dt,proCodi,ejeLot,1,serLot,numLot,nInd,0,false);
  }
  
  /**
   * Comprueba si existe un individuo en stock Partidas
   * @param dt
   * @param proCodi
   * @param ejeLot
   * @param empLot
   * @param serLot
   * @param numLot
   * @param nInd
   * @param almCodi
   * @param lock bloquear registro
   * @return true si encuentra el registro en stock partidas.
   * @throws SQLException 
   */
  public static boolean checkIndiv(DatosTabla dt, int proCodi, int ejeLot,int empLot,
                            String serLot, int numLot,
                            int nInd, int almCodi,boolean lock) throws SQLException
  {
    String s = "SELECT *  FROM stockpart " +
        " WHERE eje_nume = " + ejeLot +      
        (empLot==0?"":" and emp_codi = "+empLot)+
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot +
        " and pro_numind = " + nInd +
        " and pro_codi = " + proCodi +
        (almCodi!=0?" and alm_codi = " + almCodi:"");
     return dt.select(s,lock);      
  }
  
  public static Timestamp getFechaUltMvt(DatosTabla dt, int proCodi, int ejeLot,int empLot,
                            String serLot, int numLot,
                            int nInd, int almCodi) throws SQLException
  {
      if (!checkIndiv(dt,proCodi,ejeLot,empLot,serLot,numLot,nInd,almCodi,false))
          return null;
      return dt.getTimeStamp("stp_fefici");
  }
  private String getCampoLlave(String llave, int posIni)
  {
    posFin = llave.indexOf("|", posIni);
    if (posFin < 0)
      return llave.substring(posIni);
    else
      return llave.substring(posIni, posFin++);
  }
  
  private String getSqlMvtInvControl(int proArtcon, int almCodi,Date fecinv, Date fecsup) throws ParseException
  {
      return getSqlMvt(true,proArtcon,almCodi,fecinv,fecsup,0,true);
  }

//  public static String getSqlMvtSalida(boolean swFecMvto, int almCodi, String fecinv, String fecsup,int pro_codi,String condProd)
//  {
//     return " select 'V' as sel,'-' as tipmov,"+
//         (swFecMvto?"l.avl_fecalt": "c.avc_fecalb")+" as fecmov," +
//        "  i.avp_serlot  as serie,i.avp_numpar as  lote," +
//        " i.avp_canti  as canti,i.avp_numind as numind," +
//        " i.avp_ejelot as ejeNume,i.avp_emplot as empcodi,l.pro_codi as pro_codi, " +
//        " l.avl_prven as precio,avp_numuni as canind,  " +
//        " c.alm_codori as almori, c.alm_coddes as almfin,c.avc_serie as avc_serie " +
//        "  from v_albavel l, v_albavec c,v_albvenpar i, v_articulo as a " +
//        " where c.emp_codi = l.emp_codi " +
//        " AND c.avc_serie = l.avc_serie " +
//        " AND c.avc_nume = l.avc_nume " +
//        " and c.avc_ano = l.avc_ano " +
//        (almCodi==0?"":" and c.alm_codori = " + almCodi) +
//        " and l.avl_canti <> 0 " +
//        " and i.emp_codi = l.emp_codi " +
//        " AND i.avc_serie = l.avc_serie " +
//        " AND i.avc_nume = l.avc_nume " +
//        " and i.avc_ano = l.avc_ano " +
//        " and i.avl_numlin = l.avl_numlin " +
//        (pro_codi == 0 ? "" : " and l.pro_codi = " + pro_codi) +
//        " and a.pro_codi = l.pro_codi " +
//        //" and a.pro_activ != 0 "+
//        " and a.pro_tiplot = 'V' "+
//        condProd +
//        (fecsup==null?"":" AND "+
//        (swFecMvto?"l.avl_fecalt": "c.avc_fecalb")+
//        "<= TO_DATE('" + fecsup + "','dd-MM-yyyy') ")+
//        " AND "+
//        (swFecMvto?"l.avl_fecalt": "c.avc_fecalb")+
//        " > TO_DATE('" + fecinv + "','dd-MM-yyyy') "+
//        " UNION all " + // Despieces (Salidas de Stock)
//        " select 'D' as sel,'-' as tipmov,"+
//        (swFecMvto?"deo_tiempo": "deo_fecha")+" as fecmov," +
//        "  deo_serlot as serie,pro_lote as  lote," +
//        " deo_kilos as canti,pro_numind as numind," +
//        " deo_ejelot as ejeNume," +
//        " 1 as empcodi,l.pro_codi as pro_codi, " +
//        " deo_prcost as precio,1 as canind, " +
//        " deo_almori as almori,deo_almori as almfin, '' as avc_serie" +
//        " from  v_despori as l,v_articulo as a  " +
//        " where deo_kilos <> 0 " +
//        " and a.pro_tiplot = 'V' "+
//         (pro_codi == 0 ? "" : " and l.pro_codi = " + pro_codi) +
//        (almCodi==0?"":" and deo_almori = " + almCodi) +
//        " and a.pro_codi = l.pro_codi " +
//        condProd +
//        (fecsup==null?"":" AND "+  (swFecMvto?"deo_tiempo": "deo_fecha")+
//        " <= TO_DATE('" + fecsup + "','dd-MM-yyyy') ")+
//        " AND "+ (swFecMvto?"deo_tiempo": "deo_fecha")+
//        " > TO_DATE('" + fecinv + "','dd-MM-yyyy') ";
//  }
  /**
   * Devuelve sentencia SQL para buscar movimientos de un producto o varios.
   * @param swFecMvto Coger como fechas las de mvto (True) . En caso contrario coge las de los documentos.
   * @param proArtcon Tratamiento Articulos congelados. 2 Todos (Frescos y congelados),
   *                  0 solo frescos. Diferente de 0 Solo congelado.
   * @param almCodi Almacen de donde sacar los movimientos. (0 Todos)
   * @param fecinv Fecha de Inventario (Tambien es la fecha Inicial)
   * @param fecsup Fecha limite para buscar movimientos  
   * @param pro_codi Producto (0 Todos)
   * @param swControlInv Coge como inventario inicial uno de las tablas de control.
   * @return Sentencia SQL
   */
  private String getSqlMvt(boolean swFecMvto,int proArtcon, int almCodi, Date fecinv, Date fecsup,int pro_codi,boolean swControlInv) 
      throws ParseException
  {
    String condProd = "";
    if (proArtcon != 2)
      condProd = " and a.pro_artcon " + (proArtcon == 0 ? "= 0" : " <> 0");
    
    Date fecIni=fecinv;
    Date fecFin=fecsup;
    if (fecsup!=null)
    {
        if (Formatear.comparaFechas(fecsup, fecinv)<0)   
        {
           fecIni=fecsup;
           fecFin=fecinv;
        }
    }
    
    String s= "SELECT 2 as orden,mvt_tipo as tipmov,  "
                + " m.pro_codi, "
                + "  pro_serlot as serie,pro_numlot as lote,"
                + " pro_ejelot as ejeNume,"   
                + " pro_indlot as numind, "                                    
                + " m.alm_codi  as alm_codi,  "
                + " sum(mvt_canti) as canti,"
                + " sum(mvt_unid) as canind  " 
                + " from mvtosalm as m, v_articulo as a"
                + " where a.pro_codi=m.pro_codi  " 
                + condProd
                 + " and   mvt_canti <> 0 "            
//                + (almCodi == 0 ? "" : " and m.alm_codi = " + almCodi)                           
                + (pro_codi != 0 ? " and m.pro_codi = " + pro_codi : "")
                +  (almCodi != 0 ? " and alm_codi = "+almCodi:"")
                + " AND mvt_time::date > TO_DATE('" + Formatear.getFecha(fecIni,"dd-MM-yyyy") + "','dd-MM-yyyy') "
                + (fecFin==null?"":" and mvt_time::date <= TO_DATE('" + Formatear.getFecha(fecFin,"dd-MM-yyyy") +
                    "','dd-MM-yyyy') ")+
               " group by mvt_tipo,alm_codi,m.pro_codi,pro_ejelot,pro_serlot,pro_numlot,pro_indlot";
               
              
    if (swControlInv)
        s += " UNION all " + // Inventario de Control
        " select 1 as orden,  '=' as tipmov, " +
        " r.pro_codi as pro_codi,"+
        "  r.prp_seri as serie,r.prp_part as  lote," +        
        "  r.prp_ano as ejeNume," +       
        " r.prp_indi as numind, "+            
        " alm_codi as alm_codi, " +
        " r.lci_peso as canti, " +
        " lci_numind as canind  " +   
        " FROM v_coninvent as r,v_articulo as a " +
        " where lci_peso > 0 " +
        " and lci_regaut = 0 "+
        " and lci_depos = 0 "+
        (almCodi==0?"":" and alm_codi = " + almCodi) +
        (pro_codi == 0 ? "" : " and r.pro_codi = " + pro_codi) +
         " and a.pro_codi = r.pro_codi " +
        condProd +
        " AND r.cci_feccon = TO_DATE('" + Formatear.getFecha(fecinv,"dd-MM-yyyy") + "','dd-MM-yyyy') ";
    else
        s += " UNION all " + // Inventario Normal
            " select 1 as orden,  '=' as tipmov, " +
            " r.pro_codi as pro_codi, " +
            " r.pro_serie as serie,r.pro_nupar as  lote," +
            "  r.eje_nume as ejeNume," +
            " r.pro_numind as numind,"+
            " alm_codi as alm_codi,  " +
            " r.rgs_kilos as canti, " +                       
            "  rgs_canti as canind  " +
            " FROM v_inventar r,v_articulo as a " +
            " where r.emp_codi in ("+strAccesos+")"+
            " and rgs_kilos <> 0 " +
//            " and r.rgs_trasp != 0 "+
            (pro_codi == 0 ? "" : " and r.pro_codi = " + pro_codi) +
            (almCodi==0?"":" and alm_codi = " + almCodi) +
            " and a.pro_codi = r.pro_codi " +
            condProd +
            " AND r.rgs_fecha::date = TO_DATE('" + Formatear.getFecha(fecinv,"dd-MM-yyyy") + "','dd-MM-yyyy') ";
    s+= " order by 3,1"; // Ordenado por producto
    return s;
  }
   public void setVentana(ventana papa)
   {
     padre=papa;
   }
   private void mensajeErr(String msg)
   {
     mensajeErr(msg,true);
   }
   private void mensajeErr(String msg,boolean sonido)
   {
     if (padre==null)
       return;
     padre.mensajeErr(msg,sonido);
   }
   private void setMensajePopEspere(String msg)
   {
     if (padre==null)
       return;
     padre.setMensajePopEspere(msg,false);
   }
   public void setCamara(String camara)
   {
     this.camara=camara;
   }
   public String getCamara()
   {
     return this.camara;
   }

   public boolean regeneraStock(DatosTabla dt,int proArtcon, int almCodi, Date fecinv) throws
       Exception
   {
     return regeneraStock(dt,proArtcon,almCodi,fecinv,0);
   }
/**
 * Regenera Stock
 * @param dt
 * @param proArtcon Articulo COngelado  (2 Ambos, -1 SI, 0 No)
 * @param almCodi Almacen
 * @param fecinv Fecha Inventario
 * @param pro_codi Producto
 * @return false si no hay registros a actualizar
 * @throws Exception
 */
   public boolean regeneraStock(DatosTabla dt, int proArtcon, int almCodi, Date fecinv,
                                int pro_codi) throws Exception
   {
     return regeneraStock(dt, proArtcon, almCodi, fecinv, pro_codi,true);

   }
   /**
    * Devuelve registros para meter en tablas control stock
    * @param dt DatosTabla 
    * @param proArtcon Articulo Congelado?. 0=No -1= Si.
    * @param fecControl  Fecha Control Original
    * @param fecFinal  Fecha final de control
    * @return HashMap  con los registros de stocks a  la fecha dada.
    * @throws java.sql.SQLException
    */
   public HashMap getStockControl(DatosTabla dt,int proArtcon,int almCodi,Date fecControl,Date fecFinal) throws SQLException
   {
     String s;
     boolean restar=false;
     try {
           if (Formatear.comparaFechas(fecFinal, fecControl)<0)   
               restar=true;
         s= getSqlMvtInvControl(proArtcon, almCodi,fecControl,fecFinal);
     } catch (ParseException k1)
     {
         throw new SQLException("Error al montar select ",k1);
     }
     
     if (!dt.select(s))
       return null;
     
     return getStockInd0(dt,fecControl,restar);
    
   }
   /**
    * Devuelve un HashMap con los individiuos y su stock desde una fecha inicio
    * (de inventario) hasta la fecha actual
    * @param dt DatosTabla
    * @param proArtcon int Es Articulo Congelado (2 Ambos, -1 SI, 0 No)
    * @param almCodi Almacen (0 Todos)
    * @param fecinv Fecha Inventario. Se considera que los inventarios se hacen a final del dia.
    * @param pro_codi Producto (0 Todos) 
    * @param swIncProd Si es true llenara el Hash difProd con los diferentes productos
    *                   encontrados  
    * @para swInvControl Usar inventario control
    * @return HashMap con los diferentes individuos encontrados
    * @throws Exception
    */
   private HashMap getStockInd(DatosTabla dt,int proArtcon, int almCodi,
                                Date fecinv,Date fecsup,int pro_codi,
                                boolean swIncProd,boolean swInvControl) throws SQLException
   {

     if (swIncProd)
         difProd.clear();
     String s;    
     boolean restar=false;
     try {
        if (fecsup!=null)
        {
            if (Formatear.comparaFechas(fecsup, fecinv)<0)   
                   restar=true;         
        }
        s = getSqlMvt(true,proArtcon, almCodi, fecinv,fecsup,pro_codi,swInvControl);
     } catch (ParseException k1)
     {
         throw new SQLException("Error al montar select ",k1);
     }
     
     if (!dt.select(s))
       return null;
     
     return getStockInd0(dt,fecinv,restar);
   }
   /**
    * Carga en un hashmap el stock por individuos   
    * @param dt DatosTabla con los datos ya cargados @see getSqlMvt
    * @param fecinv Fecha Inventario
    * @param ignInvSup Ignorar inventarios superior a la fecha inventario
    * @param almCodi Almacen
    * @return HashMap
    * @throws SQLException Carga
    */
   private HashMap getStockInd0(DatosTabla dt,Date fecinv,boolean restar) throws SQLException
   {
     HashMap<String,String> ht = new HashMap();
     int n = 0;
     String tipMov, llave;
//     String feulst = fecinv;
     double canti;
     int numuni;
     String o;
     do
     {
       n++;
       if (n % 10 == 0)
         setMensajePopEspere("Buscando stocks de Individuos. Tratando Producto: " +
                    dt.getString("pro_codi"));
       tipMov = dt.getString("tipmov");
       llave = dt.getString("pro_codi") + "|" +
           dt.getInt("ejenume") + "|" +
           dt.getString("serie") + "|" + dt.getInt("lote") + "|" +
           dt.getInt("numind", true)+"|"+dt.getInt("alm_codi");
       difProd.put(dt.getInt("pro_codi"),(double) 0);

       if (tipMov.equals("="))
       { // Registro de Inventario      
         tipMov="E";
       }
       if (restar)
             tipMov=tipMov.equals("E")?"S":"E";

       canti=dt.getDouble("canti")*(tipMov.equals("S")?-1:1);
       numuni=dt.getInt("canind")*(tipMov.equals("S")?-1:1);
       if ( (o = ht.get(llave)) != null)
       {
           canti = Double.parseDouble(getCampoLlave(o, 0))+canti;
           numuni = Integer.parseInt(getCampoLlave(o, posFin))+numuni;
       }
       ht.put(llave,canti + "|" + numuni);
     }  while (dt.next());
     return ht;
}
    public boolean regeneraStock(DatosTabla dt,int proArtcon, int almCodi,
                                Date fecinv,int pro_codi,boolean resetear) throws Exception
   {
          return regeneraStock(dt, proArtcon, almCodi, fecinv, pro_codi, resetear,false);
   }
   /**
    * Regenera Stock volviendo a acumular las entradas/Salidas/Regularizaciones, etc.
    * disponibles. Actualiza Tabla v_stkpart y articulos.
    * 
    * @param dt DatosTabla Cursor Temporal
    * @param proArtcon int Es Articulo Congelado (2 Ambos, -1 SI, 0 No)
    * @param almCodi int Almacen sobre el que regenerar Stock. 0=Todos
    * @param fecinv String Fecha de Inventario  
    * @param pro_codi Producto a regenerar (0 = todos)
    * @param resetear Poner todos los acumulados a 0
    * @param swInvControl Renerar sobre Inventario Control
    * @throws Exception  En caso error de base de datos.
    * @return boolean false si no hay registros a actualizar
    */
   public boolean regeneraStock(DatosTabla dt,int proArtcon, int almCodi,
                                Date fecinv,int pro_codi,boolean resetear,boolean swInvControl) throws Exception
   {
     HashMap ht =getStockInd(dt,proArtcon,almCodi,fecinv,null,pro_codi,true,swInvControl);
     if (ht==null)
         return false;
     
     int n;
     // Pongo acumulados de stock-partidas a 0 para el almacen recibido
     // como parametro.
     String s;
     dtAdd.executeUpdate("update ajustedb set aju_regacu=0"); // Deshabilito Reg. Acum.
     s = "UPDATE stockpart set stp_unact = 0,stp_kilact= 0 " +
         " where (stp_unact != 0 or stp_kilact != 0) " +
         (almCodi==0?"":" and alm_codi = " + almCodi) ;

     if (pro_codi!=0)
       s+=" and pro_codi = "+pro_codi;
     else
     {
       if (proArtcon != 2) // Incluir Congelados
         s += " and exists (select pro_codi from v_articulo " +
             "  where v_articulo.pro_codi = stockpart.pro_codi " +
             " and pro_artcon " + (proArtcon == 0 ? "= 0" : " <> 0") + ")";

       if (!resetear) // No resetear TODOS los productos
         s += " AND exists (select pro_codi FROM v_coninvent as c" +
             " where c.pro_codi = stockpart.pro_codi  " +
             " and c.emp_codi = " + empCodi +
             (almCodi==0?"":" and c.alm_codi = " + almCodi )+
             " and c.cci_feccon = TO_DATE('" + fecinv + "','dd-MM-yyyy')) ";
     }
     if (padre!=null)
       padre.debug("Resetear Productos: "+s);
     dtAdd.executeUpdate(s);
     // Pongo todo los acumulados de los productos a 0
     s = "UPDATE v_articulo set pro_stock = 0, pro_stkuni = 0 " +
         " where 1=1";
    if (pro_codi != 0)
      s+= " and pro_codi = "+pro_codi;
    else
    {
      s+=(almCodi==0?"":"  and pro_almcom="+almCodi);
      s+=  proArtcon != 2 ? "  and pro_artcon " + (proArtcon == 0 ? "= 0" : " <> 0") : "";
     if (! resetear) // No resetear TODOS los productos
       s+=" AND exists (select pro_codi FROM coninvlin as i "+
         " where i.pro_codi = v_articulo.pro_codi  "+
         " and i.emp_codi = "+empCodi+
         (almCodi==0?"":" and i.alm_codi = "+almCodi)+
         " and i.cci_feccon = TO_DATE('" + fecinv + "','dd-MM-yyyy') )";
    }
     dtAdd.executeUpdate(s);

     Iterator it;
     it = ht.keySet().iterator();
     String key;
     String valor;
//     int ejeNume,lote,numind;
//     String serie;
     n = 0;
     DatIndiv datInd;
     while (it.hasNext())
     {
       key = it.next().toString();
       valor = (ht.get(key)).toString();
       datInd =new DatIndiv(key,valor);
       suma_regen(datInd.ejeNume,datInd.serie,datInd.lote,datInd.numind,
               datInd.proCodi,datInd.almCodi,datInd.canti,datInd.numuni,fecinv,
             CREAR_SI,0,fecinv,false,true);
       n++;
       if (n % 10 == 0)
         setMensajePopEspere("Actualizando Stock ... Tratando Producto: " + datInd.proCodi);
 //        System.out.println(key+" : "+htPrv.get(key));
     }

     boolean ret=regAcuProducto(dt,proArtcon,fecinv,pro_codi);
     if (swInvControl)
     { // Regenerar stock sobre Inventario Control. Pongo camara Actual      
         s="update stockpart set cam_codi=("+
            " select cam_codi FROM v_coninvent as i where stockpart.pro_codi=i.pro_codi " +
             " and i.lci_peso > 0"+
             " and stockpart.eje_nume=i.prp_ano "+
             " and stockpart.pro_serie = i.prp_seri "+
             " and stockpart.pro_nupar = i.prp_part "+
             " and stockpart.pro_numind=i.prp_indi"+
             " AND i.cci_feccon = TO_DATE('" + Formatear.getFecha(fecinv,"dd-MM-yyyy") + "','dd-MM-yyyy')) "+
             " where exists ( select r.cam_codi FROM v_coninvent as r,v_articulo as a"+
                " where  stockpart.pro_codi=r.pro_codi " +
                " and stockpart.eje_nume=r.prp_ano "+
                " and stockpart.pro_serie = r.prp_seri "+
                " and stockpart.pro_nupar = r.prp_part "+
                " and stockpart.pro_numind=r.prp_indi "+
                " and lci_peso > 0 " +
                " and lci_regaut = 0 "+
                (almCodi==0?"":" and alm_codi = " + almCodi) +
                (pro_codi == 0 ? "" : " and r.pro_codi = " + pro_codi) +
                " and a.pro_codi = r.pro_codi " +
                (proArtcon != 2?" and a.pro_artcon " + (proArtcon == 0 ? "= 0" : " <> 0"):"") +
                " AND r.cci_feccon = TO_DATE('" + Formatear.getFecha(fecinv,"dd-MM-yyyy") + "','dd-MM-yyyy')) ";
          dtAdd.executeUpdate(s);
     }
     dtAdd.executeUpdate("update ajustedb set aju_regacu=1"); // Habilito Reg. Acum.
     return ret;
   }
    /**
     * Inserta Regularizaciones Inventario sobre producto  Congelado
     * @param dtAdd
     * @param dtStat
     * @param almCodi Almacen
     * @param fecinv Fecha Inventario
     * @param fecsup Fecha en la que insertar las regularizaciones
     * @param usuNomb Usuario que genera las regularizaciones
     * @throws java.text.ParseException
     * @throws SQLException
     */
   public void insInventCong(DatosTabla dtAdd, DatosTabla dtStat,int almCodi,
                                Date fecinv,Date fecsup,String usuNomb) throws ParseException,SQLException
   {
     HashMap<String,String> ht =getStockInd(dtStat,-1,almCodi,fecinv,fecsup,0,true,false);
     if (ht==null)
         return ;
     /**
      * Le pongo precios a los productos
      */
     int tirCodi=pdmotregu.getTipoMotRegu(dtStat,"=");
     if (tirCodi==-1)
       throw new SQLException("No encontrado Motivo Regulacion tipo '='");
     if (mvtos==null)
         mvtos=new MvtosAlma();
     mvtos.setIncUltFechaInv(false);
     mvtos.iniciarMvtos(fecinv, fecsup,dtStat);
     Iterator<Integer> itArtic;
     itArtic =difProd.keySet().iterator();
     int proCodi;
     // Busco los precios y los coloco en el stock encontrado.
     while (itArtic.hasNext())
     {
       proCodi =  itArtic.next();
       mvtos.calculaMvtos(proCodi, dtAdd, dtStat, null,null);
       difProd.put(proCodi,mvtos.getPrecioStock());
     }
          
     String key,valor;
     Iterator<String> it;
     it = ht.keySet().iterator();

     int n = 0;
     double precio;
     DatIndiv datInd;
     while (it.hasNext())
     {
       key = it.next();
       valor = ht.get(key);
       datInd =new DatIndiv(key,valor);
//       if (datInd.numind<=0 || datInd.canti<=0)
//           continue; // Permito tener inventario en negativo.
       precio=difProd.get(datInd.proCodi);
       paregalm.insRegStock(dtAdd, fecsup, datInd.proCodi, empCodi, datInd.ejeNume, datInd.serie,
               datInd.lote,datInd.numind,
               datInd.numuni,datInd.canti,datInd.almCodi,tirCodi,"Reg. Congelado Automatica",precio,
               usuNomb,0);
     }
   }

   /**
    * Regenera acumulados sobre tabla productos a partir de los datos en la tabla
    * v_stkpart. Actualiza tanto los acumulados por almacen en actstkpart como en
    * la tabla productos.
    *
    * @param dt DatosTabla
    * @param proArtcon int Articulo Congelado/Fresco/Ambos (2)
    * @param fecMvto String Fecha Movimiento (normalmente la de inventario)
    * @param proCodi int Codigo producto (0 = todos)
    * @throws Exception
    * @return boolean Si se encontraron registros para actualizar.
    */
   private boolean regAcuProducto (DatosTabla dt, int proArtcon,Date fecMvto,int proCodi) throws
       Exception
   {
     String s = "select s.pro_codi,s.alm_codi,sum(stp_kilact) as stp_kilact, sum(stp_unact) as stp_unact "+
         " from  v_stkpart s "+
         (proArtcon==2?"":", v_articulo as a ")+
         " WHERE s.emp_codi = "+empCodi+
         " and eje_nume != 0 " +
         " and s.pro_Codi != 0 " +
         (proCodi!=0?" and s.pro_codi = "+proCodi:"")+
         (proArtcon==2?"":" and a.pro_artcon " + (proArtcon == 0 ? "= 0" : " <> 0")+
          " and s.pro_codi = a.pro_codi "
          //+ " and s.emp_codi = a.emp_codi "
          )+
         " group by s.alm_codi,s.pro_codi "+
         " order by s.pro_codi,s.alm_codi";
     if (! dt.select(s))
       return false;
     double kilos=0;
     int unidades=0,res;
     int proCodT=dt.getInt("pro_codi");
     do
     {
    
       actAcum(dt.getInt("pro_codi"),dt.getInt("alm_codi"),dt.getDouble("stp_kilact"),
               dt.getInt("stp_unact"),fecMvto,true);
      
         // Actualizo Acumulados de Producto en tabla productos
       if (proCodT!=dt.getInt("pro_codi"))
       {
           s = "UPDATE v_articulo set "
               + " pro_stock = " + kilos
               + ", pro_stkuni = " + unidades
               + " WHERE pro_codi = " + proCodT;
           res = dtAdd.executeUpdate(s);
           if (res == 0)
           {
               logger.error("Articulo: " + proCodT + " en Empresa: "
                   + empCodi + " NO Encontrado en tabla Maestros de Articulos");
           }
           proCodT=dt.getInt("pro_codi");
           kilos=0;
           unidades=0;
       }
       kilos+=dt.getDouble("stp_kilact");
       unidades+=dt.getInt("stp_unact");
     } while (dt.next());
     s = "UPDATE v_articulo set "
           + " pro_stock = " + kilos
           + ", pro_stkuni = " + unidades
           + " WHERE pro_codi = " + proCodT;
     res = dtAdd.executeUpdate(s);
     if (res == 0)
     {
           logger.error("Articulo: " + proCodT + " en Empresa: "
               + empCodi + " NO Encontrado en tabla Maestros de Articulos");
     }
     return true;
   }
   public void setAceptaNeg(boolean acepNegativo)
   {
     acepNeg=acepNegativo;
   }
   public boolean getAceptaNeg()
   {
     return acepNeg;
   }

   /**
    * Busca Stock sobre un producto. Comprueba si el producto tiene control
    * individual, para buscar por numero de individuos o no.
    *
    * @param fecStock Fecha de Stock, fecha a la que queremos el valor
    *                 . SI es nulo es a fecha actual.
    * @param fecInic Fecha Inicio desde la que buscar Mvtos. Tecnicamente la fecha
    * del ultimo stock fisico. Si es NULO 1-1-(A\uFFFDo curso)
    * @param proCodi Codigo de Producto .. No puede ser nulo.
    * @param ejeLote Ejercicio de producto .. si es 0 se pondra el actual.
    * @param empLote Empresa ... si es 0 se buscara en la activa.
    * @param serLote   Serie .. si es null se buscara en todas
    * @param numLote si es 0 .. se buscara en todas.
    * @throws SQLException en caso de cualquier error
    *
    * @return boolean true si encontro datos para tratar.
    */
   public boolean buscaStock(java.util.Date fecStock, java.util.Date fecInic, int proCodi,int empLote,int ejeLote,String serLote,
                   int numLote) throws SQLException
   {
     String s;
     boolean swErr=false;
     String feulst; // Fecha Ultimo Stock
     String fecStockStr; // Fecha de Stock (en String - Formato dd-MM-yyyy)
//     int nReg;
     double precio,prStk;
     String tipMov;
     double canti;
     double canMvto;

     if (empLote==0)
       empLote=empCodi;
     if (fecStock == null)
       fecStock = new java.util.Date(System.currentTimeMillis());

     fecStockStr=Formatear.getFechaVer(fecStock);

     feulst=Formatear.getFechaVer(fecInic);

    // Busco compras.
    s="SELECT 'C' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
        " c.acc_nume as  lote,"+
        " l.acl_canti as canti,l.acl_prcom as precio, l.acl_prstk as prstk,"+
        " l.pro_codi as pro_codori "+//,i.acp_numind as numind "+
        " FROM v_albacoc c,v_albacol l" +
        " where c.emp_codi = l.emp_codi "+
        " AND c.acc_serie = l.acc_serie "+
        " AND c.acc_nume = l.acc_nume "+
        " and c.acc_ano = l.acc_ano "+
        " AND c.emp_codi = "+empLote+
        (serLote!=null?" AND c.acc_serie = '"+serLote+"'":"")+
        (numLote==0?"": " AND c.acc_nume = "+numLote)+
        (numLote==0?"": " AND c.acc_nume = "+numLote)+
        " AND l.pro_codi = "+proCodi+
        " AND c.acc_fecrec >= TO_DATE('"+feulst+"','dd-MM-yyyy') "+
        " and c.acc_fecrec <= TO_DATE('"+fecStockStr+"','dd-MM-yyyy') "+

    " UNION all"+ // Albaranes de Venta
        " select 'V' as sel,'-' as tipmov,c.avc_fecalb as fecmov,"+
        "  c.avc_serie as serie,c.avc_nume as  lote, "+
        " l.avl_canti as canti,0 as precio,0 as prstk,"+
        " l.pro_codi as pro_codori "+
        "  from v_albavel l, v_albavec c"+
        (numLote==0?"":" ,v_albvenpar as p")+
        " where c.emp_codi = l.emp_codi "+
          " AND c.avc_serie = l.avc_serie "+
          " AND c.avc_nume = l.avc_nume "+
          " and c.avc_ano = l.avc_ano "+
//          " AND c.avc_serie !='X' "+ // Pasar de traspaso entre almacenes ?
          " AND c.emp_codi = "+empCodi+
          " AND l.pro_codi = "+proCodi+
          (numLote==0?"":"  AND l.avc_serie = p.avc_serie "+
          " AND l.avc_nume = p.avc_nume "+
          " and l.avc_ano = p.avc_ano "+
          " and l.avl_numlin = p.avl_numlin "+
          " and p.avp_numpar = "+numLote)+
         (serLote!=null?" AND p.avp_serlot = '"+serLote+"'":"")+
          " AND c.avc_fecalb >= TO_DATE('"+feulst+"','dd-MM-yyyy') "+
          " and c.avc_fecalb <= TO_DATE('"+fecStockStr+"','dd-MM-yyyy') "+

     " UNION all "+ // Despieces (Salidas)
        " select 'D' as sel,'"+
        (acepNeg ?"+":"-")+
        "' as tipmov,deo_fecha as fecmov,"+
        "  deo_serlot as serie,pro_lote as  lote,"+
        " deo_kilos as canti,0 as precio, 0 as prstk,"+
        " pro_codi as pro_codori "+
        " from  v_despori where "+
        " emp_codi = "+empCodi+
        (serLote != null ? " AND deo_Serlot = '" + serLote + "'" : "") +
        (numLote == 0 ? "" : " AND pro_lote = " + numLote) +
        " and deo_emplot = "+empLote+
        " AND pro_codi = " + proCodi +
        (ejeLote == 0 ? "" : " AND deo_ejelot = " + ejeLote) +
        " AND deo_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and deo_fecha <= TO_DATE('"+fecStockStr+"','dd-MM-yyyy') "+

    " UNION all "+ // Despieces (Entradas)
        " select 'd' as sel, '+' as tipmov,c.deo_fecha as fecmov,"+
        "  l.def_serlot as serie,l.pro_lote as  lote,"+
        " l.def_kilos as canti,l.def_prcost as precio,0 as prstk,"+
        " c.pro_codi as pro_codori "+
        " from  desporig c,v_despfin l where "+
        " c.eje_nume = l.eje_nume "+
        " and c.deo_codi = l.deo_codi "+
        " and l.emp_codi = "+empCodi+
//          " AND l.eje_nume = " + ejeNume +
//          " AND l.def_prcost <> 0 "+ // Despreciar Entr. a Precio 0 (NO VALORADAS)
        (serLote != null ? " AND l.def_Serlot = '" + serLote + "'" : "") +
        (numLote == 0 ? "" : " AND l.pro_lote = " + numLote) +
        (empLote == 0 ? "" : " AND l.def_emplot = " + empLote) +
        (ejeLote == 0 ? "" : " AND l.def_ejelot = " + ejeLote) +
        " AND l.pro_codi = " + proCodi +
        " AND c.deo_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and c.deo_fecha <= TO_DATE('"+fecStockStr+"','dd-MM-yyyy') "+

   " UNION all "+ // Regularizaciones.
       " select 'R' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"+
       "  r.pro_serie as serie,r.pro_nupar as  lote,"+
       " r.rgs_kilos as canti,r.rgs_prregu as precio,r.rgs_prregu as prstk,"+
       " r.pro_codi as pro_codori "+
       " FROM v_regstock r WHERE   r.emp_codi = "+empCodi+
//       " and r.rgs_trasp != 0 "+
//          " AND r.eje_nume = " + ejeNume +
        (serLote != null ? " AND r.pro_serie = '" + serLote + "'" : "") +
        (numLote == 0 ? "" : " AND r.pro_nupar = " + numLote) +
        (empLote == 0 ? "" : " AND r.emp_codi = " + empLote) +
        (ejeLote == 0 ? "" : " AND r.eje_nume = " + ejeLote) +
        " AND r.pro_codi = " + proCodi +
        " AND r.rgs_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and r.rgs_fecha <= TO_DATE('"+fecStockStr+"','dd-MM-yyyy') "+
    " ORDER BY 3,2 desc"; // FECHA y tipo

//    System.out.println("select: "+s);
    precAcu=0;
    cantiAcu = 0;
    precStk =0;
    if (dtAdd.select(s))
    {
      feulst="";
      do
      {
        canti=0;
        precio =precAcu;
        prStk = precStk;
        tipMov=dtAdd.getString("tipmov");
        if (tipMov.equals("="))
        {
          if (serLote == null && numLote==0)
          {
            if (!feulst.equals(dtAdd.getFecha("fecmov")))
            {
              feulst = dtAdd.getFecha("fecmov");
              precio = dtAdd.getDouble("precio") ;
              prStk= dtAdd.getDouble("prstk");
              cantiAcu=0;
              canti = dtAdd.getDouble("canti");
            }
            else
              tipMov = "+";
          }
        }

        if (tipMov.equals("+"))
        { // Entrada. (Pueden ser compras o Despieces
          canMvto=dtAdd.getString("sel").equals("D")?dtAdd.getDouble("canti",true)*-1:dtAdd.getDouble("canti",true);
          canti=cantiAcu+canMvto;
          if (dtAdd.getString("sel").equals("d"))
          { // Si el producto del Desp. Entrada es igual que el Prod. Desp.
            // Salida NO influye en el precio (si AcepNeg==false)
            if ((dtAdd.getInt("pro_codori")==proCodi  && acepNeg==false)
                || dtAdd.getDouble("precio",true)==0)
            {
              cantiAcu = canti;
              continue;
            }
          }
          if ((cantiAcu < 0 || canti < 0) && acepNeg==false)
          { // Cant. anterior en Negativo
            if (swErr==false)
            {
              swErr = true;
              mensajeErr("Atencion Stock en Negativo: " + cantiAcu +
                           " EN Fecha: " + dtAdd.getFecha("fecmov"));
            }

            precio = dtAdd.getDouble("precio",true);
            prStk = dtAdd.getDouble("prstk",true);
//              dat.setCanti(0);
//              canti=dtAdd.getDouble("canti");
          }
          else
          {
            precio = (precAcu * cantiAcu) +
                (canMvto * dtAdd.getDouble("precio", true));
            prStk = (precStk * cantiAcu) +
                (canMvto * dtAdd.getDouble("prstk", true));

            if ( canti >= 0.01 || canti <= -0.01)
            {
              if (precio >= 0.01 || precio <= -0.01)
                  precio = precio / canti;
               else
                 precio=precAcu;
               if (prStk >= 0.01 || prStk <= -0.01)
                 prStk = prStk / canti;
               else
                 precStk=prStk;
             }
            else
            { // La cantidad es cercana a 0 Ã³ 0
              precio = precAcu;
              precStk=prStk;
            }
          }
        }

        if (tipMov.equals("-"))
          canti=cantiAcu-dtAdd.getDouble("canti",true);
        precAcu=precio;
        precStk=prStk;
        cantiAcu=canti;
      } while (dtAdd.next());
    }
    else
      return false;
    return true;
  }
  public double getPrecioAcum()
  {
    return precAcu;
  }
  public double getCantiAcum()
  {
    return cantiAcu;
  }
  public double getPrecioStk()
  {
    return precStk;
  }
  /**
   * Devuelve la fecha de Ultimo ejercicio.
   * @param empCodi int Empresa (0 TODAS)
   * @param ejeCodi int Ejercicio donde buscar (Si es 0 se buscara en todos)
   * @param dt DatosTabla
   * @param fecLim Date  con la fecha a partir de la q buscar el inventario.
   *        null si se quiere buscar desde el principio.
   * @throws SQLException
   * @return String Fecha de Ultimo Inventario (dd-MM-yyyy) o Nulo si no lo encuentra.
   */
  public static String getFechaUltInv(int empCodi,int ejeCodi,Date fecLim, DatosTabla dt ) throws SQLException
  {
    String strFecLim=Formatear.getFecha(fecLim,"dd-MM-yyyy");
    String s = "select MAX(rgs_fecha) as cci_feccon from v_regstock as r " +
      " where tir_afestk='=' "+
      (ejeCodi==0?"": " and r.eje_nume = "+ejeCodi)+
      (fecLim==null?"": " and rgs_fecha <= TO_DATE('"+strFecLim+"','dd-MM-yyyy')")+
      (empCodi==0?"": " and r.emp_codi = "+empCodi);
    dt.select(s);
    if (dt.getObject("cci_feccon") == null)
      return null;
    else
     return dt.getFecha("cci_feccon", "dd-MM-yyyy");
  }
  /**
   * Devuelve la fecha de ultimo inventario inferiro a la fecha mandada
   * @param fecLim Fecha limite
   * @param dt
   * @return
   * @throws SQLException 
   */
  public static Date getDateUltInv(Date fecLim, DatosTabla dt ) throws SQLException
  {
      try {
        String fecInv=getFechaUltInv(0,0, fecLim,dt);
        if (fecInv==null)
            return null;
        return Formatear.getDate(fecInv,"dd-MM-yyyy");
      } catch (ParseException k)
      {
          return null;
      }
  }
}
