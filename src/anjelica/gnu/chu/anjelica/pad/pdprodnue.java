package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import javax.swing.event.*;
import java.awt.event.*;
//import com.borland.jbcl.layout.*;

public class pdprodnue extends ventanaPad implements PAD
{
  CPanel Pprinc = new CPanel();
  CPanel cPanel1 = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField pro_codiE = new CTextField();
  Cgrid jt = new Cgrid(5);
  CLabel cLabel2 = new CLabel();
  CTextField pro_nombE = new CTextField();
  CButton Bbuscar = new CButton("Buscar",Iconos.getImageIcon("find"));
//  CButton Baceptar = new CButton(Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("cancelar");

  String s;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
    public pdprodnue(EntornoUsuario eu, Principal p)
    {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("PAD Productos Nuevos (V 1.0)");

      try
      {
        if (jf.gestor.apuntar(this))
          jbInit();
        else
          setErrorInit(true);
      }
      catch (Exception e)
      {
        ErrorInit(e);
      }
    }

    public pdprodnue(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("PAD Productos Nuevos (V 1.0)");
      eje = false;

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
     iniciar(482, 493);
      strSql="SELECT distinct(pro_codi) FROM refproeq WHERE emp_codi = "+EU.em_cod;


      nav=new navegador(this,dtCons,false,navegador.NORMAL);
    Pprinc.setLayout(gridBagLayout1);
    cPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel1.setMaximumSize(new Dimension(375, 64));
    cPanel1.setMinimumSize(new Dimension(375, 64));
    cPanel1.setPreferredSize(new Dimension(375, 64));
    cPanel1.setDefButton(Bbuscar);
    cPanel1.setLayout(null);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(4, 4, 60, 16));
    pro_codiE.setBounds(new Rectangle(62, 4, 95, 18));
    jt.setDefButton(Baceptar);
    cLabel2.setText("Nombre");
    cLabel2.setBounds(new Rectangle(8, 23, 49, 18));
    pro_nombE.setBounds(new Rectangle(60, 23, 307, 18));
    Bbuscar.setBounds(new Rectangle(128, 43, 115, 19));
    Bbuscar.setToolTipText("");
    Vector cabecera = new Vector();
    cabecera.addElement("Codigo"); // 0 -- Codigo
    cabecera.addElement("Nombre"); //1 -- Nombre
    cabecera.addElement("Inc"); //2  -- Incluir
    cabecera.addElement("Familia"); // 3 -- Familia
    cabecera.addElement("Descrip"); // 4 -- Desc. Familia
    jt.setCabecera(cabecera);
    int i []= {46,283,49,54};
    jt.setAnchoColumna(i);
    jt.setAlinearColumna(new int[]{1,0,1,1,0});
    jt.setAjustarGrid(true);
    jt.setFormatoColumna(2,"BSN");
    jt.resetRenderer(2);
    jt.setConfigurar("gnu.chu.anjelica.pad.pdprodnue",EU,dtCon1);
    Iniciar(this);
    Baceptar.setMaximumSize(new Dimension(117, 25));
    Baceptar.setMinimumSize(new Dimension(117, 25));
    Baceptar.setPreferredSize(new Dimension(117, 25));
    Bcancelar.setMaximumSize(new Dimension(117, 25));
    Bcancelar.setMinimumSize(new Dimension(117, 25));
    Bcancelar.setPreferredSize(new Dimension(117, 25));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 3, 0, 3), 0, 0));
    Pprinc.add(cPanel1,   new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    cPanel1.add(cLabel1, null);
    cPanel1.add(pro_codiE, null);
    cPanel1.add(pro_nombE, null);
    cPanel1.add(cLabel2, null);
    cPanel1.add(Bbuscar, null);
    Pprinc.add(Baceptar,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 60, 3, 0), 0, 0));
    Pprinc.add(Bcancelar,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 60, 3, 84), 0, 0));
    }

    public void iniciarVentana() throws Exception
    {
      pro_nombE.setColumnaAlias("pro_nomb");
      pro_nombE.setQuery(true);
      activarEventos();
    }

    void activarEventos()
    {
//      Baceptar.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          baceptar_actionPerformed();
//        }
//      });
      Bbuscar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
         Bbuscar_actionPerformed();
        }
      });
      jt.tableView.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (jt.isVacio() || jt.isEnabled()==false)
            return;
          if (jt.getSelectedColumn()!=2)
            return;
          jt.setValor(jt.getValBoolean(2)?"N":"S");
        }

      });
    }

    void Bbuscar_actionPerformed()
    {
      String s=pro_nombE.getStrQuery();
      strSql="select pro_codi,pro_nomb,'N' AS cod,fam_codi,fpr_nomb FROM v_articulo a,v_famipro f "+
          " where a.fam_codi=f.fpr_codi ";
      if (! s.trim().equals(""))
        strSql+=" and "+s;
      strSql+=" order by pro_codi";
      try
      {
        if (dtCon1.select(strSql, false) == false)
        {
          mensaje(" -- NO ENCONTRADOS Productos con esas Condiones -- ");
          return;
        }
        jt.setDatos(dtCon1);
        Baceptar.setEnabled(true);
        jt.setEnabled(true);
        jt.requestFocusInicio();
      }
      catch (Throwable k)
      {
        fatalError("Error al Buscar Productos: ", dtCon1.SqlException);
        return;
      }
    }

    void guardaDatos()
    {
      String s;
      try {
        s="delete from refproeq WHERE pro_codi = '"+pro_codiE.getText()+"'";
        stUp.executeUpdate(s);
        int nRow = jt.getRowCount();
        for (int n = 0; n < nRow; n++)
        {
          if (! jt.getValBoolean(n,2))
            continue;
          s = "INSERT INTO refproeq VALUES(" + EU.em_cod + "," +
              "'" + pro_codiE.getText() + "'," +
              jt.getValInt(n, 0) + ")";
          stUp.executeUpdate(s);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Referencias",k);
        return;
      }
    }

    void verDatos()
    {
      try
      {
        pro_codiE.setText(dtCons.getString("pro_codi"));
        s = "SELECT a.pro_codi,pro_nomb,'N' AS cod,fam_codi,fpr_nomb "+
            " FROM refproeq r,v_articulo a,v_famipro f "+
            " WHERE r.pro_codi = '" + pro_codiE.getText() + "'"+
            " and  a.fam_codi=f.fpr_codi "+
            " and a.pro_codi = r.pro_nume ";
        dtCon1.select(s);
        jt.setDatos(dtCon1);
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
        return;
      }
    }
    public void activar(boolean act)
    {
      jt.setEnabled(act);
      pro_codiE.setEnabled(act);
      pro_nombE.setEnabled(act);
      Bbuscar.setEnabled(act);
      Baceptar.setEnabled(act);
      Bcancelar.setEnabled(act);
    }
  public void PADPrimero() { verDatos();
  }
  public void PADAnterior() { verDatos();
  }
  public void PADSiguiente() {
    verDatos();
  }
  public void PADUltimo() { verDatos();
  }
  public void PADQuery() {
    activar(true);
    pro_codiE.setQuery(true);
    pro_codiE.resetTexto();
    Bbuscar.setEnabled(false);
    pro_nombE.setEnabled(false);
  }

  public void ej_query1() {
    Vector v=new Vector();
    v.addElement(pro_codiE.getStrQuery());
    s="SELECT distinct(pro_codi) FROM refproeq WHERE emp_codi = "+EU.em_cod;
    s=creaWhere(s,v);

//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos regisgtros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Inventarios: ", ex);
    }

  }

  public void canc_query() {
    pro_codiE.setQuery(false);
    pro_codiE.setQuery(false);

    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }

  public void PADEdit() {
  }
  public void ej_edit1() {
    /**@todo Implement this gnu.chu.interfaces.PAD method*/
    throw new java.lang.UnsupportedOperationException("Method ej_edit1() not yet implemented.");
  }
  public void canc_edit() {
    /**@todo Implement this gnu.chu.interfaces.PAD method*/
    throw new java.lang.UnsupportedOperationException("Method canc_edit() not yet implemented.");
  }
  public void PADAddNew() {
    cPanel1.resetTexto();
    jt.removeAllDatos();
    activar(true);
    Baceptar.setEnabled(false);
    jt.setEnabled(false);
    pro_codiE.requestFocus();
  }
  public void ej_addnew1() {
    if (pro_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo de Producto");
      return;
    }

    guardaDatos();
    activaTodo();
    verDatos();
  }
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos();
  }
  public void PADDelete() {
    /**@todo Implement this gnu.chu.interfaces.PAD method*/
    throw new java.lang.UnsupportedOperationException("Method PADDelete() not yet implemented.");
  }
  public void ej_delete1() {
    /**@todo Implement this gnu.chu.interfaces.PAD method*/
    throw new java.lang.UnsupportedOperationException("Method ej_delete1() not yet implemented.");
  }
  public void canc_delete() {
    /**@todo Implement this gnu.chu.interfaces.PAD method*/
    throw new java.lang.UnsupportedOperationException("Method canc_delete() not yet implemented.");
  }
}
