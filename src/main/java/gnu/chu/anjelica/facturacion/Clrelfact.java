package gnu.chu.anjelica.facturacion;

/**
*
* <p>Título: Clrelfact</p>
* <p>Descripción: Consulta / Listado Relacion Facturas de Ventas.
*  Tambien tiene una opcion (deshabilitada)
*  para volver a  generar los recibos para giros, esta opcion solo genera un giro por factura, lo
*  cual significa que las formas de pago con mas de un vto. no funcionan correctamente.
 * <p>Copyright: Copyright (c) 2005-2018
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
* @author chuchiP
* @version 1.1
*/
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.anjelica.riesgos.clFactCob;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.mail.IFMail;
import gnu.chu.mail.MailHtml;
import gnu.chu.print.util;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Locale;
import net.sf.jasperreports.engine.*;


public class Clrelfact extends ventana implements JRDataSource 
{
  final static String PCORREORELFRA="correorelfra"; 
  String subject;
  String emailCC;
  String asunto,toEmail;
  IFMail ifMail=new IFMail();
  Locale lengua=ejecutable.local;
  boolean swCreateTable=false;
  String TABLA_TMP="lirelfac_tmp1";
  final  static  String LISTADO="L";
  final  static  String EXCEL="E";
  final static String CONSULTA="C";
  final static String CORREO="M";
  JasperReport jr;
  int nLin=0;
  int maxN=0;
  int maxL=500;
  double imp[];
  int nfras[];

  String accion;
   ResultSet rs;
  String s;
    
    public Clrelfact(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons/List. Relacion de Facturas");

        try
        {
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            ErrorInit(e);
            setErrorInit(true);
        }
  }

  public Clrelfact(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

    EU=eu;
    vl=p.getLayeredPane();
    setTitulo("Cons/List. Relacion de Facturas");

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
    iniciarFrame();

    this.setVersion("2018-05-04");
    statusBar = new StatusBar(this);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    conecta();

    initComponents();
    this.setSize(new Dimension(730, 530));
  }
     @Override
  public void iniciarVentana() throws Exception
  {
    imp=new double[maxL];
    nfras=new int[maxL];
    ifMail.setVisible(false);
    ifMail.setIconifiable(false);
    ifMail.iniciar(this);
    ifMail.setEmail(EU.getValorParam(PCORREORELFRA, "",dtCon1));
    ifMail.setLocation(this.getLocation().x+30,this.getLocation().x+30);
    vl.add(ifMail,new Integer(1));
    intfechaE.setValorInt(30);
   
//    if (ct.getDriverType()==ct.MSQL)
//        TABLA_TMP="#"+TABLA_TMP;

    PcondBus.iniciar(dtStat,this,vl,EU);
    PcondBus.feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -7));
    PcondBus.fecfinE.setText(Fecha.getFechaSys("dd-MM-yyyy"));

    activarEventos();
    PcondBus.feciniE.requestFocus();
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Baceptar_actionPerformed(Baceptar.getValor(e.getActionCommand()));
      }
    });
//    Bgengiros.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        Bgengiros_actionPerformed();
//      }
//    });

//    Bexport.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        Baceptar_actionPerformed(EXCEL);
//      }
//    });
  }

  void Bgengiros_actionPerformed()
  {
    try
    {
      if (!PcondBus.checkCampos())
        return;
      s="SELECT c.*,cl.*,fp.fpa_nomb,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3"+
         " FROM v_facvec as c,clientes as cl,v_forpago as fp "+
          " WHERE  c.cli_codi = cl.cli_codi "+
          " and cl.cli_giro = 'S' "+
          " and fp.fpa_codi = cl.fpa_codi "+
          PcondBus.getCondWhere()+
          " ORDER BY "+(tip_listadE.getValor().equals("C")?"c.cli_codi,":"")+
          "c.emp_codi, fvc_ano,fvc_nume";
      if (! dtCon1.select(s))
      {
        mensajeErr("NO encontradas facturas para estas condiciones");
        return;
      }

      do
      {
        clFactCob.calDiasVto(dtCon1.getInt("fpa_dia1"),dtCon1.getInt("fpa_dia2"),
                             dtCon1.getInt("fpa_dia3"),dtCon1.getInt("cli_dipa1"),
                             dtCon1.getInt("cli_dipa2"),0,dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
        s="SELECT * FROM V_RECIBO "+
            " WHERE eje_nume = "+dtCon1.getInt("fvc_ano")+
            " and emp_codi = "+dtCon1.getInt("emp_codi")+
            " and fvc_nume = "+dtCon1.getInt("fvc_nume");
        if (!dtStat.select(s,true))
        {
          dtStat.addNew("v_recibo");
          dtStat.setDato("eje_nume",dtCon1.getInt("fvc_ano"));
          dtStat.setDato("emp_codi",dtCon1.getInt("emp_codi"));
          dtStat.setDato("fvc_nume",dtCon1.getInt("fvc_nume"));
          dtStat.setDato("rec_nume",1);
        }
        else
          dtStat.edit();
        dtStat.setDato("rec_fecvto",clFactCob.diasVto[0],"dd-MM-yyyy");
        dtStat.setDato("rem_ejerc",0);
        dtStat.setDato("rem_codi",0);
        dtStat.setDato("rec_banrem",0);
        dtStat.setDato("rec_emitid",0);
        dtStat.setDato("rec_remant",0);
        dtStat.setDato("rec_recagr",0);
        dtStat.setDato("rec_impor2",0);
        dtStat.setDato("rec_import",dtCon1.getDouble("fvc_sumtot"));
        dtStat.update(stUp);
      } while (dtCon1.next());
      ctUp.commit();
      mensajeErr("Giros Generados");
    }  catch (Throwable k)
    {
      Error("Error al Generar giros",k);
      return;
    }
  }

  void Baceptar_actionPerformed(String accion)
  {
      this.accion = accion;
      try
      {
          if (!PcondBus.checkCampos())
              return;
          if (accion.equals(CONSULTA) && !tip_listadE.getValor().equals("R"))
          {
              msgBox("La consulta solo se permite sobre Relacion de Facturas");
              return;
          }
          if (accion.equals(EXCEL) && !tip_listadE.getValor().equals("C") && !tip_listadE.getValor().equals("R"))
          {
              msgBox("Exportar  solo se permite sobre Agrupado por Cliente");
              return;
          }

          if (accion.equals(CORREO))
          {
              if (accion.equals(CORREO))
              {
                  ifMail.setVisible(true);
                  ifMail.setSelected(true);
                  ifMail.setLiRelFact(this);
                  
                  ifMail.setAsunto("Relacion Facuras de: " + PcondBus.getFechaInicio() + "  a: " + PcondBus.getFechaFinal());
                  ifMail.setText("Estimado Señor,\n\nAdjunto le enviamos la relacion de facturas "
                      + "  de fecha: " + PcondBus.getFechaInicio()+" a fecha: "+PcondBus.getFechaFinal()
                      + "\n\nAtentamente\n\n" + pdempresa.getNombreEmpresa(dtStat, EU.em_cod));
                  ifMail.setDatosDoc("R", "", true);
                  return;
              }
          }
         
      } catch (Exception k)
      {
          Error("Error al Comprobar condiciones de consulta", k);
          return;
      }
      new miThread("")
      {
          @Override
          public void run() {               
              msgEspere("Buscando facturas");
              jt.tableView.setVisible(false);
              listFactur();
              jt.tableView.setVisible(true);
              resetMsgEspere();
          }
      };
  }

  void listFactur()
  {
    mensaje("Espere, por favor .. GENERANDO LISTADO");
    this.setEnabled(false);
    s="SELECT c.*,cl.*,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3,fp.fpa_nomb "+
        " FROM v_facvec as c,clientes as cl,v_forpago as fp "+
        " WHERE  c.cli_codi = cl.cli_codi "+
        " and fp.fpa_codi = cl.fpa_codi "+
        PcondBus.getCondWhere()+
        " ORDER BY "+(tip_listadE.getValor().equals("C")?"c.cli_codi,":"")+
        "c.emp_codi, fvc_ano,fvc_nume";

    try {

      rs = ct.createStatement().executeQuery(dtCon1.getStrSelect(s));
      if (!rs.next())
      {
        this.setEnabled(true);
        msgBox("No ENCONTRADAS FACTURAS CON ESTOS CRITERIOS");
        mensaje("");
        return;
      }
      if (tip_listadE.getValor().startsWith("F"))
      {
        if (! calcVtos())
          this.setEnabled(true);
        listar();
        return;
      }
      if (accion.equals(LISTADO))
        listar();
      else if (accion.equals( EXCEL))
      {
        if (tip_listadE.getValor().equals("R"))
            exportarRelFras();
        else
            exportar();
      }
      else
         consultar();
    }  catch (Exception k)
    {
      Error("Error al Listar Facturas",k);
    }
    }
  void exportarRelFras() throws Exception
  {
      File f=util.chooseFile(this,"csv");
      if (f==null)
      {
        msgBox("Cancelada exportacion de Fras");
        mensaje("");
        this.setEnabled(true);
        return;
      }
     
      FileOutputStream out=new FileOutputStream(f);
      s="Emp;Serie;Fra;Fecha;Cliente;Bruto;Dtos;B.Impon;%IVA;Imp.IVA;%REQ;Imp.Req;Importe";

      util.print(s,true,out);
      do {
         s=rs.getInt("emp_codi")+";"+rs.getString("fvc_serie")+";"+Formatear.format( rs.getInt("fvc_nume"),"99999")+";"+
          Formatear.getFecha(rs.getDate("fvc_fecfra"),"dd-MM-yyyy")+";"+
          rs.getString("cli_nomb")+";"+
          (""+Formatear.redondea(rs.getString("fvc_sumlin"),2)).replace('.',',')+";"+
          (""+((rs.getDouble("fvc_dtopp")+rs.getDouble("fvc_dtocom")+
                  rs.getDouble("fvc_dtootr"))* rs.getDouble("fvc_sumlin")/100)).replace('.',',')+";"+
           (""+Formatear.redondea(rs.getString("fvc_basimp"),2)).replace('.',',')+";"+
           (""+Formatear.redondea(rs.getString("fvc_poriva"),2)).replace('.',',')+";"+
          (""+Formatear.redondea(rs.getString("fvc_impiva"),2)).replace('.',',')+";"+
           (""+Formatear.redondea(rs.getString("fvc_porreq"),2)).replace('.',',')+";"+
           (""+Formatear.redondea(rs.getString("fvc_imprec"),2)).replace('.',',')+";"+
           (""+Formatear.redondea(rs.getString("fvc_sumtot"),2)).replace('.',',');
         util.print(s,true,out);
      } while (rs.next());
      out.close();
      mensaje("");
      mensajeErr("Generado Fichero EXCEL EN: "+f.getAbsolutePath());
      mensajes.mensajeAviso("Generado Fichero EXCEL EN: "+f.getAbsolutePath());
      this.setEnabled(true);
  }
  void consultar() throws Exception
  {
     
      jt.removeAllDatos();

      double impBruT=0,impTot=0,impDto=0,impBI=0,impDtoT=0,impIvaT=0,kilTot=0;
      int nFras=0;
       
     PreparedStatement psKilos=dtStat.getPreparedStatement("select sum(fvl_canti) "+
              " as fvl_canti from v_facvel as l, v_articulo as a "+
             " where l.emp_codi = ? and l.eje_nume=? and fvc_nume=? and l.fvc_serie=?"+
             " and a.pro_codi = l.pro_codi "+
             " and a.pro_tiplot = 'V' ");
      ResultSet rsKilos;
      do
      {
          ArrayList v=new ArrayList();
          v.add(rs.getInt("emp_codi")+"/"+rs.getString("fvc_serie")+"-"+Formatear.format( rs.getInt("fvc_nume"),"99999"));
          v.add(rs.getDate("fvc_fecfra"));
          v.add(rs.getString("cli_nomb"));
          v.add(rs.getString("fvc_sumlin"));
          impDto=(rs.getDouble("fvc_dtopp")+rs.getDouble("fvc_dtocom")+
                  rs.getDouble("fvc_dtootr"))* rs.getDouble("fvc_sumlin")/100;
          v.add(""+impDto);
          v.add(rs.getString("fvc_basimp"));
          v.add(rs.getString("fvc_poriva"));
          v.add(rs.getString("fvc_impiva"));
          v.add(rs.getString("fvc_porreq"));
          v.add(rs.getString("fvc_imprec"));
          v.add(rs.getString("fvc_sumtot"));
          psKilos.setInt(1, rs.getInt("emp_codi"));
          psKilos.setInt(2, rs.getInt("fvc_ano"));
          psKilos.setInt(3, rs.getInt("fvc_nume"));
          psKilos.setString(4,rs.getString("fvc_serie"));
          rsKilos=psKilos.executeQuery();
          rsKilos.next();
          if (rsKilos.getObject("fvl_canti")==null)
            v.add("");
          else
          {
            v.add(rsKilos.getDouble("fvl_canti"));
            kilTot+=rsKilos.getDouble("fvl_canti");
          }
          jt.addLinea(v);
          nFras++;
          impBruT+=rs.getDouble("fvc_sumlin");
          impDtoT+=impDto;
          impIvaT+=rs.getDouble("fvc_impiva")+rs.getDouble("fvc_imprec");
          impBI+=rs.getDouble("fvc_basimp");
          impTot+=rs.getDouble("fvc_sumtot");
      } while (rs.next());
      
      jt.requestFocusInicio();
      numfrasE.setValorDec(nFras);
      impbruE.setValorDec(impBruT);
      impDtosE.setValorDec(impDtoT);
      impBIE.setValorDec(impBI);
      impIvaE.setValorDec(impIvaT);
      impTotE.setValorDec(impTot);
      KilTotE.setValorDec(kilTot);
      mensaje("");
      mensajeErr("Consulta Realizada");

      this.setEnabled(true);
  }

    boolean calcVtos() throws Exception
    {
      String s1;
      creaTempTable();
      int n;
      for ( n=0;n<maxL;n++)
      {
        imp[n]=0;
        nfras[n]=0;
      }
      int dia=intfechaE.getValorInt();
      boolean opInc=opInclusive.isSelected();
      if (dia==0)
        dia=1;
      maxN=0;
      int dias=0;
      int nFras=0;
      do
      {
        nFras++;
        if (rs.getInt("fpa_dia1")>rs.getInt("fpa_dia2"))
          dias=rs.getInt("fpa_dia1");
        else
          dias=rs.getInt("fpa_dia2");
        if (dias < rs.getInt("fpa_dia3"))
          dias = rs.getInt("fpa_dia3");
//        if (dias>60)
//          debug("dias:"+dias);
        n=(dias-(opInc?1:0))/dia;
        if (n>maxL)
        {
          msgBox("Más de "+maxL+" Lineas ... LISTADO CANCELADO");
          return false;
        }
        if (n>maxN)
          maxN=n;
        imp[n]+=rs.getDouble("fvc_sumtot");
        nfras[n]+=1;
        s1="INSERT INTO "+TABLA_TMP+" values ("+
            "'"+rs.getString("fvc_serie")+"',"+
            rs.getInt("fvc_ano")+","+
            rs.getInt("emp_codi")+","+
            rs.getInt("fvc_nume")+","+
            (n+1)*dia+")";
        dtStat.executeUpdate(s1);
      } while (rs.next());
//      debug ("Num. de Fras: "+nFras);
//      for (n=0;n<=maxN;n++)
//        debug("< "+((n+1)*dia)+" N� Fras: "+nfras[n]+" Imp.Fras: "+imp[n]);
      return true;
    }
    void creaTempTable() throws SQLException
    {
      String s;
      if (swCreateTable || ct.getDriverType()!=conexion.POSTGRES)
      {
        s="delete from "+TABLA_TMP;
        dtStat.executeUpdate(s);
        return;
      }
      swCreateTable=true;

//      s="delete from "+TABLA_TMP;
      s="CREATE  TEMP TABLE "+TABLA_TMP+" ("+
          "fvc_serie char(1) not null, "+
          "fvc_ano int not null,"+
          " emp_codi int not null,"+
          " fvc_nume int not null,"+
          " fpa_dias int not null) ";
      dtStat.executeUpdate(s);
    }

    void exportar() throws Exception
    {
      int cliCodi=0;
      File f=util.chooseFile(this,"csv");
      if (f==null)
      {
        msgBox("Cancelada exportacion de Fras");
        mensaje("");
        this.setEnabled(true);
        return;
      }
     
      FileOutputStream out=new FileOutputStream(f);
      s="CLIENTE,FECHA,FACTURA,IMPORTE,FORMA PAGO\r\n";
      out.write(s.getBytes());
      double impCli=0;
      double impTot=0;
      int nFras=0;
      do
      {
        if (cliCodi!=rs.getInt("cli_codi"))
        {
          if (nFras>1)
          {
            s ="\"TOTAL CLIENTE\","+
                "\".\"," + // fECHA
                "\"\"," + // fACT.
                "\"" + impCli + "\"," +
                "\".\"\r\n";
            out.write(s.getBytes());
          }
          s ="\""+ rs.getString("cli_nomco") + "\",";
          cliCodi=rs.getInt("cli_codi");
          impCli=0;
          nFras=0;
        }
        else
          s=" ,";
        s+= "\""+Formatear.getFecha( rs.getDate("fvc_fecfra"),"yyyy-MM-dd")+"\","+
            "\""+rs.getInt("emp_codi")+"-"+rs.getInt("fvc_ano")+"/"+rs.getInt("fvc_nume")+"\","+
            "\""+rs.getString("fvc_sumtot")+"\","+
//            "'"+rs.getString("fvc_sumtot").replace('.',new DecimalFormatSymbols(new Locale("es","","")).getDecimalSeparator())+"\',"+
            "\""+rs.getString("fpa_nomb")+"\"";
        s+="\r\n";
        nFras++;
        impCli+=rs.getDouble("fvc_sumtot");
        impTot+=rs.getDouble("fvc_sumtot");
        out.write(s.getBytes() );
      } while (rs.next());
      if (nFras > 1)
      {
        s = "\"TOTAL CLIENTE\"," +
            "\".\"," + // fECHA
            "\"\"," + // fACT.
            "\"" + impCli + "\"," +
            "\".\"\r\n";
        out.write(s.getBytes());
      }
      s= "\"TOTAL GENERAL\"," +
          "\".\"," + // fECHA
          "\"\"," + // fACT.
          "\"" + impTot + "\"," +
          "\".\"\r\n";
      out.write(s.getBytes());
      out.close();
      mensaje("");
      mensajeErr("Generado Fichero EXCEL EN: "+f.getAbsolutePath());
      mensajes.mensajeAviso("Generado Fichero EXCEL EN: "+f.getAbsolutePath());
      this.setEnabled(true);
    }
   public void setSubject(String subject)
   {
       this.subject=subject;
   }
   public void setEmailCC(String emailCC)
   {
       this.emailCC=emailCC;
   }
  
   public void setAsunto(String asunto)
   {
       this.asunto=asunto;
   }
   
   public void setToEmail(String toEmail)
   {
       this.toEmail=toEmail;
   }
    @Override
  public void matar(boolean cerrarConexion)
  {
    if (muerto)
      return;
    if (ifMail!=null)
    {
      ifMail.setVisible(false);
      ifMail.dispose();
    }
   super.matar(cerrarConexion);
  }
   public void enviaEmail()
   {     
       try
       {
           EU.setValorParam(PCORREORELFRA, toEmail,"Correo Relacion Fras.Ventas",EU.usuario,dtAdd);
           dtAdd.commit();
           s = "SELECT c.*,cl.*,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3,fp.fpa_nomb "
            + " FROM v_facvec as c,clientes as cl,v_forpago as fp "
            + " WHERE  c.cli_codi = cl.cli_codi "
            + " and fp.fpa_codi = cl.fpa_codi "
            + PcondBus.getCondWhere()
            + " ORDER BY " + (tip_listadE.getValor().equals("C") ? "c.cli_codi," : "")
            + "c.emp_codi, fvc_ano,fvc_nume";
           rs = ct.createStatement().executeQuery(dtCon1.getStrSelect(s));
           if (!rs.next())
           {
               this.setEnabled(true);
               msgBox("No ENCONTRADAS FACTURAS CON ESTOS CRITERIOS");
               mensaje("");
               return;
           }
           if (tip_listadE.getValor().startsWith("F"))
           {
               if (!calcVtos())
               {
                   this.setEnabled(true);
                   return;
               }
           }
       } catch (Exception k)
       {
           Error("Error al enviar relacion por email", k);
           return;
       }
       new miThread("")
       {
           @Override
           public void run() {
               msgEspere("Buscando facturas");
               jt.tableView.setVisible(false);
               try
               {
                   listar();
               } catch (Exception k)
               {
                   Error("Error al enviar relacion por email", k);
                   return;
               }
               jt.tableView.setVisible(true);
               resetMsgEspere();
           }
       };       
   }
    void listar() throws Exception
    {
     
      if (tip_listadE.getValor().equals("F"))
      {
        s = "SELECT dp.fpa_dias,c.*,cl.*,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3,fp.fpa_nomb " +
            " FROM v_facvec as c,clientes as cl,v_forpago as fp, " +
            TABLA_TMP+" as dp"+
            " WHERE  c.cli_codi = cl.cli_codi " +
            " and fp.fpa_codi = cl.fpa_codi " +
            " and dp.fvc_serie = c.fvc_serie "+
            " and dp.fvc_nume = c.fvc_nume "+
            " and dp.emp_codi = c.emp_codi "+
            " and dp.fvc_ano = c.fvc_ano "+
            PcondBus.getCondWhere() +
            " ORDER BY dp.fpa_dias,fp.fpa_codi, c.cli_codi, "+
            "c.emp_codi, c.fvc_ano,c.fvc_nume";
//        debug(s);
      }
      rs=ct.createStatement().executeQuery(dtCon1.getStrSelect(s));
      switch (tip_listadE.getValor())
      {
          case "R":
              jr = Listados.getJasperReport(EU, "relfacven");
              break;
          case "F":
              jr =Listados.getJasperReport(EU, "relfavefp");
              break;
          case "FP":
              jr =Listados.getJasperReport(EU, "relfavecp");
              break;
          default:
              // Relacion de Facturas por cliente
              jr =Listados.getJasperReport(EU, "relfavecl");
              break;
      }
      java.util.HashMap mp = Listados.getHashMapDefault();
      mp.put("feciniE", PcondBus.feciniE.getText());
      mp.put("fecfinE", PcondBus.fecfinE.getText());
      mp.put("empiniE", PcondBus.empIniE.getValorInt());
      mp.put("empfinE", PcondBus.empFinE.getValorInt());
      mp.put("opInclusive", opInclusive.isSelected());
   
      JasperPrint jp;
      if (tip_listadE.getValor().equals("FP"))
      {
        nLin=-1;
        jp = JasperFillManager.fillReport(jr, mp, this);
      }
      else
        jp= JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
      if (accion.equals(CORREO))
      {
         File fichPDF=util.getFile("clrelfact",EU, "pdf",true) ;
         FileOutputStream outStr=new FileOutputStream(fichPDF);
         JasperExportManager.exportReportToPdfStream(jp,outStr);
         outStr.close();  
         MailHtml correo=new MailHtml(EU.usu_nomb,EU.email);
         correo.setEmailCC(emailCC);
         correo.enviarFichero(toEmail,asunto,subject,
             fichPDF,"relacion_facturas.pdf");       
      }
      else
        gnu.chu.print.util.printJasper(jp, EU);
      this.setEnabled(true);
      mensaje("");
      mensajeErr("Listado de Facturas .. TERMINADO");

  }
  @Override
  public boolean next() throws JRException
  {
    nLin++;
    if (nLin>maxN)
      return false;
    while (nfras[nLin]==0)
    {
      nLin++;
      if (nLin>=maxN)
        return false;
    }
    return true;
  }

  public Object getFieldValue(JRField f) throws JRException
  {
    String campo = f.getName().toLowerCase();

    if (campo.equals("condicion"))
      return "< " +(opInclusive.isSelected()?"=":"")+  ( (nLin + 1) * intfechaE.getValorInt()) + " Dias ";
    if (campo.equals("importe"))
      return new Double(imp[nLin]);

    if (campo.equals("numfras"))
      return new Integer(nfras[nLin]);

   throw new JRException("Campo: "+campo+ " No encontrado");
  }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tip_listadE = new gnu.chu.controles.CComboBox();
        tip_listadE.addItem("Relacion de Facturas","R");
        tip_listadE.addItem("Agrupado por Cliente","C");
        tip_listadE.addItem("Agrupado Formas Pago","F");
        tip_listadE.addItem("Resumen Formas Pago","FP");
        cLabel2 = new gnu.chu.controles.CLabel();
        intfechaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        opInclusive = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("print"));
        PcondBus = new gnu.chu.anjelica.facturacion.condBusFra();
        jt = new gnu.chu.controles.Cgrid(12);
        Presumen = new gnu.chu.controles.CPanel();
        cLabel3 = new gnu.chu.controles.CLabel();
        numfrasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel4 = new gnu.chu.controles.CLabel();
        impbruE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9.99");
        cLabel5 = new gnu.chu.controles.CLabel();
        impDtosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel6 = new gnu.chu.controles.CLabel();
        impIvaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel7 = new gnu.chu.controles.CLabel();
        impTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9.99");
        impBIE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9.99");
        cLabel8 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        KilTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pcondic.setMaximumSize(new java.awt.Dimension(690, 183));
        Pcondic.setMinimumSize(new java.awt.Dimension(690, 183));
        Pcondic.setPreferredSize(new java.awt.Dimension(690, 183));
        Pcondic.setLayout(null);

        cLabel1.setText("Tipo Listado");
        Pcondic.add(cLabel1);
        cLabel1.setBounds(340, 120, 80, 20);
        Pcondic.add(tip_listadE);
        tip_listadE.setBounds(410, 120, 260, 20);

        cLabel2.setText("Intervalo Fecha");
        Pcondic.add(cLabel2);
        cLabel2.setBounds(340, 150, 90, 15);

        intfechaE.setText("intfechaE");
        Pcondic.add(intfechaE);
        intfechaE.setBounds(430, 150, 30, 20);

        opInclusive.setText("Inclusive");
        opInclusive.setMaximumSize(new java.awt.Dimension(102, 18));
        opInclusive.setMinimumSize(new java.awt.Dimension(102, 18));
        opInclusive.setPreferredSize(new java.awt.Dimension(102, 18));
        Pcondic.add(opInclusive);
        opInclusive.setBounds(480, 150, 90, 18);

        Baceptar.addMenu("Imprimir",LISTADO);
        Baceptar.addMenu("Consultar",CONSULTA);
        Baceptar.addMenu("Correo",CORREO);
        Baceptar.addMenu("Exportar",EXCEL);
        Pcondic.add(Baceptar);
        Baceptar.setBounds(590, 150, 80, 26);
        Pcondic.add(PcondBus);
        PcondBus.setBounds(1, 1, 680, 180);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        Pprinc.add(Pcondic, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(600, 200));
        jt.setMinimumSize(new java.awt.Dimension(600, 200));
        ArrayList v=new ArrayList();
        v.add("Factura"); // 0
        v.add("Fecha"); // 1
        v.add("Cliente"); // 2
        v.add("Bruto"); // 3
        v.add("Dtos"); // 4
        v.add("B.Impon"); // 5
        v.add("%IVA"); // 6
        v.add("Imp.IVA"); // 7
        v.add("%REQ"); //8
        v.add("Imp.Req"); //9
        v.add("Importe"); // 10
        v.add("Kilos"); //11
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{70,70,150,75,50,75,40,60,40,60,80,70});
        jt.setAlinearColumna(new int[]{2,1,0,2,2,2,2,2,2,2,2,2});
        jt.setFormatoColumna(1,"dd-MM-yy");
        jt.setFormatoColumna(3, "----,--9.99");
        jt.setFormatoColumna(4, "---,--9.99");
        jt.setFormatoColumna(5, "----,--9.99");
        jt.setFormatoColumna(6, "#9.99");
        jt.setFormatoColumna(7, "--,--9.99");
        jt.setFormatoColumna(8, "#9.99");
        jt.setFormatoColumna(9, "--,--9.99");
        jt.setFormatoColumna(10, "----,--9.99");
        jt.setFormatoColumna(11, "---,--9.99");

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 729, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 248, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        Pprinc.add(jt, gridBagConstraints);

        Presumen.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Presumen.setMaximumSize(new java.awt.Dimension(100, 30));
        Presumen.setMinimumSize(new java.awt.Dimension(100, 30));
        Presumen.setPreferredSize(new java.awt.Dimension(100, 30));
        Presumen.setQuery(true);
        Presumen.setLayout(null);

        cLabel3.setText("Nº Fras");
        Presumen.add(cLabel3);
        cLabel3.setBounds(2, 2, 38, 17);

        numfrasE.setEditable(false);
        Presumen.add(numfrasE);
        numfrasE.setBounds(46, 2, 45, 17);

        cLabel4.setText("Bruto");
        Presumen.add(cLabel4);
        cLabel4.setBounds(95, 2, 40, 17);

        impbruE.setEditable(false);
        Presumen.add(impbruE);
        impbruE.setBounds(130, 2, 70, 17);

        cLabel5.setText("IVA");
        Presumen.add(cLabel5);
        cLabel5.setBounds(410, 2, 20, 17);

        impDtosE.setEditable(false);
        Presumen.add(impDtosE);
        impDtosE.setBounds(240, 2, 65, 17);

        cLabel6.setText("B.I.");
        Presumen.add(cLabel6);
        cLabel6.setBounds(310, 2, 20, 17);

        impIvaE.setEditable(false);
        Presumen.add(impIvaE);
        impIvaE.setBounds(440, 2, 65, 17);

        cLabel7.setText("Total");
        Presumen.add(cLabel7);
        cLabel7.setBounds(510, 2, 36, 17);

        impTotE.setEditable(false);
        Presumen.add(impTotE);
        impTotE.setBounds(540, 2, 70, 17);

        impBIE.setEditable(false);
        Presumen.add(impBIE);
        impBIE.setBounds(330, 2, 70, 17);

        cLabel8.setText("Dtos");
        Presumen.add(cLabel8);
        cLabel8.setBounds(210, 2, 30, 17);

        cLabel9.setText("Kg");
        Presumen.add(cLabel9);
        cLabel9.setBounds(620, 2, 30, 17);

        KilTotE.setEditable(false);
        Presumen.add(KilTotE);
        KilTotE.setBounds(650, 2, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Presumen, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CTextField KilTotE;
    private gnu.chu.anjelica.facturacion.condBusFra PcondBus;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Presumen;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField impBIE;
    private gnu.chu.controles.CTextField impDtosE;
    private gnu.chu.controles.CTextField impIvaE;
    private gnu.chu.controles.CTextField impTotE;
    private gnu.chu.controles.CTextField impbruE;
    private gnu.chu.controles.CTextField intfechaE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField numfrasE;
    private gnu.chu.controles.CCheckBox opInclusive;
    private gnu.chu.controles.CComboBox tip_listadE;
    // End of variables declaration//GEN-END:variables

}
