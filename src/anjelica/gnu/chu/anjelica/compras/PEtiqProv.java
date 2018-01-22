package gnu.chu.anjelica.compras;

import gnu.chu.camposdb.PaiPanel;
import gnu.chu.controles.CGridEditable;
import gnu.chu.controles.CTextField;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.vlike;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.winayu.AyuSdeMat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
/**
 *
 * <p>Título: PEtiqProv</p>
 * <p>Descripción: Panel para introducir etiquetas proveedores</p>
 *  <p>Copyright: Copyright (c) 2005-2018
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
 * @version 1.0
 */
public class PEtiqProv extends javax.swing.JPanel
{
    boolean swAutomatico=false;
    final int JTLOTE_LOTE=0;
    final int JTLOTE_MAT=1;
    final int JTLOTE_SDE=2;
    final int JTLOTE_PN=3;
    final int JTLOTE_PC=5;
    final int JTLOTE_PS=7;
    final int JTLOTE_FECCAD=9;
    final int JTLOTE_FECPRO=10;
    final int JTETI_ETIQ=0;
    final int JTETI_LOTE=1;
    final int JTETI_FECCAD=2;
    final int JTETI_KILOS=3;
    vlike lkEti=new vlike();
    MantAlbComCarne padre;
    DatosTabla dtAdd;
    DatosTabla dtCon1;
    EntornoUsuario EU;
    
    CTextField acp_matadE = new CTextField(Types.CHAR,"X",15);
    CTextField acp_saldesE = new CTextField(Types.CHAR,"X",15);
    PaiPanel acp_painacE=new PaiPanel();
    PaiPanel acp_engpaiE=new PaiPanel();
    PaiPanel acp_paisacE=new PaiPanel();
    CTextField acp_feccadE=new CTextField(Types.DATE,"dd-MM-yyyy");
    CTextField acp_fecproE=new CTextField(Types.DATE,"dd-MM-yyyy");
    AyuSdeMat ayuSde = null;
    AyuSdeMat ayuMat = null;
   
    public PEtiqProv(MantAlbComCarne papa)  throws SQLException
    {
        padre=papa;
        EU=papa.EU;
        dtAdd=padre.dtAdd;
        dtCon1=padre.dtCon1;
        
        initComponents();   
       
        pro_codiE.iniciar(dtAdd, padre, padre.vl, EU);
      
        acp_painacE.iniciar(padre.dtStat, padre, padre.vl, EU);
        acp_paisacE.iniciar(padre.dtStat, padre, padre.vl, EU);
        acp_engpaiE.iniciar(padre.dtStat, padre, padre.vl, EU);
        pro_codiE.setEnabled(false);
        jtLote.setButton(KeyEvent.VK_F5,Bcopia);
        activar(false);
        cambioPrv();
        activarEventos();       
    }
    
    void cambioPrv() throws SQLException
    {
        String s="select etp_codi,etp_nomb from etiqprov where prv_codi="+padre.prv_codiE.getValorInt()+
            " order by etp_codi";
        if (!dtCon1.select(s))
            return;
        int etpCodi=dtCon1.getInt("etp_codi");
        etp_codiE.setEnabled(false);
        etp_codiE.addItem(dtCon1);        
        etp_codiE.setValor(etpCodi);
        etp_codiE.setEnabled(true);
        leeEtiPrv(etpCodi);
        jtEti.setButton(KeyEvent.VK_F2, Bressub);
         
        
    }
    void leeEtiPrv(int etpCodi) throws SQLException
    {        
        String s="select * from etiqprov where etp_codi="+etpCodi;       
        dtCon1.selectInto(s,lkEti);        
    }
    void setProducto(int proCodi)
    {
        pro_codiE.setValorInt(proCodi);
    }
    void BTraspasa_actionPerformed()
    {
        if (jtLote.isVacio())
            return;
        int nRow=jtLote.getRowCount();
        jtEti.setEnabled(false);
        if (jtLote.isEnabled())
            jtLote.salirGrid();
        jtLote.setEnabled(false);
        try
        {
            for (int n=0;n<nRow;n++)
            {
           
                
                if (jtLote.getValString(n,JTLOTE_MAT).isEmpty())
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_MAT);
                    msgBox("Introduzca Matadero EN linea: "+n);
                    return;
                }
                acp_saldesE.setText(jtLote.getValString(n,JTLOTE_SDE)); // 2 Sala Desp
                if (jtLote.getValString(n,JTLOTE_SDE).isEmpty())
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_SDE);
                    msgBox("Introduzca  Sala Despiece EN linea: "+n);
                    return;
                }
                
                acp_painacE.setText(jtLote.getValString(n,JTLOTE_PN)); // 2 Sala Desp
                if (!acp_painacE.controlar(false))
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_PN);
                    msgBox("Pais Nacimiento NO VALIDO EN linea: "+n);
                    return;
                }
                 acp_paisacE.setText(jtLote.getValString(n,JTLOTE_PS)); // 2 Sala Desp
                if (!acp_paisacE.controlar(false))
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_PS);
                    msgBox("Pais Sacrificio NO VALIDO EN linea: "+n);
                    return;
                }
                acp_engpaiE.setText(jtLote.getValString(n,JTLOTE_PC)); // 2 Sala Desp
                if (!acp_engpaiE.controlar(false))
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_PC);
                    msgBox("Pais Criado NO VALIDO EN linea: "+n);
                    return;
                }
                if (jtLote.getValDate(n,JTLOTE_FECCAD)==null)
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_FECCAD);
                    msgBox("Fecha Caducidad NO VALIDA EN linea: "+n);
                    return; 
                }
                if (jtLote.getValDate(n,JTLOTE_FECPRO)==null)
                {
                    jtLote.setEnabled(true);
                    jtLote.requestFocusLater(n, JTLOTE_FECPRO);
                    msgBox("Fecha Produccion NO VALIDA EN linea: "+n);
                    return; 
                }                
            }
            nRow=jtEti.getRowCount();
            ArrayList<ArrayList> al=new ArrayList();
            for (int n=0;n<nRow;n++)
            {
                if (jtEti.getValString(n,JTETI_LOTE).trim().equals("") || jtEti.getValorDec(n,JTETI_KILOS)==0)
                    continue;
                ArrayList v=new ArrayList();
                v.add(jtEti.getValString(n,JTETI_LOTE)); // 0
                v.add(jtEti.getValorDec(n,JTETI_KILOS)); // 1
                int nl=getLineaLote(jtEti.getValString(n,JTETI_LOTE));
                if (nl==-1)
                {
                    jtEti.setEnabled(true);
                    jtEti.requestFocusLater(n, JTETI_ETIQ);
                    msgBox("Lote : "+jtEti.getValString(n,JTETI_LOTE)+" de linea: "+n+" No encontrado en desglose");
                    return;
                }
                v.add(jtLote.getValString(nl,JTLOTE_MAT)); // 2
                v.add(jtLote.getValString(nl,JTLOTE_SDE)); 
                v.add(jtLote.getValString(nl,JTLOTE_PN));
                v.add(jtLote.getValString(nl,JTLOTE_PC));
                v.add(jtLote.getValString(nl,JTLOTE_PS));
                v.add(jtLote.getValDate(nl,JTLOTE_FECCAD));
                v.add(jtLote.getValDate(nl,JTLOTE_FECPRO));
                al.add(v);
            }
            padre.insertaLineasEtiqueta(al);
            jtEti.removeAllDatos();
        } catch (SQLException | ParseException ex)
        {
            padre.Error("Error al Comprobar validez lotes", ex);
        }

    }
    void activarEventos()
    {
        Bcopia.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n=jtLote.getSelectedRow();
                if (jtLote.getSelectedRow()>0)
                {                           
                    jtLote.setEnabled(false);
                    jtLote.setValor(jtLote.getValString(n-1,JTLOTE_MAT ),n,JTLOTE_MAT);
                    jtLote.setValor(jtLote.getValString(n-1,JTLOTE_SDE ),n,JTLOTE_SDE);
                    jtLote.setValor(jtLote.getValString(n-1,JTLOTE_PN ),n,JTLOTE_PN);
                    jtLote.setValor(jtLote.getValString(n-1,JTLOTE_PC ),n,JTLOTE_PC);
                    jtLote.setValor(jtLote.getValString(n-1,JTLOTE_PS ),n,JTLOTE_PS);
                    jtLote.setEnabled(true);                    
                }
                jtLote.requestFocusLater();
            }
        });
        Blimpia.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpia();
            }
        });
        acp_loteE.addMouseListener(new MouseAdapter()
        {
              @Override
            public void mouseClicked(MouseEvent e) {
                if (!BTraspasa.isEnabled() || e.getClickCount()<2)
                    return;
                try 
                {
                    ArrayList v=new ArrayList();
                    v.add(jtLote.getValString(JTLOTE_LOTE)); // 2
                    v.add(jtLote.getValString(JTLOTE_MAT)); // 2
                    v.add(jtLote.getValString(JTLOTE_SDE)); 
                    v.add(jtLote.getValString(JTLOTE_PN));
                    v.add(jtLote.getValString(JTLOTE_PC));
                    v.add(jtLote.getValString(JTLOTE_PS));
                    v.add(jtLote.getValDate(JTLOTE_FECCAD));
                    v.add(jtLote.getValDate(JTLOTE_FECPRO));
                    padre.ponDatosTraza(v);
                 } catch ( ParseException ex)
            {
                padre.Error("Error al Comprobar validez lotes", ex);
            }   
            }
        });
        BTraspasa.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                BTraspasa_actionPerformed();
            }
        });
        
        Bressub.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bressub_actionPerformed();
            }
        });
        etp_codiE.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
              try{
                  if (etp_codiE.isEnabled())
                    leeEtiPrv(etp_codiE.getValorInt());
                } catch (SQLException k)
                {
                    padre.Error("Error al leer Parametros etiqueta Proveedor",k);
                }
            }
        });
        jtEti.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtEti.isEnabled() || !etp_codiE.isEnabled())
                    return;
                jtEti.setEnabled(true);
                jtEti.requestFocusLater();
                jtLote.setEnabled(false);
            }
        });
        jtLote.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtLote.isEnabled() || !etp_codiE.isEnabled() )
                    return;
                jtLote.setEnabled(true);
                jtLote.requestFocusLater();
                jtEti.setEnabled(false);
            }
        });
        jtLote.addGridListener(new GridAdapter()
        {
            @Override
            public boolean insertaLinea(GridEvent event) {
                return false;
            }
        });
        etp_codbarE.addFocusListener(new FocusAdapter()
        {
              @Override
              public void focusLost(FocusEvent e) {
                  if (swAutomatico)
                      return;
                  if (etp_codbarE.isNull(true) )
                      return;
                    boolean ret=procesaCodBarras(etp_codbarE.getText(),jtEti.getSelectedRow());
                    swAutomatico=true;
                    if (ret)
                    {
                        etp_codbarE.resetTexto();
                        jtEti.setValor("",jtEti.getSelectedRow(),JTETI_ETIQ);
                        jtEti.mueveSigLinea(0);
                    }
                    else
                    {
                        msgBox("Codigo BARRAS no Valido");
                    }
                    swAutomatico=false;
              }
        });
        jtEti.addGridListener(new GridAdapter()
        {         
       
            @Override
            public boolean afterInsertaLinea(GridEvent event){
                numCajasE.setValorInt(numCajasE.getValorInt()+1);
                numCajParcialE.setValorInt(numCajParcialE.getValorInt()+1);
                return true;
            }
               @Override
            public void afterDeleteLinea(GridEvent event){
                numCajasE.setValorInt(numCajasE.getValorInt()-1);
                numCajParcialE.setValorInt(numCajParcialE.getValorInt()-1);
            }
             @Override
            public boolean insertaLinea(GridEvent event) {
                return !jtEti.getValString(event.getLinea(),JTETI_LOTE).trim().equals("");
            }
            @Override
           public void cambiaLinea(GridEvent event){
                try
                {
                    actualizarLotes(jtEti.getValString(event.getLinea(),JTETI_LOTE),
                        jtEti.getValDate(event.getLinea(),JTETI_FECCAD));
                } catch (ParseException ex)
                {
                   return; 
                }   
                int ret=cambiaLineaEtiqueta(event.getLinea() );                
                event.setColError(ret);
            }
        });
//        acp_matadE.addKeyListener(new KeyAdapter()
//        {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_F3)
//                    consMatCodi();
//            }
//        });
//        acp_saldesE.addKeyListener(new KeyAdapter()
//        {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_F3)
//                    consSdeCodi();
//            }
//        });
    }
    int   cambiaLineaEtiqueta(int linea)
    {
        if (etp_loteE.getText().trim().equals(""))
            return -1;
        if (etp_kilosE.getValorDec()==0)
        {
            msgBox("Kilos no validos");
            return JTETI_KILOS;
        }
       
        return -1;
    }
    void msgBox(String msg)
    {        
        padre.msgBox(msg);
    }
    void Bressub_actionPerformed()
    {
      numCajParcialE.setText("0");
    }
   
    public void limpia()
    {
        jtEti.setEnabled(false);
        jtLote.setEnabled(false);
        jtEti.removeAllDatos();
        jtLote.removeAllDatos();
        jtLote.ponValores(0);
        numCajasE.setValorInt(0);
        numCajParcialE.setValorInt(0);
    }
    CGridEditable getGridLotes()
    {
        return jtLote;
    }
    /**
     * Devuelve la linea del Lote
     * @param lote
     * @return  numero linea. -1 Si no la encuentra.
     */
    int getLineaLote(String lote)
    {
       int nRow=jtLote.getRowCount();
       for (int n=0;n<nRow;n++)
       {
           if (jtLote.getValString(n,JTLOTE_LOTE).equals(lote)   )
               return n; // Ya existe linea
       }
       return -1;
    }
    /**
     * 
     * @param linea 
     */
    void actualizarLotes(String lote,java.util.Date fecCad) throws ParseException
    {
        if (lote == null || lote.trim().equals(""))
            return;
       jtLote.setEnabled(false);
       if (getLineaLote(lote)>=0)
           return;
       ArrayList v=jtLote.getLineaDefecto();
       v.set(JTLOTE_LOTE, lote);
       v.set(JTLOTE_FECCAD,fecCad);
       acp_feccadE.setDate(fecCad);
       java.util.Date fecprod=null;
       if (etp_feprdiE.getValorInt()>0 && fecCad!=null )
           fecprod=Formatear.sumaDiasDate(fecCad, etp_feprdiE.getValorInt()*-1);       
       v.set(JTLOTE_FECPRO,fecprod);
       acp_fecproE.setDate(fecprod);

       jtLote.addLinea(v);
    }
    
    boolean procesaCodBarras(String codBarras,int row)
    {
        try
        {
             if (swAutomatico)
                return true;
             if (codBarras.equals(""))
                return false;
             if (lkEti.getInt("etp_long")>0 && codBarras.length()!=lkEti.getInt("etp_long"))
                return false;
             String lote=getSubString(codBarras,lkEti.getInt("etp_lotini"),lkEti.getInt("etp_lotfin"));
             if (lote.equals(""))
                 return false;
             etp_loteE.setText(lote);
             jtEti.setValor(lote,row,JTETI_LOTE);
             String fecCad=getSubString(codBarras,lkEti.getInt("etp_fcdini"),lkEti.getInt("etp_fcdfin"));
             if (!fecCad.equals(""))
             {
                 etp_feccadE.setDate(Formatear.getDate(fecCad, lkEti.getString("etp_fecfor")));
                 jtEti.setValor(Formatear.getDate(fecCad, lkEti.getString("etp_fecfor")),row,JTETI_FECCAD);
                 
             }
             String kilos=getSubString(codBarras,lkEti.getInt("etp_kgenin"),lkEti.getInt("etp_kgenfi"));
             if (kilos.equals(""))
                 return false;
             kilos=kilos+"."+getSubString(codBarras,lkEti.getInt("etp_kgdein"),lkEti.getInt("etp_kgdefi"));
             etp_kilosE.setValorDec(Double.parseDouble(kilos.trim()));
             jtEti.setValor(Double.parseDouble(kilos.trim()),row,JTETI_KILOS);
             return true;
             
        } catch (SQLException | ParseException k)
        {
            padre.msgBox("Error al procesar codigo barras\n"+k.getMessage()  );            
        }
        return false;
    }
    
    String getSubString(String codBarras,int inicio,int fin)
    {
        if (fin==0)
            return "";  
        if (fin==-1)
            fin=codBarras.length();
        if (codBarras.length()<fin)
            return "";
        return codBarras.substring(inicio,fin);
    }
      /**
   * Ejecuta consulta sobre mataderos
   */
//    void ej_consMat() 
//    {
//        if (ayuMat.isAlta())
//        {
//            altaMat(ayuMat.getCodigoSelecion());
//            acp_matadE.addDatos("" + ayuMat.getCodigoSelecion(), ayuMat.getNombreSelecion());
//            acp_matadE.setValorInt(ayuMat.getCodigoSelecion());
//            padre.acp_matadE.addDatos("" + ayuMat.getCodigoSelecion(), ayuMat.getNombreSelecion());            
//
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
//                    padre.acp_matadE.addDatos("" + ayuMat.getCodigoSelecion(), ayuMat.getNombreSelecion());            
//                    acp_matadE.setValorInt(ayuMat.getCodigoSelecion());
//                }
//            }
//        }
//
//        ayuMat.setVisible(false);
//        this.setEnabled(true);
//        padre.toFront();
//        try
//        {
//            padre.setSelected(true);
//        } catch (Exception k)
//        {
//        }
//        padre.setFoco(null);
//        acp_matadE.requestFocus();
//    }
//  void altaMat(int matCodi)
//  {
//    try {
//        pdprove.insMatadero(padre.prv_codiE.getValorInt(), matCodi, dtAdd);
//        dtAdd.commit();
//        padre.msgBox("Matadero Insertado");
//    } catch (SQLException k)
//    {
//        padre.Error("Error al dar alta Matadero", k);
//    }
//  }
//  
//   /**
//   * Consulta Mataderos
//   * Llama a la Ayuda de Mataderos
//   */
//  public void consSdeCodi()
//  {
//    if (padre.vl==null)
//        return;
//    try
//    {
//      if (ayuSde==null)
//      {
//        ayuSde = new AyuSdeMat(padre.EU, padre.vl,dtCon1)
//        {
//               @Override
//          public void matar()
//          {
//            ej_consSde();
//          }
//        };
//        ayuSde.setPermiteAlta(true);
//        padre.vl.add(ayuSde);
//      }
//      ayuSde.setLocation(25, 25);
//      ayuSde.setVisible(true);
//      padre.setEnabled(false);
//      padre.setFoco(ayuSde);
//      ayuSde.iniciarVentana('S',padre.prv_codiE.getValorInt());
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
//         padre.acp_saldesE.addDatos(""+ayuSde.getCodigoSelecion(),ayuSde.getNombreSelecion());
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
//              padre.acp_saldesE.addDatos(""+ayuSde.getCodigoSelecion(),ayuSde.getNombreSelecion());
//              acp_saldesE.setValorInt(ayuSde.getCodigoSelecion());
//          }
//      }
//    }
//
//    ayuSde.setVisible(false);
//    padre.setEnabled(true);
//      padre.toFront();
//      try
//      {
//        padre.setSelected(true);
//      }
//      catch (Exception k)
//      {}
//      padre.setFoco(null);
//      acp_saldesE.requestFocus();
//  }
//  void altaSde(int sdeCodi)
//  {
//    try {
//        pdprove.insSalaDesp(padre.prv_codiE.getValorInt(), sdeCodi, dtAdd);
//        dtAdd.commit();
//        padre.msgBox("Sala Despiece Insertada");
//    } catch (SQLException k)
//    {
//        padre.Error("Error al dar alta Sala de Despiece", k);
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
//                ayuMat = new  AyuSdeMat(padre.EU, padre.vl,dtCon1)
//                {
//                    @Override
//                    public void matar  ()
//                    {
//                        ej_consMat();
//                    }
//                };
//                ayuMat.setPermiteAlta(true);
//                padre.vl.add(ayuMat);
//            }
//            ayuMat.setLocation(25, 25);
//            ayuMat.setVisible(true);
//            padre.setEnabled(false);
//            padre.setFoco(ayuMat);
//            ayuMat.iniciarVentana('M',padre.prv_codiE.getValorInt());
//        } catch (Exception j) {
//            padre.setEnabled(true);
//        }
//    }
    void activar(boolean enab)
    {
        etp_codiE.setEnabled(enab);     
        jtEti.setEnabled(etp_codiE.isNull()?false:enab);
        jtLote.setEnabled(false);
        BTraspasa.setEnabled(etp_codiE.isNull()?false:enab);
        Bressub.setEnabled(enab);
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

        etp_codbarE = new gnu.chu.controles.CTextField();
        etp_loteE = new gnu.chu.controles.CTextField(Types.CHAR,"X",20);
        etp_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        etp_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.999");
        acp_loteE = new gnu.chu.controles.CTextField(Types.CHAR,"X",20);
        jtEti = new gnu.chu.controles.CGridEditable(4);
        jtLote = new gnu.chu.controles.CGridEditable(11);
        Pcabe = new gnu.chu.controles.CPanel();
        etp_codiL1 = new gnu.chu.controles.CLabel();
        etp_codiE = new gnu.chu.controles.CComboBox();
        etp_codiL = new gnu.chu.controles.CLabel();
        etp_feprdiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        Ppie = new gnu.chu.controles.CPanel();
        BTraspasa = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel1 = new gnu.chu.controles.CLabel();
        numCajasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel2 = new gnu.chu.controles.CLabel();
        numCajParcialE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        Bressub = new gnu.chu.controles.CButton("F2",Iconos.getImageIcon("reload"));
        etp_codiL2 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        Blimpia = new gnu.chu.controles.CButton("Limpia",Iconos.getImageIcon("eraser"));
        Bcopia = new gnu.chu.controles.CButton("F5",Iconos.getImageIcon("reload"));

        setLayout(new java.awt.GridBagLayout());

        ArrayList v=new ArrayList();
        v.add("Cod.Barras"); //0
        v.add("Lote");//1
        v.add("Fec.Cad"); // 2
        v.add("KIlos"); // 3
        jtEti.setCabecera(v);
        jtEti.setAnchoColumna(new int[]{40,50,60,50});
        jtEti.setAlinearColumna(new int[]{0,0,1,2});
        ArrayList vc=new ArrayList();
        vc.add(etp_codbarE);
        vc.add(etp_loteE);
        vc.add(etp_feccadE);
        vc.add(etp_kilosE);
        try{
            jtEti.setCampos(vc);
        } catch (Exception k)
        {
            padre.Error("Error al Iniciar grid",k);
            return;
        }
        jtEti.setFormatoCampos();
        jtEti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtEti.setMaximumSize(new java.awt.Dimension(225, 99));
        jtEti.setMinimumSize(new java.awt.Dimension(225, 99));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(jtEti, gridBagConstraints);

        ArrayList v1=new ArrayList();
        v1.add("Lote"); //0
        v1.add("Matad"); // 1
        v1.add("S.Desp"); //2
        v1.add("P.N"); // 3
        v1.add("Nacido"); // 4
        v1.add("P.C"); // 5
        v1.add("Cebado"); // 6
        v1.add("P.S"); // 7
        v1.add("Sacrif"); // 8
        v1.add("Fec.Cad"); // 9
        v1.add("Fec.Pro"); // 10
        jtLote.setCabecera(v1);
        jtLote.setCanInsertLinea(false);
        jtLote.setCanDeleteLinea(false);
        jtLote.setBuscarVisible(false);
        jtLote.setAnchoColumna(new int[]{50,130,130,30,130,30,130,30,130,80,80});
        jtLote.setAlinearColumna(new int[]{0,0,0,0,0,0,0,0,0,1,1});
        ArrayList vc1=new ArrayList();
        acp_loteE.setEditable(false);
        //acp_feccadE.setEditable(false);
        vc1.add(acp_loteE); // 0
        vc1.add(acp_matadE); // 1 Matadero
        vc1.add(acp_saldesE); // 2 Sala Despiece
        vc1.add(acp_painacE.getFieldPaiCodi()); // 3
        vc1.add(acp_painacE.getFieldPaiNomb()); // 4
        vc1.add(acp_engpaiE.getFieldPaiCodi()); // 5
        vc1.add(acp_engpaiE.getFieldPaiNomb()); // 6
        vc1.add(acp_paisacE.getFieldPaiCodi()); // 7
        vc1.add(acp_paisacE.getFieldPaiNomb()); // 8
        vc1.add(acp_feccadE); // 9
        vc1.add(acp_fecproE); // 10
        try{
            jtLote.setCampos(vc1);
        } catch (Exception k)
        {
            padre.Error("Error al Iniciar grid",k);
            return;
        }
        jtLote.setFormatoCampos();
        jtLote.setDefButton(BTraspasa);
        jtLote.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLote.setMaximumSize(new java.awt.Dimension(498, 89));
        jtLote.setMinimumSize(new java.awt.Dimension(498, 89));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jtLote, gridBagConstraints);

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(409, 20));
        Pcabe.setMinimumSize(new java.awt.Dimension(409, 20));
        Pcabe.setPreferredSize(new java.awt.Dimension(409, 20));
        Pcabe.setLayout(null);

        etp_codiL1.setText("Etiqueta");
        Pcabe.add(etp_codiL1);
        etp_codiL1.setBounds(2, 2, 50, 17);
        Pcabe.add(etp_codiE);
        etp_codiE.setBounds(60, 2, 230, 17);

        etp_codiL.setText("Fec.Prod.");
        Pcabe.add(etp_codiL);
        etp_codiL.setBounds(310, 2, 60, 17);

        etp_feprdiE.setText("40");
        Pcabe.add(etp_feprdiE);
        etp_feprdiE.setBounds(370, 2, 30, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(Pcabe, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setEditable(false);
        Ppie.setMaximumSize(new java.awt.Dimension(489, 44));
        Ppie.setMinimumSize(new java.awt.Dimension(489, 44));
        Ppie.setPreferredSize(new java.awt.Dimension(489, 44));
        Ppie.setLayout(null);

        BTraspasa.setText("Traspasar");
        Ppie.add(BTraspasa);
        BTraspasa.setBounds(370, 20, 110, 20);

        cLabel1.setText("Nº Cajas");
        Ppie.add(cLabel1);
        cLabel1.setBounds(10, 2, 50, 15);

        numCajasE.setEditable(false);
        numCajasE.setBackground(java.awt.Color.cyan);
        numCajasE.setText("0");
        Ppie.add(numCajasE);
        numCajasE.setBounds(60, 2, 30, 17);

        cLabel2.setText("Parcial Nº Cajas");
        Ppie.add(cLabel2);
        cLabel2.setBounds(100, 2, 90, 15);

        numCajParcialE.setBackground(java.awt.Color.cyan);
        numCajParcialE.setText("0");
        Ppie.add(numCajParcialE);
        numCajParcialE.setBounds(190, 2, 30, 17);

        Bressub.setText("F2");
        Bressub.setToolTipText("Pone Contadores subtotal a 0");
        Ppie.add(Bressub);
        Bressub.setBounds(290, 0, 50, 20);

        etp_codiL2.setText("Producto");
        Ppie.add(etp_codiL2);
        etp_codiL2.setBounds(10, 20, 60, 18);
        Ppie.add(pro_codiE);
        pro_codiE.setBounds(70, 20, 270, 18);

        Blimpia.setToolTipText("Borra Etiquetas");
        Ppie.add(Blimpia);
        Blimpia.setBounds(370, 0, 80, 20);

        Bcopia.setText("F5");
        Bcopia.setToolTipText("Pone Contadores subtotal a 0");
        Ppie.add(Bcopia);
        Bcopia.setBounds(230, 0, 50, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        add(Ppie, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BTraspasa;
    private gnu.chu.controles.CButton Bcopia;
    private gnu.chu.controles.CButton Blimpia;
    private gnu.chu.controles.CButton Bressub;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CTextField acp_loteE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CTextField etp_codbarE;
    private gnu.chu.controles.CComboBox etp_codiE;
    private gnu.chu.controles.CLabel etp_codiL;
    private gnu.chu.controles.CLabel etp_codiL1;
    private gnu.chu.controles.CLabel etp_codiL2;
    private gnu.chu.controles.CTextField etp_feccadE;
    private gnu.chu.controles.CTextField etp_feprdiE;
    private gnu.chu.controles.CTextField etp_kilosE;
    private gnu.chu.controles.CTextField etp_loteE;
    private gnu.chu.controles.CGridEditable jtEti;
    private gnu.chu.controles.CGridEditable jtLote;
    private gnu.chu.controles.CTextField numCajParcialE;
    private gnu.chu.controles.CTextField numCajasE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    // End of variables declaration//GEN-END:variables
}
