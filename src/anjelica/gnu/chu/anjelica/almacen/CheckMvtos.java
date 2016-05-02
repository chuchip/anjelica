/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.almacen;

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
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author chuchip
 */
public class CheckMvtos extends ventana
{
  MvtosAlma mvtos;
  
  public CheckMvtos(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

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

      this.setVersion("2016-05-02");
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
                  if (! swIgn)
                  {
                    dtInd = new DatIndivMvto(dtCon1.getInt("alm_codi"), dtCon1.getInt("pro_codori"),
                        dtCon1.getInt("ejeNume"), dtCon1.getString("serie"), dtCon1.getInt("lote"), dtCon1.getInt("numind"));
                    row = listIndiv.indexOf(dtInd);
                    if (row >= 0)
                        dtInd = listIndiv.get(row);

                    dtInd.setCanti((dtCon1.getString("tipmov").equals("=") ? 0
                        : dtInd.getCanti()) + dtCon1.getDouble("canti")
                        * (dtCon1.getString("tipmov").equals("-") ? -1 : 1));
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
                  dtInd = new DatIndivMvto(dtCon1.getInt("alm_codi"), dtCon1.getInt("pro_codori"),
                      dtCon1.getInt("ejeNume"), dtCon1.getString("serie"), dtCon1.getInt("lote"), dtCon1.getInt("numind"));
                  row = listIndiv.indexOf(dtInd);
                  if (row >= 0)
                      dtInd = listIndiv.get(row);
                  dtInd.setCanti2((dtCon1.getString("tipmov").equals("=") ? 0
                      : dtInd.getCanti2()) + dtCon1.getDouble("canti")
                      * (dtCon1.getString("tipmov").equals("S") ? -1 : 1));
                  dtInd.setNumuni2((dtCon1.getString("tipmov").equals("=") ? 0 : dtInd.getNumuni2())
                      + dtCon1.getInt("unidades") * (dtCon1.getString("tipmov").equals("S") ? -1 : 1));
                  if (row >= 0)
                      listIndiv.set(row, dtInd);
                  else
                      listIndiv.add(dtInd);
              } while (dtCon1.next());
          }
             jt.removeAllDatos();
          for (DatIndivMvto dtInd1 : listIndiv)
          {
            if ( Formatear.redondea(dtInd1.getCanti()-dtInd1.getCanti2(),2)!=0 ||
                Formatear.redondea(dtInd1.getNumuni()-dtInd1.getNumuni2(),2)!=0)
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
        jt = new gnu.chu.controles.Cgrid(11);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(399, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(399, 50));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(399, 50));
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
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{30,50,150,40,30,50,40,50,50,40,40});
        jt.setAlinearColumna(new int[]{2,2,0,2,1,2,2,2,2,2,2});
        jt.setFormatoColumna(7, "--,--9.99");
        jt.setFormatoColumna(8, "--,--9.99");
        jt.setFormatoColumna(9, "###9");
        jt.setFormatoColumna(10, "###9");
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(529, 130));
        jt.setMinimumSize(new java.awt.Dimension(529, 130));
        jt.setPreferredSize(new java.awt.Dimension(529, 130));
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
    // End of variables declaration//GEN-END:variables
}

class DatIndivMvto extends DatIndivBase
{
    int numunid2=0;
    double canti2=0;

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
