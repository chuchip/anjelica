package gnu.chu.anjelica.ventas;

import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import java.io.*;
import gnu.chu.print.*;
import java.text.*;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.hylafax.SendFax;
import gnu.chu.mail.MailHtml;
/**
 *
 * <p>Título: lialbven</p>
 * <p>Descripción: Clase para imprimir los albaranes de Ventas <br>
 *   Imprime en modo texto (dump) o en pdf para enviar por fax.</p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * @version 1.0 - 20041227
 */
public class lialbven implements JRDataSource
{
  
  SendFax sendFax=null;
  actCabAlbFra datCab;
  String subject;
  String emailCC;
  int nAlbImp;
  int nLinALb;
  String empObsAlb="";
  ResultSet rs;
  Hashtable ht;
  DatosTabla dtLin;
  DatosTabla dtCab;
  String s;
  boolean valora;
  FileOutputStream fOut;
  
  
  public lialbven(DatosTabla dt,EntornoUsuario EU) throws SQLException,java.text.ParseException
  {
    datCab = new actCabAlbFra(dt,EU.em_cod);
  }

  public int impAlbar(Connection ct, DatosTabla dtCon1, String sql,
                        EntornoUsuario EU,boolean valora) throws Exception
  {
    return envAlbarFax(ct,dtCon1,sql,EU, valora, null,null,true,1);

  }
  /**
   * Envia un albaran por fax e imprime una copia si el parametro
   * copiaPapel es verdadero.
   * @param ct
   * @param dtCon1
   * @param sql
   * @param EU
   * @param valora
   * @param numFax
   * @param obser
   * @param copiaPapel
   * @param numCopia
   * @return
   * @throws Exception
   */
   public int envAlbarFax(Connection ct, DatosTabla dtCon1, String sql,
                      EntornoUsuario EU,boolean valora,String numFax,String obser,boolean copiaPapel,
                      int numCopia) throws Exception
    {
       return envAlbarFax(ct,  dtCon1, sql,
                       EU,valora, numFax, obser, copiaPapel,
                      numCopia,0);
   }
   public int imprEtiqPalets( String sqlAlb,lialbven liAlb,  DatosTabla dtCon1,EntornoUsuario EU ) throws Exception
   {
      Listados lis=Listados.getListado(EU.em_cod,  Listados.PALE_AVC, dtCon1);
      JasperReport jr = Listados.getJasperReport(EU,lis.getNombFich()   );
      java.util.HashMap mp =Listados.getHashMapDefault();
      mp.put("logotipo",Iconos.getPathIcon()+lis.getNombLogo());
               
      if (! dtCon1.select(sqlAlb))
      {
          mensajes.mensajeAviso("Albaran no tiene Palets");
          return 0;
      }      
      dtCon1.getResultSet().beforeFirst();
      JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(dtCon1.getResultSet()));

//     JasperViewer.viewReport(jp,false);
      gnu.chu.print.util.printJasper(jp, EU);
      return 1;
   }
   public int envAlbaranEmail(Connection ct, DatosTabla dtCon1, String sql,
                      EntornoUsuario EU,boolean valora,String email,String obser) throws Exception
   {
      return envAlbarFax(ct,dtCon1,sql,EU,valora,email,obser,false,1,0);
   }
   /**
    * Especifica el subject para cuando se envia un correo
    * @param subject 
    */
   public void setSubject(String subject)
   {
       this.subject=subject;
   }
   public void setEmailCC(String emailCC)
   {
       this.emailCC=emailCC;
   }
  /**
   * Imprime albaran tanto en papel como enviandolo por fax/Email
   * @param ct
   * @param dtCon1
   * @param sql
   * @param EU
   * @param valora
   * @param numFax
   * @param obser
   * @param copiaPapel
   * @param numCopias
   * @param avsNume  
   * @return
   * @throws Exception
   */
  public int envAlbarFax(Connection ct, DatosTabla dtCon1, String sql,
                      EntornoUsuario EU,boolean valora,String numFax,String obser,boolean copiaPapel,
                      int numCopias,int avsNume) throws Exception
  {
   getObsAlbaran(dtCon1,EU.em_cod);
   EU.getImpresora(gnu.chu.print.util.ALBARAN);
   this.valora=valora;
   rs = ct.createStatement().executeQuery(dtCon1.getStrSelect(sql));
   if (!rs.next())
   {
     mensajes.mensajeAviso("No encontrados albaranes para los criterios dados");
     return 0;
   }
   int cliCodi=rs.getInt("cli_codi");
   int avcAno=rs.getInt("avc_ano");
   int empCodi=rs.getInt("avc_empcod");
   String avcSerie=rs.getString("avc_serie");
   int avcNume=rs.getInt("avc_nume");
   Listados lis=Listados.getListado(EU.em_cod,  Listados.CAB_AVC, dtCon1);
   JasperReport jr = Listados.getJasperReport(EU, lis.getNombreFichero());
   java.util.HashMap mp = Listados.getHashMapDefault();
   mp.put(JRParameter.REPORT_CONNECTION,ct);
   if (avsNume==0)
     mp.put("valora", valora);
   else
     mp.put("valora", false);
   mp.put("emp_obsalb", rs.getString("avc_serie").equals("D")?"":empObsAlb);
   mp.put("obser", obser==null?null:obser.trim());
   mp.put("logotipo",lis.getPathLogo());
   vlike lk=  pdempresa.getDatosEmpresa(dtCon1, EU.em_cod);
   mp.put("empNif",MantPaises.getInicialesPais(lk.getInt("pai_codi"),dtCon1)+
         lk.getString("emp_nif"));
   mp.put("avs_nume", avsNume);
  
   if (avsNume!=0)
   { // Busco la fecha de albaran de servicio.
    DatosTabla dtTemp=new DatosTabla(ct);
    if (! dtTemp.select("select avs_fecha from albvenserc where avs_nume="+avsNume))
    {
        mensajes.mensajeAviso("No encontrado albaran de entrega de deposito");
        return 0;
    }
    mp.put("avs_fecha",dtTemp.getDate("avs_fecha"));
   }

   mp.put("SUBREPORT_FILE_NAME",EU.pathReport +"/"+
        Listados.getNombListado(EU.em_cod,avsNume>0?Listados.LINENT_AVC:
            Listados.LIN_AVC, dtCon1));
   
   if (!getDatosAlb(rs.getInt("avc_ano"),rs.getInt("avc_empcod"),rs.getString("avc_serie"),
                     rs.getInt("avc_nume")))
         return 0;
   Statement st=ct.createStatement();
   rs=st.executeQuery(dtCon1.getStrSelect(sql));

   JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);

   if (copiaPapel)
     gnu.chu.print.util.printJasper(jp, EU,numCopias,gnu.chu.print.util.ALBARAN);
   if (numFax==null)
     return nAlbImp; // Solo copia a Papel
//   System.out.println("Dir. temporal: "+EU.dirTmp);
   
   File fichPDF=util.getFile("albven",EU, "pdf",true) ;
   FileOutputStream outStr=new FileOutputStream(fichPDF);
   JasperExportManager.exportReportToPdfStream(jp,outStr);
   outStr.close();
   if (numFax.indexOf("@")>0)
   { // Es un correo.        
       MailHtml correo=new MailHtml(EU.usu_nomb,EU.email);
       correo.setEmailCC(emailCC);
       correo.enviarFichero(numFax,obser,subject,
          fichPDF,"albaran.pdf");       
       return nAlbImp;
   }
   if (sendFax==null)
       sendFax=new SendFax(EU,dtCon1); // Inicio clase SendFax
   sendFax.setCliente(cliCodi);
   sendFax.sendFax("A",empCodi,avcAno,avcSerie, avcNume,  numFax, fichPDF.getAbsolutePath());
  
   return nAlbImp;
 }

  @Override
  public boolean next() throws JRException
  {
    boolean ret;
    try {
      while (true)
      {
        ret = rs.next();
        if (!ret)
          return false;
        if (getDatosAlb(rs.getInt("avc_ano"),rs.getInt("avc_empcod"),rs.getString("avc_serie"),
                        rs.getInt("avc_nume")))
        {
          nAlbImp++;
          return true;
        }
      }
    } catch (SQLException | ParseException k)
    {
      throw new JRException(k);
    }
  }

  boolean getDatosAlb(int avcAno,int empCodi,String avcSerie,int avcNume) throws SQLException,java.text.ParseException
  {
    boolean incIva;

    incIva=rs.getInt("cli_exeiva")==0 && empCodi<90;
    datCab.setValora(valora);
    if (! datCab.actDatosAlb(empCodi,avcAno,avcSerie,avcNume,incIva,
                       rs.getDouble("avc_dtopp"),rs.getDouble("avc_dtocom"),
                       rs.getInt("cli_recequ")))
         return false;
    ht = datCab.getHashTable();
    return true;
  }
  @Override
  public Object getFieldValue(JRField jRField) throws JRException
  {
    String nombre=jRField.getName() ;
    try {
        if ( nombre.equals("cli_nomen"))
        {
            if (rs.getString("avc_clinom")!=null)
                return rs.getString("avc_clinom");
            else
                return rs.getString("cli_nomen");
        }
        if (nombre.equals("cli_diree") || nombre.equals("avc_clinom") ||
            nombre.equals("cli_poble") ||
            nombre.equals("avc_serie") || nombre.equals("cli_horenv") ||  nombre.equals("cli_codrut") ||  
            nombre.equals("rut_codi") ||  
            nombre.equals("cli_comenv") ||
            nombre.equals("avt_connom") || nombre.equals("avt_condni") || nombre.equals("avt_matri1") || nombre.equals("avt_matri2")  
            )
          return rs.getString(nombre);
        if (nombre.equals("tra_codi"))
            return rs.getInt(nombre);
         if (nombre.equals("avt_kilos"))
            return rs.getDouble(nombre);
        if (nombre.equals("avc_empcod") ||
            nombre.equals("avc_nume") ||
            nombre.equals("cli_codi") ||
            nombre.equals("cli_recequ") ||
            nombre.equals("avc_ano"))
          return rs.getInt(nombre);
        if (nombre.equals("cli_codpoe") )
            return rs.getString(nombre);
        if (nombre.equals("avc_fecalb"))
          return rs.getDate(nombre);
        if (jRField.getName().equals("avc_impiva"))
          return (Double) ht.get(nombre);

        if (nombre.equals("avc_impbru") ||         
            nombre.equals("avc_basimp") ||
            nombre.equals("avc_tipiva") ||
    //        jRField.getName().equals("avc_impiva") ||
            nombre.equals("avc_tipree") ||
            nombre.equals("avc_impree") ||
            nombre.equals("avc_impalb"))            
          return (Double) ht.get(nombre);
        if (   nombre.equals("avc_dtopp"))
            return (double)ht.get("avc_dtopp")+(double)ht.get("avc_dtocom");
        if (nombre.equals("avc_impdpp"))
            return (double)ht.get("avc_impdpp")+(double)ht.get("avc_impdco");
        if (nombre.equals("avt_numcaj"))
            return (int)ht.get("avt_numcaj");
        if (nombre.equals("avt_numpal"))
            return (int)ht.get("avt_numpal");
        if (nombre.equals("avt_numbol"))
            return (int)ht.get("avt_numbol");
        if (nombre.equals("avt_numcol"))
            return (int)ht.get("avt_numcol");
         if (nombre.equals("avt_portes") || nombre.equals("tra_pobl")  || nombre.equals("tra_nomb") || nombre.equals("tra_direc") || nombre.equals("tra_nif"))
            return (String)ht.get(nombre);
    throw new Exception("Campo "+jRField.getName()+" NO encontrado");
    } catch (Exception k)
    {
        k.printStackTrace();
      throw new JRException(k);
    }
  }

  public int impAlbaran(int empCodi,DatosTabla dtLin, DatosTabla dtCab, String sql,
                        EntornoUsuario EU, boolean valora) throws Exception
  {
    if (pdconfig.getCfgLialgr(empCodi,dtLin).equals("S"))
      return envAlbarFax(dtCab.getConexion().getConnection(), dtLin, sql, EU,valora,null,
                 null,true,1);
    else
      return impAlbarText(empCodi,dtLin,dtCab,sql,EU, valora);
  }
  private void getObsAlbaran(DatosTabla dt, int empCodi) throws SQLException
  {
    if (!dt.select("SELECT emp_obsalb FROM v_empresa WHERE emp_codi=" + empCodi))
      throw new SQLException("Empresa: " + empCodi + " NO ENCONTRADA");
    empObsAlb = dt.getString("emp_obsalb");  
  }

  /**
   *
   * @param empCodi
   * @param dtLin DatosTabla
   * @param dtCab DatosTabla
   * @param sql String
   * @param EU EntornoUsuario
   * @param valora boolean valora Imprimir el Albaran Valorado?
   * @throws Exception
   * @return int N. Albaranes Impresos
   */
  public int impAlbarText(int empCodi,DatosTabla dtLin, DatosTabla dtCab, String sql,
                         EntornoUsuario EU,boolean valora) throws Exception
  {

    getObsAlbaran(dtLin,empCodi);


    nAlbImp=0;
    int totUni;
    double impLin;
    DatosTabla dtStat=new DatosTabla(dtCab.getConexion());
    double totKgs;
    if (! dtCab.select(sql))
      return 0;
    this.dtLin=dtLin;
    this.dtCab=dtCab;
    this.valora=valora;
    File f=util.getFile("albven",EU);
    fOut = new FileOutputStream(f,false);
    String proNomb;
//    fOut= gnu.chu.print.util.getOutputStream("albven",EU);
    do
    {
      rs=dtCab.getResultSet();
      if (!getDatosAlb(dtCab.getInt("avc_ano"),dtCab.getInt("avc_empcod"),dtCab.getString("avc_serie"),
                      dtCab.getInt("avc_nume")))
        continue;
      totUni = 0;
      totKgs = 0;

      imprCabe(dtCab);
      s = getSqlLin();
      nLinALb = 0;
      if (dtLin.select(s))
      {
        do
        {
          impLin=Formatear.redondea(Formatear.redondea(dtLin.getDouble("avl_canti",true), 2) *
                                Formatear.redondea(dtLin.getDouble("avl_prven",true),3),2);
         proNomb=dtLin.getString("pro_nomb",true);
/**         if (proNomb.equals(""))
         {
           s="SELECT pro_nomb FROM v_articulo where emp_codi = "+dtCab.getInt("emp_codi")+
               " and pro_codi = "+dtLin.getInt("pro_codi");
           if (! dtStat.select(s))
             proNomb="**ARTICULO NO ENCONTRADO**";
           else
             proNomb=dtStat.getString("pro_nomb");
         }
*/
        print(Formatear.space(2) +
                Formatear.format(dtLin.getInt("pro_codi",true), "#99999") +
                Formatear.space(4) +
                Formatear.format(dtLin.getInt("avl_unid",true), "##9") +
                Formatear.space(2) +
                Formatear.ajusIzq(proNomb,30) +
                Formatear.space(2) +
                Formatear.format(Formatear.Redondea(dtLin.getDouble("avl_canti",true), 2),"---,--9.99") +
                Formatear.space(2) +
                (valora?Formatear.format(Formatear.Redondea(dtLin.getDouble("avl_prven",true),3), "--9.999"):"       ") +
                Formatear.space(2) +
                (valora?Formatear.format(impLin, "---,--9.99"):"          "));
          totUni+=dtLin.getInt("avl_unid",true);
          totKgs+=dtLin.getDouble("avl_canti",true);
          nLinALb++;
          if (nLinALb >= 11)
          {
            print(Formatear.space(3) + empObsAlb);
            println(11);
            imprCabe(dtCab);
            nLinALb = 0;
          }
        }  while (dtLin.next());
      }
      print(Formatear.space(3)+"  TOTAL "+Formatear.format(totUni,"###9")+
            Formatear.space(18)+" TOTAL KGS .... "+Formatear.format(totKgs,"---,--9.99"));
      println(10 - nLinALb);
      print(Formatear.space(3) + empObsAlb);
      println(3);
      if (valora)
      {
        print(Formatear.space(2) +
              Formatear.format(getValHash("avc_impbru"), "---,--9.99") +
              (getValHash("avc_dtopp") > 0 ?
               Formatear.space(1) +
               Formatear.format(getValHash("avc_dtopp"), "9.99") +
               Formatear.space(0) +
               Formatear.format(getValHash("avc_impdpp"), "---,--9.99") :
               Formatear.space(15)) +
              Formatear.space(1)+
              Formatear.format(getValHash("avc_basimp"), "---,--9.99") +
              Formatear.space(1) +
              Formatear.format(getValHash("avc_tipiva"), "9.9#") +
              Formatear.space(0) +
              Formatear.format(getValHash("avc_impiva"), "--,--9.99") +
              (dtCab.getInt("cli_recequ") != 0 ?
               Formatear.space(1) +
               Formatear.format(getValHash("avc_tipree"), "9.9#") +
               Formatear.space(1) +
               Formatear.format(getValHash("avc_impree"), "--,--9.99")+" " :
               Formatear.space(16))+
              Formatear.space(2) +
              Formatear.format(getValHash("avc_impalb"), "---,--9.99"));
      }
      else
        println(1);
      println(7);
    }  while (dtCab.next());
    fOut.close();
    Process pr=Runtime.getRuntime().exec(EU.pathCom+EU.comPrint+" "+
                                         f.getAbsolutePath()+" "+EU.puertoAlb);
    new threaEsp(pr);
    pr.waitFor();
    if (pr.exitValue()!=0)
      mensajes.mensajeUrgente("Error al Imprimir (Error: "+pr.exitValue()+")");
    dtStat.getStatement().close();
    return nAlbImp;
  }
  double getValHash(String name)
  {
    return ((Double) ht.get(name)).doubleValue();
  }

  private void imprCabe(DatosTabla dtCon1) throws SQLException, ParseException, IOException
  {
    print(util.INIT_PRINT);
//    println(1);
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_nomen"),37));
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_diree"),37));
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_codpoe") + "-" +
          dtCon1.getString("cli_poble"),37));
    println(5);
    print(Formatear.space(42) +
          Formatear.format(dtCon1.getInt("avc_empcod"), "99") +
          dtCon1.getString("avc_serie") + "/" +
          Formatear.format(dtCon1.getInt("avc_nume"), "999999") +
          Formatear.space(1) +
          dtCon1.getFecha("avc_fecalb", "dd-MM-yy") +
          Formatear.space(7) +
          Formatear.format(dtCon1.getInt("cli_codi"), "000000"));
    println(3);
  }

  String getSqlLin() throws SQLException,java.text.ParseException
  {
    return "SELECT l.pro_codi,sum(l.avl_canti) as avl_canti, " +
        " sum(avl_unid) as avl_unid, avl_prven,l.pro_nomb as pro_nomb " +
        " FROM V_ALBAVEL as l " +
        " WHERE l.avc_ano = " + dtCab.getInt("avc_ano") +
        " and l.emp_codi = " + dtCab.getInt("avc_empcod") +
        " and l.avc_serie = '" + dtCab.getString("avc_serie") + "'" +
        " and l.avc_nume = " + dtCab.getInt("avc_nume") +
        " and l.avl_canti >= 0 " +
        " group by l.pro_codi,avl_prven,l.pro_nomb " +
        " UNION ALL " +
        "SELECT l.pro_codi,sum(l.avl_canti) as avl_canti, " +
        " sum(avl_unid) as avl_unid, avl_prven,l.pro_nomb as pro_nomb " +
        " FROM V_ALBAVEL as l " +
        " WHERE l.avc_ano = " + dtCab.getInt("avc_ano") +
        " and l.emp_codi = " + dtCab.getInt("avc_empcod") +
        " and l.avc_serie = '" + dtCab.getString("avc_serie") + "'" +
        " and l.avc_nume = " + dtCab.getInt("avc_nume") +
        " and l.avl_canti < 0 " +
        " group by l.pro_codi,avl_prven,l.pro_nomb " +
        " ORDER BY 1";
  }

  void println(int nLin) throws IOException
  {
    for (int n=0;n<nLin;n++)
      print("");
  }
  void print(String str) throws IOException
  {
    print(str,true);
  }
  void print(String str,boolean saltoLinea) throws IOException
  {
    util.print(str,saltoLinea,fOut);
  }
  /**
   * Devuelve la maquina que hace de servidor de fax
   * Esta propiedad estara en config.properties.
   * 
   * @return nombre de host que hace de servidor de fax
   */
  public String getServFax()
  {
    return SendFax.getServFax();
  }
}

class threaEsp extends Thread
{
  Process pr;

  public threaEsp(Process p)
  {
    pr=p;
    this.start();
  }
  public void run()
  {
    try{
//      System.out.println("Esperando 2 segundos (threaEsp)");
      this.sleep(2000);
      pr.destroy();
    } catch (Exception k)
    {

    }
  }
}
