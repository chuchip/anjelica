package gnu.chu.utilidades;

import java.awt.MediaTracker;
import java.io.*;
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
   */
  public static String[] getIconos()
  {
    try
    {
      ResourceBundle param = ResourceBundle.getBundle("gnu.chu.anjelica.db",
          Locale.getDefault());

      String path = "";
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
      for (int i = 0; i < files.length; i++)
      {
        // Si es un directorio continua con el bucle
        if (files[i].isDirectory())
          continue;
        // Si no tiene la extension gif continua
        if (files[i].getName().indexOf(".gif") == -1)
          continue;

        lon++;
      }

      String[] icons = new String[lon];
      int x = 0;
      for (int i = 0; i < files.length; i++)
      {
        // Si es un directorio continua con el bucle
        if (files[i].isDirectory())
          continue;
        // Si no tiene la extension gif continua
        if (files[i].getName().indexOf(".gif") == -1)
          continue;

        icons[x] = files[i].getName();
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
   */
  public static ImageIcon getImageIcon(String icon)
  {
    if (icon == null || icon.trim().equals(""))
      return null;
    ImageIcon icono = null;
    try
    {

      String path = "";
      if (icon.indexOf("/iconos/") == -1)
      {
        path=getPathIcon();
        icon = icon.toLowerCase();
        if (icon.indexOf(".png") == -1 && icon.indexOf(".gif") == -1 && icon.indexOf(".jpg")==-1)
          icon += ".png";
      }
      else
      {
        path = System.getProperty("java.home");
      }

      icono = new ImageIcon(path + icon);

      if (icono.getImageLoadStatus() == MediaTracker.ERRORED)
        icono = new ImageIcon(System.getProperty("java.home") + "/iconos/" +
                              icon);
    }
    catch (Exception j)
    {
    }
    catch (Throwable j)
    {}

    return icono;
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
