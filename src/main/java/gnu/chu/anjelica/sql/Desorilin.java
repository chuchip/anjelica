package gnu.chu.anjelica.sql;
/**
 *
 * <p>Titulo: Desorilin</p>
 * <p>Descripción: Clase para actualizar tabla desorilin (de despieces)</p>
 * <p>Copyright: Copyright (c) 2005-2015
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @version 1.0
 *
 */

import gnu.chu.sql.DatosTabla;
import java.sql.SQLException;


public class Desorilin  implements java.io.Serializable {


     private DesorilinId id;
     private Integer proCodi;
     private Integer deoEjelot;
     private Character deoSerlot;
     private Integer proLote;
     private Integer proNumind;
     private Double deoPrcost;
     private Double deoKilos;
     private Double deoPrusu;
     
       
    public Desorilin() {
    }

	
    public Desorilin(DesorilinId id, double deoPrusu) {
        this.id = id;
        this.deoPrusu = deoPrusu;
    }
    public Desorilin(DesorilinId id, Integer proCodi, Integer deoEjelot, Character deoSerlot, Integer proLote, Integer proNumind, Double deoPrcost, Double deoKilos, Double deoPrusu) {
       this.id = id;
       this.proCodi = proCodi;
       this.deoEjelot = deoEjelot;
       this.deoSerlot = deoSerlot;
       this.proLote = proLote;
       this.proNumind = proNumind;
       this.deoPrcost = deoPrcost;
       this.deoKilos = deoKilos;
       this.deoPrusu = deoPrusu;
    }
   
    public DesorilinId getId() {
        return this.id;
    }
    
    public void setId(DesorilinId id) {
        this.id = id;
    }   
    public Integer getProCodi() {
        return this.proCodi;
    }
    
    public void setProCodi(Integer proCodi) {
        this.proCodi = proCodi;
    }
    public Integer getDeoEjelot() {
        return this.deoEjelot;
    }
    
    public void setDeoEjelot(Integer deoEjelot) {
        this.deoEjelot = deoEjelot;
    }
    public Character getDeoSerlot() {
        return this.deoSerlot;
    }
    
    public void setDeoSerlot(Character deoSerlot) {
        this.deoSerlot = deoSerlot;
    }
    public Integer getProLote() {
        return this.proLote;
    }
    
    public void setProLote(Integer proLote) {
        this.proLote = proLote;
    }
    public Integer getProNumind() {
        return this.proNumind;
    }
    
    public void setProNumind(Integer proNumind) {
        this.proNumind = proNumind;
    }
    public Double getDeoPrcost() {
        return this.deoPrcost;
    }
    
    public void setDeoPrcost(Double deoPrcost) {
        this.deoPrcost = deoPrcost;
    }
    public Double getDeoKilos() {
        return this.deoKilos;
    }
    
    public void setDeoKilos(Double deoKilos) {
        this.deoKilos = deoKilos;
    }
    public Double getDeoPrusu() {
        return this.deoPrusu;
    }
    
    public void setDeoPrusu(Double deoPrusu) {
        this.deoPrusu = deoPrusu;
    }
    public boolean select(DatosTabla dt,boolean block) throws SQLException
    {
        return select(dt,block,id.getEjeNume(),id.getDeoCodi(),id.getDelNumlin());
    }
    public static boolean select(DatosTabla dt,boolean block,int ejeNume,int deoCodi,int numLin) throws SQLException
    {
        String s="select * from desorilin where eje_nume ="+ejeNume+
                " and deo_codi = "+deoCodi+" and del_numlin="+numLin;
        return dt.select(s,block);
    }
    public int getNextLinea(DatosTabla dt) throws SQLException
    {
         String s="select max(del_numlin) as del_numlin from desorilin where eje_nume ="+id.getEjeNume()+
                " and deo_codi = "+id.getDeoCodi();
        dt.select(s);
        return dt.getInt("del_numlin",true)+1;
    }
    /**
     * Llena los campos con el registro activo
     * @param dtAdd
     * @param dt
     * @throws SQLException 
     */
    public void setDatos(DatosTabla dtAdd) throws SQLException
    {
     
    }
    public void save(DatosTabla dtAdd) throws SQLException
    {
        dtAdd.addNew("desorilin");
        dtAdd.setDato("eje_nume", getId().getEjeNume());
        dtAdd.setDato("deo_codi", getId().getDeoCodi());
        dtAdd.setDato("del_numlin", getId().getDelNumlin());
        dtAdd.setDato("pro_codi", getProCodi());
        dtAdd.setDato("deo_ejelot", getDeoEjelot());
        dtAdd.setDato("deo_serlot", getDeoSerlot());
        dtAdd.setDato("pro_lote", getProLote());
        dtAdd.setDato("pro_numind", getProNumind());
        dtAdd.setDato("deo_prcost",getDeoPrcost());
        dtAdd.setDato("deo_kilos",getDeoKilos());
        if (getDeoPrusu()!=null)
         dtAdd.setDato("deo_preusu",getDeoPrusu());
        dtAdd.setDato("deo_tiempo", "current_timestamp");
        dtAdd.update();
    }

}


