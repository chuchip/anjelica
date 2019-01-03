package gnu.chu.anjelica.tesoreria;

/**
 * <p>Titulo: ppagreal </p>
 * <p>Descripcion: Panel  de Pagos realizados. Muestra para un Apunte en el libro de vto.
 * mandado como parametros los pagos realizados.</p>
 * <p>Copyright: Copyright (c) 2005-2012
*  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.SystemOut;
import java.sql.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

public class ppagreal extends CPanel
{
  Cgrid jt = new Cgrid(10);
  CPanel Pacumul = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField lip_imporE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel2 = new CLabel();
  CTextField lip_numpagE = new CTextField(Types.DECIMAL,"##9");
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public ppagreal() {
    try {
      jbInit();
    }
    catch(Exception e) {
      SystemOut.print(e);
    }
  }
  private void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
    jt.setMaximumSize(new Dimension(389, 181));
    jt.setMinimumSize(new Dimension(389, 181));
    jt.setPreferredSize(new Dimension(389, 181));
    confGrid();
    jt.setBounds(new Rectangle(6, 6, 389, 181));
    Pacumul.setBorder(BorderFactory.createLineBorder(Color.black));
    Pacumul.setMaximumSize(new Dimension(352, 22));
    Pacumul.setMinimumSize(new Dimension(352, 22));
    Pacumul.setPreferredSize(new Dimension(352, 22));
    Pacumul.setLayout(null);
    cLabel1.setText("Imp. Pagado");
    cLabel1.setBounds(new Rectangle(4, 2, 75, 17));
    lip_imporE.setBounds(new Rectangle(71, 2, 79, 17));
    cLabel2.setText("Num.Pagos");
    cLabel2.setBounds(new Rectangle(253, 2, 67, 17));
    lip_numpagE.setBounds(new Rectangle(315, 2, 33, 17));
    this.add(jt,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 0, 0), 0, 0));
    this.add(Pacumul,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pacumul.add(cLabel1, null);
    Pacumul.add(cLabel2, null);
    Pacumul.add(lip_imporE, null);
    Pacumul.add(lip_numpagE, null);
    lip_imporE.setEnabled(false);
    lip_numpagE.setEnabled(false);
  }

  public boolean buscaDatos(String origen,int empCodi,int ejeNume,int numFra,int numVto,DatosTabla dt) throws SQLException
  {
    String s="SELECT c.*,l.*,bt.bat_nomb FROM libpagdet as l,libpagcab as c,bancteso as bt where l.emp_codi = "+empCodi+
        " and l.lbv_orige = '"+origen+"'"+
        " and l.lbv_numfra = "+numFra+
        " and l.lbv_nume = " + numVto+
        " and c.lbp_nume = l.lbp_nume "+
        " and c.emp_codi = "+empCodi+
        " and c.lbp_numlin = l.lbp_numlin "+
        " and bt.bat_codi = c.bat_codi ";
    jt.removeAllDatos();
    if (!dt.select(s))
      return false;
    double impPag=0;
    int nPago=0;
    do
    {
      Vector v = new Vector();
      v.add(dt.getFecha("lbp_fecpag", "dd-MM-yy"));
      v.add(getTipoPago(dt.getString("lbp_tippag").charAt(0)));
      v.add(dt.getString("bat_nomb"));
      v.add(dt.getString("lip_import"));
      v.add(dt.getString("ban_codi"));
      v.addElement(dt.getString("lbp_baofic"));
      v.addElement(dt.getString("lbp_badico"));
      v.addElement(dt.getString("lbp_bacuen"));
      v.addElement(dt.getString("lbp_numtal"));
      v.addElement(dt.getString("lbp_facprv"));
      jt.addLinea(v);
      impPag+=dt.getDouble("lip_import");
      nPago++;
    } while (dt.next());
    lip_imporE.setValorDec(impPag);
    lip_numpagE.setValorDec(nPago);
    jt.requestFocusInicio();
    return true;
  }
  public void removeAllDatos()
  {
    jt.removeAllDatos();
  }
  private void confGrid()
  {
    Vector v = new Vector();
    v.addElement("Fec.Pago"); // 0
    v.addElement("T.Pago"); // 1
    v.addElement("B.Pago"); // 2
    v.addElement("Imp.Pago"); // 3
    v.addElement("Banco"); // 4
    v.addElement("Ofic."); // 5
    v.addElement("DC"); // 6
    v.addElement("Cuenta"); // 7
    v.addElement("Num.Talon"); // 8
    v.addElement("S/Fra."); // 9
    jt.setCabecera(v);
    jt.setBuscarVisible(false);
    jt.setAlinearColumna(new int[]{1,0,2,2, 2,2,2,2, 0,2});
    jt.setAnchoColumna(new int[]{80,100,120,74,40,40,30,90,90,90});
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(4, "9999");
    jt.setFormatoColumna(5, "9999");
    jt.setFormatoColumna(6, "99");
    jt.setFormatoColumna(7, "9999999999");
  }
  String getTipoPago(char tpa_codi)
  {
    switch (tpa_codi)
    {
      case 'E':
        return "Extranjero";
      case 'P':
        return "Pagare";
      case 'C':
        return "Cheque";
      case 'R':
        return "Recibo";
      case 'T':
        return "Transferencia";
      default:
        return "*"+tpa_codi+" ERR*";
    }

  }
}
