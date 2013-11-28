
package gnu.chu.interfaces;

import java.util.Locale;

public interface ejecutable
{
  public static final String NUEVO = "(nuevo)";
  public static final String VACIO = " ";
  public static final String ROOT = "gnu.chu";
  public static final Locale local = new Locale("es", "es", "");

  public void matar();

  /**
   * Devuelve el peso asociado al programa
   */
  public int getPeso();

  /**
   * Devuelve el máximo número de copias simultaneas en ejecución
   */
  public int getMaxCopias();

  /**
   * Devuelve el nombre del programa
   */
  public String getNombre();

  /**
   * Devuelve el identificador de proceso del programa
   */
  public int getPID();

  /**
   * Establece el nuevo PID del programa
   */
  public void setPID(int nuevoPID);

  /**
   * Cambia el nombre del programa
   */
  public void setNombre(String nuevoNombre);

  /**
   * Devuelve cierto si el programa se puede matar
   */
  public boolean isMatable();

  /**
   * @return true si hubo un error en el constructor.
   */
  public boolean getErrorInit();

  /**
   * Función que llamara el Menu, cuando añada las Ventanas al
   * LayeredPane.
   * <p>
   * Normalmente estará vacía.
   */
  public void iniciarVentana() throws Exception;
  public boolean isClosable();
  public void setClosable(boolean b);

}
