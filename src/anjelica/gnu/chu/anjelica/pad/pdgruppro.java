package gnu.chu.anjelica.pad;

import gnu.chu.Menu.*;
import gnu.chu.controles.CGridEditable;
import gnu.chu.controles.CPanel;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

/**
 *
 * <p>Título: pdgruppro </p>
 * <p>Descripción: Mantenimiento de Grupos de Productos</p>
 * <p>Empresa: miCasa</p>
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
 * @author chuchiP
 * @version 1.0
 */
public class pdgruppro    extends ventanaPad     implements PAD
{
  String s;
  boolean modConsulta=false;
  CPanel Pprinc = new CPanel();
  boolean swVerDatos=true;
  CGridEditable jt = new CGridEditable(3)
  {
        @Override
    public int cambiaLinea(int row, int col)
    {
      return checkLinea(row);
    }
        @Override
     public void afterCambiaLineaDis(int nRow)
     {
        llenaFamilias(nRow);
     }
  };
  Cgrid jtFam=new Cgrid(2);
//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");
  CTextField agr_codiE = new CTextField(Types.DECIMAL,"##9");
  CTextField agp_nombE = new CTextField(Types.CHAR,"X",30);
  CTextField agp_colorE = new CTextField(Types.CHAR,"X",20);

  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public pdgruppro(EntornoUsuario eu, Principal p)
    {
      this(eu, p, null);
    }

    public pdgruppro(EntornoUsuario eu, Principal p,Hashtable ht)
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
            modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
                booleanValue();
        }
        setTitulo("Mantenimiento Grupos Productos");
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

    public pdgruppro(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
    {
      EU = eu;
      vl = p.getLayeredPane();
      eje = false;

      try
      {
        if (ht != null)
        {
          if (ht.get("modConsulta") != null)
            modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
                booleanValue();
        }
        setTitulo("Mantenimiento Grupos Productos");
        jbInit();
      }
      catch (Exception e)
      {
        ErrorInit(e);
      }
    }


    private void jbInit() throws Exception
    {
        Pprinc.setLayout(gridBagLayout1);
        iniciarFrame();
        this.setSize(new Dimension(566, 455));
        this.setVersion("2011-04-08 " + (modConsulta ? "-SOLO LECTURA-" : ""));

        strSql = "SELECT * FROM v_agupro WHERE emp_codi = "+EU.em_cod+
        getOrderQuery();

        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.BTN_EDITAR);

        conecta();

        iniciar(this);
        jt.setMaximumSize(new Dimension(522, 306));
        jt.setMinimumSize(new Dimension(522, 306));
        jt.setPreferredSize(new Dimension(522, 306));

        jtFam.setMaximumSize(new Dimension(522, 206));
        jtFam.setMinimumSize(new Dimension(522, 206));
        jtFam.setPreferredSize(new Dimension(522, 206));
        Baceptar.setMaximumSize(new Dimension(134, 26));
        Baceptar.setMinimumSize(new Dimension(134, 26));
        Baceptar.setPreferredSize(new Dimension(134, 26));
        Bcancelar.setMaximumSize(new Dimension(134, 26));
        Bcancelar.setMinimumSize(new Dimension(134, 26));
        Bcancelar.setPreferredSize(new Dimension(134, 26));
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pprinc.add(jt,     new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 4), 0, 0));
         Pprinc.add(jtFam,     new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 4), 0, 0));
        Pprinc.add(Bcancelar,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 0, 0, 100), 0, 0));
        Pprinc.add(Baceptar,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 100, 0, 0), 0, 0));
        confGrid();
    }
    @Override
    public void iniciarVentana() throws Exception
    {
      jt.setButton(KeyEvent.VK_F4,Baceptar);
      activarEventos();
      verDatos();
    }

    String getOrderQuery()
    {
      return " order by agr_codi ";
    }
    void activarEventos()
    {


    }

    private void verDatos()
    {
      swVerDatos=true;
      s = "select * from v_agupro where emp_codi = " + EU.em_cod +
          getOrderQuery();
      jt.removeAllDatos();
      try
      {
        if (!dtCon1.select(s))
        {
          mensajeErr("NO HAY Agrupaciones disponibles");
          return;
        }
        do
        {
          Vector v = new Vector();
          v.addElement(dtCon1.getString("agr_codi"));
          v.addElement(dtCon1.getString("agp_nomb"));
          v.addElement(dtCon1.getString("agp_color"));
          jt.addLinea(v);
        }
        while (dtCon1.next());
        jt.requestFocusInicio();
        swVerDatos=false;
        llenaFamilias(0);
      }
      catch (Exception k)
      {
        Error("Error al ver datos", k);
      }
    }
   public void PADPrimero() {  }
   public void PADAnterior(){ }
   public void PADSiguiente(){ }
   public void PADUltimo(){  }
    @Override
   public void PADQuery(){}


   public void ej_query1(){}

   public void canc_query(){}

    @Override
   public void PADEdit(){
     activar(true);
     mensajeErr("");
     mensaje("Editando Grupos ...");
     jt.requestFocusInicio();
   }

   public void ej_edit1(){
     try {
       jt.salirGrid();
      
       if (checkLinea(jt.getSelectedRow())>=0)
         return;
       s="SELECT MAX(agr_codi) as agr_codi from v_agupro where emp_codi = " + EU.em_cod ;
       dtStat.select(s);
       int nLin=dtStat.getInt("agr_codi",true)+1;
       s = "update v_agupro set agp_nomb='DELETE' where emp_codi = " + EU.em_cod ;
       dtAdd.executeUpdate(s);


       for (int n = 0; n < jt.getRowCount(); n++)
       {
         if (jt.getValString(n,1).trim().equals(""))
           continue;
         if (jt.getValorInt(n,0)!=0)
         {
            s="SELECT * FROM v_agupro WHERE emp_codi="+EU.em_cod+
                 " AND agr_codi = "+jt.getValorInt(n,0);
            dtAdd.select(s,true);
            dtAdd.edit();
         }
         else
         {
             dtAdd.addNew("v_agupro");
             dtAdd.setDato("emp_codi",EU.em_cod);
             dtAdd.setDato("agr_codi",nLin++);
         }
         dtAdd.setDato("agp_nomb",jt.getValString(n,1));
         dtAdd.setDato("agp_color",jt.getValString(n,2));
         dtAdd.update();
       }
       s = "DELETE FROM v_agupro where  agp_nomb='DELETE' AND emp_codi = " + EU.em_cod ;
       dtAdd.executeUpdate(s);
       ctUp.commit();
     } catch (Exception k)
     {
       Error("Error al actualizar datos",k);
       return;
     }
     mensajeErr("Datos ... Actualizados");
     mensaje("");
     activaTodo();
     verDatos();

   }

   public void canc_edit(){
     activaTodo();
     verDatos();
     mensajeErr("Modificaciones ... Canceladas");
     return;
   }


   public void PADAddNew(){}

   public void ej_addnew1(){}

   public void canc_addnew(){}

   public void PADDelete(){}


   public void ej_delete1(){}

   public void canc_delete(){}

   public void activar(boolean b) {
     nav.setEnabled(!modConsulta);

     jt.setEnabled(b);
     Baceptar.setEnabled(b);
     Bcancelar.setEnabled(b);
   }

   public void afterConecta() throws SQLException, java.text.ParseException
   {

   }


   void confGrid() throws Exception
   {
     Vector v=new Vector();
     v.addElement("Codigo");
     v.addElement("Nombre");
     v.addElement("Color");
     jt.setCabecera(v);
     jt.setAnchoColumna(new int[]{50,250,150});
    
     jt.setAlinearColumna(new int[]{2,0,0});
     agr_codiE.setValorInt(0);
     agr_codiE.setEnabled(false);
     Vector vc=new Vector();
     vc.add(agr_codiE);
     vc.add(agp_nombE);
     vc.add(agp_colorE);
     jt.setCampos(vc);

     Vector v1=new Vector();
     v1.addElement("Codigo");
     v1.addElement("Nombre");
     jtFam.setCabecera(v1);
     jtFam.setAnchoColumna(new int[]{50,250});
     jtFam.setAlinearColumna(new int[]{2,0});
     jtFam.setAjustarGrid(true);
   }
   int checkLinea(int row)
   {
     if (agp_nombE.isNull(true))
       return -1;

     int nRow=jt.getRowCount();
     for (int n=0;n<nRow;n++)
     {
       if (jt.getValString(n,1).trim().equals(agp_nombE.getText().trim()) && n!=row )
       {
         mensajeErr("Nombre de Grupo YA existe en linea: "+(n+1));
         return 1;
       }
       if (jt.getValString(n,2).trim().equals(agp_colorE.getText().trim()) && n!=row )
         mensajeErr("AVISO. Color de grupo esta repetida en Linea: " +(n+1));
     }
     return -1;
   }
   void llenaFamilias(int row)
   {
       if (swVerDatos)
           return;
       try {
           if (jt.isVacio())
               return;
           s="SELECT f.fpr_codi,f.fpr_nomb FROM grufampro g, v_famipro as f "
                   + " where g.agr_codi="+jt.getValorInt(row,0)+
                   " and g.fpr_codi = f.fpr_codi "+
                   " order by f.fpr_codi";
           dtStat.select(s);
           jtFam.setDatos(dtStat);
       }catch (SQLException k )  {Error("Error al buscar familias de grupo",k);}
   }
   /**
    * Devuelve el nombre de la familia mandada como parametro
    * @param famCodi Código de familia
    * @param dt DatosTabla
    * @return Nombre de Familia. Null si no existe ninguna familia con el codigo mandado.
    * @throws SQLException 
    */
   public static String getNombreGrupo(int agrCodi, DatosTabla dt) throws SQLException
   {
      String s="SELECT agp_nomb FROM v_agupro "+
               " where agr_codi = "+agrCodi;
      if (! dt.select(s))
          return null;
      return dt.getString("agp_nomb");
   }
}
