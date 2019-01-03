

package gnu.chu.camposdb;

import gnu.chu.controles.CButton;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.winayu.VentDiscrCli;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLayeredPane;

/**
 *
 * <p>Titulo: DiscButton  </p>
 * <p>Descripción: CButton para llamar a la venta de discriminadores de clientes </p>
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia P�blica General de GNU según es publicada por
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
 * @author chuchiP
 * Created on 29-dic-2010, 10:11:29
 *
 */
public class DiscButton extends CButton {
    private CInternalFrame papa;
    private JLayeredPane vl;
    EntornoUsuario EU;
    DatosTabla dt;
    VentDiscrCli vd;
    
    public DiscButton()
    {
        this.setToolTipText("Selecionar discriminadores para Clientes");
        this.setMargin(new Insets(0,0,0,0));
        this.setIcon(Iconos.getImageIcon("filter"));
    }
    public void iniciar( DatosTabla dt,CInternalFrame intFrame,
                    JLayeredPane layPan, EntornoUsuario entUs)
    {
       this.dt=dt;
       papa=intFrame;
       vl=layPan;
       EU=entUs;
       activarEventos();
    }
    void activarEventos()
    {
        this.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentana();
            }
        });
    }
    /**
     * 
     * @param aliasCliente
     * @return
     */
    public String getCondWhere(String aliasCliente)
    {
        if (vd==null)
            return "";
       return vd.getCondWhere(aliasCliente);
    }
    public void matar() {

        if (vd != null) {
            vd.setVisible(false);
            vd.matar(false);
        }
    }
    @Override
    public void resetTexto()
    {
        vd.resetTexto();
    }
    void abrirVentana()
    {
     try
      {
        if (vd==null)
        {
          vd = new VentDiscrCli()
          {
                    @Override
            public void matar()
            {
               salirVentanaDisc(vd);
            }
          };
          vd.iniciar(dt,EU,papa);
         
          vl.add(vd, new Integer(1));
        }
        vd.statusBar.setEnabled(true);

        vd.setLocation(papa.getLocation().x+40,papa.getLocation().y+40);
        vd.setVisible(true);
        
        if (papa!=null)
        {
          papa.setEnabled(false);
          papa.setFoco(vd);
        }
        vd.requestFocus();
      }
      catch (Exception j)
      {
        SystemOut.print(j);
        if (papa != null)
          papa.setEnabled(true);
      }

  }

  void salirVentanaDisc(VentDiscrCli aycli)
  {
    
    this.requestFocus();
    vd.setVisible(false);

    if (papa != null)
    {
      papa.setEnabled(true);
      papa.toFront();
      try
      {
        papa.setSelected(true);
      }
      catch (Exception k)
      {}
      papa.setFoco(null);
      this.requestFocus();
    }
  }
}
