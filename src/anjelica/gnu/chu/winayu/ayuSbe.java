package gnu.chu.winayu;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
/**
 *
 * <p>T�tulo: ayuSbe</p>
 * <p>Descripcion: Pantalla de Ayuda de Empresas.
* Muestra un grid con las empresas a las que tiene acceso cada usuario
* </p>
 * <p>Copyright: Copyright (c) 2007
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */


public class ayuSbe extends ventana
{
  String sbeTipo;
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


  public ayuSbe(EntornoUsuario e)
  {
    setTitulo("Ayuda SubEmpresas");
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


  public ayuSbe(EntornoUsuario e, DatosTabla dt)
  {
    setTitulo("Ayuda SubEmpresas");
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
      return;
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
    Vector cabecera = new Vector();
    cabecera.addElement("Codigo"); // 0 -- Codigo
    cabecera.addElement("Nombre"); //1 -- Nombre
    jt.setCabecera(cabecera);
    int i[] = {46, 283};
    jt.setAnchoColumna(i);
    int a[] =  {2, 0};
    jt.setAlinearColumna(a);
    jt.setAjustarColumnas(true);
    jt.setNumRegCargar(100);
    jt.setAjustarGrid(true);
    jt.setConfigurar("gnu.chu.winayu.ayuSbe", EU, dtCon1);
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
        if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER)
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

  public void iniciarVentana(int empCodi,String sbeTipo) throws Exception
  {
    this.sbeTipo=sbeTipo;
    jt.removeAllDatos();
    String s="SELECT sbe_codi,sbe_nomb FROM subempresa" +
        " WHERE sbe_codi = "+EU.getLikeUsuario().getInt("sbe_codi")+
        " and emp_codi = "+empCodi+
        " and sbe_tipo = '"+sbeTipo+"'"+
        " union "+
        " select sbe_codi, sbe_nomb FROM subempresa "+
        " where emp_codi = "+empCodi+
        (EU.getLikeUsuario().getInt("sbe_codi")!=0?
        " and sbe_codi in (select sbe_codi from accususbe where usu_nomb = '"+EU.usuario+"'"+
        " and emp_codi = "+empCodi+ " )":"")+
        " and sbe_tipo = '"+sbeTipo+"'"+
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
