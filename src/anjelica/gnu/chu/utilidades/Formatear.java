package gnu.chu.utilidades;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;
import java.text.*;
import javax.mail.internet.MimeUtility;

/**
 * Clase con funciones estaticas de variada utilidad.
 * Principalmente para formatear fechas y numeros.
 *
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 */
public class  Formatear
{
  public static final Locale local = Locale.US;//new Locale("es","es","");
  static boolean swFormat=false;
  public final static char DECIMALSEPARATOR='.';//new DecimalFormatSymbols().getDecimalSeparator();
  public final static char GROUPSEPARATOR=',';//new DecimalFormatSymbols().getGroupingSeparator();
  public final static char MINUSSIGN=new DecimalFormatSymbols().getMinusSign();
  public static String groupSeparator=",";//+GROUPSEPARATOR;
  public static String decimalSeparator=".";//+DECIMALSEPARATOR;



  /**
  * Comprueba si dos valores son igual con un margen de error
  * dado
  */
  public static boolean esIgual(double num1,double num2,double prec)
  {
    if (num1==num2)
      return true;
    if (num1>num2)
      return (num1-prec<=num2);
    else
      return (num2-prec<=num1);
  }
  /**
   * Redondea un numero a los decimales mandados. 
   * @param numero Numero a redondear
   * @param numeroDecimales numero de decimales a los q redondear
   * @deprecated  usear redondea (double numero, numeroDecimales)
   * @return Numero redondeado.
   */
  public static double Redondea(double numero, int numeroDecimales)
  {
   return redondea(numero, numeroDecimales);
  }
  /**
   * Redondea un numero a los decimales mandados, utiliza modo BigDecimal.ROUND_HALF_UP
   * @param numero Numero a redondear
   * @param numeroDecimales numero de decimales a los q redondear
   * @return Numero redondeado.
   */
  public static double redondea(double numero, int numeroDecimales)
  {      
        return redondea(numero, numeroDecimales,BigDecimal.ROUND_HALF_UP);
  }
   /**
   * Redondea un numero a los decimales mandados.
   * @param numero Numero a redondear
   * @param numeroDecimales numero de decimales a los q redondear
   * @param tipoRedondeo Especifica el tipoRedondeo como en clase BigDecimal
   * @see  java.math.RoundingMode
  
   * @return Numero redondeado.
   */
      
  public static double redondea(double numero, int numeroDecimales,int tipoRedondeo)
  {
        BigDecimal big = new BigDecimal(numero);
        big = big.setScale(numeroDecimales, tipoRedondeo);
        return big.doubleValue();
  }
  public static boolean isNumeric(String p)
  {
  try
  {
     strToDouble2(p);
  } catch (Exception k)
  {
    return false;
  }
  return true;
 }
  /**
   * Redondea un numero a los decimales mandados
   * @param numero
   * @param numeroDecimales
   * @return Numero redondeado
   * @deprecated usar redondea(String numero, int numero decimales)
   */
  public static double Redondea(String numero, int numeroDecimales) {
    return redondea(numero, numeroDecimales);
  }
   /**
   * Redondea un numero a los decimales mandados
   * @param numero
   * @param numeroDecimales
   * @return Numero redondeado
   * 
   */
  public static double redondea(String numero, int numeroDecimales) {
    return redondea(strToDouble(numero), numeroDecimales);
  }
  public static String format(int Num,String Formatear)
  {
    return format(Num,Formatear,false);
  }
    /*
     / Formatea un Numero segun un formato Dado.
     / Recibe: double a formatear. String de Formato.
     / Devuelve: String de texto Formateado.
    */
  public static String format(int Num,String Formatear,boolean elimPunto)
  {
    return FormatDecimal((double)Num,Formatear);
  }
  public static String format(long Num,String Formatear)
  {
    return format(Num,Formatear,false);
  }
    /*
     / Formatea un Numero segun un formato Dado.
     / Recibe: double a formatear. String de Formato.
     / Devuelve: String de texto Formateado.
    */
  public static String format(long Num,String Formatear,boolean elimPunto)
  {
    return FormatDecimal((double)Num,Formatear);
  }
  public static String format(double Num,String Formatear)
  {
    return FormatDecimal(Num,Formatear,false);
  }
  public static String format(double Num,String Formatear,boolean elimPunto)
  {
    return FormatDecimal(Num,Formatear,elimPunto);
  }

  public static String format(String Num,String Formatear)
  {
    return FormatChar(Num.trim(),Formatear);
  }
  public static String  FormatDecimal(double Num,String Formatear)
  {
      return FormatDecimal(Num,Formatear,false);
  }
  public static String  FormatDecimal(double Num,String Formatear,boolean elimPunto)
  {
    DecimalFormat form = new DecimalFormat();
    form.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
    form.applyPattern("##0.#####");

    String Numch=form.format(Num);

    return (FormatChar(Numch,Formatear,true,elimPunto));
  }

  public static String FormatChar(String Numero,String Formatear)
  {
    return FormatChar(Numero,Formatear,true,false);
  }
  public /*synchronized*/ static String FormatChar(String Numero,String Formatear,boolean ceroIsNull)
  {
    return FormatChar(Numero,Formatear,ceroIsNull,false);
  }
/**
 * Formatea un numero con el formato mandado.
 * Antes de formatearlo, por defecto, lo redondea.
 * @param Numero 
 * @param Formatear
 * @param ceroIsNull
 * @param elimPunto
 * @return 
 */
    @SuppressWarnings("static-access")
  public static String FormatChar(String Numero,String Formatear,boolean ceroIsNull, boolean elimPunto)
  {

      if (Formatear == null)
          return null;

      if (Numero == null)
          return null;

      while (swFormat)
      {
          try
          {
//          System.out.println("Crack...");
              Thread.currentThread().sleep(10);
          } catch (Exception ex)
          {
          }
      }
      swFormat = true;

      int n, n1, pneg;
      String text1 = "";
      char letr;
      int Ltent = 1; // Longitud de parte entera sin ESPACIOS
      String fent = "";
      String fdec = "";
      String tent = "";
      String tdec = "";

      // Cojer la parte entera del formato
      if (Numero.length() == 0)
          Numero = "0";

      n = buscaletra(Formatear, '.', 0);
      if (n >= 0)
      {
          if (n == 0)
              fent = "#";
          else
              fent = Formatear.substring(0, n); // Parte entera del formato

          fdec = Formatear.substring(n + 1); // Parte Decimal del formato
      } else
          fent = Formatear; // Parte entera del formato

     // Cojer la parte entera del texto entrado.
      n = buscaletra(Numero, decimalSeparator.charAt(0));
      if (n >= 0)
      {
          if (n == 0)
              tent = "0";
          else
              tent = Numero.substring(0, n); // Parte entera del formato
          tdec = Numero.substring(n + 1); // Parte Decimal del formato
      } else
          tent = Numero;

      Ltent = tent.trim().length(); // Longitud de parte entera sin ESPACIOS

      text1 = "";
      int Lfent = 0;

      for (n = 0; n < fent.length(); n++)
      {
          if (fent.charAt(n) != ',')
              Lfent++;
      }
      // Tratar la parte entera del numero tecleado.

      if (tent.length() > Lfent)
      {
          // Error. Longitud del Campo entero superior al Formato
          for (n = 0; n < fent.length(); n++)
          {
              text1 = text1 + "*";
          }
          if (fdec.length() > 0)
          {
              text1 = text1 + (elimPunto ? "" : decimalSeparator);
              for (n = 0; n < fdec.length(); n++)
              {
                  text1 = text1 + "*";
              }
          }
          swFormat = false;
          return text1;
      }

      for (n = tent.length(); n < fent.length(); n++)
      {
          text1 = text1 + " "; // Ajustarlo al Numero de Caracteres del Formato
      }
      tent = text1 + tent;
      pneg = buscaultletra(tent, '-');

      text1 = "";

      n1 = fent.length() - 1;
      if (fent.charAt(n1) == '#' && ceroIsNull == true)
      {
          if (strToLong(tent) == 0)
              tent = space(tent.length());
      }
      char text2;

      for (n = n1; n >= 0; n--)
      {
          letr = fent.charAt(n);
          text2 = tent.charAt(n1);
          if (text2 == '-')
              text2 = ' ';
          switch (letr)
          {
              case '9':
              case '0':
                  if (text2 == ' ')
                      text1 = "0" + text1;
                  else
                      text1 = text2 + text1;
                  n1--;
                  break;
              case '-':
              case '#':
                  text1 = text2 + text1;
                  n1--;
                  break;
              case ',':
                  text1 = groupSeparator + text1;
          }
      }

      tent = text1;
      text1 = "";

      // Quitar las comas de donde sea necesario
      for (n = tent.length() - 1; n >= 0; n--)
      {
          if (n > 0)
          {
              if (tent.charAt(n) == groupSeparator.charAt(0) && tent.charAt(n - 1) == ' ')
                  text1 = " " + text1;
              else
                  text1 = tent.charAt(n) + text1;
          } else
              text1 = tent.charAt(n) + text1;
      }

      tent = text1;
      if (pneg >= 0)
      {
          // Numero Negativo. Ponerle el signo donde corresponda.
          text1 = "";
          boolean sw_neg = true;

          for (n = tent.length() - 1; n >= 0; n--)
          {
              if (fent.charAt(n) == '-' && sw_neg == true && tent.charAt(n) == ' ')
              {
                  if (text1.charAt(0) == ' ') // Para quitar posibles separadores.
                      text1 = " -" + text1.substring(1);
                  else
                      text1 = "-" + text1;
                  sw_neg = false;
                  continue;
              }
              text1 = tent.charAt(n) + text1;
          }
          tent = text1;
      }

    // Tratar la parte decimal del Numero
      if (fdec.compareTo("") == 0)
      {
          // Sin parte decimal //
          Numero = tent;
          swFormat = false;
          return Numero;
      }
      // Con parte decimal
      text1 = "";
      for (n = tdec.length(); n < fdec.length(); n++)
      {
          text1 = text1 + " ";
      }

      tdec = tdec + text1;
      text1 = "";

      for (n = fdec.length() - 1; n >= 0; n--)
      {
          letr = fdec.charAt(n);
          if (letr == '-')
              letr = '#';

          switch (letr)
          {
              case '9':
                  if (tdec.charAt(n) == ' ')
                      text1 = "0" + text1;
                  else
                      text1 = tdec.charAt(n) + text1;
                  break;
              case '#':
                  text1 = tdec.charAt(n) + text1;
                  break;
          }
      }
      tdec = text1;
      if (tdec.trim().compareTo("") == 0)
          Numero = tent + " " + tdec;
      else
          Numero = tent + (elimPunto ? "" : decimalSeparator) + tdec;
      swFormat = false;
      return Numero;
  }
  /*****************************************************************************
  * Busca una letra  'l' dentro de una cadena 'c', comenzando a buscar a partir de
  * la posicion 'p' de la cadena.
  * Devuelve -1 si no la encuentra.
  *          >=0, posicion donde la encuentra.
  ****************************************************************************/
  public /*synchronized*/ static int buscaletra(String c,char l,int p)
  {
       for (int n=p;n< c.length();n++)
     {
        if (c.charAt(n)==l)
            return n;
     }
       return -1;
  }

    public /*synchronized*/ static int buscaletra(String c,char l)
    {
        return buscaletra(c,l,0);
    }

  public /*synchronized*/ static int buscaultletra(String c,char l)
  {
    int n1=-1;

       for (int n=0;n< c.length();n++)
     {
        if (c.charAt(n)==l)
            n1=n;
     }
        return n1;
  }
    /**
    * Convierte un String a un Integer.
    * Si no puede transformarlo lanza una exception.
    **/
  public synchronized static int StrToInt(String p)
  {
    p=ajusNumero(p);
    Integer a = new Integer(p);
    return a.intValue();
  }

    /**
    * Convierte un String a un Integer.
    * Si no puede transformarlo devuelve -1;
    **/
 public synchronized static int strToInt(String p)
 {
    p=ajusNumero(p);
        Integer a;
        try {
            a = new Integer(p);
        } catch (Exception k)
        {
            return -1;
        }
    return a.intValue();
 }
  /**
  * Quita las comas y los Espacios de Un String.
  * Normalmente utilizado para pasarlo a un Integer o un Long
  */
 public  static String ajusNumero(String num)
 {
   num=num.trim();
   String s="";
   for (int n=0;n<num.length();n++)
   {
    if (num.charAt(n)!=groupSeparator.charAt(0))
       if (num.charAt(n)!=' ')
          s=s+num.charAt(n);
   }
   return s;
 }


 final static String[] tablaDia={"","LUNES","MARTES","MIERCOLES","JUEVES","VIERNES","SABADO",
      "DOMINGO"};

 final static String[] tablaMes={"","ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO",
      "JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};

 public synchronized static String intToMes(int mes){
    if (mes<1 || mes>12){
         System.out.println("Error(intToMes):N�mero de mes no existe");
         return "";
    }
    return tablaMes[mes];
 }

 public synchronized static String intToDia(int dia){
    if (dia<1 || dia>7){
         System.out.println("Error(intToDia):Dna de semana no existe");
         return "";
    }
    return tablaDia[dia];
 }

 public synchronized static double StrToDouble(String p) throws NumberFormatException{
    p=ajusNumero(p);
        Double a = new Double(p);
    return a.doubleValue();
 }

 public synchronized static double strToDouble(String p)
 {
    p=ajusNumero(p);

        Double a;
        try {
            a = new Double(p);
        } catch (Exception k)
        {
            return -1;
        }
    return a.doubleValue();
 }
 public static double strToDouble(Object o) throws Exception
 {
  return strToDouble2(o.toString());
 }

 public synchronized static double strToDouble2(String p)throws Exception
 {
    p=ajusNumero(p);

        Double a;

    a = new Double(p);
    return a.doubleValue();
 }


 public synchronized static long strToLong(String p)
 {
        Long a;
    p=ajusNumero(p);
        try {
            a = new Long(p);
        } catch (Exception k)
        {
            return -1;
        }
    return a.longValue();
 }

 public synchronized static long strToLong2(String p) throws NumberFormatException
 {
        Long a;
    p=ajusNumero(p);
    a = new Long(p);

    return a.longValue();
 }

/*  public  static VXYConstraints trVXYString(String s) throws Exception
  {
    Integer XY[]=new Integer[4];
    String campo[]=new String[4];
    int p=0;
    String valor;
    int pos=0;
    int r;

    do {
            // Ignoro los espacios en blanco.
            for (;pos<s.length();pos++)
            {
                if (s.charAt(pos)!=' ')
                    break;
            }
      if (pos==s.length())
        break;
            r=Formatear.buscaletra(s,',',pos);
      if (r<0)
      {
            campo[p]= s.substring(pos);
        break;
      }
            campo[p]= s.substring(pos,r);
      p++;
      if (p>3)
        break;
            pos=r+1;
        } while (true);

    if (p != 3)
      throw new Exception("Numero de Campos Incorrecto");
    for (int n=0;n<4;n++)
    {
      XY[n]= new Integer(campo[n]);
    }
    return (new VXYConstraints(XY[0].intValue(),XY[1].intValue(),XY[2].intValue(),XY[3].intValue()));
  }
 */

  public static Vector separaLinea(String linea, char caracter) {
    Vector vec = new Vector();
    separaLinea(vec, linea, caracter);
    return vec;
  }

  public static void separaLinea(Vector vec, String linea, char caracter) {
    int ps = 0;
    vec.removeAllElements();
    while(ps!=-1) {
      String par;
      int ps1=ps;
      if ((ps=linea.indexOf(caracter,ps1))==-1)
        par=linea.substring(ps1);
      else
        par= linea.substring(ps1,ps);
      if (ps!=-1)
        ps++;
      vec.addElement(par);
    }
  }

  public static String llenar(char c,int ncar)
  {
    String s="";
    for (;ncar>0;ncar--)
      s=s+c;
    return s;
  }

  public static String space(int ncar)
  {
    return llenar(' ',ncar);
  }

  /*
  * Ajusta un String a la izquierda poniendole tantos caracteres como
  * los indicados.
  */
  public static String ajusIzq(String s,int anc)
  {
    if (s==null)
      return space(anc);
    int lg=s.length();
    anc+=getNumCtrlCarac(s);
    if (lg>anc)
      return s.substring(0,anc);

    if (lg==anc)
      return s;

    return s + space(anc-lg);
  }
  /**
   * Corta un String a la anchura dada. Es decir si la longitud es mayor que
   * anc lo deja a anc. En caso de que sea menor lo deja como esta.
   * @param s String String a cortar
   * @param anc int Anchura a la que cortar
   * @return String String cortado
   */
  public static String strCorta(String s,int anc)
  {
    return s.length()<=anc?s:s.substring(0,anc);
  }
 /**
   * Corta un String a la anchura dada. Es decir si la longitud es mayor que
   * anc lo deja a anc. En caso de que sea menor lo deja como esta.
   * @param s String String a cortar
   * @param anc int Anchura a la que cortar
   * @return String String cortado
   */
  public static String cortar(String s, int anc)
  {
    return strCorta(s,anc);
  }

  /*
  * Ajusta un String a la Derecha poniendole tantos caracteres como
  * los indicados.
  */
  public static String ajusDer(String s,int anc)
  {
    int lg=s.length();
    anc+= getNumCtrlCarac(s);
    if (lg>anc)
      return s.substring(0,anc);
    if (lg==anc)
      return s;

    return space(anc-lg)+s;
  }
  public static String ajusCen(String s,int anc)
  {
    anc+=getNumCtrlCarac(s);
    s=s.trim();
    int lg=s.length();
    if (lg==anc)
      return s;
    if (lg>anc)
      return s.substring(0,anc);
    s=space((anc-lg)/2)+s;
    s=s+space(anc-s.length());
    return s;
  }

  public static String ajusCen(String s,int anc, boolean trim)
  {
    anc+=getNumCtrlCarac(s);
    if (trim)
      s=s.trim();
    int lg=s.length();
    if (lg==anc)
      return s;

    s=space((anc-lg)/2)+s;
    s=s+space(anc-s.length());
    return s;
  }
  /**
  * Reemplaza en el String 'strBase' todos los 'oldString' por 'newString'
  * @return el nuevo string una vez hechas las sustituciones.
  *
  */
  public static String reemplazar(String strBase, String oldString, String newString)
  {
    String s="";
    int pos=0;
    int lPos=0;
    int lg=oldString.length()-1;
    while(lPos != -1)
    {
      lPos=strBase.indexOf(oldString,pos);
      if (lPos==-1)
      {
        s=s+strBase.substring(pos);
        break;
      }
      s=s+strBase.substring(pos,lPos)+newString;
      pos=lPos+1+lg;
    }
    return s;
  }

  /**
  * @param numCar -> Nn de Caracteres disponibles(inclusive comas).
  * @param nDec   -> Nn de Decimales a presentar.
  * @param caracter a poner por defecto en el la parte entera.
  * <p> Siempre se asume que el formato por defecto es tipo '???,???'
  * '?' Caracter a enviado.
  * A los decimales se les pondra el formato tipo '99'.
  * @return Formato calculado.
  */
  public static String getFormato(int numCar,int nDec,char carDec)
  {
    int n=0,n1,n2;
    String fr="";

    if (nDec == 0)
      n1=0;
    else
      n1=nDec+1;

    fr="9";
    for (n2=1,n=1;n<numCar-n1-1;n++)
    {
      if (n2==3)
      {
        fr=","+fr;
        n2=0;
      }
      else
      {
        fr=carDec+fr;
        n2++;
      }
    }
    fr=carDec+fr;
    if (nDec>0)
      fr=fr+"."+Formatear.llenar('9',nDec);
    return fr;

  }
  /**
  * @param numCar -> Nn de Caracteres disponibles.
  * @param nDec   -> Nn de Decimales a presentar.
  * <p> Siempre se asume que el formato por defecto es tipo '---,---'
  * A los decimales se les pondra el formato tipo '99'.
  * @return Formato calculado.
  */
  public static String getFormato(int numCar,int nDec)
  {
    return getFormato(numCar,nDec,'-');
  }

  public static String getFormDec(String formato,int nDec)
  {
    int n;
    if (formato.indexOf('.')!=-1)
      formato=formato.substring(1,formato.indexOf('.'));
    if (nDec==0)
      return formato;
    formato=formato+"."+llenar('9',nDec);
    return formato;
  }

  /**
  * @param valor -> Numero a Formatear.
  * @param numcar -> Numero de Caracteres Totales Disponibles.
  * @param nDec  -> Nn de Decimales a presentar.
  * <p>
  * Le pone formato a un Double, dependiendo del Numero de Caracteres
  * disponibles y el Nn de Decimales que se deben presentar.
  * <p> Siempre se asume que el formato por defecto es tipo '---,---'
  * A los decimales se les pondra el formato tipo '99'.
  * @return Devuelve el valor ya formateado.
  * @see format, getFormato
  */
  public static String formNumCar(double valor,int numCar,int nDec)
  {
    return Formatear.format(valor,getFormato(numCar,nDec));
  }

  public static String formNumCar(String valor,int numCar,int nDec)
  {
    return Formatear.format(valor,getFormato(numCar,nDec));
  }
  public static String formNumCar(int valor,int numCar,int nDec)
  {
    return Formatear.format(valor,getFormato(numCar,nDec));
  }
  /**
   * Convierte una Cadena de Texto a codigo Hexadecimal
   */
  public static String StrToHex(String cadenaTexto) {
    String s="";
    for (int i=0;i<cadenaTexto.length();i++) {
      int c = cadenaTexto.charAt(i);
      String h = Integer.toHexString(c);
      if (h.length()==1)
        h = "0" + h;
      s = s + h;
    }
    return s;
  }
  /**
   * Convierte un codigo Hexadecimal a una Cadena de Texto
   */
  public static String HexToStr(String valorHexadecimal)
  {
    String s = "";
    String hexa="";
    int caracters=0;
    for (int i=0;i<valorHexadecimal.length();i++) {
      hexa = hexa + valorHexadecimal.charAt(i);
      caracters++;
      if (caracters==2) {
        s = s + ((char)HexToDec(hexa));
        caracters=0;
        hexa="";
      }
    }
    return s;
  }
  /**
   * Convierte una cadena Hexadecimal a un valor decimal
   */
  public static int HexToDec(String hexa) {
    int dec=0;
    hexa=hexa.toLowerCase();
    int x =hexa.length();
    for (int i=0;i<hexa.length();i++) {
      char d = hexa.charAt(i);
      x--;
      int base = (x*16);
      if (base==0)
        base=1;
      switch (d) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          dec = dec + ((((int)d) - 48)*base);
          break;
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
          dec = dec + ((((int)d) - 87)*base);
          break;
      }
    }
    return dec;
  }
  public static byte HexToByte(String hexa) {
    return (byte) HexToDec(hexa);
  }
  public static byte[] HexToBytes(String hexa) {
    int lg=hexa.length();
    byte[] b=new byte[lg/2];
    int p=0;
    int n=0;
    if (lg % 2 == 1)
      return null;
    for (;p<lg;p=p+2,n++)
      b[n]=HexToByte(hexa.substring(p,p+2));
    return b;
  }


  /**
  * Convierte un double a su representancion escrita.
  * Ejemplo: @param 54 @return cincuenta y cuatro
  * @author gnu.chu P 22/4/99
  *
  * Modificado por Angel J. Apellnniz el 19/10/1999
  *                Para que admita numeros en femenino
  *
  */

  public static String numToLiteral(double num_i1) {
         return numToLiteral(num_i1, "M");
  }
  public static String numToLiteral(double num_i1, String sexoMoneda)
  {
        int num_i;
    int num_i2;
        String num_c="";
        int mill=0;
        int div1,div111;

    num_i = (int) num_i1;
      num_i2 = (int) num_i1;
    if (num_i == 0)
        num_c = "CERO ";
    if (num_i >1000000000 || num_i < -1000000000 )
      return null;

    if (num_i <0 )
    {
       num_c =" MENOS ";
       num_i = num_i * -1;
    }
    if (num_i>= 1000000)
    {
      mill = 1;
      div111=num_i /1000000;
      div1=div111;
      if (div111 == 1)
        num_c = num_c.trim()+" UN MILLON";
      else
        num_c = num_c.trim()+" "+letra(div1, mill, sexoMoneda).trim()+" MILLONES";
      num_i=num_i-div111*1000000;
      if (num_i==0)
       return num_c;
    }
    mill=0;
    if (num_i >= 1000)
    {
      div111=num_i/1000;
      div1=div111;
      if (div1==1 && num_c == null)
            num_c = " MIL";
      else
        num_c = num_c.trim()+" "+letra(div1,mill, sexoMoneda).trim()+" MIL";
      num_i=num_i-div111*1000;
      if (num_i==0)
        return num_c;
    }
    if (num_i >0)
    {
     div1=num_i;
       num_c = num_c.trim()+ " "+ letra(div1,mill,sexoMoneda).trim();
    }
      if (num_i1 - num_i2 != 0)
    {
        div1 = (int) (((num_i1 - num_i2)+0.001) * 100);
        num_c = num_c.trim()+" CON "+ letra(div1,mill, sexoMoneda).trim();
    }
    return num_c;
  }


  static String letra(int div1,int mill, String sexoMoneda)
  {
      int div11;
      String num_c1="";

    if (div1==100)
    {
       num_c1 = " CIEN";
       return num_c1;
    }

    if (div1>100)
    {
        div11=div1/100;
        num_c1= cientos(div11, sexoMoneda);
          div1=div1-div11*100;
    }

    if (div1>=10)
    {
        div11=div1/10;
        num_c1 = num_c1.trim()+decen(div1,div11,mill, sexoMoneda);
        div1=div1-div11*10;
          return num_c1;
    }
      num_c1 = num_c1.trim()+tr_unid(div1,mill, sexoMoneda);
    return num_c1;
  }


/**
*	Pasa un numero  a cientos de de ese numero.
*	  Recibe: Un integer de 1 a 9
*	  Devuelve: Cadena con los cientos que sea.
*/
  static String cientos(int div11, String sexoMoneda)
  {
    String pes1="";

    switch (div11)
    {
      case 1:
         pes1=" CIENTO ";
         break;
       case 2:
         pes1=" DOSCIENT";
        break;
         case 3:
        pes1=" TRESCIENT";
        break;
       case 4:
        pes1=" CUATROCIENT";
        break;
       case 5:
        pes1=" QUINIENT";
        break;
        case 6:
        pes1=" SEISCIENT";
        break;
        case 7:
        pes1=" SETECIENT";
        break;
        case 8:
        pes1=" OCHOCIENT";
        break;
        case 9:
        pes1=" NOVECIENT";
    }
    if (div11 != 1)
       if (sexoMoneda.equals("M"))
          pes1 += "OS ";
       else
           pes1 += "AS ";
    return pes1;
  }

  /**
  *
  *	Recibe un numero entre 1 y 9 devuelve la cadena con su representacion en
  * decenas.
  */
  static String  decen(int div1,int div11,int mill, String sexoMoneda)
  {

        String pes1="";

    switch (div11)
    {
      case 1:
        pes1 = tr_dece(div1);
        break;
       case 2:
        if (div1==20)
        {
          pes1=" VEINTE ";
          break;
        }
        div1=div1-div11*10;
        pes1 = " VEINTI"+tr_unid(div1,mill, sexoMoneda);
        break;
       case 3:
        if (div1==30)
        {
          pes1=" TREINTA ";
          break;
        }
        div1=div1-div11*10;
        pes1= " TREINTA Y "+ tr_unid(div1,mill, sexoMoneda);
        break;
       case 4:
        if (div1==40)
        {
          pes1=" CUARENTA ";
          break;
        }
        div1=div1-div11*10;
        pes1 = " CUARENTA Y "+tr_unid(div1,mill, sexoMoneda);
        break;
       case  5:
         if (div1==50)
         {
          pes1=" CINCUENTA ";
          break;
         }
         div1=div1-div11*10;
             pes1= " CINCUENTA Y "+tr_unid(div1,mill, sexoMoneda);
         break;
       case 6:
         if (div1==60)
         {
          pes1=" SESENTA ";
          break;
         }
        div1=div1-div11*10;
        pes1= " SESENTA Y "+tr_unid(div1,mill, sexoMoneda);
         break;
       case 7:
         if (div1==70)
         {
          pes1=" SETENTA ";
          break;
         }
         div1=div1-div11*10;
         pes1 = " SETENTA Y "+tr_unid(div1,mill, sexoMoneda);
         break;
       case 8:
         if (div1==80)
         {
          pes1=" OCHENTA ";
          break;
         }
         div1=div1-div11*10;
         pes1 = " OCHENTA Y "+tr_unid(div1,mill, sexoMoneda);
         break;
       case 9:
         if (div1==90)
         {
          pes1=" NOVENTA ";
          break;
         }
         div1=div1-div11*10;
        pes1 = " NOVENTA Y "+tr_unid(div1,mill, sexoMoneda);
      }
        return pes1;
  }


/**
*
*	Funcion de tratar las decenas.
*/
  static String tr_dece(int div1)
  {
    String pes1="";

    switch (div1)
    {
      case 10:
         pes1 = " DIEZ ";
         break;
      case 11:
        pes1 = " ONCE ";
        break;
      case 12:
        pes1 = " DOCE ";
        break;
      case 13:
        pes1 = " TRECE ";
        break;
      case 14:
        pes1 = " CATORCE ";
        break;
      case 15:
        pes1 = " QUINCE ";
        break;
      case 16:
        pes1 = " DIECISEIS ";
        break;
      case 17:
        pes1 = " DIECISIETE ";
        break;
      case 18:
        pes1 = " DIECIOCHO ";
        break;
      case 19:
        pes1 = " DIECINUEVE ";
        break;
    }

    return pes1;
  }


  /**
  *	Pasa un numero a literal en unidades.
  *	   Recibe: Un numero entre uno y 9
  *	 Devuelve: Cadena con la representancion.
  */
  static String tr_unid(int div1,int mill, String sexoMoneda)
  {
    String pes1="";

     switch (div1)
     {
      case 1:
         if (mill == 0) {
           pes1 = "UN ";
           if (sexoMoneda.equals("F"))
              pes1 = pes1.trim() + "A ";
           else
             pes1=pes1.trim() + "O ";
         }
         break;
       case 2:
        pes1 = "DOS ";
        break;
       case 3:
        pes1 = "TRES ";
         break;
       case 4:
        pes1 = "CUATRO ";
         break;
       case 5:
        pes1 = "CINCO ";
        break;
       case 6:
        pes1 = "SEIS ";
        break;
       case 7:
        pes1 = "SIETE ";
        break;
       case 8:
        pes1 = "OCHO ";
         break;
       case 9:
        pes1 = "NUEVE ";
      }
      return pes1;
  }


//******************************************************************
// Funcion chapucera que pasa un String a lineas con el numero de
// caracteres dado sin cortar las palabras.
//******************************************************************

  public static String pasalineas(String txt, int numCaractLin){
      if (txt==null) return "";
      Vector texto = pasalineasV(txt,numCaractLin);

      //Pasamos las lineas a String
      String text="";
      int numlineas=texto.size();
      for(int i=0;i<numlineas;i++){
            String t=(String)texto.elementAt(i);
            text+=(t.indexOf('\n')==-1)?t+"\n":t;
      }
      return text;
  }

//******************************************************************
// Funcion cojonudisima que pasa un Vector de lineas con el numero de
// caracteres dado sin cortar las palabras.
//******************************************************************

  public static Vector pasalineasV (String txt, int numCaractLin){
      if (txt==null) return new Vector();
      numCaractLin++;//Cogemos un caracter mas para cortar las palabras
      Vector texto = new Vector();
      String lin="";
      boolean salto=true;
      for(int i=0,pos=0;i<txt.length();){
          if (i+numCaractLin<txt.length())
            lin=txt.substring(i,i+numCaractLin);
          else
            lin=txt.substring(i);

          // Buscamos Separador
          pos=lin.indexOf('\n');
          salto=true;
          if(pos <0){ // Si no hay separador
             salto=false;
             if (lin.length()>=numCaractLin)
                pos=lin.lastIndexOf(' ');

          }

          if (pos<0){
             texto.addElement(lin); //se trata de una sola palabra o linea final
             i+=txt.length();
          }else{
              if (pos>0){
                if (salto){
                  texto.addElement(lin.substring(0,pos)+"\n");
                }else{
                  texto.addElement(lin.substring(0,pos));
                }
              }else
                texto.addElement(" ");
              i+=(pos+1); //Avanzamos el apuntador de lo que llevamos pasado
          }
      }

      return texto;
  }
  /**
   * Funcion que pasa un valor decimal a base 36
   *
   * @param int valor numerico
   * @return String con el equivalente en base 36
   *
   * @autor Angel J. Apellnniz - Creado el 11/08/1999
   *
   */
  public static String DecToB36(int numero) {
         return DecToB36(numero, 4);
  }
  /**
   * Funcion que pasa un valor decimal a base 36
   *
   * @param int valor numerico
   * @param int numero de caracteres minimos a presentar
   * @return String con el equivalente en base 36
   *
   * @autor Angel J. Apellnniz - Creado el 11/08/1999
   *
   */
  public static String DecToB36(int numero, int numChar) {
         char[] tabla = new char[36]; // Tabla con los equivalentes en base 36
         int valorAscii;
         String base36 = "";

         // llena la tabla con los valores permitidos en base 36
         valorAscii=48;
         for (int i=0;i<36;i++) {
             tabla[i] = ((char)valorAscii);
             valorAscii++;
             if (valorAscii == 58)
                valorAscii = 65;
         }

         // Transforma el valor Numerico
         do {
            int num1 = numero / 36;
            int num2 = num1;
            num1 = num1 * 36;
            int resto = numero - num1;
            base36 = tabla[resto] + base36;
            numero = num2;
            if (numero<36) {
               base36 = tabla[numero] + base36;
               break;
            }
         } while (true);
         if (base36.length() < numChar)
            for (int i=base36.length();i<numChar;i++)
                base36 = "0" + base36;
         return base36;
  }
  /**
   * Funcion que pasa un valor en base 36 a numerico
   *
   * @param String valor en base 36
   * @return int con el valor numerico
   *
   * @autor Angel J. Apellnniz - Creado el 11/08/1999
   *
   */
  public static int B36ToDec(String b36) {
         int numero = 0;
         b36 = b36.toUpperCase();

         // Transforma el valor Base 36
         for (int i=0;i<b36.length();i++) {
             numero = numero * 36;
             if (b36.charAt(i) < 58) {
                numero += (b36.charAt(i)-48);
             } else {
                numero += (b36.charAt(i)-55);
             }
         }
         return numero;
  }
  public static boolean combinaciones(Vector vec, String texto) {
    String ini = "";
    String fin = "";
    boolean inifin = true;

    if (texto.trim().length() == 0)
      return false;

    if (texto.substring(0,1).compareTo(",") == 0)
      return false;

    if (texto.substring(0,1).compareTo("-") == 0)
      return false;

    if (texto.trim().substring(texto.trim().length()-1,texto.trim().length()).compareTo(",") == 0)
      return false;

    if (texto.trim().substring(texto.trim().length()-1,texto.trim().length()).compareTo("-") == 0)
      return false;

    texto = texto + ",";
    for (int i=0;i<texto.trim().length();i++) {
      switch (texto.charAt(i)) {
        case ',':
          if (ini.compareTo("") != 0)
            vec.addElement(ini);
          if (fin.compareTo("") != 0) {
            int ini2 = Formatear.StrToInt(ini) + 1;
            int fin2 = Formatear.StrToInt(fin);
            for (int y=ini2;y<=fin2;y++)
              vec.addElement(""+y);
          }
          ini = "";
          fin = "";
          inifin = true;
          break;
        case '-':
          inifin = false;
          break;
        case ' ':
          break;
        default:
          if (inifin)
            ini = ini + texto.charAt(i);
          else
            fin = fin + texto.charAt(i);
          break;
      }
    }
    return true;
  }
  /**
   * Limpia los espacios de una cadena de texto tanto de la derecha como de la Izquierda
   */
  public synchronized static String clipped(String cadenaTexto) {
          return clipped(cadenaTexto, true, true);
  }
  /**
   * Limpia los espacios de una cadena de texto tanto de la derecha como de la Izquierda
   */
  public synchronized static String clipped(String cadenaTexto, boolean espaciosIzq, boolean espaciosDcha) {
    String texto=cadenaTexto;

    if (espaciosIzq) {
       // Limpia espacios de la Izqierda
       for (int i = 0;i<cadenaTexto.length();i++) {
           if (cadenaTexto.charAt(i) != 32) {
              texto = cadenaTexto.substring(i);
              break;
           }
       }
    }

    if (espaciosDcha) {
       // Limpia espacios de la Deracha
       for (int i = texto.length()-1;i>0;i--) {
           if (texto.charAt(i) != 32) {
              texto = texto.substring(0, i+1);
              break;
           }
       }
    }
    return texto;
  }
  /**
   * Devuelve un String con los datos que se encuentra hasta
   * la que se le envia en la variable eliminar
   * <p>
   * @param String a tratar
   * @param String cadenaa ignorar
   * @return String resultado
   */
  public static String quitarDesde(String valor, String eliminar) {
    String s=valor;
    int i = s.indexOf(eliminar);
    if (i != -1)
      s = s.substring(0, i-1);
    return s;
  }

  /**
   * Devuelve el numero de caracteres de control
   * <p>
   * @param String a buscar
   * @return int numero de caracteres de control
   */
  public static int getNumCtrlCarac(String cadena){
     int ctrl=0, pos=0;
     while ((pos=cadena.indexOf(27,pos))!=-1){
              ctrl-=pos;    // Restamos la 1n posicion
              pos=cadena.indexOf(27,++pos);
              ctrl+=++pos;  // Sumamos la ultima posicion +1
     }

     return ctrl;
  }

  private static Vector posblancos(String texto){
      Vector posiciones=new Vector();
      int pos=0;
      while ((pos=texto.indexOf(' ',pos))!=-1){
           posiciones.add(new Integer(pos));
           pos++;
      }
      return posiciones;
  }
  /**
   * Devuelve un Vector con el texto justificado al ancho
   * <p>
   * @param String a tratar
   * @param int anchura a justificar
   * @return Vector resultado con cada linea
   */
  public static Vector justificarV(String texto, int ancho){

       if (texto==null) return new Vector();

       Vector txt=pasalineasV(texto, ancho);
       Vector justi=new Vector();
       int nlin=txt.size();
       String antod="";
       String ljust;

       for (int i=0; i<nlin; i++){ // Para cada linea
           int desde=0; // Funcion donde se queda de anadir
           antod="";
           String linea= ((String)txt.get(i));
           ljust="";

           Vector posb=posblancos(linea.trim());
           if ((linea.indexOf('\n')!=-1)||posb.size()<1){
               justi.add(linea);
               continue;
           }
           linea=linea.trim();

           int reparto= ancho - linea.length();
           int cociente= reparto / posb.size();
           int resto=reparto-cociente*posb.size();

           // Formamos lo que hay que concatenar en cada blanco
           for (int j=0; j<cociente; j++)
                  antod+=" ";

           for (int j=0, hasta=0;j<posb.size();j++){  // Dentro de cada linea para cada blanco
               hasta=((Integer) posb.get(j)).intValue();
               if (hasta<0) break;
               ljust+=linea.substring(desde,++hasta)+antod;
               desde=hasta;

               if (resto-->0)
                  ljust+=" ";

           }
           justi.add(ljust+linea.substring(desde));
       }
       return justi;
  }
  /**
   * Devuelve un String con el texto justificado al ancho
   * <p>
   * @param String a tratar
   * @param int anchura a justificar
   * @return String resultado
   */
  public static String justificar(String texto, int ancho){
      if (texto==null) return "";
      Vector text = justificarV(texto,ancho);

      //Pasamos las lineas a String
      String txt="";
      int numlineas=text.size();
      for(int i=0;i<numlineas;i++){
            String t=(String)text.elementAt(i);
            txt+=(t.indexOf('\n')==-1)?t+"\n":t;
      }
      return txt;
  }

/**
 * @description Justifica un String formado por uno o varios parrafos a una anchura dada.
 *  Se supone que, al menos, deben caber dos palabras en el ancho de texto
 * dado para realizar el justificado    de forma correcta.
 * Si no cabe ni siquiera una palabra, se pasa del margen. Si cabe una
 * palabra pero no dos, y quedan huecos, no justifica esa lnnea, que constarn unicamente de
 * la primera palabra.<p>
 * @param String texto - Texto a justificar. Puede contener multiples pnrrafos, separados por el caracter '\n'<br>
 * @param nnt numCol - Nnmero de columnas a las que se justificarn el texto.<br>
 * @return Vector - Contiene los strings que se corresponden a las diferentes lnneas del texto.<p>
 */
public static Vector justificaTexto(String texto, int numCol) {
       Vector vecJusti = new Vector();
       StringTokenizer strTokPrinc = new StringTokenizer(texto,"\n",false);
       while (strTokPrinc.hasMoreTokens()) {
             Vector linea = new Vector();
             int restoChar = numCol;
             int numHuecos = 0;
             StringTokenizer strTokTrozo = new StringTokenizer(strTokPrinc.nextToken());
             while (strTokTrozo.hasMoreTokens()) {
                   String palabra = strTokTrozo.nextToken();
                   if (linea.size()==0)
                      if (palabra.length()>=numCol)
                         vecJusti.addElement(palabra);
                      else {
                           linea.addElement(palabra);
                           restoChar -= palabra.length();
                           numHuecos = 0;
                           }
                   else {
                        int auxResto = restoChar-1-palabra.length();
                        if (auxResto >= 0) {
                           linea.addElement(palabra);
                           restoChar = auxResto;
                           numHuecos++;
                           }
                        else {
                             vecJusti.addElement(creaLinea(linea, restoChar + numHuecos, false));
                             linea.removeAllElements();
                             linea.addElement(palabra);
                             restoChar = numCol-palabra.length();
                             numHuecos=0;
                             }
                        }
                   }
             if (linea.size()!=0)
                vecJusti.addElement(creaLinea(linea, restoChar + numHuecos, true));
             }
       return vecJusti;
       }
private static String creaLinea(Vector palabras, int espaciosMeter, boolean finParrafo) {
        String lin = (String)(palabras.elementAt(0));
        if (palabras.size() == 1)
           return lin;
        if (finParrafo) {
           for (int i=1; i<palabras.size(); i++)
               lin += " " + (String)(palabras.elementAt(i));
           return lin;
           }
        int totalHuecos = palabras.size()-1;
        int numCarHuecoPeq = espaciosMeter/totalHuecos;
        String huecoPequeno = Formatear.space(numCarHuecoPeq);
        String huecoGrande = " " + huecoPequeno;
        int numPalHuecoGrande = espaciosMeter - numCarHuecoPeq * totalHuecos;
        for (int i=1; i<palabras.size(); i++) {
            if (i <= numPalHuecoGrande)
               lin += huecoGrande;
            else
               lin += huecoPequeno;
            lin +=(String)(palabras.elementAt(i));
            }
        return lin;
        }
// FIN justificaTexto =====================================================================


  /**
   * Pasa de Horas a minutos
   */
  public static int horToMin(double hora) {
         if (hora == 0)
            return 0;
         int trHora = (int)Math.floor(Math.round(100*hora)/100);
         int trMinu = (int)Math.round((hora-trHora)*100);
         int total = trHora*60 + trMinu;
         return total;
  }
  /**
   * Pasa de Minutos a horas
   */
  public static double minToHor(int min) {
         if (min == 0)
            return 0;
         int minut1 = min / 60;
         int minuft = min - (minut1 * 60);
         double hora = redondea(minut1 + ((double)minuft / 100), 2);

         return hora;
  }

  /**
   * Resta dos horas
   */
  public static double restaHora(double hora1,double hora2) {
//      int min1 = horToMin(hora1), min2 = horToMin(hora2);
      return minToHor(horToMin(hora1)-horToMin(hora2));
  }

  /**
   * Suma dos a horas
   */
  public static double sumaHora(double hora1,double hora2) {
//      int min1 = horToMin(hora1), min2 = horToMin(hora2);
      return minToHor(horToMin(hora1)+horToMin(hora2));
  }
  public static String StrToMail(String msg) {
         String txt="";
         for (int n=0;n<msg.length();n++) {
             if (msg.charAt(n)>127)
                txt += "&#"+ (int) msg.charAt(n);
             else
                 txt += msg.charAt(n);
         }
         return txt;
  }
  /**
   * Devuelve si una cadena es nula ("")
   * Si por ejemplo mandamos "  -  -    " (una fecha vacia),
   * si la cadena ignorar es igual a "-" se considera como nula.
   * 
   * @param cadena Cadena a comprobar si es nula
   * @param ignorar Si se encuentra esa cadena se ignorara. Considerando que
   * la cadena es nula (puede estar repetida la cadena tantas veces como se desee)
   * @return true si se considera nula.
   *
   */
  public static boolean esNulo(String cadena,String ignorar)
  {
     if (cadena==null)
         return true;
     if (ignorar==null)
         return esNulo(cadena);
     if (cadena.trim().equals(""))
      return true;
     int n=0;
     int inic=0;
     while (n>=0)
     {
         n=cadena.indexOf(ignorar,inic);
         if (n<0)
             return true;
         if (!cadena.substring(inic,n).trim().equals(""))
             return false;
         inic=n+1;
     }
     return true;
  }
  public static boolean esNulo(String cadena)
  {
    if (cadena.trim().equals(""))
      return true;
    try{
      if (Integer.parseInt(cadena)==0)
        return true;
    } catch (Exception k)   { }
    return false;
  }
  /**
   * Devuelve un String en formato yyyyMMdd
   * @param fecha 
   * @return
   */
  public static String fechaDB(java.util.Date fecha)
  {
    GregorianCalendar gcal = new GregorianCalendar();
    gcal.setTime(fecha);
    return Formatear.format(gcal.get(GregorianCalendar.YEAR), "0000") +
        (Formatear.format(gcal.get(GregorianCalendar.MONTH) + 1, "00")) +
        Formatear.format(gcal.get(GregorianCalendar.DAY_OF_MONTH), "00");
  }
  /**
   * Devuelve un string con la fecha en formato yyyyMMddd
   * @param fecha  la fecha debe estar en formato dd-MM-yyyy
   * @return String con la fecha mandada pero en formato yyyyMMdd
   * @throws ParseException
   */
  public static String getFechaDB(String fecha) throws ParseException
  {
    return getFechaDB(fecha,"dd-MM-yyyy");
  }
 /**
   * Devuelve un string con la fecha en formato yyyyMMddd
   * @param fecha  
   * @return String con la fecha mandada  en formato yyyyMMdd
   * 
   */
  public static String getFechaDB(Date fecha) 
  {
    return getFecha(fecha,"yyyyMMdd");
  }

  public static String getFechaDB(String fecha, String Formato) throws
      ParseException
  {
    SimpleDateFormat sd = new SimpleDateFormat(Formato);
    return fechaDB(sd.parse(fecha));
  }

  /**
   * Devuelve la fecha en formato dd-MM-yyyy
   *
   * @param fecha Fecha a transformar.
   * @return Fecha en formato dd-MM-yyyy
   */
  public static String getFechaVer(java.util.Date fecha)
   {
     if (fecha==null)
         return "";
     GregorianCalendar gcal = new GregorianCalendar();
     gcal.setTime(fecha);
     return Formatear.format(gcal.get(GregorianCalendar.DAY_OF_MONTH), "00")+"-"+
         (Formatear.format(gcal.get(GregorianCalendar.MONTH) + 1, "00"))+"-"+
         Formatear.format(gcal.get(GregorianCalendar.YEAR), "0000");
   }

   /**
    * Devuelve la fecha en una clase java.util.Date
    *
    * @param fecha Fecha a transformar.
    * @param formfec Formato de la fecha mandada.
    * @return clase java.util.Date con la fecha introducida (soporta timestamp)
     * @throws java.text.ParseException
    */
   public static Date getDate(String fecha,String formfec) throws java.text.ParseException
   {
      if (fecha==null)
        return null;
      if (fecha.trim().equals(""))
        return null;
      SimpleDateFormat sd = new SimpleDateFormat(formfec);
      try
      {
          return new Date(sd.parse(fecha).getTime());
      } catch (ParseException ex)
      {
          throw new ParseException("Error al Parsear Fecha: "+fecha+ " con formato: "+formfec,ex.getErrorOffset());
      }
   }
   /**
    * Comprueba si una fecha es nula.
    * @param fecha Cadena con la presunta fecha
    * @return a true si la cadena mandada no tiene ningun numero  o es null. 
    */
   public static boolean isNullDate(String fecha)
   {
        if (fecha==null)
            return true; 
       
        return !fecha.matches(".*[1-9].*");
   }
   public static void verAncGrid(gnu.chu.controles.Cgrid jt)
   {
     for (int n=0;n<jt.getColumnCount();n++)
       System.out.println("Columna: "+n+ " Nombre: "+jt.getNameColumn(n)+ " - Ancho: "+jt.getAnchoColumna(n));
   }
   public static String formatearFecha(String fecha,String formFecOr,String formFecFin) throws ParseException
   {
     if (fecha==null)
       return "";
     if (fecha.equals("") || reemplazar(fecha,"-","").trim().equals(""))
       return "";
     SimpleDateFormat sd = new SimpleDateFormat(formFecOr);
     Date dt = sd.parse(fecha);
     sd.applyPattern(formFecFin);
     return sd.format(dt);
   }
   /**
    * Devuelve un string con la fecha a partir de un date
    * @param fecha Objeto Date con la fecha
    * @param formFecFin Formato en que queremos que nos devuelva la fecha
    * @return Fecha en formato String
    */
   public static String getFecha(Date fecha,   String formFecFin)
   {
     if (fecha == null)
       return "";
     SimpleDateFormat sd = new SimpleDateFormat();
     sd.applyPattern(formFecFin);
     return sd.format(fecha);
   }
   /**
    * Devuelve un String con la cadena a poner para una sentencia SQL en JDBC
    * 
    * @param fecha 
    * @return  String con la cadena {d 'yyyy-MM-dd'}
    */
   public static String getSQLDate(Date fecha) 
    {
      return "{d '"+getFecha(fecha,"yyyy-MM-dd")+"'}";
   }
   /**
    * Devuelve el dia de la fecha mandada
    * @param fecha Objeto Date con la fecha
    * 
    * @return dia de la fecha
    */
   public static int getDay(Date fecha)
   {
     if (fecha == null)
       return 0;
    
     return Integer.parseInt(getFecha(fecha,"dd"));
   }
   /**
    * Devuelve la fecha añadiendole 2000 años en caso de que el año sea inferior al 2000.
    * @param fecha
    * @return 
    */
   public static Date getDate2000(Date fecha)
   {
      try
      {
          if (fecha==null)
              return null;
          return Formatear.getYear(fecha)<2000?
              Formatear.getDate(Formatear.getFecha(fecha,"dd-MM-")+ (2000+Formatear.getYear(fecha)),"dd-MM-yyyy"):
              fecha;
      } catch (ParseException ex)
      {
//          Logger.getLogger(Formatear.class.getName()).log(Level.SEVERE, null, ex);
          return null;
      }
   }
  /**
    * Devuelve el año de la fecha mandada
    * @param fecha Objeto Date con la fecha
    * 
    * @return Año de la fecha
    */
   public static int getYear(Date fecha)
   {
     if (fecha == null)
       return 0;
    
     return Integer.parseInt(getFecha(fecha,"yyyy"));
   }
   /**
    * Devuelve el mes de la fecha mandada
    * @param fecha Objeto Date con la fecha
    * 
    * @return Año de la fecha
    */
   public static int getMonth(Date fecha)
   {
     if (fecha == null)
       return 0;
    
     return Integer.parseInt(getFecha(fecha,"MM"));
   }
   /**
    * @deprecated  use getFecha
    * @param fecha
    * @param formFecFin
    * @return
    */
   public static String formatearFecha(Date fecha,   String formFecFin)
   {
        return getFecha(fecha, formFecFin);
  }
  /**
   * Devuelve la fecha actual del sistema
   * @param formato formato con el que desea recibir la fecha
   * @return String con la fecha
   */
   public static String getFechaAct(String formato)
   {
     return getFecha(new Date(System.currentTimeMillis()),formato);
   }
   public static Date getDateAct()
   {
     return new Date(System.currentTimeMillis());
   }
   /**
    * Compara dos fechas
    * @param comp1 Fecha inicial
    * @param comp2 Fecha Final
    * @return Diferencia en dias entre esas dos fechas (Resta a la fecha Inicial, la fecha Final)
    */
   public static long comparaFechas(Date comp1, Date comp2)
   {
     if (comp1==null || comp2==null)
       return 0;
     return ((comp1.getTime()-comp2.getTime())/86400000);
   }

   /**
    * Devuelve la diferencia en dias entre dos fechas
    * @param fecFin Fecha Final (la mayor) en formato "dd-MM-yyyy"
    * @param fecIni Fecha Inicial (la menos)  en formato "dd-MM-yyyy"
    * @return dias que pasan
    * @throws ParseException Si hay errores de formato etc.
    */
   public static int restaDias(String fecFin, String fecIni) throws ParseException
   {
     GregorianCalendar gc = new GregorianCalendar();
     gc.setTime(Formatear.getDate(fecFin, "dd-MM-yyyy"));
     long ms = gc.getTimeInMillis();
     gc.setTime(Formatear.getDate(fecIni, "dd-MM-yyyy"));
     ms = ms - gc.getTimeInMillis();
     int dias =(int) (ms / 86400000);//(1000 * 3600 * 24);
     return dias;
   }
   /**
    * Le suma unos dias a una fecha dada.
    * @param fecha Fecha en un String
    * @param formato Formato de la fecha
    * @param numDias Numero de dias a sumar
    * @return Fecha en formato dd-MM-yyyy
    * @throws ParseException
    */
   public static String sumaDias(String fecha, String formato, int numDias) throws
       ParseException
   {
     return sumaDias(getDate(fecha, formato), numDias);
   } 
   /**
    * Suma dias a una fecha. 
    * @param fecha fecha
    * @param numDias numero dias
    * @return  Fecha en formato dd-MM-yyyy
    */
    public static String sumaDias(Date fecha,int numDias)
    {
      return getFechaVer(sumaDiasDate(fecha,numDias));
    }
    public static Date sumaDiasDate(Date fecha,int numDias)
    {
      if (fecha==null)
          return null;
      GregorianCalendar gc= new GregorianCalendar();
      gc.setTime(fecha);
      gc.add(GregorianCalendar.DAY_OF_MONTH,numDias);
      return gc.getTime();
    }
    public static Date sumaMesDate(Date fecha,int numMeses)
    {
      GregorianCalendar gc= new GregorianCalendar();
      gc.setTime(fecha);
      gc.add(GregorianCalendar.MONTH,numMeses);
      return gc.getTime();
    }
    public static Date sumaAnoDate(Date fecha,int numAnos)
    {
      GregorianCalendar gc= new GregorianCalendar();
      gc.setTime(fecha);
      gc.add(GregorianCalendar.YEAR,numAnos);
      return gc.getTime();
    }
    /**
     * Devuelve el numero de Control utilizado por los bancos
     *
     */
    public static int  getNumControl(double numero)
    {
      int[] val=new int[]{6,3,7,9,10,5,8,4,2,1};

      String numstr=Formatear.format(numero,"9999999999");
      int lg=numstr.length()-1;
      int pos;
      int valor=0;
      for (pos=0;lg>=0;lg--,pos++)
      {
        valor+=Integer.parseInt(numstr.substring(lg,lg+1))*val[pos];
      }
      pos = valor % 11;

      valor=11-pos;
      if (valor == 10)
        valor=1;
      if (valor == 11)
        valor=0;
      return valor;
    }
    /**
     * Encripta un string en formato SHA-1
     * @param plaintext Texto a encriptar.
     * @return Texto encriptado en SHA-1
     * @throws Exception Error al encriptar.
     */
    public static String encrypt(String plaintext) throws Exception {

        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA"); //step 2
        md.reset();
        md.update(plaintext.getBytes("UTF-8")); //step 3
        byte raw[] = md.digest(); //step 4
        String hash = MimeUtility.encodeText(new String(raw,0,raw.length) ); //step 5
        return hash; //step 6
    }
   public static byte[] getMD5(String input){
        try{
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        }  catch (Exception e){
             return null;
        }
    }
    static final String HEXES = "0123456789ABCDEF";

    public static String byteToHex( byte [] raw ) {
        if ( raw == null ) {
          return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
          hex.append(HEXES.charAt((b & 0xF0) >> 4))
             .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static byte[] hexToByte( String hexString){
        int len = hexString.length();
        byte[] ba = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return ba;
    }
    public static Point getCentroLocation(Dimension frameSize)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        return new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    public  static String getHostName() 
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        } catch ( UnknownHostException k)
        {
          try 
          {
              return Inet4Address.getLocalHost().getHostAddress();
          } catch ( UnknownHostException k1)
          {
             return "*FAIL*";
          }
        }
    }
}
