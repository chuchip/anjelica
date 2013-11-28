package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Types;

/**
 *
 * <p>Titulo: anuEntra</p>
 *
 * <p>Descripción: Anula entradas de compras por errores en el programa </p>
 * <p>Copyright: Copyright (c) 2005
 *   Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * </p>
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
*
* @deprecated Usada en su momento por problemas puntuales
 */

public class anuEntra extends ventana
{
  CPanel Pprinc = new CPanel();
  CTextField deo_ejelotE = new CTextField(Types.DECIMAL,"###9");
  CTextField pro_loteE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextField deo_serlotE = new CTextField(Types.CHAR,"X");
  CTextField pro_numindE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel7 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CTextField deo_emplotE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel5 = new CLabel();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel4 = new CLabel();
  CButton Baceptar = new CButton(Iconos.getImageIcon("check"));
  String s;
  CTextField deo_kilosE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel17 = new CLabel();
  CButton Bkil = new CButton();

  public anuEntra(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Anular Entradas de Compras (V 1.1)");

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

   public anuEntra(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Anular Entradas de Compras (V 1.1)");
     eje=false;

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
   iniciar(543, 191);

    Pprinc.setDefButton(Baceptar);
    Pprinc.setDefButtonDisable(false);
    Pprinc.setLayout(null);
   conecta();
   statusBar = new StatusBar(this);
    deo_ejelotE.setBounds(new Rectangle(80, 35, 33, 15));
    deo_ejelotE.setAutoNext(true);
    pro_loteE.setBounds(new Rectangle(263, 35, 46, 15));
    pro_loteE.setAutoNext(true);
    cLabel6.setBounds(new Rectangle(174, 35, 33, 15));
    cLabel6.setText("Serie");
    cLabel8.setToolTipText("");
    cLabel8.setBounds(new Rectangle(316, 35, 26, 15));
    cLabel8.setText("Ind.");
    deo_serlotE.setBounds(new Rectangle(204, 35, 17, 15));
    deo_serlotE.setText("A");
    deo_serlotE.setMayusc(true);
    deo_serlotE.setAutoNext(true);
    pro_numindE.setBounds(new Rectangle(338, 35, 37, 15));
    pro_numindE.setAutoNext(true);
    cLabel7.setBounds(new Rectangle(225, 35, 41, 15));
    cLabel7.setText("N. Lote");
    cLabel9.setBounds(new Rectangle(32, 35, 47, 15));
    cLabel9.setText("Ejercicio");
    deo_emplotE.setBounds(new Rectangle(146, 35, 21, 15));
    deo_emplotE.setAutoNext(true);
    cLabel5.setBounds(new Rectangle(116, 35, 32, 15));
    cLabel5.setText("Emp.");
    pro_codiE.setBounds(new Rectangle(70, 9, 454, 18));
    pro_codiE.setAncTexto(50);
    cLabel4.setText("Producto");
    cLabel4.setBounds(new Rectangle(16, 7, 53, 17));
    Baceptar.setBounds(new Rectangle(223, 60, 104, 32));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setMnemonic('A');
    Baceptar.setText("Aceptar");
    deo_kilosE.setEnabled(false);
    deo_kilosE.setBounds(new Rectangle(454, 36, 67, 14));
    cLabel17.setText("Kilos");
    cLabel17.setBounds(new Rectangle(422, 36, 33, 14));
    Bkil.setBounds(new Rectangle(380, 36, 14, 10));
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    Pprinc.add(cLabel4, null);
    Pprinc.add(pro_codiE, null);
    Pprinc.add(pro_loteE, null);
    Pprinc.add(cLabel9, null);
    Pprinc.add(deo_ejelotE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(deo_emplotE, null);
    Pprinc.add(cLabel6, null);
    Pprinc.add(deo_serlotE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(pro_numindE, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(deo_kilosE, null);
    Pprinc.add(cLabel17, null);
    Pprinc.add(Bkil, null);

    pro_codiE.iniciar(dtStat, this, vl, EU);
 }
 public void iniciarVentana() throws Exception
 {
   pro_codiE.iniciar(dtStat,this,vl,EU);
   activarEventos();
   pro_codiE.requestFocus();
 }
 void activarEventos()
 {
   Baceptar.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       anulaEntrada();
     }
   });
   Bkil.addFocusListener(new FocusAdapter() {
     public void focusGained(FocusEvent e) {
       verKilos();
     }
   });
 }

 boolean verKilos()
 {
   try
   {
     s = "SELECT p.* FROM v_albacol l,v_albcompar p WHERE l.emp_codi = " +
         deo_emplotE.getValorInt() +
         " and l.acc_ano = " + deo_ejelotE.getValorInt() +
         " and l.acc_Serie = '" + deo_serlotE.getText() + "'" +
         " and l.acc_nume = " + pro_loteE.getValorInt() +
         " and p.acp_numind = " + pro_numindE.getValorInt() +
         " and p.pro_codi = " + pro_codiE.getText() +
         " and l.pro_codi = " + pro_codiE.getText() +
         " and p.acc_nume = l.acc_nume " +
         " and p.acc_serie = l.acc_serie " +
         " and p.emp_Codi = l.emp_codi " +
         " and p.acc_ano = l.acc_ano ";
     if (!dtCon1.select(s))
     {
       mensajeErr("Compra NO encontrada");
       deo_kilosE.setValorDec(0);
       return false;
     }
     deo_kilosE.setValorDec(dtCon1.getDouble("acp_canti"));
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
   try {
     if (! pro_codiE.controla(true))
     {
       mensajeErr(pro_codiE.getMsgError());
       return;
     }
     if (!verKilos())
       return;
     int res=mensajes.YES;
     s = "select * from v_stkpart WHERE emp_codi = " + deo_emplotE.getValorInt() +
         " and eje_nume = " + deo_ejelotE.getValorInt() +
         " and pro_Serie = '" + deo_serlotE.getText() + "'" +
         " and pro_nupar = " + pro_loteE.getValorInt() +
         " and pro_numind = " + pro_numindE.getValorInt() +
         " and pro_codi = " + pro_codiE.getText();
     if (! dtStat.select(s))
       res=mensajes.mensajePreguntar("NO encontrado Registro en Stock. Continuar?");
     else
     {
       if (dtStat.getDouble("stp_kilact")!=dtCon1.getDouble("acp_canti"))
         res=mensajes.mensajePreguntar("Kilos en Stock son: "+dtStat.getDouble("stp_kilact")+" Continuar?");
     }
     if (res!=mensajes.YES)
     {
       pro_numindE.requestFocus();
       mensajeErr("Borrado ... ANULADO");
       return;
     }
     s="delete from v_albcompar where  emp_codi = "+deo_emplotE.getValorInt()+
         " and acc_ano = "+deo_ejelotE.getValorInt()+
         " and acc_Serie = '"+deo_serlotE.getText()+"'"+
         " and acc_nume = "+pro_loteE.getValorInt()+
         " and acp_numind = "+pro_numindE.getValorInt()+
         " and pro_codi = "+pro_codiE.getText();
     stUp.executeUpdate(s);
     s="UPDATE v_albacol set acl_numcaj = acl_numcaj - 1, "+
         " acl_canti = acl_canti - "+dtCon1.getDouble("acp_canti")+
         " where emp_codi = "+deo_emplotE.getValorInt()+
         " and acc_ano = "+deo_ejelotE.getValorInt()+
         " and acc_Serie = '"+deo_serlotE.getText()+"'"+
         " and acc_nume = "+pro_loteE.getValorInt()+
         " and acl_nulin = "+dtCon1.getInt("acl_nulin")+
         " and pro_codi = "+pro_codiE.getText();
     stUp.executeUpdate(s);

     s="delete from v_stkpart WHERE emp_codi = "+deo_emplotE.getValorInt()+
         " and eje_nume = "+deo_ejelotE.getValorInt()+
         " and pro_Serie = '"+deo_serlotE.getText()+"'"+
         " and pro_nupar = "+pro_loteE.getValorInt()+
         " and pro_numind = "+pro_numindE.getValorInt()+
         " and pro_codi = "+pro_codiE.getText();
     stUp.executeUpdate(s);
     ctUp.commit();
     mensajeErr("Registro ... Borrado");
     pro_numindE.requestFocus();
   } catch (Exception k)
   {
     Error("Error al Anular Entrada",k);
   }
 }
}
