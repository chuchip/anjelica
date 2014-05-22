
package gnu.chu.anjelica.inventario;
/**
 *
 * <p>Titulo: creastk</p>
 * <p>Descripción: Pone El stock de un prod. a lo que deseemos <br>
 * Este programa solo se deberia usar en casos limitados, si sabemos a ciencia
 * cierta que un individuo existe pero, por algun error, el programa no nos lo deja introducir.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2013
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
       setErrorInit(true);
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
       setErrorInit(true);
     }
   }

   private void jbInit() throws Exception {
     
        iniciarFrame(); 
       
        this.setVersion("2013-12-19");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(543, 191));
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
       deo_kilosE.setValorDec(0);
       stp_unactE.setValorDec(0);
       return false;
     }
     deo_kilosE.setValorDec(dtCon1.getDouble("stp_kilact"));
     stp_unactE.setValorDec(dtCon1.getDouble("stp_unact"));

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
     s = "select stp_kilact,stp_unact from stockpart WHERE emp_codi = " + deo_emplotE.getValorInt() +
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
       dtCon1.addNew("stockpart");
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
       dtCon1.setDato("stp_feccre",Fecha.getFechaSys("dd-MM-yyyy"),"dd-MM-yyyy");
       dtCon1.setDato("stp_fefici",(Date) null);
       dtCon1.setDato("stp_kilini",deo_kilosE.getValorDec());
       dtCon1.setDato("stp_kilact",deo_kilosE.getValorDec());
     }
     else
     {
       edAdd="EDIT";
       dtCon1.edit(dtCon1.getCondWhere());
     }
     dtCon1.setDato("stp_kilact",deo_kilosE.getValorDec());
     dtCon1.setDato("stp_unact",stp_unactE.getValorDec() );
     dtCon1.update(stUp);
     ctUp.commit();
     mensajeErr("Inventario ... Generado");
     if (jf != null)
     {
       jf.ht.clear();
       jf.ht.put("%u", EU.usuario);
       jf.ht.put("%p", pro_codiE.getText());
       jf.ht.put("%U", Formatear.format(stp_unactE.getText(), "---9"));
       jf.ht.put("%k", Formatear.format(deo_kilosE.getText(), "---9.99"));
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
        stp_unactE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel9 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel8 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel6 = new gnu.chu.controles.CLabel();
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
        deo_serlotE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        deo_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel2 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        deo_emplotE = new gnu.chu.camposdb.empPanel();

        Baceptar.setText("Aceptar");

        cLabel9.setText("Kilos");

        alm_codiE.setAncTexto(30);
        alm_codiE.setHasCambio(true);

        cLabel8.setText("Unidades ");

        cLabel7.setText("Empresa");

        cLabel6.setText("Individuo");

        deo_serlotE.setMayusc(true);

        cLabel5.setText("Lote");

        cLabel1.setText("Producto");

        cLabel4.setText("Serie ");

        cLabel3.setText("Almacen");

        cLabel2.setText("Ejercicio");

        deo_emplotE.setEnabled(false);

        javax.swing.GroupLayout PprincLayout = new javax.swing.GroupLayout(Pprinc);
        Pprinc.setLayout(PprincLayout);
        PprincLayout.setHorizontalGroup(
            PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PprincLayout.createSequentialGroup()
                .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PprincLayout.createSequentialGroup()
                        .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stp_unactE, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deo_kilosE, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PprincLayout.createSequentialGroup()
                        .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pro_codiE, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PprincLayout.createSequentialGroup()
                                .addComponent(deo_emplotE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(alm_codiE, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PprincLayout.createSequentialGroup()
                        .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Baceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PprincLayout.createSequentialGroup()
                                .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(deo_ejelotE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deo_serlotE, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pro_loteE, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pro_numindE, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 207, Short.MAX_VALUE))
        );
        PprincLayout.setVerticalGroup(
            PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PprincLayout.createSequentialGroup()
                .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pro_codiE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deo_emplotE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alm_codiE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deo_ejelotE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deo_serlotE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pro_loteE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pro_numindE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PprincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stp_unactE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deo_kilosE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Baceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField deo_ejelotE;
    private gnu.chu.camposdb.empPanel deo_emplotE;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CTextField deo_serlotE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField stp_unactE;
    // End of variables declaration//GEN-END:variables
}
