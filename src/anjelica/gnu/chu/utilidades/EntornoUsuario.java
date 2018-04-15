package gnu.chu.utilidades;

import gnu.chu.comm.Bascula;
import java.io.*;
import gnu.chu.sql.*;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.MissingResourceException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

 /**
 * Clase que mantiene ciertas variables criticas del usuario.
 * Una funcion de esta clase es pasada a todas los programas lanzados
 * por el Menu.
 *
 * @version 1.2
 *
 * <p>Copyright: Copyright (c) 2005-2009
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
*
*/
public class EntornoUsuario implements Serializable
{
  private String key="anJeLiCa";
  private Bascula  bascula=null;
  /*
   *  Usado para traspasos entre subempresas. La idea es crear un albaran de compra
   * y un albaran de venta. No usado actualmente.
   */
  public final static String SERIEY="Y"; 
  
  private java.util.ResourceBundle parametrosConfig=null;
  private String version="DESC";
  private Cipher ecipher;
  private Cipher dcipher;

  public int em_cod;
  public boolean debug=false;
  public String empresa=""; // Nombre de la empresa
  public int ejercicio;
  /**
   * Login del Usuario
   */  
  public String usuario; // Login del Usuario
  public String password;
  public String driverDB;
  public String addressDB;
  /**
   * Nombre completo del usuario
   */
  public String usu_nomb; // Nombre Completo del usuario.
  public String email;
  public vlike lkEmpresa = new vlike();
  private vlike lkUsuario;
  public String catalog=null;
  public int modo;
  public String pathReport;
  private String pathReportAlt=null; // Alternativa al Path Report
  public String dirTmp="";
  public String comPrint;
  public String puertoAlb;
  public String pathCom;
  public boolean previsual=false; // Mostrar Previsualizacion de Listado
  public boolean dialogoPrint=false; // Mostrar dialogo de impresion
  boolean simulaPrint=false;
             // Simula Impresion pero NO lo manda a impresora ni previsualiza (SOLO para pruebas)
  public String pathInstall="";
  private String usu_rese1;
  private String log4j=null;
  private HashMap<String,String> htParam=new HashMap();
  private HashMap<String,String> htPrinter=new HashMap();
  

  public EntornoUsuario( int emCod, String emNomb, int ejeCodi, String usua,
                        String passwd,
                        String driver, String urlDB)
  {
    this(emCod, emNomb, ejeCodi, usua, passwd, driver, urlDB, 0,null,null);
  }

  public EntornoUsuario(int emCod, String emNomb, int ejeCodi, String usua,
                        String passwd, String driver, String urlDB, int modo, String email,
                        String usuNomb)
  {
    em_cod = emCod;
    empresa = emNomb;
    ejercicio = ejeCodi;
    usuario = usua;
    password = passwd;
    driverDB = driver;
    addressDB = urlDB;
    this.modo = modo;
    this.email = email;
    this.usu_nomb = usuNomb;
    try {
        initCrypt();
    }catch (Exception k)
    {
          System.out.println("Error al inicializar Encriptacion");
    }
//    internacional();

  }
  
  public EntornoUsuario()
  {
        try {
            initCrypt();
        }catch (Exception k)
        {
              System.out.println("Error al inicializar Encriptacion");
        }
  }

//  public EntornoUsuario(int p1, String p2, int p3, String p4, String p5)
//  {
//
//    this(p1, p2, p3, p4, p5, "oracle.jdbc.driver.OracleDriver",
//         "jdbc:oracle:thin:@192.168.0.7:1521:prue", 0);
//  }

    @Override
  public Object clone()
  {
    EntornoUsuario EU = new EntornoUsuario();

    EU.em_cod = em_cod;
    EU.empresa = empresa;
    EU.ejercicio = ejercicio;
    EU.usuario = usuario;
    EU.password = password;
    EU.driverDB = driverDB;
    EU.addressDB = addressDB;
    EU.usu_nomb = usu_nomb;
    EU.email = email;
    EU.lkEmpresa = (vlike) lkEmpresa.clone();

    EU.modo = modo;
    EU.pathReport=pathReport;
    EU.pathReportAlt=pathReportAlt;
    EU.catalog=catalog;
    EU.debug=debug;
    
    EU.dirTmp = dirTmp;
    EU.comPrint = comPrint;
    EU.puertoAlb = puertoAlb;
    EU.pathCom = pathCom;
    EU.htPrinter=htPrinter;

    return EU;
  }
  
   public void setUsuReser1(String usu_reser1)
   {
     usu_rese1=usu_reser1;
   }
   public String getUsuReser1()
   {
     return usu_rese1;
   }
   /**
    * Indica si el usuario tiene permiso para ver ciertos mvtos. ocultos
    * @return
    */
   public boolean isRootAV()
   {
     return usu_rese1.equals("S");
   }
   public void setLog4J(String log4j)
   {
     this.log4j=log4j;
   }
   public String getLog4J()
   {
     return this.log4j;
   }
   public String getVersion()
   {
     return this.version;
   }
   public void setVersion(String version)
   {
      this.version=version;
   }

   public void setSimulaPrint(boolean simPrint)
   {
     simulaPrint = simPrint;
   }

   public boolean getSimulaPrint()
   {
     return this.simulaPrint;
   }
   public void setLikeUsuario(vlike lkUsua)
   {
     this.lkUsuario=lkUsua;
   }

   public vlike getLikeUsuario()
   {
     return this.lkUsuario;
   }
   public int getSbeCodi()
   {
     return getSbeCodi(null);
   }
   public int getSbeCodi(DatosTabla dt)
   {
     try {
       if (getLikeUsuario()==null && dt!=null)
         setLikeUsuario(gnu.chu.anjelica.pad.pdusua.getVLikeUsuario(usuario,dt));
       return getLikeUsuario().getInt("sbe_codi");
     } catch (Exception k)
     {
      SystemOut.print(k);
     }
     return 0;
   }
   /**
    * Devuelve la contraseña numerica (corta) de un usuario
    * @param dt
    * @return
    * @throws SQLException 
    */
   public int getClaveUsuario(DatosTabla dt) throws SQLException
   {
       if (getLikeUsuario()==null && dt!=null)
         setLikeUsuario(gnu.chu.anjelica.pad.pdusua.getVLikeUsuario(usuario,dt));
       return getLikeUsuario().getInt("usu_clanum");
   }
   /**
    * Devuelve si el usuario es administrador de la base de datos
    * @param dt DatosTabla usado en caso de que no se haya inicialiado el VLike
    * @return true si es administrador de base de datos.
    * @throws SQLException 
    */
   public boolean isAdminDB(DatosTabla dt) throws SQLException
   {
   
     if (getLikeUsuario()==null && dt!=null)
         setLikeUsuario(gnu.chu.anjelica.pad.pdusua.getVLikeUsuario(usuario,dt));
       
     return getLikeUsuario().getString("usu_admdb").equals("S");    
   }
    /**
    * Devuelve si el usuario es administrador de Bloqueos
    * @param dt DatosTabla usado en caso de que no se haya inicialiado el VLike
    * @return true si es administrador de bloqueos
    * @throws SQLException 
    */
   public boolean isAdminLock(DatosTabla dt) throws SQLException
   {
   
     if (getLikeUsuario()==null && dt!=null)
         setLikeUsuario(gnu.chu.anjelica.pad.pdusua.getVLikeUsuario(usuario,dt));
       
     return getLikeUsuario().getInt("usu_adlock")!=0;    
   }
   /**
    * Inicializa HashMap con los valores de la tabla parametros
    * @param dt
    * @throws SQLException 
    */   
   public void iniciarParametros(DatosTabla dt) throws SQLException
   {
       htParam.clear();
       String s="select par_nomb,par_valor from parametros where usu_nomb = '*'";
       if (dt.select(s))
       {
        do
        {
           htParam.put(dt.getString("par_nomb"),dt.getString("par_valor"));
         } while (dt.next());
       }
       s="select par_nomb,par_valor from parametros where usu_nomb ='"+usuario+"'";
       if (dt.select(s))
       {
           do
           {
               htParam.put(dt.getString("par_nomb"),dt.getString("par_valor"));
           } while (dt.next());
       }
   }
   /**
    * Devuelve el login del usuario
    * @return  login del usuario (Ejemplo: luis)
    */
   public String getUsuario()
   {
       return usuario;
   }
   /**
    * Deveuelve el nombre completo del usuario
    * @return  Nombre completo del usuario (Ej: Jesus Javier xxx uyyy)
    */
   public String getNombreUsuario()
   {
       return usu_nomb;
   }
   /**
    * Devuelve un valor buscado en la tabal parametros
    * @param param Parametro a buscar
    * @param paramDef Valor a devolver si no encuentra el parametro
    * @return 
    */
   public boolean getValorParam(String param,boolean paramDef)
   {
       if (htParam.get(param)==null)
           return paramDef;
       try {
         return Integer.parseInt(htParam.get(param)) != 0;
       } catch ( NumberFormatException k)
       {
           return paramDef;
       }
   }
   /**
    * Devuelve un valor buscadondolo en la tabal parametros
    * @param param Parametro a buscar
    * @param paramDef Valor a devolver si no encuentra el parametro
    * @return valor en la tabla parametros  o paramDef si no lo encuentra.
    */
   public int getValorParam(String param,int paramDef)
   {
       if (htParam.get(param)==null)
           return paramDef;
       try {
        return  Integer.parseInt(htParam.get(param));
       } catch ( NumberFormatException k)
       {
           return paramDef;
       }
   }
   public String getValorParam(String param,String paramDef)
   {
       if (htParam.get(param)==null)
           return paramDef;
       return htParam.get(param);       
   }
   public void setParametrosConfiguracion(java.util.ResourceBundle paramConfig)
   {
     parametrosConfig=paramConfig;
   }

   public java.util.ResourceBundle getParametrosConfiguracion()
   {
     return parametrosConfig;
   }
    public String getPathReportAlt() {
        return pathReportAlt;
    }

    public void setPathReportAlt(String pathReportAlt) {
        this.pathReportAlt = pathReportAlt;
    }
    /**
     * Devuelve un parametro del entorno de Anjelica (anjelica.confifg)
     * @param key Parametro
     * @param valor Valor a devolver si no lo encuentra
     * @return Valor del parametro
     */
   public String getParametroConfig(String key, String valor)
   {
     try
     {
       if (parametrosConfig==null)
       {
         String raiz = System.getProperty("raiz");
         if (raiz == null)
           raiz = "gnu.chu";
         setParametrosConfiguracion(java.util.ResourceBundle.getBundle(raiz + ".config"));

       }
       return parametrosConfig.getString(key);
     } catch (MissingResourceException k)
     {
       return valor;
     }
   }
   public void setImpresoras(HashMap printers)
   {
       htPrinter=printers;
   }
   public HashMap getImpresoras()
   {
       return htPrinter;
   }
   public void setImpresora(String clase,String nombre)
   {
        htPrinter.put(clase, nombre);
   }
   public String getImpresora(String clase)
   {
       return htPrinter.get(clase);
   }
   public Bascula getBascula()
   {
        if (bascula==null)
            bascula=new Bascula();
        return bascula;
   }

   public void initCrypt() throws Exception
   {
      byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
      
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skey = new SecretKeySpec(Formatear.getMD5(this.key), "AES");

        // CBC requires an initialization vector
        ecipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
     }

     public  String encryptAES(String plaintext) throws Exception {
            byte[] ciphertext = ecipher.doFinal(plaintext.getBytes("UTF-8"));
            return Formatear.byteToHex(ciphertext);
     }
     public  String decryptAES(String hexCipherText) throws Exception {
         String plaintext = new String(dcipher.doFinal(Formatear.hexToByte(hexCipherText)), "UTF-8");
         return  plaintext;
     }
}
