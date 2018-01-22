
package gnu.chu.anjelica.inventario;
/**
 *
 * <p>Titulo: creastk</p>
 * <p>Descripción: Pone El stock de un prod. a lo que deseemos <br>
 * Este programa solo se deberia usar en casos limitados, si sabemos a ciencia
 * cierta que un individuo existe pero, por algun error, el programa no nos lo deja introducir.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2018
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.sql.Types;


public class CreaStkPart extends ventana
{
    private String s;
    public CreaStkPart(EntornoUsuario eu, Principal p)
    {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Crea Stock ");

     try  {
       if(jf.gestor.apuntar(this))
           jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e) {
       ErrorInit(e);
     }
   }

   public CreaStkPart(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Crea Stock");
     eje=false;
     jf=null;
     try  {
       jbInit();
     }
     catch (Exception e) {
       ErrorInit(e);    
     }
   }

   private void jbInit() throws Exception {
     
        iniciarFrame(); 
       
        this.setVersion("2015-06-11");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(543, 201));
    }
    public static String getNombreClase() {
        return "gnu.chu.anjelica.inventario.CreaStkPart";
    }
    public void setProducto(int proCodi)
    {
        pro_codiE.setValorInt(proCodi);
    }
    public void setLote(int lote)
    {
        pro_loteE.setValorInt(lote);
    }
    public void setIndividuo(int indiv)
    {
        pro_numindE.setValorInt(indiv);
    }
    public void setEjercicio(int ejerc)
    {
        deo_ejelotE.setValorInt(ejerc);
    }
    public void setSerie(String serie)
    {
        deo_serlotE.setText(serie);
    }
    public void setAlmacen(int almacen)
    {
        alm_codiE.setValorInt(almacen);
    }
    public void ej_focus()
    {
        stp_unactE.requestFocusLater();
    }
    @Override
   public void iniciarVentana() throws Exception
   {
   pro_codiE.iniciar(dtStat,this,vl,EU);
   deo_serlotE.setText("A");
   deo_emplotE.iniciar(dtStat, this, vl, EU);
   deo_ejelotE.setValorInt(EU.ejercicio);
   pdalmace.llenaLinkBox(alm_codiE, dtStat);
   alm_codiE.setValorInt(1);
   deo_emplotE.setValorDec(1);
   activarEventos();
   pro_codiE.requestFocusLater();
 }
 void activarEventos()
 {
   Baceptar.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent e) {
       anulaEntrada();
     }
   });
   stp_unactE.addFocusListener(new FocusAdapter() {
            @Override
     public void focusGained(FocusEvent e) {
       verKilos();
     }
   });
 }

 public boolean verKilos()
 {
   try
   {
     s = "SELECT * FROM v_stkpart where emp_codi = "+deo_emplotE.getValorInt()+
         " and alm_codi = "+ alm_codiE.getValorInt()+
         " and eje_nume = " + deo_ejelotE.getValorInt() +
         " and pro_serie = '" + deo_serlotE.getText() + "'" +
         " and pro_nupar = " + pro_loteE.getValorInt() +
         " and pro_numind = " + pro_numindE.getValorInt() +
         " and pro_codi = " + pro_codiE.getText() ;
     if (!dtCon1.select(s))
     {
       mensajeErr("Registro de Stock NO encontrado");
       stp_kilactE.setValorDec(0);
       stp_unactE.setValorDec(0);
       return false;
     }
     stp_kilactE.setValorDec(dtCon1.getDouble("stp_kilact"));
     stp_unactE.setValorDec(dtCon1.getDouble("stp_unact"));
     stp_kiliniE.setValorDec(dtCon1.getDouble("stp_kilini"));
     stp_uniniE.setValorDec(dtCon1.getDouble("stp_unini"));
     stp_numpalE.setValorInt(dtCon1.getInt("stp_numpal",true));
     stp_numcajE.setValorInt(dtCon1.getInt("stp_numcaj",true));
     return true;
   }
   catch (Exception k)
   {
     Error("Error al buscar entrada", k);
   }
   return false;
 }
 void anulaEntrada()
 {
   new miThread("")
   {
            @Override
     public void run()
     {
       mensaje("Espere, generando Stock ...",false);
       actStk();
       mensaje("");
     }
   };
 }
 void actStk()
 {
   String edAdd="";
   try {
     if (! pro_codiE.controla(true))
     {
       mensajeErr(pro_codiE.getMsgError());
       return;
     }
     this.setEnabled(false);
     int res=mensajes.YES;
     s = "select stp_kilact,stp_unact,stp_numcaj,stp_numpal from stockpart WHERE emp_codi = " + deo_emplotE.getValorInt() +
         " and alm_codi = " + alm_codiE.getValorInt() +
         " and eje_nume = " + deo_ejelotE.getValorInt() +
         " and pro_Serie = '" + deo_serlotE.getText() + "'" +
         " and pro_nupar = " + pro_loteE.getValorInt() +
         " and pro_numind = " + pro_numindE.getValorInt() +
         " and pro_codi = " + pro_codiE.getValorInt();
     if (! dtCon1.select(s,true))
       res=mensajes.mensajePreguntar("NO encontrado Registro en Stock. Continuar?");
     if (res!=mensajes.YES)
     {
       this.setEnabled(true);
       pro_numindE.requestFocus();
       mensajeErr("Generacion Stock ... ANULADO");
       return;
     }
     if (dtCon1.getNOREG())
     {
       dtCon1.addNew("stockpart",false);
       edAdd="ADDNEW";
       dtCon1.setDato("eje_nume",deo_ejelotE.getValorInt());
       dtCon1.setDato("emp_codi",deo_emplotE.getValorInt());
       dtCon1.setDato("pro_serie",deo_serlotE.getText());
       dtCon1.setDato("stp_tiplot","P");
       dtCon1.setDato("pro_nupar",pro_loteE.getValorInt());
//       dtCon1.setDato("stk_nlipar",pro_numindE.getValorInt());
       dtCon1.setDato("pro_codi",pro_codiE.getValorInt());
       dtCon1.setDato("pro_numind",pro_numindE.getValorInt() );
       dtCon1.setDato("alm_codi",alm_codiE.getValorInt() );
       dtCon1.setDato("stp_unini",stp_unactE.getValorDec() );
       dtCon1.setDato("stp_feccre","current_timestamp");
       dtCon1.setDato("stp_fefici",(Date) null);
       dtCon1.setDato("stp_kilini",stp_kilactE.getValorDec());
       dtCon1.setDato("stp_kilact",stp_kilactE.getValorDec());
       dtCon1.setDato("stp_numpal",stp_numpalE.getValorInt());
       dtCon1.setDato("stp_numcaj",stp_numcajE.getValorInt());
     }
     else
     {
       edAdd="EDIT";
       dtCon1.edit(dtCon1.getCondWhere());
     }
     dtCon1.setDato("stp_kilact",stp_kilactE.getValorDec());
     dtCon1.setDato("stp_unact",stp_unactE.getValorDec() );
     dtCon1.setDato("stp_numpal",stp_numpalE.getValorInt());
     dtCon1.setDato("stp_numcaj",stp_numcajE.getValorInt());
     dtCon1.update(stUp);
     ctUp.commit();
     mensajeErr("Inventario ... Generado");
     if (jf != null)
     {
       jf.ht.clear();
       jf.ht.put("%u", EU.usuario);
       jf.ht.put("%p", pro_codiE.getText());
       jf.ht.put("%U", Formatear.format(stp_unactE.getText(), "---9"));
       jf.ht.put("%k", Formatear.format(stp_kilactE.getText(), "---9.99"));
       jf.ht.put("%l", deo_emplotE.getValorInt() + "-" +
                 deo_ejelotE.getValorInt() + "/" +
                 deo_serlotE.getText() + "-" +
                 pro_loteE.getValorInt()+"-"+pro_numindE.getValorInt());
       jf.guardaMens("CS", jf.ht);
     }
     this.setEnabled(true);
     pro_numindE.requestFocus();
   } catch (Exception k)
   {
     Error("Error al crear Stock\n"+edAdd,k);
   }
 }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        alm_codiE = new gnu.chu.controles.CLinkBox();
        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel6 = new gnu.chu.controles.CLabel();
        deo_serlotE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        deo_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel2 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        deo_emplotE = new gnu.chu.camposdb.empPanel();
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        stp_unactE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel9 = new gnu.chu.controles.CLabel();
        stp_kilactE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
        cPanel2 = new gnu.chu.controles.CPanel();
        cLabel12 = new gnu.chu.controles.CLabel();
        stp_uniniE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel13 = new gnu.chu.controles.CLabel();
        stp_kiliniE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
        cLabel10 = new gnu.chu.controles.CLabel();
        stp_numpalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel11 = new gnu.chu.controles.CLabel();
        stp_numcajE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");

        Pprinc.setLayout(null);

        Baceptar.setText("Aceptar");
        Pprinc.add(Baceptar);
        Baceptar.setBounds(295, 110, 121, 28);

        alm_codiE.setAncTexto(30);
        alm_codiE.setHasCambio(true);
        Pprinc.add(alm_codiE);
        alm_codiE.setBounds(170, 23, 250, 17);
        Pprinc.add(pro_numindE);
        pro_numindE.setBounds(365, 48, 32, 17);

        cLabel7.setText("Empresa");
        Pprinc.add(cLabel7);
        cLabel7.setBounds(0, 23, 49, 15);
        Pprinc.add(pro_loteE);
        pro_loteE.setBounds(232, 48, 55, 17);

        cLabel6.setText("Individuo");
        Pprinc.add(cLabel6);
        cLabel6.setBounds(305, 49, 50, 15);

        deo_serlotE.setMayusc(true);
        Pprinc.add(deo_serlotE);
        deo_serlotE.setBounds(159, 48, 20, 17);

        cLabel5.setText("Lote");
        Pprinc.add(cLabel5);
        cLabel5.setBounds(197, 49, 25, 15);

        cLabel1.setText("Producto");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(0, 0, 58, 15);

        cLabel4.setText("Serie ");
        Pprinc.add(cLabel4);
        cLabel4.setBounds(123, 49, 32, 15);

        cLabel3.setText("Almacen");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(112, 23, 48, 15);
        Pprinc.add(deo_ejelotE);
        deo_ejelotE.setBounds(56, 48, 40, 17);

        cLabel2.setText("Ejercicio");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(0, 49, 46, 15);
        Pprinc.add(pro_codiE);
        pro_codiE.setBounds(62, 0, 355, 17);

        deo_emplotE.setEnabled(false);
        Pprinc.add(deo_emplotE);
        deo_emplotE.setBounds(62, 23, 40, 19);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Actuales"));
        cPanel1.setLayout(null);

        cLabel8.setText("Unidades ");
        cPanel1.add(cLabel8);
        cLabel8.setBounds(2, 16, 54, 15);
        cPanel1.add(stp_unactE);
        stp_unactE.setBounds(60, 16, 32, 17);

        cLabel9.setText("Kilos");
        cPanel1.add(cLabel9);
        cLabel9.setBounds(100, 16, 39, 15);
        cPanel1.add(stp_kilactE);
        stp_kilactE.setBounds(135, 16, 69, 17);

        Pprinc.add(cPanel1);
        cPanel1.setBounds(2, 70, 210, 40);

        cPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Iniciales"));
        cPanel2.setLayout(null);

        cLabel12.setText("Unidades ");
        cPanel2.add(cLabel12);
        cLabel12.setBounds(2, 16, 54, 15);

        stp_uniniE.setEditable(false);
        cPanel2.add(stp_uniniE);
        stp_uniniE.setBounds(60, 16, 32, 17);

        cLabel13.setText("Kilos");
        cPanel2.add(cLabel13);
        cLabel13.setBounds(100, 16, 39, 15);

        stp_kiliniE.setEditable(false);
        cPanel2.add(stp_kiliniE);
        stp_kiliniE.setBounds(135, 16, 69, 17);

        Pprinc.add(cPanel2);
        cPanel2.setBounds(210, 70, 210, 40);

        cLabel10.setText("Palet");
        Pprinc.add(cLabel10);
        cLabel10.setBounds(10, 110, 40, 17);
        Pprinc.add(stp_numpalE);
        stp_numpalE.setBounds(50, 110, 32, 17);

        cLabel11.setText("Caja");
        Pprinc.add(cLabel11);
        cLabel11.setBounds(100, 110, 30, 17);
        Pprinc.add(stp_numcajE);
        stp_numcajE.setBounds(130, 110, 32, 17);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CPanel cPanel2;
    private gnu.chu.controles.CTextField deo_ejelotE;
    private gnu.chu.camposdb.empPanel deo_emplotE;
    private gnu.chu.controles.CTextField deo_serlotE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField stp_kilactE;
    private gnu.chu.controles.CTextField stp_kiliniE;
    private gnu.chu.controles.CTextField stp_numcajE;
    private gnu.chu.controles.CTextField stp_numpalE;
    private gnu.chu.controles.CTextField stp_unactE;
    private gnu.chu.controles.CTextField stp_uniniE;
    // End of variables declaration//GEN-END:variables
}
