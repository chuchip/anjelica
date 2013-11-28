package gnu.chu.anjelica.compras;

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import gnu.chu.anjelica.despiece.*;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;


/**
 *
 * <p>Título: PDetComp</p>
 * <p>Descripción: Panel que muestra el proveedor, Fecha de Recepcion y
 * Precio de Compra sobre un producto en particular.</p>
 *
 * <p>Copyright: Copyright (c) 2008
 *  <p>  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 */

public class PDetComp extends CPanel
{
  ventana papa;
  CLabel cLabel8 = new CLabel();
  CLabel acc_serieE = new CLabel();
  CLabel acc_feccadE = new CLabel();
  CTextField acl_prcomE = new CTextField(Types.DECIMAL,"---,--9.999");
  CLabel cLabel4 = new CLabel();
  CTextField prv_codiE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel7 = new CLabel();
  CTextField deo_codiE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel6 = new CLabel();
  CLabel prv_nombE = new CLabel();
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CCheckBox swCompraInt = new CCheckBox();
  CLabel cLabel9 = new CLabel();
  CLabel acc_fecrecE = new CLabel();
  CLabel cLabel5 = new CLabel();

  public PDetComp(ventana padre)
  {
    try
    {
      papa = padre;
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    cLabel5.setBounds(new Rectangle(3, 18, 47, 14));
    cLabel5.setText("Albaran");
    acc_fecrecE.setBackground(Color.orange);
    acc_fecrecE.setOpaque(true);
    acc_fecrecE.setHorizontalAlignment(SwingConstants.CENTER);
    acc_fecrecE.setBounds(new Rectangle(209, 18, 79, 14));
    cLabel9.setText("Despiece");
    cLabel9.setBounds(new Rectangle(197, 35, 61, 14));
    swCompraInt.setBounds(new Rectangle(326, 35, 99, 14));
    swCompraInt.setEnabled(false);
    swCompraInt.setText("Compra Int");
    swCompraInt.setBackground(Color.orange);
    acc_numeE.setBackground(Color.orange);
    acc_numeE.setOpaque(true);
    acc_numeE.setHorizontalAlignment(SwingConstants.RIGHT);
    acc_numeE.setBounds(new Rectangle(69, 18, 62, 14));
    prv_nombE.setBackground(Color.orange);
    prv_nombE.setOpaque(true);
    prv_nombE.setBounds(new Rectangle(137, 2, 287, 14));
    cLabel6.setBounds(new Rectangle(147, 18, 61, 15));
    cLabel6.setText("Fecha Alb.");
    deo_codiE.setBounds(new Rectangle(252, 35, 62, 14));
    deo_codiE.setHorizontalAlignment(SwingConstants.RIGHT);
    deo_codiE.setOpaque(true);
    deo_codiE.setBackground(Color.orange);
    cLabel7.setBounds(new Rectangle(301, 20, 37, 15));
    cLabel7.setText("Precio");
    prv_codiE.setBounds(new Rectangle(63, 2, 62, 14));
    prv_codiE.setOpaque(true);
    prv_codiE.setBackground(Color.orange);
    cLabel4.setBounds(new Rectangle(2, 2, 62, 16));
    cLabel4.setText("Proveedor");
    cLabel4.setRequestFocusEnabled(true);
    acl_prcomE.setBounds(new Rectangle(343, 18, 79, 14));
    acl_prcomE.setHorizontalAlignment(SwingConstants.RIGHT);
    acl_prcomE.setOpaque(true);
    acl_prcomE.setBackground(Color.orange);
    acc_feccadE.setBounds(new Rectangle(99, 35, 79, 14));
    acc_feccadE.setHorizontalAlignment(SwingConstants.CENTER);
    acc_feccadE.setOpaque(true);
    acc_feccadE.setBackground(Color.orange);
    acc_serieE.setBounds(new Rectangle(49, 18, 17, 14));
    acc_serieE.setOpaque(true);
    acc_serieE.setBackground(Color.orange);
    cLabel8.setBounds(new Rectangle(2, 35, 103, 14));
    cLabel8.setText("Fecha Caducidad");
    this.setLayout(null);
    this.add(prv_nombE, null);
    this.add(cLabel4, null);
    this.add(prv_codiE, null);
    this.add(cLabel5, null);
    this.add(acc_serieE, null);
    this.add(acc_numeE, null);
    this.add(acc_fecrecE, null);
    this.add(cLabel7, null);
    this.add(acl_prcomE, null);
    this.add(cLabel6, null);
    this.add(cLabel9, null);
    this.add(deo_codiE, null);
    this.add(cLabel8, null);
    this.add(acc_feccadE, null);
    this.add(swCompraInt, null);
  }
  public void resetTexto()
  {
    prv_codiE.setText("");
    prv_nombE.setText("");
    acc_fecrecE.setText("");
    acc_serieE.setText("");
    acc_numeE.setText("");
    acl_prcomE.setText("");
  }

  public void setUtilDesp(utildesp utDesp,DatosTabla dt) throws java.sql.SQLException
  {
    prv_codiE.setValorInt(utDesp.prvCodi);
    prv_nombE.setText(pdprove.getNombPrv( utDesp.prvCodi,dt));
    acc_fecrecE.setText(utDesp.fecrecepE);
    acc_serieE.setText(utDesp.accSerie);
    acc_numeE.setValorInt(utDesp.accNume);
    acl_prcomE.setValorDec(utDesp.accPrcom);
    acc_feccadE.setText(utDesp.feccadE);
    deo_codiE.setValorInt(utDesp.deoCodi);
    swCompraInt.setSelected(utDesp.isCompraInterna());
  }

}
