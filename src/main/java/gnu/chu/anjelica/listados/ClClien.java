package gnu.chu.anjelica.listados;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * <p>Titulo: ClClien </p><p>Consulta/Listado de Maestro de Clientes.
 *  </p>
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 2.0
 */
public class ClClien extends ventana
{

   public ClClien(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./Listado  Clientes");

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

  public ClClien(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./Listado  Clientes");
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

      this.setVersion("2017-02-24");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(697,480));
     
      Pcabe.setDefButton(Baceptar.getBotonAccion());
  }
  @Override
  public void iniciarVentana() throws Exception
  {
      zon_codiE.setFormato(Types.CHAR, "XX", 2);
      zon_codiE.texto.setMayusc(true);
      gnu.chu.anjelica.pad.pdclien.llenaDiscr(dtStat, zon_codiE, "Cz", EU.em_cod);
      
      zon_codiE.addDatos("**", "TODOS");
      zon_codiE.setText("**");
      pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
      rut_codiE.setCeroIsNull(true);
      rep_codiE.setFormato(Types.CHAR, "XX", 2);
      rep_codiE.texto.setMayusc(true);
      gnu.chu.anjelica.pad.MantRepres.llenaLinkBox(rep_codiE,dtCon1);
      rep_codiE.addDatos("**", "TODOS");
      rep_codiE.setText("**");
      Baceptar.addMenu("Consultar");
      Baceptar.addMenu("Listar");
      Baceptar.addMenu("Ficha");
      activarEventos();
  }
  void activarEventos()
  {
      Baceptar.addActionListener(new ActionListener()
      {
            @Override
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("Listar"))          
            Blista_actionPerformed(e);
          else 
          {
              if (e.getActionCommand().equals("Ficha"))          
                  imprimirFicha();
              else
                 Bcons_actionPerformed();
          }
        }
      });
      
    }

    String getStrSql()
    {
      String s = "SELECT cl.*,crc_nomb as cli_zoncren FROM clientes as cl left join  v_credclien  as d " +
          "  on  crc_codi=cli_zoncre" +
          " WHERE 1= 1 ";
      if (opEmail.isSelected())
          s+=" and ((cli_email1 is not null and cli_email1 != '' ) or (cli_email2 is not null and cli_email2!='')) ";
      if (!rep_codiE.isNull(true) && !rep_codiE.getText().equals("**") && !rep_codiE.getText().equals("*"))
        s += " and rep_codi  LIKE '" + Formatear.reemplazar(rep_codiE.getText(), "*", "%") + "'";
      if (!zon_codiE.isNull(true) && !zon_codiE.getText().equals("**") && !zon_codiE.getText().equals("*"))
        s += " and zon_codi  LIKE '" + Formatear.reemplazar(zon_codiE.getText(), "*", "%") + "'";
      if (! rut_codiE.isNull())
        s += " and cl.rut_codi  LIKE '" + Formatear.reemplazar(rut_codiE.getText(), "*", "%") + "'";
      if (!fecvenE.isNull())
        s+=" and cl.cli_codi in (select distinct(cli_codi) from v_albavec where avc_fecalb>= TO_DATE('" + 
            fecvenE.getText() + "','dd-MM-yyyy'))"; 
      s += cli_servirE.getValor().equals("%")?"": " and cli_servir  = " + cli_servirE.getValor();
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
        String s = getStrSql();       
        
        if (!dtCon1.select(s))
        {
          mensajeErr("No encontrados clientes con estos Criterios");
          jt.removeAllDatos();
          jtRies.removeAllDatos();
          zon_codiE.requestFocus();
          mensaje("");
          this.setEnabled(true);
          return;
        }
        int nCli = 0;
        jt.scrPanel.setVisible(false);
        jtRies.scrPanel.setVisible(false);
        jt.removeAllDatos();
        jtRies.removeAllDatos();
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
          v.add(dtCon1.getFecha("cli_feulve", "dd-MM-yyyy"));
          jt.addLinea(v);
          v.clear();
          v.add(dtCon1.getString("cli_codi"));
          v.add(dtCon1.getString("cli_nomb"));
          v.add(dtCon1.getString("cli_nomco"));
          v.add(dtCon1.getString("cli_direc"));
          v.add(dtCon1.getString("cli_pobl"));
          v.add(dtCon1.getString("cli_codpo"));
          v.add(dtCon1.getString("cli_nif"));
          v.add(dtCon1.getDouble("cli_riesg"));
          v.add(dtCon1.getString("cli_zoncren"));
          v.add(dtCon1.getString("cli_activ"));        
          jtRies.addLinea(v);
       
        }    while (dtCon1.next());
        jt.scrPanel.setVisible(true);
        jtRies.scrPanel.setVisible(true);
//        numCliE.setValorDec(nCli);
        jt.setEnabled(true);
        jt.requestFocusInicio();
        msgBox(nCli+" Cumplen las condiciones introducidas");
        mensaje("");
        this.setEnabled(true);
        mensajeErr("Consulta ... Realizada");
      }
      catch (SQLException k)
      {
        Error("Error al Buscar Datos", k);
      }
    }
  void imprimirFicha()
  {
       try
       {
           if (!checkCondiciones())
               return;
           mensaje("Espere ... generando Listado");
           String s=getStrSql();
           dtCon1.setStrSelect(s);
           ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());
           JasperReport jr;
           jr = Listados.getJasperReport(EU, "fichaCliente");
           
           java.util.HashMap mp = new java.util.HashMap();
           mp.put("detalleReport",EU.pathReport +"/fichaClienteDet.jasper");
           mp.put("subReport",EU.pathReport +"/fichaClienteAlb.jasper");
           mp.put(JRParameter.REPORT_CONNECTION,ct);
           JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
           if (!gnu.chu.print.util.printJasper(jp, EU)) {
               mensaje("");
               this.setEnabled(true);
               return;
           }
           mensaje("");
       } catch (SQLException | JRException | PrinterException ex)
       {
         Error("Error al imprimir ficha clientes",ex);
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
        String s=getStrSql();
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel16 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel17 = new gnu.chu.controles.CLabel();
        cLabel18 = new gnu.chu.controles.CLabel();
        cli_activE = new gnu.chu.controles.CComboBox();
        cLabel19 = new gnu.chu.controles.CLabel();
        fealinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel20 = new gnu.chu.controles.CLabel();
        fealfiE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel21 = new gnu.chu.controles.CLabel();
        tipolistE = new gnu.chu.controles.CComboBox();
        cLabel22 = new gnu.chu.controles.CLabel();
        ordenE = new gnu.chu.controles.CComboBox();
        opEmail = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        cLabel23 = new gnu.chu.controles.CLabel();
        fecvenE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel24 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel25 = new gnu.chu.controles.CLabel();
        cli_servirE = new gnu.chu.controles.CComboBox();
        CTabed = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.Cgrid(7);
        jtRies = new gnu.chu.controles.Cgrid(10);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(580, 90));
        Pcabe.setPreferredSize(new java.awt.Dimension(580, 90));
        Pcabe.setLayout(null);

        cLabel16.setText("Tipo Listado ");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel16);
        cLabel16.setBounds(2, 44, 80, 17);

        zon_codiE.setAncTexto(30);
        zon_codiE.setMayusculas(true);
        zon_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(80, 2, 210, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setMayusculas(true);
        rep_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(360, 2, 210, 18);

        cLabel17.setText("De Fecha Alta");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel17);
        cLabel17.setBounds(210, 24, 90, 17);

        cLabel18.setText("Ventas Desde ");
        cLabel18.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel18);
        cLabel18.setBounds(0, 65, 80, 17);

        cli_activE.addItem("Si","S");
        cli_activE.addItem("NO", "N");
        cli_activE.addItem("TODOS","%");
        Pcabe.add(cli_activE);
        cli_activE.setBounds(80, 24, 80, 17);

        cLabel19.setText("Repres");
        cLabel19.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel19);
        cLabel19.setBounds(310, 2, 40, 18);
        Pcabe.add(fealinE);
        fealinE.setBounds(300, 24, 70, 17);

        cLabel20.setText("A Fecha Alta");
        cLabel20.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel20);
        cLabel20.setBounds(410, 24, 90, 17);
        Pcabe.add(fealfiE);
        fealfiE.setBounds(500, 24, 70, 17);

        cLabel21.setText("Activo");
        cLabel21.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel21);
        cLabel21.setBounds(2, 24, 40, 17);

        tipolistE.addItem("General","N");
        tipolistE.addItem("Credito","C");
        tipolistE.addItem("EMail","E");
        Pcabe.add(tipolistE);
        tipolistE.setBounds(80, 44, 120, 17);

        cLabel22.setText("Ordenar");
        cLabel22.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel22);
        cLabel22.setBounds(210, 44, 60, 17);

        ordenE.addItem("Cod.Cliente","cli_codi");
        ordenE.addItem("Nombre","cli_nomb");
        ordenE.addItem("Poblacion","cli_pobl,cli_nomb");
        ordenE.addItem("Cod.Postal","cli_codpo,cli_nomb");
        ordenE.addItem("Est.Cred","cli_zoncre,cli_codi");
        Pcabe.add(ordenE);
        ordenE.setBounds(270, 44, 120, 17);

        opEmail.setText("Con Email");
        Pcabe.add(opEmail);
        opEmail.setBounds(150, 65, 80, 17);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(480, 63, 90, 26);

        cLabel23.setText("Zona");
        cLabel23.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel23);
        cLabel23.setBounds(2, 2, 40, 18);
        Pcabe.add(fecvenE);
        fecvenE.setBounds(80, 65, 70, 17);

        cLabel24.setText("Ruta");
        cLabel24.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel24);
        cLabel24.setBounds(240, 65, 30, 18);

        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(270, 65, 210, 18);

        cLabel25.setText("Servir");
        cLabel25.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel25);
        cLabel25.setBounds(430, 44, 40, 17);

        cli_servirE.addItem("TODOS","%");
        cli_servirE.addItem("Si","1");
        cli_servirE.addItem("No","0");
        cli_servirE.addItem("No!","2");
        Pcabe.add(cli_servirE);
        cli_servirE.setBounds(490, 44, 80, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        CTabed.setMaximumSize(new java.awt.Dimension(512, 240));
        CTabed.setMinimumSize(new java.awt.Dimension(512, 240));
        CTabed.setPreferredSize(new java.awt.Dimension(512, 240));

        ArrayList v = new ArrayList();
        v.add("Cod."); // 0
        v.add("Nombre"); // 1
        v.add("Poblacion"); // 2
        v.add("CP"); // 3
        v.add("Activ"); //4
        v.add("Fec.Alta"); //5
        v.add("Fe.Ul.Ve"); //6
        jt.setCabecera(v);
        jt.setMaximumSize(new Dimension(444, 208));
        jt.setMinimumSize(new Dimension(444, 208));
        jt.setPreferredSize(new Dimension(444, 208));
        jt.setAnchoColumna(new int[]
            {40, 160, 120, 50, 40,80,80});
        jt.setAlinearColumna(new int[]
            {2, 0, 0, 2, 1,1,2});
        jt.setAjustarGrid(true);
        CTabed.addTab("General", jt);

        ArrayList v1=new ArrayList();
        v1.add("Cod."); // 0
        v1.add("Nombre Fiscal"); // 1
        v1.add("Nombre Comercial"); // 2
        v1.add("Direccion"); // 3
        v1.add("Poblacion"); // 4
        v1.add("CP"); // 5
        v1.add("NIF"); // 6
        v1.add("Credito"); // 7
        v1.add("Estado"); //8
        v1.add("Activ"); //9

        jtRies.setCabecera(v1);
        jtRies.setFormatoColumna(7,"--,---,--9.99");
        jtRies.setAnchoColumna(new int[]
            {40, 130,130,120, 120, 50, 90,90,150,40});
        jtRies.setAlinearColumna(new int[]
            {2, 0, 0, 0,0,2,2,2,0, 1});
        CTabed.addTab("Riesgo", jtRies);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(CTabed, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CTabbedPane CTabed;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel24;
    private gnu.chu.controles.CLabel cLabel25;
    private gnu.chu.controles.CComboBox cli_activE;
    private gnu.chu.controles.CComboBox cli_servirE;
    private gnu.chu.controles.CTextField fealfiE;
    private gnu.chu.controles.CTextField fealinE;
    private gnu.chu.controles.CTextField fecvenE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtRies;
    private gnu.chu.controles.CCheckBox opEmail;
    private gnu.chu.controles.CComboBox ordenE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.controles.CComboBox tipolistE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables
}
