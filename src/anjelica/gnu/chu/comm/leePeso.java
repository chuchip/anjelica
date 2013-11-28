package gnu.chu.comm;

import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import java.util.*;
import gnu.io.*;
import java.io.*;
/**
 *
 * <p>Título: leePeso</p>
 * <p>Descripción: Clase que lee el peso de un puerto serie para basculas</p>
 * 
*  <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia P�blica General de GNU seg�n es publicada por
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
 * <p>Empresa: MISL</p>
 * @author Chuchi P
 * @version 2.0
 */
public class leePeso
{
  CommPortIdentifier portId;
  private String nombre=null;
  InputStream inputStream;
  OutputStream outputStream;
  SerialPort serialPort;
  int nInt=10;
  private boolean swInic=false;
  String puerto;
  int velocidad;
  int bitsDatos;
  int bitsStop;
  int paridad;
  int flowControl;
  boolean swDebug=false;
  String cadLeePeso=null;
  boolean datosDisponi=false;
  private int tipo=tipo_STD; // Tipo de Peso. Indica como se debe interpretar la lectura.
  public final static int tipo_STD=1;
  public final static int tipo_RAW=2; // Busca números y los devuelve sin hacer ninguna comprobación más
  private boolean activo=false;
  
  /**
   * Según el parametro bascula abrira el fichero de propiedades bascula.properties si es
   * '0', bascula1.properties si es 1, bascula2.properities si es 2...
   * @param raiz raiz de donde esta el fichero de propiedades. Normalmente gnu.chu.anjelica
   * @param bascula Numero de bascula a abrir.
   * @throws Exception
   */
  public leePeso(String ficProperties) throws Exception
  {
    ResourceBundle param=null;
    try {
        param = ResourceBundle.getBundle(ficProperties);
    } catch (Exception k)
    {
        throw new Exception("ERROR al cargar Fichero de Propiedades: "+ficProperties+"\n"+k.getMessage());
    }
    Enumeration en = param.getKeys();
    String el;
    String val;
    while (en.hasMoreElements())
    {
      el = en.nextElement().toString().toUpperCase();
      val = param.getString(el).trim();
      if (el.equals("NOMBRE"))
        nombre=val;
      if (el.equals("PUERTO"))
        puerto=val;
      if (el.equals("CADLEEPESO"))
        cadLeePeso=val;
      if (el.equals("VELOCIDAD"))
        velocidad=Integer.parseInt(val);
      if (el.equals("BITSDATOS"))
        bitsDatos=Integer.parseInt(val);
      if (el.equals("BITSSTOP"))
        bitsStop=Integer.parseInt(val);
      if (el.equals("PARIDAD"))
        paridad=Integer.parseInt(val);
      if (el.equals("FLOWCONTROL"))
        flowControl=Integer.parseInt(val);
      if (el.equals("NUMINTENTOS"))
        nInt=Integer.parseInt(val);
      if (el.equals("DEBUG"))
         if (Integer.parseInt(val)!=0)
            swDebug= true;
      if (el.equals("TIPO"))
        tipo=Integer.parseInt(val);
    }
   
  }
  /**
   * Iniciar comunicaciones por puerto serie
   */
  public void iniciar()
  {
    activo=false;
    if (swInic)
        return; // Ya se inicio
    try  {
        swInic=true;
        if (swDebug)
           System.out.println("Puerto: "+puerto+
                   " Velocidad: "+velocidad+" bitsDatos: "+bitsDatos+
                            " BitsStop: "+bitsStop+" paridad: "+paridad+" FLOWCONTROL: "+flowControl);
        portId =CommPortIdentifier.getPortIdentifier(puerto);
        serialPort = (SerialPort) portId.open("gnu.chu.comm.leePeso", 2000);
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();

        serialPort.notifyOnDataAvailable(true);
        serialPort.setFlowControlMode(flowControl);
        serialPort.setSerialPortParams(velocidad,
                 bitsDatos,// SerialPort.DATABITS_8,
                 bitsStop, // SerialPort.STOPBITS_1,
                 paridad);//SerialPort.PARITY_NONE);
    } catch (Exception k)
    {
        mensajes.mensajeAviso("Error al abrir puerto serie : "+puerto+" de bascula: "+nombre);
        return;
    }
    activo=true;
  }
  public boolean isIniciado()
  {
      return swInic;
  }
  public void resetIniciado()
    {
      swInic=false;
  }
  public boolean isActivo()
  {
    return activo;
  }
  public String getNombre()
  {
    return nombre;
  }
  public double getPeso()
  {
    if (!swInic)
      return 0;
    double peso;
    try {
      String lect;
      datosDisponi=false;
      lect=cadLeePeso==null?"0224323403":cadLeePeso;
//      System.out.println("Cadena Mandada: "+lect);

      outputStream.write(Formatear.HexToBytes(lect));
      int nIntentos=10;
//      System.out.println("Esperando a ver si puedo leer algo ....");
      while (nIntentos>0)
      {
        if (inputStream.available() >0)
        {
          peso=leeDatos(tipo);
          if (peso!=0)
            return peso;
        }
        Thread.sleep(100);
        nIntentos--;
      }
       if (inputStream.available() !=0)
        return 0;
      peso=leeDatos(tipo);
      if (peso != 0)
        return peso;
    } catch (Exception k)
    {
      k.printStackTrace();
      return 0;
    }
    return 0;
  }

  private double leeDatos(int tipo) throws IOException
  {
//    System.out.println("en LeeDatos");
    byte[] readBuffer = new byte[30];
    int numBytes=0;
    String peso="";
//    System.out.println("Disponible: "+inputStream.available());
//    if (inputStream.available() == 0)
//    {
//      System.out.println("que no tengo bytes disponibles");
//      return 0;
//    }
    numBytes=0;
    int nBytes;

    while (inputStream.available()>0)
    {
           nBytes = inputStream.read(readBuffer);
           numBytes+=nBytes;
           peso=peso + new String(readBuffer,0,nBytes);
    }

    // Para pruebas
//    readBuffer=Formatear.HexToBytes("0241202020302e3532300d");
//    peso=new String(readBuffer);
//    numBytes=peso.length();
// Fin de Pruebas
    readBuffer=peso.getBytes();
    // Fin de Pruebas
    if (swDebug)
          System.out.println("Cadena recibida en Hex: "+Formatear.StrToHex(peso)+" TIPO: "+tipo);
    if (tipo == tipo_RAW)
    {
        if (swDebug)
          System.out.println("TIPO RAW");
        int posIni=0,posFin=0,n;
        for (n=0;n<numBytes;n++)
        {
            if (Character.isDigit(peso.charAt(n)))
            {
                posIni=n;
                break;
            }
        }
        if (posIni==0)
        {
            if (swDebug)
                System.out.println("No encontrado ningun digito en la cadena");
            return 0;
        }
        for (n=posIni+1;n<numBytes;n++)
        {
            if (! Character.isDigit(peso.charAt(n)) && peso.charAt(n)!='.')
            {
                posFin=n;
                break;
            }
        }
        if (posFin==0)
            posFin=numBytes;
        if (swDebug)
             System.out.println("PESO: "+peso+" peso:*"+peso.substring(posIni,posFin)+"*");
        peso=peso.substring(posIni,posFin);
        try {
          double pesoDoble= Double.parseDouble(peso.trim());
//          if (swDebug)
//                System.out.println("peso en decimal: "+pesoDoble);
          return pesoDoble;
        } catch (NumberFormatException k)
        {
          if (swDebug)
                k.printStackTrace(System.out);
          return 0;
        }
    }
      if (swDebug)
          System.out.println("TIPO ESTANDARD");
//       000102030405060708091011
//       06024a202020202d312e330d
//    System.out.println("Leido: "+peso+ " Hex: "+Formatear.StrToHex(peso)+ " N. Car: "+numBytes);
    if (numBytes<12)
    {
       if (swDebug)
          System.out.println("Numero Bytes < 12");
      return 0;
    }

    if (readBuffer[0]!=6)
    {
        if (swDebug)
         System.out.println("Byte 0 no es 6");
      return 0;
    }
    if (readBuffer[11]!=13) // 0D
    {
      if (swDebug)
          System.out.println("Posicion 11 No ES 13. Es: "+readBuffer[11]);
      return 0;
    }
//    Formatear.HexToDec("4a");
    int chk=0;
//    byte chk1,chk2;
    int n=0;
    for (n=0;n<numBytes-3;n++)
       chk=chk ^ readBuffer[n];
    chk=chk ^ numBytes;
//    chk1= (byte) Formatear.StrToHex(""+chk).charAt(0);
//    chk2= (byte) Formatear.StrToHex(""+chk).charAt(1);
//    System.out.println("Check: "+chk);
//    System.out.println("Estado: "+readBuffer[1]);
    peso="";
    for (n=3;n<numBytes-1;n++)
      peso+=""+(char) readBuffer[n];
    if (swDebug)
        System.out.println("Peso: "+peso);
    try {
      return Double.parseDouble(peso.trim());
    } catch (NumberFormatException k)
    {
      return 0;
    }
  }

   public static void main(String[] args) {
     try {
       leePeso lp = new leePeso("gnu.chu.anjelica.bascula1");
       System.out.println("Peso: "+lp.leeDatos(leePeso.tipo_RAW));

     } catch (Exception k)
     {
       k.printStackTrace();
     }
   }

}
