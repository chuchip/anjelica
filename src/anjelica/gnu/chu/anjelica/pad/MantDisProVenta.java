/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.pad;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.PAD;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>Título: MantDisProVenta </p>
 * <p>Descripción: Mantenimiento  Discriminadores Productos de  Venta</p>
 * <p>Copyright: Copyright (c) 2005-2016
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
 * @version 1.1
 */
public class MantDisProVenta extends ventanaPad implements PAD
{
  boolean modConsulta=false;
  
  public MantDisProVenta(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantDisProVenta(EntornoUsuario eu, Principal p,Hashtable ht)
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
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString());
      }
      setTitulo("Mantenimiento Discriminadores de Articulos");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(MantFamPro.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public MantDisProVenta(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString());
      }
      setTitulo("Mantenimiento Discriminadores de Articulos");
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(MantFamPro.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, modConsulta ? navegador.NOBOTON : navegador.BTN_EDITAR);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2016-08-22" + (modConsulta ? "SOLO LECTURA" : ""));
        strSql = "SELECT * FROM disproventa "+
                " where dpv_tipo=1"+
                " ORDER BY dpv_nume";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        navActivarAll();
        this.setSize(663,524);
        activar(false);
    }
  
    private void verDatos()
    {
      String s = "select * from v_discrim where dvp_tipo = '"+dpv_tipoE.getValorInt() +
          " order by dvp_tipo ";
      jt.removeAllDatos();
      try {
        if (!dtCon1.select(s))
        {
          mensajeErr("NO HAY discriminadores para este TIPO ");
          return;
        }
        do
        {
          ArrayList v = new ArrayList();
          v.add(dtCon1.getString("dpv_nume"));
          v.add(dtCon1.getString("dpv_nomb"));
          jt.addLinea(v);
        }    while (dtCon1.next());
        jt.requestFocusInicio();
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
      }
    }
   public void PADPrimero() {  }
   @Override
   public void PADAnterior(){ }
   @Override
   public void PADSiguiente(){ }
   @Override
   public void PADUltimo(){  }
    @Override
   public void PADQuery(){}

@Override
   public void ej_query1(){}
@Override
   public void canc_query(){}

  @Override
   public void PADEdit(){
     activar(true);
     mensajeErr("");
     mensaje("Editando discriminadores ...");
     jt.requestFocusInicio();
   }

  @Override
   public void ej_edit1(){
     try {
       jt.salirGrid();
//       if (jt.getRowCount()>1)
//            jt.procesaAllFoco();
       if (checkLinea(jt.getSelectedRow())>=0)
         return;
       
       String s = "delete from v_discrim where "+
           " dvp_tipo = " + dpv_tipoE.getValorInt() ;
       dtAdd.executeUpdate(s);
       
       int nRow=jt.getRowCount();
       for (int n = 0; n < nRow; n++)
       {
//           debug("Linea: "+n);
         if (jt.getValString(n,0).trim().equals(""))
           continue;
//          debug("... Linea: "+n);
         dtAdd.addNew("disproventa");
         
         dtAdd.setDato("dvp_tipo",dpv_tipoE.getValorInt());
         dtAdd.setDato("dpv_nume",jt.getValString(n,0));
         dtAdd.setDato("dpv_nomb",jt.getValString(n,1));
         dtAdd.update();
       }
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
int checkLinea(int row)
   {
     if (dpv_numeE.isNull(true))
       return -1;
     if (dpv_nombE.isNull())
     {
       mensajeErr("Introduzca Descripción para este discriminador");
       return 1;
     }
     int nRow=jt.getRowCount();
     for (int n=0;n<nRow;n++)
     {
       if (jt.getValString(n,0).trim().equals(dpv_numeE.getText().trim()) && n!=row )
       {
         mensajeErr("Codigo de Discriminador YA existe en linea: "+(n+1));
         return 0;
       }
       if (jt.getValString(n,1).trim().equals(dpv_nombE.getText().trim()) && n!=row )
       {
         mensajeErr("Descripcion del discriminador esta repetida en Linea: " +(n+1));
         return 1;
       }
     }
     return -1;
   }
  @Override
   public void canc_edit(){
     activaTodo();
     verDatos();
     mensajeErr("Modificaciones ... Canceladas");
   }


    @Override
   public void PADAddNew(){}

  @Override
   public void ej_addnew1(){}

  @Override
   public void canc_addnew(){}

    @Override
   public void PADDelete(){}

 @Override
   public void ej_delete1(){}
 @Override
   public void canc_delete(){}

  @Override
   public void activar(boolean b) {
     nav.setEnabled(!modConsulta);
     dpv_tipoE.setEnabled(!modConsulta);
     
     jt.setEnabled(b);
     Baceptar.setEnabled(b);
     Bcancelar.setEnabled(b);
   }

  
    @Override
    public void iniciarVentana() throws Exception
    {
      jt.setButton(KeyEvent.VK_F4,Baceptar);
      activarEventos();
    }
    void activarEventos()
    {
       dpv_tipoE.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          try {         
            verDatos();
          } catch (Exception k)
          {
            Error("Error al llenar clases para este Tipo",k);
          }
        }
      });
      jt.addGridListener(new GridAdapter(){
        @Override
        public void cambiaLinea(GridEvent event){
              int reCaLin=checkLinea(event.getLinea());
              event.setColError(reCaLin);
        }
        
      });
    }
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dpv_numeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",3);
        dpv_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        Pprinc = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.CGridEditable(2);
        Pcabe = new gnu.chu.controles.CPanel();
        clv_tipoL = new gnu.chu.controles.CLabel();
        dpv_tipoE = new gnu.chu.controles.CComboBox();
        PPie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();

        dpv_numeE.setMayusc(true);

        dpv_nombE.setMayusc(true);

        Pprinc.setLayout(null);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ArrayList v=new ArrayList();
        v.add("Cod.");
        v.add("Nombre");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,200});
        jt.setAlinearColumna(new int[]{0,0});
        try{
            ArrayList vc=new ArrayList();
            vc.add(dpv_numeE);
            vc.add(dpv_nombE);
            jt.setCampos(vc);
        } catch (Exception k){
            Error("Error al iniciar grid",k);
        }
        Pprinc.add(jt);
        jt.setBounds(9, 39, 380, 210);

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setLayout(null);

        clv_tipoL.setText("Tipo");
        Pcabe.add(clv_tipoL);
        clv_tipoL.setBounds(80, 3, 24, 18);

        dpv_tipoE.addItem("Corte","1");
        dpv_tipoE.addItem("Animal","2");
        dpv_tipoE.addItem("Origen","3");
        Pcabe.add(dpv_tipoE);
        dpv_tipoE.setBounds(120, 3, 130, 18);

        Pprinc.add(Pcabe);
        Pcabe.setBounds(10, 10, 380, 25);

        PPie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PPie.setLayout(null);

        Baceptar.setText("Aceptar");
        PPie.add(Baceptar);
        Baceptar.setBounds(50, 4, 90, 24);

        Bcancelar.setText("Cancelar");
        PPie.add(Bcancelar);
        Bcancelar.setBounds(250, 4, 90, 24);

        Pprinc.add(PPie);
        PPie.setBounds(10, 252, 380, 30);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel clv_tipoL;
    private gnu.chu.controles.CTextField dpv_nombE;
    private gnu.chu.controles.CTextField dpv_numeE;
    private gnu.chu.controles.CComboBox dpv_tipoE;
    private gnu.chu.controles.CGridEditable jt;
    // End of variables declaration//GEN-END:variables
}
