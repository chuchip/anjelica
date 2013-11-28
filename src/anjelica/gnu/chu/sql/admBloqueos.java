package gnu.chu.sql;

import gnu.chu.utilidades.*;
import java.awt.*;
import gnu.chu.Menu.*;
import gnu.chu.controles.*;
import javax.swing.BorderFactory;
import java.sql.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;


public class admBloqueos extends ventana
{
  boolean dbAdmin=false;
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pdatcon = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CTextField blo_tablaE = new CTextField();
  CLabel cLabel6 = new CLabel();
  CLinkBox usu_nombE = new CLinkBox();
  CButton Baceptar = new CButton("Aceptar (F4)",Iconos.getImageIcon("check"));
  Cgrid jt = new Cgrid(6);
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  CTextField blo_ttyE = new CTextField();
  CButton Bborrar = new CButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  DatosTabla dtAdd;
  public admBloqueos(EntornoUsuario eu, Principal p) {
      EU=eu;
      vl=p.panel1;
      jf=p;
      eje=true;

      setTitulo("Administrar Bloqueos (20040804)");

      try  {
        if(jf.gestor.apuntar(this))
            jbInit();
        else
          setErrorInit(true);
      }
      catch (Exception e) {
        e.printStackTrace();
        setErrorInit(true);
      }
    }

    public admBloqueos(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

      EU=eu;
      vl=p.getLayeredPane();
      setTitulo("Administrar Bloqueos (20040804)");
      eje=false;
      jf=null;
      try  {
        jbInit();
      }
      catch (Exception e) {
        e.printStackTrace();
        setErrorInit(true);
      }
    }

  private void jbInit() throws Exception
  {
    iniciar(527, 480);
    Pprinc.setLayout(gridBagLayout1);

    statusBar=new StatusBar(this);
    jt.setMaximumSize(new Dimension(517, 361));
    jt.setMinimumSize(new Dimension(517, 361));
    jt.setPreferredSize(new Dimension(517, 361));
    jt.setNumRegCargar(100);
    Pdatcon.setMaximumSize(new Dimension(513, 69));
    Pdatcon.setMinimumSize(new Dimension(513, 69));
    Pdatcon.setPreferredSize(new Dimension(513, 69));
    Baceptar.setBounds(new Rectangle(354, 43, 153, 23));
    blo_tablaE.setBounds(new Rectangle(56, 23, 216, 17));
    cLabel5.setBounds(new Rectangle(1, 23, 35, 17));
    usu_nombE.setAncTexto(100);
    usu_nombE.setBounds(new Rectangle(53, 47, 285, 18));
    cLabel6.setBounds(new Rectangle(2, 46, 55, 16));
    feciniE.setBounds(new Rectangle(56, 2, 80, 17));
    cLabel1.setBounds(new Rectangle(3, 2, 49, 17));
    cLabel2.setText("A Fecha");
    cLabel2.setBounds(new Rectangle(374, 1, 49, 17));
    fecfinE.setBounds(new Rectangle(427, 1, 80, 17));
    cLabel4.setText("Terminal");
    cLabel4.setBounds(new Rectangle(279, 23, 49, 16));
    blo_ttyE.setBounds(new Rectangle(333, 23, 175, 17));
    Bborrar.setMaximumSize(new Dimension(193, 22));
    Bborrar.setMinimumSize(new Dimension(193, 22));
    Bborrar.setPreferredSize(new Dimension(193, 22));
    Bborrar.setText("Borrar Bloqueo (F8)");
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    conecta();
    dtAdd=new DatosTabla(ctUp);

    Vector v=new Vector();
    v.addElement("Usuario"); // 0
    v.addElement("Terminal"); // 1
    v.addElement("Fecha"); // 2
    v.addElement("Hora"); // 3
    v.addElement("Programa"); // 4
    v.addElement("Registro"); // 5
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{70,70,90,60,150,150});
    jt.setAlinearColumna(new int[]{0,0,1,1,0,0});
    jt.setAjustarGrid(true);
    Pdatcon.setBorder(BorderFactory.createRaisedBevelBorder());
    Pdatcon.setLayout(null);
    cLabel1.setText("De Fecha");
    cLabel3.setText("Tipo Mens");
    cLabel5.setText("Tabla");
    cLabel6.setText("Usuario");
    Pprinc.add(Pdatcon,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    Pdatcon.add(cLabel1, null);
    Pdatcon.add(feciniE, null);
    Pdatcon.add(fecfinE, null);
    Pdatcon.add(cLabel2, null);
    Pdatcon.add(cLabel6, null);
    Pdatcon.add(blo_tablaE, null);
    Pdatcon.add(cLabel5, null);
    Pdatcon.add(Baceptar, null);
    Pdatcon.add(usu_nombE, null);
    Pprinc.add(jt,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Bborrar,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 181, 3, 139), 0, 0));
    Pdatcon.add(cLabel4, null);
    Pdatcon.add(blo_ttyE, null);
  }

  public void iniciarVentana() throws Exception
  {
    Pdatcon.setDefButton(Baceptar);
    Pdatcon.setButton(KeyEvent.VK_F4,Baceptar);
    jt.setButton(KeyEvent.VK_F8,Bborrar);
    usu_nombE.setColumnaAlias("usu_nomb");
    usu_nombE.setQuery(true);
    blo_tablaE.setColumnaAlias("blo_tabla");
    blo_ttyE.setColumnaAlias("blo_tty");
    s="SELECT usu_admdb from usuarios WHERE usu_nomb = '"+EU.usuario+"'";
    if (dtStat.select(s))
      dbAdmin=dtStat.getString("usu_admdb").equals("S");
    s="SELECT usu_nomb,usu_nomb FROM usuarios ORDER BY usu_nomb";
    dtStat.select(s);
    usu_nombE.addDatos(dtStat);
    activarEventos();
    Pdatcon.setQuery(true);
    feciniE.setQuery(false);
    fecfinE.setQuery(false);
    feciniE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    Bborrar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bborrar_actionPerformed();
      }
    });

  }

  void Baceptar_actionPerformed()
  {
    try {
      if (feciniE.getError() || fecfinE.getError())
        return;
      if (feciniE.isNull())
      {
        mensajeErr("Introduzca Fecha Inicio");
        feciniE.requestFocus();
        return;
      }
      if (fecfinE.isNull())
      {
        mensajeErr("Introduzca Fecha Final");
        fecfinE.requestFocus();
        return;
      }

      Vector v = new Vector();
      v.addElement(usu_nombE.getStrQuery());
      v.addElement(blo_tablaE.getStrQuery());
      v.addElement(blo_ttyE.getStrQuery());
      s = "SELECT * FROM bloqueos where blo_fecha >= TO_DATE('"+feciniE.getText()+"','dd-MM-yyyy') "+
          " and blo_fecha <= TO_DATE('"+fecfinE.getText()+"','dd-MM-yyyy')";
      s = creaWhere(s, v, false);
      s += " ORDER BY blo_fecha desc,blo_hora desc,usu_nomb,blo_tabla";
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados Bloqueos para estos criterios");
        jt.removeAllDatos();
        feciniE.requestFocus();
        return;
      }
      jt.setDatos(dtCon1);
      jt.requestFocusInicio();
      mensajeErr("Datos ... ENCONTRADOS");
    } catch (Exception k)
    {
      Error("Error al buscar Bloqueos",k);
    }
  }

  void  Bborrar_actionPerformed()
  {
    try
    {
      if (jt.isVacio())
        return;
      if (! jt.getValString(0).equals(EU.usuario) && !dbAdmin)
      {
        mensajeErr("Bloqueo de otro Usuario");
        return;
      }
      resetBloqueo(dtAdd,jt.getValString(4),jt.getValString(5));
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%t", jt.getValString(4));
        jf.ht.put("%r", jt.getValString(5));
        jf.guardaMens("BL", jf.ht);
      }

      jt.removeLinea();
      jt.requestFocus();
    } catch (Exception k)
    {
      Error("Error al Borrar Bloqueo",k);
    }
  }
}
