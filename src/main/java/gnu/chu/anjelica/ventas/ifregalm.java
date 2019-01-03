package gnu.chu.anjelica.ventas;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.anjelica.almacen.paregalm;
/**
 *
 * <p>Titulo: ifregalm</p>
 * <p>Descripcion: Ventana para Crear Regularizacion de Almacenes.
 * Llamado desde el Mant. Albaran de Ventas (pdalbara)</p>
 * <p>Copyright: Copyright (c) 2005-2010
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class ifregalm  extends ventana
{
  int numRegIns=0;
  paregalm pRegAlm;
  ventana papa;
  CPanel Pinicial = new CPanel();
  CPanel cPanel1 = new CPanel();
  CButton Baceptar = new CButton("Aceptar F4", Iconos.getImageIcon("check"));
  CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));

  public ifregalm()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
    }

  }

  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(549, 310));
    this.setResizable(false);
    this.setIconifiable(false);
    this.setMaximizable(false);
    this.setTitle("Insercion Mvtos. Regularizacion");
    Pinicial.setLayout(null);
    statusBar = new StatusBar(this);
    pRegAlm = new paregalm();
    pRegAlm.setBorder(BorderFactory.createRaisedBevelBorder());
    pRegAlm.setBounds(new Rectangle(7, 2, 525, 174));
    Baceptar.setBounds(new Rectangle(19, 180, 129, 29));
    Bcancelar.setBounds(new Rectangle(390, 179, 129, 29));
    this.getContentPane().add(Pinicial, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pinicial.add(pRegAlm, null);
    Pinicial.add(Bcancelar, null);
    Pinicial.add(Baceptar, null);
    Pinicial.setDefButton(Baceptar);
  }

  public void iniciar(DatosTabla dtStat, DatosTabla dtCon1, DatosTabla dtAdd, ventana padre) throws
      Exception
  {
    papa = padre;
    EU=padre.EU;
    ctUp=dtAdd.getConexion();
    pRegAlm.iniciar(EU, dtStat, dtAdd, padre.vl, this,dtCon1);
    activarEventos();
  }
  private void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    Bcancelar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        matar();
      }
    });

  }
  public int getNumeroRegInsertado()
  {
      return numRegIns;
  }
  
  public void reset()
  {
      numRegIns=0;
      pRegAlm.addNew();
  }
  private void Baceptar_actionPerformed()
  {
    try {
      if (!pRegAlm.checkCampos())
        return;
      pRegAlm.setRegNume(0);
      numRegIns=pRegAlm.insRegistro();
      ctUp.commit();
      mensajeErr("Mvto. Regularizacion ... INSERTADO con numero: "+numRegIns);
      matar(false);
    } catch (Exception k)
    {
      Error("Error al Insertar Mvto. Regularizacion",k);
    }
  }
  public paregalm getPanelReg()
  {
    return pRegAlm;
  }

}
