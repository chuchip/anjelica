package gnu.chu.anjelica.ventas;

import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import gnu.chu.controles.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.util.*;
import javax.swing.SwingConstants;
import java.awt.event.*;
import javax.swing.event.*;
/**
 *
 * <p>Título: ayuVenPro </p>
 * <p>Descripción: Muestra un Historico de ventas para un producto y un cliente</p>
 * <p>LLamado por las clases conVenProd y Covezore</p>
 * <p>Copyright: Copyright (c) 2005
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
 * @version 1.0
 *
 */

public class ayuVenPro extends ventana
{
  String s;
  CPanel cPanel2 = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLabel pro_codiL = new CLabel();
  CLabel pro_nombL = new CLabel();
  CLabel cLabel2 = new CLabel();
  CLabel cli_nombL = new CLabel();
  CLabel cli_codiL = new CLabel();
  Cgrid jt = new Cgrid(9);
  DatosTabla dt = new DatosTabla();
  boolean swIns;
  double precio=0;
  public ayuVenPro()
  {
    this(false);
  }
  public ayuVenPro(boolean modInserta)
  {
    swIns=modInserta;
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
    this.addInternalFrameListener(new InternalFrameAdapter()
    {
      public void internalFrameClosing(InternalFrameEvent e)
      {
        precio=0;
        matar();
      }
    });
    if (swIns)
    {
      jt.tableView.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent m)
        {
          if (m.getClickCount() > 1 && jt.isVacio() == false)
          {
            precio = jt.getValorDec(7);
            matar();
          }
        }

      });

      jt.tableView.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == e.VK_INSERT && jt.isVacio() == false)
          {
            precio = jt.getValorDec(7);
            matar();
          }
        }
      });
    }
    this.setSize(new Dimension(509, 392));
    this.setTitle("Historico Ventas Producto/Cliente");
    this.setClosable(false);
    this.setIconifiable(false);
    cPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel2.setMaximumSize(new Dimension(100, 50));
    cPanel2.setMinimumSize(new Dimension(100, 50));
    cPanel2.setPreferredSize(new Dimension(100, 50));

    Vector v= new Vector();
    pro_codiL.setHorizontalAlignment(SwingConstants.RIGHT);
    pro_codiL.setHorizontalTextPosition(SwingConstants.LEADING);
    pro_codiL.setText("");
    cli_codiL.setHorizontalAlignment(SwingConstants.RIGHT);

    v.add("Fecha"); // 0
    v.add("Emp"); // 1
    v.add("Eje"); // 2
    v.add("Serie"); // 3
    v.add("Numero"); // 4
    v.add("Kilos"); // 5
    v.add("Unid."); // 6
    v.add("Precio"); //7
    v.add("Importe"); //8
    jt.setCabecera(v);
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setFormatoColumna(6, "---9");
    jt.setFormatoColumna(7, "---,--9.99");
    jt.setFormatoColumna(8, "--,---,--9.99");
    jt.setAlinearColumna(new int[] {0, 2,2,1,2, 2, 2, 2, 2});
    jt.setAnchoColumna(new int[]{80,30,40,30,50,72,40,72,87});
    jt.setAjustarGrid(true);
    cPanel2.setLayout(null);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(4, 7, 55, 17));
    pro_codiL.setBackground(Color.orange);
    pro_codiL.setForeground(Color.white);
//    pro_codiL.setDebugGraphicsOptions(0);
    pro_codiL.setOpaque(true);
    pro_codiL.setPreferredSize(new Dimension(42, 15));
    pro_codiL.setBounds(new Rectangle(64, 5, 67, 18));
    pro_nombL.setBounds(new Rectangle(135, 5, 281, 18));
    pro_nombL.setPreferredSize(new Dimension(42, 15));
    pro_nombL.setText("");
    pro_nombL.setOpaque(true);

    pro_nombL.setForeground(Color.white);
    pro_nombL.setBackground(Color.orange);
    cLabel2.setText("Cliente");
    cLabel2.setBounds(new Rectangle(7, 27, 55, 17));
    cli_nombL.setBackground(Color.orange);
    cli_nombL.setForeground(Color.white);

    cli_nombL.setOpaque(true);
    cli_nombL.setText("");
    cli_nombL.setPreferredSize(new Dimension(42, 15));
    cli_nombL.setBounds(new Rectangle(136, 28, 281, 18));
    cli_codiL.setBounds(new Rectangle(65, 28, 67, 18));
    cli_codiL.setText("");
    cli_codiL.setPreferredSize(new Dimension(42, 15));
    cli_codiL.setOpaque(true);
    cli_codiL.setForeground(Color.white);
    cli_codiL.setBackground(Color.orange);

    jt.setBounds(new Rectangle(4, 5, 423, 260));
    this.getContentPane().add(jt, BorderLayout.CENTER);

    this.getContentPane().add(cPanel2, BorderLayout.NORTH);
     statusBar = new StatusBar(this);
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    cPanel2.add(cLabel1, null);
    cPanel2.add(pro_codiL, null);
    cPanel2.add(pro_nombL, null);
    cPanel2.add(cli_nombL, null);
    cPanel2.add(cli_codiL, null);
    cPanel2.add(cLabel2, null);
  }

  public void cargaDatos(conexion ct,String cliCodi,String cliNomb,String proCodi,String proNomb,
                         EntornoUsuario eu) throws Exception
  {
    dt.setConexion(ct);
    cli_codiL.setText(Formatear.format(cliCodi,"####9"));
    pro_codiL.setText(Formatear.format(proCodi,"####9"));
    cli_nombL.setText(cliNomb);
    pro_nombL.setText(proNomb);
    s="select  c.avc_fecalb,l.emp_codi,l.avc_ano,l.avc_serie,l.avc_nume,"+
        " sum(l.avl_canti) as avl_canti,sum(l.avl_unid) as avl_unid, "+
        " l.avl_prven, sum(l.avl_prven*l.avl_canti) as avl_impor from v_albavec c,v_albavel l where  "+
        " c.cli_codi = "+cliCodi+
        " AND l.avc_ano = c.avc_ano " +
        " and l.emp_Codi = c.emp_codi" +
        " and  l.avc_Serie = c.avc_serie" +
        " and l.avc_nume = c.avc_nume " +
        " and l.pro_codi = "+proCodi +
        (eu.isRootAV()?"":" and c.div_codi > 0 ")+
        " group by c.avc_fecalb,l.emp_codi,l.avc_ano,l.avc_serie,l.avc_nume,l.avl_prven "+
        " order by c.avc_fecalb desc";
    jt.removeAllDatos();
    if (! dt.select(s))
    {
      mensajeErr("Sin Ventas de este producto para este cliente");
      return;
    }
    jt.setDatos(dt);
    javax.swing.SwingUtilities.invokeLater(new Thread()
    {
      public void run()
      {
        jt.requestFocusInicio();
      }
    });
    this.setSelected(true);
    jt.requestFocusInicio();
    this.setEnabled(true);
    statusBar.setEnabled(true);
  }

}
