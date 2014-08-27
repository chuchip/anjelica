package gnu.chu.utilidades;

import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
/**
 * <b>Funciones para obtener imagenes.</b>
 * <p>
 * @autor Angel J. Apellaniz
 * @version 1.0  -  05/01/2000
 */
public class Iconos
{
  /**
   * Retorna un Vector de Vectores para rellenar el grid del PadMenu
   */
  public static ArrayList getDatosGrid()
  {
    ArrayList v = new ArrayList();

    // Recoge los nombre de los Iconos
    String[] icons = getIconos();
    if (icons == null)
      return null;

    for (int i = 0; i < icons.length; i++)
    {
      ArrayList v1 = new ArrayList();
      v1.add(icons[i]);

      v.add(v1);
    }

    return v;
  }

  /**
   * Retorna un Array de String con los nombre de los Iconos
     * @return Array de Strings con todos los iconos.
   */
  public static String[] getIconos()
  {
    try
    {
      ResourceBundle param = ResourceBundle.getBundle("gnu.chu.anjelica.db",
          Locale.getDefault());

      String path;
      if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
        path = param.getString("GenPathIconWIN");
      else if (System.getProperty("os.name").indexOf("UNIX") != -1)
        path = param.getString("GenPathIconUNIX");
      else
        path = param.getString("GenPathIcon");

        // Recoge todos los Iconos si no hay iconos retorna un false
      File directorio = new File(path);
      File[] files = directorio.listFiles();

      if (files == null)
        return null;
      Arrays.sort(files);

      // Cuenta el numero de Iconos
      int lon = 0;
      for (File file : files)
      {
            // Si es un directorio continua con el bucle
            if (file.isDirectory())
                continue;
            // Si no tiene la extension gif continua
            if (file.getName().indexOf(".gif") == -1)
                continue;
            lon++;
      }

      String[] icons = new String[lon];
      int x = 0;
      for (File file : files)
      {
            // Si es un directorio continua con el bucle
            if (file.isDirectory())
                continue;
            // Si no tiene la extension gif continua
            if (file.getName().indexOf(".gif") == -1)
                continue;
            icons[x] = file.getName();
            x++;
      }
      return icons;
    }
    catch (Exception j)
    {}
    return null;
  }

  /**
   * Retorna el icono que se envia como parametro
     * @param icon
     * @return ImageIcon con el icono
   */
  public static ImageIcon getImageIcon(String icon)
  {
    if (icon == null || icon.trim().equals(""))
        return null;
    String iconMin=icon.toLowerCase();
            
    if ( ! iconMin.endsWith(".png")  && ! iconMin.endsWith(".gif")  && ! iconMin.endsWith(".jpg"))
          icon += ".png";

    URL ur=Iconos.class.getClass().getResource((iconMin.indexOf("gnu.chu")==-1?"/gnu/chu/icons/":"")+icon);
    if (ur!=null)
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(ur));      
    
    return new ImageIcon(Toolkit.getDefaultToolkit().getImage(Iconos.class.getClass().getResource("/gnu/chu/icons/break.png")));
  }
  
  public static String getPathIcon()
  {
    String raiz = System.getProperty("raiz");
    if (raiz == null)
      raiz = "gnu.chu";

    ResourceBundle param = ResourceBundle.getBundle(raiz+".config",
        Locale.getDefault());

    String path = "";
    try {
      if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
        path = param.getString("GenPathIconWIN");
      if  (System.getProperty("os.name").toUpperCase().indexOf("UNIX") != -1 ||
               System.getProperty("os.name").toUpperCase().indexOf("LINUX") != -1)
        path = param.getString("GenPathIconUNIX");
    } catch (Throwable  k)
    {

    }
    if (path==null)
      path="";
    if (path.equals(""))
      path = param.getString("GenPathIcon");
    return path;
  }
}
