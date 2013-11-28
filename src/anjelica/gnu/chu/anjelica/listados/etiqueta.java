package gnu.chu.anjelica.listados;

import gnu.chu.print.util;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.SQLException;
import net.sf.jasperreports.engine.*;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.linear.code128.Code128Barcode;

/**
 *
 * <p>Título: etiqueta</p>
 * <p>Descripción: Utilidades varias (metodos estaticos y no) para impresion etiquetas
 * de productos.</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * @author chuchiP
 * <p>Empresa: MISL</p>
 * @version 1.2
 */
public class etiqueta  extends JRDefaultScriptlet
{
  private String LOGOTIPO="logotipo_bn.jpg"; // Logotipo por defecto.
  String fichEtiq;
  EntornoUsuario EU;
  String codbarras;
  String lote;
  String codArti,articulo;
  String nacido;
  String cebado;
  String despiezado;
  String litFecha;
  String ntraza, peso, conservar, sacrificado;
  String fecrecep;
  java.util.Date fecprod=null;
  String feccadu=null;
  String fecCong=null;
  java.util.Date fecSacr=null;
  public final static int NORMAL=0;
  public final static int MINI=1;
  public final static int REDUCIDA=2;
  public final static int ETIQINT=3;
  boolean preVisual=false;
  boolean printDialog=false;
  boolean simulaPrint=false;
  String diremp=null;
  String datmat=null;
  String logotipo=null;
  int tipoEtiq=-1;
  JasperReport jr=null;
  int tipEtiqOld;
  private int numCopias=0;

  public etiqueta()
  { 
  }

  public etiqueta(EntornoUsuario eu)
  {
    this.EU=eu;
  }
  public void setPrintDialog(boolean printDialog)
  {
    this.printDialog=printDialog;
  }

  public boolean getPrintDialog()
  {
    return this.printDialog;
  }

  public void iniciar(String codbarras, String lote,String codArti,
                      String articulo,String nacido,String cebado,
                String despiezado,String ntraza,String peso,String conservar,
                String sacrificado,String fecrecep,Date fecSacr)
  {
    iniciar(codbarras,lote,codArti,articulo,nacido,cebado,despiezado,ntraza,peso,
            conservar,sacrificado,fecrecep, "Fec.Cad.",null,null,fecSacr);
  }
  public void iniciar(String codbarras, String lote,String codArti,
                      String articulo,String nacido,String cebado,
                  String despiezado,String ntraza,String peso,String conservar,
                  String sacrificado,String fecrecep, String litFecha,
                  java.util.Date fecprod,String feccadu,java.util.Date fecSacr)
  {
    this.codbarras=codbarras;
    this.lote=lote;
    this.codArti= codArti;
    this.articulo=articulo;
    this.nacido=nacido;
    this.cebado=cebado;
    this.despiezado=despiezado;
    this.ntraza=ntraza;
    this.peso=peso;
    this.conservar=conservar;
    this.sacrificado=sacrificado;
    this.fecrecep=fecrecep;
    this.fecprod=fecprod;
    this.litFecha=litFecha;
    this.feccadu=feccadu;
    this.fecSacr=fecSacr;
  }
  public void config(String dirEmp, String datmat,String logotipo)
  {
    this.diremp=dirEmp;
    this.datmat=datmat;
    this.logotipo=logotipo;
  }

  public void listar() throws Exception
  {
    listar(tipoEtiq,fichEtiq,logotipo);
  }

  public void listar(int tipEtiq) throws Exception
  {
    fichEtiq="etiqueta";
    if (tipEtiq==MINI)
      fichEtiq="etiqmini";
    if (tipEtiq==REDUCIDA)
      fichEtiq="etiqCom50";
    if (tipEtiq==ETIQINT)
      fichEtiq="etiqInt";
    listar(tipEtiq,fichEtiq,logotipo);
  }
  public String getDirEmpresa()
  {
      return this.diremp;
  }
  public void setDirEmpresa(String dirEmpresa)
  {
      this.diremp=dirEmpresa;
  }
   public String getDatMatadero()
  {
      return this.datmat;
  }
  public void setDatMatadero(String datMatadero)
  {
      this.datmat=datMatadero;
  }
  public void setLogotipo(String logotipo)
  {
      this.logotipo=logotipo;
  }
  public String getLogotipo()
  {
      return logotipo;
  }
  /**
   * Imprime una o varias etiqueta
   * @param tipEtiq Tipo Etiqueta.
   * @param fichEtiq Fichero jasper con la etiqueta a imprimir
   * @param logo Logo a poner en la etiqueta. Si el logo es null pone por defecto 'logo'.
   * El logo no deberia ser nunca nulo... ;)
   * @throws java.lang.Exception
   */
  public void listar(int tipEtiq,String fichEtiq,String logo) throws Exception
  {
    if (jr==null || tipEtiq!=tipEtiqOld)
      jr = util.getJasperReport(EU,fichEtiq);
     
    tipEtiqOld=tipEtiq;
    java.util.HashMap mp = new java.util.HashMap();
    
    mp.put("codbarra",codbarras);
    mp.put("lote",lote);
    mp.put("codarti",codArti);
    mp.put("articulo",articulo);
    mp.put("nacido",nacido);
    mp.put("cebado",cebado);
    mp.put("despiezado",despiezado);
    mp.put("ntraza",ntraza);
    mp.put("peso",peso);
    mp.put("conservar",conservar);
    mp.put("sacrificado",sacrificado);
    mp.put("fecrecep",fecrecep);
    mp.put("fecprod",litFecha);
    mp.put("feccadu",feccadu);
    mp.put("fecSacr",fecSacr);
    mp.put("congelado",fecCong);
    if (diremp!=null)
      mp.put("diremp",diremp);
    if (datmat!=null)
      mp.put("datmat",datmat);
    String img="";
    if (logo!=null)
    {
      if (! logo.equals(""))
        img=Iconos.getPathIcon()+logo;
    }
    else
      img=Iconos.getPathIcon()+LOGOTIPO;

    mp.put("logotipo",img.equals("")?null:img);

    JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JREmptyDataSource());
    if (EU.getSimulaPrint())
      return;
    gnu.chu.print.util.printJasper(jp, EU,numCopias);
//    if (preVisual || EU.previsual)
//      JasperViewer.viewReport(jp, false);
//    else
//    {
//      if (printDialog || EU.dialogoPrint)
//        JasperPrintManager.printReport(jp, true);
//      else
//      {
//        if (numCopias <= 1)
//          JasperPrintManager.printReport(jp, false);
//        else
//        { // Establece el numero de copias
//          PrintService dfI = PrintServiceLookup.lookupDefaultPrintService();
//          System.out.println("Impresora por defecto:" + dfI.getName());
//          // create the print service exporter so that we can print to a named printer
//          JRPrintServiceExporter exporter = new JRPrintServiceExporter();
//          exporter.setParameter(JRPrintServiceExporterParameter.JASPER_PRINT, jp);
//          PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//          aset.add(new Copies(numCopias));
////        aset.add(MediaSizeName.ISO_A4);
//          exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, aset);
//          // let the exporter know which printer we want to print on
//          PrintServiceAttributeSet serviceAttributeSet = new HashPrintServiceAttributeSet();
////     serviceAttributeSet.add(new PrinterName("HP LaserJet 4050 Series PCL6", null));
//          serviceAttributeSet.add(new PrinterName(dfI.getName(), null));
////     serviceAttributeSet.add(dfI.getName());
//          exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, serviceAttributeSet);
//          exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//          exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//          exporter.exportReport();
//        }
//      }
//    }
  }

    @Override
  public void afterReportInit() throws JRScriptletException
  {
    iniciaCodBarras();
  }

  void iniciaCodBarras() throws JRScriptletException
  {
    BufferedImage bufferedImage = null;
    try
    {
      Barcode barcode = new Code128Barcode(super.getParameterValue("codbarra").toString());
//      Barcode barcode =  BarcodeFactory.createCode128(super.getParameterValue("codbarra").toString());
      barcode.setBarWidth(1);
     barcode.setFont(new Font("Serif", Font.PLAIN, 12));
      bufferedImage = new BufferedImage(300, 60, BufferedImage.TYPE_BYTE_INDEXED);

      barcode.draw(bufferedImage.createGraphics(), 0, 0);
      bufferedImage = BarcodeImageHandler.getImage(barcode);
      super.setVariableValue("codbarras", bufferedImage);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new JRScriptletException(ex.getMessage());
    }

  }
  public void setPreVisual(boolean prVis)
  {
    preVisual=prVis;
  }
  public void setSimulaPrint(boolean simPrint)
  {
    simulaPrint=simPrint;
  }

  public boolean getSimulaPrint()
  {
    return this.simulaPrint ;
  }

  public boolean getPreVisual()
  {
    return preVisual;
  }
  /**
   * Llena un datostabla con las etiquetas disponibles
   * @param dt DatosTabla a llenar
   * @param empCodi empresa
   * @param incCliente Incluir etiquetas de cliente (0: no).
   * @return DatosTabla sobre el q se realizo la select
   * @throws java.sql.SQLException 
   */
  public static DatosTabla getReports(DatosTabla dt,int empCodi, int incCliente) throws SQLException
  {
    dt.select("select eti_codi,eti_nomb from etiquetas "+
             " WHERE emp_codi = "+empCodi+ 
             " and eti_client = "+incCliente+
             " order by eti_defec desc,eti_codi");
    return dt;
  }
  /**
   * Recoge los datos de Etiquetas
   * @param dt DatosTabla
   * @param empCodi int
   * @param etiCodi int
   * @throws SQLException
   * @throws ParseException
   * @return DatosTabla
   */
  public DatosTabla getDatosRep(DatosTabla dt,int empCodi,int etiCodi)  throws SQLException
  {
    String s="SELECT * FROM etiquetas WHERE emp_codi = "+empCodi+
        " and eti_codi = "+etiCodi;
    dt.select(s);
    return dt;
  }
  /**
   * Devuelve la etiqueta definida para un cliente
   * @param dt DatosTabla
   * @param empCodi Empresa
   * @param cliCodi Cliente
   * @return Codigo de etiqueta. -1 Si no existe el cliente.
   * @throws java.sql.SQLException
   */
  public static int getEtiquetaCliente(DatosTabla dt, int empCodi,int cliCodi) throws SQLException
  {
      String s="SELECT eti_codi FROM clientes WHERE emp_codi = "+empCodi+
           " and cli_codi = "+cliCodi;
      if (! dt.select(s))
          return -1;
      return dt.getInt("eti_codi");
  }
  
  public boolean setTipoEtiq(DatosTabla dt,int empCodi, int etiCodi)  throws SQLException
  {
    if (etiCodi==this.tipoEtiq)
      return true;
    if (etiCodi==0)
      return setEtiqDefault(dt,empCodi);
    dt=getDatosRep(dt,empCodi,etiCodi);
    if (dt.getNOREG())
      return false;
    tipoEtiq=dt.getInt("eti_codi");
    fichEtiq=dt.getString("eti_ficnom");
    logotipo=dt.getString("eti_logo");
    return true;
  }
  public boolean setEtiqDefault(DatosTabla dt,int empCodi) throws SQLException
  {
    String s="SELECT * FROM etiquetas WHERE emp_codi = "+empCodi+
        " AND eti_defec= 'S'";
    dt.select(s);
    if (dt.getNOREG())
      return false;
    tipoEtiq=dt.getInt("eti_codi");
    fichEtiq=dt.getString("eti_ficnom");
    logotipo=dt.getString("eti_logo");
    return true;
  }

  public void listarDefec() throws Exception
  {
    listar(tipoEtiq,fichEtiq,logotipo);
  }
  public void setNumCopias(int numCopias)
  {
    this.numCopias=numCopias;
  }
  public int getNumCopias()
  {
    return numCopias;
  }
  public void setFecSacrif(Date fecSacr)
  {
    this.fecSacr=fecSacr;
  }
  
  public void setFechaCongelado(String fecCong)
  {
    this.fecCong=fecCong;
  }
  public String getFechaCongelado()
  {
    return  fecCong;
  }
  public java.util.Date getFecSacrif()
  {
    return this.fecSacr;
  }

}
