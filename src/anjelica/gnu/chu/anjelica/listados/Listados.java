package gnu.chu.anjelica.listados;

import gnu.chu.interfaces.ejecutable;
import java.sql.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
/*
 * Utilidades para listados
 * <p>Copyright: Copyright (c) 2005-2016</p>
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
public class Listados
{
    public final static  int TRAZA=1;
    public final static int CAB_FRV=2;    
    public final static int LIN_FRV=3;
    public final static int PCO_FRV=5; // Listado Comentario Pie
    public final static int CABPI_FRV=6; // CABECERA listado fra. PreImpreso
    
    public final static int CAB_AVC=7;
    public final static int LIN_AVC=8;
    public final static int LINENT_AVC=9;
    public final static int PALE_AVC=10;
    public final static int TARIFA=11; // Tarifa precios
    private String nombFich;
    private String nombLogo;
    private int codListado;
    private int empCodi;

    public int getEmpCodi() {
        return empCodi;
    }

    public void setEmpCodi(int empCodi) {
        this.empCodi = empCodi;
    }
    
    public Listados(int empCod,int codList,String nombFich,String nombLogo)
    {
        this.empCodi=empCod;
        codListado=codList;
        this.nombFich=nombFich;
        this.nombLogo=nombLogo;
    }
    
    public int getCodListado() {
        return codListado;
    }

    public void setCodListado(int codListado) {
        this.codListado = codListado;
    }
    /**
     * @deprecated usar getNombreFichero
     * @return 
     */
    public String getNombFich() {
        return getNombreFichero();
    }
    /**
     * 
     * @param incExtension
     * @return 
     */
    public String getNombreFichero(boolean incExtension) {
        if (incExtension)
        {
            if (nombFich.endsWith(".jasper"))
              return nombFich;
            else
              return nombFich+".jasper";
        }
        if (nombFich.endsWith(".jasper"))
            return nombFich.substring(0,nombFich.length()-7);
        return nombFich;
    }
    public String getNombreFichero() {
      return getNombreFichero(true);
    }

    public void setNombFich(String nombFich) {
        this.nombFich = nombFich;
    }

    public String getNombLogo() {
        if (nombLogo.equals(""))
            nombLogo="logotipo.jpg";      
        return nombLogo;
    }

    public void setNombLogo(String nombLogo) {
        if (nombLogo.equals(""))
            nombLogo="logotipo.jpg";
        this.nombLogo = nombLogo;
    }
    
    public  static Listados getListado(int empCodi,int codList, DatosTabla dtStat) throws SQLException
    {
      String s;
      s="SELECT * FROM listados WHERE emp_codi = "+empCodi+
          " and lis_codi = "+codList;
      if (dtStat.select(s))
      {
          return new Listados(empCodi,codList,dtStat.getString("lis_file"),dtStat.getString("lis_logo"));
      }
      s="SELECT * FROM listados WHERE emp_codi = 0"+ // Busco para empresa General
          " and lis_codi = "+codList;
      if (dtStat.select(s))
        return new Listados(empCodi,codList,dtStat.getString("lis_file"),dtStat.getString("lis_logo"));
      
      throw new SQLException("NO encontrado codigo Listado: "+codList+ " en Empresa: "+empCodi);
    }
    public  static String getNombListado(int empCodi,int codList, DatosTabla dtStat) throws SQLException
    {
        Listados lis=getListado(empCodi,codList,dtStat);
        return lis.getNombFich();
    }
    
    public static File getFileJasperReport(EntornoUsuario EU, String fichJasper,String extension)
    {
      File f;
      if (! fichJasper.endsWith(".jasper"))
          fichJasper+= extension+".jasper";
      if (EU.getPathReportAlt() != null)
      {
          f=new File(EU.getPathReportAlt()+ fichJasper );
          if (f.exists()){
            return f;            
          }
      }
      f=new File(EU.pathReport+ fichJasper);
      if (f.exists())
          return f;            
      return null;
    }
        
    public static JasperReport  getJasperReport(EntornoUsuario EU, String fichJasper) throws JRException
    {
      File f=getFileJasperReport(EU,fichJasper,"");    
      JasperReport jr = (JasperReport) JRLoader.loadObjectFromFile(f.getAbsolutePath());
      return jr;
    }
    public String getPathLogo()
    {
         return Iconos.getPathIcon() +getNombLogo();
    }
    public static HashMap getHashMapDefault()
    {
        HashMap hm=new HashMap();
        ResourceBundle rsB=ResourceBundle.getBundle("gnu.chu.anjelica.locale.jasper",ejecutable.local);
        hm.put(JRParameter.REPORT_LOCALE,ejecutable.local);
        hm.put(JRParameter.REPORT_RESOURCE_BUNDLE,rsB);
        return hm;
    }
}
