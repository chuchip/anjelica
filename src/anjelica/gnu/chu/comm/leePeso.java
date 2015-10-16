package gnu.chu.comm;

import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.mensajes;
import java.util.*;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
/**
 *
 * <p>Título: leePeso</p>
 * <p>Descripción: Clase que lee el peso de un puerto serie para basculas</p>
 * 
*  <p>Copyright: Copyright (c) 2005-2014
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia P�blica General de GNU según es publicada por
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
  double pesoReal=-1;
  double pesoCaja=0,tara=0;
  int numeroCajas=0;
 
  private int startChar=0,endChar=0;   
  private String nombre=null;
//  InputStream inputStream;
//  OutputStream outputStream;
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
      if (el.equals("STARTCHAR"))
      {
        try {
            startChar=Integer.parseInt(val);
        }  catch (NumberFormatException k){}
      }
       if (el.equals("ENDCHAR"))
      {
        try {
            endChar=Integer.parseInt(val);
        }  catch (NumberFormatException k){}
      }
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
        if (puerto.equals("null"))
        {
            if (swDebug)
                System.out.println("Puerto deshabilitado");
            return;
        }
        serialPort = new SerialPort(puerto);
        if (! serialPort.openPort())
        {
             if (swDebug)
                System.out.println("No se pudo abrir el puerto");
            return;
        }

                
       
        serialPort.setFlowControlMode(flowControl);
        serialPort.setParams(velocidad,
                 bitsDatos,// SerialPort.DATABITS_8,
                 bitsStop, // SerialPort.STOPBITS_1,
                 paridad);//SerialPort.PARITY_NONE);
        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
        serialPort.setEventsMask(mask);//Set mask
        serialPort.addEventListener(new SerialPortEventListener()
        {
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                try
                {                    
                     if(event.isRXCHAR())
                     {
                        if (event.getEventValue() >5)
                        {
                            double peso= leeDatos(tipo);
                            pesoReal=peso;
                        }
                     }
                     else    if(event.isCTS())
                     {//If CTS line has changed state
                        if(event.getEventValue() == 1)
                        {//If line is ON
                           if (swDebug) System.out.println("CTS - ON");
                        }
                        else 
                        {
                           if (swDebug)  System.out.println("CTS - OFF");
                        }
                    }
                     else if(event.isDSR())
                     {///If DSR line has changed state
                        if(event.getEventValue() == 1){//If line is ON
                           if (swDebug)  System.out.println("DSR - ON");
                        }
                        else
                        {
                            if (swDebug)  System.out.println("DSR - OFF");
                        }
                     }
                } catch (SerialPortException ex)
                {
                    if (swDebug)
                        SystemOut.print(ex)  ;                 
                }
            }
           });
    } catch (SerialPortException k)
    {
        if (swDebug)
            SystemOut.print(k);
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
      return getPeso(true);
  }
  public void setTara(double tara)
  {
      this.tara=tara;
  }
  public double getPesoDescontar()
  {
      return tara + (numeroCajas*pesoCaja);      
  }
  public void setNumeroCajas(int numCajas)
  {
      this.numeroCajas=numCajas;
  }
  public void setPesoCajas(double pesoCaja)
  {
   this.pesoCaja=pesoCaja;   
  }

    public double getPesoCaja() {
        return pesoCaja;
    }

    public double getTara() {
        return tara;
    }

    public int getNumeroCajas() {
        return numeroCajas;
    }
  
  /**
   * 
   * @param desTarado Indica si se debe quitar la tara y peso envases.
   * @return 
   */
  public  double getPeso(boolean desTarado)
  {
    if (! swInic)
        return 0;
    if ( serialPort==null) 
        return 0;
    if (!serialPort.isOpened()) 
       return 0;
    try {
      String lect;
      pesoReal=-1;
      datosDisponi=false;
      lect=cadLeePeso==null?"0224323403":cadLeePeso;
      if (swDebug)
          System.out.println("Intentando mandar cadena: "+lect);
      if (serialPort!=null)
      {
        if (serialPort.isOpened())
        {           
            serialPort.writeBytes(Formatear.HexToBytes(lect));          
        }
      }
      if (swDebug)
          System.out.println("Cadena mandada...");
      int nIntentos=10;
      
      while (nIntentos>0)
      {    
          if (swDebug)
              System.out.println("en getPeso. Intento: "+nIntentos+" Peso Real: "+pesoReal);
          if (pesoReal>=0)
            return pesoReal - (double) (desTarado? getPesoDescontar():0);
          Thread.sleep(500);
          nIntentos--;
      }     
    } catch (SerialPortException | InterruptedException k)
    {
       SystemOut.print(k);
       return 0;
    }
    return 0;
  }

  double leeDatos(int tipo) throws SerialPortException
  {
    if (swDebug)
        System.out.println("en LeeDatos");
    byte[] readBuffer;// = new byte[30];
    int numBytes;
    String peso="";

    numBytes=0;
//    int nBytes;
    if (serialPort!=null)
    {
//        if (swDebug)
//          System.out.println("InputStream: NO ES NULL");
        if (serialPort.isOpened())
        {
            while (serialPort.getInputBufferBytesCount()>0)
            {   
                   readBuffer = serialPort.readBytes();
                   numBytes+=readBuffer.length;
                   peso=peso + new String(readBuffer,0,readBuffer.length);
            }
            // Por si acaso va lento, espero 300 ms y vuelvo a intentar leer.
            try
            {
                 Thread.sleep(300);
            } catch (InterruptedException ex)
            {
                 SystemOut.print(ex);
            }
            while (serialPort.getInputBufferBytesCount()>0)
            {   
                   readBuffer = serialPort.readBytes();
                   numBytes+=readBuffer.length;
                   peso=peso + new String(readBuffer,0,readBuffer.length);
            }
        }
    }
//    else
//    {
//     // Para pruebas
//        readBuffer=Formatear.HexToBytes("02412020202020342C350d");
//        peso=new String(readBuffer);
//        numBytes=peso.length();
//        // Fin de Pruebas
//     }
//   
    readBuffer=peso.getBytes();
   
    if (swDebug)
          System.out.println("Cadena recibida en Hex: "+Formatear.StrToHex(peso)+" TIPO: "+tipo);
    if (tipo == tipo_RAW)
    {
        if (swDebug)
          System.out.println("TIPO RAW");
        int posIni=startChar>0?startChar:0;
        if (posIni>=numBytes)
            throw new SerialPortException(puerto, "", "Numero bytes leidos ("+numBytes+
                ") inferior Posicion Incial"+posIni);
        int posFin=endChar>0?endChar:0;
        int n;
        if (peso.indexOf(".")==-1) // No hay puntos, x si acaso remplazo las , x .
              peso=peso.replace(',', '.');

        for (n=posIni;n<numBytes;n++)
        {
            if (Character.isDigit(peso.charAt(n)))
            {
                posIni=n;
                break;
            }
        }
        if (posIni==0)
        {
            throw new SerialPortException(puerto, "", "No encontrado ningún digito en la cadena");
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
        peso=peso.substring(posIni,posFin>numBytes?numBytes:posFin);
        try {
          double pesoDoble= Double.parseDouble(peso.trim());
          if (swDebug)
                System.out.println("peso en decimal: "+pesoDoble);
          return pesoDoble;
        } catch (NumberFormatException k)
        {
          if (swDebug)
                 SystemOut.print(k);
           throw new SerialPortException(puerto, "", "Error al pasar string "+peso+ " a numero");
        }
    }
      if (swDebug)
          System.out.println("TIPO ESTANDARD");
//       000102030405060708091011
//       06024a202020202d312e330d
//    System.out.println("Leido: "+peso+ " Hex: "+Formatear.StrToHex(peso)+ " N. Car: "+numBytes);
    if (numBytes<12)
         throw new SerialPortException(puerto, "","Numero Bytes < 12");    

    if (readBuffer[0]!=6)
        throw new SerialPortException(puerto, ""," Byte 0 no es 6");    
    if (readBuffer[11]!=13) // 0D
       throw new SerialPortException(puerto, "","Posicion 11 No ES 13. Es: "+readBuffer[11]);    
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
       throw new SerialPortException(puerto, "", "Error al pasar String "+peso+ " a numero");
    }
  }

   public static void main(String[] args) {
     try {
       leePeso lp = new leePeso("gnu.chu.anjelica.bascula1");
       lp.startChar=5;
       lp.endChar=0;
       System.out.println("Peso: "+lp.leeDatos(leePeso.tipo_RAW));

     } catch (Exception k)
     {
       SystemOut.print(k);
     }
   }
   
}
