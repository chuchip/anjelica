package gnu.chu.logs;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * <p>Título: MantLogs </p>
 * <p>Descripcion: Mantenimiento de Logs de Aplicaciones</p>
 * <p>Puede recibir el parametro <strong>modConsulta</strong> que indica si es en modo consulta
 * o tambien permite borrar logs. Por defecto es true. <br/>
 * El parametro <strong>allUser</strong> decide si puede ver los logs de todos los usuarios o
 * solo del que lanza el programa. Por defecto es false, lo que indica que solo
 * puede ver lo del usuario que lanza el programa</p>
 *
 * <p>Empresa: miSL</p>
 *  <p>Copyright: Copyright (c) 2005-2011
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
 * @author ChuchiP
 * @version 1.0
 *
 */
public class MantLogs extends ventana {
    private boolean swBorrar;
    boolean PARAM_CONSULTA=true;
    boolean PARAM_ALLUSER=false;
    String s;
    /** Creates new form MantLogs */
    public MantLogs() {
        initComponents();
    }
 public MantLogs(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantLogs(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          PARAM_CONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
         if (ht.get("allUser") != null)
          PARAM_ALLUSER = Boolean.valueOf(ht.get("allUser").toString()).
              booleanValue();
      }
       setTitulo("Mantenimiento de Logs");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  public MantLogs(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          PARAM_CONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
        if (ht.get("allUser") != null)
          PARAM_ALLUSER = Boolean.valueOf(ht.get("allUser").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento de Logs");
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        iniciarFrame();
        if (EU.isRootAV())
        {
            PARAM_CONSULTA=false;
            PARAM_ALLUSER=true;
        }
        this.setVersion("2014-08-27" + (PARAM_CONSULTA ? "SOLO LECTURA" : ""));
      
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        initComponents();

        this.setSize(700,502);
        if (PARAM_CONSULTA)
            Bborrar.setVisible(false);
        conecta();
    }
    @Override
    public void iniciarVentana() throws Exception
    {

        usu_nombE.setColumnaAlias("h.usu_nomb");
        usu_nombE.setQuery(true);
        men_codiE.setColumnaAlias("h.men_codi");
        men_codiE.setQuery(true);
        men_tipoE.setColumnaAlias("men_tipo");
        men_tipoE.setQuery(true);
        men_nombE.setColumnaAlias("h.men_nomb");
        men_nombE.setQuery(true);
        men_codiE.setFormato(true);
        men_codiE.setFormato(Types.CHAR,"XX",2);
        men_codiE.setMayusculas(true);

        if (! PARAM_ALLUSER)
        {
            usu_nombE.addItem(EU.usuario);
            usu_nombE.setText(EU.usuario);
            usu_nombE.setEnabled(false);
        }
        else
        {
            usu_nombE.addItem("*TODOS*");
            s="SELECT usu_nomb FROM usuarios ORDER BY usu_nomb";
            dtStat.select(s);
            usu_nombE.addItem(dtStat,false);
            usu_nombE.setText("*TODOS*");
        }
        s="SELECT men_codi,men_nomb FROM mensajes ORDER BY men_codi";
        dtStat.select(s);
        men_codiE.addDatos(dtStat);
        activarEventos();
        Pdatos.setQuery(true);
        fecIniE.setQuery(false);
        fecFinE.setQuery(false);
        fecIniE.requestFocus();
        fecIniE.setDate(Formatear.getDateAct());
        fecFinE.setDate(Formatear.getDateAct());
        Pcabe.setDefButton(Baceptar);
    }
    private void activarEventos()
    {
     Baceptar.addActionListener(new ActionListener()
     {
      public void actionPerformed(ActionEvent e)
      {
        swBorrar=false;
        Baceptar_actionPerformed();
      }
     });
     Bborrar.addActionListener(new ActionListener()
     {
      public void actionPerformed(ActionEvent e)
      {
        swBorrar=true;
        Baceptar_actionPerformed();
      }
     });
     jt.addListSelectionListener(new  ListSelectionListener()
     {
      public void valueChanged(ListSelectionEvent e)
      {
          if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
            return;
          try {
            buscaComent();
          }catch (Exception k)
          {
              Error("Error al llenar familias",k);
          }
      }
     });
  }
  private void buscaComent()
  {
    try {
        if (jt.isVacio() || !jt.isEnabled())
          return;
        s="SELECT rme_descr FROM razonmens WHERE him_codi ="+jt.getValorInt(0);
        if (! dtStat.select(s))
            rme_descrE.setText("");
        else
            rme_descrE.setText(dtStat.getString("rme_descr"));
    } catch (SQLException k)
    {
        Error("Error al buscar comentarios de logs",k);
    }
  }
  void Baceptar_actionPerformed()
  {
      
      if (fecIniE.getError() || fecFinE.getError())
        return;
      if (fecIniE.isNull())
      {
        mensajeErr("Introduzca Fecha Inicio");
        fecIniE.requestFocus();
        return;
      }
      if (fecIniE.isNull())
      {
        mensajeErr("Introduzca Fecha Final");
        fecIniE.requestFocus();
        return;
      }
      new miThread("")
      {
            @Override
        public void run()
        {
          consultar();
        }
      };
    }

    void consultar()
    {
      try 
      {
        this.setEnabled(false);
        if (swBorrar)
        {
         mensaje("Buscando datos....");
         int res= mensajes.mensajeYesNo("Esto borrara TODOS los registros entre las fechas introducidas. Continuar, seguro?");
         if (res!=mensajes.YES)
         {
             mensaje("");
             mensajeErr("Registros NO borrados");
             this.setEnabled(true);
             return;
         }
         s="delete from histmens where him_fecha >= TO_DATE('" + fecIniE.getText() + "','dd-MM-yyyy') " +
                   " and him_fecha <= TO_DATE('" + fecFinE.getText() + "','dd-MM-yyyy')";
         res=dtCon1.executeUpdate(s);
         dtCon1.commit();
         mensaje("");
         mensajeErr(res+" Registros borrados!! ");
         this.setEnabled(true);
         return;
        }
        mensaje("Buscando datos....");
        rme_descrE.setText("");
        jt.setEnabled(false);
        Vector v = new Vector();
        if (! usu_nombE.getText().startsWith("*"))
         v.addElement(usu_nombE.getStrQuery());
        v.addElement(men_codiE.getStrQuery());
        v.addElement(men_nombE.getStrQuery());
        if (! men_tipoE.getValor().startsWith("*"))
         v.addElement(men_tipoE.getStrQuery());
        s = "SELECT h.*,m.men_tipo FROM histmens as h, mensajes as m"
                + " where him_fecha >= TO_DATE('" + fecIniE.getText() + "','dd-MM-yyyy') " +
                   " and him_fecha <= TO_DATE('" + fecFinE.getText() + "','dd-MM-yyyy')"+
                   " and h.men_codi = m.men_codi";
        s = creaWhere(s, v, false);
        s += " ORDER BY him_fecha desc,him_hora desc,usu_nomb,men_codi";
        if (!dtCon1.select(s))
        {
          mensajeErr("No encontrados mensajes para estos criterios");
          jt.removeAllDatos();
          fecIniE.requestFocus();
          mensaje("");
          this.setEnabled(true);
          return;
        }
        jt.setDatos(dtCon1);
        jt.setEnabled(true);
        jt.requestFocusInicio();
        mensajeErr("Datos ... ENCONTRADOS");
        this.setEnabled(true);
        mensaje("");
      }
      catch (Exception k)
      {
        Error("Error al buscar mensajes", k);
      }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        fecFinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        fecFinL = new gnu.chu.controles.CLabel();
        fecIniL = new gnu.chu.controles.CLabel();
        fecIniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        men_codiL = new gnu.chu.controles.CLabel();
        men_codiE = new gnu.chu.controles.CLinkBox();
        men_nombL = new gnu.chu.controles.CLabel();
        men_nombE = new gnu.chu.controles.CTextField();
        men_tipoE = new gnu.chu.controles.CComboBox();
        usu_nombL = new gnu.chu.controles.CLabel();
        usu_tipoL = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bborrar = new gnu.chu.controles.CButton(Iconos.getImageIcon("eraser"));
        jt = new gnu.chu.controles.Cgrid(7);
        Pdatos = new gnu.chu.controles.CPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rme_descrE = new javax.swing.JTextArea();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(609, 75));
        Pcabe.setMinimumSize(new java.awt.Dimension(609, 75));
        Pcabe.setPreferredSize(new java.awt.Dimension(609, 75));
        Pcabe.setLayout(null);

        fecFinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecFinE);
        fecFinE.setBounds(190, 2, 76, 17);

        fecFinL.setText("A Fecha");
        fecFinL.setMaximumSize(new java.awt.Dimension(43, 18));
        fecFinL.setMinimumSize(new java.awt.Dimension(43, 18));
        fecFinL.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(fecFinL);
        fecFinL.setBounds(140, 2, 44, 17);

        fecIniL.setText("De Fecha");
        fecIniL.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(fecIniL);
        fecIniL.setBounds(0, 2, 52, 17);

        fecIniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecIniE);
        fecIniE.setBounds(60, 2, 76, 17);

        men_codiL.setText("Codigo");
        Pcabe.add(men_codiL);
        men_codiL.setBounds(286, 2, 39, 17);

        men_codiE.setAncTexto(30);
        Pcabe.add(men_codiE);
        men_codiE.setBounds(343, 2, 304, 17);

        men_nombL.setText("Descripcion ");
        men_nombL.setMaximumSize(new java.awt.Dimension(43, 18));
        men_nombL.setMinimumSize(new java.awt.Dimension(43, 18));
        men_nombL.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(men_nombL);
        men_nombL.setBounds(0, 21, 70, 17);
        Pcabe.add(men_nombE);
        men_nombE.setBounds(70, 21, 570, 17);

        men_tipoE.addItem("Todos", "*");
        men_tipoE.addItem("Informacion", "I");
        men_tipoE.addItem("Aviso","A");
        men_tipoE.addItem("Critico","C");
        Pcabe.add(men_tipoE);
        men_tipoE.setBounds(290, 40, 110, 18);

        usu_nombL.setText("Usuario");
        usu_nombL.setMaximumSize(new java.awt.Dimension(43, 18));
        usu_nombL.setMinimumSize(new java.awt.Dimension(43, 18));
        usu_nombL.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(usu_nombL);
        usu_nombL.setBounds(0, 40, 44, 17);

        usu_tipoL.setText("Tipo");
        Pcabe.add(usu_tipoL);
        usu_tipoL.setBounds(250, 40, 24, 17);
        Pcabe.add(usu_nombE);
        usu_nombE.setBounds(70, 40, 153, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(410, 40, 110, 30);

        Bborrar.setText("Borrar");
        Pcabe.add(Bborrar);
        Bborrar.setBounds(550, 40, 100, 30);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Vector v=new Vector();
        v.addElement("Nº"); // 0
        v.addElement("Usuario"); // 0
        v.addElement("Fecha"); // 1
        v.addElement("Hora"); // 2
        v.addElement("Cod"); // 3
        v.addElement("Mensaje"); // 4
        v.addElement("Tipo"); // 5
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{40,100,90,60,50,300,30});
        jt.setAlinearColumna(new int[]{2,0,1,1,0,0,1});
        jt.setAjustarGrid(true);

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jt, gridBagConstraints);

        Pdatos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Pdatos.setMinimumSize(new java.awt.Dimension(669, 66));
        Pdatos.setPreferredSize(new java.awt.Dimension(669, 66));
        Pdatos.setLayout(new javax.swing.BoxLayout(Pdatos, javax.swing.BoxLayout.LINE_AXIS));

        rme_descrE.setColumns(20);
        rme_descrE.setEditable(false);
        rme_descrE.setRows(5);
        jScrollPane1.setViewportView(rme_descrE);

        Pdatos.add(jScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        Pprinc.add(Pdatos, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bborrar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pdatos;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField fecFinE;
    private gnu.chu.controles.CLabel fecFinL;
    private gnu.chu.controles.CTextField fecIniE;
    private gnu.chu.controles.CLabel fecIniL;
    private javax.swing.JScrollPane jScrollPane1;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CLinkBox men_codiE;
    private gnu.chu.controles.CLabel men_codiL;
    private gnu.chu.controles.CTextField men_nombE;
    private gnu.chu.controles.CLabel men_nombL;
    private gnu.chu.controles.CComboBox men_tipoE;
    private javax.swing.JTextArea rme_descrE;
    private gnu.chu.controles.CComboBox usu_nombE;
    private gnu.chu.controles.CLabel usu_nombL;
    private gnu.chu.controles.CLabel usu_tipoL;
    // End of variables declaration//GEN-END:variables

}
