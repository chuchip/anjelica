package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.camposdb.PaiPanel;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;     
import gnu.chu.utilidades.*;
import gnu.chu.winayu.ayuMat;
import gnu.chu.winayu.ayuSde;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;     
import java.util.Hashtable;
import javax.swing.event.*;

/**
 *
 * <p>Título: MantAlbComCarne</p>
 * <p>Descripción: Mantenimiento Albaranes de Compra</p>
 * Parametros: modPrecio Indica si se puede modificar los precios del albaran.
 *  admin: Modo Aministrador.
 *  AlbSinPed true/False Indica si se pueden cargar albaranes sin un pedido de compras
 * <p>Created on 03-abr-2009, 18:14:38</p>
 *  <p>Copyright: Copyright (c) 2005-2016
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
 * @author Chuchi P
 * @version 2.0
 */
public class MantAlbComCarne extends MantAlbCom  
{
   String ultMat=null,ultSalDes,ultNac,ultCeb,ultSacr,ultFecCad,ultFecSac,ultFecPro;
   CLinkBox mat_codiE,sde_codiE;
   
   PaiPanel acp_painacE;
   PaiPanel acp_engpaiE;
   PaiPanel acp_paisacE;
   
  
   CTextField acp_feccadE,acp_fecsacE,acp_fecproE;
   /**
    * Codigo matadero 4
    */   
   final int JTD_MATCODI=4;
   /**
    * Sala Despiece 5
    */
   final int JTD_SDECODI=5;
   /**
    * Pais Nacimiento - 6
    */
   final int JTD_PAINAC=6; 
   final int JTD_PNACNO=7; 
   /**
    * Pais Engorde 8
    */
   final int JTD_ENGPAI=8;
   final int JTD_PENGNO=9;
   /**
    * Pais Sacrificio 10
    */
   final int JTD_PAISAC=10;
   final int JTD_PSACNO=11;
   /**
    * Fecha Caducicad 12
    */
   final int JTD_FECCAD=12;
   /**
    * Fecha Sacrificio 13
    */
   final int JTD_FECSAC=13;
   /**
    * Fecha Prodcuccion 14
    */
   final int JTD_FECPRO=14;
   
   
   public MantAlbComCarne(EntornoUsuario eu, Principal p) {
        super(eu, p);
   }

   public MantAlbComCarne(EntornoUsuario eu, Principal p, Hashtable ht) {
        super(eu, p, ht); 
   }

   public MantAlbComCarne(menu p, EntornoUsuario eu) {
        super(p, eu);
   }

   public MantAlbComCarne(menu p, EntornoUsuario eu, Hashtable ht) {
        super(p, eu, ht);
   }
     

   private void activarEventos0()
   {
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
   }
   @Override
  public void confGridDesglose() throws Exception
  {
      acp_painacE = new PaiPanel();
      acp_engpaiE = new PaiPanel();
      acp_paisacE = new PaiPanel();
      
      acp_painacE.iniciar(dtStat, this, vl, EU);
      acp_paisacE.iniciar(dtStat, this, vl, EU);
      acp_engpaiE.iniciar(dtStat, this, vl, EU);
    jtDes = new CGridEditable(17)
    {
        @Override
        public boolean deleteLinea(int row, int col)
        {
          if (! jtDes.isLineaEditable())
          {
             mensajeErr("Una linea cuyo individuo haya tenido movientos NO se puede borrar");
            return false;
          }
          if (jtDes.getValorInt(JTD_NUMIND)==0)
            return true;
          try
          {
            boolean res=borraInd(row,jtDes.getValorInt(JTD_NUMIND));
            ctUp.commit();
            return res;
          }
          catch (Exception k)
          {
            Error("Error al Borrar Linea", k);
          }
          return true;
        }

        @Override
        public void afterCambiaLinea()
        {
          afterCambiaLinDes();
        }

        @Override
        public boolean afterInsertaLinea(boolean insLinea)
        {
          int nRow;
          if (jtDes.getRowCount()==0 || jtDes.isVacio() )
            return true;
          nRow=jtDes.getSelectedRow();
          ponValDefDes(nRow,insLinea?nRow+1:nRow-1);
          return true;
        }
            @Override
        public int cambiaLinea(int row, int col)
        {
    //      jtDes.setValor(jtDes.actValGrid(row,col),row,col);
          return cambiaLinDes(row);
        }
    };
    
    mat_codiE=new CLinkBox();
    sde_codiE=new CLinkBox();
      
    acp_feccadE    = new CTextField(Types.DATE,"dd-MM-yyyy");
    acp_fecsacE    = new CTextField(Types.DATE,"dd-MM-yyyy");
    acp_fecproE    = new CTextField(Types.DATE,"dd-MM-yyyy");
    acp_canindE.setValorDec(1);
    

    activarEventos0();
    ArrayList v1 = new ArrayList();
    v1.add("N.Ind"); // 0 No. Individuo
    v1.add("Peso"); // 1
    v1.add("Lote/Clas."); //2
    v1.add("Crotal"); // 3
    v1.add("Matad"); // 4
    v1.add("S.Desp"); //5
    v1.add("P.N"); // 6
     v1.add("Nacido"); // 7
    v1.add("P.C"); // 8
    v1.add("Cebado"); // 9
    v1.add("P.S"); // 10 
    v1.add("Sacrif"); // 11
    v1.add("Fec.Cad"); // 12
    v1.add("Fec.Sac"); // 13
    v1.add("Fec.Pro"); // 14
    v1.add("N.Lin"); // 15
    v1.add("C.Ind."); // 16 Cantidad de Indiv.
    jtDes.setCabecera(v1);
    jtDes.setMaximumSize(new Dimension(743, 168));
    jtDes.setMinimumSize(new Dimension(743, 158));
    jtDes.setPreferredSize(new Dimension(743, 168));
    jtDes.setAnchoColumna(new int[]{50,70,70,120,150,150,30,130,30,130,30,130,80,80,80,40,40});
    jtDes.setAlinearColumna(new int[]{2,2,0,0,0,0,0,0,0,0,0,0,1,1,1,2,2});
    botonBascula = new BotonBascula(EU,this);
    ArrayList vc1=new ArrayList();
    acp_feccadE.setText("");

    mat_codiE.setAncTexto(40);
    sde_codiE.setAncTexto(40);
    mat_codiE.setCeroIsNull(true);
    sde_codiE.setCeroIsNull(true);
    sde_codiE.setFormato(Types.DECIMAL,"####9",5);
    mat_codiE.setFormato(true);
    mat_codiE.setFormato(Types.DECIMAL,"####9",5);

   
    
    acp_nucrotE.setToolTipText("Pulse F3 para generar Num. Crotal");
    acp_cantiE.setLeePesoBascula(botonBascula);
    acp_numlinE.setEnabled(false);
    acp_feccadE.setText("");
    acp_clasiE.setMayusc(true);
    vc1.add(acp_numindE); // 0
    vc1.add(acp_cantiE); // 1
    vc1.add(acp_clasiE); // 1
    vc1.add(acp_nucrotE); // 2
    vc1.add(mat_codiE); // 3 Matadero
    vc1.add(sde_codiE); // 4
    vc1.add(acp_painacE.getFieldPaiCodi()); // 5
    vc1.add(acp_painacE.getFieldPaiNomb()); // 5
    vc1.add(acp_engpaiE.getFieldPaiCodi()); // 6
    vc1.add(acp_engpaiE.getFieldPaiNomb()); // 6
    vc1.add(acp_paisacE.getFieldPaiCodi()); // 7
    vc1.add(acp_paisacE.getFieldPaiNomb()); // 7
    vc1.add(acp_feccadE); // 8
    vc1.add(acp_fecsacE); // 9
    vc1.add(acp_fecproE); // 10
    vc1.add(acp_numlinE); // 11
    vc1.add(acp_canindE); // 12
    jtDes.setAjusAncCol(false);
    jtDes.setColNueva(1);
    jtDes.setCampos(vc1);
    jtDes.setFormatoCampos();
//      jtDes.setFormatoColumna(1,"---9.99");
//    jtDes.setFormatoColumna(8,acp_feccadE.getFormato());
//    jtDes.setFormatoColumna(JTD_FECSAC,acp_fecsacE.getFormato());
//    jtDes.setFormatoColumna(JTD_FECPRO,acp_fecproE.getFormato());
  }
  
    /**
     * Invocado desde MantAlbCom, despues de cambiar linea desglose del grid
     * @param row
     * @return
     * @throws Exception
     */
    @Override
   public int cambiaLinDesg0(int row) throws Exception
   {
    
   
    if (mat_codiE.getError() && !mat_codiE.isNull())
     {
       mensajeErr("Introduzca un Codigo de Matadero  valido");
       return JTD_MATCODI;
     }
     if (sde_codiE.getError() && !sde_codiE.isNull())
     {
       mensajeErr("Introduzca un Codigo de SALA DE DESPIECE valido");
       return JTD_SDECODI;
     }
     if (!acp_painacE.controlar(false) && !acp_painacE.isNull())
     {
       mensajeErr("Introduzca un PAIS DE NACIMIENTO");
       return JTD_PAINAC;
     }
     if (!acp_engpaiE.controlar(false) && !acp_engpaiE.isNull())
     {
       mensajeErr("Introduzca un PAIS DE ENGORDE");
       return JTD_ENGPAI;
     }
     if (!acp_paisacE.controlar(false) && !acp_paisacE.isNull())
     {
       mensajeErr("Introduzca un PAIS DE SACRIFICIO");
       return JTD_PAISAC;
     }
      if (getLinGrDes().equals(lineaAnt))
        return -1;
     if (acp_feccadE.isNull() || acp_feccadE.getError())
     {
       acp_feccadE.setDate(Formatear.sumaDiasDate(acc_fecrecE.getDate(),pro_codiE.getDiasCad()) );
       mensajeErr("Introduzca Fecha de Caducidad");
       return JTD_FECCAD;
     }
     if (Formatear.comparaFechas(acp_feccadE.getDate(), acc_fecrecE.getDate())<=0)
         msgBox("ATENCION Fecha Caducidad deberia ser superior a la del Albarán");

     if (proOblfsa && ( acp_fecsacE.isNull() || acp_fecsacE.getError()))
     {
       mensajeErr("Es obligatorio introducir la Fecha Sacrificio  para este Producto");
       return JTD_FECSAC;
     }
     if (acp_fecproE.isNull() && !acp_fecsacE.isNull() && jtDes.getValorInt(row,JTD_NUMIND)==0)
     {
         jtDes.setValor(acp_fecsacE.getText(),row,JTD_FECPRO);
         acp_fecproE.setText(acp_fecsacE.getText());
     }
     if (acp_fecsacE.isNull() && acp_fecproE.isNull())
     {
         mensajeErr("Introducir fecha Produccion");
         return JTD_FECPRO;
     }
     if (proNumcro>0)
    {
        if (acp_nucrotE.isNull())
        {
            mensajeErr("Es obligatorio introducir codigo crotal para este producto");
            return JTD_NUMCRO;
        }
        /*
        * @todo revisar en caso de f6. Xq falla.
        */
        int numCrotal=getNumCrotal(acp_nucrotE.getText(),numIndAnt,jt.getValorInt(1));
        if (numCrotal>= proNumcro)
        {
            mensajeErr("No puede haber mas de "+proNumcro+"  numeros de crotal iguales");
            if (acp_nucrotE.isEditable())
                return JTD_NUMCRO;
        }
        if (mat_codiE.isNull())
        {
          mensajeErr("Codigo de Matadero Obligatorio para este producto");
          return JTD_MATCODI;
        }
        if (sde_codiE.isNull())
        {
            mensajeErr("SALA DE DESPIECE Obligatoria para este producto");
            return JTD_SDECODI;
        }
        if (acp_painacE.isNull())
        {
            mensajeErr("PAIS DE NACIMIENTO Obligatorio para este producto");
            return JTD_PAINAC;
        }
        if (acp_engpaiE.isNull())
        {
            mensajeErr("PAIS DE CEBADO Obligatorio para este producto");
            return JTD_ENGPAI;
        }
        if (acp_paisacE.isNull())
        {
            mensajeErr("PAIS DE SACRIFICIO Obligatorio para este producto");
            return JTD_PAISAC;
        }
    }
     if (nav.pulsado==navegador.ADDNEW)
      {
        if (Formatear.comparaFechas(acp_feccadE.getDate(), Formatear.getDateAct()) <
            0)
        {
          mensajeErr("Fecha de Caducidad NO puede ser inferior a la actual");
          return JTD_FECCAD;
        }
      }
      
     return -1;
  }
   
   @Override
  public String getLinGrDes()
  {
//    numIndAnt=jtDes.getValorInt(JTD_NUMIND);
//    System.out.println("Lin. Ant: "+numIndAnt);
    return acp_numindE.getValorInt()+""+acp_cantiE.getValorDec()+acp_clasiE.getText()+ acp_nucrotE.getText()+
        mat_codiE.getValorInt()+sde_codiE.getValorInt()+
        acp_painacE.getText()+acp_engpaiE.getText()+
        acp_paisacE.getText()+acp_feccadE.getText()+acp_fecsacE.getText()+
        acp_fecproE.getText()+
        acp_numlinE.getValorInt()+
        acp_canindE.getValorInt();
  }
   @Override
  public void guardaUltValoresDesg(){
        ultCeb=acp_engpaiE.getText();
        ultFecCad=acp_feccadE.getText();
        ultFecSac=acp_fecsacE.getText();
        ultFecPro=acp_fecproE.getText();
        ultMat=mat_codiE.getCellEditorValue().toString();
        ultNac=acp_painacE.getText();
        ultSacr=acp_paisacE.getText();
        ultSalDes=sde_codiE.getText();
  }
   @Override
  public  void guardaLinDes(int row,int nLiAlDe,int nLiAlb,int nInd) throws SQLException,NumberFormatException
  {

    try {
      if (opImpEti.isSelected())
        imprEtiq(jt.getValString(JT_PROCOD), row, nInd);
//    debug("guardaLinDes: row "+row+" nLiAlDe: "+nLiAlDe+" nInd: "+nInd);
       
      jtDes.setValor(""+nInd,row,DESNIND);
      guardaLinDes(nLiAlDe,nInd,jtDes.getValString(row,JTD_CLASI),
                 Formatear.cortar(jtDes.getValString(row,JTD_NUMCRO),30),
                 jtDes.getValString(row,JTD_PAINAC,true),
                 jtDes.getValDate(row,JTD_FECCAD),
                 jtDes.getValString(row,JTD_PAISAC,true),
                 jtDes.getValString(row,JTD_ENGPAI,true),
                 jtDes.getValDate(row,JTD_FECSAC),
                 jtDes.getValDate(row,JTD_FECPRO),
                 jt.getValorInt(JT_PROCOD), // codigo Prod. 
                 nLiAlb,
                 mat_codiE.getTextoInt(jtDes.getValString(row,JTD_MATCODI,true)),
                 sde_codiE.getTextoInt(jtDes.getValString(row,JTD_SDECODI,true)),
                 jtDes.getValorDec(row, JTD_CANTI),
                 jtDes.getValorInt(row, JTD_CANIND));

   
      jtDes.setValor(""+nLiAlDe,row,JTD_NUMLIN);

    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al Parsear fechas",k);
    }
  }

  void guardaLinDes(int acp_numlin,int acp_numind,String acp_clasi,String acp_nucrot,
                    String acp_painac,java.util.Date acp_feccad,String acp_paisac,
                    String acp_engpai,java.util.Date acp_fecsac,java.util.Date acp_fecpro,int pro_codi,
                    int acl_nulin,int mat_codi,int sde_codi,
                    double acp_canti, int acp_canind) throws SQLException
  {

    dtAdd.addNew("v_albcompar");
    dtAdd.setDato("acc_ano",acc_anoE.getValorInt());
    dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
    dtAdd.setDato("acc_serie",acc_serieE.getText());
    dtAdd.setDato("acc_nume",acc_numeE.getValorInt());
    dtAdd.setDato("acl_nulin", acl_nulin);// Numero Linea Albaran
    dtAdd.setDato("acp_numlin", acp_numlin);
    dtAdd.setDato("acp_claani", 0);
    dtAdd.setDato("acp_numind", acp_numind);
    dtAdd.setDato("pcc_nume", 0);
     dtAdd.setDato("acp_clasi",acp_clasi);
    dtAdd.setDato("acp_nucrot",acp_nucrot);
    dtAdd.setDato("acp_painac",acp_painac);
    dtAdd.setDato("acp_feccad",acp_feccad);
    dtAdd.setDato("acp_paisac", acp_paisac);
    dtAdd.setDato("acp_engpai",acp_engpai); // Pais Engorde
    dtAdd.setDato("acp_fecsac",acp_fecsac); // Fecha Sacrificio
    dtAdd.setDato("acp_fecpro",acp_fecpro); // Fecha Produccion
    dtAdd.setDato("pro_codi", pro_codi);
    dtAdd.setDato("mat_codi",mat_codi); // Matadero
    dtAdd.setDato("sde_codi",sde_codi); // Sala Despiece
    dtAdd.setDato("acp_canti", acp_canti);
    dtAdd.setDato("acp_canind", acp_canind);
    dtAdd.update(stUp);
  }
  public void cambioPrv(boolean forzarCambioPrv)
  {
    try {
      if (prv_codiE.isNull())
          return;
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
      if (acc_copvfaE.isNull() || forzarCambioPrv)
      {
        acc_copvfaE.setText(prv_codiE.getText());
        acc_copvfaE.controla(false);
      }
    } catch (Exception k)
    {
      Error("Error al buscar datos Mataderos de Proveedores",k);
    }
  }
  /**
   * Actualizo Datos de Desglose
   * @param nLinAlb int
   * @param row int Linea del Grid de Desgloses
   * @param nLinDes int Numero Linea de Desglose
   * @param nInd int Numero Individuo
   * @param nLiAlAnt int Numero Linea Anterior
   * @param nIndAnt Numero Individuo anterior
   * @throws SQLException
   * @throws ParseException
   */
   @Override
  public void actGridDes(int nLinAlb,int row,int nLinDes,int nInd,int nIndAnt, int nLiAlAnt) throws SQLException,java.text.ParseException
  {
    s = "SELECT * FROM v_albcompar "+
        " WHERE emp_codi = " +emp_codiE.getValorInt() +
        " AND acc_ano = " + acc_anoE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt()+
        " and acc_serie = '"+acc_serieE.getText()+"'"+
        " and acl_nulin = "+nLiAlAnt+
        " and acp_numlin = "+nLinDes;
    if (! dtCon1.select(s))
    { // No lo encuentro por Num. Linea. Pruebo por Num. Individuo
      if (! getIndAlbcompar(nLiAlAnt, nIndAnt,dtCon1))
      {
        aviso("pdalbaco: (NO encontrado Individuo en desglose de Alb. Compras)\n" +s);
        return;
      }
//        throw new SQLException("NO ENCONTRADO REGISTRO EN PARTIDAS DE COMPRAS\n"+s);
    }
    if (nIndAnt==nInd && acp_cantiE.getValorDec() == dtCon1.getDouble("acp_canti") &&
            acp_clasiE.getText().equals(dtCon1.getString("acp_clasi")) &&
            acp_nucrotE.getText().equals(dtCon1.getString("acp_nucrot")) &&
            mat_codiE.getValorInt() == dtCon1.getInt("mat_codi") &&
            sde_codiE.getValorInt() == dtCon1.getInt("sde_codi") &&
            acp_painacE.getText().equals(dtCon1.getString("acp_painac")) &&
            acp_engpaiE.getText().equals(dtCon1.getString("acp_engpai")) &&
            acp_paisacE.getText().equals(dtCon1.getString("acp_paisac")) &&
            acp_feccadE.getText().equals(dtCon1.getFecha("acp_feccad","dd-MM-yyyy")) &&
            acp_fecsacE.getText().equals(dtCon1.getFecha("acp_fecsac","dd-MM-yyyy")) &&
            acp_fecproE.getText().equals(dtCon1.getFecha("acp_fecpro","dd-MM-yyyy")) &&
            acp_canindE.getValorInt() == dtCon1.getInt("acp_canind"))
          return; // Son iguales

    nIndAnt=dtCon1.getInt("acp_numind");
    nLinDes=dtCon1.getInt("acp_numlin");
    //double canti=dtCon1.getDouble("acp_canti");
    // int canInd=dtCon1.getInt("acp_canind");
    int nIndiv;
//    int nLiAlDe=dtCon1.getInt("acl_nulin");
    if (nInd!=nIndAnt && ARG_ADMIN)
        nIndiv=nInd;
    else
      nIndiv=utildesp.getMaxNumInd(dtCon1,jt.getValorInt(JT_PROCOD),acc_anoE.getValorInt(),emp_codiE.getValorInt(),
                        acc_serieE.getText(),acc_numeE.getValorInt());

    s = "delete from v_albcompar WHERE acc_ano = " + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt()+
        " and acc_serie = '"+acc_serieE.getText()+"'"+
        " and acl_nulin = "+nLiAlAnt+
        " and acp_numlin = "+nLinDes;
    stUp.executeUpdate(s);
    nLinDes=getNumLinDes(nLinAlb);
    guardaLinDes(row,nLinDes, nLinAlb,nIndiv);
    ctUp.commit();
  }
   /**
   * Imprime etiqueta
   * @param nLin Numero de linea del grid de desglose
   * @param nInd Numero de Individuo.
   * @throws SQLException
   * @throws java.text.ParseException
   */
   @Override
  public void imprEtiq(String proCodi,int nLin,int nInd) throws SQLException,java.text.ParseException
  {
    if (jtDes.getValorDec(nLin,1)<=0 )
    {
      msgBox("No se pueden imprimir etiquetas sobre Cantidades negativos o a cero");
      return;
    }
    CodigoBarras codBarras = new CodigoBarras("C", 
        acc_anoE.getText() ,
        acc_serieE.getText() ,
        acc_numeE.getValorInt(),        
        Integer.parseInt(proCodi.trim()) ,
        nInd,
        jtDes.getValorDec(nLin,JTD_CANTI));
        
  

    String sacrificadoE,despiezadoE;
    String nombArt;
    
    nombArt=pro_codiE.getNombArt(proCodi,emp_codiE.getValorInt());
    int sdeCodi=0,matCodi=0;
    try {
      matCodi=Integer.parseInt(mat_codiE.getTexto(jtDes.getValString(JTD_MATCODI)));
    } catch (NumberFormatException k) {}

    s = "SELECT mat_nrgsa,pai_codi FROM v_matadero m WHERE m.mat_codi = " + matCodi;
    if (dtCon1.select(s))
    {
      sacrificadoE =  dtCon1.getString("mat_nrgsa");
      s = MantPaises.getNombrePais(dtCon1.getInt("pai_codi"), dtCon1);
      if (s!=null)
        sacrificadoE=s + "-" +sacrificadoE;
    }
    else
      sacrificadoE=matCodi+" NO ENCONTRADO";
    try {
      sdeCodi=Integer.parseInt(sde_codiE.getTexto(jtDes.getValString(nLin,JTD_SDECODI)));
    } catch (NumberFormatException k) {}

    s = "SELECT sde_nrgsa,pai_codi FROM v_saladesp m " +
        " WHERE m.sde_codi = " + sdeCodi;
    if (dtCon1.select(s))
    {
      despiezadoE = dtCon1.getString("sde_nrgsa");
      s = MantPaises.getNombrePais(dtCon1.getInt("pai_codi"), dtCon1);
      if (s!=null)
        despiezadoE = s + "-" + despiezadoE;
    }
    else
      despiezadoE = sdeCodi + " NO ENCONTRADO";

    if (etiq == null)
      etiq = new etiqueta(EU);

    etiq.iniciar(codBarras.getCodBarra(), codBarras.getLote(),
                 proCodi, nombArt,
                 mat_codiE.getTextoCombo(jtDes.getValString(nLin, JTD_PAINAC)),
                 mat_codiE.getTextoCombo(jtDes.getValString(nLin, JTD_ENGPAI)),
                 despiezadoE,
                 Formatear.cortar(jtDes.getValString(nLin, JTD_NUMCRO),30),
                 jtDes.getValorDec(nLin, JTD_CANTI),
                 getConservar(jt.getValorInt(JT_PROCOD)),
                 sacrificadoE,   acc_fecrecE.getDate() ,
                 jtDes.getValDate(nLin,JTD_FECPRO) ,
                 jtDes.getValDate(nLin,JTD_FECCAD) ,
                 jtDes.getValDate(nLin, JTD_FECSAC));
    etiq.setTipoEtiq(dtCon1,emp_codiE.getValorInt(),proCodeti);
   
  
    try
    {
      etiq.listar();
//        new ThreadPrint_alco(this);
    }
    catch (Throwable k)
    {
      Error("Error al Imprimir Etiquetas", k);
    }
    mensajeErr("Etiqueta ... Listada");  
  }
   @Override
  public void copiaJtValorAnt()
  {
    if (ultMat!=null)
    {
        jtDes.setValor(ultMat,  JTD_MATCODI);
        jtDes.setValor(ultSalDes, JTD_SDECODI);
        jtDes.setValor(ultNac, JTD_PAINAC);
        jtDes.setValor(ultCeb, JTD_ENGPAI);
        jtDes.setValor(ultSacr, JTD_PAISAC );
        jtDes.setValor(ultFecCad, JTD_FECCAD );
        jtDes.setValor(ultFecSac, JTD_FECSAC);
        jtDes.setValor(ultFecPro, JTD_FECPRO);
        jtDes.setValor("0",JTD_NUMLIN);
    }
  }
   @Override
  public void PADAddNew0()
  {
    ultMat=null;
  }
   void ej_consMat(ayuMat ayMat)
  {
    if (ayMat.consulta)
    {
      mat_codiE.setText(ayMat.mat_codiT);
      if (! mat_codiE.controla())
      {
          int res=mensajes.mensajeYesNo("Matadero "+ayMat.mat_nombT +
               " NO Habilitado para este Proveedor. Habilitarlo ?");
          if (res==mensajes.YES)
          {
              altaMat(mat_codiE.getValorInt());
              mat_codiE.addDatos(""+ayMat.mat_codiT,ayMat.mat_nombT);
              mat_codiE.setText(ayMat.mat_codiT);
          }
      }
    }
   
    ayMat.dispose();
    this.setEnabled(true);
      this.toFront();
      try
      {
        this.setSelected(true);
      }
      catch (Exception k)
      {}
      this.setFoco(null);
      mat_codiE.requestFocus();
  }
  void altaMat(int matCodi)
  {
    try {
        pdprove.insMatadero(prv_codiE.getValorInt(), matCodi, dtAdd);
        dtAdd.commit();
        msgBox("Matadero Insertado");
    } catch (SQLException k)
    {
        Error("Error al dar alta Matadero", k);
    }
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
      aySde = new ayuSde(EU, vl);
      aySde.addInternalFrameListener(new InternalFrameAdapter()
      {
                @Override
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consSde(aySde);
        }
      });

      vl.add(aySde);
      aySde.setLocation(25, 25);
      aySde.setVisible(true);
      this.setEnabled(false);
      this.setFoco(aySde);
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
      if (! sde_codiE.controla())
      {
          int res=mensajes.mensajeYesNo("Sala Despiece "+aySde.sde_nombT +
               " NO Habilitada para este Proveedor. Habilitarla ?");
          if (res==mensajes.YES)
          {
              altaSde(sde_codiE.getValorInt());
              sde_codiE.addDatos(""+aySde.sde_codiT,aySde.sde_nombT);
              sde_codiE.setText(aySde.sde_codiT);
          }
      }
    }

    aySde.dispose();
    this.setEnabled(true);
      this.toFront();
      try
      {
        this.setSelected(true);
      }
      catch (Exception k)
      {}
      this.setFoco(null);
      sde_codiE.requestFocus();
  }
  void altaSde(int sdeCodi)
  {
    try {
        pdprove.insSalaDesp(prv_codiE.getValorInt(), sdeCodi, dtAdd);
        dtAdd.commit();
        msgBox("Sala Despiece Insertada");
    } catch (SQLException k)
    {
        Error("Error al dar alta Sala de Despiece", k);
    }
  }
    
      /**
     * Consulta Mataderos
     * Llama a la Ayuda de Mataderos
     */
    public void consMatCodi() {
        final ayuMat ayMat;

        try {
            ayMat = new ayuMat(EU, vl);
            ayMat.addInternalFrameListener(new InternalFrameAdapter()
            {

                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    ej_consMat(ayMat);
                }
            });

            vl.add(ayMat);
            ayMat.setLocation(25, 25);
            ayMat.setVisible(true);
            this.setEnabled(false);
            this.setFoco(ayMat);
            ayMat.iniciarVentana();
        } catch (Exception j) {
            this.setEnabled(true);
        }
    }
   @Override
    public ArrayList getDatosDesgl() throws SQLException
    {
      ArrayList v=new ArrayList();
      v.add(dtCon1.getString("acp_numind"));
      v.add(dtCon1.getString("acp_canti"));
      v.add(dtCon1.getString("acp_clasi",true));
      v.add(dtCon1.getString("acp_nucrot"));
      s = "SELECT mat_nrgsa FROM v_matadero m "+
          " WHERE m.mat_codi = " + dtCon1.getInt("mat_codi");
      if (dtStat.select(s))
        v.add(dtCon1.getInt("mat_codi")+" - "+ dtStat.getString("mat_nrgsa"));
      else
        v.add(dtCon1.getInt("mat_codi")+" - Mat. "+dtCon1.getInt("mat_codi")+"  NO ENCONTRADO");

      s = "SELECT sde_nrgsa FROM v_saladesp m "+
          " WHERE m.sde_codi = " + dtCon1.getInt("sde_codi");
      if (dtStat.select(s))
        v.add(dtCon1.getInt("sde_codi")+" - "+ dtStat.getString("sde_nrgsa"));
      else
        v.add(dtCon1.getInt("sde_codi") + " - S.DESP. "+dtCon1.getInt("sde_codi") +
                     " NO ENCONTRADO");

      v.add(dtCon1.getString("acp_painac"));
      v.add(getPais(dtCon1.getString("acp_painac")));
      v.add(dtCon1.getString("acp_engpai"));
      v.add(getPais(dtCon1.getString("acp_engpai")));
      v.add(dtCon1.getString("acp_paisac"));
      v.add(getPais(dtCon1.getString("acp_paisac")));
      v.add(dtCon1.getFecha("acp_feccad","dd-MM-yyyy"));
      v.add(dtCon1.getFecha("acp_fecsac","dd-MM-yyyy"));
      v.add(dtCon1.getFecha("acp_fecpro","dd-MM-yyyy"));
      v.add(dtCon1.getString("acp_numlin"));
      v.add(dtCon1.getString("acp_canind"));
      return v;
     }
     public static String getNombreClase()
     {
        return  "gnu.chu.anjelica.compras.MantAlbComCarne";
     }
} 
