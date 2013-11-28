package gnu.chu.utilidades;

import java.sql.Date;
import java.io.*;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.text.SimpleDateFormat;

public class Fecha implements Serializable {

private char[] chDia = new char[2]; // Varibles de
private char[] chMes = new char[2]; // fecha en
private char[] chYear = new char[4]; // tipo char

private int intDia = 0; // Varibles de
private int intMes = 0; // fecha en
private int intYear = 0; // tipo int

private String strFecha; // Fecha en tipo String
public Date dtFecha; // Fecha en tipo Date

private String frRecepcion = "yyyy-MM-dd"; // Formato Recepcionado
private String frSalida = "yyyy-MM-dd"; // Formato Salida

private char[] separador = new char[1];
private String MsgError=""; // Mensaje de error

// Dias de la semana
static public final int SUNDAY = 0;
static public final int MONDAY = 1;
static public final int TUESDAY = 2;
static public final int WEDNESDAY = 3;
static public final int THURSDAY = 4;
static public final int FRIDAY = 5;
static public final int SATURDAY = 6;

/*
/* Constructor                                            */
public Fecha() {
}

/*
/* Constructor                                            */
/* recibe la fecha y asigna formatos por defecto          */
public Fecha(String fech) {
this(fech, "yyyy-MM-dd", "yyyy-MM-dd");
}

/*
/* Constructor                                            */
/* recibe la fecha y el formato de recepcion              */
/* Asigna el formato de salida por defecto                */
public Fecha(String fech, String frRecep) {
this(fech, frRecep, "yyyy-MM-dd");
}

/*
/* Constructor                                            */
/* recibe la fecha, el formato de recepcion y el de salida*/
public Fecha(String fech, String frRecep, String frSali) {
setFrSalida(frSali);
setFrRecepcion(frRecep);
setFecha(fech);
}

/*
/* Compara la fecha que se le envia como parametro        */
/* con la fecha actualmente asignada                      */
/* Devuelve el numero de dias de diferencia               */

public long comparaFechas(Date comp) {
    MsgError="";
try {
    return comparaFechas(dtFecha, comp);
} catch (Exception j) {
    MsgError = "" + j;
    return 0;
}
}

/*
/* Compara las fechas que se le envia como parametros
      */
public long comparaFechas(Date comp1, Date comp2) {
    MsgError="";
try {
    return ((comp1.getTime()-comp2.getTime())/86400000);
} catch (Exception j) {
    MsgError = "" + j;
    return 0;
}
}
public static long comparaFechas(String fecIni,String frFecIni,String fecFin,String  frFecFin)
{
Fecha fin = new Fecha(fecIni,frFecIni,frFecIni);
Fecha ffi = new Fecha(fecFin,frFecFin,frFecFin);

return fin.comparaFechas(ffi.dtFecha);
}

public static long comparaFechas(String fecIni,String fecFin)
{
return comparaFechas(fecIni,"dd-MM-yyyy",fecFin,"dd-MM-yyyy");
}
public static String Suma(String fec,int dias)
{
return Suma(fec,"dd-MM-yyyy",dias);
}
public static String Suma(String fec,String frFec,int dias)
{
 Fecha f = new Fecha(fec,frFec,frFec);
 return f.sumarDias(dias);
}
public static String SumaAno(String fec,int ano) {
return SumaAno(fec,"dd-MM-yyyy",ano);
}
public static String SumaAno(String fec,String frFec,int ano) {
 Fecha f = new Fecha(fec,frFec,frFec);
 return f.sumarAnos(ano);
}
public static String sumaMes(String fec,int mes) {
return sumaMes(fec,"dd-MM-yyyy",mes);
}
public static String sumaDia(String fec,String frFec,int dias) {
 Fecha f = new Fecha(fec,frFec,frFec);
 return f.sumarDias(dias);
}

/**
*  Suma a una fecha dada los dias laborables indecados
*/
public static String sumaDiaLaborables(String fec,String frFec,int dias){
Fecha f = new Fecha (fec,frFec,frFec);
String diasemana;  //dia de la semana
while (dias > 0){
    diasemana =f.formatFecha (f.sumarDias (1),"dd-MM-yyyy","EEEE");
    if (!(diasemana.equals ("sábado") || diasemana.equals ("domingo")))
      dias--;
}
return f.getFecha ("dd-MM-yyyy");
}


public static String sumaMes(String fec,String frFec,int mes) {
 Fecha f = new Fecha(fec,frFec,frFec);
 return f.sumarMes(mes);
}
/**
* Suma Dias a una fecha
* @param String Fecha inicial
* @param int dia a sumar
* @return Fecha el resultado de la suma de dias
*/
public static Fecha SumaFecha(String fec,int dias) {
return SumaFecha(fec,"dd-MM-yyyy",dias);
}
/**
* Suma Dias a una fecha
* @param String Fecha inicial
* @param String Formato de recepcion
* @param int dia a sumar
* @return Fecha el resultado de la suma de dias
*/
public static Fecha SumaFecha(String fec,String frFec,int dias) {
 Fecha f = new Fecha(fec,frFec,frFec);
 f.sumarDias(dias);
 return f;
}
/*
/* Devuelve el Year como int                               */
public int getYear(){
    MsgError="";
try {
    return intYear;
} catch (Exception j) {
    MsgError = "" + j;
    return 0;
}
}

/*
/* Devuelve el Dia como int                               */
public int getDia(){
    MsgError="";
try {
    return intDia;
} catch (Exception j) {
    MsgError = "" + j;
    return 0;
}
}

/*
/* Devuelve el ultimo error producido                     */
public String getError() {
return MsgError;
}

/*
/* Devuelve la fecha con el formato definido              */
public String getFecha() {
    MsgError="";
try {
    return getFecha(frSalida);
} catch (Exception j) {
    MsgError = "" + j;
    return "";
}
}

public String getFecha(String frSali) {
    MsgError="";
try {
    return getFecha (frSali, dtFecha);
} catch (Exception j) {
    MsgError = "" + j;
    return ""+j ;
}
}

public String getFecha(String frSali, Date dt) {
try {
    Locale.setDefault(new Locale("es","es",""));
    SimpleDateFormat formatter = new SimpleDateFormat(frSali);

    MsgError="";
    String s = formatter.format(dt);
    Locale.setDefault(Formatear.local);
    return s;
} catch (Exception j) {
    Locale.setDefault(Locale.getDefault());
    MsgError = "" + j;
    return ""+j ;
}
}

private static String getFecha(java.util.Date dt, String frSali) {
try {
    Locale.setDefault(new Locale("es","es",""));
    SimpleDateFormat formatter = new SimpleDateFormat(frSali);

    String s = formatter.format(dt);
    Locale.setDefault(Formatear.local);
    return s;
} catch (Exception j) {
   Locale.setDefault(Formatear.local);
   return ""+j ;
}
}

/*
/* Devuelve el mes como int                               */
public int getMes(){
    MsgError="";
try {
    return intMes;
} catch (Exception j) {
    MsgError = "" + j;
    return 0;
}
}

/*
/* Devuelve el separador del formato de recepcion         */
private char getSeparador(String frRecep) {
int n;
char separa = '-';

MsgError="";
try {
    for (n = 0; n < frRecep.length(); n++) {
        if (frRecep.charAt(n) != 'd' && frRecep.charAt(n) != 'M' && frRecep.charAt(n) != 'y') {
        separa = frRecep.charAt(n);
        }
  }
    return separa;
} catch (Exception j) {
    MsgError = "" + j;
    return separa;
}
}

/*
/* Crea una variable tipo Date con el dia, mes y Year      */
private void putDate() {
    MsgError="";

try {
    Integer dd = new Integer("" + chDia[0] + chDia[1]);
    intDia=dd.intValue();

    dd = new Integer("" + chMes[0] + chMes[1]);
    intMes=dd.intValue();

    dd = new Integer("" + chYear[0] + chYear[1] + chYear[2] + chYear[3]);
    intYear=dd.intValue();

    if (intYear<100) {
        if (intYear<70) {
        intYear=intYear+2000;
        } else {
        intYear=intYear+1900;
      }
    } else {
        if (intYear<1000) {
            if (intYear<970) {
            intYear=intYear+2000;
          } else {
            intYear=intYear+1000;
        }
        }
    }
    dtFecha = creaDate(intDia, intMes, intYear);
} catch (Exception j) {
    MsgError = "" + j;
  return;
}
}

/*
/* Resta a la fecha
*/
public String restarDias(int dias) {
    MsgError="";
try {
    dtFecha = new Date(intYear-1900, intMes-1, intDia - dias);
    separaFecha(getFecha(frRecepcion, dtFecha), frRecepcion);
    return getFecha();
} catch (Exception j) {
    MsgError = "" + j;
    return "" + j;
}
}

/*
/* Separa la fecha en dia, mes y Year                      */
private void separaFecha(String  fech, String frRecep) {
    char[] formato = new char[3];
int Wi=0, Wt=0;
int ndia=0, nMes=0, nYear=0;

    MsgError="";
if (fech==null)
{
  MsgError="Fecha es NULL";
  return;
}
if (fech.compareTo("") == 0){
    MsgError = "Fecha no asignada correctamente";
  return;
}
if (frRecep.compareTo("") == 0){
    MsgError = "Formato Recepcion no asignado correctamente";
  return;
}

separador[0] = getSeparador(frRecep);

    try {
    // comprueba el formato de recepcion
        for(Wi=0;Wi<3;formato[Wi++]=' ');

    Wt=0;
        for (Wi=0;Wi<frRecep.length();Wi++) {
        if (frRecep.charAt(Wi) == separador[0]) {
            Wt++;
      } else {
            formato[Wt]=frRecep.charAt(Wi);
      }
    }

    // asigna valores a dia, intMes, Year
        for(Wi=0;Wi<2;chDia[Wi++]=' ');
        for(Wi=0;Wi<2;chMes[Wi++]=' ');
        for(Wi=0;Wi<4;chYear[Wi++]=' ');

    Wt=0;
        for (Wi=0;Wi<strFecha.length();Wi++) {
        if (fech.charAt(Wi) == separador[0]) {
            Wt++;
      } else {
            switch (formato[Wt]) {
                case 'd':
                            chDia[ndia]=fech.charAt(Wi);
                  ndia++;
                  break;
                case 'M':
                            chMes[nMes]=fech.charAt(Wi);
                  nMes++;
                  break;
                case 'y':
                            chYear[nYear]=fech.charAt(Wi);
                  nYear++;
                  break;
          }
      }
    }

    if (chDia[0]==' ') {
        chDia[0]='0';
    }
        if (chDia[1]==' ') {
        chDia[1]=chDia[0];
      chDia[0]='0';
    }
    if (chMes[0]==' ') {
        chMes[0]='0';
    }
        if (chMes[1]==' ') {
        chMes[1]=chMes[0];
      chMes[0]='0';
    }
    if (chYear[0]==' ') {
        chYear[0]='0';
    }
        if (chYear[1]==' ') {
        chYear[1]=chYear[0];
      chYear[0]='0';
    }
        if (chYear[2]==' ') {
        chYear[2]=chYear[1];
        chYear[1]=chYear[0];
      chYear[0]='0';
    }
        if (chYear[3]==' ') {
        chYear[3]=chYear[2];
        chYear[2]=chYear[1];
        chYear[1]=chYear[0];
      chYear[0]='0';
    }

  putDate();

} catch (Exception j) {
    MsgError = "" + j;
  return;
}
}

/*
/* Inicializa la variable fecha                           */
public void setFecha(String fech) {
setFecha(fech, frRecepcion);
}
public void setFecha(String fech, String frRecepcion) {
setFrRecepcion(frRecepcion);
    MsgError="";
if (fech==null)
{
  MsgError="Fecha  es NULL";
  return;
}
if (fech.length()<frRecepcion.length())
{
  MsgError="Fecha NO Valida";
  return;
}
strFecha=fech.substring(0,frRecepcion.length());
separaFecha(strFecha, frRecepcion);
}

/*
/* Asigna el formato de recepcion predeterminado          */
public void setFrRecepcion(String frRecep) {
    MsgError="";
frRecepcion = frRecep;
}

/*
/* Asigna el formato de salida predeterminado             */
public void setFrSalida(String frSali) {
    MsgError="";
frSalida = frSali;
}

/*
/* Suma Dias a la fecha                                   */
public String sumarDias(int dias) {
    MsgError="";
try {
    dtFecha = new Date(intYear-1900, intMes-1, intDia + dias);
    separaFecha(getFecha(frRecepcion, dtFecha), frRecepcion);
    return getFecha();
} catch (Exception j) {
    MsgError = "" + j;
    return "" + j;
}
}
/*
/* Suma Anos a la fecha                                   */
public String sumarAnos(int ano) {
    MsgError="";
try {
    dtFecha = new Date(intYear-1900+ano, intMes-1, intDia );
    separaFecha(getFecha(frRecepcion, dtFecha), frRecepcion);
    return getFecha();
} catch (Exception j) {
    MsgError = "" + j;
    return "" + j;
}
}
/**
* Suma Meses a la fecha
*/
public String sumarMes(int nmes) {
    MsgError="";
try {
    dtFecha = new Date(intYear-1900, intMes-1+nmes, intDia );
    separaFecha(getFecha(frRecepcion, dtFecha), frRecepcion);
    return getFecha();
} catch (Exception j) {
    MsgError = "" + j;
    return "" + j;
}
}

/*
Devuelve una variable tipo Date
*/
public Date creaDate(int Dia, int Mes, int Year) {
    MsgError="";
if (System.getProperty("java.version").trim().compareTo("1.1.5") == 0)
  return new Date(Year-1900, Mes-1, Dia+1);
else
  return new Date(Year-1900, Mes-1, Dia);
}
/**
* Convierte un fecha en milisegundos
* a un Formato dd-MM-yyyy
*/
public static String milisegToString(long fechaMiliSegundos) {
return milisegToString(fechaMiliSegundos, "dd-MM-yyyy");
}
/**
* Convierte un fecha en milisegundos
* al formato que se le envia como parametro
*/
public static String milisegToString(long fechaMiliSegundos, String forma) {
try {
    java.util.Date a;
    Locale.setDefault(new Locale("es","es",""));
    if (System.getProperty("java.version").trim().compareTo("1.1.5") == 0)
        a = new java.util.Date(fechaMiliSegundos + 9 * 60 * 60 * 1000);
    else
        a = new java.util.Date(fechaMiliSegundos);
    String s = getFecha(a, forma);
    Locale.setDefault(Formatear.local);
    return s;
} catch (Throwable j) {
    Locale.setDefault(Formatear.local);
    return "";
}
}
/**
* Funcion static que formatea una Fecha en yyyy-MM-dd
*
* @param String Fecha a formatear
* @param String Formato de Recepcion
*
* @return String Fecha con formato yyyy-MM-dd
*/
public static String formatFecha(String fecha, String formaRecep) {
return formatFecha(fecha, formaRecep, "yyyy-MM-dd");
}
/**
* Funcion static que formatea una Fecha en el formato deseado
*
* @param String Fecha a formatear
* @param String Formato de Recepcion
* @param String Formato de Salida
*
* @return String Fecha con el formato indicado en formaSalida
*/
public static String formatFecha(String fecha, String formaRecep, String formaSalida) {
Fecha f = new Fecha(fecha, formaRecep, formaSalida);
return f.getFecha();
}
/**
* Retorna la fecha del sistema
*/
public static String getFechaSys(String forma) {
return milisegToString(System.currentTimeMillis(), forma);
}

public static int getYearSys()
{
String an=getFechaSys("yyyy");
return Formatear.StrToInt(an);
}

/**
* Funcion que me devuelve el dia del ano en el que estamos 1-36x
**/
public static int getDiadelAno(int dia,int mes,int ano){
 int numDias=0;
 if(mes>1){
   for(int m=1;m<mes;m++)
       numDias+=getDiasMes(m,ano);
 }
 numDias+=dia;
 return numDias;
}

/**
* Funcinn que me devuelve el nnmero de dias que tiene el un ano determinado
**/
public static int getDiasYear(int ano){
if(isBisiesto(ano)) return 366;
else return 365;
}

public static int getDiasMes(int mes,int ano)
{
if (mes > 12 || mes < 1)
    return -1;
 if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
      return 30;

 if (mes == 2)
 {
          if (isBisiesto(ano)==false)
      return 28;
    else
      return 29;
 }
 return 31;
}

/*
* Checkea si una fecha es Valida.
* @return null si es valida.
*         Un string con la descripcion del Error si NO es Valida.
*/
public static String isValida(int dia,int mes,int ano)
{
String msgError;
if (mes > 12 || mes < 1)
    return "El mes debe estar comprendido entre 1 y 12";
 if (dia > 31 || dia < 1)
     return "El dia debe estar comprendido entre 1 y 31";
 if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
 {
      if (dia > 30)
            return "El dia para el mes: "+mes+ " No puede ser superior a 30";
 }

 if (mes == 2)
 {
      if (dia > 29)
            return "Febrero NO pude tener mns de 29 dias";
          if (isBisiesto(ano)==false)
    {
              if (dia > 28)
                return "Febrero en Ano NO bisiesto no puede tener 29 dias";
    }
 }

 return null;
}
/*
* Comprueba si un Ano es Correcto.
* @param Ano a comprobar.
* @return true si el ano es bisiesto.
*/
public static boolean isBisiesto(int ano)
{
 int   in;

 if ( (ano / 100)*100 != ano)
 {
    if ((ano / 4)*4 != ano)
        return false;
    else
        return true;
 }
 else
 {
    in = ano / 100;
    if ( (in / 4) *4 != in)
       return false;
        in = in * 100;
    ano = ano - in;
    if ((ano / 4)*4 != ano)
        return false;
    else
        return true;
 }
}
/**
* Retorna los dias que tiene un mes
*/
public static int diasQueTieneElMes(int mes, int ano) {
      if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
          return 30;

      if (mes == 2)
         if (isBisiesto(ano))
            return 29;
         else
             return 28;

      return 31;
}

/**
* Nos dice si la fecha actual es mayor o no que la que se pasa como par�metro
* @param date es un dato tipo Date.
* @return boolean true si la fecha actual es mayor, false si es menor o igual.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean isMayorQue(Date date) {
if (0 < comparaFechas(date)) return true;
else return false;
}

/**
* Nos dice si la fecha actual es menor o no que la que se pasa como par�metro
* @param date es un dato tipo Date.
* @return boolean true si la fecha actual es menor, false si es mayor o igual.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean isMenorQue(Date date) {
if (0 > comparaFechas(date)) return true;
else return false;
}

/**
* Nos dice si la fecha actual es igual o no que la que se pasa como par�metro
* @param date es un dato tipo Date.
* @return boolean true si la fecha actual es igual, false si es distinta.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean equalsTo(Date date) {
if (0 == comparaFechas(date)) return true;
else return false;
}

/**
* Nos dice si la fecha actual es mayor o no que la que se pasa como par�metro
* @param date es un dato tipo Fecha.
* @return boolean true si la fecha actual es mayor, false si es menor o igual.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean isMayorQue(Fecha date) {
if (0 < comparaFechas(date.dtFecha)) return true;
else return false;
}

/**
* Nos dice si la fecha actual es menor o no que la que se pasa como par�metro
* @param date es un dato tipo Fecha.
* @return boolean true si la fecha actual es menor, false si es mayor o igual.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean isMenorQue(Fecha date) {
if (0 > comparaFechas(date.dtFecha)) return true;
else return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es MAYOR que la SEGUNDA
*/
public static boolean isMayorQue(String fecIni,String fecFin,String formato){
if (comparaFechas(fecIni,formato,fecFin,formato)>0)
  return true;
return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es MENOR que la SEGUNDA
*/
public static boolean isMenorQue(String fecIni,String fecFin,String formato){
if (comparaFechas(fecIni,formato,fecFin,formato)<0)
  return true;
return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es IGUAL a la SEGUNDA
*/
public static boolean isIgualQue(String fecIni,String fecFin,String formato){
if (comparaFechas(fecIni,formato,fecFin,formato)==0)
  return true;
return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es MAYOR que la SEGUNDA
*/
public static boolean isMayorQue(String fecIni,String fecFin){
if (comparaFechas(fecIni,"dd-MM-yyyy",fecFin,"dd-MM-yyyy")>0)
  return true;
return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es MENOR que la SEGUNDA
*/
public static boolean isMenorQue(String fecIni,String fecFin){
if (comparaFechas(fecIni,"dd-MM-yyyy",fecFin,"dd-MM-yyyy")<0)
  return true;
return false;
}

/**
* @param String fecIni Fecha inicial a comparar
* @param String fecFin Segunda de las fechas a comparar
* @return <i>true</i> Si la PRIMERA es IGUAL a la SEGUNDA
*/
public static boolean isIgualQue(String fecIni,String fecFin){
if (comparaFechas(fecIni,"dd-MM-yyyy",fecFin,"dd-MM-yyyy")==0)
  return true;
return false;
}

/**
* Nos dice si la fecha actual es igual o no que la que se pasa como par�metro
* @param date es un dato tipo Fecha.
* @return boolean true si la fecha actual es igual, false si es distinta.
* @author J. Carlos Muro (12/01/2000)
*/
public boolean equalsTo(Fecha date) {
if (0 == comparaFechas(date.dtFecha)) return true;
else return false;
}

/**
* Este m�todo nos devuelve el d�a de la semana al que pertenece la fecha, siendo
* 0 el Domingo, 1 el Lunes, ...., 7 el S�bado. Podemos usar las constantes
* SUNDAY, MONDAY, ..., SATURDAY.
*/
public int getDiaSemana() {
int dia = -1;
GregorianCalendar gg = new GregorianCalendar(getYear(),getMes()-1,getDia());
return gg.get(gg.DAY_OF_WEEK)-1;
}
}

