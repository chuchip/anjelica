package gnu.chu.anjelica.listados;

import gnu.chu.Menu.Principal;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
/**
 * Consulta y Listado Maestro del Clientes
 *
 * Tambien permite delimitar por proveedor y por albarán.
 *  <p>Copyright: Copyright (c) 2005-2012<p>
 * <p>
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
 *  Para sacar los clientes con sus correos correspondientes ejecutar la sentencia SQL:
 * <p>
 *  select cli_codi as codigo,cli_Activ as activo,cli_nomb as nombre, cli_nomco as nombre_comercial, 
 *  cli_pobl as poblacion, cli_email1 as email1,cli_email2 as email2 ,cli_comen as comentario 
 *   from clientes  order by cli_activ desc,cli_nomb
 * </p>
 * </p>
 * @author chuchiP
 */

public class lisclien extends ventana
{
  String s;
  CPanel Pprinc = new CPanel();
  CButton Blista = new CButton(Iconos.getImageIcon("print"));
  CLabel discrim1L = new CLabel();
  CLinkBox zon_codiE = new CLinkBox();
  CComboBox ordenE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  Cgrid jt = new Cgrid(6);
  CCheckBox opEmail=new CCheckBox();
  CButton Bcons = new CButton();
  CPanel Pcabe = new CPanel();
  CPanel Presum = new CPanel();
  CLabel cLabel3 = new CLabel();
  CTextField numCliE = new CTextField(Types.DECIMAL,"###9");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel discrim3L = new CLabel();
  CComboBox cli_activE = new CComboBox();
  CLabel discrim2L = new CLabel();
  CLinkBox rep_codiE = new CLinkBox();
  CLabel cLabel5 = new CLabel();
  CTextField fealinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField fealfiE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel fealfiL = new CLabel();
  CLabel cLabel1 = new CLabel();
  CComboBox tipolistE = new CComboBox();

  public lisclien(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Consulta/Listado Clientes");

    try
    {
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

  public lisclien(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta/Listado Clientes");

    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e); 
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(600, 530));
    this.setVersion("2013-11-15");
    Pprinc.setLayout(gridBagLayout1);
    conecta();
    Blista.setBounds(new Rectangle(166, 3, 122, 20));
    Blista.setMargin(new Insets(0, 0, 0, 0));
    Blista.setText("Listar Clientes");

    discrim1L.setText("Zona");
    discrim1L.setBounds(new Rectangle(6, 2, 73, 18));
    zon_codiE.setAncTexto(30);
    zon_codiE.setBounds(new Rectangle(83, 2, 222, 18));
    ordenE.setBounds(new Rectangle(243, 48, 117, 16));
    cLabel2.setForeground(Color.black);
    cLabel2.setText("Ordenar");
    cLabel2.setBounds(new Rectangle(187, 48, 49, 16));
    statusBar = new StatusBar(this);
    Bcons.setBounds(new Rectangle(490, 42, 88, 24));
    Bcons.setMargin(new Insets(0, 0, 0, 0));
    Bcons.setText("Consultar");
    opEmail.setText("Con email");
    opEmail.setBounds((new Rectangle(380, 48, 88, 16)));
    opEmail.setMargin(new Insets(0, 0, 0, 0));
    
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(582, 71));
    Pcabe.setMinimumSize(new Dimension(582, 71));
    Pcabe.setOpaque(true);
    Pcabe.setPreferredSize(new Dimension(582, 71));
    Pcabe.setLayout(null);
    ArrayList v = new ArrayList();
    v.add("Cod."); // 0
    v.add("Nombre"); // 1
    v.add("Poblacion"); // 2
    v.add("CP"); // 3
    v.add("Activ"); //4
    v.add("Fec.Alta"); //5
    jt.setCabecera(v);
    jt.setMaximumSize(new Dimension(444, 208));
    jt.setMinimumSize(new Dimension(444, 208));
    jt.setPreferredSize(new Dimension(444, 208));
    jt.setAnchoColumna(new int[]
                       {40, 160, 120, 50, 40,80});
    jt.setAlinearColumna(new int[]
                         {2, 0, 0, 2, 1,1});
    jt.setAjustarGrid(true);
    Presum.setBorder(BorderFactory.createLoweredBevelBorder());
    Presum.setMaximumSize(new Dimension(303, 29));
    Presum.setMinimumSize(new Dimension(303, 29));
    Presum.setPreferredSize(new Dimension(303, 29));
    Presum.setLayout(null);
    cLabel3.setBounds(new Rectangle(10, 4, 67, 16));
    cLabel3.setText("N.Clientes");
    numCliE.setBounds(new Rectangle(82, 4, 34, 18));
    numCliE.setEnabled(false);
    numCliE.setTipoCampo(1);
    discrim3L.setText("Activo");
    discrim3L.setBounds(new Rectangle(6, 24, 73, 18));
    cli_activE.setBounds(new Rectangle(84, 22, 68, 18));
    discrim2L.setText("Repres");
    discrim2L.setBounds(new Rectangle(308, 2, 65, 18));
    rep_codiE.setBounds(new Rectangle(368, 2, 212, 18));
    rep_codiE.setAncTexto(30);
    cLabel5.setText("De Fecha Alta ");
    cLabel5.setBounds(new Rectangle(220, 22, 86, 18));
    fealinE.setBounds(new Rectangle(302, 22, 70, 18));
    fealfiE.setBounds(new Rectangle(508, 21, 70, 18));

    fealfiL.setBounds(new Rectangle(429, 21, 77, 18));
    fealfiL.setText("A Fecha Alta ");
    cLabel1.setText("Tipo Listado");
    cLabel1.setBounds(new Rectangle(2, 48, 78, 16));

    tipolistE.setBounds(new Rectangle(76, 48, 107, 16));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.add(Pcabe, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                             new Insets(2, 0, 0, 0), 0, 0));
    Pcabe.add(zon_codiE, null);
    Pcabe.add(discrim2L, null);
    Pcabe.add(rep_codiE, null);
    Pcabe.add(discrim3L, null);
    Pcabe.add(discrim1L, null);
    Pcabe.add(fealinE, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(cli_activE, null);
    Pcabe.add(fealfiE, null);
    Pcabe.add(fealfiL, null);
    Pcabe.add(Bcons, null);
    Pcabe.add(opEmail,null);
    Pcabe.add(ordenE, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(tipolistE, null);
    Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0),
                                          0, 0));
    Presum.add(numCliE, null);
    Presum.add(cLabel3, null);
    Presum.add(Blista, null);
    Pprinc.add(Presum,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -1, 0));
  }
    @Override
    public void iniciarVentana() throws Exception
    {
      zon_codiE.setFormato(Types.CHAR, "XX", 2);
      zon_codiE.texto.setMayusc(true);
      gnu.chu.anjelica.pad.pdclien.llenaDiscr(dtStat, zon_codiE, "Cz", EU.em_cod);

      zon_codiE.addDatos("**", "TODOS");
      zon_codiE.setText("**");

      rep_codiE.setFormato(Types.CHAR, "XX", 2);
      rep_codiE.texto.setMayusc(true);
      gnu.chu.anjelica.pad.MantRepres.llenaLinkBox(rep_codiE,dtCon1);
      rep_codiE.addDatos("**", "TODOS");
      rep_codiE.setText("**");

      ordenE.addItem("Cod.Cliente","cli_codi");
      ordenE.addItem("Nombre","cli_nomb");
      ordenE.addItem("Poblacion","cli_pobl,cli_nomb");
      ordenE.addItem("Cod.Postal","cli_codpo,cli_nomb");
      cli_activE.addItem("Si","S");
      cli_activE.addItem("NO", "N");
      cli_activE.addItem("TODOS","%");
      tipolistE.addItem("General","N");
      tipolistE.addItem("Credito","C");
      tipolistE.addItem("EMail","E");

      activarEventos();
//      Blista.doClick();
    }

    void activarEventos()
    {
      Blista.addActionListener(new ActionListener()
      {
            @Override
        public void actionPerformed(ActionEvent e)
        {
          Blista_actionPerformed(e);
        }
      });
      Bcons.addActionListener(new ActionListener()
      {
            @Override
        public void actionPerformed(ActionEvent e)
        {
          Bcons_actionPerformed();
        }
      });
    }

    String getStrSql()
    {
      s = "SELECT * FROM clientes " +
          " WHERE 1= 1 ";
      if (opEmail.isSelected())
          s+=" and ((cli_email1 is not null and cli_email1 != '' ) or (cli_email2 is not null and cli_email2!='')) ";
      if (!rep_codiE.isNull(true) && !rep_codiE.getText().equals("**") && !rep_codiE.getText().equals("*"))
        s += " and rep_codi  LIKE '" + Formatear.reemplazar(rep_codiE.getText(), "*", "%") + "'";
      if (!zon_codiE.isNull(true) && !zon_codiE.getText().equals("**") && !zon_codiE.getText().equals("*"))
        s += " and zon_codi  LIKE '" + Formatear.reemplazar(zon_codiE.getText(), "*", "%") + "'";
      s += " and cli_activ  LIKE '" + cli_activE.getValor() + "'" +
          (fealinE.isNull() ? "" : " and cli_fecalt >= TO_DATE('" + fealinE.getText() + "','dd-MM-yyyy')") +
          (fealfiE.isNull() ? "" : " and cli_fecalt <= TO_DATE('" + fealfiE.getText() + "','dd-MM-yyyy')") +
          " ORDER BY " + ordenE.getValor();
      return s;
    }
    boolean checkCondiciones()
    {
//      if (! rep_codiE.controla())
//      {
//        mensajeErr("Zona/Credito NO valida");
//        return false;
//      }
//      if (!zon_codiE.controla())
//      {
//        mensajeErr("Zona NO valida");
//        return false;
//      }
      if (fealinE.getError())
      {
        mensajeErr("Fecha Alta Inicial ... NO VALIDA");
        return false;
      }
      if (fealfiE.getError())
      {
        mensajeErr("Fecha Alta Final ... NO VALIDA");
        return false;
      }
      return true;

    }

    void Bcons_actionPerformed()
    {
      if (!checkCondiciones())
        return;
      new miThread("")
      {
            @Override
        public void run()
        {
         jt.tableView.setVisible(false);
         consultar();
         jt.tableView.setVisible(true);
        }
      };
    }
    void consultar()
    {
      this.setEnabled(false);
      mensaje("Espere .... buscando datos");
      try
      {
        s = getStrSql();
        jt.setEnabled(false);
       
        jt.removeAllDatos();
        if (!dtCon1.select(s))
        {
          mensajeErr("No encontrados clientes con estos Criterios");
          zon_codiE.requestFocus();
          mensaje("");
          this.setEnabled(true);
          return;
        }
        int nCli = 0;
        do
        {
          nCli++;
          ArrayList v = new ArrayList();
          v.add(dtCon1.getString("cli_codi"));
          v.add(dtCon1.getString("cli_nomb"));
          v.add(dtCon1.getString("cli_pobl"));
          v.add(dtCon1.getString("cli_codpo"));
          v.add(dtCon1.getString("cli_activ"));
          v.add(dtCon1.getFecha("cli_fecalt", "dd-MM-yyyy"));
          jt.addLinea(v);
        }
        while (dtCon1.next());
        numCliE.setValorDec(nCli);
        jt.setEnabled(true);
        jt.requestFocusInicio();
        
        mensaje("");
        this.setEnabled(true);
        mensajeErr("Consulta ... Realizada");
      }
      catch (Exception k)
      {
        Error("Error al Buscar Datos", k);
      }
    }

  void Blista_actionPerformed(ActionEvent e) {
      if (!checkCondiciones())
        return;
      new miThread("")
      {
            @Override
        public void run()
        {
          listar();
        }
      };
  }
  void listar()
  {
      try {
        this.setEnabled(false);
        mensaje("Espere ... generando Listado");
        s=getStrSql();
        dtCon1.setStrSelect(s);
        ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());
        JasperReport jr;
        
        switch (tipolistE.getValor())
        {
            case "N":
              jr = gnu.chu.print.util.getJasperReport(EU, "clientes_mayor");
              break;
            case "E":
              jr = gnu.chu.print.util.getJasperReport(EU, "clientes_email");
              break;
            default:
              jr = gnu.chu.print.util.getJasperReport(EU, "credito_clientes");
        }
      
        java.util.HashMap mp = new java.util.HashMap();
        mp.put("zona", zon_codiE.getTextCombo());
        mp.put("cdto", rep_codiE.getTextCombo());
        mp.put("feinal", fealinE.getText());
        mp.put("fefial", fealinE.getText());
        JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
        if (!gnu.chu.print.util.printJasper(jp, EU)) {
                mensaje("");
                this.setEnabled(true);
                return;
        }
        mensaje("");
        this.setEnabled(true);
        mensajeErr("Listado .... generado");
    } catch (SQLException | JRException | PrinterException k)
    {
      Error("Error al generar Listado",k);
    }
  }

}

