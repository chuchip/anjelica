package gnu.chu.Menu;

import gnu.chu.controles.*;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.EntornoUsuario;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Frame para Configurar el modo de comportamiento de los Listados
*
 * <p>Copyright: Copyright (c) 2007</p>
 * <p> Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * </p>
 * @author chuchiP
 * @version 1.0
*/

class frPrevisual extends CInternalFrame
{
  CPanel Pprinc = new CPanel();
  CCheckBox opPrevisual = new CCheckBox();
  CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
  public CButton Bcancelar = new CButton("Cancelar",
                                         Iconos.getImageIcon("cancel"));
  CCheckBox opDialogoPrint = new CCheckBox();

  EntornoUsuario EU;
  Principal padre;
  CCheckBox opSimuList = new CCheckBox();
  CCheckBox opGuardar = new CCheckBox();
  public frPrevisual(EntornoUsuario EU,Principal fr)
  {
    try
    {
      this.padre=fr;
      this.EU = EU;
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    this.setTitle("Configurar Listados");
    this.setSize(new Dimension(400, 100));
    Pprinc.setLayout(null);
    opPrevisual.setText("Previsualizar Listados");
    opDialogoPrint.setText("Mostrar Dialogo Impr.");
    opPrevisual.setBounds(new Rectangle(5, 3, 174, 20));
    opDialogoPrint.setBounds(new Rectangle(204, 2, 170, 20));
    Baceptar.setBounds(new Rectangle(42, 46, 117, 29));
    Baceptar.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed(e);
      }
    });
    Bcancelar.setBounds(new Rectangle(200, 46, 117, 29));
    Bcancelar.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bcancelar_actionPerformed(e);
      }
    });
    opSimuList.setSelected(EU.previsual);
    opSimuList.setBounds(new Rectangle(206, 23, 150, 20));
    opSimuList.setText("Simular Listados");
    opGuardar.setToolTipText("Mantener datos entre sesiones");
    opGuardar.setText("Guardar Datos");
    opGuardar.setBounds(new Rectangle(5, 23, 156, 18));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(opDialogoPrint, null);
    Pprinc.add(opPrevisual, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(opSimuList, null);
    Pprinc.add(opGuardar, null);
    opPrevisual.setSelected(EU.previsual);
    opDialogoPrint.setSelected(EU.dialogoPrint);
    opSimuList.setSelected(EU.getSimulaPrint());
  }

  void Baceptar_actionPerformed(ActionEvent e)
  {
    EU.previsual = opPrevisual.isSelected();
    EU.dialogoPrint = opDialogoPrint.isSelected();
    EU.setSimulaPrint(opSimuList.isSelected());
    try {
      if (opGuardar.isSelected())
      {
        padre.dt1.select("select * from usuarios WHERE usu_nomb = '" + EU.usuario+"'",true);
        padre.dt1.edit();
        padre.dt1.setDato("usu_diapri",EU.dialogoPrint?"S":"N");
        padre.dt1.setDato("usu_previ",EU.previsual?"S":"N");
        padre.dt1.update();
        padre.dt1.commit();
      }
    } catch (java.sql.SQLException k)
    {
      gnu.chu.utilidades.mensajes.mensajeUrgente("Error al Actualizar tabla Usuarios: \n"+k.getMessage());
    }
    this.dispose();
  }

  void Bcancelar_actionPerformed(ActionEvent e)
  {
    this.dispose();
  }
}

