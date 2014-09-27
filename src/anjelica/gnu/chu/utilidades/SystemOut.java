package gnu.chu.utilidades;

import java.io.*;

/**
* Clase para tratar la standard out y standard error, dirigiendolo
* a log4j2
* @Version 1.0
* @author chuchip (2014)
*/
public class SystemOut extends PrintStream implements Serializable
{
  private boolean salidaErr=false;  
  
  public SystemOut(OutputStream out,boolean autoFlush)
  {
    super(out,autoFlush);
  }
  public SystemOut(OutputStream out)
  {
    this(out,true);
  }
  
  public void setSalidaError(boolean salErr)
  {
      this.salidaErr=salErr;
  }
  
  public void println(char s[])
  {
    if (salidaErr)
        ventana.logger.error(s+"\n");
    else
        ventana.logger.trace(s+"\n");        
  }

  public void println(String s)
  {
    if (salidaErr)
        ventana.logger.error(s+"\n");
    else
        ventana.logger.trace(s+"\n");        
  }

  public void print(char s[])
  {
    if (salidaErr)
        ventana.logger.error(s);
    else
        ventana.logger.trace(s);
  }

  public void print(String s)
  {
   if (salidaErr)
        ventana.logger.error(s);
    else
        ventana.logger.trace(s);
  }
  public void print(Object o)
  {
  if (salidaErr)
        ventana.logger.error(o);
    else
        ventana.logger.trace(o);
  }

  
}

