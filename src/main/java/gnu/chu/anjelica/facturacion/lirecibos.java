package gnu.chu.anjelica.facturacion;

import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.controles.*;
import gnu.chu.interfaces.ejecutable;
import java.awt.*;
import java.sql.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.awt.event.*;


/**
 *
 * <p>Título: lirecibos</p>
 * <p>Descripción: Listado de Recibos para cobros de las facturas
 *  de ventas</p>
 * <p>Copyright: Copyright (c) 2005
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
 * @author chuchi P.
 * @version 1.0
 */
public class lirecibos extends ventana  implements JRDataSource
{
  condBusFra PcondBus = new condBusFra();
  ResultSet rs;
  String s;
  CPanel Pprinc = new CPanel();
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));

  public lirecibos(EntornoUsuario eu, Principal p) {
    EU=eu;
    vl=p.panel1;
    jf=p;
    eje=true;

    setTitulo("Listado Recibos de Facturas (20050630)");

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

  public lirecibos(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

    EU=eu;
    vl=p.getLayeredPane();
    setTitulo("Listado Recibos de Facturas (20050630)");

    eje=false;

    try  {
      jbInit();
    }
    catch (Exception e) {
    ErrorInit(e);
    }
  }

  private void jbInit() throws Exception
  {
    iniciar(732, 273);
    conecta();
    statusBar= new StatusBar(this);
    Pprinc.setLayout(null);
    Baceptar.setBounds(new Rectangle(420, 128, 95, 27));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    PcondBus.setBounds(new Rectangle(1, 2, 687, 184));


    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(PcondBus, null);
    PcondBus.add(Baceptar, null);
  }
  public void iniciarVentana() throws Exception
  {
    PcondBus.iniciar(dtStat,this,vl,EU);

    activarEventos();
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Baceptar_actionPerformed();
      }
    });
  }

  void Baceptar_actionPerformed()
  {
    s="SELECT c.*,cl.* FROM v_facvec as c,clientes as cl,v_forpago as fp "+
        " WHERE  c.cli_codi = cl.cli_codi "+
        " and fp.fpa_codi = cl.fpa_codi "+
        PcondBus.getCondWhere()+
        " ORDER BY c.emp_codi, fvc_ano,fvc_nume";

    try {
      rs=ct.createStatement().executeQuery(dtCon1.getStrSelect(s));
      JasperReport jr= Listados.getJasperReport(EU,"recibos");
      java.util.HashMap mp = Listados.getHashMapDefault();
      mp.put( "imagen",Iconos.getPathIcon()+"logotipo.jpg");
      mp.put(JRParameter.REPORT_LOCALE,ejecutable.local);
      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
      gnu.chu.print.util.printJasper(jp, EU);
      mensajeErr("Listado de Recibos .. TERMINADO");
    } catch (Exception k)
    {
      Error("Error al Listar Recibos",k);
    }
  }

  public boolean next() throws JRException
  {
    try {
      return rs.next();
    } catch (SQLException k)
    {
      throw new JRException(k);
    }
  }
  public Object getFieldValue(JRField jRField) throws JRException
  {
    try {
      if (jRField.getName().equals("impLiteral"))
        return Formatear.numToLiteral(rs.getDouble("fvc_sumtot"));
      if (jRField.getName().equals("fvc_sumtot"))
        return Formatear.format(rs.getDouble("fvc_sumtot"),"---,--9.99");
      if (jRField.getName().equals("fvc_fecfra"))
        return rs.getDate("fvc_fecfra");
      return rs.getString(jRField.getName());
    } catch (SQLException k)
    {
      throw new JRException(k);
    }
  }

}
