package gnu.chu.camposdb;
/**
 *
 * <p>Título: tidCodi2 </p>
 * <p>Descripción: Panel con un textfield para poner los tipos de despiece </p>
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
import gnu.chu.anjelica.despiece.MantTipDesp;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.winayu.AyuTid;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class tidCodi2 extends CLinkBox
{
  private boolean ADMIN=false;
  private boolean tidAsprfi=false;
  private boolean tidAuclpe=false;
  private boolean tidResadm=false;
  private int NUMDESPLIBRE= 10;
  private boolean incluirEstaticos=true;
  AyuTid ayuTid;
  CInternalFrame infFrame;
  JLayeredPane layPane;
  EntornoUsuario EU;
  DatosTabla dt;
  String msgError;
  ArrayList<ArrayList> articList=new ArrayList();
  private boolean tidActiv=true; 
  private boolean swIncDespLibre=true; // Incluir despiece libre
  private boolean swModoCons=true; // Modo consulta. No limita el despiece libre
  private String deoCodi=null;
  boolean verSoloActivo=false;
  
  public tidCodi2()
  {
    this(false);
  }

  public tidCodi2(boolean verBoton)
  {
    super(verBoton);
  }


  public void iniciar(DatosTabla dtb, CInternalFrame infFrame,
                      JLayeredPane layPane, EntornoUsuario entUsuario) throws SQLException
  {
    this.setFormato(Types.DECIMAL,"###9");
    this.setAncTexto(40);
    this.infFrame=infFrame;
    this.layPane=layPane;
    EU=entUsuario;
    NUMDESPLIBRE = EU.getValorParam("numdesplibre", NUMDESPLIBRE);
    dt=dtb;
    texto.setToolTipText("Pulse F3 para buscar tipos despiece");
    combo.setFocusable(false);
    swIncDespLibre=dtb.select("select * from tipodesp  as t "+
            " WHERE tid_activ != 0 "+ // Solo si esta activo
           " and tid_codi = "+MantTipDesp.LIBRE_DESPIECE);
    
    if (! releer())
      throw new SQLException ("tidCodi2 (iniciar) "+msgError);
    activarEventos();
  }
  /**
   * Incluir despiece Libre.
   * @param incDespLibre 
   */
  public void setIncluirDespiecelibre(boolean incDespLibre)
  {
      swIncDespLibre=incDespLibre;
  }
  private void activarEventos()
  {
    texto.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3 )
          consTid();
      }
    });
  }
  @Override
  public boolean controla()
  {
    return controla(true);
  }

  /**
   * Funcion de controlar
   * @param reqFoc realizar req. focus en caso de error.
   *
   * @return boolean -> true si todo es correcto
   */
  @Override
    public boolean controla(boolean reqFoc) 
    {
        String s;
        setError(false);
        msgError = "";
        if (isNull())
        {
            msgError = "Debe introducir un Tipo de Despiece";
            setError(true);
            requestFocus();
            return false;
        }
        super.controla(false);
        if (getError())
        {
            msgError = "Tipo de Despiece no Valido";
            setError(true);
            requestFocus();
            return false;
        }
        
        try
        {
            if (getValorInt()<9990)
            {
                s = "select  tid_resadm,tid_activ,tid_asprfi,tid_auclpe from tipodesp "
                    + " WHERE tid_codi = " + getValorInt()
                    + " and tid_activ != 0";// Solo activos
                tidActiv = dt.select(s);
                if (!tidActiv)
                {
                    msgError = "Tipo de Despiece esta inactivo";
                    setError(true);
                    requestFocus();
                    return false;
                }
                
                tidResadm=dt.getInt("tid_resadm")!=0;               
                tidAsprfi=dt.getInt("tid_asprfi")!=0;
                tidAuclpe=dt.getInt("tid_auclpe")!=0;
            }
            if (getValorInt() == MantTipDesp.LIBRE_DESPIECE && !swModoCons && !ADMIN)
            {               
                s = "select count(*) as cuantos from desporig  WHERE tid_codi = " + MantTipDesp.LIBRE_DESPIECE
                    + (deoCodi == null ? "" : " and deo_codi != " + deoCodi)
                    + " and deo_fecha = current_date and eje_nume = " + EU.ejercicio;
                dt.select(s);
                if (dt.getInt("cuantos") >= NUMDESPLIBRE)
                {
                    msgError = "Excedidos número máximo de despieces Libres para hoy.(" + dt.getInt("cuantos") +
                        " >= " + NUMDESPLIBRE + ")";
//                    mensajes.mensajeAviso(msgError);
                    setError(true);
                    requestFocus();
                    return false;
                }
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(tidCodi2.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    public boolean getAsignarProdSalida()
    {
        return tidAsprfi;
    }
     public boolean getClasificaPeso()
    {
        return tidAuclpe;
    }
     /**
      * Devuelve si el tipo despiece asignado es modo Admin
      * @return 
      */
    public boolean getModoAdmin()
    {
        return tidResadm;
    }
     
  public void setModoConsulta(boolean modoConsulta )
  {
      swModoCons=modoConsulta;
  }
  public boolean getModoConsulta()
  {
      return swModoCons;
  }
  /**
   * Añade un articulo para los tipos de despiece. 
   * Incluye tambien todos sus equivalentes.
   * 
   * @param proCodi
   * @throws SQLException 
   */
  public void addArticulo(int proCodi) throws SQLException
  {
    
    ArrayList<Integer> al=MantArticulos.getEquivalentes(proCodi, dt);
    
    if (al==null)
        al=new ArrayList();
    al.add(proCodi);
    articList.add(al);
  }
  public void setDeoCodi(String deoCodi)
  {
      this.deoCodi=deoCodi;
  }
  /**
   * Llena el combo con los tipos de despieces disponible segun el articulo mandado.
   * @param lista Lista de articulos
   * @throws SQLException 
   */
  public void setArticulos(ArrayList<String> lista) throws SQLException
  {
    clearArticulos();
    if (lista==null)
        return;
    int nRow=lista.size();
    int art;
    for (int n=0;n<nRow;n++)
    {
        art=Integer.parseInt(lista.get(n).trim());
        if (art!=0)
            addArticulo(art);
    }   
  }
  public boolean isAutoMer() throws SQLException
  {
      String s="select  tid_merven from tipodesp "
        + " WHERE tid_codi = " +getValorInt();
      if (! dt.select(s))
          return false;
      return dt.getInt("tid_merven")!=0;
  }
  public ArrayList getArticulos()
  {
      return articList;
  }
  public void clearArticulos()
  {
    articList.clear();
  }
 
  /**
   * Devuelve si El tipo despiece actual esta inactivo
   * @return 
   */
  public boolean getTidActiv()
  {
      return this.tidActiv;
  }
  /**
   * Rellena el Combo con los Despieces seleccionados.
   * Presenta el codigo del Almacen
   * @return boolean retorna un true si todo ha sido correcto
     * @throws java.sql.SQLException
   */
  public boolean releer() throws SQLException
  {
    setError(false);
    String s="select tid_codi, tid_nomb from tipodesp  as t "+           
             " where tid_codi < 9990 "+
             (verSoloActivo?" and tid_activ != 0":"")+ // Solo activos
             " ORDER BY t.tid_nomb";
    if (! articList.isEmpty())
    {
        removeAllItems();
//      // Busco Numero de articulos distintos.
//      int artDif=0;
//      for (int n=0;n<articList.size();n++)
//      {
//          if (articList.indexOf(articList.get(n))>=n)
//              artDif++;
//      }      
      s="select t.tid_codi, t.tid_nomb from tipodesp as t,tipdesent as te  WHERE "+
        " t.tid_codi=te.tid_codi "+        
       (verSoloActivo?" and tid_activ != 0":"")+ // Solo activos
        "  and pro_codi in  (";
     
//      s += " and  (select count(*) from tipdesent where tipdesent.tid_codi = tipodesp.tid_codi "+
//          " and pro_codi in  (" ;
      for (int n=0;n<articList.size();n++)
      {
          ArrayList al=articList.get(n);
          for (int n1=0;n1<al.size();n1++)
             s+=al.get(n1)+",";
      }
//      s=s.substring(0,s.length()-1)+")) >= "+artDif;
      s=s.substring(0,s.length()-1)+") group by t.tid_codi,tid_nomb ";
      if (dt.select(s))
      {        
        Statement st=dt.getConexion().createStatement();
        ResultSet rs;
        do
        {
            boolean swExist=true;
            for (int n=0;n<articList.size();n++)
            {
              ArrayList al=articList.get(n);
              s="";
              for (int n1=0;n1<al.size();n1++)
                 s+=al.get(n1)+",";
              s=s.substring(0,s.length()-1);
              rs=st.executeQuery("SELECT * FROM tipdesent where tid_codi = "+dt.getInt("tid_codi")+
                   " and pro_codi in ("+s+")");
              if (! rs.next())
              { // No existe ese producto o equivalentes en el tipo despiece. No se insertara.
                  swExist=false;
                  break;
              }
            }
            if (swExist)
              addDatos(dt.getString("tid_codi"),dt.getString("tid_nomb"));
        } while (dt.next());
        st.close();
      }
    }
    else
    {
        dt.select(s);
        addDatos(dt);
    }
    if (isIncluirEstaticos())
    {
        if (swIncDespLibre)
            addDatos(""+MantTipDesp.LIBRE_DESPIECE,"LIBRE");
        addDatos(""+MantTipDesp.AUTO_DESPIECE,"REENVASADO");
        addDatos(""+MantTipDesp.CONGELADO_DESPIECE,"CONGELADO");      
    }    
    return true;
  }
  /**
   * Deuvelve si se incluye los tipos despiece, libres, reenvasado y congelado.
   * 
   * @return  true si se incluyen (por defecto se incluyen)
   */
  public boolean isIncluirEstaticos()
  {
      return incluirEstaticos;
  }
  /**
   * Indica  si se deben incluir los tipos despiece, libres, reenvasado y congelado.
   * @param incluirEstaticos
   * 
   */
  public void setIncluirEstaticos(boolean incluirEstaticos)
  {
      this.incluirEstaticos=incluirEstaticos;
  }
  public String getMsgError()
  {
    return msgError;
  }
  
  public void consTid()
  {
    try
    {
      if (ayuTid == null)
      {
        ayuTid = new AyuTid(EU, layPane, dt)
        {
          @Override
          public void matar()
          {
            ej_consTid(ayuTid);
          }
        };
        layPane.add(ayuTid);
        ayuTid.setLocation(25, 25);
        ayuTid.iniciarVentana();
      }

      ayuTid.setVisible(true);
      if (infFrame !=null)
      {
        infFrame.setEnabled(false);
        infFrame.setFoco(ayuTid);
      }
      ayuTid.reset();
     
    }
    catch (Exception j)
    {
      if (infFrame != null)
        infFrame.setEnabled(true);
    }
  }

  void ej_consTid(AyuTid aytid)
  {
    if (aytid.getChose())
      texto.setValorInt(aytid.getTidCodi());
  
    texto.requestFocus();
    aytid.setVisible(false);

    if (infFrame != null)
    {
      infFrame.setEnabled(true);
      infFrame.toFront();
      try
      {
        infFrame.setSelected(true);
      }
      catch (Exception k)
      {}
      infFrame.setFoco(null);
      this.requestFocus();
    }
  }
   public boolean isAdmin() {
        return ADMIN;
    }

    public void setAdmin(boolean ADMIN) {
        this.ADMIN = ADMIN;
    }
    /**
     * Ver solo activos.
     * @param versoloactivo
     * 
     */
    public void setVerSoloActivo(boolean versoloactivo)
    {
        this.verSoloActivo=versoloactivo;
    }
}
