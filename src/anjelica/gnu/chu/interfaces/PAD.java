/**
* @author Jesus J. Puente
* @version 1.1
*/
package gnu.chu.interfaces;

import java.awt.event.*;

public interface PAD extends ejecutable
{
  public final static int NORMAL = 0;
  public final static int CONSULTA = 1;
  public final static int ALTA = 2;

// Rutinas a Ejecutarse en Background.
  public static final int ej_addnew = 1;
  public static final int ej_delete = 2;
  public static final int ej_edit = 3;
  public static final int ej_query = 4;
  public static final int PADADDNEW = 5;
  public static final int PADEDIT = 6;
  public static final int PADDELETE = 7;
  public static final int PADQUERY = 8;

  // Funciones a Implementar
  public void PADPrimero();

  public void PADAnterior();

  public void PADSiguiente();

  public void PADUltimo();

  public void PADQuery();
  public void PADQuery1();

  public void ej_query();

  public void ej_query1();

  public void canc_query();

  public void PADEdit();
  public void PADEdit1();
  public void ej_edit();

  public void ej_edit1();

  public void canc_edit();

  public void PADAddNew();
  public void PADAddNew1();

  public void ej_addnew();

  public void ej_addnew1();

  public void canc_addnew();

  public void PADDelete();
  public void PADDelete1();
  public void ej_delete();

  public void ej_delete1();

  public void canc_delete();

  public void PADChose();

  public void activar(boolean b);

  // Implementados en VentanaPad
  public void ej_Bcancelar(ActionEvent e);

  public void ej_Baceptar(ActionEvent e);

  public void mensaje(String s, boolean sonido);

  public void salirEnabled(boolean b);
}
