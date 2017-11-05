package gnu.chu.anjelica.inventario;

import gnu.chu.controles.*;
import javax.swing.*;
import java.awt.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;

/**
 *  Panel con las fechas de los Diferentes Controles de Inventario
 *
 * <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class PfechaInv extends CPanel
{
  Cgrid jt;
  boolean iniciado=false;
  JInternalFrame iframe =null;
  CTextField cci_fecconE = new CTextField(java.sql.Types.DATE,"dd-MM-yyyy");
  CButton Bcons = new CButton(Iconos.getImageIcon("find"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  DatosTabla dt;
//  CInternalFrame intfr;
  JLayeredPane vl;
  EntornoUsuario eu;
  ventana padre;
  private int desplazaX=0;
  private int desplazaY=20;
  public PfechaInv()
  {
    jbInit();
  }

  private void jbInit()
  {
    cci_fecconE.setMaximumSize(new Dimension(76, 16));
    cci_fecconE.setMinimumSize(new Dimension(76, 16));
    cci_fecconE.setPreferredSize(new Dimension(76, 16));
    Bcons.setPreferredSize(new Dimension(16, 16));
    Bcons.setMinimumSize(new Dimension(16, 16));
    Bcons.setMaximumSize(new Dimension(16, 16));
    this.setLayout(gridBagLayout1);
    this.add(cci_fecconE, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                                                 0));
    this.add(Bcons, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 0, 0));
    Bcons.setFocusable(false);
  }
  public void iniciar(DatosTabla datTabla, ventana papa,
                     JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
 {
   padre = papa;
   dt = new DatosTabla(datTabla.getConexion());
   vl = layPan;
   eu = entUsu;

   activarEventos();
 }
 void activarEventos()
 {
   cci_fecconE.addKeyListener(new KeyAdapter()
  {
            @Override
    public void keyPressed(KeyEvent e)
    {
      procesaTecla(e);
    }
  });
  cci_fecconE.addFocusListener(new FocusAdapter()
  {
            @Override
    public void focusLost(FocusEvent e)
    {
      if (e.isTemporary() || iframe==null ||  ! iniciado || ! padre.isSelected())
        return;
      iframe.setVisible(false);
    }
  });

  Bcons.addActionListener(new ActionListener()
  {
            @Override
    public void actionPerformed(ActionEvent e)
    {
      if (isEnabled() && cci_fecconE.isEditable())
        consFecha();
    }
  });
//  intfr.addVetoableChangeListener(new VetoableChangeListener()
//  {
//    public void vetoableChange(PropertyChangeEvent evt)
//				throws PropertyVetoException
//    {
//      if (iframe==null)
//        return;
//      if (! iframe.isVisible() || ! iniciado)
//        return;
//      System.out.println("xx: "+evt.getPropertyName());
//      if (evt.getPropertyName().equals("selected"))
//      {
//        if (! new Boolean(evt.getNewValue().toString()).booleanValue())
//          iframe.setVisible(false);
//      }
//    }
//  });

  padre.addAncestorListener(new AncestorListener()
  {
            @Override
    public void ancestorAdded(AncestorEvent event)
    {}

            @Override
    public void ancestorRemoved(AncestorEvent event)
    {}


            @Override
    public void ancestorMoved(AncestorEvent event)
    {
      if (event.getID()==AncestorEvent.ANCESTOR_MOVED)
        ponPosicion();

    }

    });
    padre.addInternalFrameListener(new InternalFrameAdapter()
    {
            @Override
      public void internalFrameDeactivated(InternalFrameEvent e)
      {
        if (iframe != null && iniciado)
        {
          if (!iframe.isSelected())
            iframe.setVisible(false);
        }
      }
    });


 }
    @Override
 public synchronized void addFocusListener(FocusListener fl)
 {
    if (cci_fecconE==null)
        super.addFocusListener(fl);
    else
        cci_fecconE.addFocusListener(fl);
  }
 public void setDesplazaX(int desplazaX)
 {
     this.desplazaX=desplazaX;
 }
 public int getDesplazaX()
    {
     return desplazaX;
 }
  public void setDesplazaY(int desplazaY)
 {
     this.desplazaY=desplazaY;
 }
 public int getDesplazaY()
    {
     return desplazaY;
 }
 void ponPosicion()
 {
   if (iframe!=null && iframe.isVisible())
   {
//     iframe.setLocation(20,20);
//     System.out.println("Y:"+intfr.getBounds().y);
     int x=0,y=0;
     Component objeto=this;
     do
     {
         x+=objeto.getX();
         y+=objeto.getY();
         objeto=objeto.getParent();
         if (objeto instanceof ventana )
             break;
     } while (objeto != null);

     iframe.setLocation(padre.getLocation().x + x +this.desplazaX,
                        padre.getLocation().y + y + this.getHeight()+desplazaY);
   }
 }
 void procesaTecla(KeyEvent e)
 {
   if (e.getKeyCode() == KeyEvent.VK_F3 && isEditable())
    {
      consFecha();
    }

 }
 public void Error(String s,Throwable k)
 {
    padre.Error(s,k);
 }
 public DatosTabla getDatosTabla()
 {
     return dt;
 }
    @Override
 public void setText(String texto)
 {
     cci_fecconE.setText(texto);
 }
    @Override
 public String getText()
 {
     return cci_fecconE.getText();
 }
    @Override
 public boolean isEditable()
 {
   return cci_fecconE.isEditable();
 }

 void consFecha()
 {
   if (iframe == null)
     iniciar();
   
   iniciado=false;
   iframe.setVisible(true);
   try  {
     iframe.setSelected(true); } catch(Exception k){}
   ponPosicion();
   iniciado=true;
 }
 void iniciar()
 {
     iframe = new JInternalFrame();

     iframe.setSize(new Dimension(100, 100));
//     iframe.putClientProperty(
//             PlasticInternalFrameUI.IS_PALETTE,
//             Boolean.TRUE);
     ((BasicInternalFrameUI) iframe.getUI()).setNorthPane(null);

     vl.add(iframe,new Integer(1));
     iframe.setBorder(null);
     iframe.setBackground(Color.BLUE);
     iframe.addInternalFrameListener(new InternalFrameAdapter()
     {
                @Override
       public void internalFrameDeactivated(InternalFrameEvent e)
       {
         if (iniciado)
         {
           if (!padre.isSelected())
             iframe.setVisible(false);
         }
       }
     });
     jt = new Cgrid(1)
     {
                @Override
       public boolean procesaKeyEvent(KeyEvent ke)
       {
         if (ke.getID() == KeyEvent.KEY_PRESSED)
          {
            if (ke.getKeyCode()==KeyEvent.VK_ENTER)
              salir(true);
            if (ke.getKeyCode()==KeyEvent.VK_ESCAPE)
              salir(false);
          }
          return true;
       }
     };
     jt.setOrdenar(false);
     jt.setNumRegCargar(0);
     jt.setBuscarVisible(false);
     jt.tableView.setTableHeader(null);
     jt.getScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
     ArrayList v=new ArrayList();
     v.add("Fechas");
     jt.setCabecera(v);
     jt.setAlinearColumna(new int[]{1});
     cargaGrid();
    
     jt.addMouseListener(new MouseAdapter()
     {
                @Override
       public void mouseClicked(MouseEvent e)
       {
           salir(true);
       }
     });

     iframe.getContentPane().add(jt,BorderLayout.CENTER);
 }
 /**
  * Funcion  a machacar si queremos cargar otras fechas.
  */
 public void cargaGrid()
 {
      String s="SELECT DISTINCT(cci_feccon) as cci_feccon FROM coninvcab order by cci_feccon desc";
     try {
       dt.select(s);
       jt.setDatos(dt);
     } catch (Exception k)
     {
       Error("Error al buscar Fechas de Inventario",k);
     }
 }
 
 public void setDatos(DatosTabla dt)
 {
    jt.setDatos(dt);
 }
 void salir(boolean pasar)
 {
   iframe.setVisible(false);
   if (pasar)
     PfechaInv.this.setText(jt.getValString(0));
   try
   {
     padre.setSelected(true);
   }
   catch (Exception k)
   {}
   cci_fecconE.requestFocus();
 }
 public String getFecha()
 {
   return cci_fecconE.getFecha();
 }
 public java.util.Date getDate() throws ParseException
 {
    return cci_fecconE.getDate();
 }
 public boolean isNull()
 {
   return cci_fecconE.isNull();
 }
 
  public void setDate(java.util.Date fecha)
 {
   cci_fecconE.setDate(fecha);
 }
 public boolean getError()
 {
     return cci_fecconE.getError();
 }
 
 public String getFechaDB() throws ParseException
 {
     return cci_fecconE.getFechaDB();
 }
}
