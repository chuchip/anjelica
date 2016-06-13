package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
/**
 *
 * <p>Título: clrealve</p>
 * <p>Descripción: Consulta/Listado  Relación Albaranes de Ventas<br>
 * Permite consultar y listar ordenando por fecha y cliente
 * <p>Copyright: Copyright (c) 2005-2015
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class clrealve extends ventana
{
  CPanel Pprinc = new CPanel();
  condBusq PcondBus = new condBusq();
  CButton Bconsulta = new CButton("F4 Consultar",Iconos.getImageIcon("buscar"));
  Cgrid jt = new Cgrid(8);
  CPanel Ppie = new CPanel();
  CLabel cLabel1 = new CLabel();
  CComboBox ordenE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CTextField numAlbE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel3 = new CLabel();
  CTextField impAlbE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public clrealve(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public clrealve(EntornoUsuario eu, Principal p, Hashtable ht)
  {

    EU = eu;
    vl = p.panel1;
    jf = p;
    setTitulo("Cons./List. Relacion Alb. de Ventas");
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

  public clrealve(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./List. Relacion Alb. de Ventas");
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
    this.setSize(new Dimension(652, 484));
    this.setVersion("2012-02-21");
    conecta();
    statusBar = new StatusBar(this);
    Pprinc.setLayout(gridBagLayout1);
    PcondBus.setBorder(BorderFactory.createEtchedBorder());
    PcondBus.setMaximumSize(new Dimension(587, 170));
    PcondBus.setMinimumSize(new Dimension(587, 170));
    PcondBus.setPreferredSize(new Dimension(587, 170));

    ArrayList v=new ArrayList();
    v.add("Emp"); // 0
    v.add("Ejer"); // 1
    v.add("Ser"); // 2
    v.add("Num.Alb"); // 3
    v.add("Fec.Alb"); // 4
    v.add("Cliente"); // 5
    v.add("Nomb.Cliente"); // 6
    v.add("Imp.Alb"); // 7
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{30,30,30,50,90,50,180,80});
    jt.setAlinearColumna(new int[]{2,2,1,2,1,2,0,2});
    jt.setFormatoColumna(7,"----,--9.99");
    jt.setMaximumSize(new Dimension(634, 233));
    jt.setMinimumSize(new Dimension(634, 233));
    jt.setPreferredSize(new Dimension(634, 233));
    jt.setAjustarGrid(true);
    Ppie.setBorder(BorderFactory.createRaisedBevelBorder());
    Ppie.setDebugGraphicsOptions(0);
    Ppie.setMaximumSize(new Dimension(623, 36));
    Ppie.setMinimumSize(new Dimension(623, 36));
    Ppie.setPreferredSize(new Dimension(623, 36));
    Ppie.setLayout(null);
    cLabel1.setText("Ordenar por");
    cLabel1.setBounds(new Rectangle(412, 115, 75, 17));
    cLabel2.setText("Num. Albaranes");
    cLabel2.setBounds(new Rectangle(2, 4, 93, 17));
    numAlbE.setEnabled(false);
    numAlbE.setBounds(new Rectangle(92, 4, 49, 17));
    cLabel3.setText("Imp.Albaranes");
    cLabel3.setBounds(new Rectangle(147, 4, 84, 17));
    impAlbE.setEnabled(false);
    impAlbE.setEjecSonido(false);
    impAlbE.setBounds(new Rectangle(234, 4, 76, 17));
    Bconsulta.setBounds(new Rectangle(444, 139, 132, 24));
    Bconsulta.setMargin(new Insets(0, 0, 0, 0));
    Blistar.setPreferredSize(new Dimension(24, 24));
    Blistar.setMaximumSize(new Dimension(24, 24));
    Blistar.setMinimumSize(new Dimension(24, 24));
    Blistar.setToolTipText("Generar Listado sobre condiciones");

    ordenE.setBounds(new Rectangle(483, 114, 91, 17));
    statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.EAST,
                 GridBagConstraints.VERTICAL,
                 new Insets(0, 5, 0, 0), 0, 0));

    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(PcondBus,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PcondBus.add(Bconsulta, null);
    PcondBus.add(ordenE, null);
    PcondBus.add(cLabel1, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Ppie,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -295, -12));
    Ppie.add(cLabel2, null);
    Ppie.add(numAlbE, null);
    Ppie.add(impAlbE, null);
    Ppie.add(cLabel3, null);
    PcondBus.setVerProd(false);
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    PcondBus.setButton(KeyEvent.VK_F4,Bconsulta);
    PcondBus.iniciar(dtCon1,this,vl,EU);
    ordenE.addItem("Albaran","A");
    ordenE.addItem("Cliente","C");
    PcondBus.requestFocus();
    PcondBus.fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    PcondBus.fecIniE.setText(Formatear.sumaDias(Formatear.getDateAct(),-30));
    activarEventos();
  }

  void activarEventos()
  {
    Bconsulta.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Bconsulta_actionPerformed();
      }
    });
    Blistar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });

  }

  void Bconsulta_actionPerformed()
  {
    try {
      if (!PcondBus.checkCampos())
      {
        mensaje("");
        this.setEnabled(true);
        return;
      }
    } catch (Exception k)
    {
      Error("Error al comprobar datos de entrada",k);
      return;
    }

      new miThread("consulta")
      {
            @Override
        public void run()
        {
          consulta();
        }
      };
    }
    void consulta()
    {
      try {
        String condWhere= PcondBus.getCondWhere(EU);
        String s=getStrSql( condWhere);
        jt.removeAllDatos();
        impAlbE.resetTexto();
        numAlbE.resetTexto();
        if (!dtCon1.select(s))
        {
            mensajeErr("NO encontrados datos para estos criterios");
            mensaje("");
            this.setEnabled(true);
            return;
        }
        jt.setDatos(dtCon1);
        s="SELECT sum(avc_impalb) as avc_impalb,count(*)  as cuantos " +
            "  FROM v_albavec as a,clientes as cl " +
            " WHERE  cl.cli_codi = a.cli_codi "+   condWhere;
        dtStat.select(s);
        impAlbE.setValorDec(dtStat.getDouble("avc_impalb",true));
        numAlbE.setValorDec(dtStat.getDouble("cuantos",true));

    } catch (Exception k)
    {
      Error("Error al Consultar albaranes",k);
    }
  }
  private String getStrSql(String condWhere)
  {
    return "SELECT a.emp_codi, a.avc_ano, a.avc_serie, a.avc_nume,a.avc_fecalb,a.cli_codi, " +
        " cl.cli_nomb,avc_impalb " +
        "  FROM v_albavec as a,clientes as cl " +
        " WHERE cl.cli_codi = a.cli_codi " +
        condWhere+" ORDER BY  "+   (ordenE.getValor().equals("C")? "a.cli_codi,":"")+
        "a.emp_codi,a.avc_ano,a.avc_serie,a.avc_nume";
  }
  void Blistar_actionPerformed()
  {

    new miThread("jjj")
    {
      public void run()
      {
        listar();
      }
    };
  }

  void listar()
  {
    mensaje("Espere .... generando Listado");
    this.setEnabled(false);
    try
    {
        String sql=getStrSql( PcondBus.getCondWhere(EU));
    //     debug("Sql Princ: "+sql);
        dtCon1.setStrSelect(sql);
        ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
        JasperReport jr;
        jr = Listados.getJasperReport(EU, "realbve");
        java.util.HashMap mp = Listados.getHashMapDefault();
        mp.put("tipList","VENTAS ORDENADO POR "+ordenE.getText());
        mp.put("opAgrCl", ordenE.getValor().equals("C"));
        if (!PcondBus.cliIniE.isNull())
          mp.put("cliIni",
                 PcondBus.cliIniE.getText() + " - " + PcondBus.cliIniE.getTextNomb());
        if (!PcondBus.cliFinE.isNull())
          mp.put("cliFin",
                 PcondBus.cliFinE.getText() + " - " + PcondBus.cliFinE.getTextNomb());
        if (PcondBus.ejeIniE.getValorDec() != 0)
          mp.put("ejeIni", PcondBus.ejeIniE.getText());
        if (PcondBus.ejeFinE.getValorDec() != 0)
          mp.put("ejeFin", PcondBus.ejeFinE.getText());
        if (!PcondBus.fecIniE.isNull())
          mp.put("fecIni", PcondBus.fecIniE.getText());
        if (!PcondBus.fecFinE.isNull())
          mp.put("fecFin", PcondBus.fecFinE.getText());
        if (PcondBus.empIniE.getValorDec() != 0)
          mp.put("empIni", PcondBus.empIniE.getText());
      

        if (!PcondBus.serieIniE.isNull())
          mp.put("serieIni", PcondBus.serieIniE.getText());
        if (!PcondBus.serieFinE.isNull())
          mp.put("serieFin", PcondBus.serieFinE.getText());

        if (PcondBus.albIniE.getValorInt() > 0)
          mp.put("albIni", PcondBus.albIniE.getText());
        if (PcondBus.albFinE.getValorInt() > 0)
          mp.put("albFin", PcondBus.albFinE.getText());

        mp.put("cli_zonrep",PcondBus.zon_codiE.getText());
        mp.put("cli_zoncre",PcondBus.rep_codiE.getText());
        mp.put("cli_activ",PcondBus.cli_activE.getText());
        mp.put("cli_giro",PcondBus.cli_giroE.getText());



       JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
       mensaje("");
       mensajeErr("Listado ... GENERADO");
    } catch (Exception k)
    {
      Error("Error al imprimir",k);
    }
    this.setEnabled(true);
  }

}
