package gnu.chu.anjelica.ventas;

import gnu.chu.anjelica.DatosIVA;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.MantTipoIVA;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import gnu.hylafax.job.TimeParser.ParseException;
import java.sql.*;
import java.util.*;
import java.util.Date;


/**
 *
 * <p>Título: actCabAlbFra</p>
 * <p>Descripción: Calcula Los Importes de los datos de cabecera para albaranes y facturas</p>
 * <p>Copyright: Copyright (c) 2005-2010
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
 * @author chuchiP
 * @version 1.1
 */

public class actCabAlbFra
{
  DatosTabla dtCab;
  DatosTabla dtLin;
  String s;
  int tipIva;
  boolean swCamTipIva=false;
  Hashtable ht=new Hashtable();
  boolean valora=true;
  int numDec=2;
  int numDecPrecio=4;
  int empCodi;

  public actCabAlbFra(DatosTabla dtLin,int empCodi) throws SQLException
  {
    this(null,dtLin,empCodi);
  }

  public actCabAlbFra(DatosTabla dtCab, DatosTabla dtLin,int empCodi) throws SQLException
  {    
    this(dtCab,dtLin,empCodi,pdconfig.getNumDecimales(empCodi,dtLin));   
  }
  public actCabAlbFra(DatosTabla dtCab, DatosTabla dtLin,int empCodi,int numDecPrec)
  {
    this.numDecPrecio=numDecPrec;
    this.dtCab=dtCab;
    this.dtLin=dtLin;
  }
  public void setValora(boolean valora)
  {
    this.valora=valora;
  }

  public boolean actDatosAlb(int empCodi, int avcAno, String avcSerie,
                             int avcNume) throws SQLException,java.text.ParseException
  {
    if (dtCab==null)
      throw new SQLException("Datos de Cabecera NO ha sido Inicializada");
    swCamTipIva=false;
    s = "SELECT * FROM v_albavec c, clientes cl WHERE c.avc_ano = " + avcAno +
        " and c.emp_codi = " + empCodi +
        " and c.avc_serie = '" + avcSerie + "'" +
        " and c.avc_nume = " + avcNume +
        " and cl.cli_codi = c.cli_codi ";
    if (!dtCab.select(s))
      return false;
    boolean incIva = dtCab.getInt("cli_exeiva") == 0 && empCodi < 90;

    return actDatosAlb(empCodi,avcAno,avcSerie,avcNume,incIva,
              Formatear.redondea(dtCab.getDouble("avc_dtopp"),numDec), 
              Formatear.redondea(dtCab.getDouble("avc_dtocom"),numDec),
              dtCab.getInt("cli_recequ"),dtCab.getDate("avc_fecalb"));

  }
  public boolean actDatosAlb(int empCodi, int avcAno, String avcSerie,
                             int avcNume,boolean incIva,
                             double dtopp,double dtoCom,int recequ) throws SQLException

  {
      return actDatosAlb(empCodi,avcAno,avcSerie,avcNume,incIva,dtopp,dtoCom,recequ,null);
  }
  /**
   * Actualizar datos de albaran de venta. Mete en el HashTable ht ciertos valores
   *
   * @param empCodi
   * @param avcAno
   * @param avcSerie
   * @param avcNume
   * @param incIva
   * @param dtopp
   * @param dtoCom
   * @param recequ
   * @return
   * @throws SQLException
   */
  public boolean actDatosAlb(int empCodi, int avcAno, String avcSerie,
                             int avcNume,boolean incIva,
                             double dtopp,double dtoCom,int recequ,Date fecAlb) throws SQLException
  {
    if (fecAlb==null)
    {// Buscar fecha Albaran
        s = "SELECT * FROM v_albavec as c WHERE c.avc_ano = " + avcAno +
            " and c.emp_codi = " + empCodi +
            " and c.avc_serie = '" + avcSerie + "'" +
            " and c.avc_nume = " + avcNume;
        if (!dtLin.select(s))
              throw new SQLException("No encontrado Albaran"+s);
        fecAlb=dtLin.getDate("avc_fecalb");

    }
    ht.put("incIva", incIva);
    ht.put("avc_impbru", (double) 0);
    ht.put("kilos", (double) 0);
    ht.put("nLin", 0);
    ht.put("avc_dtocom", (double) 0);
    ht.put("avc_impdco", (double) 0);
    ht.put("avc_dtopp", (double) 0);
    ht.put("avc_impdpp", (double) 0);
    ht.put("avc_basimp", (double) 0);
    ht.put("avc_tipiva", (double) 0);
    ht.put("avc_impiva", (double) 0);
    ht.put("avc_tipree", (double) 0);
    ht.put("avc_impree", (double) 0);
    ht.put("avc_impalb", (double) 0);

    s = "SELECT l.pro_codi,sum(l.avl_canti) as avl_canti, " +
        " avl_prven-avl_dtolin as avl_prven,pro_tipiva,pro_indtco FROM V_ALBAVEL as l, v_articulo as a " +
        " WHERE l.avc_ano = " + avcAno +
        " and l.emp_codi = " + empCodi +
        " and l.avc_serie = '" + avcSerie + "'" +
        " and l.avc_nume = " + avcNume +
        " and l.avl_canti != 0 " +
        " and l.pro_codi = a.pro_Codi " +
        " group by l.pro_codi,avl_prven,avl_dtolin,pro_tipiva,pro_indtco ";
    if (!dtLin.select(s))
      return false; // SIN LINEAS DE ALBARAN
    double impLin;
    double kilos=0;
    double impBim = 0, impDtoPP = 0, impIva = 0, impReq = 0;
    double impDtCom=0,impDtoCom=0;
    
    int nLin=0;
    tipIva=-1;
    do
    {
      if (tipIva != -1 && tipIva != dtLin.getInt("pro_tipiva") && dtLin.getDouble("avl_canti",true)!=0)
        swCamTipIva = true;
      tipIva = dtLin.getInt("pro_tipiva");
      impLin=Formatear.redondea(Formatear.redondea(dtLin.getDouble("avl_canti",true), 2) *
                                Formatear.redondea(dtLin.getDouble("avl_prven",true),numDecPrecio),numDec);

      kilos+=dtLin.getDouble("avl_canti", true);
      impDtCom+=dtLin.getInt("pro_indtco")==0?0:impLin;    
      impBim += impLin;
      
      nLin++;
    }  while (dtLin.next());
  
    impBim=Formatear.redondea(impBim,numDec);
    ht.put("avc_impbru", impBim);
    ht.put("kilos", Formatear.redondea(kilos,numDec));
    ht.put("nLin", nLin);

    if (dtopp != 0)
      impDtoPP = Formatear.redondea(impBim * dtopp / 100, numDec);
     if (dtoCom != 0)
      impDtoCom = Formatear.redondea(impDtCom * dtoCom / 100, numDec);
     
    impBim = Formatear.redondea(impBim - impDtoPP-impDtoCom,numDec);
    if (valora)
    {
      ht.put("avc_dtopp", dtopp);
      ht.put("avc_impdpp", impDtoPP);
      ht.put("avc_dtocom", dtoCom);
      ht.put("avc_impdco", impDtoCom);

      ht.put("avc_basimp", impBim);
    }
    DatosIVA dtIva=null;
    if (incIva)
    {
      dtIva= MantTipoIVA.getDatosIva(dtLin, tipIva,fecAlb);
      
      if (dtIva!=null)
      {
        impIva = Formatear.redondea(impBim * dtIva.getPorcIVA() /
                                    100, numDec);
        if (recequ == -1)
          impReq = Formatear.redondea(impBim * dtIva.getPorcREQ() /
                                      100, numDec);
      }
      else
        throw new SQLException(" Tipo de Iva " + tipIva + " NO ENCONTRADO");
    }
    double impAlb = Formatear.redondea(impBim + impIva + impReq,numDec);

    if (valora)
    {
      if (incIva)
      {
        ht.put("avc_tipiva", dtIva.getPorcIVA());
        ht.put("avc_impiva", impIva);
        ht.put("avc_tipree", recequ == 0?0: dtIva.getPorcREQ());
        ht.put("avc_impree", impReq);
      }
      ht.put("avc_impalb",impAlb); // Importe de ALbaran con Imp. Incluidos
    }
    else
    {
      ht.put("avc_tipiva", (double) 0);
      ht.put("avc_impiva",(double) 0);
      ht.put("avc_tipree", (double) 0);
      ht.put("avc_impree", (double) 0);
      ht.put("avc_impalb", (double) 0);
    }
    return true;
  }
  public String getValString(String name)
  {
    return ht.get(name).toString();
  }
  public double getValDouble(String name)
  {
    return ( (Double) ht.get(name)).doubleValue();
  }

  public double getValInt(String name)
  {
    return ( (Integer) ht.get(name)).intValue();
  }
  public boolean getValBoolean(String name)
  {
    return ( (Boolean) ht.get(name)).booleanValue();
  }

  public boolean getCambioIva()
  {
    return swCamTipIva;
  }
  public boolean actDatosFra(int ejeNume,int empCodi,String fvcSerie,int fvcNume) throws SQLException
  {
    if (dtCab==null)
      throw new SQLException("Datos de Cabecera NO ha sido Inicializada");

    s="SELECT f.*,c.* FROM v_facvec AS f,clientes as c WHERE "+
        " c.cli_codi = f.cli_codi "+
        " AND f.fvc_ano = " + ejeNume+
        " and f.emp_codi = " + empCodi +
        " and f.fvc_serie = '"+fvcSerie+"'"+
        " and f.fvc_nume = " + fvcNume;
    if (! dtCab.select(s))
      return false;

    boolean incIva=dtCab.getInt("cli_exeiva")==0 && empCodi<90;
    return actDatosFra(ejeNume,empCodi,fvcSerie,fvcNume,incIva,
                       Formatear.redondea(dtCab.getDouble("fvc_dtopp"),numDec),
                       Formatear.redondea(dtCab.getDouble("fvc_dtocom"),numDec),
                       dtCab.getDouble("cli_recequ")!=0,dtCab.getDate("fvc_fecfra") );
  }
  /**
   * Actualizar datos de factura
   * @param ejeNume int
   * @param empCodi int
   * @param fvcSerie char
   * @param fvcNume int
   * @param incIva boolean
   * @param dtopp double
   *  @param dtoCom double
   * @param cliRecequ boolean (Cliente tiene rec.equiv)
   * @throws SQLException
   * 
   * @return boolean
   */
  public boolean actDatosFra(int ejeNume,int empCodi,String fvcSerie,int fvcNume,
       boolean incIva,double dtopp,double dtoCom,boolean cliRecequ,Date fecha) throws SQLException
  {
      if (fecha==null)
      {
           s = "SELECT fvc_fecfra  FROM v_facvec WHERE  fvc_ano = " + ejeNume+
            " and emp_codi = " + empCodi +
            " and  fvc_serie = '"+fvcSerie+"'"+
            " and  fvc_nume = " + fvcNume ;
          if (! dtLin.select(s))
              throw new SQLException("Error al buscar factura: "+s);
          fecha=dtLin.getDate("fvc_fecfra");
      }
//    double impBruto=0;
    double impDtoPP=0;
    double impDtoCom=0;
    int tipIva=-1;

    double impIva=0;
    double impReq=0,impLin,impLinT=0,impLinDtoComT=0;
    double impBim;
    s = "SELECT l.avc_fecalb,l.avc_ano,l.avc_serie,l.avc_nume, l.pro_codi,"+
        " sum(l.fvl_canti) as fvl_canti,  fvl_prven,fvl_dto,pro_tipiva,pro_indtco"
        + "  FROM v_facvel as l, v_articulo as a "+
        " WHERE l.eje_nume = " + ejeNume+
        " and l.emp_codi = " + empCodi +
        " and l.fvc_serie = '"+fvcSerie+"'"+
        " and l.fvc_nume = " + fvcNume +
        " and l.pro_codi = a.pro_Codi "+
        " group by l.avc_fecalb, l.avc_serie,l.avc_ano,l.avc_nume, l.pro_codi,fvl_prven,fvl_dto,pro_tipiva,pro_indtco ";
    if (! dtLin.select(s))
      return false; // SIN LINEAS DE Factura
    do
    {
      if (tipIva!=-1 && tipIva!=dtLin.getInt("pro_tipiva") && dtLin.getDouble("fvl_canti") != 0)
        swCamTipIva=true;
      tipIva=dtLin.getInt("pro_tipiva");
      impLin=Formatear.redondea(Formatear.redondea(dtLin.getDouble("fvl_canti",true), 2) *
                                Formatear.redondea(dtLin.getDouble("fvl_prven",true),numDecPrecio),2);
      impLinDtoComT+=dtLin.getInt("pro_indtco")==0?0:impLin;
      impLinT+=impLin;
    } while (dtLin.next());
  
    impLinT=Formatear.redondea(impLinT,numDec);
    if (valora)
      ht.put("fvc_impbru", impLinT);
    else
      ht.put("fvc_impbru", (double) 0);
    if (dtopp!=0 )
      impDtoPP= Formatear.redondea(impLinT *dtopp / 100,numDec);
    if (dtoCom!=0 )
        impDtoCom= Formatear.redondea(impLinDtoComT *dtoCom / 100,numDec);

    impBim= Formatear.redondea(impLinT-impDtoPP-impDtoCom,numDec);
    if (valora)
    {
      ht.put("fvc_dtopp",dtopp);
      ht.put("fvc_impdpp", impDtoPP);
      ht.put("fvc_dtoco",dtoCom);
      ht.put("fvc_impdco", impDtoCom);

      ht.put("fvc_basimp",impBim);
    }
    else
    {
      ht.put("fvc_dtopp", (double) 0);
      ht.put("fvc_impdpp", (double) 0);
      ht.put("fvc_basimp", (double) 0);
    }
    DatosIVA tipoIva=null;
    if (incIva)
    {
     tipoIva= MantTipoIVA.getDatosIva(dtLin, tipIva,fecha);
     
      if (dtLin.select(s))
      {
        impIva = Formatear.redondea(impBim * tipoIva.getPorcIVA() / 100, numDec);
        if (cliRecequ )
          impReq = Formatear.redondea(impBim * tipoIva.getPorcREQ() / 100,numDec );
      }
      else
        throw new SQLException(" Tipo de Iva " + tipIva + " NO ENCONTRADO");
    }
    double impFra = Formatear.redondea(impBim + impIva + impReq,numDec);

    if (valora)
    {
      if (incIva)
      {
        ht.put("fvc_tipiva", tipoIva.getPorcIVA());
        ht.put("fvc_impiva", impIva);
        ht.put("fvc_tipree", cliRecequ ?tipoIva.getPorcREQ():(double) 0);
        ht.put("fvc_impree", impReq);
      }
      else
      {
        ht.put("fvc_tipiva", (double) 0);
        ht.put("fvc_impiva", (double) 0);
        ht.put("fvc_tipree",(double) 0);
        ht.put("fvc_impree", (double) 0);
      }

      ht.put("fvc_sumtot", new Double(impFra));
    }
    else
    {
      ht.put("fvc_tipiva", new Double(0));
      ht.put("fvc_impiva", new Double(0));
      ht.put("fvc_tipree", new Double(0));
      ht.put("fvc_impree", new Double(0));
      ht.put("fvc_impalb", new Double(0));
    }
    return true;
  }
  public Hashtable getHashTable()
  {
    return ht;
  }
}
