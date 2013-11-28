package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Título: AutoDesp </p>
 * <p>Descripción: Ventana para generar Auto Despiece . Es llamada desde MantDesp.
 * </p>
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
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.sql.Desorilin;
import gnu.chu.anjelica.sql.DesorilinId;
import gnu.chu.anjelica.sql.Desporig;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class AutoDesp extends ventana {
    actStkPart stkPart;
    int newDeoCodi=0;
    final String SERIE="X";
    DatosTabla dtAdd,dtBloq;
    ventanaPad papa;
    int ejeNume,deoCodi, numLin;
    ArrayList<DesorilinId> desorilinId;
    Desorilin desorli;
    String s; 
    
    public AutoDesp(ventanaPad padre) {
        this.dtAdd=padre.dtAdd;
        this.dtStat=padre.dtStat;
        this.dtBloq=padre.dtBloq;
        this.vl=padre.vl;
        this.EU=padre.EU;
        this.papa=padre;
        statusBar=new StatusBar(this);
        this.add(statusBar,BorderLayout.SOUTH);
        initComponents();
        
        this.setTitle("Auto Despieces");
        setResizable(false);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(440,170);
        setVersion("20120105");
    }
    
    /** Creates new form AutoDesp */
    public AutoDesp() {
        initComponents();
    }
    @Override
    public void iniciarVentana() throws Exception
    {
      Pprinc.setDefButton(Baceptar);
      pro_codoriE.iniciar(dtStat, papa, vl, EU);  
      pro_codfinE.iniciar(dtStat, papa, vl, EU); 
      tid_codiE.iniciar(dtStat, papa, vl, EU);
      stkPart = new actStkPart(dtAdd, EU.em_cod);
      activarEventos();
    }
    private void activarEventos()
    {
        Baceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarDespieces();
            }
        });
        Bcancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matar();
            }
        });
    }
   
    void generarDespieces()
    {
        try {
            if (!tid_codiE.controla())
            {
                mensajeErr("Tipo de despiece NO valido");
                return;
            }
            if (!pro_codfinE.controla())
            {
                mensajeErr(pro_codfinE.getMsgError());
                return;
            }
         
            if (! MantTipDesp.checkArticuloEntrada(dtStat, pro_codoriE.getValorInt(), tid_codiE.getValorInt()))
            {
                  mensajeErr("ARTICULO Origen no valido para este tipo de despiece");
                  return;
            }
            if (tid_codiE.getValorInt()==MantTipDesp.AUTO_DESPIECE)
            {
              if (! MantTipDesp.esEquivalente( pro_codoriE.getValorInt(),pro_codfinE.getValorInt(),dtStat))
              {
                 mensajeErr("Para auto-despieces solo permitidos el mismo producto o equivalentes");
                 return;
              }
            }
            else
            {
              if (! MantTipDesp.checkArticuloSalida(dtStat, pro_codfinE.getValorInt(),  tid_codiE.getValorInt()))
              {
                  mensajeErr("ARTICULO Final no valido para este tipo de despiece");
                return;
              }
            }
         
            newDeoCodi=0;
          
            Iterator<DesorilinId> iter=desorilinId.iterator();
            DesorilinId deorliId;
          
            int defOrden=1;
            int delNumlin=1,prvCodi=1;
            int defEjelot,proLote=0,proLotOri,proNumind;
            long deoTiempo;
            Date deoFeccad=null;
            String deoSerlot;
            String deoFecha="";
            double deoKilos,deoPrcost;
            while (iter.hasNext())
            {
                deorliId =iter.next();
                ejeNume=deorliId.getEjeNume();
                deoCodi=deorliId.getDeoCodi();
                numLin=deorliId.getDelNumlin();
                if (newDeoCodi==0)
                {
                    // Creo Nuevo Despiece
                    Desorilin.select(dtAdd, false,ejeNume,deoCodi,numLin);
                    proLote=dtAdd.getInt("pro_lote");                  
                    newDeoCodi=utildesp.incNumDesp(dtAdd,EU.em_cod,ejeNume);
                    if (! Desporig.select(dtAdd, isIcon, ejeNume, deoCodi))
                        throw new SQLException("Numero despiece: "+deoCodi+" NO encontrado");
                    deoFeccad=dtAdd.getDate("deo_feccad");
                    deoFecha=dtAdd.getString("deo_fecha");
                    prvCodi=dtAdd.getInt("prv_codi");
                    dtAdd.edit();
                    dtAdd.setDato("deo_codi",newDeoCodi);
                    dtAdd.setDato("tid_codi",tid_codiE.getValorInt());
                    dtAdd.setDato("deo_seloge",SERIE);
                    dtAdd.setDato("deo_nuloge",proLote);
                    dtAdd.setDato("deo_lotnue",-1); // Marcado como Lote Nuevo 
                    dtAdd.setDato("deo_numdes",0);
                    dtAdd.setDato("deo_desnue","N"); // se marca como que no es desp. nuestro
                    dtAdd.copy(dtBloq);
                }
                Desorilin.select(dtAdd, true,ejeNume,deoCodi,numLin);
                // Guardo variables temporales
                defEjelot=dtAdd.getInt("deo_ejelot");
                deoSerlot=dtAdd.getString("deo_serlot");
                proLotOri=dtAdd.getInt("pro_lote");
                proNumind=dtAdd.getInt("pro_numind");
                deoKilos=dtAdd.getDouble("deo_kilos");
                deoPrcost=dtAdd.getDouble("deo_prcost");
                deoTiempo=dtAdd.getTimeStamp("deo_tiempo").getTime();
                deoTiempo--;
                // Modifico linea origen,poniendo la nueva serie, lote y producto
                dtAdd.edit();
                dtAdd.setDato("deo_serlot",SERIE);
                dtAdd.setDato("pro_lote",proLote);  // Pongo todos los numeros de lote igual
                dtAdd.setDato("pro_codi",pro_codfinE.getValorInt());
                dtAdd.update();
                // Copio linea antigua y la modifico poniendo el nuevo codigo producto.
                Desorilin.select(dtAdd, true,ejeNume,deoCodi,numLin); 
                dtAdd.edit();
                dtAdd.setDato("deo_codi",newDeoCodi);
                dtAdd.setDato("deo_serlot",deoSerlot);
                dtAdd.setDato("del_numlin",delNumlin++);
                dtAdd.setDato("pro_lote",proLotOri);
                dtAdd.setDato("pro_codi",pro_codoriE.getValorInt());         
                dtAdd.copy(dtBloq);
               
                dtBloq.addNew("v_despfin");
                s = "SELECT * FROM v_despfin " +
                    " WHERE eje_nume = " + ejeNume+
                    " and deo_codi = " + deoCodi;
                dtAdd.select(s,true);
                dtAdd.edit();
                dtAdd.setDato("deo_codi",newDeoCodi);
                dtAdd.setDato("pro_codi",pro_codfinE.getValorInt());              
                dtAdd.setDato("def_orden", defOrden++);
                dtAdd.setDato("def_ejelot", defEjelot);
                dtAdd.setDato("def_serlot", SERIE);
                dtAdd.setDato("pro_lote",proLote);
                dtAdd.setDato("pro_numind",proNumind);
                dtAdd.setDato("def_tippes","V");
                dtAdd.setDato("def_numdes",0); // Grupo del N. Desp.
                dtAdd.setDato("def_unicaj",1);
                dtAdd.setDato("def_prcost",deoPrcost);
                dtAdd.setDato("def_tiempo", new Timestamp(deoTiempo));
                dtAdd.setDato("def_numpie",1);//def_numpieE.getValorInt());
                dtAdd.setDato("def_kilos", deoKilos); //def_kilosE.getValorDec());
                dtAdd.setDato("def_feccad",deoFeccad);//def_kilosE.getValorDec());
                dtAdd.copiaRegistro(dtBloq,null,null,0);
                //Creo apunte en stock partidas.
                stkPart.sumar(defEjelot, SERIE, proLote, proNumind, pro_codfinE.getValorInt(),
                    dtAdd.getInt("alm_codi"),
                    0, 0, deoFecha, actStkPart.CREAR_SI,
                    prvCodi, deoFeccad);
            }
            dtAdd.commit();
            msgBox("Generado Nuevo despiece "+newDeoCodi);
            matar();
        } catch (Exception k)
        {
            try {
                dtAdd.rollback();
            } catch (SQLException j){}
            papa.Error("Error al generar autodespiece",k);
        }
    }
   /**
     * Pone los valores con los que se trabajara.
     * Realiza un requestFocus al codigo final. 
     * @param proCodi Codigo del producto de Origen.
     * @param desorilinId ArrayList con clases DesorilinId que apuntan a las
     * diferentes lineas de los despieces.
     */ 
    public void reset(int proCodi,ArrayList<DesorilinId> desorilinId)
    {   
        newDeoCodi=0;
        this.desorilinId=desorilinId;
        pro_codoriE.setValorInt(proCodi,true);
        pro_codfinE.requestFocusLater();
    }
    public int getDeoCodiNew()
    {
        return newDeoCodi;
    }
    public void setTidCodi(int tidCodi)
    {
        tid_codiE.setValorDec(tidCodi);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        tid_codiE = new gnu.chu.camposdb.tidCodi2();
        pro_codoriE = new gnu.chu.camposdb.proPanel();
        pro_codfinE = new gnu.chu.camposdb.proPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));

        Pprinc.setLayout(null);

        cLabel1.setText("Tipo Despiece ");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(10, 20, 90, 15);

        cLabel2.setText("Producto Final");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(10, 40, 100, 15);

        cLabel3.setText("Producto Origen ");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(10, 0, 100, 15);

        tid_codiE.setAncTexto(50);
        Pprinc.add(tid_codiE);
        tid_codiE.setBounds(110, 20, 300, 17);

        pro_codoriE.setEnabled(false);
        Pprinc.add(pro_codoriE);
        pro_codoriE.setBounds(110, 0, 300, 17);
        Pprinc.add(pro_codfinE);
        pro_codfinE.setBounds(110, 40, 300, 17);

        Baceptar.setText("Aceptar");
        Pprinc.add(Baceptar);
        Baceptar.setBounds(90, 60, 100, 30);

        Bcancelar.setText("Cancelar");
        Pprinc.add(Bcancelar);
        Bcancelar.setBounds(230, 60, 100, 30);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.camposdb.proPanel pro_codfinE;
    private gnu.chu.camposdb.proPanel pro_codoriE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    // End of variables declaration//GEN-END:variables
}
