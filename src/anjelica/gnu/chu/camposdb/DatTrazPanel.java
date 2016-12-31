/**
 *
 * <p>Titulo: DatTrazaPanel </p>
 * <p>Descripcion: Panel para sacar los datos de trazabilidad y compra de un 
 * individuo.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2016
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @author chuchiP
 * @version 1.1
 */
package gnu.chu.camposdb;

import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.compras.MantAlbComPlanta;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdsaladesp;
import gnu.chu.controles.CCheckBox;
import gnu.chu.controles.CPanel;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventana;
import gnu.chu.winayu.ayuMat;
import gnu.chu.winayu.ayuSde;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import javax.swing.JLayeredPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


public class DatTrazPanel extends CPanel {
    private CCheckBox opDatosCompra;
    private LotePanel lotePanel=null;
    private String s;
    private ventana papa;
    private DatosTabla dtStat;
    private DatosTabla dtCon1;
    EntornoUsuario EU;
    int proCodi;
    int ejeInd;
    String serInd;
    int lotInd;
    int  numInd;
    utildesp utDesp;
    
    public DatTrazPanel() {
        initComponents();
    }
    public void iniciar(DatosTabla dtStat,DatosTabla dtCon1,ventana padre, JLayeredPane layPane,EntornoUsuario EU) throws SQLException
    {
        this.dtStat=dtStat;
        this.dtCon1=dtCon1;
        this.papa=padre;
        this.EU=EU;
        prv_codiE.iniciar(dtStat, padre, layPane, EU);
        
        acp_painacE.iniciar(dtStat, padre, layPane, EU);
        acp_paisacE.iniciar(dtStat, padre, layPane, EU);
        acp_engpaiE.iniciar(dtStat, padre, layPane, EU);
        acp_painacE.setPeso(3);
//        s = "SELECT pai_inic,pai_nomb FROM v_paises ORDER BY pai_nomb";
//        if (dtStat.select(s))
//        {
//          do
//          {
//            
//            acp_paisacE.addDatos(dtStat.getString("pai_inic"),dtStat.getString("pai_nomb"));
//            acp_engpaiE.addDatos(dtStat.getString("pai_inic"),dtStat.getString("pai_nomb"));
//          } while (dtStat.next());
//        }
        acc_numeE.setEditable(false);
        setEditable(false); // Por defecto es solo consulta.
        activarEventos();
    }
    public void setLotePanel(LotePanel lotePanel)
    {
        this.lotePanel=lotePanel;
    }
    public void setVerDatosCompra(boolean verDatosCompra)
    {
        if (opDatosCompra==null)
            return;
        opDatosCompra.setSelected(verDatosCompra);
    }
    public boolean getVerDatosCompra()
    {
         if (opDatosCompra==null)
            return false;
        return opDatosCompra.isSelected();
    }
    public void setDatosCompraCheckBox(CCheckBox opDatCompra)
    {
      this.opDatosCompra=opDatCompra;
      opDatosCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                  if (lotePanel!=null)
                    verDatos();
                } catch (SQLException k)
                {
                    papa.Error("Error al ver Datos Compra",k);
                }
            }
        });  
    }
    void activarEventos()
    {
        acp_nucrotE.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
              if (e.getKeyCode() == KeyEvent.VK_F3)
                acp_nucrotE.setText(getRandomCrotal( acp_nucrotE.getText()));
            }
        });

        mat_codiE.addKeyListener(new KeyAdapter()
        {
                @Override
          public void keyPressed(KeyEvent e)
          {
            if (e.getKeyCode() == KeyEvent.VK_F3)
              consMatCodi();
          }
        });
        sde_codiE.addKeyListener(new KeyAdapter()
        {
                @Override
          public void keyPressed(KeyEvent e)
          {
            if (e.getKeyCode() == KeyEvent.VK_F3)
              consSdeCodi();
          }
        });
       acc_numeE.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2)
                  return;
              if (papa.jf==null)
                  return;
               ejecutable prog;
              if (! opDatosCompra.isSelected())
              {
                  if ((prog=papa.jf.gestor.getProceso(MantDesp.getNombreClase()))==null)
                      return;
                  MantDesp cm=(MantDesp) prog;
                  cm.PADQuery();
                  cm.setDeoCodi(acc_numeE.getText());
                  cm.ej_query();
                  papa.jf.gestor.ir(cm);
              }
              else
              {
                  try      
                  {
                      if (pdconfig.getTipoEmpresa(EU.em_cod, dtStat)==pdconfig.TIPOEMP_PLANTACION)
                      {
                          if ((prog = papa.jf.gestor.getProceso(MantAlbComPlanta.getNombreClase())) == null)
                              return;
                      }
                      else
                      {
                          if ((prog = papa.jf.gestor.getProceso(MantAlbComCarne.getNombreClase())) == null)
                              return;
                      }
                      
                      MantAlbCom cm = (MantAlbCom) prog;
                      cm.PADQuery();
                      cm.setAccCodi(acc_numeE.getText());
                      cm.ej_query();
                      papa.jf.gestor.ir(cm);
                  } catch (SQLException ex)
                  {
                      papa.Error("Error al ir albaran compras",ex);
                  }
              }
          }
      });
    }
    
    public void setDatos(int proCodi,int ejeInd,String serInd,int lotInd,int  numInd)
    {
        this.proCodi=proCodi;
        this.ejeInd=ejeInd;
        this.serInd=serInd;
        this.lotInd=lotInd;
        this.numInd=numInd;
    }
    /**
     * Actualiza los datos de Trazabilidad Mostrados 
     * @throws java.sql.SQLException
     * @see setDatos
     */
    public void actualizar() throws SQLException
    {
        if (utDesp==null)
        {
            utDesp=new utildesp();
        }
        utDesp.busDatInd(serInd, proCodi, EU.em_cod, ejeInd,lotInd, numInd,dtCon1,dtStat,EU);
        verDatos();
    }
    void verDatos() throws SQLException
    {
        if (getVerDatosCompra())
        {
            verDatosCompra();
            return;
        }
        if (lotePanel!=null)
        {
            if (lotePanel.getProNumind()!=numInd)
            {
                lotePanel.setDatos(proCodi, serInd, ejeInd, lotInd, numInd);
                utDesp.busDatInd(serInd, proCodi, EU.em_cod, ejeInd,lotInd, numInd,dtCon1,dtStat,EU);
            }
        }
        verDatosInd();  
    }
    
    
    void verDatosCompra() throws SQLException
    {
         if (lotePanel!=null)
        {
            if (lotePanel.getProNumind()!=utDesp.getProIndiCompra())
            {
                lotePanel.setDatos(utDesp.getProCodiCompra(), utDesp.getProSerieCompra(),
                        utDesp.getEjeLotCompra(), utDesp.getProLoteCompra(), utDesp.getProIndiCompra());
                utDesp.busDatInd(utDesp.getProSerieCompra(),
                    utDesp.getProCodiCompra(), EU.em_cod,utDesp.getEjeLotCompra(),  utDesp.getProLoteCompra(),
                    utDesp.getProIndiCompra(), dtCon1, dtStat, EU);
            }
        }
        verDatosInd();      
    }
    
    public utildesp getUtilDespiece(boolean actual) throws ParseException, SQLException 
    {
        if (!actual)
            return utDesp;
        utDesp.setAcpEngpai(acp_engpaiE.getText());
        utDesp.setPaisEngorde(acp_engpaiE.getTextNomb());
        utDesp.setFecSacrif(acp_fecsacE.getDate());
        utDesp.setNumCrot(acp_nucrotE.getText());
        utDesp.setAcpPainac(acp_painacE.getText());
        utDesp.setPaisNacimiento(acp_painacE.getTextNomb());
        utDesp.setAcpPaisac(acp_paisacE.getText());
        utDesp.setSacrificado(acp_paisacE.getTextNomb()+ " - "+mat_codiE.getTextCombo());
        utDesp.setFecCompra(avc_fecalbE.getDate());
        utDesp.setConservar(conservarE.getText());
        utDesp.setMatCodi(mat_codiE.getValorInt());
       
        utDesp.setSdeCodi(sde_codiE.getValorInt());
        utDesp.setDespiezado(pdsaladesp.getNombrePais(dtStat,sde_codiE.getValorInt())+" - "+ sde_codiE.getTextCombo());
        
        utDesp.setCambio(false);
        
        return utDesp;
    }
    void verDatosInd()
    {
        prv_codiE.setValorInt(utDesp.getPrvCompra());
        cambioPrv();
        acp_engpaiE.setText(utDesp.getAcpEngpai());
        acp_fecsacE.setDate(utDesp.getFecSacrif());
        acp_nucrotE.setText(utDesp.getNumCrot());
        acp_painacE.setText(utDesp.getAcpPainac());
        acp_paisacE.setText(utDesp.getAcpPaisac());       
        avc_fecalbE.setDate(utDesp.getFecCompra());
        conservarE.setText(utDesp.getConservar());
        mat_codiE.setValorInt(utDesp.getMatCodi());
        sde_codiE.setValorInt(utDesp.getSdeCodi());
        acc_numeE.setValorInt(utDesp.getProLoteCompra());
        acc_fecprodE.setDate(utDesp.getFechaProduccion());
    }
        
    @Override
    public void setEditable(boolean editable)
    {
        acp_engpaiE.setEditable(editable);
        acp_fecsacE.setEditable(editable);
        acp_nucrotE.setEditable(editable);
        acc_fecprodE.setEditable(editable);
        acp_painacE.setEditable(editable);
        acp_paisacE.setEditable(editable);
        avc_fecalbE.setEditable(editable);
        
        conservarE.setEditable(editable);
        mat_codiE.setEditable(editable);
        prv_codiE.setEditable(editable);
        sde_codiE.setEditable(editable);
        avc_feccadE.setEditable(editable);
    }
    public boolean isEditable()
    {
        return acp_engpaiE.isEditable();
    }
    public static String getRandomCrotal(String crotalBase)
    {
        int len = crotalBase.length();
        int numAleatorio;
        String numCrot = crotalBase;
        if (len > 6)
        {
            numCrot = crotalBase.substring(0, len - 6);
            len = 6;
        } else
            numCrot = "";
        if (len == 0)
            len = 6;
        int numMult = 1;
        String formato = "";
        for (int n = 0; n < len; n++)
        {
            numMult = numMult * 10;
            formato = formato + "9";
        }
        numAleatorio = (int) (Math.random() * numMult) + 1;
        if (numAleatorio >= numMult)
            numAleatorio = numMult - 1;
        numCrot = numCrot + Formatear.format(numAleatorio, formato);
        return numCrot;
    }

    /**
     * Consulta Mataderos
     * Llama a la Ayuda de Mataderos
     */
    public void consMatCodi() {
        final ayuMat ayMat;

        try
        {
            ayMat = new ayuMat(EU, papa.vl);
            ayMat.addInternalFrameListener(new InternalFrameAdapter()
            {

                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    ej_consMat(ayMat);
                }
            });

            papa.vl.add(ayMat);
            ayMat.setLocation(25, 25);
            ayMat.setVisible(true);
            papa.setEnabled(false);
            papa.setFoco(ayMat);
            ayMat.iniciarVentana();
        } catch (Exception j)
        {
            this.setEnabled(true);
        }
    }

  void ej_consMat(ayuMat ayMat)
  {
    if (ayMat.consulta)
    {
      mat_codiE.setText(ayMat.mat_codiT);
    }
    ayMat.dispose();
    papa.setEnabled(true);
    papa.toFront();
    try
    {
        papa.setSelected(true);
    }
    catch (Exception k)   {}
    papa.setFoco(null);
    mat_codiE.requestFocus();
  }
    /**
   * Consulta Mataderos
   * Llama a la Ayuda de Mataderos
   */
  public void consSdeCodi()
  {
    final ayuSde aySde;

    try
    {
      aySde = new ayuSde(EU, papa.vl);
      aySde.addInternalFrameListener(new InternalFrameAdapter()
      {
        @Override
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consSde(aySde);
        }
      });

      papa.vl.add(aySde);
      aySde.setLocation(25, 25);
      aySde.setVisible(true);
      papa.setEnabled(false);
      papa.setFoco(aySde);
      aySde.iniciarVentana();
    }
    catch (Exception j)
    {
        this.setEnabled(true);
    }
  }

  void ej_consSde(ayuSde aySde)
  {
    if (aySde.consulta)
    {
      sde_codiE.setText(aySde.sde_codiT);
   
    }

    aySde.dispose();
    papa.setEnabled(true);
      papa.toFront();
      try
      {
        papa.setSelected(true);
      }
      catch (Exception k)
      {}
      papa.setFoco(null);
      sde_codiE.requestFocus();
  }
  /**
   * Carga las salas de despieces y mataderos disponibles para un proveedor
   */
   void cambioPrv()
  {
    try {
      s = "SELECT v_saladesp.sde_codi,sde_nrgsa FROM v_prvsade,v_saladesp "+
          " WHERE prv_codi = " +prv_codiE.getText()+
          " and v_prvsade.sde_codi = v_saladesp.sde_codi "+
          " ORDER BY sde_nrgsa";
      dtStat.select(s);
      sde_codiE.addDatos(dtStat);
      s = "SELECT v_matadero.mat_codi,mat_nrgsa FROM v_prvmata,v_matadero "+
          " WHERE prv_codi = " + prv_codiE.getText()+
          " and v_prvmata.mat_codi = v_matadero.mat_codi "+
          " order by mat_nrgsa";
      dtStat.select(s);
      mat_codiE.addDatos(dtStat);
    } catch (Exception k)
    {
      papa.Error("Error al buscar datos Mataderos de Proveedores",k);
    }
  }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        mat_codiE = new gnu.chu.controles.CLinkBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        sde_codiE = new gnu.chu.controles.CLinkBox();
        cLabel6 = new gnu.chu.controles.CLabel();
        prv_codiE = new gnu.chu.camposdb.prvPanel(){
            public void afterCambioPrv()
            {
                cambioPrv();
            }};
            cLabel7 = new gnu.chu.controles.CLabel();
            avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
            conservarE = new gnu.chu.controles.CTextField();
            cLabel9 = new gnu.chu.controles.CLabel();
            acp_nucrotE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
            acp_feccadL = new gnu.chu.controles.CLabel();
            acc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
            acp_fecsacL = new gnu.chu.controles.CLabel();
            acp_fecsacE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
            cLabel8 = new gnu.chu.controles.CLabel();
            avc_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
            acp_feccadL1 = new gnu.chu.controles.CLabel();
            acc_fecprodE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
            acc_tipdocL = new gnu.chu.controles.CLabel();
            acp_painacE = new gnu.chu.camposdb.PaiPanel();
            acp_paisacE = new gnu.chu.camposdb.PaiPanel();
            acp_engpaiE = new gnu.chu.camposdb.PaiPanel();

            setLayout(null);

            cLabel1.setText("Nacido");
            add(cLabel1);
            cLabel1.setBounds(10, 20, 47, 15);

            cLabel2.setText("Documento");
            add(cLabel2);
            cLabel2.setBounds(310, 40, 70, 18);

            cLabel3.setText("Sacrificado");
            add(cLabel3);
            cLabel3.setBounds(10, 40, 72, 15);

            cLabel4.setText("Matadero");
            add(cLabel4);
            cLabel4.setBounds(10, 60, 72, 15);

            mat_codiE.setFormato(Types.DECIMAL,"####9",5);
            mat_codiE.getComboBox().setPreferredSize(new Dimension(17,150));
            mat_codiE.setAncTexto(40);
            add(mat_codiE);
            mat_codiE.setBounds(80, 60, 270, 17);

            cLabel5.setText("Sala Desp.");
            add(cLabel5);
            cLabel5.setBounds(10, 80, 72, 15);

            mat_codiE.setFormato(Types.DECIMAL,"####9",5);
            mat_codiE.getComboBox().setPreferredSize(new Dimension(17,150));
            sde_codiE.setAncTexto(40);
            add(sde_codiE);
            sde_codiE.setBounds(80, 80, 370, 17);

            cLabel6.setText("Proveedor ");
            add(cLabel6);
            cLabel6.setBounds(10, 2, 61, 15);
            add(prv_codiE);
            prv_codiE.setBounds(80, 2, 420, 17);

            cLabel7.setText("Crotal ");
            add(cLabel7);
            cLabel7.setBounds(10, 100, 40, 17);

            avc_fecalbE.setMinimumSize(new java.awt.Dimension(10, 18));
            avc_fecalbE.setPreferredSize(new java.awt.Dimension(10, 18));
            add(avc_fecalbE);
            avc_fecalbE.setBounds(240, 120, 79, 17);
            add(conservarE);
            conservarE.setBounds(290, 100, 200, 17);

            cLabel9.setText("Fec. Docum");
            add(cLabel9);
            cLabel9.setBounds(170, 120, 70, 17);
            add(acp_nucrotE);
            acp_nucrotE.setBounds(80, 100, 200, 17);

            acp_feccadL.setText("Fec.Produc");
            add(acp_feccadL);
            acp_feccadL.setBounds(355, 60, 70, 18);

            acc_numeE.setMinimumSize(new java.awt.Dimension(10, 18));
            acc_numeE.setPreferredSize(new java.awt.Dimension(10, 18));
            add(acc_numeE);
            acc_numeE.setBounds(380, 40, 50, 18);

            acp_fecsacL.setText("Fec. Sacrificio");
            add(acp_fecsacL);
            acp_fecsacL.setBounds(330, 120, 80, 17);

            acp_fecsacE.setMinimumSize(new java.awt.Dimension(10, 18));
            acp_fecsacE.setPreferredSize(new java.awt.Dimension(10, 18));
            add(acp_fecsacE);
            acp_fecsacE.setBounds(410, 120, 79, 17);

            cLabel8.setText("Cebado");
            add(cLabel8);
            cLabel8.setBounds(270, 20, 61, 15);

            avc_feccadE.setMinimumSize(new java.awt.Dimension(10, 18));
            avc_feccadE.setPreferredSize(new java.awt.Dimension(10, 18));
            add(avc_feccadE);
            avc_feccadE.setBounds(80, 120, 79, 17);

            acp_feccadL1.setText("Fec. Caduc");
            add(acp_feccadL1);
            acp_feccadL1.setBounds(10, 120, 62, 17);

            acc_fecprodE.setMinimumSize(new java.awt.Dimension(10, 18));
            acc_fecprodE.setPreferredSize(new java.awt.Dimension(10, 18));
            add(acc_fecprodE);
            acc_fecprodE.setBounds(430, 60, 70, 18);

            acc_tipdocL.setBackground(java.awt.Color.orange);
            acc_tipdocL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            acc_tipdocL.setText("Compra");
            acc_tipdocL.setOpaque(true);
            add(acc_tipdocL);
            acc_tipdocL.setBounds(440, 40, 60, 17);
            add(acp_painacE);
            acp_painacE.setBounds(80, 20, 180, 18);
            add(acp_paisacE);
            acp_paisacE.setBounds(80, 40, 220, 18);
            add(acp_engpaiE);
            acp_engpaiE.setBounds(320, 20, 180, 18);
        }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CTextField acc_fecprodE;
    private gnu.chu.controles.CTextField acc_numeE;
    private gnu.chu.controles.CLabel acc_tipdocL;
    private gnu.chu.camposdb.PaiPanel acp_engpaiE;
    private gnu.chu.controles.CLabel acp_feccadL;
    private gnu.chu.controles.CLabel acp_feccadL1;
    private gnu.chu.controles.CTextField acp_fecsacE;
    private gnu.chu.controles.CLabel acp_fecsacL;
    private gnu.chu.controles.CTextField acp_nucrotE;
    private gnu.chu.camposdb.PaiPanel acp_painacE;
    private gnu.chu.camposdb.PaiPanel acp_paisacE;
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.controles.CTextField avc_feccadE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField conservarE;
    private gnu.chu.controles.CLinkBox mat_codiE;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    private gnu.chu.controles.CLinkBox sde_codiE;
    // End of variables declaration//GEN-END:variables
}
