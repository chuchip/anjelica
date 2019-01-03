package gnu.chu.anjelica.sql;

import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * <p>Título: Desporig </p>
 * <p>Descripción: Clase para trabajar con tabla Desporig</p>
 * <p>Copyright: Copyright (c) 2005-2011
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
 *
 *
 */
public class Desporig  implements java.io.Serializable {


     private DesporigId id;
     private Integer tidCodi;
     private Date deoFecha;
     private int cliCodi;
     private String usuNomb;
     private Integer deoAlmori;
     private Integer deoAlmdes;
     private Integer deoEjloge;
     private String deoSeloge;
     private Integer deoNuloge;
     private Short deoCerra;
     private Short deoLotnue;
     private Integer deoNumdes;
     private Date deoFeccad;
     private Date deoFecpro;
     private Date deoFecsac;
     private String deoIncval;
     private String deoValor;
     private String deoBlock=null;
     private int prvCodi;
     private int anularControl=0;
     private char deoDesnue;
     private Date deoFecval;
     private String deoUsuval;
     private Integer deoNumuni;
    public Desporig() {
    }

	
    public Desporig(DesporigId id, String deoIncval, String deoValor, int prvCodi, char deoDesnue) {
        this.id = id;
        this.deoIncval = deoIncval;
        this.deoValor = deoValor;
        this.prvCodi = prvCodi;
        this.deoDesnue = deoDesnue;
    }
    public Desporig(DesporigId id, Integer tidCodi, Date deoFecha, String usuNomb, Integer deoAlmori, 
            Integer deoAlmdes, Integer deoEjloge, String deoSeloge, Integer deoNuloge, Short deoCerra,
            Short deoLotnue, Integer deoNumdes, Date deoFeccad, Date deoFecpro,Date deoFecSacr, String deoIncval, 
            String deoValor, String deoBlock, int prvCodi, char deoDesnue, Date deoFecval, String deoUsuval) {
       this.id = id;
       this.tidCodi = tidCodi;
       this.deoFecha = deoFecha;
       this.usuNomb = usuNomb;
       this.deoAlmori = deoAlmori;
       this.deoAlmdes = deoAlmdes;
       this.deoEjloge = deoEjloge;
       this.deoSeloge = deoSeloge;
       this.deoNuloge = deoNuloge;
       this.deoCerra = deoCerra;
       this.deoLotnue = deoLotnue;
       this.deoNumdes = deoNumdes;
       this.deoFeccad = deoFeccad;
       this.deoFecpro = deoFecpro;
       this.deoFecsac = deoFecSacr;
       this.deoIncval = deoIncval;
       this.deoValor = deoValor;
       this.deoBlock = deoBlock;
       this.prvCodi = prvCodi;
       this.deoDesnue = deoDesnue;
       this.deoFecval = deoFecval;
       this.deoUsuval = deoUsuval;
    }
   
    public DesporigId getId() {
        return this.id;
    }
    
    public void setId(DesporigId id) {
        this.id = id;
    }
    public Integer getTidCodi() {
        return this.tidCodi;
    }
    
    public void setTidCodi(Integer tidCodi) {
        this.tidCodi = tidCodi;
    }
    public Date getDeoFecha() {
        return this.deoFecha;
    }
    public void setCliente(int cliente) {
        this.cliCodi=cliente;
    }
    public int getCliente() {
        return this.cliCodi;
    }
    public void setDeoFecha(Date deoFecha) {
        this.deoFecha = deoFecha;
    }
    public String getUsuNomb() {
        return this.usuNomb;
    }
    
    public void setUsuNomb(String usuNomb) {
        this.usuNomb = usuNomb;
    }
    public Integer getDeoAlmori() {
        return this.deoAlmori;
    }
    
    public void setDeoAlmori(Integer deoAlmori) {
        this.deoAlmori = deoAlmori;
    }
    public Integer getDeoAlmdes() {
        return this.deoAlmdes;
    }
    
    public void setDeoAlmdes(Integer deoAlmdes) {
        this.deoAlmdes = deoAlmdes;
    }
    public Integer getDeoEjloge() {
        return this.deoEjloge;
    }
    
    public void setDeoEjloge(Integer deoEjloge) {
        this.deoEjloge = deoEjloge;
    }
    public String getDeoSeloge() {
        return this.deoSeloge;
    }
    
    public void setDeoSeloge(String deoSeloge) {
        this.deoSeloge = deoSeloge;
    }
    public Integer getDeoNuloge() {
        return this.deoNuloge;
    }
    
    public void setDeoNuloge(Integer deoNuloge) {
        this.deoNuloge = deoNuloge;
    }
    public Short getDeoCerra() {
        return this.deoCerra;
    }
    
    public void setDeoCerra(Short deoCerra) {
        this.deoCerra = deoCerra;
    }
    public Short getDeoLotnue() {
        return this.deoLotnue;
    }
    
    public void setDeoLotnue(Short deoLotnue) {
        this.deoLotnue = deoLotnue;
    }
    public Integer getDeoNumdes() {
        return this.deoNumdes;
    }
    
    public void setDeoNumdes(Integer deoNumdes) {
        this.deoNumdes = deoNumdes;
    }
    public Date getDeoFeccad() {
        return this.deoFeccad;
    }
    
    public void setDeoFeccad(Date deoFeccad) {
        this.deoFeccad = deoFeccad;
    }
    public Date getDeoFecpro() {
        return this.deoFecpro;
    }
    public Date getDeoFecsac() {
        return this.deoFecsac;
    }
    public void setDeoFecpro(Date deoFecpro) {
        this.deoFecpro = deoFecpro;
    }
    public void setDeoFecsac(Date deoFecsac) {
        this.deoFecsac = deoFecsac;
    }
    public String getDeoIncval() {
        return this.deoIncval;
    }
    
    public void setDeoIncval(String deoIncval) {
        this.deoIncval = deoIncval;
    }
    public String getDeoValor() {
        return this.deoValor;
    }
    
    public void setDeoValor(String deoValor) {
        this.deoValor = deoValor;
    }
    public String getDeoBlock() {
        return this.deoBlock;
    }
    
    public void setDeoBlock(String deoBlock) {
        this.deoBlock = deoBlock;
    }
    public void setAnularControl(int anularControl)
    {
        this.anularControl=anularControl;
    }
    public int getAnularControl()
    {
        return anularControl;
    }
    public int getPrvCodi() {
        return this.prvCodi;
    }
    
    public void setPrvCodi(int prvCodi) {
        this.prvCodi = prvCodi;
    }
    public char getDeoDesnue() {
        return this.deoDesnue;
    }
    
    public void setDeoDesnue(char deoDesnue) {
        this.deoDesnue = deoDesnue;
    }
    public Date getDeoFecval() {
        return this.deoFecval;
    }
    
    public void setDeoFecval(Date deoFecval) {
        this.deoFecval = deoFecval;
    }
    public String getDeoUsuval() {
        return this.deoUsuval;
    }
    
    public void setDeoUsuval(String deoUsuval) {
        this.deoUsuval = deoUsuval;
    }
    public void save(DatosTabla dtAdd,int ejeNume,EntornoUsuario EU) throws SQLException
    {
      int numDesp;
      if (getId() == null)
      {
         numDesp = utildesp.incNumDesp(dtAdd,EU.em_cod,ejeNume);
         setId(new DesporigId(ejeNume,numDesp));
      }
      dtAdd.addNew("desporig",false);
      dtAdd.setDato("eje_nume", getId().getEjeNume());
      dtAdd.setDato("deo_codi", getId().getDeoCodi());
     // dtAdd.setDato("deo_fecha", getDeoFecha());
      dtAdd.setDato("usu_nomb", EU.getUsuario());
    
      setDesorca(dtAdd);
      dtAdd.update();
    }
    public boolean select(DatosTabla dt,boolean block) throws SQLException
    {
        return select(dt,block,id.getEjeNume(),id.getDeoCodi());
    }
    public static boolean select(DatosTabla dt,boolean block,int ejeNume,int deoCodi) throws SQLException
    {
        String s="select * from desporig where eje_nume ="+ejeNume+
                " and deo_codi = "+deoCodi;
        return dt.select(s,block);
    }
    public boolean iniciar(DatosTabla dt, int ejeNume,int deoCodi) throws SQLException
    {
        setId(new DesporigId(ejeNume,deoCodi));
        if (!select(dt,false))
           return false;
         this.tidCodi = dt.getInt("tid_codi");
       this.deoFecha = dt.getDate("deo_fecha");
       this.usuNomb = dt.getString("usu_nomb");
       this.deoAlmori = dt.getInt("deo_almori");
       this.deoAlmdes = dt.getInt("deo_almdes");
       this.deoEjloge = dt.getInt("deo_ejloge");
       this.deoSeloge = dt.getString("deo_seloge");
       this.deoNuloge = dt.getInt("deo_nuloge");
       this.deoCerra = dt.getShort("deo_cerra");
       this.deoLotnue = dt.getShort("deo_lotnue");
       this.deoNumdes = dt.getInt("deo_numdes");
       this.deoFeccad = dt.getDate("deo_Feccad");
       this.deoFecpro = dt.getDate("deo_fecpro");
       this.deoFecsac = dt.getDate("deo_fecsac");
       this.deoIncval = dt.getString("deo_incval");
       this.deoValor = dt.getString("deo_Valor");
       this.deoBlock = dt.getString("deo_Block");
       this.anularControl=dt.getInt("deo_anucon");
       this.prvCodi = dt.getInt("prv_Codi");
       this.deoDesnue = dt.getString("deo_desnue").charAt(0);
       this.deoFecval = dt.getDate("deo_fecval");
       this.deoUsuval = dt.getString("deo_usuval");
       return true;
    }
    public void update(DatosTabla dtAdd) throws SQLException
    {
      if (!select(dtAdd,true))
          throw new SQLException("No encontrado cabecera despiece: "+id.getEjeNume()+"-"+id.getDeoCodi());
      dtAdd.edit();
      setDesorca(dtAdd);
     
      dtAdd.update();
    }
    /**
     * Actualizo solo el tipo de despiece (tid_codi) sobre el despiece actual.
     * @param dtAdd DatosTabla sobre el que realizar la actualizcion
     * @throws SQLException 
     */
    public void updateTidCodi(DatosTabla dtAdd) throws SQLException
    {
      if (id==null)
          return; // No hay indice. No hago Nada.
      if (!select(dtAdd,true))
          throw new SQLException("No encontrado cabecera despiece: "+id.getEjeNume()+"-"+id.getDeoCodi());
      dtAdd.edit();
      dtAdd.setDato("tid_codi", getTidCodi());
      
      dtAdd.update();
    }
    void setDesorca(DatosTabla dtAdd) throws SQLException
    {
//      dtAdd.setDato("deo_numdes", getDeoNumdes());
      dtAdd.setDato("deo_fecha", getDeoFecha());
      dtAdd.setDato("tid_codi", getTidCodi());
      dtAdd.setDato("deo_feccad",getDeoFeccad());
      dtAdd.setDato("deo_fecpro",getDeoFecpro());
      dtAdd.setDato("deo_fecsac",getDeoFecsac());
      dtAdd.setDato("deo_ejloge",getDeoEjloge());
      dtAdd.setDato("deo_seloge",getDeoSeloge());
      dtAdd.setDato("deo_nuloge",getDeoNuloge());
      dtAdd.setDato("deo_cerra",getDeoCerra());
      dtAdd.setDato("deo_lotnue",getDeoLotnue());
      dtAdd.setDato("prv_codi",getPrvCodi());
      dtAdd.setDato("deo_desnue",""+getDeoDesnue());
      dtAdd.setDato("deo_almori",""+getDeoAlmori());
      dtAdd.setDato("deo_almdes",""+getDeoAlmdes()); 
      dtAdd.setDato("deo_incval",getDeoIncval() );
      dtAdd.setDato("cli_codi",getCliente() );
      if (getDeoBlock()!=null)
        dtAdd.setDato("deo_block",getDeoBlock());
      dtAdd.setDato("deo_anucon",getAnularControl());
      if (getDeoNumuni()!=null)
        dtAdd.setDato("deo_numuni",deoNumuni);
      if (getDeoValor()!=null)
          dtAdd.setDato("deo_valor",deoValor);
    }

    public Integer getDeoNumuni() {
        return deoNumuni;
    }

    public void setDeoNumuni(Integer deoNumuni) {
        this.deoNumuni = deoNumuni;
    }
    
}


