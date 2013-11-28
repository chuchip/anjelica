package gnu.chu.utilidades;

import java.io.*;
/**
* Clase donde dejar los printStakTrace.
* @Version 1.0
* @author Chuchi 4/11/98
*/
public class escribe extends PrintWriter implements Serializable
{
  String mensaje="";
  boolean inicio=true;

  public escribe(OutputStream out,boolean autoFlush)
  {
    super(out,autoFlush);
  }
  public escribe(OutputStream out)
  {
    this(out,true);
  }

  public void println(char s[])
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
    }
    mensaje=mensaje+"\n"+s;
//        mensaje=mensaje+"-"+s;
  }

  public void println(String s)
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
    }
//    mensaje=mensaje+";"+s;
    mensaje=mensaje+"\n"+s;
  }

  public void print(char s[])
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
    }
    mensaje=mensaje+s;
  }

  public void print(String s)
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
    }
    mensaje=mensaje+"\n"+s;

  }
  public void print(Object o)
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
    }
    mensaje=mensaje+o;
  }

  public String getMessage()
  {
    inicio=true;
    return mensaje;
  }

  public void setInicio(boolean inic)
  {
    inicio=inic;
  }
}

