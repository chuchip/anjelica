/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MantEnvioAlbEmail extends ventana
{
      lialbven liAlb = null;
    String msgCorreo;
    int JT_INCLUIR=8;
    int jt_CORREO=7;
    private boolean ARG_MODCONSULTA=false;
    /**
     * Creates new form MantEnvioAlbEmail
     */
    public MantEnvioAlbEmail() {
        initComponents();
    }
    public MantEnvioAlbEmail(EntornoUsuario eu, Principal p)
    {
        this(eu, p, null);
    }

 public MantEnvioAlbEmail(EntornoUsuario eu, Principal p,Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     if (ht != null)
     {
       if (ht.get("modConsulta") != null)
         ARG_MODCONSULTA = Boolean.parseBoolean(ht.get("modConsulta").toString());
     }
     setTitulo("Mantenimiento Precios de Albaranes");
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

    public MantEnvioAlbEmail(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            if (ht != null) {
                if (ht.get("modConsulta") != null) {
                    ARG_MODCONSULTA = Boolean.parseBoolean(ht.get("modConsulta").toString());
                }
            }
            setTitulo("Mantenimiento Precios de Albaranes");


            jbInit();
        } catch (Exception e) {
            ErrorInit(e);
        } 
    }
    
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
     
        iniciarFrame();

        this.setVersion("2017-07-17" + (ARG_MODCONSULTA ? "SOLO LECTURA" : ""));
        
       
        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();

    }
    @Override
    public void iniciarVentana() throws Exception {
        fecIniE.setText(Formatear.sumaDias(Formatear.getDateAct(),-15));
        fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        cli_codiE.iniciar(dtStat, this, vl, EU);
        //String s="select emp_nomb  from empresa where emp_codi= "+EU.empresa 
        activarEventos();
}
    private void activarEventos() {
        Bbuscar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               buscarAlbaran();
            }
        });
         Benviar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               enviarEmail();
            }
        });
    }
    
    void buscarAlbaran()
    {
        try
        {
            if (!checkBuscar())
                return;
            jt.setEnabled(false);
            jt.removeAllDatos();
            PreparedStatement psRegEmail=dtCon1.getPreparedStatement("select reg_codi,r.reg_valor from registro as r,"
                + "  v_albavec as a where a.avc_id = r.reg_numdoc and men_codi ='AVP' \n" +
                "and a.cli_codi = ?  order by reg_codi desc");
            PreparedStatement psReg=dtCon1.getPreparedStatement("select * from registro "
                + "where reg_numdoc = ? and men_codi = 'AVP' ORDER BY reg_codi DESC");
            ResultSet rsReg;
            String s="select a.*,c.cli_nomb,c.cli_email2 from v_albavec as a,v_cliente as c "
                + "where avc_fecalb between '"+fecIniE.getFechaDB()+"' and '"+fecFinE.getFechaDB()+"'"+
                 " and avc_revpre = " + pdalbara.REVPRE_REVISA+
                 " and a.cli_codi = c.cli_codi "+
                 " and a.emp_codi = "+EU.em_cod+
                 (EU.isRootAV() ? "" : " AND v.div_codi > 0 ")+
                (cli_codiE.isNull()?"":" and a.cli_codi = "+cli_codiE.getValorInt())+
                (opEnvioC.getValor().equals("T")?"":
                    " and  "+(opEnvioC.getValor().equals("N")?" NOT ":"")+
                    " exists (select reg_codi from registro as r where reg_numdoc = avc_id and men_codi = 'AVP') ")+
                " order by a.cli_codi";
            String estado;
            String correo;
            if (dtCon1.select(s))
            {
                do
                {
                    psReg.setInt(1, dtCon1.getInt("avc_id"));
                    rsReg = psReg.executeQuery();
                    if (!rsReg.next())
                    {
                        estado="N";
                        psRegEmail.setInt(1, dtCon1.getInt("cli_codi"));
                        rsReg=psRegEmail.executeQuery();
                        if (!rsReg.next())
                            correo=dtCon1.getString("cli_email2");
                        else
                            correo=rsReg.getString("reg_valor");
                    }
                    else
                    {
                        estado="S";
                        correo=rsReg.getString("reg_valor");
                    }
                    ArrayList v= new ArrayList();
                    v.add(dtCon1.getInt("avc_ano"));
                    v.add(dtCon1.getString("avc_serie"));
                    v.add(dtCon1.getInt("avc_nume"));
                    v.add(dtCon1.getDate("avc_fecalb"));
                    v.add(dtCon1.getInt("cli_codi"));
                    v.add(dtCon1.getString("cli_nomb"));
                    v.add(estado); // Estado
                    v.add(correo); // Correo
                    v.add(false);
                    jt.addLinea(v);
                } while (dtCon1.next());
                jt.setEnabled(true);
                jt.requestFocusInicio();
            }
        } catch (ParseException | SQLException ex)
        {
           Error("Error al buscar albaranes",ex);
        }
    }
    void enviarEmail() 
    {
          try
          {
              if (liAlb == null)
                  liAlb = new lialbven(dtStat, EU);
              int nRows=jt.getRowCount();
              for (int n=0;n<nRows;n++)
              {
                  if (jt.getValBoolean(n,JT_INCLUIR) && !jt.getValString(n,jt_CORREO).trim().equals(""))
                  {
                      enviaAlbaranEmail(jt.getValorInt(n,0),jt.getValString(n,1),jt.getValorInt(n,2),
                          jt.getValString(n,jt_CORREO));
                  }
              } 
          } catch (Exception ex)
          {
             Error("Error al mandar albaranes por email",ex);
          }
    }
    
    void enviaAlbaranEmail(int avcAno,String avcSerie,int avcNume,String email) throws Exception
    {
       String sqlDoc=pdalbara.getSqlListaAlb(avcAno,EU.em_cod,avcSerie,avcNume);
       dtStat.select(sqlDoc);
       int idAlbaran=dtStat.getInt("avc_id");
       int cliCodi= dtStat.getInt("cli_codi");
       msgCorreo="Estimado cliente,\n\nAdjunto le enviamos el albaran "+avcAno+avcSerie+
                avcNume+
                "  de fecha: "+dtStat.getFecha("avc_fecalb","dd-MM-yyyy")+
                "\n\nAtentamente\n\n"+ EU.empresa;
       String asunto="Albaran "+avcAno+avcSerie+ avcNume+"  de fecha: "+dtStat.getFecha("avc_fecalb","dd-MM-yyyy");
            liAlb.setSubject(asunto);
            liAlb.setEmailCC(opCopia.isSelected()? EU.email:null);
            liAlb.envAlbaranEmail(ct.getConnection(), dtStat, sqlDoc, EU,
                            true,email,
                            msgCorreo);
            HashMap hm=new HashMap();
            hm.put("%c", cliCodi);
            hm.put("%a",msgCorreo);
            Principal.guardaMens(dtCon1, "EA",hm,null,EU.usuario);
            Principal.guardaRegistro(dtCon1,"AVP",EU.usuario,idAlbaran,email);            
      
    }
    
    boolean checkBuscar() throws ParseException
    {
          if (fecIniE.getError())
                return false;
            if (fecFinE.getError())
                return false;

            if (! fecIniE.isNull() && ! fecFinE.isNull() && Formatear.comparaFechas(fecIniE.getDate(),fecFinE.getDate())>0)
            {
                mensajeErr("Fecha final no puede ser inferios a Inicial");
                fecIniE.requestFocus();
                return false;
            }
            return true;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        avc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        avc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        avc_clicodE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        avc_clinomE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        avc_estadE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        avc_emailE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        opIncluirC = new gnu.chu.controles.CCheckBox();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecIniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        fecFinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel7 = new gnu.chu.controles.CLabel();
        opEnvioC = new gnu.chu.controles.CComboBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        Bbuscar = new gnu.chu.controles.CButton();
        jt = new gnu.chu.controles.CGridEditable(9);
        jtEnv = new gnu.chu.controles.Cgrid();
        Ppie = new gnu.chu.controles.CPanel();
        opCopia = new gnu.chu.controles.CCheckBox();
        Benviar = new gnu.chu.controles.CButton();
        Binvsel = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("filter"));
        cLabel11 = new gnu.chu.controles.CLabel();
        numRegSelE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");

        avc_anoE.setEnabled(false);

        avc_serieE.setEnabled(false);

        avc_numeE.setEnabled(false);

        avc_fecalbE.setEnabled(false);

        avc_clicodE.setEnabled(false);

        avc_clinomE.setEnabled(false);

        avc_estadE.setEnabled(false);

        opIncluirC.setText("cCheckBox3");

        Pprinc.setLayout(null);

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel5);
        cLabel5.setBounds(10, 1, 52, 17);

        fecIniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecIniE);
        fecIniE.setBounds(70, 1, 76, 17);

        cLabel6.setText("Estado");
        cLabel6.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(300, 1, 44, 17);

        fecFinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecFinE);
        fecFinE.setBounds(210, 1, 76, 17);

        cLabel7.setText("A Fecha");
        cLabel7.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(cLabel7);
        cLabel7.setBounds(160, 1, 44, 17);

        opEnvioC.addItem("No Enviado","N");
        opEnvioC.addItem("Enviado","E");
        opEnvioC.addItem("Todo","T");
        Pcabe.add(opEnvioC);
        opEnvioC.setBounds(350, 1, 80, 17);

        cLabel8.setText("Cliente");
        cLabel8.setPreferredSize(new java.awt.Dimension(39, 18));
        Pcabe.add(cLabel8);
        cLabel8.setBounds(10, 20, 39, 18);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(60, 20, 251, 18);

        Bbuscar.setText("Buscar (F4)");
        Pcabe.add(Bbuscar);
        Bbuscar.setBounds(330, 20, 100, 19);

        Pprinc.add(Pcabe);
        Pcabe.setBounds(0, 0, 440, 50);

        ArrayList v=new ArrayList();
        v.add("Ej."); // 0
        v.add("S"); // 1
        v.add("Num."); // 2
        v.add("Fec.Alb"); // 3
        v.add("Cliente"); // 4
        v.add("Nombre Cliente"); // 5
        v.add("E"); // 6
        v.add("Correo"); // 7
        v.add("Inc"); //8
        jt.setCabecera(v);
        try {
            ArrayList vc=new ArrayList();
            vc.add(avc_anoE);
            vc.add(avc_serieE);
            vc.add(avc_numeE);
            vc.add(avc_fecalbE);
            vc.add(avc_clicodE);
            vc.add(avc_clinomE);
            vc.add(avc_estadE);
            vc.add(avc_emailE);
            vc.add(opIncluirC);
            jt.setCampos(vc);
        } catch (Exception k)
        {
            Error("Error al iniciar grid",k);
            return;
        }
        jt.setFormatoCampos();
        jt.setAnchoColumna(new int[]{30,15,40,60,50,200,10,200,20});
        jt.setAlinearColumna(new int[]{2,0,2,1,2,0,1,0,1});
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pprinc.add(jt);
        jt.setBounds(0, 60, 490, 140);

        jtEnv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pprinc.add(jtEnv);
        jtEnv.setBounds(0, 210, 490, 40);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Ppie.setLayout(null);

        opCopia.setText("Copia Local");
        Ppie.add(opCopia);
        opCopia.setBounds(290, 2, 79, 23);

        Benviar.setText("Enviar");
        Ppie.add(Benviar);
        Benviar.setBounds(380, 2, 100, 19);

        Binvsel.addMenu("Invertir Seleccion");
        Binvsel.addMenu("Seleccionar Todo");
        Binvsel.addMenu("Deselecionar Todo");
        Binvsel.setText("Filtrar");
        Ppie.add(Binvsel);
        Binvsel.setBounds(190, 2, 90, 22);

        cLabel11.setText("Reg. Selec");
        Ppie.add(cLabel11);
        cLabel11.setBounds(10, 2, 70, 17);

        numRegSelE.setEnabled(false);
        Ppie.add(numRegSelE);
        numRegSelE.setBounds(80, 2, 30, 17);

        Pprinc.add(Ppie);
        Ppie.setBounds(0, 260, 490, 30);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bbuscar;
    private gnu.chu.controles.CButton Benviar;
    private gnu.chu.controles.CButtonMenu Binvsel;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField avc_anoE;
    private gnu.chu.controles.CTextField avc_clicodE;
    private gnu.chu.controles.CTextField avc_clinomE;
    private gnu.chu.controles.CTextField avc_emailE;
    private gnu.chu.controles.CTextField avc_estadE;
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CTextField avc_serieE;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField fecFinE;
    private gnu.chu.controles.CTextField fecIniE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.Cgrid jtEnv;
    private gnu.chu.controles.CTextField numRegSelE;
    private gnu.chu.controles.CCheckBox opCopia;
    private gnu.chu.controles.CComboBox opEnvioC;
    private gnu.chu.controles.CCheckBox opIncluirC;
    // End of variables declaration//GEN-END:variables
}
