package gnu.chu.Menu;

/**
* Contiene la estructura de la información que incluye la tabla de procesos
* @author Diego Cuesta
* @version 1.1
*/

public class InfoProgram {

    /**
    * Constructor de la clase.
    * @param desc descripción del programa
    * @param nom nombre del fichero que contiene el ByteCode
    * @param maxc número máximo de copias simultáneas del programa
    * @param p peso relativo (de 0 a 99) del programa
    * @param t tipo de ejecución (DIRECTA, BACKGROUND, BATCH)
    * @param m verdadero si se puede matar al programa directamente
    * @param params vector de par�metros que se le pasan al constructor
    * @param tips vector de tipos de los par�metros pasados
    */
    public InfoProgram(String desc, String nom, int maxc, int p, int t, boolean m,
     Object[] params, Class[] tips) {
      this.descripcion = desc;
      this.nombre = nom;
      this.maxCopias = maxc;
      this.peso = p;
      this.tipoEjecucion = t;
      this.matable = m;
      this.parametros = params;
      this.tipos = tips;
    }

    /**
    * Ejecuci�n en Background
    */
    public static final int BACKGROUND = 0;

    /**
    * Ejecución directa
    */
    public static final int DIRECTA = 1;

    /**
    * Ejecución en Batch
    */
    public static final int BATCH = 2;

    private String descripcion;
    private String nombre;
    private int maxCopias;
    private int peso;
    private int tipoEjecucion;
    private int PID;
    private boolean matable;
    private Object[] parametros;
    private Class[] tipos;



    /**
    * Nombre largo del programa
    */
    public String getDescripcion() {
      return descripcion;
    }

    /**
    * Nombre de la clase que contiene el ByteCode
    */
    public String getNombre() {
      return nombre;
    }

    /**
    * M�ximo n�mero de copias que se admiten en ejecuci�n de ese programa
    */
    public int getMaxCopias() {
      return maxCopias;
    }


    /**
    * Peso del programa, utilizado para la gesti�n de recursos
    */
    public int getPeso() {
      return peso;
    }


    /**
    * Tipo de ejecuci�n
    */
    public int getTipoEjecucion() {
      return tipoEjecucion;
    }


    /**
    * Identificador de proceso
    */
    public int getPID() {
      return PID;
    }

    /**
    * Cambia el PID interno del programa
    */
    public void setPID(int pid) {
      this.PID = pid;
    }

    /**
    * El programa se puede matar
    */
    public boolean isMatable() {
      return matable;
    }

    /**
    * Lista de parámetros que se le pasan al programa
    */
    public Object[] getParametros() {
      return parametros;
    }

    /**
    * Lista con los tipos de los par�metros que se le pasan al programa
    */
    public Class[] getTipos() {
      return tipos;
    }

}
