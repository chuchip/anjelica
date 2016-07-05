package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.menu;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;     
import gnu.chu.utilidades.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;     
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * <p>Título: MantAlbComPlanta</p>
 * <p>Descripción: Mantenimiento Albaranes de Compra para Plantas</p>
 * Parametros: modPrecio Indica si se puede modificar los precios del albaran.
 *  admin: Modo Aministrador.
 *  AlbSinPed true/False Indica si se pueden cargar albaranes sin un pedido de compras
 * 
 *  <p>Copyright: Copyright (c) 2005-2014
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
public class MantAlbComPlanta extends MantAlbCom  
{
   String ultFecCad,ultFecPro,ultIdDatos;
    
   CTextField acp_feccadE,acp_fecproE,dre_numeE;
   final int JTD_FECCAD=3;
   final int JTD_FECPRO=4;
   final int JTD_DRENUM=5;
   int JTD_NUMLIN=6;
   int JTD_CANIND=7;
   
   public MantAlbComPlanta(EntornoUsuario eu, Principal p) {
        super(eu, p);
   }

   public MantAlbComPlanta(EntornoUsuario eu, Principal p, Hashtable ht) {
        super(eu, p, ht);
   }

   public MantAlbComPlanta(menu p, EntornoUsuario eu) {
        super(p, eu);
   }

   public MantAlbComPlanta(menu p, EntornoUsuario eu, Hashtable ht) {
        super(p, eu, ht);
   }
  
   private void activarEventos0()
   {
     
   }
  public void confGridDesglose() throws Exception
  {
    jtDes = new CGridEditable(8)
    {
        @Override
        public boolean deleteLinea(int row, int col)
        {
          if (! jtDes.isLineaEditable())
          {
             mensajeErr("Una linea cuyo individuo haya tenido movientos NO se puede borrar");
            return false;
          }
          if (jtDes.getValorInt(0)==0)
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
    
    botonBascula = new BotonBascula(EU,this);
    acp_canindE.setValorDec(1);    
    acp_feccadE    = new CTextField(Types.DATE,"dd-MM-yyyy");    
    acp_fecproE    = new CTextField(Types.DATE,"dd-MM-yyyy");
    dre_numeE   = new CTextField(Types.DECIMAL,"####9");
    dre_numeE.setEnabled(false);
    activarEventos0();
    ArrayList v1 = new ArrayList();
    v1.add("N.Ind"); // 0 No. Individuo
    v1.add("Peso"); // 1
    v1.add("Crotal"); // 2
    v1.add("Fec.Cad"); // 3
    v1.add("Fec.Pro"); // 4
    v1.add("Id.Datos"); // 5
    v1.add("N.Lin"); // 6
    v1.add("C.Ind."); // 7 Cantidad de Indiv.
    jtDes.setCabecera(v1);
    jtDes.setMaximumSize(new Dimension(743, 168));
    jtDes.setMinimumSize(new Dimension(743, 158));
    jtDes.setPreferredSize(new Dimension(743, 168));
    jtDes.setAnchoColumna(new int[]{50,70,120,80,80,60,40,40});
    jtDes.setAlinearColumna(new int[]{2,2,0,1,1,2,2,2});

    //jtDes.setFormatoColumna(JTD_CANTI,"---9.99");
    ArrayList vc1=new ArrayList();
    acp_nucrotE.setToolTipText("Pulse F3 para generar Num. Crotal");
    acp_cantiE.setLeePesoBascula(botonBascula);
    acp_numlinE.setEnabled(false);
   

    vc1.add(acp_numindE); // 0
    vc1.add(acp_cantiE); // 1
    vc1.add(acp_nucrotE); // 2
    vc1.add(acp_feccadE); // 3
    vc1.add(acp_fecproE); // 4
    vc1.add(dre_numeE); // 5        
    vc1.add(acp_numlinE); // 6
    vc1.add(acp_canindE); // 7
    jtDes.setAjusAncCol(false);
    jtDes.setColNueva(1);
    jtDes.setCampos(vc1);
    jtDes.setFormatoCampos();
  }
  
   public int cambiaLinDesg0(int row) throws Exception
   {
   
     if (proNumcro>0)
    {
        if (acp_nucrotE.isNull())
        {
            mensajeErr("Es obligatorio introducir codigo crotal para este producto");
            return 2;
        }
        int numCrotal=getNumCrotal(acp_nucrotE.getText(),numIndAnt,jt.getValorInt(1));
        if (numCrotal>= proNumcro)
        {
            mensajeErr("No puede haber mas de "+proNumcro+"  numeros de crotal iguales");
            if (acp_nucrotE.isEditable())
                return JTD_NUMCRO;
        }
        
    }
    
     return -1;
  }
   
  public String getLinGrDes()
  {
    numIndAnt=jtDes.getValorInt(JTD_NUMIND);
//    System.out.println("Lin. Ant: "+numIndAnt);
    return acp_numindE.getValorInt()+""+acp_cantiE.getValorDec()+acp_nucrotE.getText()+
        acp_feccadE.getText()+acp_fecproE.getText()+
        acp_numlinE.getValorInt()+
        acp_canindE.getValorInt();
  }
  public void guardaUltValoresDesg(){
   ultFecCad=acp_feccadE.getText();        
   ultFecPro=acp_fecproE.getText();
  }
  public  void guardaLinDes(int row,int nLiAlDe,int nLiAlb,int nInd) throws SQLException,NumberFormatException
  {

    try {
      if (opImpEti.isSelected())
      imprEtiq(jt.getValString(JT_PROCOD), row, nInd);
//    debug("guardaLinDes: row "+row+" nLiAlDe: "+nLiAlDe+" nInd: "+nInd);
       
      jtDes.setValor(""+nInd,row,DESNIND);
      guardaLinDes(nLiAlDe,nInd,jtDes.getValString(row,JTD_NUMCRO),                
                 jt.getValorInt(JT_PROCOD), // codigo Prod. 
                 nLiAlb,              
                 jtDes.getValDate(row,JTD_FECCAD),
                 jtDes.getValDate(row,JTD_FECPRO),
                 jtDes.getValorInt(row,JTD_DRENUM),
                 jtDes.getValorDec(row, JTD_CANTI),
                 jtDes.getValorInt(row, JTD_CANIND));

   
      jtDes.setValor(""+nLiAlDe,row,JTD_NUMLIN);

    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al Parsear fechas",k);
    }
  }

void guardaLinDes(int acp_numlin,int acp_numind,String acp_nucrot,
                    int pro_codi,
                    int acl_nulin,
                    Date acp_feccad,Date acp_fecpro,int dre_nume,
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
    dtAdd.setDato("acp_nucrot",acp_nucrot);
//    dtAdd.setDato("acp_painac",acp_painac);
    dtAdd.setDato("acp_feccad",acp_feccad);
    dtAdd.setDato("dre_nume",dre_nume);
//    dtAdd.setDato("acp_paisac", acp_paisac);
//    dtAdd.setDato("acp_engpai",acp_engpai); // Pais Engorde
//    dtAdd.setDato("acp_fecsac",acp_fecsac); // Fecha Sacrificio
    dtAdd.setDato("acp_fecpro",acp_fecpro); // Fecha Produccion
    dtAdd.setDato("pro_codi", pro_codi);
//    dtAdd.setDato("mat_codi",mat_codi); // Matadero
//    dtAdd.setDato("sde_codi",sde_codi); // Sala Despiece
    dtAdd.setDato("acp_canti", acp_canti);
    dtAdd.setDato("acp_canind", acp_canind);
    dtAdd.update(stUp);
  }
  public void cambioPrv(boolean forzarCambioPrv)
  {      
    try {
      if (prv_codiE.isNull())
           return;
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
            acp_nucrotE.getText().equals(dtCon1.getString("acp_nucrot")) &&
            acp_feccadE.getText().equals(dtCon1.getFecha("acp_feccad","dd-MM-yyyy")) &&            
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
  public void imprEtiq(String proCodi, int nLin,int nInd) throws SQLException,java.text.ParseException
  {
    if (jtDes.getValorDec(nLin,1)<=0 )
    {
      msgBox("No se pueden imprimir etiquetas sobre Cantidades negativos o a cero");
      return;
    }
    CodigoBarras codBarras = new CodigoBarras("C", acc_anoE.getText() ,acc_serieE.getText() ,
        acc_numeE.getValorInt(), Integer.parseInt(proCodi.trim()), nInd,
        jtDes.getValorDec(JTD_CANTI));    
       

    
 
    String nombArt=pro_codiE.getNombArt(proCodi,emp_codiE.getValorInt());
    
    
    if (etiq == null)
      etiq = new etiqueta(EU);

    etiq.iniciar(codBarras.getCodBarra(), codBarras.getLote(),
                 proCodi, nombArt,                              
                 jtDes.getValString(nLin, JTD_NUMCRO),
                 jtDes.getValorDec(nLin, JTD_CANTI),
                 jtDes.getValorInt(JTD_CANIND),
                 acc_fecrecE.getDate()                
                 );
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

  }
   @Override
  public void PADAddNew0()
  {
   
  }
  
   @Override
  public ArrayList getDatosDesgl() throws SQLException
  {
      ArrayList v=new ArrayList();
      v.add(dtCon1.getString("acp_numind"));
      v.add(dtCon1.getString("acp_canti"));
      v.add(dtCon1.getString("acp_nucrot"));  
      v.add(dtCon1.getFecha("acp_feccad","dd-MM-yyyy"));
      v.add(dtCon1.getFecha("acp_fecpro","dd-MM-yyyy"));
      v.add(dtCon1.getInt("dre_nume",true));
      v.add(dtCon1.getString("acp_numlin"));
      v.add(dtCon1.getString("acp_canind"));
      return v;
     }   
     public static String getNombreClase()
     {
        return  "gnu.chu.anjelica.compras.MantAlbComPlanta";
     }
} 
