package gnu.chu.anjelica.listados;
/**
 *
 * <p>Titulo: CreaEtiqMarca </p>
 * <p>Descripción: Crear etiquetas para Marcar  </p>
 * <p>Copyright: Copyright (c) 2005-2017
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
import gnu.chu.Menu.Principal;
import gnu.chu.controles.CButton;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author cpuente
 */
public class CreaEtiqMarca extends ventana  implements  JRDataSource
{
    String botonTexto="";
    int numHoja;
    String[] articulos= new String[]{"20+","VA108","7 COST","8 COST.","23+","25+","28+",
        "PIST.","LNP","LOMO P.-90","8 COST SP","8C.EXTRA",
        "BOLA +60","BOLA -60","LOMO TERN.",
        "T-BONE","108B","108B ESP."};
    
     public CreaEtiqMarca(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Crear Etiquetas Marcar");

        try {
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
          ErrorInit(e);
        }
    }

    public CreaEtiqMarca(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Crear Etiquetas Marcar");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame();

        this.setVersion("2017-01-25");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        int x=5;
        int y=5;
        ArrayList<CButton> botones=new ArrayList();
        for (String articulo : articulos)
        {
            final CButton boton = new CButton(articulo);
            boton.setBounds(x, y, 80, 24);
            boton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ponTexto(boton.getText());
                }
            });
            Pbotones.add(boton,null);
            x=x+85;
            if (x>460)
            {
                x=5;
                y+=28;
            }
        }
        this.setSize(new Dimension(550, 250));
    }
    void ponTexto(String texto)
    {
        botonTexto=texto;
        pro_nombE.setText(texto+" "+opMer.getText());
        numEtiquE.setValorInt(1);
        numEtiquE.requestFocus();
    }
    @Override
    public void iniciarVentana() throws Exception {
        cli_codiE.iniciar(dtStat,this,vl,EU);
        cli_codiE.setCampoReparto(true);
        activarEventos();
    }
    private void activarEventos()
    {
        opMer.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                   ponTexto(botonTexto);
                }
            });  
         Bimprimir.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    imprimir();
                }
            });  
    }
    void imprimir()
    {
        try
        {
            try
            {
                if (cli_codiE.getValorInt()==9999)
                {
                    cli_codiE.setTextReparto("");
                }
                else
                {
                    if (! cli_codiE.controlar())
                    {
                        msgBox("Introduzca codigo cliente");
                        return;
                    }
                }
                if (numEtiquE.isNull())
                {
                    msgBox("Introduzca numero copias");
                    return;                    
                }
            } catch (SQLException ex)
            {
                Error("Error al controlar cliente", ex);
            }
            java.util.HashMap mp = new java.util.HashMap();
            JasperReport jr = Listados.getJasperReport(EU, "etiqMarcar");
            numHoja=0;
//            mp.put("cli_codrep1",cli_codiE.getCodigoReparto());
//            mp.put("cli_codrep2",cli_codiE.getCodigoReparto());
//            mp.put("pro_nomb1",pro_nombE.getText());
//            mp.put("pro_nomb2",pro_nombE.getText());
//            mp.put("cli_nomb1",Formatear.cortar(cli_codiE.getTextNomb(),15));
//            mp.put("cli_nomb2",Formatear.cortar(cli_codiE.getTextNomb(),15));
                     
            JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
            if (EU.getSimulaPrint())
                return;
            gnu.chu.print.util.printJasper(jp, EU);
            msgBox("Etiquetas listadas");
        } catch (JRException | PrinterException ex)
        {
             Error("Error al imprimir etiqueta", ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        cli_codiL = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        Pbotones = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        numEtiquE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        Bimprimir = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        opImpNomb = new gnu.chu.controles.CCheckBox();
        opMer = new gnu.chu.controles.CComboBox();

        Pprinc.setLayout(null);

        cli_codiL.setText("Cliente");
        cli_codiL.setPreferredSize(new java.awt.Dimension(39, 18));
        Pprinc.add(cli_codiL);
        cli_codiL.setBounds(10, 10, 39, 18);
        Pprinc.add(cli_codiE);
        cli_codiE.setBounds(60, 10, 480, 18);

        Pbotones.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pbotones.setLayout(null);
        Pprinc.add(Pbotones);
        Pbotones.setBounds(10, 40, 530, 90);

        cLabel2.setText("Numero Etiquetas");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(390, 135, 110, 17);

        numEtiquE.setText("1");
        Pprinc.add(numEtiquE);
        numEtiquE.setBounds(500, 135, 40, 17);

        Bimprimir.setText("Imprimir");
        Pprinc.add(Bimprimir);
        Bimprimir.setBounds(240, 160, 90, 30);

        cLabel1.setText("Producto");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(130, 135, 60, 17);

        pro_nombE.setMayusc(true);
        Pprinc.add(pro_nombE);
        pro_nombE.setBounds(190, 135, 190, 17);

        opImpNomb.setText("Imprimir Nombre Cliente");
        opImpNomb.setToolTipText("Imprimir Nombre corto Cliente");
        Pprinc.add(opImpNomb);
        opImpNomb.setBounds(10, 160, 170, 17);

        opMer.addItem("");
        opMer.addItem("C/MER");
        opMer.addItem("S/MER");
        Pprinc.add(opMer);
        opMer.setBounds(10, 135, 110, 17);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bimprimir;
    private gnu.chu.controles.CPanel Pbotones;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CLabel cli_codiL;
    private gnu.chu.controles.CTextField numEtiquE;
    private gnu.chu.controles.CCheckBox opImpNomb;
    private gnu.chu.controles.CComboBox opMer;
    private gnu.chu.controles.CTextField pro_nombE;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean next() throws JRException {
        numHoja++;
        return numHoja<=numEtiquE.getValorInt();            
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
       
        String campo = jrf.getName().toLowerCase();
        switch (campo)
        {
          case "cli_codrep":
              return cli_codiE.getCodigoReparto();                  
          case "pro_nomb":
              return  pro_nombE.getText();
          case "cli_nomb":
              return opImpNomb.isSelected()?Formatear.cortar(cli_codiE.getTextNomb(),15):null;
        }
        return null;
    }
      
}
