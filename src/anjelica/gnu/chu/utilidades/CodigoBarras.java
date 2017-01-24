package gnu.chu.utilidades;

/**
 *
 * <p>Título: CodigoBarras</p>
 * <p>Descripción: Clase para almacenar el codigo de barras que se imprime en etiquetas</p>
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
 * @version 1.0
 */
public class CodigoBarras
{
    
    private String codBarra,lote,indiceEti,ejeLot;
    private int proEjeLote,proLote,proCodi,proIndi,grupoLote;
    private boolean error=false;
    private int cliente=0;
    private int avcAno;
    private String avcSerie;
    private int avcNume;
    
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
    
    public void setCodBarra(String codBarra) {       
        setCodigobarras_Int(codBarra);
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
  public void setGrupoLote(String grupolote) {
        this.grupoLote = grupoLote;
    }

    public int getProEjeLote() {
        return proEjeLote;
    }

    public void setProEjeLote(int proEjeLote) {
        this.proEjeLote = proEjeLote;
    }

    public int getProLote() {
        return proLote;
    }

    public void setProLote(int proLote) {
        this.proLote = proLote;
    }

    public int getProCodi() {
        return proCodi;
    }

    public void setProCodi(int proCodi) {
        this.proCodi = proCodi;
    }

    public int getProIndi() {
        return proIndi;
    }

    public void setProIndi(int proIndi) {
        this.proIndi = proIndi;
    }

    public double getProKilos() {
        return proKilos;
    }

    public void setProKilos(double proKilos) {
        this.proKilos = proKilos;
    }

    public String getProSerie() {
        return proSerie;
    }

    public void setProSerie(String proSerie) {
        this.proSerie = proSerie;
    }
    double proKilos;
    String proSerie;
    /**
     * Devuelve el texto del codigo de Barras para imprimir
     * @return 
     */
    public String getCodBarra() {
        return codBarra;
    }
    /**
     * Establece el codigo de cliente para ponerlo en el codigo de barras
     * @param cliente
     * 
     */
    public void setCliente(int cliente)
    {
        this.cliente=cliente;
    }
    public int getCliente()
    {
        return this.cliente;
    }
    /**
     * Devuelve el texto del Lote para imprimir.Incluye el individuo
     * @return 
     */
    public String getLote() {
        return lote;
    }
     /**
     * Devuelve el texto del Lote para imprimir
     * @param incIndividuo inlcuir individuo
     * @return 
     */
    public String getLote(boolean incIndividuo) 
    {
        if (incIndividuo)
            return lote;
        else
          return getStringLote(false);
    }
    public CodigoBarras()
    {
        
    }
     /**
   * Inicializa la clase
   * @param ejeLot
   * @param indiceEti Primera letra a poner en la etiqueta para indicar que es 'C','D', etc.
   * @param serLot
   * @param proLote
   * @param proCodi
   * @param proNumind
   * @param deoKilos
   * 
   */  
    public CodigoBarras(String indiceEti,String ejeLot,String serLot,int proLote,int proCodi,
          int proNumind,double deoKilos)
    {
        this(indiceEti,ejeLot,serLot,proLote,proCodi,proNumind,deoKilos,0);
    }
   /**
   * Inicializa la clase
   * @param ejeLot
   * @param indiceEti Primera letra a poner en la etiqueta para indicar que es 'C','D', etc.
   * @param serLot
   * @param proLote
   * @param proCodi
   * @param proNumind
   * @param deoKilos
   * @param grupoLote Grupo del Lote
   * 
   */  
    public CodigoBarras(String indiceEti,String ejeLot,String serLot,int proLote,int proCodi,
          int proNumind,double deoKilos,int grupoLote)
    {
        this.indiceEti=indiceEti;
        this.ejeLot=ejeLot;
        proEjeLote=Integer.parseInt(ejeLot);
        this.proSerie=serLot;       
        this.proLote=proLote;
        this.proCodi=proCodi;
        this.proIndi=proNumind;
        this.proKilos=deoKilos;
        initCodigoBarras();
    }
    
    public final void initCodigoBarras()
    {
       if (indiceEti.equals("A") && avcAno>0)
        codBarra= indiceEti+  Formatear.format(avcAno, "9999")
                + avcSerie+
                 Formatear.format(avcNume, "99999")
                + Formatear.format(proCodi, "99999") 
                + (cliente==0?"":"C"+cliente);             
       else
       codBarra= indiceEti+(ejeLot.length()>2? ejeLot.substring(2):ejeLot)
                + proSerie+
                 Formatear.format(proLote, "99999")
                + Formatear.format(proCodi, "99999")
                + Formatear.format(proIndi, "999")
                + (proKilos==0?"":Formatear.format(proKilos, "9999.99"))
                + (cliente==0?"":"C"+cliente);             
        lote=getStringLote(true);
    }
    
    String getStringLote(boolean incIndiv)
    {
        return  (ejeLot.length()>2? ejeLot.substring(2):ejeLot)
           + proSerie+  
             Formatear.format(grupoLote==0?proLote:grupoLote,"99999").trim()+
           (proIndi>0 && incIndiv?"/"+ Formatear.format(proIndi,"#99").trim():"");

    }
    public void setAlbaranVenta(int avcAno,String avcSerie,int avcNume)
    {
        this.avcAno=avcAno;
        this.avcSerie=avcSerie;
        this.avcNume=avcNume;
    }
    public CodigoBarras(String codBarras) throws NumberFormatException
    {      
        setCodigobarras_Int(codBarras);
    }
    private void setCodigobarras_Int(String codBarras)
    {
        codBarra=codBarras;
         if ( Character.isDigit(codBarras.charAt(0))) 
            error=formatoAntiguo(codBarras);
        else
            error=formatearCodBarraNuevo(codBarras);
    }
    /**
     * Devuelve true si hubo error.
     * @param codBarras Codigo Barras a intentar parsear
     * @return true si hubo error. False si todo fue bien
     */
    private boolean formatearCodBarraNuevo(String codBarras)
    {      
       
         /*
        Indice: C
     Año: 03
     Serie: A
     Partida: 01240
     Producto: 10951
     Individuo: 001
     Peso: 0025.33
     Ejemplo: C03A01240109510010025.33
              012345678901234567890123
        */
        if (codBarras.length()<17)
            return true;
        try {
            proEjeLote=Integer.parseInt(codBarras.substring(1, 3))+2000;
            proSerie=codBarras.substring(3,4);       
            proLote=Integer.parseInt(codBarras.substring(4,9));
            proCodi=Integer.parseInt( codBarras.substring(9,14));
            proIndi=Integer.parseInt(codBarras.substring(14,17));
        } catch ( java.lang.NumberFormatException k)
        {
            mensajes.mensajeAviso("Codigo barras (Nuevo): "+codBarras+" No es valido");
            return true;
        }
        proKilos=0;
        
        if (codBarras.length()>17)
            proKilos=Double.parseDouble(codBarras.substring(17,24));
        return false;
    }
    
    /**
     * Parsea codigo barras con formato antiguo
     * @param valor Codigo barras a intentar parsear
     * @return false si todo fue bien. True en caso de error.
     */
    private boolean formatoAntiguo(String valor)
    {    
        /*
     Año: 03
     Emp: 1 o 01
     Serie: A
     Partida: 1240 o 01240
     Producto: 10951
     Individuo: 001
     Peso: 025.33
     Ejemplo: 0301A124010951001025.33
              01234567890123456789012
             
     */
        if (valor.length()<23)
            return true;        
        try {
          proEjeLote=Integer.parseInt(valor.substring(0, 2))+2000;

    // Localizo donde esta la Serie.
        int posSerie=4;
        for (int n=2;n<6;n++)
        {
          if (!Character.isDigit(valor.charAt(n)))
            posSerie=n; // NO Es tipo  Digito. Hemos encontrado la serie
        }

        if (posSerie==4)
        {     
          proSerie=valor.substring(4,5);
          proLote=Integer.parseInt(valor.substring(5,9));
        }
        else
        {
          proSerie=valor.substring(3,4);  
          proLote=Integer.parseInt(valor.substring(4,9));
        }
        proCodi=Integer.parseInt(valor.substring(9,14));
        if (valor.length()==23)
        {
          proIndi = Integer.parseInt(valor.substring(14,17));
          proKilos=Double.parseDouble(valor.substring(17,23));
        }
        else
        {
          proIndi=Integer.parseInt(valor.substring(14,18));
          proKilos=Double.parseDouble(valor.substring(18,23));
        }
    } catch (NumberFormatException k){        
        mensajes.mensajeAviso("Codigo barras (Viejo): "+valor+" No es valido");
        return true;
    }
    return false;
  }
   
}
