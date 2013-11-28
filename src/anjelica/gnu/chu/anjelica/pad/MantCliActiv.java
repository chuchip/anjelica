
package gnu.chu.anjelica.pad;
/**
 * <p>Titulo: MantCliActiv</p>
 * <p>Descripcion: Programa para tratar masivamente clientes.
 * Permite Cambiar los siguientes campos: Activo/Zona/Seccion/</p>
* <p>Copyright: Copyright (c) 2005-2012
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
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MantCliActiv extends ventana {
   private static final int JT_ACTIV=1;
   private static final int JT_CLICODI=0;
   public MantCliActiv(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public MantCliActiv(EntornoUsuario eu, Principal p, Hashtable ht)
  {

   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Mantenimiento Clientes Activos");

   try
   {
     if(jf.gestor.apuntar(this))
         jbInit();
      else
        setErrorInit(true);
   }
   catch (Exception e) {
     setErrorInit(true);
     Logger.getLogger(MantCliActiv.class.getName()).log(Level.SEVERE, null, e);
   }
 }

 public MantCliActiv(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Mantenimiento Clientes Activos");
   eje=false;

   try  {
     jbInit();
   }
   catch (Exception e) {
     Logger.getLogger(MantCliActiv.class.getName()).log(Level.SEVERE, null, e);
     setErrorInit(true);
   }
 }

private void jbInit() throws Exception
{
   iniciarFrame();

   this.setVersion("2012-05-21");
   statusBar = new StatusBar(this);
   conecta();
   initComponents();
   this.setSize(660,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);
}
@Override
public void iniciarVentana() throws Exception
{
    sbe_codiE.setAceptaNulo(true);
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setLabelSbe(sbe_nombL);
    sbe_codiE.setValorInt(0);
    activarEventos();
}
    private void activarEventos()
    {
        Baceptar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Baceptar_actionPerformed();
            }
        });
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jt.getSelectedColumn()==JT_ACTIV && ! jt.isVacio() && jt.isEnabled())
                    jt.setValor(!jt.getValBoolean(JT_ACTIV),JT_ACTIV);
            }
        });
        Bselec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int nCol=jt.getRowCount();
               if (e.getActionCommand().startsWith("Todo"))
               {
                 for (int n=0;n<nCol;n++)
                    jt.setValor(true,n,JT_ACTIV);
                 return;
               }
               if (e.getActionCommand().startsWith("Nada"))
               {
                 for (int n=0;n<nCol;n++)
                    jt.setValor(false,n,JT_ACTIV);
                 return;
               }
               for (int n=0;n<nCol;n++)
                    jt.setValor(!jt.getValBoolean(n,JT_ACTIV),n,JT_ACTIV);
            }
        });
        Bactualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                   if (jt.isVacio())
                       return;
                   actualizar();
               }
        });
    }
    private void actualizar()
    {
      new miThread("")
      {
            @Override
        public void run()
        {
         actualizar1();
        }
      };
    }
    private void actualizar1()
    {
        int nCliCamb=0;
        try {
             msgEspere("Actualizando estado de Clientes");
             int nCol=jt.getRowCount();
            
             String s;
             for (int n=0;n<nCol;n++)
             {
                 String nuevoValor=jt.getValBoolean(n,JT_ACTIV)?"S":"N";
                 dtCon1.select("select * from clientes  WHERE cli_codi ="+jt.getValorInt(n,JT_CLICODI),true);
                 if (dtCon1.getString("cli_activ").equals(nuevoValor))
                     continue; // No hay cambios
                 s = " INSERT INTO cliencamb values(" + dtCon1.getStrInsert() +
                       ", '" + EU.usuario + "'" +
                       ",TO_DATE('" + Formatear.getFechaAct("dd-MM-yyyy") + "','dd-MM-yyyy')" +
                       "," + Formatear.getFechaAct("HH.mm") +
                       ",'" + "Mant. Clientes Activos: "+(jt.getValBoolean(n,JT_ACTIV)?"S":"N")
                       + "')";
             //     debug(dtAdd.getStrSelect(s));
                 stUp.executeUpdate(dtCon1.getStrSelect(s));
                 dtCon1.edit();
                 dtCon1.setDato("cli_activ",(jt.getValBoolean(n,JT_ACTIV)?"S":"N"));
                 dtCon1.update();
                 nCliCamb++;
             }
             dtCon1.commit();
             resetMsgEspere();
             mensajeErr("Estado de clientes, ACTUALIZADOS");
             msgBox("Actualizado el estado de: "+nCliCamb);
        } catch (SQLException k)
        {
            Error("Error al actualizar datos clientes",k);
        }
    }
    private boolean checkCondiciones()
    {
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

    private void Baceptar_actionPerformed()
    {

      if (!checkCondiciones())
        return;
      new miThread("")
      {
            @Override
        public void run()
        {
         consultar();
        }
      };
    }
    void consultar()
    {
      msgEspere("Espere .... buscando datos");
      try
      {
        String s = getStrSql();
        jt.setEnabled(false);
        jt.removeAllDatos();
        if (!dtCon1.select(s))
        {
          mensajeErr("No encontrados clientes con estos Criterios");
          zon_codiE.requestFocus();
          resetMsgEspere();
          this.setEnabled(true);
          return;
        }
        Date feulmvt=null;
        if (! feulmvE.isNull())
            feulmvt=feulmvE.getDate();
        PreparedStatement ps=dtStat.getPreparedStatement("select max(avc_fecalb) as avc_fecalb from  v_albavec "+
                " where cli_codi= ?");
        int nCli = 0;
        do
        {
          ps.setInt(1, dtCon1.getInt("cli_codi"));
          ResultSet rs=ps.executeQuery();
          rs.next();
          if (feulmvt!=null)
          {
              if (rs.getDate("avc_fecalb")==null )
                continue;
              if (Formatear.comparaFechas(feulmvt, rs.getDate("avc_fecalb"))<0)
                  continue;
          }
          nCli++;
          ArrayList v = new ArrayList();
          v.add(dtCon1.getString("cli_codi"));
          v.add(dtCon1.getString("cli_activ"));
          v.add(dtCon1.getString("cli_nomb"));
          v.add(dtCon1.getString("cli_nomco"));
          v.add(dtCon1.getString("cli_pobl"));
          v.add(dtCon1.getFecha("cli_fecalt", "dd-MM-yyyy"));
          v.add(dtCon1.getString("zon_codi"));
          v.add(dtCon1.getString("rep_codi"));
          v.add(dtCon1.getInt("sbe_codi"));
          v.add(rs.getDate("avc_fecalb"));
          jt.addLinea(v);
        }
        while (dtCon1.next());
        resetMsgEspere();
        numCliE.setValorDec(nCli);
        jt.setEnabled(true);
        jt.requestFocusInicio();
        
        mensajeErr("Consulta ... Realizada");
      }
      catch (Exception k)
      {
        Error("Error al Buscar Datos", k);
      }
    }
    String getStrSql()
    {
     String s = "SELECT cl.* FROM clientes as cl  " +
          " WHERE 1= 1 ";
      if (!rep_codiE.isNull(true) && !rep_codiE.getText().equals("**") && !rep_codiE.getText().equals("*"))
        s += " and cl.rep_codi  LIKE '" + Formatear.reemplazar(rep_codiE.getText(), "*", "%") + "'";
      if (!zon_codiE.isNull(true) && !zon_codiE.getText().equals("**") && !zon_codiE.getText().equals("*"))
        s += " and cl.zon_codi  LIKE '" + Formatear.reemplazar(zon_codiE.getText(), "*", "%") + "'";
      if (sbe_codiE.getValorInt()!=0)
         s+=" and cl.sbe_codi = "+sbe_codiE.getValorInt();
      s += " and cl.cli_activ  LIKE '" + cli_activE.getValor() + "'" +
          (fealinE.isNull() ? "" : " and cl.cli_fecalt >= TO_DATE('" + fealinE.getText() + "','dd-MM-yyyy')") +
          (fealfiE.isNull() ? "" : " and cl.cli_fecalt <= TO_DATE('" + fealfiE.getText() + "','dd-MM-yyyy')") +
          " ORDER BY cl.cli_activ,cl.cli_codi desc";
      return s;
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

        cLinkBox2 = new gnu.chu.controles.CLinkBox();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        zon_codiL = new gnu.chu.controles.CLabel();
        zon_codiL1 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel1 = new gnu.chu.controles.CLabel();
        cli_activE = new gnu.chu.controles.CComboBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        fealinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        fealfiE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        Baceptar = new gnu.chu.controles.CButtonMenu();
        cLabel4 = new gnu.chu.controles.CLabel();
        feulmvE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        zon_codiL2 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        sbe_nombL = new gnu.chu.controles.CLabel();
        jt = new gnu.chu.controles.Cgrid(10);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        numCliE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        Bselec = new gnu.chu.controles.CButtonMenu();
        Bactualizar = new gnu.chu.controles.CButton();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(639, 79));
        Pcabe.setMinimumSize(new java.awt.Dimension(639, 79));
        Pcabe.setPreferredSize(new java.awt.Dimension(639, 79));
        Pcabe.setLayout(null);

        zon_codiE.setAncTexto(30);
        try { zon_codiE.setFormato(Types.CHAR, "XX", 2);
            zon_codiE.texto.setMayusc(true);
            gnu.chu.anjelica.pad.pdclien.llenaDiscr(dtStat, zon_codiE, "Cz", EU.em_cod);
        } catch (SQLException k){Error("Error al llenar Discriminadores de Zona",k);}
        zon_codiE.addDatos("**", "TODOS");
        zon_codiE.setText("**");
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(60, 2, 190, 17);

        zon_codiL.setText("Zona");
        Pcabe.add(zon_codiL);
        zon_codiL.setBounds(10, 2, 27, 17);

        zon_codiL1.setText("Represent");
        Pcabe.add(zon_codiL1);
        zon_codiL1.setBounds(350, 0, 70, 17);

        rep_codiE.setFormato(Types.CHAR, "XX", 2);
        rep_codiE.texto.setMayusc(true);
        try {
            gnu.chu.anjelica.pad.MantRepres.llenaLinkBox(rep_codiE,dtCon1);
        } catch (SQLException k){Error("Error al llenar Discriminadores de Representantes",k);}
        rep_codiE.addDatos("**", "TODOS");
        rep_codiE.setText("**");
        rep_codiE.setAncTexto(30);
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(420, 2, 210, 17);

        cLabel1.setText("Activo ");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 22, 37, 15);

        cli_activE.addItem("Si","S");
        cli_activE.addItem("NO", "N");
        cli_activE.addItem("TODOS","%");
        Pcabe.add(cli_activE);
        cli_activE.setBounds(60, 20, 90, 17);

        cLabel2.setText("Fecha Ult. Mvto Menor ");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(270, 40, 130, 17);
        Pcabe.add(fealinE);
        fealinE.setBounds(400, 20, 70, 17);

        cLabel3.setText("A Fec.Alta ");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(480, 20, 70, 17);
        Pcabe.add(fealfiE);
        fealfiE.setBounds(560, 20, 70, 17);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(520, 40, 110, 26);

        cLabel4.setText("De Fec.Alta ");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(330, 20, 70, 17);
        Pcabe.add(feulmvE);
        feulmvE.setBounds(400, 40, 70, 17);

        zon_codiL2.setText("Seccion");
        Pcabe.add(zon_codiL2);
        zon_codiL2.setBounds(10, 40, 50, 17);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(60, 40, 40, 17);

        sbe_nombL.setBackground(java.awt.Color.orange);
        sbe_nombL.setOpaque(true);
        Pcabe.add(sbe_nombL);
        sbe_nombL.setBounds(100, 40, 170, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(539, 139));
        jt.setMinimumSize(new java.awt.Dimension(539, 139));
        ArrayList v=new ArrayList();
        v.add("Cliente"); //0
        v.add("Act"); // 1
        v.add("Nombre Social"); // 1
        v.add("Nombre Fiscal"); //2
        v.add("Poblacion"); // 3
        v.add("Fec.Alta"); //4
        v.add("Zona"); // 5
        v.add("Repr"); // 6
        v.add("Seccion"); // 8
        v.add("Fec.Mvt"); // 9
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,30,200,200,100,80,30,30,30,80});
        jt.setAlinearColumna(new int[]{2,1,0,0,0,1,0,0,2,1});
        jt.setFormatoColumna(JT_ACTIV, "BSN");

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1182, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(536, 26));
        Ppie.setMinimumSize(new java.awt.Dimension(536, 26));
        Ppie.setPreferredSize(new java.awt.Dimension(536, 26));
        Ppie.setLayout(null);

        cLabel5.setText("Nº Clientes");
        Ppie.add(cLabel5);
        cLabel5.setBounds(12, 2, 70, 17);
        Ppie.add(numCliE);
        numCliE.setBounds(80, 2, 40, 17);

        Bselec.addMenu("Invertir");
        Bselec.addMenu("Todo");
        Bselec.addMenu("Nada");
        Bselec.setText("Selecionar");
        Ppie.add(Bselec);
        Bselec.setBounds(190, 0, 110, 26);

        Bactualizar.setText("Actualizar");
        Ppie.add(Bactualizar);
        Bactualizar.setBounds(420, 0, 100, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CButton Bactualizar;
    private gnu.chu.controles.CButtonMenu Bselec;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLinkBox cLinkBox2;
    private gnu.chu.controles.CComboBox cli_activE;
    private gnu.chu.controles.CTextField fealfiE;
    private gnu.chu.controles.CTextField fealinE;
    private gnu.chu.controles.CTextField feulmvE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField numCliE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLabel sbe_nombL;
    private gnu.chu.controles.CLinkBox zon_codiE;
    private gnu.chu.controles.CLabel zon_codiL;
    private gnu.chu.controles.CLabel zon_codiL1;
    private gnu.chu.controles.CLabel zon_codiL2;
    // End of variables declaration//GEN-END:variables

}
