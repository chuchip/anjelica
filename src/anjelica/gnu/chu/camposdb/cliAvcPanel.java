package gnu.chu.camposdb;


import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdclien;
import java.sql.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.mensajes;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * <p>Título: cliAvcPanel</p>
 * <p>Descripción: Panel para introducir el codigo del cliente con posibilidad
 *  de modificar el nombre. Usado por Mantenimiento de Albaranes y facturas de
 *  ventas.</p>
 * <p>Copyright: Copyright (c) 2006-2016
 * Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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

public class cliAvcPanel extends cliPanel
{
  int avcAno;
  int empCodi;
  String avcSerie;
  int avcNume=0; 
  
  public void setAvcAno(int avc_ano)
  {
    this.avcAno = avc_ano;
  }

  public void setEmpCodi(int emp_codi)
  {
    this.empCodi = emp_codi;
  }

  public void setAvcSerie(String avc_serie)
  {
    this.avcSerie = avc_serie;
  }

  public void setAvcNume(int avc_nume)
  {
    this.avcNume = avc_nume;
  }

  public void setAlbaran(int emp_codi, int avc_ano, String avc_serie, int avc_nume)
  {
    setEmpCodi(emp_codi);
    setAvcAno(avc_ano);
    setAvcSerie(avc_serie);
    setAvcNume(avc_nume);
  }

  public void resetAlbaran()
  {
    setEmpCodi(0);
    setAvcAno(0);
    setAvcSerie("");
    setAvcNume(0);
  }

  public CTextField getCliNomb()
  {
    return cli_nombL;
  }
    @Override
  public boolean controlar(int caso) throws SQLException
  {
    String cliNomb = null;
    String s;
    setMsgError("");
    setError(! checkCond());
    if (avcNume!=0)
    {
      s = "SELECT avc_clinom FROM v_albavec WHERE avc_ano =" + avcAno +
          " and emp_codi = " + empCodi +
          " and avc_serie = '" + avcSerie + "'" +
          " and avc_nume = " + avcNume;
      if (dt.select(s))
      {
        cliNomb = dt.getString("avc_clinom", true);
        if (cliNomb.equals(""))
          cliNomb = null;
      }
    }
    boolean err=controlar1(caso==COMPROBAR?CHECK_NOTEXT:caso,cliNomb);
    if (cli_codiE.isEnabled())
    {
      if (err)
        cli_nombL.setEnabled(getLikeCliente().getInt("cli_gener") != 0);
    }
    return err;
  }

  public void setValorInt(int valor,String nombCli)
  {
    if (nombCli!=null)
    {
      if (nombCli.trim().equals(""))
        nombCli = null;
    }
    cli_codiE.setValorDec(valor);
    try
    {
      controlar(DBSKIP,nombCli);
    }
    catch (Exception k)
    {

    }
    cli_codiE.resetCambio();
  }
  @Override
  public void setEnabled(boolean enab)
  {
    super.setEnabled(enab);

    if (! enab && cli_nombL!=null)
      cli_nombL.setEnabled(false);      

  }
 
}
