package gnu.chu.anjelica.ventas;

import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdgruppro;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * <p>Título: conVenProd</p>
 * <p>Descripción: Permite Consultar las Ventas por Productos y desglosar
 * luego los clientes que han comprado ese producto.</p>
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
 * @author chuchi P
 * @version 1.0
 */

public class conVenProd  extends ventana
{
  String zonCodi,repCodi;

  String s;
  String condWhere;
  CPanel Pprinc = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel grupoL = new CLabel("Grupo");
  CComboBox grupoE=new CComboBox();
  CButton Baceptar = new CButton();
  CLabel cLabel2 = new CLabel();
  CPanel PintrDatos = new CPanel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLinkBox rep_codiE = new CLinkBox();
  CLabel cLabel18 = new CLabel();
  CLabel cLabel20 = new CLabel();
  CLabel sbe_codiL = new CLabel("Seccion");
  sbePanel sbe_codiE = new sbePanel();
  CLabel sbe_nombL ;
  CLinkBox zon_codiE = new CLinkBox();
  Cgrid jt = new Cgrid(6);
  proPanel pro_codiE = new proPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel3 = new CLabel();
  CComboBox pro_artconE = new CComboBox();
  AlbVenZR alVeZo=null;
  String AGENTE_ARG=null;
  CCheckBox sbe_excluC=new CCheckBox("Excluir Seccion");
  public conVenProd(EntornoUsuario eu, gnu.chu.Menu.Principal p)
  {
    this(eu,p,null);
  }
  public conVenProd(EntornoUsuario eu, gnu.chu.Menu.Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

   setTitulo("Consulta Ventas por Productos");

    try
    {
      if (ht != null)
      {
        if (ht.get("agente") != null)
          AGENTE_ARG = ht.get("agente").toString();
      }

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
  public conVenProd(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    this(p,eu,null);
  }
  public conVenProd(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta Ventas por Productos");
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("zona") != null)
        {
         if (ht.get("agente") != null)
          AGENTE_ARG = ht.get("agente").toString();
        }
      }
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }


   private void jbInit() throws Exception
   {
     iniciarFrame();
     this.setVersion("2017-07-06");
     this.setSize(new Dimension(590,446));
     Pprinc.setLayout(gridBagLayout1);

     conecta();
     statusBar = new StatusBar(this);

    jt.setMaximumSize(new Dimension(565, 281));
    jt.setMinimumSize(new Dimension(565, 281));
    jt.setPreferredSize(new Dimension(565, 281));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

     ArrayList v= new ArrayList();
     v.add("Ref"); // 0
     v.add("Cod"); // 1
     v.add("Nombre"); // 1
     v.add("Kilos"); // 2
     v.add("Importe"); // 3
     v.add("Pr.Medio"); //4
     jt.setCabecera(v);
     jt.setAnchoColumna(new int[]{60,50,150,80,80,60});
     jt.setAlinearColumna(new int[]{0,2,0,2,2,2});
     jt.setFormatoColumna(3,"----,--9.99");
     jt.setFormatoColumna(4,"----,--9.99");
     jt.setFormatoColumna(5,"--,--9.99");
     jt.setAjustarColumnas(true);
     jt.setConfigurar("gnu.chu.anjelica.ventas.conVenProd",EU,dtStat);
    
     cLabel3.setText("Estado");
     cLabel3.setBounds(new Rectangle(265, 24, 45, 16));
     pro_artconE.addItem("Todos","*");
     pro_artconE.addItem("Fresco","F");
     pro_artconE.addItem("Congelado","C");
    
     feciniE.setBounds(new Rectangle(63, 3, 77, 16));
     cLabel1.setText("De Fecha");
     cLabel2.setText("A");
     cLabel1.setBounds(new Rectangle(4, 3, 55, 16));
     Baceptar.setText("Aceptar");
     Baceptar.setIcon(Iconos.getImageIcon("check"));
     Baceptar.setMargin(new Insets(0, 0, 0, 0));
     PintrDatos.setDefButton(Baceptar);
     PintrDatos.setButton(KeyEvent.VK_F4,Baceptar);
     cLabel2.setBounds(new Rectangle(153, 3, 19, 16));
     PintrDatos.setBorder(BorderFactory.createRaisedBevelBorder());
     PintrDatos.setMaximumSize(new Dimension(580, 73));
     PintrDatos.setMinimumSize(new Dimension(580, 73));
     PintrDatos.setPreferredSize(new Dimension(580, 73));
     PintrDatos.setDefButton(Baceptar);
     PintrDatos.setLayout(null);
     fecfinE.setBounds(new Rectangle(176, 3, 77, 16));
     rep_codiE.setAncTexto(30);
     grupoE.addItem("Prod", "A");
     grupoE.addItem("Fam.","F");
     grupoE.addItem("Grupo","G");
     grupoE.setBounds(new Rectangle(547, 3, 30, 16));
     grupoL.setBounds(new Rectangle(520, 3, 25, 16)); 
     rep_codiE.setBounds(new Rectangle(330, 3, 223, 18));
     cLabel18.setText("Represent.");
     cLabel18.setBounds(new Rectangle(262, 3, 65, 16));
     cLabel20.setRequestFocusEnabled(true);
     cLabel20.setText("Zona");
     cLabel20.setBounds(new Rectangle(7, 24, 60, 16));
     zon_codiE.setBounds(new Rectangle(63, 24, 193, 18));
     zon_codiE.setAncTexto(30);
     pro_artconE.setBounds(new Rectangle(311, 24, 70, 18));
     grupoL.setBounds(new Rectangle(385, 24, 35, 16)); 
     grupoE.setBounds(new Rectangle(422, 24, 70, 18)); 
     sbe_codiL.setBounds(new Rectangle(2, 44, 70, 18)); 
     sbe_codiE.setBounds(new Rectangle(75, 44, 50, 18)); 
     sbe_nombL = sbe_codiE.creaLabelSbe();
     sbe_nombL.setBounds(new Rectangle(128, 44, 150, 18)); 
     sbe_excluC.setBounds(new Rectangle(290, 44, 150, 18)); 
     Baceptar.setBounds(new Rectangle(495, 22, 80, 21));
     sbe_codiE.setLabelSbe(sbe_codiE.creaLabelSbe());
     this.getContentPane().add(Pprinc, BorderLayout.CENTER);
     Pprinc.add(PintrDatos,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
     PintrDatos.add(cLabel1, null);
     PintrDatos.add(feciniE, null);
     PintrDatos.add(zon_codiE, null);
     PintrDatos.add(cLabel20, null);
     PintrDatos.add(fecfinE, null);
     PintrDatos.add(cLabel2, null);
     PintrDatos.add(rep_codiE, null);
     PintrDatos.add(cLabel18, null);
     PintrDatos.add(Baceptar, null);
     PintrDatos.add(cLabel3, null);
     PintrDatos.add(pro_artconE, null);
     PintrDatos.add(grupoL, null);
     PintrDatos.add(grupoE, null);
     PintrDatos.add(sbe_codiL, null);
     PintrDatos.add(sbe_codiE, null);
     PintrDatos.add(sbe_codiE, null);
     PintrDatos.add(sbe_nombL, null);
     PintrDatos.add(sbe_excluC, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
   }
   
    @Override
   public void iniciarVentana() throws Exception
   {
     jt.tableView.setToolTipText("Doble click encima linea para detalles venta");
     pro_codiE.iniciar(dtStat,this,vl,EU);
     sbe_codiE.setTipo(sbePanel.TIPO_ARTICULO);
     sbe_codiE.iniciar(dtStat, this, vl, EU);
     sbe_codiE.setLabelSbe(sbe_nombL);
     sbe_codiE.setValorInt(0);
     zon_codiE.getComboBox().setPreferredSize(new Dimension(250,18));
     rep_codiE.getComboBox().setPreferredSize(new Dimension(250,18));
     rep_codiE.setFormato(Types.CHAR, "XX", 2);
     rep_codiE.texto.setMayusc(true);
     MantRepres.llenaLinkBox(rep_codiE, AGENTE_ARG,dtCon1);
     //gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,rep_codiE,"Cr",EU.em_cod,AGENTE_ARG);


     zon_codiE.setFormato(Types.CHAR, "XX", 2);
     zon_codiE.texto.setMayusc(true);
     gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,zon_codiE,"Cz",EU.em_cod);
//    pro_artconE.addItem("Todos","*");
  
     fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     GregorianCalendar gc = new GregorianCalendar();
     gc.setTime(fecfinE.getDate());
     gc.add(GregorianCalendar.DAY_OF_MONTH, -6);
     feciniE.setDate(gc.getTime());
     activarEventos();
     feciniE.requestFocus();
   }
   void activarEventos()
   {
     Baceptar.addActionListener(new java.awt.event.ActionListener()
     {
       @Override
       public void actionPerformed(ActionEvent e)
       {
         Baceptar_actionPerformed(e);
       }
     });
     feciniE.addFocusListener(new java.awt.event.FocusAdapter()
     {
            @Override
       public void focusLost(FocusEvent e)
       {
         feciniE_focusLost(e);
       }
     });
     jt.addMouseListener(new MouseAdapter()
     {
            @Override
       public void mouseClicked(MouseEvent e) {
         if (jt.isVacio())
           return;
         if (e.getClickCount()<2)
           return;
         busVenZona();
       }
     });

   }

   void feciniE_focusLost(FocusEvent e)
   {
     try
     {
       if (e.isTemporary())
         return;
       if (feciniE.getText().equals(""))
         return;
       GregorianCalendar gc = new GregorianCalendar();
       gc.setTime(feciniE.getDate());
       gc.add(GregorianCalendar.DAY_OF_MONTH, 6);
       fecfinE.setDate(gc.getTime());
     }
     catch (Exception k)
     {
       Error("Error al buscar Fecha Final",k);
     }
}
   void Baceptar_actionPerformed(ActionEvent e)
   {
     String s1="",s2="";
     if (feciniE.isNull() || feciniE.getError())
     {
       mensajeErr("Introduzca Fecha Inicial");
       feciniE.requestFocus();
       return;
     }

     if (fecfinE.isNull() || fecfinE.getError())
     {
       mensajeErr("Introduzca Fecha Final");
       fecfinE.requestFocus();
       return;
     }
    
     mensaje("Doble Click en Resultados Zona para Desglosar Clientes");
 //    Formatear.verAncGrid(jt);
     try
     {
       zonCodi = zon_codiE.getText().trim().equals("") ? null :
           zon_codiE.getText();
       if (zonCodi != null)
       {
         if (zonCodi.equals("*") || zonCodi.equals("**"))
           zonCodi = null;
       }

       repCodi = rep_codiE.getText().trim().equals("") ? null :
           rep_codiE.getText();
       if (AGENTE_ARG!=null)
           repCodi=AGENTE_ARG;
       if (repCodi != null)
       {
         if (repCodi.equals("*") || repCodi.equals("**"))
           repCodi = null;
       }
       if (repCodi != null)
         repCodi = repCodi.replace('*', '%');
       if (zonCodi != null)
         zonCodi = zonCodi.replace('*', '%');
       char grupo=grupoE.getValor().charAt(0);
       condWhere=", sum(l.avl_canti) as kilos, " +
             " sum(l.avl_canti*l.avl_prbase)  as importe" +
             " from v_albventa as l,clientes AS cl, v_articulo as a "
           + (grupo=='F'?", v_famipro as f ":"")+
             (grupo=='G'?", v_agupro as g,grufampro as gf ":"") +
            " where avc_fecalb >= TO_DATE('" + feciniE.getText() +
           "','dd-MM-yyyy') " +
           " and avc_fecalb <= TO_DATE('" + fecfinE.getText() +
           "','dd-MM-yyyy') " +
           " and a.pro_codi = l.pro_codi "+
           (grupo=='F'?" and a.fam_codi = f.fpr_codi ":"")+
           (grupo=='G'?" and g.agr_codi = gf.agr_codi "+
             " and a.fam_codi = gf.fpr_codi ":"")+
           (pro_artconE.getValor().equals("*")?"":
               " and a.pro_artcon  "+
                (pro_artconE.getValor().equals("C")?"!=0":"=0"))+
           " and avc_serie >= 'A' AND avc_serie <='C' " +
           " and cl.cli_codi = l.cli_codi " +
           (sbe_codiE.getValorInt()==0?"": " and a.sbe_codi "+ (sbe_excluC.isSelected()?"!":"") +
           "= "+sbe_codiE.getValorInt())+
           " and l.avl_canti != 0 " +
           (EU.isRootAV()?"":" and l.div_codi > 0 ")+
           (repCodi != null ? " and cl.rep_codi LIKE '" + repCodi + "'" : "") +
           (zonCodi != null ? " and cl.zon_codi LIKE '" + zonCodi + "'":"" ) ;
          
         switch (grupoE.getValor().charAt(0) )
         {
             case 'A':
                 s1=" l.pro_codi ";
                 s2=" group by l.pro_codi order by l.pro_codi";
                 break;
             case 'F':
                 s1=" f.fpr_codi ";
                 s2=" group by f.fpr_codi order by f.fpr_codi ";
                 break;
             case 'G':
                 s1=" g.agr_codi ";
                 s2=" group by g.agr_codi order by g.agr_codi ";
         }
       s = "select  "+s1 +
             condWhere+
             s2;
//       debug(s);
       jt.removeAllDatos();
       //    rs=st.executeQuery(s);
       if (!dtCon1.select(s))
       {
         mensajeErr("No encontradas Ventas en estas fechas");
         return;
       }
     } catch (Exception k)
     {
       Error("Error al buscar Datos",k);
       return;
     }
     new miThread("")
     {
       @Override
       public void run()
       {
         llenaGrid();
       }
     };
   }

   void llenaGrid()
   {
     try
     {
       jt.panelG.setVisible(false);
       this.setEnabled(false);
       double kilosT=0,importeT=0;
       String proCodi;
       int proNume=0;
       double kilos,importe;
       char grupo=grupoE.getValor().charAt(0);
       do
       {
         ArrayList v= new ArrayList();

         kilos=dtCon1.getDouble("kilos");
         importe=dtCon1.getDouble("importe");
         String proNomb="";
         switch (grupo )
         {
             case 'A':
                 proNume=dtCon1.getInt("pro_codi");
                 proNomb=pro_codiE.getNombArt(proNume);
                 break;
             case 'F':
                 proNume=dtCon1.getInt("fpr_codi");
                 proNomb=MantFamPro.getNombreFam(proNume,dtStat);
                 break;
             case 'G':
                 proNume=dtCon1.getInt("agr_codi");
                 proNomb=pdgruppro.getNombreGrupo(proNume, dtStat);
         }
         proCodi="";

         v.add(proCodi);
         v.add(proNume);
         v.add(proNomb);
         v.add(""+kilos);
         v.add(""+importe);
         v.add(""+(importe/kilos));
         jt.addLinea(v);
         kilosT+=kilos;
         importeT+=importe;
       } while (dtCon1.next());
       if (grupo!='G')
       {
        ArrayList v=new ArrayList();
        v.add("");
        v.add("");
        v.add(" TOTAL GENERAL ...");
        v.add(""+kilosT);
        v.add(""+importeT);
        v.add(""+(importeT/kilosT));
        jt.addLinea(v);
       }
       jt.panelG.setVisible(true);
       this.setEnabled(true);
       jt.requestFocusInicio();
       mensaje("-- Doble click en las lineas para consultar Clientes --");
       mensajeErr("Consulta ... Realizada");
     }
     catch (Exception k)
     {
       Error("Error al buscar Ventas",k);
     }
   }

   void busVenZona()
   {
     try
     {

       if (alVeZo == null)
       {
         alVeZo = new AlbVenZR();
         alVeZo.iniciar(this);
         vl.add(alVeZo);
        
       }
       alVeZo.setLocation(this.getLocation().x+40, this.getLocation().x+60);
       alVeZo.setSelected(true);
      
       alVeZo.cargaDatosPro( feciniE.getText(), fecfinE.getText(),
                         jt.getValorInt(1),zonCodi,repCodi,
                         "A", "C",0,grupoE.getValor().charAt(0));
      

       this.setEnabled(false);
       this.setFoco(alVeZo);
       alVeZo.setVisible(true);
     }
     catch (Exception ex)
     {
       fatalError("Error al Cargar datos de productos", ex);
     }
   }

    @Override
   public void matar(boolean cerrarConexion)
   {
     if (muerto)
       return;
     if (alVeZo != null)
       alVeZo.matar(false);
     

     super.matar(cerrarConexion);
   }

}
