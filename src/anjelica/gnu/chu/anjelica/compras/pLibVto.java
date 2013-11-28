package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import java.awt.*;
import java.sql.Types;
import java.util.*;
import gnu.chu.sql.*;
import java.sql.*;
import javax.swing.BorderFactory;
import gnu.chu.anjelica.riesgos.clFactCob;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventanaPad;
import gnu.chu.utilidades.navegador;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
* <p>Titulo:   plibrovto </p>
 * <p>Descripción: Panel Para meter los vtos. sobre las facturas de compras.<br>
 * Tambien Permite Agrupar las facturas para crear grupos de pago.
 * <p>Copyright: Copyright (c) 2005-2009
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

public class pLibVto extends CPanel
{
  boolean verPagoVto=false;
  gnu.chu.anjelica.tesoreria.ppagreal pPagos = new gnu.chu.anjelica.tesoreria.ppagreal();
  boolean totPagado=false;
  vlike lkFpa=new vlike();
  CTextField lbv_numeE = new CTextField(Types.DECIMAL, "##9");
  CTextField lbv_fecvtoE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField lbv_impvtoE = new CTextField(Types.DECIMAL, "---,--9.99");
  CTextField lbv_imppagE = new CTextField(Types.DECIMAL, "---,--9.99");
  CTextField lbv_comenE = new CTextField(Types.CHAR, "X",255);
  CCheckBox lbv_pagadoE = new CCheckBox("S", "N");
  boolean isEnable=true;
  String msgError;
  CTabbedPane tPane1 = new CTabbedPane();
  CGridEditable jtVto;
  StatusBar statusBar;
  CGridEditable jtGru;
  CTextField fcc_fecfraE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField fcc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CTextField fcc_sumtotE = new CTextField(Types.DECIMAL,"----,--9.99");
  CCheckBox fcc_conpagE= new CCheckBox("S","N");
  CTextField fcc_comentE = new CTextField(Types.CHAR,"X",150);
  int fcc_numgru=0;
  int emp_codi,eje_nume, fcc_nume;
  ventanaPad padre;
//  String fvcFecfra;
  DatosTabla dtStat;
  int fpa_codi=0;
  CPanel Ppie = new CPanel();
  CLabel cLabel2 = new CLabel();
  CTextField numFrasE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel3 = new CLabel();
  CTextField impFrasE = new CTextField(Types.DECIMAL,"----,--9.99");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  int prv_codi=0;
  String gfc_orige;
  public pLibVto(StatusBar statusBar) throws Exception
  {
    this.statusBar=statusBar;
    jbInit();
  }
  private void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
    confGridVto();
    jtGru=confGridFac();
    impFrasE.setEnabled(false);
    numFrasE.setEnabled(false);
    Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
    Ppie.setMaximumSize(new Dimension(413, 22));
    Ppie.setMinimumSize(new Dimension(413, 22));
    Ppie.setPreferredSize(new Dimension(413, 22));
    Ppie.setLayout(null);
    cLabel2.setText("Num. Fras");
    cLabel2.setBounds(new Rectangle(11, 2, 58, 17));
    cLabel3.setText("Importe Fras.");
    cLabel3.setBounds(new Rectangle(108, 2, 77, 17));
    impFrasE.setBounds(new Rectangle(186, 2, 83, 17));
    jtGru.setMaximumSize(new Dimension(524, 77));
    jtGru.setMinimumSize(new Dimension(524, 77));
    jtGru.setPreferredSize(new Dimension(524, 77));
    jtGru.setPuntoDeScroll(50);
    numFrasE.setBounds(new Rectangle(69, 2, 26, 17));
    tPane1.setMaximumSize(new Dimension(524, 107));
    tPane1.setMinimumSize(new Dimension(524, 107));
    tPane1.setPreferredSize(new Dimension(524, 107));
    tPane1.setTabPlacement(CTabbedPane.LEFT);
    this.add(jtVto,     new GridBagConstraints(0, 0, 1, 1, 1.0, 2.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    tPane1.add(jtGru,"Grupos");
    tPane1.add(pPagos,"Pagos");
    Ppie.add(cLabel2, null);
    Ppie.add(numFrasE, null);
    Ppie.add(impFrasE, null);
    Ppie.add(cLabel3, null);
    this.add(tPane1,      new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(Ppie,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -132, 0));
  }


  public void iniciar(ventanaPad papa, DatosTabla dt)
  {
    padre = papa;
    dtStat = dt;
    activarEventos();
  }
  private void activarEventos()
  {
    jtVto.tableView.getSelectionModel().addListSelectionListener(new
       ListSelectionListener()
   {
     public void valueChanged(ListSelectionEvent e)
     {

       if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
         return;
       actDatPagos();
     }
   });
  }

  private void actDatPagos()
  {
    if (jtVto.isEnabled() || ! verPagoVto)
      return;
    try {
      pPagos.buscaDatos(gfc_orige, emp_codi, eje_nume, fcc_nume, jtVto.getValorInt(0,jtVto.getSelectedRowDisab()), dtStat);
    } catch (Exception k)
    {
      padre.Error("Error al buscar datos de pago",k);
    }
  }
  public void setDatosFra(String gfcOrige, int empCodi, int ejeNume, int fccNume)
  {
    gfc_orige=gfcOrige;
    emp_codi = empCodi;
    eje_nume = ejeNume;
    fcc_nume = fccNume;
  }
  public void setProveedor(int prvCodi)
  {
    prv_codi=prvCodi;
  }
  private void confGridVto() throws Exception
  {
    jtVto = new CGridEditable(6)
    {
      public boolean deleteLinea(int row, int col)
      {
        if (jtVto.getValorDec(row, 3) != 0)
        {
          mensajeErr("No se puede borrar un Vto con pagos ya realizados");
          return false;
        }
        return true;
      }

      public int cambiaLinea(int row, int col)
      {
        return cambiaLineaVto();
      }
    };

    Vector vv = new Vector();
    vv.addElement("NV"); // 0
    vv.addElement("Fec.Vto"); // 1
    vv.addElement("Importe"); // 2
    vv.addElement("Imp.Pag."); //3
    vv.addElement("Pag."); // 4
    vv.addElement("Coment"); // 5
    jtVto.setCabecera(vv);
    jtVto.setMaximumSize(new Dimension(525, 122));
    jtVto.setMinimumSize(new Dimension(525, 122));
    jtVto.setPreferredSize(new Dimension(525, 122));

    jtVto.setAnchoColumna(new int[]
                          {30, 90, 80, 80, 50, 250});
    jtVto.setAlinearColumna(new int[]
                            {2, 1, 2, 2, 1, 0});
    jtVto.setFormatoColumna(2, "---,--9.99");
    jtVto.setFormatoColumna(3, "---,--9.99");
    jtVto.setFormatoColumna(4, "BSN");
    jtVto.resetRenderer(4);
    ArrayList vvc = new ArrayList();
    lbv_numeE.setEnabled(false);
    lbv_imppagE.setEnabled(false);
    lbv_pagadoE.setEnabled(false);

    vvc.add(lbv_numeE);
    vvc.add(lbv_fecvtoE);
    vvc.add(lbv_impvtoE);
    vvc.add(lbv_imppagE);
    vvc.add(lbv_pagadoE);
    vvc.add(lbv_comenE);
    jtVto.setCampos(vvc);
  }
  private CGridEditable confGridFac() throws Exception
  {
    CGridEditable jt = new CGridEditable(6)
    {
      public void afterCambiaLinea()
      {
        actAcumFra();
      }

      public int cambiaLinea(int row, int col)
      {
        return cambiaLineaFra();
      }

      public void cambiaColumna(int col, int colNueva, int row)
      {
        try {
          String msgErr;
          if (col == 1 && eje_numeE.getValorInt() != 0 && fcc_numeE.getValorInt() != 0)
          {
            if (eje_numeE.getValorInt()==eje_nume && fcc_numeE.getValorInt()==fcc_nume)
            {
              mensajeErr("NO se puede agrupar a si mismo");
              fcc_fecfraE.resetTexto();
              fcc_sumtotE.resetTexto();
              fcc_comentE.resetTexto();
              return;
            }
            msgErr=buscaDatFra(emp_codi, eje_numeE.getValorInt(), fcc_numeE.getValorInt(), dtStat);
            if (msgErr==null)
            {
              ponValDefGrid(this,dtStat,row);
              ponValDefCam(dtStat);
            }
            else
            {
              mensajeErr(msgErr);
              fcc_fecfraE.resetTexto();
              fcc_sumtotE.resetTexto();
              fcc_comentE.resetTexto();
            }
          }
        } catch (Exception k)
        {
          padre.Error("Error al Buscar datos de factura",k);
        }
      }
    };
    Vector v=new Vector();
    v.addElement("Ejer."); // 0
    v.addElement("Fra."); // 1
    v.addElement("Fec.Fra"); // 2
    v.addElement("Imp.Fra"); // 3
    v.addElement("CP"); // 4  Conf. Pago
    v.addElement("Coment"); // 5 Comentario
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{40,50,80,80,40,250});
    jt.setAlinearColumna(new int[]{2,2,1,2,1,0});
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(4,"BSN");
    fcc_fecfraE.setEnabled(false);
    fcc_sumtotE.setEnabled(false);
    fcc_comentE.setEnabled(false);
    fcc_conpagE.setEnabled(false);
    ArrayList vc=new ArrayList();
    vc.add(eje_numeE);
    vc.add(fcc_numeE);
    vc.add(fcc_fecfraE);
    vc.add(fcc_sumtotE);
    vc.add(fcc_conpagE);
    vc.add(fcc_comentE);
    jt.setCampos(vc);
    return jt;
  }
  private void ponValDefGrid(Cgrid jt, DatosTabla dt,int row) throws SQLException
  {
    jt.setValor(dt.getFecha("fcc_fecfra"), row, 2);
    jt.setValor(dt.getString("fcc_sumtot"), row, 3);
    jt.setValor(dt.getString("fcc_conpag"), row, 4);
    jt.setValor(dt.getString("fcc_coment"), row, 5);
  }
  private void ponValDefCam(DatosTabla dt) throws SQLException
  {
    fcc_fecfraE.setText(dt.getFecha("fcc_fecfra"));
    fcc_sumtotE.setText(dt.getString("fcc_sumtot"));
    fcc_conpagE.setSelecion(dt.getString("fcc_conpag"));
    fcc_comentE.setText(dt.getString("fcc_coment"));
  }
  int cambiaLineaFra()
  {
    if (eje_numeE.getValorInt()==0 || fcc_numeE.getValorInt()==0)
      return -1;
    if (fcc_fecfraE.isNull())
      return 0;
        return -1;
  }
  void mensajeErr(String msg)
  {
    padre.mensajeErr(msg);
  }
  String buscaDatFra(int empCodi, int ejeNume, int fccNume, DatosTabla dt) throws SQLException
  {
    return buscaDatFra(empCodi,ejeNume,fccNume,prv_codi,dt);
  }
  String buscaDatFra(int empCodi, int ejeNume, int fccNume,int prvCodi, DatosTabla dt) throws SQLException
  {
    String s;
    if (gfc_orige.equals("C"))
    {
      s = "SELECT * FROM v_facaco WHERE emp_codi = " + empCodi +
          " and eje_nume = " + ejeNume +
          " and fcc_nume = " + fccNume;
      if (!dt.select(s))
        return "Factura NO encontrada";
      if (dt.getInt("prv_codi") != prvCodi)
      {
        if (padre.nav.pulsado == navegador.ADDNEW || padre.nav.pulsado == navegador.ADDNEW)
          return "Factura NO es de Proveedor " + prv_codi;
      }
    }
    else
    {
      s = "SELECT tra_codi,frt_fecha as fcc_fecfra,frt_imptot as fcc_sumtot,'S' AS fcc_conpag,'' as fcc_coment FROM fratraca WHERE emp_codi = " + empCodi +
          " and eje_nume = " + ejeNume +
          " and frt_nume = " + fccNume;
      if (!dt.select(s))
        return "Factura NO encontrada";
      if (dt.getInt("tra_codi") != prvCodi)
      {
        if (padre.nav.pulsado == navegador.ADDNEW || padre.nav.pulsado == navegador.ADDNEW)
          return "Factura NO es de Transportista " + prv_codi;
      }

    }
    return null;
  }
  /**
   * Indica el Grupo inicial de la Factura
   * @param grupo int
   */
  public void setGrupo(int grupo)
  {
    fcc_numgru=grupo;
  }
  public CGridEditable getGridGrupo()
  {
      return jtGru;
  }
  public int getGrupo()
  {
    return fcc_numgru;
  }
  public int calculaGrupo() throws java.sql.SQLException
  {
    String s="SELECT MAX(fcc_numgru) as fcc_numgru from v_facaco WHERE emp_codi = "+emp_codi;
    dtStat.select(s);
    return dtStat.getInt("fcc_numgru",true);
  }
  public void removeAllDatos()
  {
    jtVto.removeAllDatos();
    jtGru.removeAllDatos();
  }
  public void setEnabled(boolean b)
  {
    if (jtVto!=null)
    {
      jtVto.setEnabled(b);
      if (! isEnable  && b)
        jtVto.requestFocusInicio();
    }

    if (jtGru!=null)
    {
      jtGru.setEnabled(b);
      if (! isEnable  && b)
        jtGru.requestFocusInicio();
    }
    isEnable=b;
  }
  public boolean isEnabled()
  {
    if (jtVto!=null)
      return jtVto.isEnabled();
    return false;
  }

  public boolean llenaGrid(DatosTabla dt, char lbvOrige, int empCodi,
                           int ejeNume,  int numFra) throws  Exception
  {
    String s = "select * from librovto WHERE lbv_orige = '" + lbvOrige + "'" +
        " and emp_codi = " + empCodi +
        " and eje_nume = " + ejeNume +
        " and lbv_numfra = " + numFra;
    boolean enab = jtVto.isEnabled();
    verPagoVto=false;
    totPagado=false;
    jtVto.setEnabled(false);
    jtVto.removeAllDatos();
    pPagos.removeAllDatos();
    if (!dt.select(s))
    {
      jtVto.setEnabled(enab);
      return false;
    }
    totPagado=true;
    do
    {
      Vector v=new Vector();
      v.addElement(dt.getString("lbv_nume"));
      v.addElement(dt.getFecha("lbv_fecvto","dd-MM-yyyy"));
      v.addElement(dt.getString("lbv_impvto"));
      v.addElement(dt.getString("lbv_imppag"));
      v.addElement(dt.getString("lbv_pagado"));
      if (! dt.getString("lbv_pagado").equals("S"))
        totPagado=false;
      v.addElement(dt.getString("lbv_comen"));
      jtVto.addLinea(v);
    }  while (dt.next());
    jtVto.requestFocusInicio();
    verPagoVto=true;
    actDatPagos();
    jtVto.setEnabled(enab);
    return true;
  }

  public boolean isTotalPagado()
  {
    return totPagado;
  }
  public boolean hasPagos(DatosTabla dt,  char lbvOrige, int empCodi,
                       int ejeNume, int numFra) throws Exception
  {
    String s = "SELECT sum(lbv_imppag) as lbv_imppag FROM librovto " +
        " where lbv_orige = '" + lbvOrige + "'" +
        " and emp_codi = " + empCodi +
        " and eje_nume = " + ejeNume +
        " and lbv_numfra = " + numFra;
    dt.select(s);
    if (dt.getDouble("lbv_imppag",true) != 0)
      return true; // Hay pagos Realizados sobre esta fra.
    return false;
  }

  public void delDatos(DatosTabla dt, Statement st, char lbvOrige, int empCodi,
                          int ejeNume, int numFra) throws Exception
  {
    String s = "DELETE from librovto " +
        " where lbv_orige = '" + lbvOrige + "'" +
        " and emp_codi = " + empCodi +
        " and eje_nume = " + ejeNume +
        " and lbv_numfra = " + numFra;
    st.executeUpdate(s);
    delGrupo(st,lbvOrige,empCodi,ejeNume,numFra);
    return;
  }
  /**
   * Actualiza los datos de Vtos. y del grupo de fras.
   * @param dt DatosTabla
   * @param st Statement
   * @param lbvOrige char
   * @param empCodi int
   * @param ejeNume int
   * @param numFra int
   * @param fecfra Date
   * @param prvCodi int
   * @param prvNomb String
   * @throws Exception
   */
  public void actDatos(DatosTabla dt,Statement st, char lbvOrige, int empCodi,
                          int ejeNume,  int numFra,java.util.Date fecfra,int prvCodi,String prvNomb) throws  Exception
 {
   jtVto.procesaAllFoco();
   int nCol=jtVto.getRowCount();
   int n=0;

   // Primero borro del Libro Vto. Las lineas que ya no existen
   String condWhere="lbv_orige = '" + lbvOrige + "'" +
      " and emp_codi = " + empCodi +
      " and eje_nume = " + ejeNume +
      " and lbv_numfra = " + numFra;

   String s = "select * from librovto where "+condWhere;
   String s1="DELETE from librovto where "+condWhere+
      " and lbv_nume = ";

  if (dt.select(s,true))
  {
    do
    {
      for (n=0;n<nCol;n++)
      {
        if (jtVto.getValorInt(n, 0) == dt.getInt("lbv_nume"))
          break;
      }
      if (n==nCol)
        st.executeUpdate(s1+dt.getInt("lbv_nume")); // Borro Linea
    } while (dt.next());
  }

  s1=  "SELECT MAX(lbv_nume) as lbv_nume from librovto where "+condWhere;
  dt.select(s1);
  int maxNum=dt.getInt("lbv_nume",true)+1;
  dt.addNew("librovto");
  for (n=0;n<nCol;n++)
  {
    if (jtVto.getValorDec(n,2)==0)
      continue;
    if (jtVto.getValorDec(n,0)==0)
    {
      dt.addNew();
      dt.setDato("lbv_orige",""+lbvOrige);
      dt.setDato("lbv_copvtr",prvCodi); // Proveedor o Transportista
      dt.setDato("lbv_nombre",prvNomb); // Nombre Proveedor o Transportista
      dt.setDato("emp_codi",empCodi);
      dt.setDato("eje_nume",ejeNume);
      dt.setDato("lbv_numfra",numFra);
      dt.setDato("lbv_fecfra", new java.sql.Date(fecfra.getTime()));
      dt.setDato("lbv_nume",maxNum++);
      dt.setDato("lbv_imppag",0);
      dt.setDato("lbv_pagado","N");
    }
    else
    { // Estoy editando la linea.
      if (! dt.select("select * from librovto where "+condWhere+
              " and lbv_nume = "+jtVto.getValorInt(n,0),true))
      {
          throw new SQLException("Vencimiento: "+jtVto.getValorInt(n,0)+" con condiciones: "+condWhere+
                  " No encontrado");
      }
      dt.edit();
    }
    dt.setDato("lbv_fecvto",jtVto.getValString(n,1),"dd-MM-yyyy");
    dt.setDato("lbv_impvto",jtVto.getValorDec(n,2));
    dt.setDato("lbv_comen",jtVto.getValString(n,5)); // Comentario
    dt.update(st);
  }
  jtGru.procesaAllFoco();
  actAcumFra(); // Actualizo Acumulado
  delGrupo(st,lbvOrige, empCodi, ejeNume, numFra);

  nCol=jtGru.getRowCount();

  for (n=0;n<nCol;n++)
  {
    if (jtGru.getValorInt(n, 0) > 0 && jtGru.getValorInt(n, 1) > 0)
    {
      dt.addNew("grufaco");
      dt.setDato("gfc_orige",""+lbvOrige);
      dt.setDato("emp_codi1", empCodi);
      dt.setDato("eje_nume1", ejeNume);
      dt.setDato("fcc_nume1", numFra);
      dt.setDato("emp_codi2", empCodi);
      dt.setDato("eje_nume2", jtGru.getValorInt(n, 0));
      dt.setDato("fcc_nume2", jtGru.getValorInt(n, 1));
      dt.update(st);
    }
  }
 }

  private void delGrupo(Statement st,char lbvOrige, int empCodi, int ejeNume, int numFra) throws
      SQLException
  {
    String s="DELETE FROM grufaco WHERE eje_nume1 = "+ejeNume+
        " and emp_codi1 = "+empCodi+
        " and fcc_nume1 = "+numFra+
        " and gfc_orige = '"+lbvOrige+"'";
    st.executeUpdate(s);
    s="DELETE FROM grufaco WHERE eje_nume2 = "+ejeNume+
        " and emp_codi2 = "+empCodi+
        " and fcc_nume2 = "+numFra+
        " and gfc_orige = '"+lbvOrige+"'";
    st.executeUpdate(s);
  }
 public int getRowCount()
 {
   return jtVto.getRowCount();
 }

 public int getLinActivas()
 {
   int nCol = jtVto.getRowCount();
   int n = 0;
   int nAct = 0;
   for (n = 0; n < nCol; n++)
   {
     if (jtVto.getValorDec(n, 2) == 0)
       continue;
     nAct++;
   }
   return nAct;
 }

 public double getImpVtos()
 {
   int nCol = jtVto.getRowCount();
   int n = 0;
   double impVtos = 0;
   for (n = 0; n < nCol; n++)
     impVtos += jtVto.getValorDec(n, 2);
   return Formatear.Redondea(impVtos,2);
 }

 public void requestFocusInicio()
 {
   lbv_fecvtoE.resetTexto();
   jtVto.requestFocusInicio();
 }
 public void requestFocusVto()
 {
   jtVto.requestFocus();
 }
 public void requestFocusGru()
 {
   jtGru.requestFocus();
 }
 public int cambiaLineaVto()
 {
   if (lbv_impvtoE.getValorDec()==0)
     return -1;
   if (lbv_fecvtoE.isNull() || lbv_fecvtoE.getError())
   {
     msgError="Fecha de Vto. No es valida";
     statusBar.setTextErr(msgError);
     return 1;
   }
   return -1;
 }

 public String getMsgError()
 {
   return msgError;
 }

 public boolean ponValoresDef(double impFra,int fpaCodi,String fvcFecfra) throws Exception
 {
   if (fpaCodi==0 || impFra==0 )
     return false;
   // Si tiene algun vto. pagado NO actualizamos el GRID
   for (int n=0;n<jtVto.getRowCount();n++)
     if (jtVto.getValBoolean(n,4))
       return false;
   if (fpaCodi!=fpa_codi)
   {
     String s="SELECT * FROM v_forpago WHERE fpa_codi = "+fpaCodi;
     if (! dtStat.selectInto(s,lkFpa))
       throw new java.sql.SQLException("Forma de PAGO: "+fpaCodi+" NO encontrada");
     fpa_codi = fpaCodi;
   }
   int nVtos = clFactCob.calDiasVto(lkFpa.getDatoInt("fpa_dia1"),
                                    lkFpa.getDatoInt("fpa_dia2"), lkFpa.getDatoInt("fpa_dia3"),
                                    fvcFecfra);
   double impVto=Formatear.Redondea(impFra/nVtos,2);
   boolean grVtoEnab=jtVto.isEnabled();
   jtVto.setEnabled(false);
   int n;
   double impLinea;
//   jtVto.removeAllDatos();
    for (n=0;n<nVtos;n++)
    {

      impLinea=n==nVtos-1?impFra:impVto;
      Vector v=new Vector();
      if (n>=jtVto.getRowCount() || jtVto.isVacio())
      {
        v.add("0");
        v.add(clFactCob.diasVto[n]);
        v.add("" + impLinea);
        v.add("0");
        v.add("N");
        v.add("");
        jtVto.addLinea(v);
      }
      else
      {
        jtVto.setValor(clFactCob.diasVto[n], n, 1);
        jtVto.setValor("" + impLinea, n, 2);
      }
      impFra -= impVto;
    }
    jtVto.ponValores(jtVto.getSelectedRow());
    for (;n<jtVto.getRowCount();n++)
      jtVto.setValor("0",n,2);
    jtVto.setEnabled(grVtoEnab);
    jtVto.requestFocusInicio();
    return true;
 }
 public static String  getSqlBuscaGrupoFra(int eje_nume,int emp_codi,int fcc_nume,String gfc_orige) throws SQLException
 {
   return "SELECT eje_nume2 as eje_nume,fcc_nume2 as fcc_nume " +
       " FROM grufaco as g " +
       "  WHERE eje_nume1 = " + eje_nume +
       " and emp_codi1 = " + emp_codi +
       " and fcc_nume1 = " + fcc_nume +
       " and gfc_orige = '" + gfc_orige + "'" +
       " UNION  ALL " +
       " SELECT eje_nume1  as eje_nume,fcc_nume1 as fcc_nume " +
       "  FROM grufaco as g " +
       " WHERE eje_nume2 = " + eje_nume +
       " and emp_codi2 = " + emp_codi +
       " and fcc_nume2 = " + fcc_nume +
       " and gfc_orige = '" + gfc_orige + "'" +
       " ORDER BY 1,2";
 }
 /**
  *
  * Muestra los datos del grupo de factura actual
  * @throws SQLException
  * @return int
  */
 public int verGrupoFra() throws SQLException
 {
   String s ="SELECT eje_nume2 as eje_nume,fcc_nume2 as fcc_nume, "+
        (gfc_orige.equals("C")?
        " fcc_fecfra,fcc_sumtot,fcc_conpag,fcc_coment "+
        " FROM grufaco as g left join v_facaco as f on "+
        " g.emp_codi2=  f.emp_codi "+
        " and g.eje_nume2 = f.eje_nume "+
        " and g.fcc_nume2 = f.fcc_nume ":
        " frt_fecha as fcc_fecfra,frt_imptot as fcc_sumtot,'S' AS fcc_conpag,'' as fcc_coment "+
        "  FROM grufaco as g left join fratraca as f on "+
        " g.emp_codi2=  f.emp_codi "+
        " and g.eje_nume2 = f.eje_nume "+
        " and g.fcc_nume2 = f.frt_nume ")+
        "  WHERE eje_nume1 = " + eje_nume +
        " and emp_codi1 = " + emp_codi +
        " and fcc_nume1 = " + fcc_nume +
        " and gfc_orige = '"+gfc_orige+"'"+
        " UNION  ALL " +
        " SELECT eje_nume1  as eje_nume,fcc_nume1 as fcc_nume,"+
        (gfc_orige.equals("C")?
        " fcc_fecfra,fcc_sumtot,fcc_conpag,fcc_coment "+
        "  FROM grufaco as g left join v_facaco as f on "+
        " g.emp_codi1=  f.emp_codi "+
        " and g.eje_nume1 = f.eje_nume "+
        " and g.fcc_nume1 = f.fcc_nume ":
        " frt_fecha as fcc_fecfra,frt_imptot as fcc_sumtot,'S' AS fcc_conpag,'' as fcc_coment "+
        "  FROM grufaco as g left join fratraca as f on "+
        " g.emp_codi1=  f.emp_codi "+
        " and g.eje_nume1 = f.eje_nume "+
        " and g.fcc_nume1 = f.frt_nume ")+
        " WHERE eje_nume2 = " + eje_nume +
        " and emp_codi2 = " + emp_codi +
        " and fcc_nume2 = " + fcc_nume +
        " and gfc_orige = '"+gfc_orige+"'"+
        " ORDER BY 1,2";

   jtGru.removeAllDatos();
//   System.out.println(s);
   if (! dtStat.select(s))
     return 0;
   do
   {
     Vector v=new Vector();
     v.add(dtStat.getString("eje_nume"));
     v.add(dtStat.getString("fcc_nume"));
     v.add(dtStat.getFecha("fcc_fecfra","dd-MM-yyyy"));
     v.add(dtStat.getString("fcc_sumtot"));
     v.add(new Boolean(dtStat.getString("fcc_conpag").equals("S")));
     v.add(dtStat.getString("fcc_coment"));
     jtGru.addLinea(v);
   } while (dtStat.next());
   return jtGru.getRowCount();
 }

 void actAcumFra()
 {
   int nRow=jtGru.getRowCount();
   int nFac=0;
   double impFac=0;

   for (int n=0;n<nRow;n++)
   {
     if (jtGru.getValorInt(n, 0) > 0 && jtGru.getValorInt(n, 1) > 0)
     {
       nFac++;
       impFac+=jtGru.getValorDec(n,3);
     }
   }
   numFrasE.setValorInt(nFac);
   impFrasE.setValorDec(impFac);
 }
 /**
  * Comprueba que todas las facturas del grupo son del proveedor mandado
  * @param prvCodi int Proveedor sobre el que compbrobar
  * @throws SQLException error en DB
  * @return boolean TRUE si son todos iguales
  */
 public boolean checkPrv(int prvCodi) throws SQLException
 {
   int nRow = jtGru.getRowCount();
//   int nFac = 0;
//   double impFac = 0;

   for (int n = 0; n < nRow; n++)
   {
     if (jtGru.getValorInt(n, 0) > 0 && jtGru.getValorInt(n, 1) > 0)
     {
       if (buscaDatFra(emp_codi, jtGru.getValorInt(n,0), jtGru.getValorInt(n, 1),
                       prvCodi,dtStat)!=null)
         return false;
     }
   }
   return true;
 }
}
