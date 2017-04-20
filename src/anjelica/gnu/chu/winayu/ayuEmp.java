package gnu.chu.winayu;


import gnu.chu.camposdb.empPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.util.ArrayList;
/**
 *
 * <p>Título: ayuEmp</p>
 * <p>Descripcion: Pantalla de Ayuda de Empresas.
* Muestra un grid con las empresas a las que tiene acceso cada usuario
* </p>
 * <p>Copyright: Copyright (c) 2005-2010
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


public class ayuEmp extends ventana
{
  Vector datos = new Vector();
  BorderLayout bLay1 = new BorderLayout();
  String strSql = "";
  private int emp_codiT=0;;
  private String emp_nombT = "";
  private boolean consulta = false;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(2);
  CButton Belegir = new CButton(Iconos.getImageIcon("rotarup"));
  GridBagLayout gridBagLayout2 = new GridBagLayout();


  public ayuEmp(EntornoUsuario e)
  {
    setTitulo("Ayuda Empresas");
    eje = true;
    EU = e;

    try
    {
      jbInit();
    }
    catch (Exception k)
    {
      setErrorInit(true);
      return;
    }
  }


  public ayuEmp(EntornoUsuario e, DatosTabla dt)
  {
    setTitulo("Ayuda Empresas");
    EU = e;
    eje = false;
    if (dt != null)
      dtCon1 = dt;
    try
    {
      jbInit();
    }
    catch (Exception k)
    {
      fatalError("constructor: ", k);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    this.setTitle(getTitulo());
    this.setResizable(true);
    this.setClosable(false);
    this.setIconifiable(false);
    this.setSize(new Dimension(595, 481));

    statusBar = new StatusBar(this);

    if (dtCon1 == null)
      conecta();

    Pprinc.setLayout(gridBagLayout2);
    // Configurando el Grid.
    ArrayList cabecera = new ArrayList();
    cabecera.add("Codigo"); // 0 -- Codigo
    cabecera.add("Nombre"); //1 -- Nombre
    jt.setCabecera(cabecera);
    int i[] = {46, 283};
    jt.setAnchoColumna(i);
    int a[] =  {2, 0};
    jt.setAlinearColumna(a);
    jt.ajustar(true);
    jt.setNumRegCargar(100);
    jt.setAjustarGrid(true);
    jt.setConfigurar("gnu.chu.winayu.ayuEmp", EU, dtCon1);
    jt.setBackground(Color.orange);
    jt.setMaximumSize(new Dimension(455, 265));
    jt.setMinimumSize(new Dimension(455, 265));
    jt.setOpaque(true);
    jt.setPreferredSize(new Dimension(455, 265));
    jt.setNumRegCargar(25);

    Belegir.setEnabled(false);
    Pprinc.setDefButton(Belegir);
    Belegir.setMargin(new Insets(0, 0, 0, 0));
    Belegir.setToolTipText("Elegir el Registro Activo");

    jt.tableView.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent m)
      {
        if (m.getClickCount() > 1)
        {
          Belegir_actionPerformed();
        }
      }
    });
    jt.tableView.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if ((e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER))
          Belegir_actionPerformed();
      }
    });
    Belegir.setToolTipText("Elegir el Registro Activo");

    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 1, 0, 5),
                                          0, 0));
    Belegir.setBounds(200, 0, 25, 22);
    jt.panelBuscar.add(Belegir, null); //, new XYConstraints(200,0, 25, 22));
  }

    @Override
  public void iniciarVentana() throws Exception
  {
   jt.removeAllDatos();
    String s="SELECT emp_codi,emp_nomb FROM v_empresa WHERE emp_codi IN ("+
        empPanel.getStringAccesos(dtCon1, EU.getUsuario())+")"+
        " order by 1 ";
    dtCon1.select(s);
    jt.setDatos(dtCon1);
    jt.requestFocusInicio();
  }

  public void reset()
  {
    consulta = false;
    emp_codiT = 0;
    emp_nombT = null;
  }
  void Belegir_actionPerformed()
  {
    if (jt.isVacio())
    {
      mensaje("No Encontrados Registros para Selecionar");
      jt.requestFocus();
      return;
    }

    // Editar Columna Activa.
    emp_codiT = jt.getValorInt(0);
    emp_nombT = jt.getValString(1);
    consulta = true;
    matar();
  }
  public boolean isConsulta()
  {
    return consulta;
  }
  public int getEmpCodi()
  {
    return emp_codiT;
  }
  public String getEmpNomb()
  {
    return emp_nombT;
  }

} // Final de Clase
