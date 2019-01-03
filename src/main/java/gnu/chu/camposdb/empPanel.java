package gnu.chu.camposdb;

import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.anjelica.pad.pdusua;
import gnu.chu.controles.*;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.winayu.ayuEmp;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;

/**
 *
 * <p>Título: empPanel </p>
 * <p>Descripción: Campo para introducir la empresa. <br>
 *  Restringe el uso de las empresas según los permisos en tablas</p>
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author chuchiP
 * @version 1.1
 *
 */

public class empPanel extends CPanel
{
 HashSet <Integer> listaEmp=null;
 private String empNomb=null;
 private boolean error=false;
 private ArrayList cambioListenerList = new ArrayList();
 DatosTabla dt;
 EntornoUsuario eu;
 JLayeredPane vl;
 CInternalFrame intfr;
 ayuEmp ayEmp;
 CLabel emp_nombL=null;
 String msgError=null;
 CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CButton Bcons = new CButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  public empPanel()
  {
   
     try
     {
         jbInit();
     } catch (Throwable ex)
     {
         Logger.getLogger(empPanel.class.getName()).log(Level.SEVERE, null, ex);
     }
   
  }

  private void jbInit() throws Throwable
  {
    this.setLayout(gridBagLayout1);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    Bcons.setFocusable(false);
    emp_codiE.setAceptaComodines(false);

    this.add(emp_codiE,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(Bcons,   new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(1, 0, 0, 0), 1, 1));

  }
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
  {
    Bcons.setIcon(Iconos.getImageIcon("find"));
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
    ponOrejas(); 

    setValorInt(entUsu.em_cod);
//      controla(false, true);
    setAceptaNulo(false);
  }
    @Override
  public void requestFocus() {
      if (emp_codiE!=null)
          emp_codiE.requestFocus();
  }
  public void setValorInt(int valor)
  {
    emp_codiE.setValorInt(valor);
    if (! emp_codiE.isQuery())
      actNombEmp();
  }
    @Override
  public void setText(String valor)
  {
    emp_codiE.setText(valor);
    if (! emp_codiE.isQuery())
      actNombEmp();
  }
    @Override
  public String getText()
  {
    return emp_codiE.getText();
  }
  public int getValorInt()
  {
    return emp_codiE.getValorInt();
  }
  public void setAceptaNulo(boolean aceptaNulo)
  {
    emp_codiE.setAceptaNulo(aceptaNulo);
  }
  public boolean getAceptaNulo()
  {
    return emp_codiE.getAceptaNulo();
  }
  public synchronized void addCambioListener(CambioListener cambioListener)
  {
    if (emp_codiE.isNull())
        return;
    emp_codiE.resetCambio();
    cambioListenerList.add(cambioListener);
  }

  public synchronized void removeCambioListener(CambioListener cambioListener)
  {
    cambioListenerList.remove(cambioListener);
  }
  /**
   * Procesa el evento de cambio. Cuando escucha al listener, el valor antiguo de
   * emp_codi esta en emp_codiE.getTextAnt();
   */
  protected void processCambio()
  {
    if (!emp_codiE.isEnabled() || ! emp_codiE.hasCambio() || cambioListenerList.isEmpty())
      return;
    emp_codiE.resetCambio();
    CambioEvent ev = new CambioEvent(this);
     for (Object cambioListenerList1 : cambioListenerList)
     {
         ((CambioListener) cambioListenerList1).cambio(ev);
     }
    emp_codiE.resetCambio();
  }
  void ponOrejas()
  {
    Bcons.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        consEmpresa();
      }
    });
    emp_codiE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusLost(FocusEvent e)
      {
          if (controla(false))
            processCambio();
      }
    });
    emp_codiE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3 && isEditable())
          consEmpresa();
        
      }
    });

  }
  /**
   * Controla si el valor del campo es valido.
   * En caso de que el campo no sea valido, realiza un requestFocus al campo.
   * @return true es valido
   */
  public boolean controla() 
  {
    return controla(true);
  }
  public void setTextLabel(String texto)
  {
    emp_codiE.setToolTipText(texto);
    empNomb=texto;
    if (emp_nombL!=null)
      emp_nombL.setText(texto);
  }
  public void actNombEmp()
  {
      controla(false);
  }
  /**
   * Controla si el valor del campo es valido.
   *
   * @param reqFocus true Realiza un requestFocus si no es valido
   * @return true es valido
   */
  public boolean controla(boolean reqFocus)
  {
    try {

        if (dt.getConexion().isClosed()) {
            return true;
        }
        msgError = "";
        error = false;
        setTextLabel("");
        if (emp_codiE.getValorInt() == 0) {
            if (!getAceptaNulo()) {
                if (reqFocus) {
                    emp_codiE.requestFocus();
                }
                msgError = "Introduzca una empresa";
                error = true;
                return false;
            } else
            {
                if (! hasAccesoTotal(dt,eu.usuario))
                {
                    if (reqFocus)
                        emp_codiE.requestFocus();

                    msgError = "Usuario NO tiene acceso a todas las empresas";
                    error = true;
                    return false;
                }
                return true;
            }
        }
        if (eu == null) {
            return true; // No se ha inicializado el control
        }
        String s;

        if (eu.lkEmpresa.getInt("emp_codi") != emp_codiE.getValorInt()) {
            // Controlar acceso a Empresas
            s = "select * from  accusuemp where usu_nomb = '" + eu.usuario
                    + "'" + " and (emp_codi = " + emp_codiE.getValorInt()
                    + " OR emp_codi = 0) ";
            if (!dt.select(s)) {
                msgError = "Empresa NO Encontrada o sin ACCESO";
                error = true;
                setTextLabel(msgError);
                if (reqFocus) {
                    emp_codiE.requestFocus();
                }
                return false;
            }
        }
        s = "SELECT * FROM v_empresa where emp_codi = " + emp_codiE.getValorInt();
        if (!dt.select(s)) {
            msgError = "Empresa NO encontrada o sin ACCESO";
            setTextLabel(msgError);
            if (reqFocus) {
                emp_codiE.requestFocus();
            }
            error = true;
            return false;
        }
        setTextLabel(dt.getString("emp_nomb"));
        
    } catch (SQLException k) {
        mensajes.mensajeUrgente("Error al buscar datos de empresa\n" + k.getMessage());
        return false;
    }
    return true;
  }
  /**
   * Devuelve string con las empresas a las q el usuario tiene acceso
   * El string se deberia usar en una clausula IN de la selecct.
   * @param dt Conexión a DB
   * @param usuario Usuario a comprobar
   * @return String con un formato parecido a "1,2,3"
   * @throws SQLException 
   */
  public static String getStringAccesos(DatosTabla dt, String usuario) throws SQLException
  {
      return getStringAccesos(dt, usuario,false);
  }
  /**
   * Devuelve string con las empresas a las q el usuario tiene acceso
   * El string se debería usar en una clausula IN de la selecct.
   * @param dt Conexión a DB
   * @param usuario Usuario a comprobar
   * @param allIsNull si es mandado true, en el caso de tener acceso a todas las empresas devuelve NULL.
   * @return String con un formato parecido a "1,2,3"
   * @throws SQLException 
   */
  public static String getStringAccesos(DatosTabla dt, String usuario,boolean allIsNull) throws SQLException
  {
     String s;
     if (hasAccesoTotal(dt,usuario))
     {
       if (allIsNull)
          return null;
       s="SELECT emp_codi FROM v_empresa  "; // Doy acceso a todas las empresas disponibles.
     }
     else
      s="SELECT emp_codi from accusuemp where usu_nomb = '" +usuario+"'";
    if (!dt.select(s))
        return ""+pdusua.getEmpresa(dt, usuario); // Sin accesos excepto a la suya.
    s="";
    do
    {
        s+=dt.getInt("emp_codi")+",";
    } while (dt.next());
    s+=pdusua.getEmpresa(dt, usuario);
    return s;
  }
  
  public static boolean hasAccesoTotal(DatosTabla dt,String usuario) throws SQLException
  {
    return dt.select( "select * from  accusuemp where usu_nomb = '" + usuario+"'"
                    + " and emp_codi = 0");
  }
  /**
   * Devuelve si tiene acceso a una empresa dada
   * @param empCodi
   * @return  True si tiene acceso
   */
  public boolean hasAcceso(int empCodi) throws SQLException
  {
      if (listaEmp==null)
          cargaListaEmp();
      if (listaEmp.isEmpty())
          return true;
      return listaEmp.contains(empCodi); 
  }
  /**
   * Carga HashSet con la lista de Empresas disponibles para un usuario.
   * @throws SQLException 
   */
  private void cargaListaEmp() throws SQLException
  {   
      listaEmp=new HashSet();
      if (hasAccesoTotal(dt, eu.usuario))
          return; // La dejo vacia, lo cual significa q tiene acceso a todo
      listaEmp.add(eu.em_cod); // La empresa del usuario es la mandada. 
      String s="SELECT emp_codi from accusuemp where usu_nomb = '" +eu.usuario+"'";
      if (!dt.select(s))
        return;
      do
      {
         listaEmp.add(dt.getInt("emp_codi"));
      } while (dt.next());
  }
  /**
   * Indica si una empresa existe y tiene acceso el usuario
   * @param dt
   * @param empCodi
   * @param usuario
   * @return 1 Todo OK. -1 Empresa no existe. 0 Sin acceso
   * @throws SQLException 
   */
  public static int isValida(DatosTabla dt, int empCodi,String usuario) throws SQLException
  {
      if (! pdempresa.checkEmpresa(dt,empCodi))
          return -1;
      if (hasAccesoTotal(dt,usuario))
          return 1;
      if (pdusua.getEmpresa(dt,usuario)==empCodi)
          return 1; // La empresa del usuario es la mandada.
      return dt.select("select * from  accusuemp where usu_nomb = '" + usuario+"'"
                    + " and emp_codi = "+empCodi)?1:0;
  }
  public String getEmpNomb()
  {
      return empNomb;
  }
  /**
   * Indica si ha habido un error en la ultima llamada a controla
   *
   * @return true si ha habido error.
   */
  public boolean getError()
  {
      return error;
  }
  public void setError(boolean error)
  {
      this.error=error;
  }
  void consEmpresa()
  {
    try
    {
      if (ayEmp == null)
      {
        ayEmp = new ayuEmp(eu, dt)
        {
          @Override
          public void matar()
          {
            ej_consEmp(ayEmp);
          }
        };
        vl.add(ayEmp);
        ayEmp.setLocation(25, 25);
        ayEmp.iniciarVentana();
      }
      ayEmp.reset();
      ayEmp.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(ayEmp);
      }


    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consEmp(ayuEmp ayEmp)
  {
    if (ayEmp.isConsulta())
    {
      emp_codiE.setValorInt(ayEmp.getEmpCodi());
      emp_codiE.setToolTipText(ayEmp.getEmpNomb());
    }
    emp_codiE.requestFocus();
    ayEmp.setVisible(false);

    if (intfr != null)
    {
      intfr.setEnabled(true);
      intfr.toFront();
      try
      {
        intfr.setSelected(true);
      }
      catch (Exception k)
      {}
      intfr.setFoco(null);
      this.requestFocus();
    }
  }
  public void setColumnaAlias(String alias)
  {
    emp_codiE.setColumnaAlias(alias);
  }
    @Override
  public String getColumnaAlias()
  {
    return emp_codiE.getColumnaAlias();
  }
  
    @Override
  public String getStrQuery() 
  {
    if (emp_codiE.getValorInt()!=0)
        return emp_codiE.getColumnaAlias()+" = "+emp_codiE.getValorInt();
    String strQuery="";
    try
    {
        String s;
        if (hasAllAccess())
            return " 1 = 1 "; // Tiene acceso a todas las empresas
      
    strQuery=emp_codiE.getColumnaAlias()+
         " in ("+eu.lkEmpresa.getInt("emp_codi")+",";
    s="select emp_codi from  accusuemp where usu_nomb = '" + eu.usuario+"'";
    if (dt.select(s))
    {
      do
      {
        strQuery+=dt.getInt("emp_codi")+",";
      } while (dt.next());
    }
    strQuery=strQuery.substring(0,strQuery.length()-1)+")";
    } catch (SQLException k)
    {
        return " error en strquery de empPanel "+k.getMessage();
    }
    return strQuery;
  }
 @Override
  public  Component getErrorConf()
  {
    if (emp_codiE.getErrorConf()!=null)
      return emp_codiE.getErrorConf();
    if (!controla(false))
      return this;
    return null;
  }
  public String getTextAnt()
  {
    return emp_codiE.getTextAnt();
  }
  public void setValorDec(double valor)
  {
    emp_codiE.setValorDec(valor);
    if (! emp_codiE.isQuery())
      actNombEmp();
  }

    @Override
  public boolean hasCambio()
  {
    return emp_codiE.hasCambio();
  }
    @Override
  public void resetCambio()
  {
    emp_codiE.resetCambio();
  }
    @Override
  public void addFocusListener(FocusListener f)
  {
    if (emp_codiE!=null)
      emp_codiE.addFocusListener(f);
  }
  /**
   * Establede la etiqueta (Clabel) donde se pondra el nombre de la empresa.
   * @param empNombL CLabel
   */
  public void setLabelEmp(CLabel empNombL)
  {
    emp_nombL=empNombL;
  }

  public CLabel creaLabelEmp()
  {
    CLabel empNombL = new CLabel();
    empNombL.setBackground(Color.orange);
    empNombL.setForeground(Color.black);
    empNombL.setOpaque(true);
    empNombL.setText("");
    empNombL.setFont(new java.awt.Font("Dialog", 0, 11));
    return empNombL;
  }

  public CTextField getTextField()
  {
    return emp_codiE;
  }
  public String getMsgError()
  {
      return msgError;
  }
  /**
   * Comprueba si el usuario tiene acceso a TODAS las empresas
   * @return
   * @throws SQLException
   */
  private boolean hasAllAccess()  throws SQLException
  {
      return hasAccesoTotal(dt,eu.usuario);
  }
}
