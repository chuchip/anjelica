package gnu.chu.controles;

import com.toedter.calendar.JCalendar;
import gnu.chu.utilidades.Formatear;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;

/**
 *
 * <p>Título: CButtonCalendar </p>
 * <p>Descripción: Campo Tipo JButton que al pulsarlo invocara un JInternalFrame.
 * Se unira con un campo TextField con formato fecha<br>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 * @version 2.0
 */
public class CButtonCalendar extends CButton
{
    CTextField ct=null;
    JDialog ifr=new JDialog();
    JCalendar dch=new JCalendar(true);

    public void iniciar(CTextField textField )
    {
        ifr.setTitle("Calendario");
        ct=textField;
        ifr.setSize(new Dimension(250,250));
        ifr.setLocation(Formatear.getCentroLocation(ifr.getSize()));
        ifr.setLayout(new BorderLayout());
        
        dch.getDayChooser().setAlwaysFireDayProperty(true);
        ifr.getContentPane().add(dch);
        dch.getDayChooser().addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("day"))
                {
                    ifr.setVisible(false);
                    if (ct!=null)
                        ct.setDate(dch.getCalendar().getTime());
                }
            }
        });
        ifr.addWindowListener(new WindowAdapter()
        {
            @Override
             public void windowClosing(WindowEvent e)
             {
                ifr.setVisible(false);
             }
        });
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ifr.setVisible(true);
            }
        });
        

    }
    public void setTextField(CTextField textField)
    {
        ct=textField;
    }

    public CTextField getTextField()
    {
        return ct;
    }
}
