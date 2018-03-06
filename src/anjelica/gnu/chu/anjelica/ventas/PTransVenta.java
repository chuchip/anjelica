package gnu.chu.anjelica.ventas;
/**
 *
 * <p>Titulo: PTransVenta </p>
 * <p>Descripción: Panel para mantenimiento Hoja Trasnportistas  de Ventas</p>
 *<p> Es utilizada en pdalbara</p>
 * <p>Copyright: Copyright (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN
 * NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 *
 */ 
import gnu.chu.controles.CPanel;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.ventana;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Hashtable;

public class PTransVenta extends CPanel
{
    boolean incHojaTrans;
    ventana padre;
   int avcId; // Numero Albaran Venta
   DatosTabla dt;
    final static String CAJAS="C";
    final static String BOLSAS="B";
    final static String PALETS="P";
    final static String COLGADO="F";
    
    public PTransVenta() {
        initComponents();
    }
    
    public void iniciarPanel(DatosTabla dt,boolean incHojaTrans,ventana papa) throws SQLException
    {
        this.padre=papa;
        this.incHojaTrans=incHojaTrans;
        this.dt=dt;
        String s="select tra_codi,tra_nomb from v_transventext order by tra_nomb";
        dt.select(s);
        tra_codiE.addDatos(dt);
        if (incHojaTrans)
            activarEventos();
    }
    void activarEventos()
    {
        tra_codiE.addCambioListener(new CambioListener()
        {
            @Override
            public void cambio(CambioEvent event) {
                try
                {
                    verUltimosValores(0,tra_codiE.getValorInt());
                } catch (SQLException ex)
                {
                    padre.Error("Error al buscar ultimos valores", ex);
                }
            }
        });
    }
    @Override
    public void resetCambio()
    {
        tra_codiE.resetCambio();
    }
    public void setAvcId(int avcId)
    {
        this.avcId=avcId;
    }
    public CPanel getPanelBultos()
    {
        return Pbultra;
    }
    public void removePanelBultos()
    {
        this.remove(Pbultra);
    }
    public int getAvcId()
    {
        return this.avcId;
    }
    public void actualizaPantalla() throws SQLException
    {
        if (incHojaTrans)
            actualizaPantallaTrans();
        actualizaPantallaBultos();
    }
    /**
     * Muestra los ultimos valores para un cliente y/o transportista
     * @param cliCodi 
     * @param traCodi Si es 0, buscara solo por cliente.
     * @throws SQLException 
     */
    public void verUltimosValores(int cliCodi,int traCodi) throws SQLException
    {
      String s="select  ht.* from v_albavec as a,albvenht as ht where cli_codi="+   cliCodi+
          (traCodi==0?"":" and tra_codi="+traCodi)+
          " and ht.avc_id = a.avc_id order by ht.avc_id desc";
      if (!dt.select(s))
      {
          if (traCodi==0) 
            return;
          s="select  ht.* from v_albavec as a,albvenht as ht where  tra_codi="+traCodi+
            " and ht.avc_id = a.avc_id order by ht.avc_id desc";
          if (!dt.select(s))
              return;
      }
      tra_codiE.setValorInt(dt.getInt("tra_codi"));    
      avt_portesE.setValor(dt.getString("avt_portes"));      
      avt_connomE.setText(dt.getString("avt_connom"));
      avt_condniE.setText(dt.getString("avt_condni"));
      avt_matri1E.setText(dt.getString("avt_matri1"));
      avt_matri2E.setText(dt.getString("avt_matri2"));
      
    }
     private void actualizaPantallaTrans() throws SQLException
     {
        this.resetTexto();
        if (avcId==0)
            return;
        String s="select * from albvenht where avc_id = "+avcId;
        if (!dt.select(s))
            return;
        tra_codiE.setValorInt(dt.getInt("tra_codi"));
        avt_fectraE.setDate(dt.getDate("avt_fectra"));
        avt_portesE.setValor(dt.getString("avt_portes"));
        avt_kilosE.setValorDec(dt.getDouble("avt_kilos"));
        avt_connomE.setText(dt.getString("avt_connom"));
        avt_condniE.setText(dt.getString("avt_condni"));
        avt_matri1E.setText(dt.getString("avt_matri1"));
        avt_matri2E.setText(dt.getString("avt_matri2"));
     }
     
     public void  setKilosBrutos(double kilos)
     {
         avt_kilosE.setValorDec(kilos);
     }
    /**
     * Actualiza Pantalla Bultos
     * @throws SQLException 
     */
    private void actualizaPantallaBultos() throws SQLException
    {
        resetUnidades();
        if (avcId==0)
            return;
        String s="select * from albventra where avc_id = "+avcId;
        if (!dt.select(s))
            return;
        do
        {
            String tipo=dt.getString("avt_tipo");
            switch (tipo)
            {
                case CAJAS:
                     avt_numcajE.setValorInt(dt.getInt("avt_unid"));
                     break;
                case BOLSAS:
                    avt_numbolE.setValorInt(dt.getInt("avt_unid"));
                    break;
                case COLGADO:
                    avt_numcolE.setValorInt(dt.getInt("avt_unid"));
                    break;
                case PALETS:
                    avt_numpalE.setValorInt(dt.getInt("avt_unid"));          
            }  
        } while (dt.next());
    }
    /**
     * Devuelve el numero de cajas
     * @return 
     */
    public int getNumeroCajas()
    {
        return avt_numcajE.getValorInt();
    }
    
    public boolean checkValores()
    {
        if (!incHojaTrans)
              return true;
        if (tra_codiE.isNull() )
            return true;
        if (!tra_codiE.controla(true))
        {
           padre.msgBox("Transportista NO valido");
           return false;           
        }
        
        return true;
    }
    public void guardaValores(boolean commit) throws SQLException,ParseException
    {
        if (incHojaTrans)
            guardaValoresTrans();
        guardaValoresBultos(commit);
    }
    /**
     * 
     * @throws SQLException 
     */
     private void guardaValoresTrans() throws SQLException,ParseException
     {
        if (tra_codiE.isNull())
            return;
        if (dt.select("select * from albvenht where avc_id="+avcId,true))
            dt.edit();
        else
        {
            dt.addNew("albvenht");
            dt.setDato("avc_id",avcId);            
        }      
        dt.setDato("tra_codi",tra_codiE.getValorInt());
        dt.setDato("avt_fectra",avt_fectraE.getDate());
        dt.setDato("avt_portes",avt_portesE.getValor());
        dt.setDato("avt_kilos",avt_kilosE.getValorDec());
        dt.setDato("avt_connom",avt_connomE.getText());
        dt.setDato("avt_condni",avt_condniE.getText());
        dt.setDato("avt_matri1",avt_matri1E.getText());
        dt.setDato("avt_matri2",avt_matri2E.getText());
        dt.update();
     }
    /**
     * 
     * @throws SQLException 
     */
    private void guardaValoresBultos(boolean commit) throws SQLException
    {
        guardaBultos(BOLSAS,avt_numbolE.getValorInt());
        guardaBultos(CAJAS,avt_numcajE.getValorInt());
        guardaBultos(PALETS,avt_numpalE.getValorInt()); 
        guardaBultos(COLGADO,avt_numcolE.getValorInt());
        if (commit)
            dt.commit();
    }
    /**
     * Guarda o actualiza valor para un tipo bulto
     * @param tipo
     * @param unid
     * @throws SQLException 
     */
    void guardaBultos(String tipo,int unid) throws SQLException
    {
        if (dt.select("select * from albventra where avc_id="+avcId+" and avt_tipo='"+tipo+"'",true))
            dt.edit();
        else
        {
            dt.addNew("albventra");
            dt.setDato("avc_id",avcId);
            dt.setDato("avt_tipo",tipo);
        }      
        dt.setDato("avt_unid",unid);
        dt.update();
    }
    @Override
    public void resetTexto()
    {
        if (incHojaTrans)
        {
            super.resetTexto();
            tra_codiE.setText("");
        }
        resetUnidades();
    }
    public void resetUnidades()
    {
        avt_numcajE.resetTexto();
        avt_numbolE.resetTexto();
        avt_numpalE.resetTexto();
        avt_numcolE.resetTexto();
    }
    
    /**
     * Devuelve numero de unidades del tipo bulto elegido
     * @param tipo
     * @return -1 si no es un tipo valido
     */
    public int getUnidades(String tipo)
    {
        switch (tipo)
        {
            case CAJAS:
                return avt_numcajE.getValorInt();
            case BOLSAS:
                return avt_numbolE.getValorInt();
            case COLGADO:
                return avt_numcolE.getValorInt();
            case PALETS:
                return avt_numpalE.getValorInt();             
        }
        return  -1;
    }
    
    public static void getDatosBultos(DatosTabla dt,Hashtable ht,int avcId) throws SQLException
    {
        String s="select * from albventra where avc_id = "+avcId;
        if (!dt.select(s))
            return;
        do
        {
            String tipo=dt.getString("avt_tipo");
            switch (tipo)
            {
                case CAJAS:
                     ht.put("avt_numcaj",dt.getInt("avt_unid"));
                     break;
                case BOLSAS:
                    ht.put("avt_numbol",dt.getInt("avt_unid"));                    
                    break;
                case COLGADO:
                    ht.put("avt_numcol",dt.getInt("avt_unid"));
                    break;
                case PALETS:
                    ht.put("avt_numpal",dt.getInt("avt_unid"));
            }  
        } while (dt.next());
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cLabel1 = new gnu.chu.controles.CLabel();
        tra_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        avt_connomE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        cLabel3 = new gnu.chu.controles.CLabel();
        avt_condniE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        cLabel4 = new gnu.chu.controles.CLabel();
        avt_fectraE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel5 = new gnu.chu.controles.CLabel();
        avt_matri2E = new gnu.chu.controles.CTextField(Types.CHAR,"X",20);
        avt_matri1E = new gnu.chu.controles.CTextField(Types.CHAR,"X",20);
        cLabel7 = new gnu.chu.controles.CLabel();
        Pbultra = new gnu.chu.controles.CPanel();
        cLabel11 = new gnu.chu.controles.CLabel();
        avt_numpalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel9 = new gnu.chu.controles.CLabel();
        avt_numcajE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel8 = new gnu.chu.controles.CLabel();
        avt_numbolE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel10 = new gnu.chu.controles.CLabel();
        avt_numcolE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel6 = new gnu.chu.controles.CLabel();
        avt_portesE = new gnu.chu.controles.CComboBox();
        cLabel12 = new gnu.chu.controles.CLabel();
        avt_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");

        setLayout(null);

        cLabel1.setText("Portes");
        add(cLabel1);
        cLabel1.setBounds(220, 30, 60, 17);

        tra_codiE.setFormato(Types.DECIMAL, "##9");
        tra_codiE.setAncTexto(40);
        add(tra_codiE);
        tra_codiE.setBounds(100, 10, 290, 17);

        cLabel2.setText("Transportista");
        add(cLabel2);
        cLabel2.setBounds(10, 11, 90, 15);
        add(avt_connomE);
        avt_connomE.setBounds(100, 60, 290, 17);

        cLabel3.setText("D.N.I");
        add(cLabel3);
        cLabel3.setBounds(10, 80, 40, 17);
        add(avt_condniE);
        avt_condniE.setBounds(100, 80, 120, 17);

        cLabel4.setText("Matricula");
        add(cLabel4);
        cLabel4.setBounds(10, 100, 60, 17);
        add(avt_fectraE);
        avt_fectraE.setBounds(140, 140, 80, 17);

        cLabel5.setText("Kilos");
        add(cLabel5);
        cLabel5.setBounds(10, 30, 70, 17);

        avt_matri2E.setMayusc(true);
        add(avt_matri2E);
        avt_matri2E.setBounds(100, 120, 120, 17);

        avt_matri1E.setMayusc(true);
        add(avt_matri1E);
        avt_matri1E.setBounds(100, 100, 120, 17);

        cLabel7.setText("Matricula 2");
        add(cLabel7);
        cLabel7.setBounds(10, 120, 70, 17);

        Pbultra.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Pbultra.setLayout(null);

        cLabel11.setText("Palets");
        Pbultra.add(cLabel11);
        cLabel11.setBounds(2, 2, 40, 17);

        avt_numpalE.setText("0");
        Pbultra.add(avt_numpalE);
        avt_numpalE.setBounds(40, 2, 25, 17);

        cLabel9.setText("Cajas");
        Pbultra.add(cLabel9);
        cLabel9.setBounds(72, 2, 40, 17);

        avt_numcajE.setText("0");
        Pbultra.add(avt_numcajE);
        avt_numcajE.setBounds(110, 2, 25, 17);

        cLabel8.setText("Bolsas");
        Pbultra.add(cLabel8);
        cLabel8.setBounds(142, 2, 50, 17);

        avt_numbolE.setText("0");
        Pbultra.add(avt_numbolE);
        avt_numbolE.setBounds(184, 2, 25, 17);

        cLabel10.setText("Colgado");
        Pbultra.add(cLabel10);
        cLabel10.setBounds(210, 2, 50, 17);

        avt_numcolE.setText("0");
        Pbultra.add(avt_numcolE);
        avt_numcolE.setBounds(260, 2, 25, 17);

        add(Pbultra);
        Pbultra.setBounds(10, 170, 290, 30);

        cLabel6.setText("Conductor");
        add(cLabel6);
        cLabel6.setBounds(10, 60, 90, 17);

        avt_portesE.addItem("Pagado", "P");
        avt_portesE.addItem("Debido", "D");
        add(avt_portesE);
        avt_portesE.setBounds(310, 30, 80, 20);

        cLabel12.setText("Fecha Transporte ");
        add(cLabel12);
        cLabel12.setBounds(10, 140, 110, 17);

        avt_kilosE.setMayusc(true);
        add(avt_kilosE);
        avt_kilosE.setBounds(70, 30, 80, 17);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Pbultra;
    private gnu.chu.controles.CTextField avt_condniE;
    private gnu.chu.controles.CTextField avt_connomE;
    private gnu.chu.controles.CTextField avt_fectraE;
    private gnu.chu.controles.CTextField avt_kilosE;
    private gnu.chu.controles.CTextField avt_matri1E;
    private gnu.chu.controles.CTextField avt_matri2E;
    private gnu.chu.controles.CTextField avt_numbolE;
    private gnu.chu.controles.CTextField avt_numcajE;
    private gnu.chu.controles.CTextField avt_numcolE;
    private gnu.chu.controles.CTextField avt_numpalE;
    private gnu.chu.controles.CComboBox avt_portesE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CLinkBox tra_codiE;
    // End of variables declaration//GEN-END:variables
}
