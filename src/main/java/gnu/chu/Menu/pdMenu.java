package gnu.chu.Menu;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Vector;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import gnu.chu.interfaces.*;
import gnu.chu.controles.*;

public class pdMenu extends ventanaPad implements PAD {
//	public CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
//	public CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));

    JLayeredPane CPanel1 = new JLayeredPane();
  CPanel CPanel2 = new CPanel();
  CLabel mnu_usuaL = new CLabel();
  CLabel mnu_acroL = new CLabel();
  CLabel mnu_padrL = new CLabel();
  CLabel mnu_nuliL = new CLabel();
  CLabel mnu_defiL = new CLabel();
  CLabel mnu_tipoL = new CLabel();
  CLabel mnu_progL = new CLabel();
  CLabel mnu_iconL = new CLabel();
  CTextField mnu_usuaE = new CTextField(Types.CHAR,"X",30);
  CTextField mnu_acroE = new CTextField(Types.CHAR,"X",30);
  CTextField mnu_padrE = new CTextField(Types.CHAR,"X",30);
  CTextField mnu_nuliE = new CTextField(Types.DECIMAL,"#9");
  CTextField mnu_defiE = new CTextField(Types.CHAR,"X",60);
  CTextField mnu_progE = new CTextField(Types.CHAR,"X",100);
  CTextField mnu_iconE = new CTextField(Types.CHAR,"X",100);
  CLabel icono= new CLabel();
  Cgrid grid = new Cgrid(8,false);
  CButton bImpr= new CButton("Print",Iconos.getImageIcon("impresora"));
  CButton BEntrar = new CButton("Entrar",Iconos.getImageIcon("rotardown"));
  CButton BRetro = new CButton("Retro",Iconos.getImageIcon("rotarup"));
  CButton BInicio = new CButton("Inicio",Iconos.getImageIcon("back"));
  CButton BCopia = new CButton("Copia",Iconos.getImageIcon("papeldbl"));
  CButton BBus = new CButton("Buscar",Iconos.getImageIcon("buscar"));
  CButton BUsr = new CButton ("Usuario",Iconos.getImageIcon("aldeano"));

  final static String PADRE="MENU";
  String padre="MENU";
  String acron;

  String usuario;
  String usuarioT; //Usuario que quiere transferencia
  String padreT; // Padre para la transferencia

  final static int BOTONES=21;
  final static int NINGUNO=103;
  final static int TRANSFER=102;
  public final static int IMPR=101;
  int botpulsado=NINGUNO;
  int posgrid=-1;
  int rowOld=-1;
  Selector selector;

  DatosTabla dtDestino=new DatosTabla();
  CComboBox mnu_tipoE = new CComboBox();
  CLabel TituloL = new CLabel();
  CPanel CPanel3 = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  int anchoPag=128;
  boolean imprimiendo=false;
//  Impresion impre;
//  CgridReport gridreport=new CgridReport ();
//  ReportDatos rd;
  String arbol="MENU";
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  public pdMenu(EntornoUsuario eu, Principal p) {
    EU=eu;
    vl=p.panel1;
    jf=p;
    eje=true;

    setTitulo("Mantenimiento Menus");

    try  {
      if(jf.gestor.apuntar(this))
          jbInit();
          selector=new Selector(pdMenu.this);
    }
    catch (Exception e) {
      SystemOut.print(e);
      setErrorInit(true);
    }
  }

  public pdMenu(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

    EU=eu;
    vl=p.getLayeredPane();
    setTitulo("Mantenimiento Menus");
    eje=false;

    try  {
      jbInit();
      selector=new Selector(pdMenu.this);
    }
    catch (Exception e) {
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
    iniciarFrame();
    this.setSize(new Dimension(730,440));
    this.setVersion("2007-31-08");
    mnu_nuliL.setText("Numero de linea");
    mnu_nuliL.setBounds(new Rectangle(19, 108, 91, 17));
    mnu_usuaL.setMaximumSize(new Dimension(46, 17));
    mnu_usuaL.setMinimumSize(new Dimension(46, 17));
    mnu_usuaL.setPreferredSize(new Dimension(46, 17));
    mnu_usuaL.setText("Usuario");
    mnu_usuaL.setBounds(new Rectangle(19, 33, 46, 17));
    mnu_acroL.setMaximumSize(new Dimension(99, 17));
    mnu_acroL.setMinimumSize(new Dimension(99, 17));
    mnu_acroL.setPreferredSize(new Dimension(99, 17));
    mnu_acroL.setText("Acronimo Menu");
    mnu_acroL.setBounds(new Rectangle(19, 58, 99, 17));
    mnu_padrL.setMaximumSize(new Dimension(86, 17));
    mnu_padrL.setMinimumSize(new Dimension(86, 17));
    mnu_padrL.setPreferredSize(new Dimension(86, 17));
    mnu_padrL.setText("Nombre Padre");
    mnu_padrL.setBounds(new Rectangle(19, 83, 86, 17));
    mnu_defiL.setText("Descripcion Menu");
    mnu_defiL.setBounds(new Rectangle(19, 132, 110, 17));
    mnu_tipoL.setText("Tipo P / H");
    mnu_tipoL.setBounds(new Rectangle(19, 169, 72, 17));
    mnu_progL.setText("Ruta Programa");
    mnu_progL.setBounds(new Rectangle(18, 189, 84, 17));
    mnu_iconL.setText("Icono");
    mnu_iconL.setBounds(new Rectangle(19, 231, 70, 17));

    CPanel1.setLayout(gridBagLayout2);
    CPanel2.setLayout(null);
    CPanel1.setBackground(CPanel2.getBackground());
    bImpr.setMargin(new Insets(0,0,0,0));
    BEntrar.setMargin(new Insets(0,0,0,0));
    BRetro.setMargin(new Insets(0,0,0,0));
    BInicio.setMargin(new Insets(0,0,0,0));
    BCopia.setMargin(new Insets(0,0,0,0));
    BBus.setBounds(new Rectangle(122, 227, 80, 17));
    BBus.setMargin(new Insets(0,0,0,0));
    BUsr.setMargin(new Insets(0,0,0,0));

    nav=new navegador(this,false,navegador.CONSU);
    statusBar=new StatusBar (this);

    grid.setMaximumSize(new Dimension(281, 265));
    grid.setMinimumSize(new Dimension(281, 265));
    grid.setPreferredSize(new Dimension(281, 265));
    CPanel2.setBorder(BorderFactory.createEtchedBorder());
    CPanel2.setMaximumSize(new Dimension(210, 310));
    CPanel2.setMinimumSize(new Dimension(210, 310));
    CPanel2.setPreferredSize(new Dimension(210, 310));
    Baceptar.setBounds(new Rectangle(2, 270, 104, 25));
    Baceptar.setMaximumSize(new Dimension(70, 31));
    Baceptar.setMinimumSize(new Dimension(70, 31));
    Baceptar.setPreferredSize(new Dimension(70, 31));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Bcancelar.setBounds(new Rectangle(108, 270, 100, 25));
    Bcancelar.setMaximumSize(new Dimension(70, 31));
    Bcancelar.setMinimumSize(new Dimension(70, 31));
    Bcancelar.setPreferredSize(new Dimension(70, 31));
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    mnu_usuaE.setMaximumSize(new Dimension(88, 17));
    mnu_usuaE.setMinimumSize(new Dimension(88, 17));
    mnu_usuaE.setPreferredSize(new Dimension(88, 17));
    mnu_usuaE.setMayusc(false);
    mnu_usuaE.setMinusc(false);
    mnu_usuaE.setBounds(new Rectangle(114, 34, 88, 17));
    mnu_acroE.setMaximumSize(new Dimension(88, 17));
    mnu_acroE.setMinimumSize(new Dimension(88, 17));
    mnu_acroE.setPreferredSize(new Dimension(88, 17));
    mnu_acroE.setMaxLong(30);
    mnu_acroE.setBounds(new Rectangle(114, 57, 88, 17));
    mnu_padrE.setMaximumSize(new Dimension(88, 17));
    mnu_padrE.setBounds(new Rectangle(114, 83, 88, 17));
    mnu_iconE.setBounds(new Rectangle(16, 246, 187, 17));
    mnu_progE.setBounds(new Rectangle(16, 205, 187, 17));
    mnu_tipoE.setBounds(new Rectangle(84, 168, 59, 17));
    mnu_defiE.setBounds(new Rectangle(16, 147, 187, 17));
    icono.setBounds(new Rectangle(162, 104, 40, 40));
    mnu_nuliE.setBounds(new Rectangle(114, 108, 35, 17));
    TituloL.setBounds(new Rectangle(13, 2, 188, 19));
    nav.add(BInicio);
    nav.add(BRetro);
    nav.add(BEntrar);
    nav.add(BCopia);
    nav.add(BUsr);
    nav.add(bImpr);

    iniciar(this);

    CPanel2.add(mnu_acroL, null);
    CPanel2.add(mnu_iconL, null);
    CPanel2.add(mnu_progL, null);
    CPanel2.add(mnu_tipoL, null);
    CPanel2.add(icono, null);
    CPanel2.add(mnu_defiL, null);
    CPanel2.add(mnu_nuliL, null);
    CPanel2.add(mnu_padrL, null);
    CPanel2.add(mnu_usuaL, null);
    CPanel2.add(mnu_nuliE, null);
    CPanel2.add(mnu_padrE, null);
    CPanel2.add(mnu_acroE, null);
    CPanel2.add(mnu_usuaE, null);
    CPanel2.add(mnu_defiE, null);
    CPanel2.add(mnu_progE, null);
    CPanel2.add(mnu_iconE, null);
    CPanel2.add(BBus, null);
    CPanel2.add(Baceptar, null);
    CPanel2.add(Bcancelar, null);
    CPanel2.add(TituloL, null);
    CPanel2.add(mnu_tipoE, null);
    CPanel1.add(grid,   new GridBagConstraints(0, 0, 1, 1, 2.0, 2.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));
    CPanel1.add(CPanel2,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
    this.getContentPane().add(CPanel1,  BorderLayout.CENTER);


    //CPanel1.setDefButton(Baceptar);
    //CPanel1.setEscButton(Bcancelar);
  }

  private void activarEventos() {

    grid.tableView.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() != e.BUTTON1_MASK){
          if (e.getClickCount() < 2)
            return;
          BRetro.doClick();
          return;
        }

        if (e.getClickCount() < 2)
          return;
        BEntrar.doClick();
      }
    });

    grid.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged (ListSelectionEvent e){
            if (e.getValueIsAdjusting()) return;
//            if (grid.isCargando()) return;
            long i=0;
           // if (!grid.isEnabled()) return;


            if (rowOld == grid.getSelectedRow())
              return;
            rowOld=grid.getSelectedRow();

            if ((posgrid==-1)||(grid.getSelectedRow()==-1)) return;  // Al principio

           mnu_nuliE.setText(grid.getValString("Num. Linea"));
           mnu_usuaE.setText(grid.getValString("Usuario"));
           mnu_acroE.setText(grid.getValString("Nombre Menu"));
           mnu_padrE.setText(grid.getValString("Padre"));

           mnu_defiE.setText (grid.getValString("Descripcion"));
           if (grid.getValString("Tipo").compareTo("H")==0)
                mnu_tipoE.setSelectedIndex(1);
           else  mnu_tipoE.setSelectedIndex(0);
           mnu_progE.setText(grid.getValString("Ruta Programa"));
           mnu_iconE.setText(grid.getValString("Icono"));
           icono.setIcon(Iconos.getImageIcon(grid.getValString("Icono")));
           mensaje("");
           validate();
           repaint();
        }
    });

    BBus.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        new pdMenuThread(pdMenu.this,4);
      }
      });

    //Atendemos al evento de pulsado del nuevo boton
    BEntrar.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        new pdMenuThread(pdMenu.this,0);
      }
      });
  //Atendemos al evento de pulsado del nuevo boton
    BInicio.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        new pdMenuThread(pdMenu.this,2);
      }
      });



    //Atendemos al evento de pulsado del nuevo boton
    BCopia.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        botpulsado=TRANSFER;
        new pdMenuThread(pdMenu.this,3);
      }
    });

    //Atendemos al evento de pulsado del nuevo boton
    BRetro.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        new pdMenuThread(pdMenu.this,1);
      }
    });

    BUsr.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent e){
        new pdMenuThread(pdMenu.this,5);
      }
    });

    bImpr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(imprimiendo){
                    imprimiendo = false;
                    mensaje("Cancelando Impresion", true);
                    restaurarConfiguracion();
                }else{
                    imprimiendo = true;
                    bImpr.setToolTipText("Cancelar Impresion");
                    bImpr.setMnemonic('C');
                    bImpr.setIcon(Iconos.getImageIcon("stop"));
                    mensaje("Generando el fichero de impresion");
                    bImpr.setEnabled(true);
                    new pdMenuThread(pdMenu.this,6);
                }
            }

        });

  }

  public void iniciarVentana() throws Exception
  {



    //usuario=JOptionPane.showInputDialog(this,"Introduce el usuario",setTitulo(",JOptionPane.QUESTION_MESSAGE);
    usuario=EU.usuario;
    strSql="SELECT menus.* FROM menus WHERE mnu_padr='"+PADRE+"'"+
      " AND mnu_usua='"+usuario+"'"+
      " ORDER BY mnu_usua, mnu_nuli";

    try{
      conecta();
      configurarControles();
      rellenaGrid(dtCons);
    }catch (Throwable e){
        fatalError("Error (padMenu): "+ dtCons.getMsgError(),e);
        return;
    }


    TituloL.setText("SUBMENUS DEL MENU PRINCIPAL ");

      activar(false);
      //conectamos otro apuntador
      dtDestino.setConexion(ct);
      redibujar();
      activarEventos();
      navActivarAll();
  }



  void configurarControles() throws Exception
  {
      CPanel2.setDefButton(Baceptar);
      CPanel2.setEscButton(Bcancelar);
      mnu_usuaE.setMayusc(false);
      mnu_acroE.setMayusc(false);
      mnu_padrE.setMayusc(false);
      mnu_defiE.setMayusc(false);
      mnu_progE.setMayusc(false);
      mnu_iconE.setMayusc(false);

      //mnu_usuaE.setConectaDB(dtCons,"mnu_usua","");
//      mnu_usuaE.setAceptaNulo(false);
//      mnu_usuaE.setMaxLong(8);

      //mnu_acroE.setConectaDB(dtCons,"mnu_acro","");
//      mnu_acroE.setAceptaNulo(false);
//      mnu_acroE.setMaxLong(8);

      //mnu_padrE.setConectaDB(dtCons,"mnu_padr","");
//      mnu_padrE.setAceptaNulo(false);
//      mnu_padrE.setMaxLong(8);

      //mnu_nuliE.setConectaDB(dtCons,"mnu_nuli","###9");
//      mnu_nuliE.setFormato("###9");
//      mnu_nuliE.setAceptaNulo(false);

      //mnu_defiE.setConectaDB(dtCons,"mnu_defi","");
//      mnu_defiE.setAceptaNulo(false);
//      mnu_defiE.setMaxLong(50);

      //mnu_tipoE.setConectaDB(dtCons,"mnu_tipo","");

      //mnu_progE.setConectaDB(dtCons,"mnu_prog","");
//      mnu_progE.setAceptaNulo(false);
//      mnu_progE.setMaxLong(100);

      //mnu_iconE.setConectaDB(dtCons,"mnu_icon","");
//      mnu_iconE.setAceptaNulo(false);
//      mnu_iconE.setMaxLong(100);

      BUsr.setMnemonic('E');
      BUsr.setToolTipText("Cambiar de usuario");

      BEntrar.setMnemonic('E');
      BEntrar.setToolTipText("Entrar a Submenu");

      BRetro.setMnemonic('R');
      BRetro.setToolTipText("Retroceder a menu Anterior");

      BInicio.setMnemonic('I');
      BInicio.setToolTipText("Ir al principal");

      BCopia.setMnemonic('T');
      BBus.setText("Buscar");
      BBus.setMargin(new Insets(0, 0, 0, 0));
      BBus.setMnemonic('B');

      BCopia.setMnemonic('T');
      BCopia.setToolTipText("Transferencia");

      Vector CabeceraGrid =new Vector();
      CabeceraGrid.addElement("Num. Linea");
      CabeceraGrid.addElement("Usuario");
      CabeceraGrid.addElement("Nombre Menu");
      CabeceraGrid.addElement("Padre");
      CabeceraGrid.addElement("Descripcion");
      CabeceraGrid.addElement("Tipo");
      CabeceraGrid.addElement("Ruta Programa");
      CabeceraGrid.addElement("Icono");

      mnu_tipoE.addItem("Padre","P");
      mnu_tipoE.addItem("Hijo","H");

      grid.setCabecera(CabeceraGrid);
      grid.setAnchoColumna(new int[]{20,50,70,60,150,30,160,160});
//      grid.alinearColumna(3,2);
      grid.setAjustarGrid(true);
      grid.ajustar(false);
      grid.setConfigurar("gnu.chu.Menu.pdmenu",EU,dtStat);
      bImpr.setToolTipText("Imprimir");
      bImpr.setMnemonic('I');
  }

  public void PADPrimero()
  {
    posgrid = 0;
    grid.setRowSelectionInterval(0);
  };

  public void PADAnterior()
  {
    grid.setRowSelectionInterval(--posgrid);

  };
  public void PADSiguiente()
  {
    if (dtCons.isLast())return;
    grid.setRowSelectionInterval(++posgrid);
  };


	public void PADUltimo(){
          grid.setRowSelectionInterval(grid.getNumRegCargar());

  };

//*****************************************************************
//*****************************************************************

	public void PADQuery(){
      mensaje("Busqueda");
      activar(true,BOTONES);
      CPanel2.resetTexto();
      mnu_acroE.setEnabled(true);
      mnu_acroE.requestFocus();
  };

  public void canc_query(){
      mensaje("");
      redibujar();
      activaTodo();

  };

  public void ej_query(){
      new PADThread (this, ej_query);
  };

  public void ej_query1()
  {

      String select="SELECT menus.* FROM menus WHERE"+
      " mnu_acro='"+mnu_acroE.getText()+"'" +
      " AND mnu_usua='" + usuario + "'"+
      " ORDER BY mnu_usua, mnu_nuli";

      try
      {
        if (lanzaSelect(dtCons, select))
        {
          padre = dtCons.getString("mnu_padr");
          TituloL.setText("SUBMENUS DE " + padre);
          strSql = select; //Guardamos select correcta para posible regeneraci�n
        }
        else
        { // Si no encuentra ning�n registro
          rgSelect();
          mensaje("Registro no encontrado");
        }
        rellenaGrid(dtCons);

      }
      catch (Exception ex)
      {
        SystemOut.print(ex);
        return;
      }

      redibujar();
      activaTodo();
  };

//***************************************************************
// Funciones relacionadas con a�adir nuevo Registro
//****************************************************************

	public void PADAddNew(){
      mensaje("A�adir Registro");
      activar(true);
//      CPanel1.resetCambio();
//      CPanel2.resetCambio();
      CPanel2.resetTexto();  // Borramos el contenido de los campos
      mnu_acroE.requestFocus();

      try{
         if (dtStat.select("SELECT max(mnu_nuli) FROM menus WHERE"+
                       " mnu_padr='"+padre+"'" +
                       " AND mnu_usua='" + usuario +"'")){

             int nl=dtStat.getDatoInt(0);
             if (nl++<=23)
                mnu_nuliE.setText(""+nl);
         }else
             mnu_nuliE.setText(""+1);
      }catch (Exception e){
             mnu_nuliE.setText(""+1);
      }

      mnu_usuaE.setText(""+usuario);
      mnu_usuaE.setEnabled(false);
      mnu_padrE.setText(""+padre);


  };

  public void ej_addnew(){
      new PADThread (this,ej_addnew);
  };

//****************************************************************
// Funci�n que a�ade un dato nuevo en la tabla
//****************************************************************

  public void ej_addnew1(){
     String anadir="";

      if (!validarDatos())
              return;

      //  amos el dato

       anadir= "INSERT INTO menus VALUES ('" + usuario +"'"+
                  ", '"+mnu_acroE.getText()+"'"+
                  ", '"+mnu_padrE.getText()+"'"+
                  ", " +mnu_nuliE.getValorInt()+
                  ", '" +mnu_defiE.getText()+"'"+
                  ", '"+mnu_tipoE.getValor()+ "'"+
                  ", '"+mnu_progE.getText()+"'"+
                  ", '"+mnu_iconE.getText()+"','')";


      try{
          if (dtAdd.executeUpdate(anadir)==0){
              fatalError("Error al a�adir Datos (pdmenu):");
              return;
          }
      }catch (Exception e){
          fatalError("Error (pdmenu)"+e.getMessage(),e);
          return;
      }


      // Lo actualizamos en la Base de datos
      try {
        dtAdd.commit();
      } catch (Exception k)
      {
        Error("Error al Terminar Transacion",k);
      }

      try{

      rgSelect();  // Regeneramos la Select
      rellenaGrid(dtCons);
    }catch (Exception e){
              fatalError("Error (pdmenu)"+e.getMessage(),e);
              return;
          }

      mensaje (" REGISTRO INSERTADO");

      redibujar();
      activaTodo();

  };

  public void canc_addnew(){
      mensaje("Adiccion Cancelada");
      activaTodo();
      try {
      rellenaGrid(dtCons); } catch (Exception k){SystemOut.print(k);;}
      redibujar();
  };

//***************************************************************
//  Funciones para Borrar
//****************************************************************

 	public void PADDelete(){
      String select;
      activar(true,BOTONES);
      mensaje("Borrar Registro");

  };

  public void canc_delete(){
    try {
      dtBloq.rollback();
    } catch (Exception k)
    {
      Error("Error al Cancelar Transacion",k);
    }
      activaTodo();
      mensaje("Borrado Cancelado");
  };

  public void ej_delete(){
      new PADThread (this,ej_delete);
  };


  public void ej_delete1(){
   String select;
   int i=0; // Indice del vector de los reg que hay que borrar

   Vector datB=new Vector(); // Vector donde almacenamos los datos a guardar

   String acro=mnu_acroE.getText();
   String usua=usuario;

   datB.addElement(mnu_tipoE.getText());
   datB.addElement(acro);

   select="SELECT menus.* FROM menus WHERE"+
        " mnu_acro='"+acro+"'" +
        " AND mnu_usua='" + usua + "'";
   if (!bloqueaRegistro(select)){
      mensaje("Bloqueo imposible");
      return;
   }

   try {
     select="delete from menus WHERE"+
         " mnu_acro='"+acro+"'" +
         " AND mnu_usua='" + usua + "'";
       dtBloq.executeUpdate(select);
   } catch (Exception j) {
       fatalError("Error (pdMenu)", j);
       return;
   }


   for(i=0;i<datB.size();i++){  // Vamos a�adiendo los datos a borrar

        if (datB.elementAt(i++)=="H"){ // Si es un hijo
              continue;
        }
        // Seleccionamos los hijos
        select="SELECT menus.* FROM menus WHERE"+
        " mnu_padr='"+datB.elementAt(i)+"'" +
        " AND mnu_usua='" + usua + "'";

        //bloqueamos y pasamos los datos al vector
        try{
          if (!dtBloq.select(select,true)){
            continue; // No hay hijos, pasamos al siguiente
          }
          do{
            datB.addElement(dtBloq.getString("mnu_tipo"));
            datB.addElement(dtBloq.getString("mnu_acro"));

            if(!dtBloq.next()) break;

          }while(true);
        }catch(Exception e){
            fatalError("Error (pdMenu):"+e.getMessage());
            return;
        }
        // Nos cepillamos los datos

        try{
          dtBloq.delete();

        }catch (Exception e){
          fatalError("Error (pdMenu)"+e.getMessage());
          return;
        }

   }// del for

  // Actualizamos en la base de datos
      try{
        dtBloq.commit();
        rgSelect();rellenaGrid(dtCons);
      }catch (Exception k)
      {
        fatalError("Error al borrar",k);
        return;
      }

      redibujar();
      activaTodo();
      mensaje("Registro Borrado");
 };

	public void PADChose(){
      matar();
  };


//********************************************************
//   Ejecuta Editar
//*********************************************************
  public void PADEdit(){

      // activamos los campos a modificar
      activar(true);
//      CPanel1.resetCambio();
//      CPanel2.resetCambio();
      mnu_usuaE.setEnabled(false);
      acron=mnu_acroE.getText();
      mensaje("Editando Registros");
      try {
        String selec = "SELECT menus.* FROM menus WHERE mnu_usua='" + usuario +
            "'" +
            " AND mnu_acro ='" + acron + "'";

        if (!dtCon1.select(selec))
        {
          mensaje("Reg no Encontrado");
          return; // Excepci�n controlada por BloqueaRegistro
        }
      } catch (Exception k)
      {
        Error("Error al editar registro",k);
        return;
      }
      mnu_acroE.resetCambio();
      mnu_acroE.requestFocus();

      return;
  };

  //***********************************************************

   public void ej_edit()
   {
      new PADThread (this,ej_edit);
  }

 public void ej_edit1()
 {
   String modific;

   if (!validarDatos())
     return;

   try
   {
     String selec = "SELECT menus.* FROM menus WHERE mnu_usua='" + usuario +
         "'" +
         " AND mnu_acro ='" + acron + "'";
     dtAdd.select(selec,true);
     dtAdd.edit(dtAdd.getCondWhere());
     dtAdd.setDato("mnu_usua", usuario);
     dtAdd.setDato("mnu_acro", mnu_acroE.getText());
     dtAdd.setDato("mnu_padr", mnu_padrE.getText());
     dtAdd.setDato("mnu_nuli", mnu_nuliE.getText());
     dtAdd.setDato("mnu_defi", mnu_defiE.getText());
     dtAdd.setDato("mnu_tipo", mnu_tipoE.getValor());
     dtAdd.setDato("mnu_prog", mnu_progE.getText());
     dtAdd.setDato("mnu_icon", mnu_iconE.getText());

     dtAdd.update(stUp);

     // Modificamos los enlaces de los hijos si es padre y ha cambiado
     if (mnu_tipoE.getValor().compareTo("H") != 0)
     {
       if (acron.compareTo(mnu_acroE.getText()) != 0)
       {
         selec = "update menus set mnu_padr= '" + mnu_acroE.getText() +
             "' WHERE mnu_usua='" + usuario + "'" +
             " AND mnu_padr ='" + acron + "'";
         stUp.executeUpdate(selec);
       }

     }

     ctUp.commit();
     rgSelect();
     rellenaGrid(dtCons);
   }
   catch (Exception k)
   {
     Error("Error al Realizar", k);
     return;
   }

   redibujar();
   activaTodo();
   mensaje("Registro Modificado");
 }


  //*************************************************************
  // Cancela Editar
  //**************************************************************

  public void canc_edit(){
    try {
      dtBloq.rollback();
    } catch (SQLException k)
    {
      Error("Error al Cancelar Transacion",k);
      return;
    }

      activaTodo();
      try {rellenaGrid(dtCons);} catch (Exception k){SystemOut.print(k);}
      redibujar();
      mensaje("Edicion Cancelada");

  };



//****************************************************************
// Retorna True si han habido Datos , false si no
//*******************************************************************
 boolean lanzaSelect(DatosTabla dt,String selec){
    try {
        return (dt.select(selec,false));
    }catch(Exception e){
        fatalError("Error  (padmenu): "+ e.getMessage());
        return false;
    }
 }

 //****************************************************************
  // Funci�n que redibuja el panel con los valores de la �ltima
  // consulta a la tabla
  //****************************************************************

  void redibujar(){
      //CPanelCon.setVisible(false);
      //CPanel2.setVisible(false);
//      dtCons.processEventonext();
      mnu_nuliE.setText(grid.getValString("Num. Linea"));
      mnu_usuaE.setText(grid.getValString("Usuario"));
      mnu_acroE.setText(grid.getValString("Nombre Menu"));
      mnu_padrE.setText(grid.getValString("Padre"));

      mnu_defiE.setText (grid.getValString("Descripcion"));
      if (grid.getValString("Tipo").compareTo("H")==0)
          mnu_tipoE.setSelectedIndex(1);
      else  mnu_tipoE.setSelectedIndex(0);
          mnu_progE.setText(grid.getValString("Ruta Programa"));
          mnu_iconE.setText(grid.getValString("Icono"));
         icono.setIcon(Iconos.getImageIcon(grid.getValString("Icono")));
     // CPanelCon.setVisible(true);
      //CPanel2.setVisible(true);
  }

  boolean validarDatos(){


    String select = "SELECT menus.mnu_nuli FROM menus WHERE" +
        " mnu_acro='" + mnu_acroE.getText() + "'" + // Existe ese Padre
        " AND mnu_usua='" + usuario + "'";

    if (mnu_acroE.hasCambio())
    {
      if (lanzaSelect(dtCon1,select))
      { // Ya existe un ACRONIMO
              mensaje("Existe Acronimo");
              mnu_acroE.requestFocus();
              return false;
      }
    }

      select="SELECT menus.mnu_nuli FROM menus WHERE"+
             " mnu_acro='"+mnu_padrE.getText()+"'"+  // Existe el padre
             " AND mnu_tipo ='P'"+
             " AND mnu_usua='" + usuario+ "'";
      if(mnu_padrE.getText().compareTo("MENU")!=0)
      if (!lanzaSelect(dtCon1,select)){
           mensaje("Padre Inexistente");
           mnu_padrE.requestFocus();
           return false;
      }
    if ((mnu_nuliE.getValorInt()==0) || (mnu_nuliE.getValorInt()>24)){
        mensaje("Numero de Linea incorrecto");
        mnu_nuliE.requestFocus();
        return false;
    }
    try{
      if (mnu_nuliE.hasCambio()){
      if (dtStat.select("SELECT * FROM menus WHERE"+
                       " mnu_padr='"+padre+"'" +
                       " AND mnu_usua='" + usuario +"'"+
                       " AND mnu_nuli="+mnu_nuliE.getValorInt())){


         if (mensajes.mensajeYesNo("DESEA INSERTAR EN ESA LINEA")==mensajes.YES){
             if (!insertar(mnu_nuliE.getValorInt(),padre)){
                mensaje ("Numero de linea Repetido");
                mnu_nuliE.requestFocus();
                return false;
             }

         }else{
            mensaje ("Numero de linea Repetido");
            mnu_nuliE.requestFocus();
            return false;
         }
      }
      } // del if del hascambio
    }catch (Exception e){
        fatalError("Error en select (pdMenu):",e);
        return false;
    }
    return true;

  }

  boolean validarAcron(){

    // comprobamos que no haya duplicidad de padres y que exista el padre
    // del que vamos a a�adir

    String select="SELECT menus.mnu_nuli FROM menus WHERE"+
      " mnu_acro='"+mnu_acroE.getText()+"'" + // Existe ese Padre
      " AND mnu_usua='" +usuario+ "'";

    if (lanzaSelect(dtCon1,select)){ // Ya existe un padre con ese acronimo
        mensaje("Existe Acronimo");
        mnu_acroE.requestFocus();
        return false;
    }

    return true;
  }

  boolean validarAcron(DatosTabla dt) throws Exception
  {


    // comprobamos que no haya duplicidad de padres y que exista el padre
    // del que vamos a a�adir

    String select="SELECT menus.mnu_nuli FROM menus WHERE"+
      " mnu_acro='"+dt.getString("mnu_acro")+"'" + // Existe ese Padre
      " AND mnu_usua='" +usuario+ "'";

    if (lanzaSelect(dtDestino,select)){ // Ya existe un padre con ese acronimo
        mensaje("Existe Acronimo"+dt.getString("mnu_acro"));
        mnu_acroE.requestFocus();
        return false;
    }

    return true;
  }

  boolean validarPadre(){
    if(mnu_padrE.getText().compareTo("MENU")==0) return true;

    String select="SELECT menus.mnu_nuli FROM menus WHERE"+
      " mnu_acro='"+mnu_padrE.getText()+"'"+  // Exixte el padre
      " AND mnu_tipo ='P'"+
      " AND mnu_usua='" +usuario+ "'";

    if (!lanzaSelect(dtCon1,select)){
        mensaje("Padre Inexistente");
        mnu_padrE.requestFocus();
        return false;
    }
    return true;

  }


  boolean insertar(int inicio,String padre)
  {
      try{
          return (dtStat.executeUpdate("UPDATE menus SET mnu_nuli=mnu_nuli+1"+
                            " WHERE mnu_usua='"+usuario+"'"+
                            " AND mnu_padr='"+padre+"'"+
                            " AND mnu_nuli>="+inicio)>0);

      }catch (Exception e){
          fatalError("Error al insertar dato (pdMenu):",e);
          return false;
      }
  }

  public void activar(boolean b){
     activar(b,ALL);
  }

  public void activar(boolean b,int i){
    switch(i){
      case ALL:
        mnu_usuaE.setEnabled(b);
        mnu_acroE.setEnabled(b);
        mnu_padrE.setEnabled(b);
        mnu_nuliE.setEnabled(b);
        mnu_defiE.setEnabled(b);
        mnu_tipoE.setEnabled(b);
        mnu_progE.setEnabled(b);
        mnu_iconE.setEnabled(b);
        BBus.setEnabled(b);

      case BOTONES:
         Baceptar.setEnabled(b);
         Bcancelar.setEnabled(b);

         BRetro.setEnabled(!b);
         BEntrar.setEnabled(!b);
         BInicio.setEnabled(!b);
         BCopia.setEnabled(!b);
         BUsr.setEnabled(!b);
         nav.btnAddNew.setEnabled(!b);
         nav.btnEdit.setEnabled(!b);
         nav.btnDelete.setEnabled(!b);
         nav.btnQuery.setEnabled(!b);
         grid.setEnabled(!b);
         break;
      case IMPR:
         BRetro.setEnabled(b);
         BEntrar.setEnabled(b);
         BInicio.setEnabled(b);
         BCopia.setEnabled(b);
         BUsr.setEnabled(b);
         nav.btnAddNew.setEnabled(b);
         nav.btnEdit.setEnabled(b);
         nav.btnDelete.setEnabled(b);
         nav.btnQuery.setEnabled(b);
         grid.setEnabled(b);
         break;
    }
  }


  int rellenaGrid(DatosTabla dt) throws SQLException
  {
      grid.removeAllDatos();
      if (dt.getNOREG()) return 0;
      int i=0;

      ArrayList<ArrayList> dat=new ArrayList();

      posgrid=0;

      while (true)
      {
          ArrayList v=new ArrayList();
          v.add(dt.getString("mnu_nuli"));
          v.add(dt.getString("mnu_usua"));
          v.add(dt.getString("mnu_acro"));
          v.add(dt.getString("mnu_padr"));
         // v.addElement(dt.getString("mnu_nuli"));
          v.add(dt.getString("mnu_defi"));
          v.add(dt.getString("mnu_tipo"));
          v.add(dt.getString("mnu_prog"));
          v.add(dt.getString("mnu_icon"));
          dat.add(v);

          try{
              if (!dt.next())
              { // Avanzamos el puntero
                grid.tableView.setVisible(false);
                grid.setDatos(dat);
                grid.tableView.setVisible(true);
                grid.setRowSelectionInterval(0);
                redibujar();
                navActivarAll();
                return i;
          }
          }catch(SQLException e){
              fatalError("Error (pdmenu) "+e.getMessage());
              return 0;
          }
         i++;
      }
  }

  boolean entrar(){

      if (grid.getValString("Tipo").compareTo("H")==0){
          mensaje("Es hijo");
          return false;
      }

      rowOld=-1;
      padre=grid.getValString("Nombre Menu");
      BRetro.setEnabled(true);
      BInicio.setEnabled(true);

      String select="SELECT menus.* FROM menus WHERE"+
      " mnu_padr='"+grid.getValor("Nombre Menu")+"'" +
      " AND mnu_usua='" + usuario + "'"+
      " ORDER BY mnu_usua, mnu_nuli";
  try {
      if(lanzaSelect (dtCons,select)){
          TituloL.setText("SUBMENUS DE "+dtCons.getString("mnu_padr"));
          strSql=select; //Guardamos select correcta para posible regeneraci�n
      }else TituloL.setText("SUBMENUS DE "+padre);


      rellenaGrid(dtCons);
      } catch (Exception k)
      {SystemOut.print(k);}
      redibujar();
      return true;
  }

  boolean retroceder(){
      boolean datos=false;


      String select="SELECT menus.* FROM menus WHERE"+
      " mnu_acro='"+padre+"'" +
      " AND mnu_usua='" +usuario + "'"+
      " ORDER BY mnu_usua, mnu_nuli";
  try{
    if (lanzaSelect(dtCons, select))
    {
      datos = true;
      padre = dtCons.getString("mnu_padr");
      TituloL.setText("SUBMENUS DE " + padre);

      select = "SELECT * FROM menus WHERE" +
          " mnu_padr='" + padre + "'" +
          " AND mnu_usua='" + usuario + "'" +
          " ORDER BY mnu_usua, mnu_nuli";
      lanzaSelect(dtCons, select);
      strSql = select; //Guardamos select correcta para posible regeneraci�n
    }
    else
    {
      datos = false;
      mensaje("No mas Padres");
      BRetro.setEnabled(false);
      padre = PADRE;
      rgSelect();
    }

    rellenaGrid(dtCons);
  } catch (Exception k){SystemOut.print(k);;}
      redibujar();
      return datos;
  }

  boolean inicial(){
      boolean datos=false;
      BRetro.setEnabled(false);
      BInicio.setEnabled(false);

      padre=PADRE;
      String select="SELECT menus.* FROM menus WHERE"+
      " mnu_padr='"+PADRE+"'" +
      " AND mnu_usua='" + usuario + "'"+
      " ORDER BY mnu_usua, mnu_nuli";
  try{
    if (lanzaSelect(dtCons, select))
    {
      datos = true;
      TituloL.setText("SUBMENUS DEL MENU PRINCIPAL ");
      strSql = select; //Guardamos select correcta para posible regeneraci�n
    }
    else
    {
      datos = false;
      rgSelect();
    }

    rellenaGrid(dtCons);
  }catch (Exception k){SystemOut.print(k);;}
      redibujar();
      return datos;
  }


  void PADTransfer(){
      mensaje("Elija Menu");
      usuarioT=usuario;
      //padreT=""+grid.getValor("Padre");
      padreT=""+padre;
      if (padreT.compareTo("")==0) padreT="MENU";
      activar(true); // Activamos los campos y desactivamos navegador

      CPanel2.resetTexto();

      mnu_padrE.setEnabled(false);
      mnu_nuliE.setEnabled(false);
      mnu_defiE.setEnabled(false);
      mnu_tipoE.setEnabled(false);
      mnu_progE.setEnabled(false);
      mnu_iconE.setEnabled(false);

      mnu_usuaE.requestFocus();

  }
    /*******************************************************/
 void ej_transfer(){
    // Buscamos el dato

        int i=0; // Indice del vector de los reg que hay que a�adir

        Vector datB=new Vector(); // Vector donde almacenamos los datos a guardar
        String usua=mnu_usuaE.getText();
        String select;
        if (!mnu_acroE.getText().equals("MENU")){
            select="SELECT menus.* FROM menus WHERE"+
                        " mnu_acro='"+mnu_acroE.getText()+"'" +
                        " AND mnu_usua='" + usua + "'";

            if (!lanzaSelect(dtCon1,select)){
                 mensaje("No Encontrado");   // Registro no encontrado
                 mnu_usuaE.requestFocus();
                 return;
            }

try{
            if (!validarAcron(dtCon1)){
                 mensaje("Acronimo Repetido");   // Registro no encontrado
                 mnu_acroE.requestFocus();
                 return;
            }

            // Lo a�adimos al vector para buscar los hijos
            datB.addElement(dtCon1.getString("mnu_tipo"));
            datB.addElement(dtCon1.getString("mnu_acro"));
            // Hacemos la copia en la base de datos
            select = "INSERT INTO menus VALUES ('" +usuarioT+"'"+
                     ", '"+dtCon1.getString("mnu_acro")+"'"+
                     ", '"+padreT+"'"+  // Cambiamos el padre
                     ", " +dtCon1.getString("mnu_nuli")+
                     ", '"+dtCon1.getString("mnu_defi")+ "'"+
                     ", '"+dtCon1.getString("mnu_tipo")+"'"+
                     ", '"+dtCon1.getString("mnu_prog")+"'"+
                     ", '"+dtCon1.getString("mnu_icon")+"')";

              if (dtAdd.executeUpdate(select)==0){
                 fatalError("Error al a�adir Datos (pdmenu):");
                 return;
              }
           }catch (Exception e){
               fatalError("Error (pdmenu)"+e.getMessage());
               return;
           }
        }else{ //TRANSFERENCIA DE TODO EL MENU
             // Lo a�adimos al vector para buscar los hijos
            datB.addElement("P");
            datB.addElement("MENU");

        }

      for(i=0;i<datB.size();i++){  // Vamos a�adiendo los datos

            if (datB.elementAt(i++)=="H"){ // Si es un hijo
               continue;
           }
              // Seleccionamos los hijos
           select="SELECT menus.* FROM menus WHERE"+
              " mnu_padr='"+datB.elementAt(i)+"'" +
              " AND mnu_usua='" + usua + "'";

        //Lanzamos la selec
        try {
        if (!lanzaSelect(dtCon1,select)){
          continue; // No hay hijos, pasamos al siguiente
        }
        do{
          datB.addElement(dtCon1.getString("mnu_tipo"));
          datB.addElement(dtCon1.getString("mnu_acro"));

          // Hacemos la copia en la base de datos

          if (!validarAcron(dtCon1)){
              rgSelect();
              redibujar();
              rellenaGrid(dtCons);

              activaTodo();
              mensaje("Acronimo Repetido");   // Registro no encontrado
              botpulsado=NINGUNO;
              return;
          }

        select = "INSERT INTO menus VALUES ('" +usuarioT+"'"+
                  ", '"+dtCon1.getString("mnu_acro")+"'"+
                  ", '"+dtCon1.getString("mnu_padr")+"'"+
                  ", " +dtCon1.getString("mnu_nuli")+
                  ", '"+dtCon1.getString("mnu_defi")+ "'"+
                  ", '"+dtCon1.getString("mnu_tipo")+"'"+
                  ", '"+dtCon1.getString("mnu_prog")+"'"+
                  ", '"+dtCon1.getString("mnu_icon")+"')";


              if (dtAdd.executeUpdate(select)==0){
              fatalError("Error al a�adir Datos (pdmenu):");
              return;
              }

            if(!dtCon1.next()) break;
        }while(true);
      }catch (Exception e){
                   fatalError("Error (pdmenu)"+e.getMessage());
                   return;
               }

   }// del for

  // Actualizamos en la base de datos
      try {
        dtCon1.commit();
      rgSelect();
      redibujar();
      rellenaGrid(dtCons);
      } catch (Exception k){SystemOut.print(k);}
      activaTodo();
      mensaje("Transf. OK");
      botpulsado=NINGUNO;

  }

  void canc_transfer(){
      botpulsado=NINGUNO;
      mensaje("Transferencia Cancelada");
      activaTodo();
      redibujar();
      try{
      rellenaGrid(dtCons);
      } catch (Exception k){SystemOut.print(k);}
  }
  /**
 	* Rutina a ejecutarse cuando se pulsa el BOTON Aceptar.
 	*/
	public void ej_Baceptar(ActionEvent e){
		// Pulsado Aceptar -
    switch (botpulsado){
        case NINGUNO : super.ej_Baceptar(e);
                      break;
        case TRANSFER : ej_transfer();
                      break;
    }

	}

	/**
 	* Rutina a ejecutarse cuando se pulsa el BOTON Cancelar
 	*/
	public void ej_Bcancelar(ActionEvent e)
	{
     switch (botpulsado){
        case NINGUNO : super.ej_Bcancelar(e);
                      break;
        case TRANSFER : canc_transfer();
                      break;
    }
  }

  void ej_BusIcon(){
      //this.setEnabled(false);
      BBus.setEnabled(false);
      selector.setVisible(true);
      selector.revalidate();
      selector.repaint();
  }

  public void setIcono(String icon){
       icono.setIcon(Iconos.getImageIcon(icon));
       mnu_iconE.setText(icon);
       grid.setValor(icon,"Icono");
  };


  void ej_usuario(){

    if ((usuario=mensajes.mensajeGetTexto("Introduce el usuario",getTitulo(),this))==null) return;


    strSql="SELECT menus.* FROM menus WHERE mnu_padr='"+PADRE+"'"+
      " AND mnu_usua='"+usuario+"'"+
      " ORDER BY mnu_usua, mnu_nuli";
try {
    lanzaSelect(dtCons,strSql);
    if (dtCons.getNOREG()){
        grid.removeAllDatos();
        mensaje("Usuario no encontrado");
    }else
        rellenaGrid(dtCons);
        } catch (Exception k){SystemOut.print(k);}
    TituloL.setText("SUBMENUS DEL MENU PRINCIPAL ");
//    configurarControles();
    activar(false);
    redibujar();
  }

 	public void navActivarAll(){
 	salirEnabled(true);
  	if (grid.isVacio()){
  		nav.setEnabled(navegador.TODOS,false);
			nav.setEnabled(navegador.ADDNEW,true);
		}
		else
	  	nav.setEnabled(navegador.TODOS,true);
  }

  //   Funciones para Imprimir

  public void imprimir(){
       /* impre=new Impresion();
        rd = new ReportDatos();
        rd.EU = EU;
        rd.fichero = "Menu";
        rd.descripcion = "Menu de "+EU.usu_nomb;
        try{
            if(!gridreport.getIniciar() && !gridreport.iniciar(dtStat, "VirtualCom.Menu.pdMenu", EU)){
                fatalError("El fallo es en iniciar :" + gridreport.getMsgError());
                imprimiendo = false;
                return;
            }
            anchoPag = gridreport.getAnchoPapel();
            impre.setVirtualPrint(this);
            impre.setAnchoPapel(anchoPag);
            impre.setMargenSuperior(2);
            impre.setMargenInferior(2);
            impre.setAltoPapel(gridreport.getAltoPapel());
            impre.setLineasCabecera(10);

            // IMPRESION
            Vector padre=new Vector();
            Vector varbol = new Vector();
            padre.add("MENU");
            varbol.add("MENU");
            arbol="MENU";
            impre.startDoc(rd);

            for(int i=0; i<padre.size(); i++){
                if (!dtCon1.select("SELECT * FROM menus WHERE mnu_padr='"+padre.get(i)+"'" +
                " AND mnu_usua='" + usuario + "' ORDER BY mnu_nuli"))
                                             continue;
                mensaje ("Imprimiendo "+dtCon1.getString("mnu_padr")+" ...");
                if (i>0){
                   arbol=varbol.get(i).toString();
                   impre.saltoPagina();
                }
                do{
                   if(!imprimiendo){
                      cancelaImpre();
                      mensaje("Impresion cancelada por el usuario");
                      return;
                   }

                   if (dtCon1.getString("mnu_tipo").equals("P")){
                       padre.add(dtCon1.getString("mnu_acro"));
                       varbol.add(varbol.get(i)+" / "+dtCon1.getString("mnu_acro"));
                       impre.print(impre.getValorNegrita(true)+gridreport.formatea(
                                   dtCon1.getString("mnu_nuli")+"|"+
                                   dtCon1.getString("mnu_acro")+"|"+
                                   dtCon1.getString("mnu_defi")+"|"+
                                   "   D I R E C T O R I O"
                       )+impre.getValorNegrita(false));
                   }else impre.print(gridreport.formatea(
                                   dtCon1.getString("mnu_nuli")+"|"+
                                   dtCon1.getString("mnu_acro")+"|"+
                                   dtCon1.getString("mnu_defi")+"|"+
                                   dtCon1.getString("mnu_prog")
                       ));
                   impre.print();
                }while (dtCon1.next());

            }
            impre.endDoc();
        }catch(Exception e){
            cancelaImpre();
            fatalError("Error Durante la Impresion (pdMenu)", e);
            return;
        }
        imprimiendo = false;
        */
    }



  public void restaurarConfiguracion(){
        bImpr.setIcon(Iconos.getImageIcon("impresora"));
        bImpr.setMnemonic('I');
        bImpr.setToolTipText("Imprimir Menu");
  }

    public void cancelaImpre(){
       /* try{

            impre.setComprimida(false);
            impre.setNegrita(false);
            impre.cancelDoc();
        }
        catch(Exception e){}*/
    }

    public String newPag(){
      return null;
     /*   try{
          return impre.getValorNegrita(true) + impre.getValorComprimida(true)+ Formatear.llenar('_', anchoPag)+impre.SALTO_LINEA +
               Formatear.ajusIzq(EU.empresa, anchoPag / 3) + Formatear.ajusCen("Fecha: " + Fecha.getFechaSys("dd-MM-yyyy"), anchoPag / 3) + Formatear.ajusDer("Pagina: ", anchoPag / 3) + impre.SALTO_LINEA +
               impre.SALTO_LINEA +
               "U S U A R I O: "+usuario+"   M E N U: "+arbol+ impre.SALTO_LINEA+
               Formatear.llenar('_', anchoPag) + impre.SALTO_LINEA +
               impre.SALTO_LINEA +
               gridreport.getCabecera()+ impre.SALTO_LINEA +
               gridreport.getsubCabecera('_')+impre.getValorNegrita(false) + impre.SALTO_LINEA;
        }catch (Exception e){
            fatalError("Error Recogiendo Datos (EnsMP):",e);
            return null;
        }*/
    }

    public String endPag(){
        return null;
    }

    public String endDoc()
    {
        return null;//impre.getValorNegrita(false) + impre.getValorComprimida(false);
    }

    public String startDoc(){
        return null;
    }


}  // De la clase

class pdMenuThread implements Runnable {
  pdMenu pdmenu;
  Thread t;
  int f;
  public pdMenuThread(pdMenu p,int funcion) {
    pdmenu = p;
    f=funcion;
    t = new Thread(this);
    t.start();

  }

  public void run() {
    switch (f){
    case 0:
      if (!pdmenu.entrar())
               pdmenu.nav.btnEdit.doClick();
          break;
    case 1: pdmenu.retroceder();
          break;
    case 2: pdmenu.inicial();
          break;
    case 3: pdmenu.PADTransfer();
          break;
    case 4: pdmenu.ej_BusIcon();
          break;
    case 5:
          pdmenu.ej_usuario();
          break;
    case 6:
          pdmenu.activar(false,pdmenu.IMPR);
          pdmenu.bImpr.setEnabled(true);
          pdmenu.imprimir();
          pdmenu.activar(true,pdmenu.IMPR);
          pdmenu.restaurarConfiguracion();
          pdmenu.mensaje("");
          break;
    }
  }
}// de la Clase PdMenuThread

//*******************************************************************
//         Ventana para navegador de ficheros
//*******************************************************************

class Selector extends CInternalFrame {
  CPanel CPanel1 = new CPanel();
//  XYLayout xYLayout1 = new XYLayout();
  Cgrid vfich = new Cgrid(1,false);
  CLabel viconoL = new CLabel();
  CButton Bok = new CButton();
  CButton Bcancel = new CButton();
  pdMenu pdmenu;

  public Selector(pdMenu p) {
    try  {
      pdmenu=p;
      dibujarVent();
      pdmenu.vl.add(this,new Integer(this.getLayer()+1));
    }
    catch (Exception e) {
     SystemOut.print(e);
    }
  }
  //***************************************************************
  //   Dibuja y rellena la ventana con los iconos
  //****************************************************************

  private void dibujarVent() throws Exception {
    this.setVisible(false);
    this.setSize(new Dimension(361, 245));
    this.setTitle("Seleccione icono");
    Bok.setText("Ok");
    Bcancel.setText("Cancel");

    // Cabeceta para el Grid
    Vector cabecera=new Vector();
    cabecera.addElement("Icono");
    vfich.setCabecera(cabecera);

    vfich.setAnchoColumna(0,220);

    Bok.setIcon(Iconos.getImageIcon("check"));
    Bcancel.setIcon(Iconos.getImageIcon("cancel"));

    // Cargamos los ficheros del directorio de Iconos
    ArrayList v= Iconos.getDatosGrid();
    if (v!=null){
       vfich.setDatos(v);
       vfich.setRowSelectionInterval(0);
       vfich.redibujar();
    }

//    CPanel1.setLayout(xYLayout1);
    //pdmenu.CPanel1.add(this);

    //toFront();

    this.getContentPane().add(CPanel1, BorderLayout.CENTER);
//    CPanel1.add(vfich, new XYConstraints(8, 8, 231, 197));
//    CPanel1.add(viconoL, new XYConstraints(259, 7, 89, 73));
//    CPanel1.add(Bok, new XYConstraints(245, 106, 100, 30));
//    CPanel1.add(Bcancel, new XYConstraints(245, 151, 100, 30));
    CPanel1.setDefButton(Bok);
    CPanel1.setEscButton(Bcancel);
    Bok.setMnemonic('O');
    Bcancel.setMnemonic('C');
    activarEventos();

    revalidate();
    repaint();
  }

  // ****************************************************************
  //   Escuchamos los eventos del Grid y los Botones
  //*****************************************************************

  void activarEventos(){
    Bok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdmenu.BBus.setEnabled(true);
        pdmenu.setIcono(vfich.getValString("Icono"));
        Selector.this.setVisible(false);
      }
    });

    Bcancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdmenu.BBus.setEnabled(true);
        Selector.this.setVisible(false);
      }
    });

    vfich.tableView.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() != e.BUTTON1_MASK)
          return;
        if (e.getClickCount() < 2)
          return;
        Bok.doClick();
      }
    });

    vfich.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged (ListSelectionEvent e){
            long i=0;
            if (vfich.getSelectedRow()==-1) return;  // Al principio

           viconoL.setIcon(Iconos.getImageIcon(vfich.getValString("Icono")));

           validate();
           repaint();
        }
    });



  }

}// De la clase Selector
