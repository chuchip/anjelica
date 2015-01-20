package gnu.chu.camposdb;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.*;
import gnu.chu.anjelica.pad.pdusua;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * <p>Título: sbePanel </p>
 * <p>Descripción: Campo para introducir la SubEmpresa. <br>
 *  Restringe el uso de las empresas segun los permisos en tablas</p>
 * <p>Copyright: Copyright (c) 2008-2015
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

public class sbePanel extends CPanel
{
 String tipo="C"; // Por defecto (C)lientes
 CTextField emp_codiE=null;
 DatosTabla dt;
 EntornoUsuario eu;
 JLayeredPane vl;
 CInternalFrame intfr;
 ayuSbe aySbe;
 CLabel sbe_nombL=null;

  CTextField sbe_codiE = new CTextField(Types.DECIMAL,"#9");
  CButton Bcons = new CButton(Iconos.getImageIcon("find"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public sbePanel()
  {
     try
     {
         jbInit();
     } catch (Throwable ex)
     {
         Logger.getLogger(sbePanel.class.getName()).log(Level.SEVERE, null, ex);
     }
  }

  private void jbInit() throws Throwable
  {
    this.setLayout(gridBagLayout1);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    Bcons.setFocusable(false);
    sbe_codiE.setAceptaComodines(false);
    this.add(sbe_codiE,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(Bcons,   new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(1, 0, 0, 0), 1, 1));

  }
  /**
   * Funcion a llamar antes de usar este Control.
   * Si esta subempresa depende de una empresa (como es normal) se debera llamar
   * a la funcion setFieldEmpCodi()
   * @param datTabla
   * @param intFrame
   * @param layPan
   * @param entUsu
   * @throws SQLException
   */
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
  {
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
    ponOrejas();
    if (entUsu.getLikeUsuario()==null)
      entUsu.setLikeUsuario(pdusua.getVLikeUsuario(entUsu.usuario,dt));
    setValorInt(entUsu.em_cod);
//      controla(false, true);
    setAceptaNulo(false);
  }

  public void setValorInt(int valor)
  {
    sbe_codiE.setValorInt(valor);
    if (! sbe_codiE.isQuery())
      actNombSbe();
  }
    @Override
  public void setText(String valor)
  {
    sbe_codiE.setText(valor);
    if (! sbe_codiE.isQuery())
      actNombSbe();
  }
    @Override
  public String getText()
  {
    return sbe_codiE.getText();
  }
  public int getValorInt()
  {
    return sbe_codiE.getValorInt();
  }
  public void setAceptaNulo(boolean aceptaNulo)
  {
    sbe_codiE.setAceptaNulo(aceptaNulo);
  }
  public boolean getAceptaNulo()
  {
    return sbe_codiE.getAceptaNulo();
  }
  void ponOrejas()
  {
    Bcons.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        consSubempresa();
      }
    });
    sbe_codiE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        try {
          controla(false);
        } catch (SQLException k)
        {
         Logger.getLogger(sbePanel.class.getName()).log(Level.SEVERE, null, k);
        }
      }
    });
    sbe_codiE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if ((e.getKeyCode() == KeyEvent.VK_F3 || (e.isControlDown() && e.getKeyChar()=='3')) && isEditable())
          consSubempresa();
      }
    });

  }

  public boolean controla() throws SQLException
  {
    return controla(true);
  }
  public void setTextLabel(String texto)
  {
    sbe_codiE.setToolTipText(texto);
    if (sbe_nombL!=null)
      sbe_nombL.setText(texto);
  }
  public void actNombSbe()
  {
    try {
      controla(false);
    } catch (SQLException k)
    {
        Logger.getLogger(sbePanel.class.getName()).log(Level.SEVERE, null, k);
    }
  }
  public void setTipo(String tipo)
  {
    this.tipo=tipo;
  }
  public String getTipo()
  {
    return this.tipo;
  }
  /**
   * Campo con la empresa.
   * @param empCodi
   */
  public void setFieldEmpCodi(CTextField empCodi)
  {
    this.emp_codiE=empCodi;
  }
  public int getEmpCodi()
  {
    if (emp_codiE==null)
      return eu.em_cod;
    else
      return emp_codiE.getValorInt();
  }

 
  public boolean controla(boolean reqFocus) throws SQLException
  {
    setTextLabel("");
    if (sbe_codiE.getValorInt()==0)
    {
      if ( ! getAceptaNulo() )
      {
        if (reqFocus)
          sbe_codiE.requestFocus();
        return false;
      }
      else
        return hasAllAccess();
    }
    if (eu==null)
      return true; // No se ha inicializado el control
    String s;

    eu.getLikeUsuario().getInt("sbe_codi");
    eu.lkEmpresa.getInt("emp_codi");

    if ( eu.getSbeCodi()!=0 &&  ( eu.getSbeCodi() != sbe_codiE.getValorInt()
         || eu.lkEmpresa.getInt("emp_codi") != getEmpCodi()) )
    {
      // Controlar acceso a Subempresas
      s = "select * from  accususbe where usu_nomb = '" + eu.usuario + "'" +
          " and (sbe_codi = " +  sbe_codiE.getValorInt() + " or sbe_codi = 0)"+
          " and emp_codi = "+getEmpCodi();

      if (!dt.select(s))
      {
        setTextLabel("Subempresa NO Encontrada o sin ACCESO");
        if (reqFocus)
          sbe_codiE.requestFocus();
        return false;
      }
    }
   
    
    if (! getSubEmpresa(dt,sbe_codiE.getValorInt(),getEmpCodi(),tipo))
    {
      setTextLabel("Subempresa NO encontrada o sin ACCESO");
      if (reqFocus)
          sbe_codiE.requestFocus();
      return false;
    }
    setTextLabel(dt.getString("sbe_nomb"));
    return true;
  }
  /**
   * Indica si en una subempresa de una empresa dada, es obligatorio meter
   * un pedido para cargar el albaran
   * @param dt
   * @param empCodi
   * @param sbeCodi
   * @return true si es obligatorio.
   * @throws SQLException 
   */
  public static boolean incPedidosAlb(DatosTabla dt,int empCodi,int sbeCodi) throws SQLException
  {
      if (!dt.select("SELECT sbe_albped FROM subempresa where sbe_codi = "+sbeCodi+
           " and emp_codi = "+empCodi))
          return false;
      return dt.getInt("sbe_albped")!=0;
  }
  public boolean getSubEmpresa(DatosTabla dt,int sbeCodi,int empCodi,String tipo) throws SQLException
  {
     return dt.select("SELECT * FROM subempresa where sbe_codi = "+sbeCodi+
            (empCodi==0?"":" and emp_codi = "+empCodi)+
            (tipo==null?"":" and sbe_tipo ='"+tipo+"'"));
  }
  public String getNombSubEmpresa(DatosTabla dt,int sbeCodi,int empCodi) throws SQLException
  {
      return getNombSubEmpresa(dt, sbeCodi, empCodi,tipo);
  }
  public String getNombSubEmpresa(DatosTabla dt,int sbeCodi,int empCodi,String tipo) throws SQLException
  {
     if (getSubEmpresa(dt,sbeCodi,empCodi,tipo))
         return dt.getString("sbe_nomb");
     else
         return null;
  }
  void consSubempresa()
  {
    try
    {
      if (aySbe == null)
      {
        aySbe = new ayuSbe(eu, dt)
        {
          @Override
          public void matar()
          {
            ej_consSbe(aySbe);
          }
        };
        vl.add(aySbe);
        aySbe.setLocation(25, 25);
      }
      aySbe.iniciarVentana(getEmpCodi(),getTipo());
      aySbe.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(aySbe);
      }
    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consSbe(ayuSbe aySbe)
  {
    if (aySbe.isConsulta())
    {
      sbe_codiE.setValorInt(aySbe.getEmpCodi());
      sbe_codiE.setToolTipText(aySbe.getEmpNomb());
    }
    sbe_codiE.requestFocus();
    aySbe.setVisible(false);

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
    sbe_codiE.setColumnaAlias(alias);
  }
    @Override
  public String getStrQuery()
  {
    return sbe_codiE.getStrQuery();
  }
    @Override
  public  Component getErrorConf()
  {
    if (eu.getSbeCodi()==0 || getAceptaNulo() || getQuery())
      return null; // Permito cualquier valor si el usuario tiene acceso a todas subempresas o esta puesto que acepte NULO.
    if (sbe_codiE.getErrorConf()!=null)
      return sbe_codiE.getErrorConf();
    try {
      if (!controla(false))
        return this;
    } catch (SQLException k)
    {
      return this;
    }
    return null;
  }
  public String getTextAnt()
  {
    return sbe_codiE.getTextAnt();
  }
  public void setValorDec(double valor)
  {
    sbe_codiE.setValorDec(valor);
    if (! sbe_codiE.isQuery())
      actNombSbe();
  }

  public boolean hasCambio()
  {
    return sbe_codiE.hasCambio();
  }
    @Override
  public void resetCambio()
  {
    sbe_codiE.resetCambio();
  }
    @Override
  public void requestFocus()
  {
    if (sbe_codiE!=null)
      sbe_codiE.requestFocus();
  }
    @Override
  public void addFocusListener(FocusListener f)
  {
    if (sbe_codiE!=null)
      sbe_codiE.addFocusListener(f);
  }
  /**
   * Establede la etiqueta (Clabel) donde se pondra el nombre de la Subempresa.
   * @param sbeNombL CLabel
   */
  public void setLabelSbe(CLabel sbeNombL)
  {
    sbe_nombL=sbeNombL;
  }

  public CLabel creaLabelSbe()
  {
    CLabel sbeNombL  = new CLabel();
    sbeNombL.setBackground(Color.orange);
    sbeNombL.setForeground(Color.black);
    sbeNombL.setOpaque(true);
    sbeNombL.setText("");
    sbeNombL.setFont(new java.awt.Font("Dialog", 0, 11));
    return sbeNombL;
  }
  public boolean hasAllAccess()  throws SQLException
  {
      return hasAllAccess(emp_codiE.getValorInt());
  }
  public boolean hasAllAccess(int empCodi)  throws SQLException
  {
      String s = "select * from  accususbe where usu_nomb = '" + eu.usuario+"'"+
                  " and emp_codi = "+empCodi+
                  " and sbe_codi = 0";
      return dt.select(s);
  }
}
