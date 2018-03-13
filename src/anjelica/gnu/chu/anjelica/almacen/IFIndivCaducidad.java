package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Titulo: IFIndivCaducidad </p>
 * <p>Descripción: JDialog para mostrar individuos con fecha caducidad problematica</p>
 *<p> Es utilizada en pdalbara</p>
 * <p>Copyright: Copyright (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN
 * NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 *
 */ 
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.controles.Cgrid;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JDialog;


public class IFIndivCaducidad
{    
    JDialog frame;
    ventana papa; 
    PIndivCaduc panel; 
    int swAceptado=CANCELAR;
    public final static int CANCELAR=0;
    public final static int ACEPTAR=1;
    Cgrid jt;
    /**
     * Creates new form IFStockPart
     * @param padre
     */
    public IFIndivCaducidad(ventana padre) {
         papa=padre;
         frame = new JDialog((Frame) null, true);
         
         frame.setSize(new Dimension(650, 400));
        
         frame.setTitle("Individuos con Caducidad");
       
//        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        
        //setVisibleCabeceraVentana(false);
        
        panel=new PIndivCaduc();
        activarEventos();
        frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        frame.getContentPane().add(panel,BorderLayout.CENTER);
        jt=panel.getGrid();
    }
    void activarEventos()
    {
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                swAceptado = CANCELAR;
                frame.setVisible(false);
            }
        });

        panel.getBotonAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    swAceptado=ACEPTAR;
                    matar();
            }
        });
        panel.getBotonCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matar();
            }
        });
    }
    /**
     * Establece que hay un error en fechas caducidad. 
     * No habilitara el boton de acpeptar
     * @param error 
     */
    public void setError(boolean error)
    {
       panel.getBotonCancelar().setEnabled(!error);
       if (error)
       {
           panel.setTextoAviso("Articulos con Fecha Caducidad ERRONEA");
           panel.setTextoAviso2("CORRIJALA");
       }
    }
     public int getResultado()
    {
      return swAceptado;
     }
     void matar()
     {
      frame.setVisible(false);

     }
    public void iniciar(ArrayList<DatIndiv> listIndiv,DatosTabla dtStat) throws SQLException
    {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height)
          frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
          frameSize.width = screenSize.width;
        frame.setLocation((screenSize.width - frameSize.width) / 2, (int)papa.getLocation().y+80);
        swAceptado=CANCELAR;  
     
        jt.removeAllDatos();
        for (DatIndiv dtInd1 : listIndiv)
        {          
            ArrayList v=new ArrayList();
            v.add(dtInd1.getProducto()); // 0
            v.add(MantArticulos.getNombProd( dtInd1.getProducto(),dtStat)); // 1
            v.add(dtInd1.getEjercLot()); // 2
            v.add( dtInd1.getSerie()); // 3
            v.add(dtInd1.getLote()); //4
            v.add(dtInd1.getNumind()); // 5
            v.add(dtInd1.getCanti()); // 6
            v.add(dtInd1.getFechaCaducidad()); // 7
            v.add(dtInd1.getAuxiliar()); // 8
            jt.addLinea(v);
        }
        jt.requestFocusInicio();
        frame.setVisible(true);
//        mensajeErr("Datos individuos mostrados");
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
 
}
