package gnu.chu.utilidades;

import java.io.*;
import gnu.chu.interfaces.*;
/**
* Clase que sustitye a la estandar System.err
* <p>
* Cuando haya cualquier salida hacia la salida estandar de errores la capturara
* esta pantalla mostrando un PopError con el mensaje y dando la opcion de cancelar
* o reintentar.
*
* @Version 1.0
* @author Chuchi 4/11/98
*/
public class printError extends  PrintStream  implements Serializable {
  String mensaje="";
  boolean inicio=true;
  Thread  th;
  ejecutable prog;

  public printError(OutputStream out, boolean autoFlush) {
    super(out,autoFlush);
    th = Thread.currentThread();
  }

  public void println(char s[])
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
      new printTH(this);
    }
    mensaje=mensaje+"\n"+s+"\n";
  }

  public void println(String s)
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
      new printTH(this);
    }
    mensaje=mensaje+"\n"+s+"\n";
  }

  public void print(char s[])
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
      new printTH(this);
    }
    mensaje=mensaje+s+"\n";
  }

  public void print(String s)
  {
    System.out.println(s);
  }
  public void print(Object o)
  {
    if (inicio)
    {
      inicio=false;
      mensaje="";
      new printTH(this);
    }
    mensaje=mensaje+o+"\n";
  }

  public void getMessage()
  {
    PopError pe = new PopError(mensaje);
    pe.setEjecutable(prog);
    pe.Bcancelar.requestFocus();
    pe.setVisible(true);
  }

  public void setEjecutable(ejecutable prog)
  {
    this.prog=prog;
  }

}


class printTH implements Runnable
{
  printError pErr;
  Thread t;
  public printTH(printError p)
  {
    pErr=p;
    t= new Thread(this);
//    t=Thread.currentThread();
    t.start();
  }

  public void run()
  {
    try {
      Thread.sleep(2000);
    } catch (Exception k){}
    pErr.inicio=true;
    pErr.th.suspend();
    pErr.getMessage();
    pErr.th.resume();
  }
}
