package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.camposdb.PaiPanel;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;     
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;     
import java.util.Hashtable;

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
 
   public static int MINLONCROTAL=10;
   private int CROTALAUTOMA=1; // Numero crotal Automatico
   String ultMat=null,ultSalDes,ultNac,ultCeb,ultSacr,ultFecCad,ultFecSac,ultFecPro;
   CTextField acp_matadE,acp_saldesE;
   
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
    acp_nucrotE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3 &&  acp_nucrotE.isEditable())     
        {
          genNumCrotal();
          jtDes.requestFocus(jtDes.getSelectedRow(),JTD_NUMCRO);
        }
      }
    });
//     acp_matadE.addKeyListener(new KeyAdapter()
//     {
//      @Override
//      public void keyPressed(KeyEvent e)
//      {
//        if (e.getKeyCode() == KeyEvent.VK_F3)
//          consMatCodi();
//      }
//     });
//     acp_saldesE.addKeyListener(new KeyAdapter()
//     {
//      @Override
//      public void keyPressed(KeyEvent e)
//      {
//        if (e.getKeyCode() == KeyEvent.VK_F3)
//          consSdeCodi();
//      }
//     });
   }
   @Override
  public void confGridDesglose() throws Exception
  {
      CROTALAUTOMA=EU.getValorParam("crotalAutoma", CROTALAUTOMA);
      MINLONCROTAL=EU.getValorParam("minLonCrotal", MINLONCROTAL);
      
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
             if (! ARG_GOD)
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
    
    acp_matadE=new CTextField(Types.CHAR,"X",15);
    acp_saldesE=new CTextField(Types.CHAR,"X",15);
    acp_matadE.setMayusc(true);
    acp_saldesE.setMayusc(true);
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

  
   
    
    acp_nucrotE.setToolTipText("Pulse F3 para generar Num. Crotal");
    acp_cantiE.setLeePesoBascula(botonBascula);
    acp_numlinE.setEnabled(false);
    acp_feccadE.setText("");
    acp_clasiE.setMayusc(true);
    vc1.add(acp_numindE); // 0
    vc1.add(acp_cantiE); // 1
    vc1.add(acp_clasiE); // 2
    vc1.add(acp_nucrotE); // 3
    vc1.add(acp_matadE); // 4 Matadero
    vc1.add(acp_saldesE); // 5
    vc1.add(acp_painacE.getFieldPaiCodi()); // 6
    vc1.add(acp_painacE.getFieldPaiNomb()); // 7
    vc1.add(acp_engpaiE.getFieldPaiCodi()); // 8
    vc1.add(acp_engpaiE.getFieldPaiNomb()); // 9
    vc1.add(acp_paisacE.getFieldPaiCodi()); // 10
    vc1.add(acp_paisacE.getFieldPaiNomb()); // 11
    vc1.add(acp_feccadE); // 12
    vc1.add(acp_fecsacE); // 13
    vc1.add(acp_fecproE); // 14
    vc1.add(acp_numlinE); // 15
    vc1.add(acp_canindE); // 16
    jtDes.setAjusAncCol(false);
    jtDes.setColNueva(1);
    jtDes.setCampos(vc1);
    jtDes.setFormatoCampos();
    pEtiPrv =new PEtiqProv(this);
    
    
//      jtDes.setFormatoColumna(1,"---9.99");
//    jtDes.setFormatoColumna(8,acp_feccadE.getFormato());
//    jtDes.setFormatoColumna(JTD_FECSAC,acp_fecsacE.getFormato());
//    jtDes.setFormatoColumna(JTD_FECPRO,acp_fecproE.getFormato());
    Tpanel1.addTab("Eti.Prv", pEtiPrv);
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
//      if (acp_matadE.isNull())
//     {
//       mensajeErr("Introduzca un Codigo de Matadero  valido");
//       return JTD_MATCODI;
//     }
//     if (acp_saldesE.isNull())
//     {
//       mensajeErr("Introduzca un Codigo de SALA DE DESPIECE valido");
//       return JTD_SDECODI;
//     }
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
       acp_feccadE.setDate(Formatear.sumaDiasDate(acc_fecrecE.getDate(),pro_codiE.getDiasCaducidad()) );
       mensajeErr("Introduzca Fecha de Caducidad");
       return JTD_FECCAD;
     }
     if (Formatear.comparaFechas(acp_feccadE.getDate(), acc_fecrecE.getDate())<=0)
     {
         msgBox("ATENCION Fecha Caducidad debe ser superior a la del Albarán");
         return JTD_FECCAD;
     }

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
     if (!acp_fecsacE.isNull())
     {
         if (Formatear.comparaFechas(acp_fecsacE.getDate(), acc_fecrecE.getDate())>= 0)
         {
            msgBox("ATENCION Fecha Sacrificio  debe ser inferior a la del Albarán");
            return JTD_FECSAC;
         }
     }
     if (!acp_fecproE.isNull())
     {
         if (Formatear.comparaFechas(acp_fecproE.getDate(), acc_fecrecE.getDate())>= 0)
         {
            msgBox("ATENCION Fecha Producción  debe ser inferior a la del Albarán");
            return JTD_FECPRO;
         }
         if (!acp_fecsacE.isNull()) 
         {
             if (Formatear.comparaFechas(acp_fecproE.getDate(), acp_fecsacE.getDate())<0) 
             {
                msgBox("ATENCION Fecha Producción  debe ser Superior o Igual  a la de Sacrificio");
                return JTD_FECPRO;
             }
         }
     }
     if (acp_fecsacE.isNull() && acp_fecproE.isNull())
     {
         mensajeErr("Introducir fecha Produccion");
         return JTD_FECPRO;
     }
     if (proNumcro>0)
     {        
           if (acp_nucrotE.isNull() || acp_nucrotE.getText().length() < MINLONCROTAL)
           {
               if (CROTALAUTOMA != 0)
               {
                   genNumCrotal();
                   jtDes.setValor(acp_nucrotE.getText(), JTD_NUMCRO);
               }
               if (acp_nucrotE.isNull() || acp_nucrotE.getText().length() < MINLONCROTAL)
               {
                   mensajeErr("Producto debe tener numero crotal. Longitud minima: " + MINLONCROTAL);
                   return JTD_NUMCRO;
               }
           }

           int numCrotal = getNumCrotal(acp_nucrotE.getText(), numIndAnt, jt.getValorInt(JT_PROCOD));
           if (numCrotal >= proNumcro)
           {
               if (CROTALAUTOMA != 0)
               {
                   genNumCrotal();
                   jtDes.setValor(acp_nucrotE.getText(), JTD_NUMCRO);
               } else
               {
                   mensajeErr("No puede haber mas de " + proNumcro + "  numeros de crotal iguales");
                   if (acp_nucrotE.isEditable())
                       return JTD_NUMCRO;
               }
           }
           if (acp_matadE.isNull())
           {
               mensajeErr("Codigo de Matadero Obligatorio para este producto");
               return JTD_MATCODI;
           }
           if (acp_saldesE.isNull())
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
       if (nav.pulsado == navegador.ADDNEW)
       {
           if (Formatear.comparaFechas(acp_feccadE.getDate(), Formatear.getDateAct()) < 0)
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
        acp_matadE.getText()+acp_saldesE.getText()+
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
        ultMat=acp_matadE.getText();
        ultNac=acp_painacE.getText();
        ultSacr=acp_paisacE.getText();
        ultSalDes=acp_saldesE.getText();
  }
  /**
   * 
   * @param row
   * @param nLiAlDe
   * @param nLiAlb
   * @param nInd
   * @throws SQLException
   * @throws NumberFormatException 
   */
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
                 jtDes.getValString(row,JTD_MATCODI,true),
                 jtDes.getValString(row,JTD_SDECODI,true),
                 jtDes.getValorDec(row, JTD_CANTI),
                 jtDes.getValorInt(row, JTD_CANIND));

   
      jtDes.setValor(""+nLiAlDe,row,JTD_NUMLIN);

    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al Parsear fechas",k);
    }
  }
/**
 * Guarda Linea de despiece en la base de datos.
 * @param acp_numlin
 * @param acp_numind
 * @param acp_clasi
 * @param acp_nucrot
 * @param acp_painac
 * @param acp_feccad
 * @param acp_paisac
 * @param acp_engpai
 * @param acp_fecsac
 * @param acp_fecpro
 * @param pro_codi
 * @param acl_nulin
 * @param acp_matad
 * @param acp_saldes
 * @param acp_canti
 * @param acp_canind
 * @throws SQLException 
 */
  void guardaLinDes(int acp_numlin,int acp_numind,String acp_clasi,String acp_nucrot,
                    String acp_painac,java.util.Date acp_feccad,String acp_paisac,
                    String acp_engpai,java.util.Date acp_fecsac,java.util.Date acp_fecpro,int pro_codi,
                    int acl_nulin,String acp_matad,String acp_saldes,
                    double acp_canti, int acp_canind) throws SQLException
  {
    if (acp_numind>0 && acp_canti<=0)
        throw new SQLException("Cantidad no puede ser 0, si tiene individuo");
    dtAdd.addNew("v_albcompar",false);
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
    dtAdd.setDato("acp_matad",acp_matad); // Matadero
    dtAdd.setDato("acp_saldes",acp_saldes); // Sala Despiece
    dtAdd.setDato("acp_canti", acp_canti);
    dtAdd.setDato("acp_canind", acp_canind);
    dtAdd.update(stUp);
    
  }
  
 
  void ponDatosTraza(ArrayList v)
  {       
          acp_clasiE.setText((String) v.get(0));
          acp_matadE.setText((String) v.get(1));
          acp_saldesE.setText((String) v.get(2));
          acp_painacE.setText((String) v.get(3));
          acp_engpaiE.setText((String) v.get(4));
          acp_paisacE.setText((String) v.get(5));
          acp_feccadE.setDate((java.util.Date) v.get(6));
          acp_fecsacE.setDate((java.util.Date) v.get(7));          
          jtDes.setValor( v.get(0),JTD_CLASI);
          jtDes.setValor( v.get(1),JTD_MATCODI);
          jtDes.setValor( v.get(2),JTD_SDECODI);
          jtDes.setValor( v.get(3),JTD_PAINAC);
          jtDes.setValor( v.get(4),JTD_ENGPAI);
          jtDes.setValor( v.get(5),JTD_PAISAC);
          jtDes.setValor(v.get(6),JTD_FECCAD);
          jtDes.setValor( v.get(7),JTD_FECPRO);
          Tpanel1.setSelectedIndex(0);          
          jtDes.requestFocusLater();
  }
  /**
   * Muestra en el grid de Etiqueta Proveedores los diferentes lotes
   */
   @Override
  public void verDiferentesLotes()
  {
      if (swInsLinEti)
          return;
    int nl=jtDes.getRowCount();
    pEtiPrv.getGridLotes().setEnabled(false);
    pEtiPrv.limpia();
    pEtiPrv.setProducto(pro_codiE.getValorInt());


    String lote,clasi;
    ArrayList<String> lotes=new ArrayList();
    ArrayList<String> clasif=new ArrayList();
    for (int n=0;n<nl;n++)
    {
        lote=jtDes.getValString(n,JTD_MATCODI)+jtDes.getValString(n,JTD_SDECODI)+
            jtDes.getValString(n,JTD_PAINAC)+jtDes.getValString(n,JTD_ENGPAI)+
            jtDes.getValString(n,JTD_PAISAC)+jtDes.getValString(n,JTD_FECCAD)+
            jtDes.getValString(n,JTD_FECPRO);
        if (lotes.indexOf(lote)>=0)
            continue;
        lotes.add(lote);
        ArrayList v=pEtiPrv.getGridLotes().getLineaDefecto();
        clasi=jtDes.getValString(n,JTD_CLASI);
        if (clasi.trim().equals(""))
            clasi="NL"+n;
        if (clasif.indexOf(clasi)>=0)
            clasi="NL"+n;
        clasif.add(clasi);
            
        v.set(pEtiPrv.JTLOTE_LOTE ,clasi);
        v.set(pEtiPrv.JTLOTE_MAT , jtDes.getValString(n,JTD_MATCODI));
        v.set(pEtiPrv.JTLOTE_SDE , jtDes.getValString(n,JTD_SDECODI));
        v.set(pEtiPrv.JTLOTE_PN , jtDes.getValString(n,JTD_PAINAC));
        v.set(pEtiPrv.JTLOTE_PC , jtDes.getValString(n,JTD_ENGPAI));
        v.set(pEtiPrv.JTLOTE_PS , jtDes.getValString(n,JTD_PAISAC));
        v.set(pEtiPrv.JTLOTE_FECCAD , jtDes.getValString(n,JTD_FECCAD));
        v.set(pEtiPrv.JTLOTE_FECPRO , jtDes.getValString(n,JTD_FECPRO));
        pEtiPrv.getGridLotes().addLinea(v);
    }
  }
  /**
   * Inserta lineas de Etiquetas Proveedor
   * @param al 
   */
  void insertaLineasEtiqueta(ArrayList<ArrayList> al)
  {      
      int nl=al.size();
      if (nl==0)
          return;
      jtDes.setEnabled(false);
      swInsLinEti=true;
     
      for (int n=0;n<nl;n++)
      {
          jtDes.setEnabled(false);
          ArrayList v=al.get(n);
          ArrayList vc=jtDes.getLineaDefecto();
          jtDes.addLinea(vc);
          jtDes.requestFocusFinal();
          jtDes.setEnabled(true);
          acp_cantiE.setValorDec((double) v.get(1));
          acp_clasiE.setText((String) v.get(0));
          acp_matadE.setText((String) v.get(2));
          acp_saldesE.setText((String) v.get(3));
          acp_painacE.setText((String) v.get(4));
          acp_engpaiE.setText((String) v.get(5));
          acp_paisacE.setText((String) v.get(6));
          acp_feccadE.setDate((java.util.Date) v.get(7));
          acp_fecsacE.setDate((java.util.Date) v.get(8));
          jtDes.setValor( v.get(1),JTD_CANTI);
          jtDes.setValor( v.get(0),JTD_CLASI);
          jtDes.setValor( v.get(2),JTD_MATCODI);
          jtDes.setValor( v.get(3),JTD_SDECODI);
          jtDes.setValor( v.get(4),JTD_PAINAC);
          jtDes.setValor( v.get(5),JTD_ENGPAI);
          jtDes.setValor( v.get(6),JTD_PAISAC);
          jtDes.setValor(v.get(7),JTD_FECCAD);
          jtDes.setValor( v.get(8),JTD_FECPRO);

          jtDes.mueveSigLinea();

      }
      Tpanel1.setSelectedIndex(0);
      jtDes.setEnabled(true);
      jtDes.requestFocusFinalLater();
      swInsLinEti=false;
  }
  /**
   * Actualizo Datos de Desglose
   * @param nLinAlb int
   * @param row int Linea del Grid de Desgloses
   * @param nLinDes int Numero Linea de Desglose
   * @param nInd int Numero Individuo
   * @param nLiAlAnt int Numero Linea Anterior
   * @param nIndAnt Numero Individuo anterior
     * @return true si 
   * @throws SQLException
   * @throws ParseException
   */
   @Override
  public boolean actGridDes(int nLinAlb,int row,int nLinDes,int nInd,int nIndAnt, int nLiAlAnt) throws SQLException,java.text.ParseException
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
        msgBox("NO encontrado Individuo en desglose de Alb. Compras)\n" +s);
        enviaMailError("NO encontrado Individuo en desglose de Alb. Compras)\n" +s);
        return false;
      }
//        throw new SQLException("NO ENCONTRADO REGISTRO EN PARTIDAS DE COMPRAS\n"+s);
    }
    if (nIndAnt==nInd && acp_cantiE.getValorDec() == dtCon1.getDouble("acp_canti") &&
            acp_clasiE.getText().equals(dtCon1.getString("acp_clasi")) &&
            acp_nucrotE.getText().equals(dtCon1.getString("acp_nucrot")) &&
            acp_matadE.getText().equals(dtCon1.getString("acp_matad")) &&
            acp_saldesE.getText().equals(dtCon1.getString("acp_saldes")) &&
            acp_painacE.getText().equals(dtCon1.getString("acp_painac")) &&
            acp_engpaiE.getText().equals(dtCon1.getString("acp_engpai")) &&
            acp_paisacE.getText().equals(dtCon1.getString("acp_paisac")) &&
            acp_feccadE.getText().equals(dtCon1.getFecha("acp_feccad","dd-MM-yyyy")) &&
            acp_fecsacE.getText().equals(dtCon1.getFecha("acp_fecsac","dd-MM-yyyy")) &&
            acp_fecproE.getText().equals(dtCon1.getFecha("acp_fecpro","dd-MM-yyyy")) &&
            acp_canindE.getValorInt() == dtCon1.getInt("acp_canind"))
          return true; // Son iguales

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
    return true;
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
        
  


    String nombArt;
    
    nombArt=pro_codiE.getNombArt(proCodi,emp_codiE.getValorInt());
    
    String  matCodi=jtDes.getValString(nLin, JTD_MATCODI);
   
//    sacrificadoE = pdmatadero.getRegistroSanitario(dtCon1, matCodi, false);
//    s = "SELECT mat_nrgsa,pai_codi FROM v_matadero m WHERE m.mat_codi = " + matCodi;
//    if (dtCon1.select(s))
//    {
//      sacrificadoE =  dtCon1.getString("mat_nrgsa");
//      s = MantPaises.getNombrePais(dtCon1.getInt("pai_codi"), dtCon1);
//      if (s!=null)
//        sacrificadoE=s + "-" +sacrificadoE;
//    }
//    else
//      sacrificadoE=matCodi+" NO ENCONTRADO";
    String sdeCodi=jtDes.getValString(nLin,JTD_SDECODI);    
//    despiezadoE=pdsaladesp.getRegistroSanitario(dtCon1, sdeCodi, false);
//    s = "SELECT sde_nrgsa,pai_codi FROM v_saladesp m " +
//        " WHERE m.sde_codi = " + sdeCodi;
//    if (dtCon1.select(s))
//    {
//      despiezadoE = dtCon1.getString("sde_nrgsa");
//      s = MantPaises.getNombrePais(dtCon1.getInt("pai_codi"), dtCon1);
//      if (s!=null)
//        despiezadoE = s + "-" + despiezadoE;
//    }
//    else
//      despiezadoE = sdeCodi + " NO ENCONTRADO";
    matCodi=utildesp.getRegistroSanitario(false, dtStat, matCodi, jtDes.getValString(nLin, JTD_PAISAC));
    if (etiq == null)
      etiq = new etiqueta(EU);
    String paisNacido=MantPaises.getNombrePais(jtDes.getValString(nLin, JTD_PAINAC), dtCon1);
    if (paisNacido==null)
        paisNacido="Pais "+jtDes.getValString(nLin, JTD_PAINAC)+" NO encontrado";
    String paisEngorde=MantPaises.getNombrePais(jtDes.getValString(nLin, JTD_ENGPAI), dtCon1);
    if (paisEngorde==null)
        paisEngorde="Pais "+jtDes.getValString(nLin, JTD_ENGPAI)+" NO encontrado";

    String paisSacrificio=MantPaises.getNombrePais(jtDes.getValString(nLin, JTD_PAISAC), dtCon1);
//    if (paisSacrificio==null)
//        paisSacrificio="Pais "+jtDes.getValString(nLin, JTD_PAISAC)+" NO encontrado";
    
    etiq.iniciar(codBarras.getCodBarra(), codBarras.getLote(),
                 proCodi, nombArt,
                 paisNacido,
                 paisEngorde,
                 sdeCodi,
                 Formatear.cortar(jtDes.getValString(nLin, JTD_NUMCRO),30),
                 jtDes.getValorDec(nLin, JTD_CANTI),
                 getConservar(jt.getValorInt(JT_PROCOD)),
                 matCodi,   acc_fecrecE.getDate() ,
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
    acp_painacE.resetCambio();
    acp_paisacE.resetCambio();
    acp_engpaiE.resetCambio();
    pEtiPrv.limpia();
    ultMat=null;
  }
  /**
   * Ejecuta consulta sobre mataderos
     * @throws java.sql.SQLException
   */
//    void ej_consMat() 
//    {
//        if (ayuMat.isAlta())
//        {
//            altaMat(ayuMat.getCodigoSelecion());
//            acp_matadE.addDatos("" + ayuMat.getCodigoSelecion(), ayuMat.getNombreSelecion());
//            acp_matadE.setValorInt(ayuMat.getCodigoSelecion());
//        }
//
//        if (ayuMat.isConsulta())
//        {
//            acp_matadE.setValorInt(ayuMat.getCodigoSelecion());
//            if (!acp_matadE.controla())
//            {
//                int res = mensajes.mensajeYesNo("Matadero " + ayuMat.getNombreSelecion()
//                    + " NO Habilitado para este Proveedor. Habilitarlo ?");
//                if (res == mensajes.YES)
//                {
//                    altaMat(acp_matadE.getValorInt());
//                    acp_matadE.addDatos("" + ayuMat.getCodigoSelecion(), ayuMat.getNombreSelecion());
//                    acp_matadE.setValorInt(ayuMat.getCodigoSelecion());
//                }
//            }
//        }
//
//        ayuMat.setVisible(false);
//        this.setEnabled(true);
//        this.toFront();
//        try
//        {
//            this.setSelected(true);
//        } catch (Exception k)
//        {
//        }
//        this.setFoco(null);
//        acp_matadE.requestFocus();
//    }
//  void altaMat(int matCodi)
//  {
//    try {
//        pdprove.insMatadero(prv_codiE.getValorInt(), matCodi, dtAdd);
//        dtAdd.commit();
//        msgBox("Matadero Insertado");
//    } catch (SQLException k)
//    {
//        Error("Error al dar alta Matadero", k);
//    }
//  }
//   /**
//   * Consulta Mataderos
//   * Llama a la Ayuda de Mataderos
//   */
//  public void consSdeCodi()
//  {
//    if (vl==null)
//        return;
//    try
//    {
//      if (ayuSde==null)
//      {
//        ayuSde = new AyuSdeMat(EU, vl,dtCon1)
//        {
//               @Override
//          public void matar()
//          {
//            ej_consSde();
//          }
//        };
//        ayuSde.setPermiteAlta(true);
//        vl.add(ayuSde);
//      }
//      ayuSde.setLocation(25, 25);
//      ayuSde.setVisible(true);
//      this.setEnabled(false);
//      this.setFoco(ayuSde);
//      ayuSde.iniciarVentana('S',prv_codiE.getValorInt());
//    }
//    catch (Exception j)
//    {
//        this.setEnabled(true);
//    }
//  }
//
//  void ej_consSde()
//  {
//    if (ayuSde.isAlta())
//    {
//         altaSde(ayuSde.getCodigoSelecion());
//         acp_saldesE.addDatos(""+ayuSde.getCodigoSelecion(),ayuSde.getNombreSelecion());
//         acp_saldesE.setValorInt(ayuSde.getCodigoSelecion());
//    }
//    if (ayuSde.isConsulta())
//    {
//      acp_saldesE.setValorInt(ayuSde.getCodigoSelecion());
//      if (! acp_saldesE.controla())
//      {
//          int res=mensajes.mensajeYesNo("Sala Despiece "+ayuSde.getNombreSelecion() +
//               " NO Habilitada para este Proveedor. Habilitarla ?");
//          if (res==mensajes.YES)
//          {
//              altaSde(acp_saldesE.getValorInt());
//              acp_saldesE.addDatos(""+ayuSde.getCodigoSelecion(),ayuSde.getNombreSelecion());
//              acp_saldesE.setValorInt(ayuSde.getCodigoSelecion());
//          }
//      }
//    }
//
//    ayuSde.setVisible(false);
//    this.setEnabled(true);
//      this.toFront();
//      try
//      {
//        this.setSelected(true);
//      }
//      catch (Exception k)
//      {}
//      this.setFoco(null);
//      acp_saldesE.requestFocus();
//  }
//  void altaSde(int sdeCodi)
//  {
//    try {
//        pdprove.insSalaDesp(prv_codiE.getValorInt(), sdeCodi, dtAdd);
//        dtAdd.commit();
//        msgBox("Sala Despiece Insertada");
//    } catch (SQLException k)
//    {
//        Error("Error al dar alta Sala de Despiece", k);
//    }
//  }
//    
//      /**
//     * Consulta Mataderos
//     * Llama a la Ayuda de Mataderos
//     */
//    public void consMatCodi() {
//    
//
//        try {
//            if (ayuMat==null)
//            {
//                ayuMat = new  AyuSdeMat(EU, vl,dtCon1)
//                {
//                    @Override
//                    public void matar  ()
//                    {
//                        ej_consMat();
//                    }
//                };
//                ayuMat.setPermiteAlta(true);
//                vl.add(ayuMat);
//            }
//            ayuMat.setLocation(25, 25);
//            ayuMat.setVisible(true);
//            this.setEnabled(false);
//            this.setFoco(ayuMat);
//            ayuMat.iniciarVentana('M',prv_codiE.getValorInt());
//        } catch (Exception j) {
//            this.setEnabled(true);
//        }
//    }
   @Override
    public ArrayList getDatosDesgl() throws SQLException
    {
      ArrayList v=new ArrayList();
      v.add(dtCon1.getString("acp_numind"));
      v.add(dtCon1.getString("acp_canti"));
      v.add(dtCon1.getString("acp_clasi",true));
      v.add(dtCon1.getString("acp_nucrot"));
      v.add(dtCon1.getString("acp_matad"));
//      s = "SELECT mat_nrgsa FROM v_matadero m "+
//          " WHERE m.mat_codi = " + dtCon1.getInt("mat_codi");
//      if (dtStat.select(s))
//        v.add(dtCon1.getInt("mat_codi")+" - "+ dtStat.getString("mat_nrgsa"));
//      else
//        v.add(dtCon1.getInt("mat_codi")+" - Mat. "+dtCon1.getInt("mat_codi")+"  NO ENCONTRADO");

//      s = "SELECT sde_nrgsa FROM v_saladesp m "+
//          " WHERE m.sde_codi = " + dtCon1.getInt("sde_codi");
//      if (dtStat.select(s))
//        v.add(dtCon1.getInt("sde_codi")+" - "+ dtStat.getString("sde_nrgsa"));
//      else
//        v.add(dtCon1.getInt("sde_codi") + " - S.DESP. "+dtCon1.getInt("sde_codi") +
//                     " NO ENCONTRADO");

      v.add(dtCon1.getString("acp_saldes"));
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
    /**
    * Genera un numero de crotal aleatorio sobre el ya introducido. Modifica los ultimos 6 digitos.
    */
    private void genNumCrotal() {
       
        String numCrot=null;
        int numIntentos=0;
        if (acp_nucrotE.isNull())
            acp_nucrotE.setText(acp_painacE.getText());
        for (numIntentos=0;numIntentos<10;numIntentos++)
        {            
            numCrot = getRandomCrotal( acp_nucrotE.getText(),MINLONCROTAL);
            try {
                if (proNumcro > 0) {
                    int numCrotal = getNumCrotal(numCrot, 0, jt.getValorInt(1));
                    if (numCrotal < proNumcro) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (SQLException k) {
                Error("Error al generar numero crotal automaticamente", k);
                return;
            }
        }
        if (numIntentos>=10)
        {
            msgBox("No ha sido posible generar un número crotal nuevo automaticamente. Aumente el numero de digitos del crotal");
            return;
        }
        acp_nucrotE.setText(numCrot);      
    }
    public static String getRandomCrotal(String crotalBase,EntornoUsuario eu)
    {
        int minLonCrotal=MINLONCROTAL;
        if (eu!=null)
          minLonCrotal=eu.getValorParam("minLonCrotal", MINLONCROTAL); 
        return getRandomCrotal(crotalBase,minLonCrotal);
    }
       /**
      * Genera un numero crotal aleatorio.
      * @param crotalBase
      * @param MINLONCROTAL Longitud Minima del Numero Crotal
      * @return 
      */
    public static String getRandomCrotal(String crotalBase,int MINLONCROTAL)
    {
        if (crotalBase==null)
            crotalBase="";
        int maxLonCrotal=MINLONCROTAL;
        int len = crotalBase.length();         
        String numCrot;
        if (len >= MINLONCROTAL) 
        {
             numCrot = crotalBase.substring(0, len - MINLONCROTAL/2);    
             maxLonCrotal=len;
             len = numCrot.length();
        }
        else            
             numCrot=crotalBase;           
        for (int n = len; n < maxLonCrotal; n++) 
            numCrot+=""+(int)( Math.random() * 10);             
        return numCrot;
    }
} 
