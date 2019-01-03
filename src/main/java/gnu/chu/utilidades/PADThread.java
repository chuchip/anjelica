package gnu.chu.utilidades;

import gnu.chu.interfaces.*;

/**
* Clase para ejecutar rutinas en Background.
*/
public class PADThread
    implements Runnable {
  PAD frVentana;
  int rutina;

  public PADThread(PAD f, int p) {
    //abrir();
    frVentana = f;
    rutina = p;
    Thread t = new Thread(this);
    t.setPriority(8);
    t.start();
  }

  public void run() {
    switch (rutina) {
      case PAD.ej_addnew:
        frVentana.ej_addnew1();
        break;
      case PAD.ej_delete:
        frVentana.ej_delete1();
        break;
      case PAD.ej_edit:
        frVentana.ej_edit1();
        break;
      case PAD.ej_query:
        frVentana.ej_query1();
        break;
      case PAD.PADADDNEW:
        frVentana.PADAddNew1();
        break;
      case PAD.PADEDIT:
        frVentana.PADEdit1();
        break;
      case PAD.PADDELETE:
        frVentana.PADDelete1();
        break;
      case PAD.PADQUERY:
        frVentana.PADQuery1();
        break;


    }
  }

}
