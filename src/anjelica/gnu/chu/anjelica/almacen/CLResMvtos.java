/**
 *
 * <p>Título: CLResMvtos </p>
 * <p>Descripción: Consulta Listado Resultado de Movimientos
 * @see gnu.chu.anjelica.almacen.Comvalm</p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * @author chuchiP
 * @version 1.0
 */
package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;


public class CLResMvtos extends ventana {
  DatosTabla dtPro ;
  private boolean cancelarConsulta=false;
  private MvtosAlma mvtosAlm = new MvtosAlma();


  public CLResMvtos(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./List. Resumen Mvtos Alm.");

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

  public CLResMvtos(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./List. Resumen Mvtos Alm.");
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

      this.setVersion("2011-10-29");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(750,500));
      Pcabe.setDefButton(Baceptar.getBotonAccion());
   
  }
  @Override
  public void iniciarVentana() throws Exception
  {
    mvtosAlm.setAccesoEmp(empPanel.getStringAccesos(dtStat, EU.usuario));
    pdalmace.llenaLinkBox(alm_codiE,dtStat);
    MantFamPro.llenaLinkBox(fam_codiE, dtStat);
    pro_codiE.setCeroIsNull(true);
    dtPro = new DatosTabla(ct);
//    alm_codiE.setText(""+pdconfig.getAlmVentas(EU.em_cod, dtStat));
    pro_codiE.iniciar(dtStat, this, vl, EU);
    tipoProdE.addItem("Congelado", "C");
    tipoProdE.addItem("No Congelado", "N");
    tipoProdE.addItem("Todos", "T");
    tipoProdE.setValor("T");
    emp_codiE.iniciar(dtStat, this, vl, EU);
    emp_codiE.setAceptaNulo(true);
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    sbe_codiE.setAceptaNulo(true);
    emp_codiE.setText("0");
    sbe_codiE.setText("0");
    activarEventos();
    alm_codiE.requestFocus();
  }
  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e)
        {
           if (e.getActionCommand().equals("Listar"))
              listar();
           else
            consultar();
        }
    });
     popEspere_BCancelaraddActionListener(new ActionListener()
       {
        public void actionPerformed(ActionEvent e)
        {
           setCancelarConsulta(true);
           mensaje("A esperar.. estoy cancelando la consulta");
        }
       });
  }
  public void setCancelarConsulta(boolean cancConsulta)
  {
      cancelarConsulta=cancConsulta;
      popEspere_BCancelarSetEnabled(!cancConsulta);
  }
  public boolean isCancelarConsulta()
  {
       return cancelarConsulta;
  }
  private boolean checkCond() throws SQLException {
    if (!emp_codiE.controla()) {
        mensajeErr(emp_codiE.getMsgError());
        return false;
    }
    if (!sbe_codiE.controla()) {
        mensajeErr("SubEmpresa No valida");
        return false;
    }
   
    if (feciniE.getError() || feciniE.isNull()) {
        mensajeErr("Fecha Inicial NO es valida");
        feciniE.requestFocus();
        return false;
    }
    if (fecfinE.getError() || fecfinE.isNull()) {
        mensajeErr("Fecha FINAL NO es valida");
        feciniE.requestFocus();
        return false;
    }
     if ((emp_codiE.getValorInt()!=0 || sbe_codiE.getValorInt()!=0) && opMvtos.isSelected())
    {
        msgBox("No se puede consultar por movimientos si seleccionas empresa o Subempresa");
        emp_codiE.requestFocus();
        return false;
    }
    return true;
  }
  void   consultar()
  {
//      System.out.println("Anchura: "+this.getSize().width+" Alto: "+this.getSize().height);
      try {       
          if (! checkCond())
            return;
      } catch (SQLException k)
      {
          Error("Error al validar campos",k);
          return;
      }
      new miThread("")
      {
            @Override
          public void run()
          {
            msgEspere("Buscando datos ... ");
            buscaDatos();
            resetMsgEspere();
          }
      };
  }
  void buscaDatos()
  {
    int almCodi;
    if (alm_codiE.getText().trim().equals(""))
       almCodi=0;
     else
       almCodi=alm_codiE.getValorInt();

     mvtosAlm.setPadre(this);
     jt.removeAllDatos();
     setCancelarConsulta(false);
     try {
        mvtosAlm.setAlmacen(almCodi);
        mvtosAlm.setEmpresa(emp_codiE.getValorInt());
               
        mvtosAlm.setSbeCodi(sbe_codiE.getValorInt());
        mvtosAlm.setMvtoDesgl(true);
        boolean proCodNull=pro_codiE.isNull();
        String s = "select a.pro_codi,a.pro_nomb "+
                " from v_articulo as a where 1=1 "+
                (fam_codiE.isNull() ? "" : " and a.fam_codi = " + fam_codiE.getValorInt()) +
                (alm_codiE.isNull() ? "" : " and alm_codi =" + alm_codiE.getValorInt()) +
                (tipoProdE.getValor().equals("T") ? "" : " and a.pro_artcon " +
                (tipoProdE.getValor().equals("N") ? "=0" : "!=0")) +
                (proCodNull?
                   (prodVendE.getValor().equals("V")?" and a.pro_tiplot = 'V'":"")+
                   (prodVendE.getValor().equals("C")?
                     " and a.pro_tiplot != 'V'":""):
                        " and pro_codi = "+pro_codiE.getValorInt())+
                " order by pro_codi ";
        if (! dtPro.select(s))
        {
            msgBox("No encontrados Productos para estas condiciones");
            return;
        }
        int unEntT=0,unSalT=0,unComprasT=0,unVentasT=0, unEntDespT=0,unDespSalT=0,unRegT=0;
        double kgEntT=0,kgSalT=0, kgComprasT=0,kgVentasT=0, kgEntDespT=0,kgDespSalT=0,kgRegT=0;
        double impSalT=0,impEntT=0,impComprasT=0,impVentasT=0, impEntDespT=0,impDespSalT=0,impRegT=0;
        double sumaLinea;
        do {
            if (cancelarConsulta)
            {
                mensaje("");
                mensajeErr("Consulta CANCELADA");
                return;
            }
            if (opMvtos.isSelected())
            {
                if (! mvtosAlm.getAcumuladosMvtos(feciniE.getText(), fecfinE.getText(), alm_codiE.getValorInt(),
                    dtPro.getInt("pro_codi"), 0, dtCon1))
                    continue;
            } else
            {
                if (!mvtosAlm.calculaMvtos(feciniE.getText(), feciniE.getText(),
                    fecfinE.getText(), dtPro.getInt("pro_codi"), null, null, 0, dtCon1, dtStat, null))
                    continue;
            }
            ArrayList v =new ArrayList();
            v.add(dtPro.getString("pro_codi"));
            v.add(dtPro.getString("pro_nomb"));

            v.add(mvtosAlm.getUnidadesCompra());
            v.add(mvtosAlm.getKilosCompra());
            v.add(mvtosAlm.getImporteCompra()); // 5
            v.add(mvtosAlm.getUnidadesVenta());
            v.add(mvtosAlm.getKilosVenta());
            v.add(mvtosAlm.getImporteVenta()); // 8
            v.add(mvtosAlm.getUnidadesSalDesp());
            v.add(mvtosAlm.getKilosSalDesp());
            v.add(mvtosAlm.getImporteSalDesp()); // 11
            v.add(mvtosAlm.getUnidadesEntDesp());
            v.add(mvtosAlm.getKilosEntDesp());
            v.add(mvtosAlm.getImporteEntDesp()); //14

            v.add(mvtosAlm.getUnidadesRegul());
            v.add(mvtosAlm.getKilosRegul());
            v.add(mvtosAlm.getImporteRegul()); // 17
            if (proCodNull)
            {
                sumaLinea=mvtosAlm.getUnidadesCompra()+
                        mvtosAlm.getKilosCompra()+
                        mvtosAlm.getImporteCompra() +
                        mvtosAlm.getUnidadesVenta()+
                        mvtosAlm.getKilosVenta()+
                        mvtosAlm.getImporteVenta()+ // 8
                        mvtosAlm.getUnidadesSalDesp()+
                        mvtosAlm.getKilosSalDesp()+
                        mvtosAlm.getImporteSalDesp()+ // 11
                        mvtosAlm.getUnidadesEntDesp()+
                        mvtosAlm.getKilosEntDesp()+
                        mvtosAlm.getImporteEntDesp()+ //14
                        mvtosAlm.getUnidadesRegul()+
                        mvtosAlm.getKilosRegul()+
                        mvtosAlm.getImporteRegul();
                if (sumaLinea==0)
                    continue;
            }
            unEntT+=mvtosAlm.getUnidadesEntrada();
            unSalT+=mvtosAlm.getUnidadesSalida();
            unComprasT+=mvtosAlm.getUnidadesCompra();
            unVentasT+=mvtosAlm.getUnidadesVenta();
            unEntDespT+=mvtosAlm.getUnidadesEntDesp();
            unDespSalT+=mvtosAlm.getUnidadesSalDesp();
            unRegT+=mvtosAlm.getUnidadesRegul();

            kgEntT+=mvtosAlm.getKilosEntrada();
            kgSalT+=mvtosAlm.getKilosSalida();
            kgComprasT+=mvtosAlm.getKilosCompra();
            kgVentasT+=mvtosAlm.getKilosVenta();
            kgEntDespT+=mvtosAlm.getKilosEntDesp();
            kgDespSalT+=mvtosAlm.getKilosSalDesp();
            kgRegT+=mvtosAlm.getKilosRegul();

            impEntT+=mvtosAlm.getImporteEntrada();
            impSalT+=mvtosAlm.getImporteSalida();
            impComprasT+=mvtosAlm.getImporteCompra();
            impVentasT+=mvtosAlm.getImporteVenta();
            impEntDespT+=mvtosAlm.getImporteEntDesp();
            impDespSalT+=mvtosAlm.getImporteSalDesp();
            impRegT+=mvtosAlm.getImporteRegul();

            jt.addLinea(v);
        } while (dtPro.next());
        ArrayList v =new ArrayList();
        v.add("");
        v.add(" ** TOTAL ** ");
        v.add(unComprasT);
        v.add(kgComprasT);
        v.add(impComprasT); // 5
        v.add(unVentasT);
        v.add(kgVentasT);
        v.add(impVentasT); // 8
        v.add(unDespSalT);
        v.add(kgDespSalT);
        v.add(impDespSalT); // 11
        v.add(unEntDespT);
        v.add(kgEntDespT);
        v.add(impEntDespT); //14

        v.add(unRegT);
        v.add(kgRegT);
        v.add(impRegT); // 17
        jt.addLinea(v);
        jt.requestFocusInicio();
        if (opMvtos.isSelected())
        {
            kgEntE.setValorDec(kgEntT);
            unEntE.setValorDec(unEntT);
            impEntE.setValorDec(impEntT);
            kgSalE.setValorDec(kgSalT);
            unSalE.setValorDec(unSalT);
            impSalE.setValorDec(impSalT);
        } 
        else
        {
            Ppie.resetTexto();
        }
        mensaje("");
        mensajeErr("Consulta realizada");
     } catch (Exception k)
     {
         Error("Error al buscar datos",k);
     }
  }
  void listar()
  {
      try {
        if (! checkCond())
            return;
      } catch (SQLException k)
      {
          Error("Error al validar campos",k);
          return;
      }
      msgBox("Funcion SIN Implementar en esta versión");
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
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        tipoProdE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        fam_codiE = new gnu.chu.controles.CLinkBox();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        prodVendE = new gnu.chu.controles.CComboBox();
        prodVendE.addItem("Vendible","V");
        prodVendE.addItem("No Vendible","C");
        prodVendE.addItem("Todos","T");
        opMvtos = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(17);
        ArrayList v=new ArrayList();
        v.add("Producto"); // 0
        v.add("Nombre"); // 1
        v.add("Un.Comp"); // 2
        v.add("Kg.Comp"); // 3
        v.add("Imp.Comp"); // 4
        v.add("Un.Vent"); // 5
        v.add("Kg.Vent"); // 6
        v.add("Imp.V");  // 7
        v.add("Un.SDesp"); // 8
        v.add("Kg.SDesp"); // 9
        v.add("Imp.SDesp"); // 10
        v.add("Un.EDesp"); // 11
        v.add("Kg.EDesp"); // 12
        v.add("Imp.EDesp"); // 13
        v.add("Un.Reg."); // 14
        v.add("Kg.Reg."); // 15
        v.add("Imp.Reg."); // 16
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{55,200,40,60,60,40,60,60,40,60,60,40,60,60,40,60,60});
        jt.setAlinearColumna(new int[]{2,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2});
        jt.setFormatoColumna(2, "--,--9");
        jt.setFormatoColumna(3, "---,--9.9");
        jt.setFormatoColumna(4, "--,---,--9");
        jt.setFormatoColumna(5, "--,--9");
        jt.setFormatoColumna(6, "---,--9.9");
        jt.setFormatoColumna(7, "--,---,--9");
        jt.setFormatoColumna(8, "--,--9");
        jt.setFormatoColumna(9, "---,--9.9");
        jt.setFormatoColumna(10, "--,---,--9");
        jt.setFormatoColumna(11, "--,--9");
        jt.setFormatoColumna(12, "---,--9.9");
        jt.setFormatoColumna(13, "--,---,--9");
        jt.setFormatoColumna(14, "--,--9");
        jt.setFormatoColumna(15, "---,--9.9");
        jt.setFormatoColumna(16, "----,--9");
        Ppie = new gnu.chu.controles.CPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        cLabel10 = new gnu.chu.controles.CLabel();
        kgEntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        kgSalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel11 = new gnu.chu.controles.CLabel();
        unEntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        cLabel12 = new gnu.chu.controles.CLabel();
        unSalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        cLabel13 = new gnu.chu.controles.CLabel();
        impEntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel14 = new gnu.chu.controles.CLabel();
        impSalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(550, 105));
        Pcabe.setMinimumSize(new java.awt.Dimension(550, 105));
        Pcabe.setPreferredSize(new java.awt.Dimension(550, 105));
        Pcabe.setLayout(null);

        cLabel1.setText("Almacen");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 2, 62, 18);

        alm_codiE.setAncTexto(30);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(70, 2, 240, 18);

        cLabel2.setText("Tipo Prod.");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(2, 51, 62, 18);

        tipoProdE.setMinimumSize(new java.awt.Dimension(32, 20));
        Pcabe.add(tipoProdE);
        tipoProdE.setBounds(68, 51, 142, 18);

        cLabel3.setText("Producto");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(2, 28, 62, 18);
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(70, 28, 340, 18);

        feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(feciniE);
        feciniE.setBounds(70, 74, 79, 18);

        cLabel4.setText("De Fecha");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(2, 74, 62, 18);

        cLabel5.setText("A Fecha");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(167, 74, 43, 18);

        fecfinE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecfinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(fecfinE);
        fecfinE.setBounds(216, 74, 79, 18);

        cLabel6.setText("Empresa");
        cLabel6.setPreferredSize(new java.awt.Dimension(49, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(320, 2, 49, 18);

        emp_codiE.setPreferredSize(new java.awt.Dimension(29, 18));
        Pcabe.add(emp_codiE);
        emp_codiE.setBounds(370, 2, 41, 18);

        cLabel7.setText("SubEmpresa");
        cLabel7.setPreferredSize(new java.awt.Dimension(70, 18));
        Pcabe.add(cLabel7);
        cLabel7.setBounds(430, 2, 70, 18);

        sbe_codiE.setPreferredSize(new java.awt.Dimension(29, 18));
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(500, 2, 38, 18);

        cLabel8.setText("Familia");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(220, 51, 38, 18);

        fam_codiE.setAncTexto(30);
        fam_codiE.setMinimumSize(new java.awt.Dimension(64, 20));
        fam_codiE.setPreferredSize(new java.awt.Dimension(80, 20));
        Pcabe.add(fam_codiE);
        fam_codiE.setBounds(270, 51, 270, 18);

        Baceptar.setText("Aceptar");
        Baceptar.addMenu("Consultar");
        Baceptar.addMenu("Listar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(370, 70, 170, 30);
        Pcabe.add(prodVendE);
        prodVendE.setBounds(420, 28, 130, 18);

        opMvtos.setSelected(true);
        opMvtos.setText("Mvtos");
        opMvtos.setToolTipText("Usa mvtos en vez de documentos");
        Pcabe.add(opMvtos);
        opMvtos.setBounds(300, 74, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMinimumSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 651, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Ppie.setEnabled(false);
        Ppie.setMaximumSize(new java.awt.Dimension(500, 40));
        Ppie.setMinimumSize(new java.awt.Dimension(500, 40));
        Ppie.setPreferredSize(new java.awt.Dimension(500, 40));
        Ppie.setQuery(true);
        Ppie.setLayout(null);

        cLabel9.setText("KilosSalida ");
        Ppie.add(cLabel9);
        cLabel9.setBounds(10, 20, 80, 17);

        cLabel10.setText("Kilos Entrada ");
        Ppie.add(cLabel10);
        cLabel10.setBounds(10, 0, 80, 17);
        Ppie.add(kgEntE);
        kgEntE.setBounds(90, 0, 70, 17);
        Ppie.add(kgSalE);
        kgSalE.setBounds(90, 20, 70, 17);

        cLabel11.setText("Unid. Entrada ");
        Ppie.add(cLabel11);
        cLabel11.setBounds(180, 0, 80, 17);
        Ppie.add(unEntE);
        unEntE.setBounds(260, 0, 50, 17);

        cLabel12.setText("Unid. Salida ");
        Ppie.add(cLabel12);
        cLabel12.setBounds(180, 20, 80, 17);
        Ppie.add(unSalE);
        unSalE.setBounds(260, 20, 50, 17);

        cLabel13.setText("Imp. Entrada ");
        Ppie.add(cLabel13);
        cLabel13.setBounds(320, 0, 80, 17);
        Ppie.add(impEntE);
        impEntE.setBounds(400, 0, 80, 17);

        cLabel14.setText("Imp. Salida ");
        Ppie.add(cLabel14);
        cLabel14.setBounds(320, 20, 80, 17);
        Ppie.add(impSalE);
        impSalE.setBounds(400, 20, 80, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CLinkBox fam_codiE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CTextField impEntE;
    private gnu.chu.controles.CTextField impSalE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kgEntE;
    private gnu.chu.controles.CTextField kgSalE;
    private gnu.chu.controles.CCheckBox opMvtos;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CComboBox prodVendE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CComboBox tipoProdE;
    private gnu.chu.controles.CTextField unEntE;
    private gnu.chu.controles.CTextField unSalE;
    // End of variables declaration//GEN-END:variables

}
