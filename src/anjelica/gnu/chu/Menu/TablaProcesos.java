package gnu.chu.Menu;

import gnu.chu.controles.CInternalFrame;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import gnu.chu.interfaces.*;


/**
* Gestiona la tabla interna de procesos añadiendo funciones como el chequeo de integridad
* de la tabla interna y la posibilidad de eliminar los frames
* @author Diego Cuesta
* @version 2.0
*/
public class TablaProcesos {

/**
* Tablas para mantener la estructura de las ventanas y procesos
*/
private Hashtable tablaObjetos = new Hashtable();
private Hashtable tablaInfo = new Hashtable();
public int nProc=1;


/**
* Aade el thread o la ventana a la tabla de procesos del sistema, esta función se llamará
* únicamente desde otros programas sobre los que ya se haya realizado un chequeo para
* determinar su posibilidad de ejecución o como complemento de la llamada a actualización
* de tabla de programas del menú.
* @param nombre nombre del proceso (mnemónico)
* @param hijo proceso a ejecutar
* @param pid identificador de proceso de su padre
* @param clase nombre de la clase que contiene el ByteCode
*/
public synchronized void anadir(StringBuffer nombre, ejecutable hijo, StringBuffer pid, StringBuffer clase) {

  StringBuffer elem;
  boolean encontrado = false;
  Enumeration claves = tablaObjetos.keys();
//  while(claves.hasMoreElements()) {
//    elem = (StringBuffer)claves.nextElement();
//    if((elem.toString()).compareTo(pid.toString())==0)
//      encontrado = true;
//  }
//  if(encontrado)
//     pid = pid.append('1');
  InfoProgram info = new InfoProgram(nombre.toString(), clase.toString(),
                                     hijo.getMaxCopias(), hijo.getPeso(), 0,
                                     hijo.isMatable(), null, null);

  int iPID;
  Integer aux = new Integer(0);
  String s = pid.toString();
  aux = aux.valueOf(s);
  iPID = aux.intValue();
  info.setPID(iPID);
  hijo.setPID(iPID);
  tablaObjetos.put(pid, hijo);
  tablaInfo.put(pid, info);
  nProc++;
}

/**
* Elimina el thread o la ventana de la tabla de procesos, junto con todos
* los elementos que dependen de el.
* @param pid identificador del proceso a matar
* @return peso de los programas eliminados
*/
public synchronized int eliminar(StringBuffer pid) {
  Enumeration claves = tablaObjetos.keys();
  Enumeration clavesBorro;
  Vector clavesABorrar = new Vector();
  String pidString = pid.toString();
  StringBuffer elem;
  int peso = 0;
  // jur, jur, recorrido por la tabla buscando

  while(claves.hasMoreElements()) {
    elem = (StringBuffer)claves.nextElement();
    if((elem.toString()).startsWith(pidString)) {
      clavesABorrar.addElement(elem);
    }
  }
  // recorrido matando, cerrando, y borrando

  clavesBorro = clavesABorrar.elements();
  while(clavesBorro.hasMoreElements()) {
    elem = (StringBuffer)clavesBorro.nextElement();

    if(muerete((ejecutable)tablaObjetos.get(elem))) {
      ejecutable v = (ejecutable)tablaObjetos.get(elem);
      peso += v.getPeso();
      tablaObjetos.remove(elem);
      tablaInfo.remove(elem);
    }
  }
  return peso;

}

/**
 * Le da el foco al programa en cuestion
 *
 * @param pid
 */
public void ir(String pid)
    {
    CInternalFrame ej=null;
    StringBuffer elem=null;
    Enumeration claves = tablaObjetos.keys();
    while(claves.hasMoreElements()) {
        elem = (StringBuffer)claves.nextElement();
        if((elem.toString()).startsWith(pid)) {
         ej=(CInternalFrame) tablaObjetos.get(elem);
         break;
        }
    }
    ir (ej);
  }

  void ir(CInternalFrame ej)
  {
    if (ej==null)
        return;
    try {
//        ej.setSelected(true);
        if (ej.isIcon())
            ej.setIcon(false);
        ej.moveToFront();
        ej.show();
        ej.setSelected(true);
    } catch (Exception k)
    {    }
  }
/**
* Elimina el thread o la ventana de la tabla de procesos, junto con todos
* los elementos que dependen de el.
* @param pid identificador del proceso a matar
* @return peso de los programas eliminados
*/
public synchronized int matando(StringBuffer pid) {
  Enumeration claves = tablaObjetos.keys();
  Enumeration clavesBorro;
  Vector clavesABorrar = new Vector();
  String pidString = pid.toString();
  StringBuffer elem;
  int peso = 0;
  // jur, jur, recorrido por la tabla buscando

  while(claves.hasMoreElements()) {
    elem = (StringBuffer)claves.nextElement();
    if((elem.toString()).startsWith(pidString)) {
      clavesABorrar.addElement(elem);
    }
  }
  // recorrido matando, cerrando, y borrando

  clavesBorro = clavesABorrar.elements();
  while(clavesBorro.hasMoreElements()) {
    elem = (StringBuffer)clavesBorro.nextElement();
    if(cepillarse((ejecutable)tablaObjetos.get(elem))) {
      ejecutable v = (ejecutable)tablaObjetos.get(elem);
      peso += v.getPeso();
      tablaObjetos.remove(elem);
      tablaInfo.remove(elem);
    }
  }
  return peso;

}

ejecutable getProceso(String proceso)
{
    Iterator claves = tablaObjetos.values().iterator();
    Object elem;

  while(claves.hasNext()) {
            try {
                elem = claves.next();
                if ( Class.forName(proceso).isAssignableFrom(elem.getClass()))
                        return (ejecutable) elem;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TablaProcesos.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    return null;
}
private boolean cepillarse(ejecutable victima) {
  if(victima.isMatable()) {
    victima.setPID(-1);
    victima.matar();
    return true;
  }
  else {
    JOptionPane.showMessageDialog(null, "Ese programa no se muere", "Atención", JOptionPane.ERROR_MESSAGE);
    return false;
  }

}

private boolean muerete(ejecutable victima) {
    victima.setPID(-1);
    return true;
}

/**
* Extrae la información que está asociada con el proceso.
* @param pid clave de búsqueda
*/
public InfoProgram programaAsociado(StringBuffer pid) {
  InfoProgram info = (InfoProgram) tablaInfo.get(pid);
  return info;
}


/**
* Obtiene una enumeración de la información de todos los procesos
*/
public Enumeration getProgramInfo() {
  return tablaInfo.elements();
}
public int getNumerosElementos()
{
    return tablaInfo.size();
}
}

