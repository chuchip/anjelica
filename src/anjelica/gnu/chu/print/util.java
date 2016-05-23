package gnu.chu.print;

import gnu.chu.anjelica.listados.Listados;
import java.io.*;
import gnu.chu.utilidades.*;
import java.awt.Component;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Date;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import javax.print.*;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JFileChooser;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * <p>Título: util</p>
 * <p>Descripción: Diferentes rutinas con utilidades para listar tanto con
 *  JasperReport como en fichero plano.
 *
 *   <p>Copyright: Copyright (c) 2005-2010
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
 * @author chuchi P.
 * @version 1.0
 */

public class util
{
  public static final String ALBARAN="ALBARAN";
  public static final String FACTURA="FACTURA";
  public static final String ETIQUETA1="ETIQUETA1";
  public static String SALTO_LINEA = "" + ((char)10);// System.getProperty("line.separator");
  // Codigos  tipo ESC/P de epson.
  public static String INIT_PRINT=(char)27+"@"+ // Resetea la Impresora
          (char)27+"t"+  (char)1+ // Asigna la tabla de caracteres PC437
          (char)27+"6"+ // permite a la impresora imprimir codigos altos
          (char)27+"P"+ // Seleciona caracteres 10 CPI
          (char)27+"x0"+ // La pone en modo DRAFT.
          (char)27+"R"+(char)11+ // Seleciona un juego de caracteres internacional 11 -> SPAIN 2
          (char)27+(char) 18;
  public static File getFile(String file,EntornoUsuario eu) throws java.io.IOException
  {
      return getFile(file,eu, "prn",true);
  }
/**
 * Abre un fichero cuyo nombre empieza por file en el directorio tmp y con un numero secuencial
 * al ultimo que ya exista
 * @param file String Cominenzo del fichero a abrir
 * @param eu  EntornoUsuario
 * @param ext Extension
 * @param removeOld Intenta borrar ficheros antiguos, si no hay mas disponibles
 * @return File descriptor 
 * @throws java.io.IOException
 */
  public static File getFile(String file,EntornoUsuario eu,String ext,boolean removeOld) throws java.io.IOException
  {
    String path=eu.dirTmp;
    String numeral;
    
    File fichero;
    if ((fichero=getFichero(0, path, file, ext))!=null) 
            return fichero;
    
    if (removeOld)
    {
        // Intento borrar ficheros que tengan mas de 7 dias.
        for (int n=0;n<1000;n++)
        {
          numeral=Formatear.format(n,"999");
          fichero = new File(path, file+numeral+"."+ext);
          if (fichero.exists())
          {
              if (Formatear.comparaFechas(new Date(fichero.lastModified()),Formatear.getDateAct())>3)
                  fichero.delete();
          }
        }
        if ((fichero=getFichero(0, path, file, ext))!=null) 
            return fichero;
        int ficBor=0;
        for (int n=0;n<1000 && ficBor <50 ;n++)
        {
          numeral=Formatear.format(n,"999");
          fichero = new File(path, file+numeral+"."+ext);
          if (fichero.exists())          
          {
             fichero.delete();
             ficBor++;
          }
        }
        if ((fichero=getFichero(0, path, file, ext))!=null) 
           return fichero;
    }
    throw new IOException("Mas de 999 Ficheros Temporales con el mismo Nombre");
  }
/**
 * Devuelve un puntero a un fichero nuevo para escribir.
 * Ese fichero es creado a traves de la cadean  'file', con extension 'ext'
 * Si ya esixte file.ext, busca file001.ext, file002.ext y asi sucesivamente, hata 999
 * si estan todos ocupados devuelve null.
 * @param n No usado.
 * @param path
 * @param file
 * @param ext
 * @return 
 */
  private static File getFichero(int n, String path, String file, String ext) 
  {
    String numeral;
    File fichero;
    for (; n < 1000; n++)
    {
        numeral = Formatear.format(n, "999");
        fichero = new File(path, file + numeral + "." + ext);
        if (!fichero.exists())
        {
            return fichero;
        }
    }
    return null;
  }
  
  public static FileOutputStream getOutputStream(String file,EntornoUsuario eu) throws java.io.IOException
  {
    return new FileOutputStream(getFile(file,eu),false);
  }

  public static void limpiaTmp(EntornoUsuario eu) throws java.io.IOException
  {
    File f=new File(eu.dirTmp);
    if (f==null)
      return; // NO existe el Directorio TEMPORAL.
    File fs[] = f.listFiles();
    if (fs==null)
      return;
    long timeAct=System.currentTimeMillis();
    timeAct-=3600*24*3*1000; // Hace 3 dias

    for (int n=0;n<fs.length;n++)
    {
      if (fs[n].isDirectory())
        continue;
      if (! fs[n].getName().endsWith(".prn"))
        continue;
      if (fs[n].lastModified()<timeAct)
        fs[n].delete();
    }
  }
 public static void println(int nLin,FileOutputStream fOut) throws IOException
 {
   for (int n=0;n<nLin;n++)
     print("",fOut);
 }
 public static void print(String str,FileOutputStream fOut) throws IOException
 {
   print(str,true,fOut);
 }
 public static void print(String str,boolean saltoLinea,FileOutputStream fOut) throws IOException
 {
   if (saltoLinea)
     str+=util.SALTO_LINEA;

   str = str.replace('á', (char) 92);
   str = str.replace('é', (char) 124);
   str = str.replace('í', (char) 64);
   str = str.replace('ó', (char) 94);
   str = str.replace('ú', (char) 123);
   str = str.replace('ñ', (char) 125);
   str = str.replace('Ñ', (char) 126);

   fOut.write(str.getBytes());
 }
 public static double getValHash(String name,java.util.Hashtable ht)
  {
    return ((Double) ht.get(name)).doubleValue();
  }

  public static boolean printJasper(JasperPrint jp, EntornoUsuario eu) throws JRException,PrinterException
  {
    return printJasper(jp,eu,0);
  }
  /**
   * @deprecated usar Listados.getJasperReport(EntornoUsuario EU, String fichJasper);
   * @param EU
   * @param fichJasper
   * @return
   * @throws JRException 
   */
   public static JasperReport  getJasperReport(EntornoUsuario EU, String fichJasper) throws JRException
   {
       return Listados.getJasperReport(EU, fichJasper);
   }
  /**
   *  Devuelve el path sobre un fichero de report
   * @param EU
   * @param fichJasper String (sin la terminación .jasper) del fichero a buscar
   * @return String con el path completo al fichero. Por ejemplo
   * si fichJasper era: 'albaran', devolveria '/anjelica/report/albaran.jasper'
   * @throws JRException
   */
  public static String  getPathReport(EntornoUsuario EU, String fichJasper) throws JRException
  {
      if (EU.getPathReportAlt() != null)
      {
          File f=new File(EU.getPathReportAlt()+ fichJasper + ".jasper");
          if (f.exists()){
            return EU.getPathReportAlt() + fichJasper + ".jasper";
          }
      }
      return EU.pathReport + fichJasper + ".jasper";
  }
  public static boolean printJasper(JasperPrint jp, EntornoUsuario eu, int numCopias) throws
      JRException,PrinterException
  {
        return printJasper(jp,eu,numCopias, null);
  }

  public static boolean printJasper(JasperPrint jp, EntornoUsuario eu, int numCopias,String impresora) throws
      JRException,PrinterException
  {
    if (jp.getPages().isEmpty())
    {
      javax.swing.JOptionPane.showMessageDialog(null, " NO hay datos para listar ", "!! AVISO !!",
                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
      return false;
    }

    if (eu.previsual)
    {
      JasperViewer.viewReport(jp, false);
      return true;
    }
    if (eu.dialogoPrint || numCopias <= 1)
    {
      JasperPrintManager.printReport(jp, eu.dialogoPrint);
      return true;
    }
    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
    int selectedService = 0;
    if (impresora!=null)
    {
        impresora=eu.getImpresora(impresora);
        if (impresora!=null)
        {
            PrinterJob job = PrinterJob.getPrinterJob();
            /* Scan found services to see if anyone suits our needs */
            for(int i = 0; i < services.length;i++){
                if(services[i].getName().toUpperCase().contains(impresora)){
                    /*If the service is named as what we are querying we select it */
                    selectedService = i;
                }
            }
            job.setPrintService(services[selectedService]);
        }
    }
    PrintService dfI = PrintServiceLookup.lookupDefaultPrintService();
//        System.out.println("Impresora por defecto:" + dfI.getName());
    // create the print service exporter so that we can print to a named printer
    JRPrintServiceExporter exporter = new JRPrintServiceExporter();
    exporter.setParameter(JRPrintServiceExporterParameter.JASPER_PRINT, jp);
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    aset.add(new javax.print.attribute.standard.Copies(numCopias));
//        aset.add(MediaSizeName.ISO_A4);
    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, aset);
    // let the exporter know which printer we want to print on
    PrintServiceAttributeSet serviceAttributeSet = new HashPrintServiceAttributeSet();
//     serviceAttributeSet.add(new PrinterName("HP LaserJet 4050 Series PCL6", null));
    serviceAttributeSet.add(new PrinterName(dfI.getName(), null));
//     serviceAttributeSet.add(dfI.getName());
    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
                          serviceAttributeSet);
    if (impresora!=null)
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
    exporter.exportReport();

    return true;
  }
  /**
   * Permite elegir un fichero.
   *
   * @param parent Ventana sobre la que mostrar el dialogo para abrir fichero
   * @return File o null si no se elige ningún fichero
   */
  public static File chooseFile(Component parent) throws Exception
  {
     return chooseFile(parent, null);
  }
  /**
   * Permite elegir un fichero.
   *
   * @param parent Ventana sobre la que mostrar el dialogo para abrir fichero
   * @param extension Extension a añadir al fichero.
   * @return File o null si no se elige ningún fichero
   */
  public static File chooseFile(Component parent,String extension) throws Exception
  {
     JFileChooser jf=new JFileChooser();
     if (extension!=null)
        jf.setFileFilter(new MiFileFilter(extension));
     jf.setName("Elija Fichero ");
     if ( jf.showOpenDialog(parent)!= JFileChooser.APPROVE_OPTION)
        return null;
     if (extension==null)
        return jf.getSelectedFile();
     if (jf.getSelectedFile().getAbsolutePath().endsWith("."+extension))
         return jf.getSelectedFile();
     return new File(jf.getSelectedFile()+"."+extension);
  }
}
class MiFileFilter extends javax.swing.filechooser.FileFilter
{

  public String extension;

  public MiFileFilter(String extension)
  {
      this.extension=extension;
  }
  public boolean accept(File pathname)
  {
    if (pathname.isDirectory())
      return true;
    if (pathname.getAbsolutePath().endsWith("."+extension))
      return true;
    return false;
  }

  public  String getDescription()
  {
    return extension;
  }
}
