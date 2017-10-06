package gnu.chu.anjelica.ventas;
/**
 * <p>Titulo: Mantenimiento envio albaranes por email</p>
 * <p>Descripcion: mantenimiento Envio de albaranes valorados por email.
 * </p>
* <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.eventos.GridListener;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
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
    PreparedStatement psRegEmail;
    PreparedStatement psReg;
    ResultSet rsReg;
    lialbven liAlb = null;
    String msgCorreo;
    final int JT_INCLUIR=8;
    final int JT_AVCID=9;
    final int jt_CORREO=7;
    final int JT_ENVIADO=6;
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
     setTitulo("Envio Masivo Alb. por Email");
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
           setTitulo("Envio Masivo Alb. por Email");


            jbInit();
        } catch (Exception e) {
            ErrorInit(e);
        } 
    }
    
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
     
        iniciarFrame();

        this.setVersion("2017-07-22" + (ARG_MODCONSULTA ? "SOLO LECTURA" : ""));
        
       
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
        psReg=dtCon1.getPreparedStatement("select r.*,men_nomb from registro as r,mensajes as m "
                + "where reg_numdoc = ? and r.men_codi = 'AVP' "
                + " and r.men_codi = m.men_codi"
                + " ORDER BY reg_codi DESC");
        //String s="select emp_nomb  from empresa where emp_codi= "+EU.empresa 
        activarEventos();
}
    private void activarEventos() {
        jt.addGridListener(new GridAdapter()
        {
                   
            @Override
            public void afterCambiaLinea(GridEvent event) {
                cuentaSeleccion();
                if (jt.isEnabled())
                    verEmailsEnvio(event.getLinea());
            }

     
        });
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
        Binvsel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               filtrar(e.getActionCommand());
            }
        });
    }
    void filtrar(String accionComando)
    {
        char accion=accionComando.charAt(0);
        jt.setEnabled(false);
        jt.salirGrid();
        int nRow=jt.getRowCount();
        for (int n=0;n<nRow;n++)
        {
            switch (accion)
            {
                case 'D':
                    jt.setValor(false,n,JT_INCLUIR);
                    break;
                case 'S':
                    jt.setValor(true,n,JT_INCLUIR);
                    break;
                default:
                    jt.setValor(!jt.getValBoolean(n,JT_INCLUIR),n,JT_INCLUIR);
            }                
        }
        cuentaSeleccion();
        jt.setEnabled(true);
        jt.requestFocusLater();
    }
    void verEmailsEnvio(int linea)
    {
        try
        {
            jtEnv.removeAllDatos();
            if (jt.getValString(linea,JT_ENVIADO).equals("N"))
                return;
            psReg.setInt(1, jt.getValorInt(linea,JT_AVCID));
            rsReg=psReg.executeQuery();
            while (rsReg.next())
            {
                ArrayList v=new ArrayList();                
                v.add(Formatear.getFecha(rsReg.getDate("reg_time"),"dd-MM-yy"));
                v.add(Formatear.getFecha(rsReg.getDate("reg_time"),"HH:mm"));
                v.add(rsReg.getString("usu_nomb"));
                v.add(rsReg.getString("men_nomb"));
                v.add(rsReg.getString("reg_valor"));
                jtEnv.addLinea(v);
            }
        } catch (SQLException ex)
        {
            Error("Error al ver datos emails enviados",ex);
        }
            
    }
    void cuentaSeleccion()
    {
         int nRow=jt.getRowCount();
         int nSel=0;
         for (int n=0;n<nRow;n++)
         {
             if (jt.getValBoolean(n,JT_INCLUIR))
                 nSel++;
         }
         numRegSelE.setValorDec(nSel);
    }
    void buscarAlbaran()
    {
        try
        {
            if (!checkBuscar())
                return;
            jt.setEnabled(false);
            jtEnv.setEnabled(false);
            jt.removeAllDatos();
            
            psRegEmail=dtCon1.getPreparedStatement("select reg_codi,r.reg_valor from registro as r,"
                + "  v_albavec as a where a.avc_id = r.reg_numdoc and men_codi ='AVP' \n" +
                "and a.cli_codi = ?  order by reg_codi desc");
            
            
            String s="select a.*,c.cli_nomb,c.cli_email2 from v_albavec as a,v_cliente as c "
                + "where avc_fecalb between '"+fecIniE.getFechaDB()+"' and '"+fecFinE.getFechaDB()+"'"+
                 " and (avc_revpre = " + pdalbara.REVPRE_REVISA + " OR c.cli_enalva="+pdclien.ENV_ALBVAL_SI+")"+
                 " and a.cli_codi = c.cli_codi "+
                 " and a.emp_codi = "+EU.em_cod+
                 (EU.isRootAV() ? "" : " AND a.div_codi > 0 ")+
                 " and c.cli_enalva != "+pdclien.ENV_ALBVAL_NO+
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
                    v.add(true);
                    v.add(dtCon1.getInt("avc_id"));
                    jt.addLinea(v);
                } while (dtCon1.next());
                jt.setEnabled(true);                
                jt.requestFocusInicio();
                jtEnv.setEnabled(true);
            }   verEmailsEnvio(0);
        } catch (ParseException | SQLException ex)
        {
           Error("Error al buscar albaranes",ex);
        }
    }
    void enviarEmail() 
    {
        new miThread("")
        {
            @Override
            public void run()
            {
                msgEspere("Mandando albaranes por email");
                enviaEmail1();
                resetMsgEspere();
            }
        };
     }
    void enviaEmail1()
    {
          try
          {
              if (liAlb == null)
                  liAlb = new lialbven(dtStat, EU);
              int nRows=jt.getRowCount();
              for (int n=0;n<nRows;n++)
              {
                  setMensajePopEspere("Enviando Email a :"+jt.getValorInt(n,5),false);
                  if (jt.getValBoolean(n,JT_INCLUIR) && !jt.getValString(n,jt_CORREO).trim().equals(""))
                  {
                      
                      enviaAlbaranEmail(jt.getValorInt(n,0),jt.getValString(n,1),jt.getValorInt(n,2),
                          jt.getValString(n,jt_CORREO));
                      jt.setValor(false,n,JT_INCLUIR);
                  }
              } 
              msgBox("Albaranes valorados enviados por email");
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        jt = new gnu.chu.controles.CGridEditable(10);
        jtEnv = new gnu.chu.controles.Cgrid(5);
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

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(439, 39));
        Pcabe.setMinimumSize(new java.awt.Dimension(439, 39));
        Pcabe.setPreferredSize(new java.awt.Dimension(439, 39));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

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
        v.add("ID");
        jt.setCabecera(v);
        try {
            CTextField tf1=new CTextField();
            tf1.setEnabled(false);
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
            vc.add(tf1);
            jt.setCampos(vc);
        } catch (Exception k)
        {
            Error("Error al iniciar grid",k);
            return;
        }
        jt.setFormatoCampos();
        jt.setAnchoColumna(new int[]{30,15,40,60,50,200,10,200,20,10});
        jt.setAlinearColumna(new int[]{2,0,2,1,2,0,1,0,1,0});
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setCanDeleteLinea(false);
        jt.setCanInsertLinea(false);
        jt.setMaximumSize(new java.awt.Dimension(489, 139));
        jt.setMinimumSize(new java.awt.Dimension(489, 139));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        {
            ArrayList v1=new ArrayList();
            v1.add("Fecha");
            v1.add("Hora");
            v1.add("Usuario");
            v1.add("Accion");
            v1.add("Salida");
            jtEnv.setCabecera(v1);
            jtEnv.setAnchoColumna(new int[]{58,40,60,70,250});
            jtEnv.setAlinearColumna(new int[]{1,1,0,0,0});
            jtEnv.setAjustarGrid(true);
        }
        jtEnv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtEnv.setMaximumSize(new java.awt.Dimension(489, 39));
        jtEnv.setMinimumSize(new java.awt.Dimension(489, 39));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtEnv, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Ppie.setMaximumSize(new java.awt.Dimension(489, 29));
        Ppie.setMinimumSize(new java.awt.Dimension(489, 29));
        Ppie.setPreferredSize(new java.awt.Dimension(489, 29));
        Ppie.setLayout(null);

        opCopia.setSelected(true);
        opCopia.setText("Copia Local");
        Ppie.add(opCopia);
        opCopia.setBounds(290, 2, 79, 23);

        Benviar.setText("Enviar");
        Ppie.add(Benviar);
        Benviar.setBounds(380, 2, 100, 19);

        Binvsel.addMenu("Invertir Seleccion","I");
        Binvsel.addMenu("Seleccionar Todo","T");
        Binvsel.addMenu("Deselecionar Todo","N");
        Binvsel.setText("Filtrar");
        Ppie.add(Binvsel);
        Binvsel.setBounds(190, 2, 90, 22);

        cLabel11.setText("Reg. Selec");
        Ppie.add(cLabel11);
        cLabel11.setBounds(10, 2, 70, 17);

        numRegSelE.setEnabled(false);
        Ppie.add(numRegSelE);
        numRegSelE.setBounds(80, 2, 30, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

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
