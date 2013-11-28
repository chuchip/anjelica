package gnu.chu.anjelica.despiece;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java. awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;

/**
 *
 * <p>Titutulo: datraza </p>
 * <p>Descripcion: Ventana para Busca datos de trazabilidad.
 * Es llamada desde pdtactil </p>
 * <p>Copyright: Copyright (c) 2005-2010
 *
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 */

public class datTraza  extends ventana
{
  datTraPanel PdatTra = new datTraPanel();
  utildesp utDesp=null;
  CPanel Pprinc = new CPanel();
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  public datTraza() throws Exception
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }

  }
  private void jbInit() throws Exception
  {
    this.setTitle("Datos Trazabilidad");
    this.setSize(new Dimension(469, 236));
    this.setResizable(false);
    this.setMaximizable(false);
    this.setIconifiable(false);
    this.setClosable(false);

    statusBar=new StatusBar(this);
    Pprinc.setLayout(null);
    Pprinc.setMaximumSize(new Dimension(32767, 32767));

    Baceptar.setBounds(new Rectangle(65, 127, 130, 35));
    Bcancelar.setBounds(new Rectangle(256, 127, 134, 35));
    PdatTra.setBorder(BorderFactory.createRaisedBevelBorder());
    PdatTra.setBounds(new Rectangle(1, 1, 455, 118));
    Pprinc.add(PdatTra,null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    activarEventos();
  }
  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    Bcancelar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bcancelar_actionPerformed();
      }
    });
  }

  public void setUtilDesp(utildesp utDes)
  {
    this.utDesp=utDes;
    PdatTra.nacidoE.setText(utDesp.nacidoE);
    PdatTra.cebadoE.setText(utDesp.cebadoE);
    PdatTra.sacrificadoE.setText(utDesp.sacrificadoE);
    PdatTra.despiezadoE.setText(utDesp.despiezadoE);
    PdatTra.ntrazaE.setText(utDesp.ntrazaE);
    PdatTra.fecrecepE.setText(utDesp.fecrecepE);
    PdatTra.conservarE.setText(utDesp.conservarE);

  }
  public utildesp getUtilDesp()
  {
    return utDesp;
  }

  void Baceptar_actionPerformed()
  {
   utDesp.nacidoE=PdatTra.nacidoE.getText();
   utDesp.cebadoE=PdatTra.cebadoE.getText();
   utDesp.sacrificadoE=PdatTra.sacrificadoE.getText();
   utDesp.despiezadoE=PdatTra.despiezadoE.getText();
   utDesp.ntrazaE=PdatTra.ntrazaE.getText();
   utDesp.fecrecepE=PdatTra.fecrecepE.getText();
   utDesp.conservarE=PdatTra.conservarE.getText();
   matar(false);
  }

  void Bcancelar_actionPerformed()
  {
    matar(false);
  }
}
