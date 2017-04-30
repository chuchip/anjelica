package gnu.chu.anjelica.ventas;

import gnu.chu.controles.*;
import java.awt.*;
import java.sql.*;
import gnu.chu.utilidades.*;
import java.awt.event.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>T�tulo: clvenart </p>
 * <p>Descripción: Consulta/Listado Ventas por Cliente y Articulo</p>
 * <p>Copyright: Copyright (c) 2005-2016
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
public class clvenart extends ventana implements  JRDataSource
{
  boolean subQuery=false;
  ResultSet rs;
  ResultSet rs1;
  double cantiCl=0,unidCl=0,importeCl=0;
  double cantiT=0,unidT=0,importeT=0;

  String s;
  CPanel Pprinc = new CPanel();
  condBusq PcondBus = new condBusq();
  Cgrid jt = new Cgrid(8);
  CPanel PtipoCons = new CPanel();
  CLabel cLabel1 = new CLabel();
  CComboBox tipoListE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CComboBox agrupadoE = new CComboBox();
  CCheckBox opDesgl = new CCheckBox();
  CButton Baceptar = new CButton("F4 Aceptar",Iconos.getImageIcon("check"));
  CButton BoculPanel = new CButton(Iconos.getImageIcon("insertar"));
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public clvenart(EntornoUsuario eu, Principal p)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo(" Cons./List. Ventas por Articulo");

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

   public clvenart(gnu.chu.anjelica.menu p, EntornoUsuario eu)
   {
     EU = eu;
     vl = p.getLayeredPane();
     setTitulo("Cons./List. Ventas por Articulo");
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
     this.setSize(new Dimension(602, 533));
     this.setVersion("2016-08-07");
    statusBar = new StatusBar(this);
    conecta();
    Pprinc.setLayout(gridBagLayout1);
    PcondBus.setBorder(BorderFactory.createRaisedBevelBorder());
    PcondBus.setMaximumSize(new Dimension(592, 171));
    PcondBus.setMinimumSize(new Dimension(592, 171));
    PcondBus.setPreferredSize(new Dimension(592, 171));
    BoculPanel.setPreferredSize(new Dimension(24,24));
    BoculPanel.setMaximumSize(new Dimension(24,24));
    BoculPanel.setMinimumSize(new Dimension(24,24));
    BoculPanel.setToolTipText("Ocultar Paneles de Condiciones");
    Blistar.setPreferredSize(new Dimension(24,24));
    Blistar.setMaximumSize(new Dimension(24,24));
    Blistar.setMinimumSize(new Dimension(24,24));
    Blistar.setToolTipText("Generar Listado sobre condiciones");

    Pprinc.setInputVerifier(null);
    PtipoCons.setMaximumSize(new Dimension(588, 28));
    PtipoCons.setMinimumSize(new Dimension(588, 28));
    PtipoCons.setPreferredSize(new Dimension(588, 28));
    jt.setMaximumSize(new Dimension(589, 212));
    jt.setMinimumSize(new Dimension(589, 212));
    jt.setPreferredSize(new Dimension(589, 212));
    Baceptar.setBounds(new Rectangle(463, 3, 122, 22));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    opDesgl.setBounds(new Rectangle(362, 3, 101, 18));
    agrupadoE.setBounds(new Rectangle(278, 3, 82, 18));
    cLabel2.setBounds(new Rectangle(191, 3, 83, 18));
    tipoListE.setBounds(new Rectangle(75, 3, 111, 18));
    cLabel1.setBounds(new Rectangle(3, 3, 75, 18));
    statusBar.add(BoculPanel, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                         , GridBagConstraints.EAST,
                                         GridBagConstraints.VERTICAL,
                                         new Insets(0, 5, 0, 0), 0, 0));

    statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0,
                  GridBagConstraints.EAST,
                   GridBagConstraints.VERTICAL,
                   new Insets(0, 5, 0, 0), 0, 0));

    ArrayList v=new ArrayList();
    PtipoCons.setBorder(BorderFactory.createRaisedBevelBorder());
    PtipoCons.setLayout(null);
    cLabel1.setText("Tipo Listado");
    cLabel2.setText("Agrupado Por:");

    opDesgl.setText("Desgl. Partida");
    v.add("Fec./Albar."); // 0
    v.add("Referencia"); //  1
    v.add("Nombre"); // 2
    v.add("Unid.");   // 3
    v.add("Kilos"); // 4
    v.add("Kg.Medio"); // 5
    v.add("Precio"); // 6
    v.add("Importe"); // 7
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{85,50,179,34,67,60,53,79});
    jt.setAlinearColumna(new int[]{1,2,0,2,2,2,2,2});
    jt.setFormatoColumna(3,"--,--9");
    jt.setFormatoColumna(4,"---,--9.99");
    jt.setFormatoColumna(5,"---9.99");
    jt.setFormatoColumna(6,"---9.99");
    jt.setFormatoColumna(7,"--,---,--9.99");
    jt.setAjustarGrid(true);
//    jt.ajustar(false);
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    Pprinc.add(PcondBus,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(PtipoCons,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PtipoCons.add(cLabel1, null);
    PtipoCons.add(tipoListE, null);
    PtipoCons.add(agrupadoE, null);
    PtipoCons.add(cLabel2, null);
    PtipoCons.add(Baceptar, null);
    PtipoCons.add(opDesgl, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   }

   public void iniciarVentana() throws Exception
   {

     Pprinc.setButton(KeyEvent.VK_F4,Baceptar);

     PcondBus.iniciar(dtStat,this,vl,EU);
     PcondBus.ejeIniE.setValorInt(EU.ejercicio);
     PcondBus.ejeFinE.setValorInt(EU.ejercicio);
     tipoListE.addItem("Cliente/Articulo","C");
     tipoListE.addItem("Articulo","A");

     agrupadoE.addItem("Articulo","P");
     agrupadoE.addItem("Fecha","F");
     agrupadoE.addItem("Albaran","A");
     activarEventos();
   }

   void activarEventos()
   {
     Baceptar.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         Baceptar_actionPerformed();
       }
     });

     BoculPanel.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         BoculPanel_actionPerformed();
       }
     });
     Blistar.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         Blistar_actionPerformed();
       }
     });

   }

   void Baceptar_actionPerformed()
   {
     try
     {
       if (!PcondBus.checkCampos())
       {
         mensaje("");
         this.setEnabled(true);
         return;
       }
     }
     catch (Exception k)
     {
       Error("Error al comprobar validez de campos", k);
     }

     new miThread("xx")
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
     mensaje("Espere ... Buscando Datos");
     this.setEnabled(false);
     jt.panelG.setVisible(false);
     try
     {

       s=getStrSql( PcondBus.getCondWhere(EU));
//       debug(s);
       jt.removeAllDatos();
       if (! dtCon1.select(s))
       {
         mensajeErr("NO encontrados datos para estos criterios");
         mensaje("");
         this.setEnabled(true);
         return;
       }
       int cliCodi=0;
       cantiCl=0;unidCl=0;importeCl=0;
       cantiT=0;unidT=0;importeT=0;

       do
       {
         if (tipoListE.getValor().equals("C"))
         {
           if (cliCodi != dtCon1.getInt("cli_codi"))
           {
             if (cliCodi != 0)
               verTotCli();
             cliCodi = dtCon1.getInt("cli_codi");
             verDatCli();
           }
         }

         ArrayList v= new ArrayList();
         if (agrupadoE.getValor().equals("F"))
           v.add(dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
         if (agrupadoE.getValor().equals("A"))
           v.add(Formatear.format(dtCon1.getInt("avc_ano"),"9999").substring(2,4)+"/"+
                 Formatear.format(dtCon1.getString("emp_codi"),"99")+"/"+
                 dtCon1.getString("avc_serie")+"-"+
                 Formatear.format(dtCon1.getString("avc_nume"),"99999"));
         if (agrupadoE.getValor().equals("P"))
           v.add("");
         v.add(dtCon1.getString("pro_codi"));
         v.add(dtCon1.getString("pro_nomb"));
         v.add(dtCon1.getString("avl_unid"));
         v.add(dtCon1.getString("avl_canti"));
         v.add(dtCon1.getDouble("avl_canti")/dtCon1.getInt("avl_unid"));
         v.add(dtCon1.getDouble("importe")/dtCon1.getDouble("avl_canti"));
         v.add(""+dtCon1.getDouble("importe"));
         jt.addLinea(v);
         if (tipoListE.getValor().equals("C"))
         {
           unidCl += dtCon1.getInt("avl_unid");
           cantiCl += dtCon1.getDouble("avl_canti");
           importeCl += dtCon1.getDouble("importe");
         }
         else
         {
           unidT += dtCon1.getInt("avl_unid");
           cantiT += dtCon1.getDouble("avl_canti");
           importeT += dtCon1.getDouble("importe");
         }
         if (opDesgl.isSelected())
         {
           s=getStrSubQuery(dtCon1.getInt("pro_codi"), tipoListE.getValor().equals("C")?
                            dtCon1.getInt("cli_codi"):0,
                 agrupadoE.getValor().equals("F")?dtCon1.getDate("avc_fecalb"):null,
                 agrupadoE.getValor().equals("A")?dtCon1.getInt("avc_ano"):0,
               agrupadoE.getValor().equals("A")?dtCon1.getInt("emp_codi"):0,
               agrupadoE.getValor().equals("A")?dtCon1.getString("avc_serie"):"",
               agrupadoE.getValor().equals("A")?dtCon1.getInt("avc_nume"):0);

           if (dtStat.select(s))
           {
             do
             {
               ArrayList v0=new ArrayList();
               v0.add("");
               v0.add("");
               v0.add("DESGLOSE");
               v0.add("L");
               v0.add(dtStat.getString("avp_numpar"));
               v0.add(dtStat.getString("avp_numind"));
               v0.add(dtStat.getString("avp_numuni"));
               v0.add(dtStat.getString("avp_canti"));
               jt.addLinea(v0);
             } while (dtStat.next());
           }
         }
       } while (dtCon1.next());
       if (tipoListE.getValor().equals("C"))
         verTotCli();
       ArrayList v=new ArrayList();
       v.add("");
       v.add("");
       v.add("TOTAL GENERAL");
       v.add(unidT);
       v.add(cantiT);
       v.add(cantiT/unidT);
       v.add(importeT/cantiT);
       v.add(importeT);
       jt.addLinea(v);
       mensaje("");
       mensajeErr("Consulta ... REALIZADA");
     } catch (Exception k)
     {
       Error("Error al Buscar datos",k);
     }
     jt.panelG.setVisible(true);
     this.setEnabled(true);

   }

  private String getStrSql(String condWhere)
  {
    String s="SELECT "+(tipoListE.getValor().equals("C")?"a.cli_codi,":"")+
        "l.pro_codi,p.pro_nomb,sum(avl_canti) as avl_canti,"+
        " sum(avl_unid) as avl_unid,sum(avl_prbase*avl_canti) as importe ";
    if (agrupadoE.getValor().equals("F"))
      s+=", a.avc_fecalb ";
    if (agrupadoE.getValor().equals("A"))
      s+=", a.avc_ano,a.emp_codi,a.avc_serie,a.avc_nume";
    s+= " FROM v_albavec as a,v_albavel as l,clientes as cl, v_articulo as p "+
        " WHERE p.pro_codi = l.pro_codi "+
        " and cl.cli_codi = a.cli_codi "+
        " and l.avc_ano = a.avc_Ano "+
        " and l.emp_codi = a.emp_codi "+
        " and l.avc_nume = a.avc_nume "+
        " and l.avc_serie = a.avc_serie "+
        " and a.avc_serie != 'X'"+
        " and l.avl_canti != 0 "+condWhere+
        " group by "+(tipoListE.getValor().equals("C")?"a.cli_codi,":"") +"l.pro_codi,p.pro_nomb ";
    if (agrupadoE.getValor().equals("F"))
      s+=", a.avc_fecalb  "+
          " order by "+(tipoListE.getValor().equals("C")?"a.cli_codi,":"")+"a.avc_fecalb,l.pro_codi";
    if (agrupadoE.getValor().equals("A"))
      s += ", a.avc_ano,a.emp_codi,a.avc_serie,a.avc_nume  " +
          " order by "+(tipoListE.getValor().equals("C")?"a.cli_codi,":"")+
          "a.avc_ano,a.emp_codi,a.avc_serie,a.avc_nume,l.pro_codi";
    if (agrupadoE.getValor().equals("P"))
      s+=" order by "+(tipoListE.getValor().equals("C")?"a.cli_codi,":"")+"l.pro_codi";
    return s;
  }

   void verTotCli() throws Exception
   {
     ArrayList v= new ArrayList();
     v.add("");
     v.add("");
     v.add("Total Cliente");
     v.add(""+unidCl);
     v.add(""+cantiCl);
     v.add(""+(cantiCl/unidCl));    
     v.add(""+(importeCl/cantiCl));
     v.add(""+importeCl);
     jt.addLinea(v);
     cantiT+=cantiCl;
     unidT+=unidCl;
     importeT+=importeCl;
     cantiCl=0;
     unidCl=0;
     importeCl=0;
   }

   void verDatCli() throws Exception
   {
     ArrayList v=new ArrayList();
     v.add("Cliente: ");
     v.add(dtCon1.getString("cli_codi"));
     s = "SELECT * from clientes WHERE cli_codi = " + dtCon1.getInt("cli_codi");
     if (dtStat.select(s))
       v.add(dtStat.getString("cli_nomb"));
     else
       v.add("**CLIENTE NO ENCONTRADO**");
    v.add("");
    v.add("");
    v.add("");
    v.add("");
    v.add("");
    jt.addLinea(v);
   }
   void  BoculPanel_actionPerformed()
   {
     PcondBus.setVisible(! PcondBus.isVisible());
     PtipoCons.setVisible(! PtipoCons.isVisible());
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
     rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
     JasperReport jr;
     if (opDesgl.isSelected())
       jr = Listados.getJasperReport(EU,"veartclde");
     else
       jr = Listados.getJasperReport(EU,"veartcl");
     java.util.HashMap mp = Listados.getHashMapDefault();

     mp.put("tipList","VENTAS POR "+tipoListE.getText()+" ("+ agrupadoE.getText()+")");
     mp.put("opAgrCl",new Boolean(tipoListE.getValor().equals("C")));
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

     if (PcondBus.proIniE.getValorInt() > 0)
       mp.put("proIni",
              PcondBus.proIniE.getText() + " - " +
              PcondBus.proIniE.getTextNomb());
     if (PcondBus.proFinE.getValorInt() > 0)
       mp.put("proFin",PcondBus.proFinE.getText() + " - " +
              PcondBus.proFinE.getTextNomb());

     mp.put("cli_zonrep",PcondBus.zon_codiE.getText());
     mp.put("cli_zoncre",PcondBus.rep_codiE.getText());
     mp.put("cli_activ",PcondBus.cli_activE.getText());
     mp.put("cli_giro",PcondBus.cli_giroE.getText());


    mp.put("proClas",PcondBus.cam_codiE.getText());
    mp.put("proMaCa",PcondBus.pro_artconE.getText());
    mp.put("SUBREPORT_FILE_NAME",EU.pathReport +"/veartdes.jasper");
    subQuery=false;
    JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
    gnu.chu.print.util.printJasper(jp, EU);
       mensaje("");
       mensajeErr("Listado ... GENERADO");
     } catch (SQLException | JRException | PrinterException k)
     {
       Error("Error al imprimir",k);
     }
     this.setEnabled(true);
   }

   public boolean next() throws JRException{
     try {
       if (subQuery)
       {
         if (rs1.next())
           return true;
         subQuery=false;
         return false;
       }
       return rs.next();
     } catch (SQLException k)
     {
       throw new JRException(k);
     }
   }
  public Object getFieldValue(JRField jRField) throws JRException
  {
    try {
      String campo=jRField.getName();
      if (campo.equals("pro_codi") || campo.equals("avl_unid") )
        return new Integer(rs.getInt(campo));
      if (campo.equals("avl_canti") )
        return new Double(rs.getDouble(campo));
      if (campo.equals("avl_prven") )
        return new Double(rs.getDouble("importe")/rs.getDouble("avl_canti"));

      if (campo.equals("avl_kgmed") )
        return new Double(rs.getDouble("avl_canti")/rs.getInt("avl_unid"));
      if (campo.equals("avl_impor") )
        return new Double(rs.getDouble("importe"));

      if (campo.equals("cli_codi")  )
      {
        if (tipoListE.getValor().equals("C"))
          return new Integer(rs.getInt(campo));
        else
          return null;
      }
       if (campo.equals("pro_nomb"))
         return rs.getString("pro_nomb");
       if (campo.equals("cli_nomb"))
       {
         if (!tipoListE.getValor().equals("C"))
          return null;
         s = "SELECT * from clientes WHERE cli_codi = " + rs.getInt("cli_codi");
         if (dtStat.select(s))
          return dtStat.getString("cli_nomb");
         else
           return "**CLIENTE NO ENCONTRADO**";
       }
       if (campo.equals("fec_alb"))
       {
         if (agrupadoE.getValor().equals("F"))
           return Formatear.formatearFecha(rs.getDate("avc_fecalb"), "dd-MM-yyyy");
         else if (agrupadoE.getValor().equals("A"))
           return Formatear.format(rs.getInt("avc_ano"),"9999").substring(2,4)+"/"+
                 Formatear.format(rs.getString("emp_codi"),"99")+"/"+
                 rs.getString("avc_serie")+"-"+
                 Formatear.format(rs.getString("avc_nume"),"99999");
         else
           return "";
       }
       if (campo.equals("strSql"))
       {
         subQuery=true;
         s=getStrSubQuery(rs.getInt("pro_codi"),tipoListE.getValor().equals("C")?
                          rs.getInt("cli_codi"):0,
                          agrupadoE.getValor().equals("F")?rs.getDate("avc_fecalb"):null,
                          agrupadoE.getValor().equals("A")?rs.getInt("avc_ano"):0,
                        agrupadoE.getValor().equals("A")?rs.getInt("emp_codi"):0,
                        agrupadoE.getValor().equals("A")?rs.getString("avc_serie"):"",
                        agrupadoE.getValor().equals("A")?rs.getInt("avc_nume"):0);
         rs1=ct.createStatement().executeQuery(dtCon1.getStrSelect(s));
       }
    
       if (campo.equals("avp_numpar") || campo.equals("avp_numind") || campo.equals("avp_numuni"))
         return rs1.getInt(campo);
       if (campo.equals("avp_canti") )
         return rs1.getDouble(campo);
      return null;
    } catch (SQLException k)
    {
     SystemOut.print(k);
     return null;
    }
  }
  String getStrSubQuery(int proCodi,int cliCodi,java.sql.Date avcFecalb,int avcAno,int empCodi,String avcSerie,
                        int avcNume)
  {
    String sql="select ap.* from v_albvenpar as ap,v_albavec as a,v_albavel as l, clientes as cl,"+
        " v_articulo as p "+
            " where ap.avc_ano = l.avc_ano "+
            " and ap.emp_codi = l.emp_Codi "+
            " and ap.avc_serie = l.avc_serie "+
            " AND ap.avc_nume = l.avc_nume "+
            " and ap.avl_numlin = l.avl_numlin  "+
            " and a.avc_ano = l.avc_ano "+
            " and a.emp_codi = l.emp_Codi "+
            " and a.avc_serie = l.avc_serie "+
            " and a.avc_serie != 'X'"+
            " AND a.avc_nume = l.avc_nume "+
            " and cl.cli_codi = a.cli_codi "+
            " and l.pro_codi = "+proCodi+
            " and p.pro_codi = l.pro_codi ";
        if (tipoListE.getValor().equals("C"))
          sql+=" AND a.cli_codi = "+cliCodi;
         if (agrupadoE.getValor().equals("F"))
           sql+= " and a.avc_fecalb = TO_DATE('"+Formatear.formatearFecha(avcFecalb, "dd-MM-yyyy")+"','dd-MM-yyyy')";
         if (agrupadoE.getValor().equals("A"))
           sql+=" and a.avc_ano = "+avcAno+
               " and a.emp_codi = "+empCodi+
               " and a.avc_serie = '"+avcSerie+"'"+
               " and a.avc_nume = "+avcNume;
        sql+= PcondBus.getCondWhere(EU);
    return sql;

  }
}


