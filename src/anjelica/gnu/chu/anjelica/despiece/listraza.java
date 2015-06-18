package gnu.chu.anjelica.despiece;

import gnu.chu.hylafax.SendFax;
import gnu.chu.mail.MailHtml;
import gnu.chu.print.util;
import net.sf.jasperreports.engine.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * <p>Título: listraza </p>
 * <p>Descripción: Genera un listado con la trazabilidad de los productos.
 *  existentes en un albaran.</p>
 *  <p>Copyright: Copyright (c) 2005-2015
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
 * @author ChuchiP
 * @version 1.0
 */

public class listraza  implements JRDataSource
{
  int cliCodi;
  SendFax sendFax=null;
  boolean copiaPapel=true;
  String numFax=null;
  String subject;
  String emailCC;
  String asunto;
  boolean swListar=true;
  private  String msgLog="";
  static final int CODLISTA=1;
  DatosTabla dtStat,dtCon1,dtCur;
//  Statement st;
//  ResultSet rs;
  EntornoUsuario EU;
  utildesp utdes=null;
  int numAlb,empCodi,ejeNume;
  String serie,artVenta;
  boolean preview=false;
  ArrayList datos = new  ArrayList();
  int nElem=0; // Elemento en el arrayList
  private String s;
  private String namField;
  private DatosTraza dtTrz; // Datos de Trazabilidad Activos
  private DatosTrazaGrid dtGrid=null;
  ventana  padre;

  class DatosTraza
  {

      String ntrazaE,nacidoE,sacrificadoE,despiezadoE,cebadoE;
      String artVenta,pro_nomb,avp_serlot,msgLog;
      Double avp_canti;
      Integer proCodi,avp_numpar,avp_numind;
      java.util.Date fecSacrE,fecCaduE;

      DatosTraza(String ntrazaE,String nacidoE,String sacrificadoE,
              String despiezadoE,String cebadoE,
            String artVenta,java.util.Date fecSacrE,java.util.Date fecCaduE,
            String pro_nomb,
            Double avp_canti,Integer avp_numind,Integer avp_numpar,String avp_serlot,
            Integer proCodi,String msgLog)
      {
          this.ntrazaE=ntrazaE;
          this.nacidoE=nacidoE;
          this.sacrificadoE=sacrificadoE;
          this.despiezadoE=despiezadoE;
          this.cebadoE=cebadoE;
          this.artVenta=artVenta;
          this.fecSacrE=fecSacrE;
          this.fecCaduE=fecCaduE;
          this.pro_nomb=pro_nomb;
          this.avp_canti=avp_canti;
          this.avp_numpar=avp_numpar;
          this.avp_numind=avp_numind;
          this.avp_serlot=avp_serlot;
          this.msgLog=msgLog;
          this.proCodi=proCodi;
      }
      public Object getCampo(String namField)
      {
       if (namField.equals("crotal"))
        return ntrazaE;
      if (namField.equals("matadero"))
        return sacrificadoE;
      if (namField.equals("salaDespiece"))
        return despiezadoE;
      if (namField.equals("nacido"))
        return nacidoE;
      if (namField.equals("engordado"))
        return cebadoE;
      if (namField.equals("artVenta"))
        return artVenta;
      if (namField.equals("fecSacri"))
        return fecSacrE;
      if (namField.equals("pro_nomb"))
        return pro_nomb;
      if (namField.equals("avp_canti"))
        return avp_canti;
      if (namField.equals("avp_serlot"))
        return avp_serlot;
      if (namField.equals("avp_numpar"))
        return avp_numpar;
      if (namField.equals("avp_numind"))
        return avp_numind;
       if (namField.equals("fecCaduc"))
        return fecCaduE;
       return proCodi;
      }

      ArrayList getVector()
      {
          ArrayList v=new ArrayList();
          v.add(proCodi);
          v.add(pro_nomb);
          v.add(avp_serlot+" "+avp_numpar+"-"+avp_numind);
          v.add(avp_canti);
          v.add(ntrazaE);
          v.add(sacrificadoE);
          v.add(despiezadoE);
          v.add(nacidoE);
          v.add(cebadoE);
          v.add(artVenta);
          v.add(fecSacrE);
          v.add(msgLog);
          v.add(fecCaduE);
          return v;
      }
      void actualiza(String ntrazaE,String sacrificadoE,
              String despiezadoE,String nacidoE,String cebadoE,
            String artVenta,
            java.util.Date fecSacrE,java.util.Date fecCaduE)
      {
          this.ntrazaE=ntrazaE;
          this.sacrificadoE=sacrificadoE;
          this.despiezadoE=despiezadoE;
           this.nacidoE=nacidoE;
          this.cebadoE=cebadoE;
          this.artVenta=artVenta;
          this.fecSacrE=fecSacrE;
          this.fecCaduE=fecCaduE;
      }
  }
  public listraza(DatosTabla dtCur,DatosTabla dtStat,DatosTabla dtCon1,EntornoUsuario eu, ventana padre)
  {
    this.dtCur=dtCur;
    this.dtStat=dtStat;
    this.dtCon1=dtCon1;
    this.EU=eu;
    this.padre=padre;
  }
  public void setPreview(boolean prev)
  {
    preview=prev;
  }
  public void setRepiteIndiv(int numRepet)
  {
      if (utdes==null)
        utdes=new utildesp();
      utdes.setRepiteIndiv(numRepet);
  }
  public void setBuscaIndDesp(boolean buscaIndDesp)
  {
     if (utdes==null)
        utdes=new utildesp();
      utdes.setBuscaIndDesp(buscaIndDesp);
  }
  public void setDatosAlbaran(int numAlb, int empCodi,int ejeNume,String serie)
  {
    this.numAlb=numAlb;
    this.empCodi=empCodi;
    this.ejeNume=ejeNume;
    this.serie=serie;        
  }
  public void setCliente(int cliCodi)
  {
      this.cliCodi=cliCodi;
  }
  /**
   * Carga datos de trazabilidad
   * @return 0 si todo ha ido bien. 1 Si se han editado los datos. -1 en caso de error.
   * @throws SQLException
   * @throws JRException 
   */
  public int cargaDatosTraz() throws SQLException, JRException 
  {
     msgLog="";
    if (utdes==null)
      utdes=new utildesp();
    
    if (utdes.getRepiteIndiv()>0)
        utdes.resetIndividuos();
    
    s="select a.fam_codi,a.pro_nomb,p.*,l.pro_codi as proCodAlb "+
        " from v_albvenpar as p,v_articulo as a,v_albavel as l "+
        " where p.emp_codi = "+empCodi+
        " and p.avc_ano= "+ejeNume+
        " and p.avc_nume="+numAlb+
        " and p.avc_serie = '"+serie+"'"+
        " and avp_canti != 0 "+
        " and p.pro_codi=a.pro_codi "+
        " and l.emp_codi = p.emp_codi "+
        " and l.avc_ano = p.avc_ano  "+
        " and l.avc_nume = p.avc_nume "+
        " and l.avc_serie = p.avc_serie  "+
        " and l.avl_numlin = p.avl_numlin "+
        " ORDER BY p.AVL_NUMLIN,p.AVP_NUMLIN ";

    if (! dtCur.select(s))
    {
      mensajes.mensajeAviso("(listraza) No encontrados datos para imprimir");
      return -1;
    }
    datos.clear();
    boolean ret;
    String msgLogL;
    do
    {
        artVenta = null;

         ret=utdes.busDatInd(dtCur.getString("avp_serlot"),dtCur.getInt("pro_codi"),
                          dtCur.getInt("avp_emplot"),
                      dtCur.getInt("avp_ejelot"),dtCur.getInt("avp_numpar"),
                      dtCur.getInt("avp_numind"),
                      dtCon1,dtStat,EU);
        msgLogL=utdes.getMsgAviso();
        if (msgLogL != null) {
            msgLogL = "Producto: " + dtCur.getInt("pro_codi") + " Individuo: " +
                    + dtCur.getInt("avp_ejelot") + dtCur.getString("avp_serlot")+
                    dtCur.getInt("avp_numpar") + "-"
                    + dtCur.getInt("avp_numind")+"\n"
                    + msgLogL + "\n";
            msgLog+=msgLogL;
        }
    
        if (utdes.swDesp)
        {
            s = "SELECT pro_nomb FROM v_articulo WHERE pro_codi = " +
                    utdes.proCodiDes;
            if (!dtStat.select(s))
              throw new JRException("Articulo " + utdes.proCodiDes +" NO ENCONTRADO");
            artVenta = Formatear.format(utdes.proCodiDes, "#####9") + "  " +
                dtStat.getString("pro_nomb");
        }
        else
        {
            artVenta=Formatear.format(dtCur.getInt("pro_codi"), "#####9") + "  " +
                dtCur.getString("pro_nomb");
        }
        DatosTraza dtTraza=new DatosTraza(utdes.ntrazaE,
                utdes.nacidoE,utdes.sacrificadoE,utdes.despiezadoE,utdes.cebadoE,
                artVenta,  utdes.getFecSacrif() ,utdes.getFecCaduc(), dtCur.getString("pro_nomb"),
                dtCur.getDouble("avp_canti"),
                dtCur.getInt("avp_numind"),
                dtCur.getInt("avp_numpar"),
                dtCur.getString("avp_serlot"),
                dtCur.getInt("pro_codi"),msgLogL
                );
        datos.add(dtTraza);
    } while (dtCur.next());
    if (!msgLog.equals(""))
    {
        int LIMLONGITUD=100;
        if (msgLog.length()>LIMLONGITUD)
            msgLog=msgLog.substring(0,LIMLONGITUD-1)+" ... ";
        if (mensajes.mensajeYesNo("Incidencias en Listado Trazabilidad\n"+ msgLog+
                " Editar campos ? ")==mensajes.YES)
        {
            cargaGrid();
            return 1;
        }
    }
    return 0;
  }
  
  public void lista(int numAlb, int empCodi,int ejeNume,String serie) throws Exception
  {
    swListar=true;
    copiaPapel=true;
    setDatosAlbaran(numAlb, empCodi, ejeNume, serie);
    if (cargaDatosTraz()==0)        
        imprimir();
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
  public void envHojaEmail() throws Exception
  {
      copiaPapel=false;
      swListar=false;
      imprimir();
  }
  public void setCopiaPapel(boolean copiaPapel)
  {
      this.copiaPapel=copiaPapel;
  }
   public void setAsunto(String asunto)
   {
       this.asunto=asunto;
   }
   
   public void setToEmail(String toEmail)
   {
       numFax=toEmail;
   }
  public void imprimir() throws Exception
  {
    String nombJasper=gnu.chu.anjelica.listados.utillista.getNombList(empCodi,CODLISTA,dtStat);
    nElem=0;
    JasperReport jr = gnu.chu.print.util.getJasperReport(EU, nombJasper);
    java.util.HashMap mp = new java.util.HashMap();
    mp.put("albaran", Formatear.format(empCodi,"99")+serie+"/"+numAlb);

    JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);

    if (copiaPapel)
     gnu.chu.print.util.printJasper(jp, EU);
   if (numFax==null)
     return ; // Solo copia a Papel
   
//   System.out.println("Dir. temporal: "+EU.dirTmp);
   
   File fichPDF=util.getFile("hojatra",EU, "pdf",true) ;
   FileOutputStream outStr=new FileOutputStream(fichPDF);
   JasperExportManager.exportReportToPdfStream(jp,outStr);
   outStr.close();
   if (numFax.indexOf("@")>0)
   { // Es un correo.        
       MailHtml correo=new MailHtml(EU.usu_nomb,EU.email);
       correo.setEmailCC(emailCC);
       correo.enviarFichero(numFax,asunto,subject,
          fichPDF,"hojatra.pdf");       
       return;
   }
   if (sendFax==null)
       sendFax=new SendFax(EU,dtCon1); // Inicio clase SendFax
   sendFax.setCliente(cliCodi);
   
   sendFax.sendFax("A",empCodi,ejeNume,serie, numAlb,  numFax, fichPDF.getAbsolutePath());
  

  
  }

  public boolean next() throws JRException
  {
        if (nElem>=datos.size())
         return false;
        dtTrz=(DatosTraza) datos.get(nElem);
        nElem++;
        return true;
  }

  public Object getFieldValue(JRField jRField) throws JRException
  {
      namField=jRField.getName();
      return dtTrz.getCampo(namField);
  }
  void cargaGrid()
  {
    try
    {
      if (dtGrid == null)
      {
        dtGrid = new DatosTrazaGrid()
        {
          @Override
          public void matar()
          {
            salirCargaGrid(dtGrid);
          }
        };
        padre.vl.add(dtGrid);
        dtGrid.setLocation(25, 25);
      }
      dtGrid.setVisible(true);
      dtGrid.setSelected(true);
      padre.setFoco(dtGrid);
      dtGrid.iniciarVentana(datos);
    
    }
    catch (Exception j)
    {

    }
  }

  void salirCargaGrid(DatosTrazaGrid dtGrid)
  {
      dtGrid.setVisible(false);
      padre.toFront();
      try
      {
        padre.setSelected(true);
      }
      catch (Exception k)
      {}
      padre.setFoco(null);
      try {
        int nRow=datos.size();
        for (int n=0;n<nRow;n++)
        {
            dtTrz= (DatosTraza)datos.get(n);
            dtTrz.actualiza(
                   dtGrid.getGrid().getValString(n,4),
                   dtGrid.getGrid().getValString(n,5),
                   dtGrid.getGrid().getValString(n,6),
                   dtGrid.getGrid().getValString(n,7),
                   dtGrid.getGrid().getValString(n,8),
                   dtGrid.getGrid().getValString(n,9),
                   dtGrid.getGrid().getValDate(n,10),
                   dtGrid.getGrid().getValDate(n,12)
                   );
        }
        if (swListar)
            imprimir();
      } catch (Exception k)
      {
          padre.Error("Error al imprimir hoja trazabilidad", k);
      }
  }
  
}
