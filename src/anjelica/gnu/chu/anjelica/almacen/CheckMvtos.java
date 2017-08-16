package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Título: CheckMvtos</p>
 * <p>Descripcion: Comprueba tabla movimientos contra tablas de documentos. Buscando inconsitencias </p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CheckMvtos extends ventana
{
  MvtosAlma mvtos;
  final int JT_PROCOD=1;
  final int JT_EJERC=3;  
  final int JT_SERIE=4;
  final int JT_LOTE=5;
  final int JT_INDIV=6;
  final int JT_PRMVT=11;
  final int JT_PRDOC=12;
  PreparedStatement psAlb;
  PreparedStatement psMvt;
  public CheckMvtos(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;
    setAcronimo("chkmvt");
    setTitulo("Comprueba mvtos VS Documentos");

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

  public CheckMvtos(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Comprueba mvtos VS Documentos");
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

      this.setVersion("2017-08-01");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(750,500));
      Pcabe.setDefButton(Baceptar);
   
  }
  @Override
  public void iniciarVentana() throws Exception
  { 
    pdalmace.llenaLinkBox(alm_codiE,dtStat,'*');
    mvtos=new MvtosAlma();
    activarEventos();
   
  }
  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e)
        {
            consultar();
        }
    });
    BRegenerar.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e)
        {
           if (jt.isVacio() || !opSoloVentas.isSelected())
           {
               mensajeErr("Imposible regenerar costos");
               return;
           }
           regenerarCostos();
        }
    });
    jt.addMouseListener(new MouseAdapter()
    {
           @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount()<2 || jt.isVacio() || jf==null)
                return;
            Comvalm.ir(jf,jt.getValorInt(JT_PROCOD),
                jt.getValorInt(JT_EJERC),
                jt.getValString(JT_SERIE),
                jt.getValorInt(JT_LOTE),
                jt.getValorInt(JT_INDIV));
        }
    });
  }
  
  void consultar()
  {
      if (feciniE.getError() || fecfinE.getError())
          return;
      if (feciniE.isNull() )
      {
          mensajeErr("Introduzca fecha inicio");
          feciniE.requestFocus();
          return;
      }
      if (fecfinE.isNull() )
      {
          mensajeErr("Introduzca fecha Final");
          fecfinE.requestFocus();
          return;
      }

      new miThread("")
      {
          @Override
          public void run()
          {
              msgEspere("Espere, buscando datos");
              jt.tableView.setVisible(false);
              consultar0();
              jt.tableView.setVisible(true);
              resetMsgEspere();
              msgBox("Consulta terminada");
          }
      };
  }
  void regenerarCostos()
  {
      int nRows = jt.getRowCount();
      try
      {
          String s="select * from v_albventa_detalle where pro_codi = ?"+
            " and avp_ejelot = ?"+
            " and avp_serlot = ?"+
            " and avp_numpar = ?"+
            " and avp_numind = ?";
          
          psAlb=dtCon1.getPreparedStatement(s);
          s="update mvtosalm set mvt_prec = ?,mvt_prenet=? where pro_codi = ?"+
            " and pro_ejelot = ?"+
            " and pro_serlot = ?"+
            " and pro_numlot = ?"+
            " and pro_indlot = ?"+
            " and mvt_tipdoc= 'V'"+
            " and mvt_ejedoc= ?"+
            " and mvt_serdoc= ?"+
            " and mvt_numdoc= ?";
          psMvt=dtAdd.getPreparedStatement(s);
          for (int n = 0; n < nRows; n++)
          {

              if (jt.getValorDec(n, JT_PRMVT) == jt.getValorDec(n, JT_PRDOC))
                  continue;
              regeneraCosto(jt.getValorInt(n, JT_PROCOD), jt.getValorInt(n, JT_EJERC),
                  jt.getValString(n, JT_SERIE), jt.getValorInt(n, JT_LOTE),
                  jt.getValorInt(n, JT_INDIV));
          }
          dtAdd.commit();
          msgBox("Actualizados precios de Mvtos");
      } catch (SQLException ex)
      {
          Error("Error al regenerar costos", ex);
      }
  }
  
  void regeneraCosto(int proCodi,int ejerc,String serie,int lote,int indiv) throws SQLException
  {
    psAlb.setInt(1,proCodi);
    psAlb.setInt(2,ejerc);
    psAlb.setString(3,serie);
    psAlb.setInt(4,lote);
    psAlb.setInt(5,indiv);
    ResultSet rs=psAlb.executeQuery();
    if (!rs.next())
        return; // No encontrado documento en ventas
    do
    {
        psMvt.setDouble(1,rs.getDouble("avl_prven"));
        psMvt.setDouble(2,rs.getDouble("avl_prbase"));
        psMvt.setInt(3,proCodi);
        psMvt.setInt(4,ejerc);
        psMvt.setString(5,serie);
        psMvt.setInt(6,lote);
        psMvt.setInt(7,indiv);
        psMvt.setInt(8,rs.getInt("avc_ano") );
        psMvt.setString(9,rs.getString("avc_serie"));
        psMvt.setInt(10,rs.getInt("avc_nume"));
        psMvt.executeUpdate();
    } while (rs.next());
  }
  void consultar0()
  {      
      try
      {
          ArrayList<DatIndivMvto>  listIndiv=new ArrayList(); 
          mvtos.setUsaDocumentos(true);
          mvtos.setFechasDocumento(false);
          mvtos.setDesglIndiv(true);
          mvtos.setIncIniFechaInv(true);
          mvtos.setIncluyeSerieX(true);
          int almCodi=alm_codiE.getValorInt();
          mvtos.setAlmacen(almCodi);
          String sql=mvtos.getSqlMvt(feciniE.getFecha("dd-MM-yyyy"), fecfinE.getFecha("dd-MM-yyyy"),0);
          DatIndivMvto dtInd;
          boolean swIgn;
          int row;       
          boolean swSoloVentas=opSoloVentas.isSelected();
          double canti;
          if (dtCon1.select(sql))
          {
              do
              {
                  swIgn=false;
                  if (dtCon1.getString("avc_serie").equals("X") )
                  {
                      if (almCodi!=0 && almCodi !=dtCon1.getInt("alm_codi"))
                          swIgn=true;
                  }
                  if (swSoloVentas && !dtCon1.getString("sel").equals("VE"))
                         swIgn=true;
               
                  if (! swIgn)
                  {
//                     if ( !(dtCon1.getInt("pro_codori")==40801 && dtCon1.getInt("lote")==10299 &&   dtCon1.getInt("numind")==3))
//                         continue;
                     dtInd = new DatIndivMvto(dtCon1.getInt("alm_codi"), dtCon1.getInt("pro_codori"),
                        dtCon1.getInt("ejeNume"), dtCon1.getString("serie"), dtCon1.getInt("lote"), dtCon1.getInt("numind"));
                    row = listIndiv.indexOf(dtInd);
                    if (row >= 0)
                        dtInd = listIndiv.get(row);
                    canti=Formatear.redondea( dtCon1.getDouble("canti")
                        * (dtCon1.getString("tipmov").equals("-") ? -1 : 1),2);
                    
                    dtInd.setCanti(dtCon1.getString("tipmov").equals("=") ? 0:dtInd.getCanti() + canti);
                    if (dtCon1.getString("sel").equals("CO") || dtCon1.getString("sel").equals("VE"))
                        dtInd.setPrecio(
                            Formatear.redondea(dtInd.getPrecio() +  (canti * dtCon1.getDouble("precio",true)),2));
                    dtInd.setNumuni((dtCon1.getString("tipmov").equals("=") ? 0 : dtInd.getNumuni())
                        + dtCon1.getInt("unidades") * (dtCon1.getString("tipmov").equals("-") ? -1 : 1));
                    if (row >= 0)
                        listIndiv.set(row, dtInd);
                    else
                        listIndiv.add(dtInd);
                  }
                  if (dtCon1.getString("avc_serie").equals("X") )
                  {
                      if (almCodi!=0 && almCodi !=dtCon1.getInt("alm_coddes"))
                          continue;
                      dtInd = new DatIndivMvto(dtCon1.getInt("alm_coddes"), dtCon1.getInt("pro_codori"),
                        dtCon1.getInt("ejeNume"), dtCon1.getString("serie"), dtCon1.getInt("lote"), dtCon1.getInt("numind"));
                      row = listIndiv.indexOf(dtInd);
                      if (row >= 0)
                            dtInd = listIndiv.get(row);
                 
                      dtInd.setCanti(dtInd.getCanti() + dtCon1.getDouble("canti"));                     
                      dtInd.setNumuni(dtInd.getNumuni()+ dtCon1.getInt("unidades") );
                      if (row >= 0)
                            listIndiv.set(row, dtInd);
                      else
                            listIndiv.add(dtInd);
                  }
              } while (dtCon1.next());
          }
          mvtos.setUsaDocumentos(false);
          sql=mvtos.getSqlMvt(feciniE.getFecha("dd-MM-yyyy"), fecfinE.getFecha("dd-MM-yyyy"),0);
          if (dtCon1.select(sql))
          {
              do
              {
                  swIgn=false;
                  if (dtCon1.getString("avc_serie").equals("X") )
                  {
                      if (almCodi!=0 && almCodi !=dtCon1.getInt("alm_codi"))
                          swIgn=true;
                  }
                  if (swSoloVentas && !dtCon1.getString("sel").equals("V"))
                         swIgn=true;
                  //                
                
                  //
                  if (!swIgn)
                  {
//                   if ( ! (dtCon1.getInt("pro_codori")==40801 && dtCon1.getInt("lote")==10299 &&   dtCon1.getInt("numind")==3))
//                       continue;
                    dtInd = new DatIndivMvto(dtCon1.getInt("alm_codi"), dtCon1.getInt("pro_codori"),
                        dtCon1.getInt("ejeNume"), dtCon1.getString("serie"), dtCon1.getInt("lote"), dtCon1.getInt("numind"));
                    row = listIndiv.indexOf(dtInd);
                    if (row >= 0)
                        dtInd = listIndiv.get(row);
                 
                    canti=Formatear.redondea( dtCon1.getDouble("canti")
                        * (dtCon1.getString("tipmov").equals("S") ? -1 : 1),2);
                    dtInd.setCanti2(dtCon1.getString("tipmov").equals("=")?0: dtInd.getCanti2()+canti);
                    if (dtCon1.getString("sel").equals("C") || dtCon1.getString("sel").equals("V"))
                      dtInd.setPrecio2(Formatear.redondea(dtInd.getPrecio2() +  (canti * dtCon1.getDouble("precioneto",true)),2));
                    dtInd.setNumuni2((dtCon1.getString("tipmov").equals("=") ? 0 : dtInd.getNumuni2())
                        + dtCon1.getInt("unidades") * (dtCon1.getString("tipmov").equals("S") ? -1 : 1));
                    if (row >= 0)
                        listIndiv.set(row, dtInd);
                    else
                        listIndiv.add(dtInd);
                  }
              } while (dtCon1.next());
          }
             jt.removeAllDatos();
          double precio,precio2;
          for (DatIndivMvto dtInd1 : listIndiv)
          {
            precio=dtInd1.getCanti()==0?0: Formatear.redondea(dtInd1.getPrecio()/dtInd1.getCanti(),2);
            precio2=dtInd1.getCanti2()==0?0: Formatear.redondea(dtInd1.getPrecio2()/dtInd1.getCanti2(),2);
            if (dtInd1.getCanti()!=dtInd1.getCanti2()||
                //Formatear.redondea(dtInd1.getNumuni()-dtInd1.getNumuni2(),0)!=0 || 
               precio!= precio2 )
            {
                ArrayList v=new ArrayList();
                v.add(dtInd1.getAlmCodi());
                v.add(dtInd1.getProducto());
                v.add(MantArticulos.getNombProd(dtInd1.getProducto(), dtCon1));
                v.add(dtInd1.getEjercLot());
                v.add(dtInd1.getSerie());
                v.add(dtInd1.getLote());
                v.add(dtInd1.getNumind());
                v.add(dtInd1.getCanti());
                v.add(dtInd1.getCanti2());
                v.add(dtInd1.getNumuni());
                v.add(dtInd1.getNumuni2());
                v.add(precio);
                v.add(precio2);

                jt.addLinea(v);
            }
          }
         
         
          jt.requestFocusInicio();
          
      } catch (ParseException | SQLException ex)
      {
          Error("Error al buscar mvtos", ex);
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
        cLabel1 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel5 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        opSoloVentas = new gnu.chu.controles.CCheckBox();
        BRegenerar = new gnu.chu.controles.CButton(Iconos.getImageIcon("run"));
        jt = new gnu.chu.controles.Cgrid(13);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(450, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(450, 50));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(450, 50));
        Pcabe.setLayout(null);

        cLabel1.setText("Almacen");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 2, 62, 18);

        alm_codiE.setAncTexto(30);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(70, 2, 240, 18);

        cLabel4.setText("De Fecha");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(0, 22, 62, 18);

        feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(feciniE);
        feciniE.setBounds(70, 22, 79, 18);

        cLabel5.setText("A Fecha");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(170, 22, 43, 18);

        fecfinE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecfinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecfinE);
        fecfinE.setBounds(220, 22, 79, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(300, 20, 90, 19);

        opSoloVentas.setText("Solo Ventas");
        Pcabe.add(opSoloVentas);
        opSoloVentas.setBounds(311, 2, 88, 17);

        BRegenerar.setToolTipText("Regenerar Precios");
        Pcabe.add(BRegenerar);
        BRegenerar.setBounds(405, 20, 20, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        ArrayList v=new ArrayList();
        v.add("Alm"); // 0
        v.add("Prod."); // 1
        v.add("Nombre"); // 2
        v.add("Ejer"); // 3
        v.add("Serie"); // 4
        v.add("Lote"); //5
        v.add("Ind"); // 6
        v.add("Kilos Doc"); // 7
        v.add("Kilos Mvt"); // 7
        v.add("Unid Doc" ); // 8
        v.add("Unid Mvt" ); // 8
        v.add("€ Doc" ); // 9
        v.add("€ Mvt" ); // 10

        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{30,50,150,40,30,50,40,50,50,40,40,40,40});
        jt.setAlinearColumna(new int[]{2,2,0,2,1,2,2,2,2,2,2,2,2});
        jt.setFormatoColumna(7, "--,--9.99");
        jt.setFormatoColumna(8, "--,--9.99");
        jt.setFormatoColumna(9, "###9");
        jt.setFormatoColumna(10, "###9");
        jt.setFormatoColumna(11, "--9.99");
        jt.setFormatoColumna(12, "--9.99");

        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(529, 130));
        jt.setMinimumSize(new java.awt.Dimension(529, 130));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jt, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BRegenerar;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CCheckBox opSoloVentas;
    // End of variables declaration//GEN-END:variables
}

class DatIndivMvto extends DatIndivBase
{
    int numunid2=0;
    double canti2=0;
    double precio2=0;
    
    public int getNumuni2() {
        return numunid2;
    }

    public void setNumuni2(int unid) {
        this.numunid2 = unid;
    }

    public double getCanti2() {
        return canti2;
    }

    public void setCanti2(double canti) {
        this.canti2 = canti;
    }
     public void setPrecio2(double precio) {
        this.precio2 = precio;
    }
    public double getPrecio2() {
        return precio2;
    }
    public DatIndivMvto(int almCodi,int proCodi,int ejeNume,String serie, int lote, int numind)
    {
        setAlmCodi(almCodi);
        setProducto(proCodi);
        setEjercLot(ejeNume);
        setSerie(serie);
        setLote(lote);        
        setNumind(numind);
    }
    
}
