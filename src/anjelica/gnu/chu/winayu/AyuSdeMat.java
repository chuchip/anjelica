package gnu.chu.winayu;

import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;

/**
 *
 * <p>Titulo: AyuSdeMat</p>
 * <p>Descripcion: Pantalla de Ayuda de Salas de Despiece y Mataderos</p>
 * <p>Copyright: Copyright (c) 2005-2017
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

public class AyuSdeMat extends   ventana
{
  private char tipo;
  private boolean permisoAlta=false;
  private int prvCodi;
  boolean swAlta=false; // Alta Nuevo registro.
  boolean modoAlta=false; // En modo alta nuevo registro.
  int codiT;
  String nombT="";
  boolean consulta=false;
  final int JT_NOMB=1;
  final int JT_CODI=0;
  public AyuSdeMat(EntornoUsuario e, JLayeredPane fr, DatosTabla dt)
  {
    setTitulo("Ayuda Sala Despieces y Mataderos");
    
    EU = e;
    eje = false;
    vl=fr;
    if (dt != null)
      dtCon1 = dt;
    try
    {
      jbInit();
    }
    catch (Exception k)
    {
      ErrorInit(k);
    }
  }

  private void jbInit() throws Exception
  {
    statusBar = new StatusBar(this);
    iniciarFrame();
    
    this.setIconifiable(false);
  //  this.setResizable(false);
    this.setMaximizable(false);
    if (dtCon1 == null)
      conecta();

    initComponents();
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.setSize(new Dimension(480, 426));
    Pcabe.setDefButton(Baceptar);
    pai_codiE.iniciar(dtCon1, this, vl, EU);
    activarEventos();
  }
    /**
     * Creates new form AyuSdeMat
     */
    public AyuSdeMat() {
        initComponents();
    }
    /**
     * Establede si permite dar de alta registros. Defecto es false
     * @param permisoAlta 
     */
    public void setPermiteAlta(boolean permisoAlta)
    {
        this.permisoAlta=permisoAlta;
    }
    /**
     * Iniciar ventana
     * @param tipo  'S' Sala Despiece. 'M' Matadero
     * @param prvCodi Proveedor
     */
    public void iniciarVentana(char tipo, int prvCodi)
    {
        if (modoAlta)
        {
            Pcabe.resetTexto();
            jt.removeAllDatos();
        }
        swAlta=false;
        consulta=false;
        this.tipo=tipo;
        this.prvCodi=prvCodi;
        if (tipo=='S')
          cabeceraL.setText("Busqueda Sala Despiece");
        else
          cabeceraL.setText("Busqueda Matadero");
        
        activaAlta(false);
        nombE.requestFocusLater();
    }
    
    private void activaAlta(boolean activar)
    {
        modoAlta=activar;
        Bcancelar.setEnabled(activar);
        nrgsaE.setEnabled(activar);
        pai_codiE.setEnabled(activar);
    }
    
    void Baceptar_actionPerformed()
    {
      try
      {
          if (modoAlta)
          {
              altaRegistro();
              return;
          }
          if (nombE.getText().trim().length()<3 )
          {
              msgBox("Introduzca al menos 4 caracteres para la busqueda");
              return;
          }
          String s="select ";
          if (tipo=='S')
              s+="sde_codi as codi, sde_nomb as nomb, sde_nrgsa as nrgsa,pai_inic from v_saladesp "+
                  " where upper(sde_nomb) like '%"+nombE.getText()+"%' or upper(sde_nrgsa) like '%"+nombE.getText()+"%'";
          else
              s+="mat_codi as codi, mat_nomb as nomb, mat_nrgsa as nrgsa,pai_inic from v_matadero "+
                  " where upper(mat_nomb) like '%"+nombE.getText()+"%' or upper(mat_nrgsa) like '%"+nombE.getText()+"%'";
          
          s+=" order by nomb,nrgsa";
          if (!dtCon1.select(s))
          {
              if (permisoAlta)
              {
                int ret=mensajes.mensajeYesNo("Registro NO ENCONTRADO", "¿Dar de Alta?",this);
                if (ret==mensajes.YES)
                {
                    modoAlta=true;
                    activaAlta(true);                                   
                    nrgsaE.setText(nombE.getText());
                    pai_codiE.setText(nombE.getText().substring(0,2));
                    nrgsaE.requestFocusLater();
                    return;
                }
              }
              mensajeErr("Registro no Encontrado");
              nombE.requestFocusLater();
              return;
          }
          jt.removeAllDatos();
          do
          {
              ArrayList v=new ArrayList();
              v.add(dtCon1.getString("codi"));
              v.add(dtCon1.getString("nomb"));
              v.add(dtCon1.getString("nrgsa"));
              v.add(dtCon1.getString("pai_inic",true));
              jt.addLinea(v);
          } while (dtCon1.next());
          
          jt.requestFocusInicio();
      } catch (SQLException ex)
      {
          Logger.getLogger(AyuSdeMat.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    void altaRegistro() {
       
        String s;
        nombT = nombE.getText();
        try
        {
            if (tipo == 'S')
            { // Sala Despiece
                codiT = getSiguienteCodigo("v_saladesp", "sde_codi");
                dtCon1.addNew("v_saladesp");
                dtCon1.setDato("sde_codi", codiT);
                dtCon1.setDato("sde_nomb", nombT);
                dtCon1.setDato("sde_nrgsa", nrgsaE.getText());
                dtCon1.setDato("pai_inic", pai_codiE.getText());
                dtCon1.update();
                if (prvCodi != 0)
                {
                    s = "INSERT INTO v_prvsade VALUES (" + prvCodi + "," + codiT + ")";
                    dtCon1.executeUpdate(s);
                }
            }
            else
            { // Matadero
                codiT = getSiguienteCodigo("v_matadero", "mat_codi");
                dtCon1.addNew("v_matadero");
                dtCon1.setDato("mat_codi", codiT);
                dtCon1.setDato("mat_nomb", nombT);
                dtCon1.setDato("mat_nrgsa", nrgsaE.getText());
                dtCon1.setDato("pai_inic", pai_codiE.getText());
                dtCon1.update();
                if (prvCodi != 0)
                {
                    s = "INSERT INTO v_prvmata VALUES (" + prvCodi + "," + codiT + ")";
                    dtCon1.executeUpdate(s);
                }
            }
            dtCon1.commit();
            msgBox("Registro dado de alta con código: "+codiT);
            swAlta=true;
            consulta = true;
            matar();
        } catch (SQLException ex)
        {
            Logger.getLogger(AyuSdeMat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    int getSiguienteCodigo(String tabla, String campo) throws SQLException
    {
          int antNumero = 0;
          String s = "SELECT "+campo+" as codigo FROM "+tabla+" order by  codigo";
          if (dtCon1.select(s))
          {
              do
              {
                  if (antNumero > 0 && dtCon1.getInt("codigo") > antNumero + 1)
                      break;
                  antNumero = dtCon1.getInt("codigo");
              } while (dtCon1.next());
          }
          return antNumero + 1;
    }
    
    void activarEventos()
    {        
        Baceptar.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Baceptar_actionPerformed();
            }
        });
        Bcancelar.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                matar();
            }
        });
        jt.tableView.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (m.getClickCount() > 1)
                    Belegir_actionPerformed();
            }
        });
        jt.tableView.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER)
                    Belegir_actionPerformed();
            }
        });
    }
    void Belegir_actionPerformed() {
        if (jt.isVacio())
        {
            mensaje("No Encontrados Registros para Selecionar");
            jt.requestFocus();
            return;
        }

        // Editar Columna Activa.
        nombT = jt.getValString(JT_NOMB);
        codiT = jt.getValorInt(JT_CODI);
        consulta = true;
        matar();
    }

    public String getNombreSelecion() {
        return nombT;
    }

    public int getCodigoSelecion() {
        return codiT;
    }

    public boolean isConsulta() {
        return consulta;
    }
    /**
     * Especifica si se ha dado de alta un registro nuevo
     * @return true si es nuevo registro
     */
    public boolean isAlta()
    {
        return swAlta;
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

        Pprinc = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.Cgrid(4);
        Pcabe = new gnu.chu.controles.CPanel();
        cabeceraL = new gnu.chu.controles.CLabel();
        nombL = new gnu.chu.controles.CLabel();
        nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        nombL1 = new gnu.chu.controles.CLabel();
        nrgsaE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        nombL2 = new gnu.chu.controles.CLabel();
        pai_codiE = new gnu.chu.camposdb.PaiPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));

        Pprinc.setLayout(new java.awt.GridBagLayout());

        ArrayList cabecera = new ArrayList();
        cabecera.add("Codigo"); // 0 -- Codigo
        cabecera.add("Nombre"); //1 -- Nombre
        cabecera.add("Reg.San."); // 2 -- Num.Reg.San
        cabecera.add("Pais"); // 2 -- Num.Reg.San
        jt.setCabecera(cabecera);

        jt.setAnchoColumna(new int[]{60,283,283,30});

        jt.setAlinearColumna(new int[]{2,0,0,1});
        jt.setAjustarColumnas(true);
        jt.setNumRegCargar(100);
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(300, 101));
        jt.setMinimumSize(new java.awt.Dimension(300, 101));
        jt.setPreferredSize(new java.awt.Dimension(300, 101));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 1);
        Pprinc.add(jt, gridBagConstraints);

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(400, 85));
        Pcabe.setMinimumSize(new java.awt.Dimension(400, 85));
        Pcabe.setPreferredSize(new java.awt.Dimension(400, 85));
        Pcabe.setLayout(null);

        cabeceraL.setBackground(java.awt.Color.yellow);
        cabeceraL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cabeceraL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cabeceraL.setText("Busqueda Sala Despiece");
        cabeceraL.setOpaque(true);
        Pcabe.add(cabeceraL);
        cabeceraL.setBounds(20, 2, 350, 17);

        nombL.setText("Pais");
        Pcabe.add(nombL);
        nombL.setBounds(10, 60, 40, 15);

        nombE.setMayusc(true);
        Pcabe.add(nombE);
        nombE.setBounds(60, 20, 330, 17);

        nombL1.setText("Nombre");
        Pcabe.add(nombL1);
        nombL1.setBounds(10, 20, 50, 15);

        nrgsaE.setMayusc(true);
        Pcabe.add(nrgsaE);
        nrgsaE.setBounds(100, 40, 120, 17);

        nombL2.setText("Reg. Sanitario ");
        Pcabe.add(nombL2);
        nombL2.setBounds(10, 40, 80, 15);
        Pcabe.add(pai_codiE);
        pai_codiE.setBounds(60, 60, 230, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(290, 40, 100, 19);

        Bcancelar.setText("Cancelar");
        Pcabe.add(Bcancelar);
        Bcancelar.setBounds(290, 60, 100, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        Pprinc.add(Pcabe, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cabeceraL;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField nombE;
    private gnu.chu.controles.CLabel nombL;
    private gnu.chu.controles.CLabel nombL1;
    private gnu.chu.controles.CLabel nombL2;
    private gnu.chu.controles.CTextField nrgsaE;
    private gnu.chu.camposdb.PaiPanel pai_codiE;
    // End of variables declaration//GEN-END:variables
}
