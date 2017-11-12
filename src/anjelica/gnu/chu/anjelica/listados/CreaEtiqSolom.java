package gnu.chu.anjelica.listados;
/**
 *
 * <p>Titulo: CreaEtiqSolom </p>
 * <p>Descripción: Crear etiquetas para Marcar Solomillos </p>
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
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class CreaEtiqSolom extends ventana  implements  JRDataSource
{
    int numeroEtiq;
    String botonTexto="";
    int numHoja;
    String[] articulos= new String[]{"20+","23+","25+","28+","30+","LNP 28+","LNP 30+",   
        };
    
     public CreaEtiqSolom(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Crear Etiquetas Solomillos");
         setAcronimo("cretso");
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

    public CreaEtiqSolom(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Crear Etiquetas Solomillos");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame();

        this.setVersion("2017-11-02");
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
        pro_nombE.setText(texto);
//        numEtiquE.setValorInt(1);
        numEtiquE.requestFocus();
    }
    @Override
    public void iniciarVentana() throws Exception {        
        fechaE.setDate(Formatear.getDateAct());
        activarEventos();
    }
    private void activarEventos()
    {       
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
             
            if (etiqInicioE.isNull())
            {
                msgBox("Etiqueta Inicial debe ser superior a 0");
                etiqInicioE.setValorInt(1);
                etiqInicioE.requestFocus();
                return;                    
            }
            if (fechaE.isNull() || fechaE.getError())
            {
                msgBox("Fecha No valida");
                fechaE.setDate(Formatear.getDateAct());
                fechaE.requestFocus();
                return;                                    
            }
            if (numEtiquE.isNull())
            {
                msgBox("Introduzca numero copias");
                numEtiquE.requestFocus();
                return;                    
            }
            java.util.HashMap mp = new java.util.HashMap();
            JasperReport jr = Listados.getJasperReport(EU, "etiqSolomillos");
            numHoja=0;
            numeroEtiq=etiqInicioE.getValorInt();
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
        Pbotones = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        numEtiquE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        Bimprimir = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel3 = new gnu.chu.controles.CLabel();
        fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel4 = new gnu.chu.controles.CLabel();
        etiqInicioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");

        Pprinc.setLayout(null);

        Pbotones.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pbotones.setLayout(null);
        Pprinc.add(Pbotones);
        Pbotones.setBounds(10, 10, 530, 120);

        cLabel2.setText("Etiqueta Inicial");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(400, 132, 90, 17);

        numEtiquE.setText("1");
        Pprinc.add(numEtiquE);
        numEtiquE.setBounds(120, 152, 40, 17);

        Bimprimir.setText("Imprimir");
        Pprinc.add(Bimprimir);
        Bimprimir.setBounds(170, 152, 90, 30);

        cLabel1.setText("Fecha");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(270, 132, 40, 17);

        pro_nombE.setMayusc(true);
        Pprinc.add(pro_nombE);
        pro_nombE.setBounds(70, 132, 190, 17);

        cLabel3.setText("Producto");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(10, 132, 60, 17);
        Pprinc.add(fechaE);
        fechaE.setBounds(310, 132, 60, 17);

        cLabel4.setText("Numero Etiquetas");
        Pprinc.add(cLabel4);
        cLabel4.setBounds(10, 152, 110, 17);

        etiqInicioE.setText("1");
        Pprinc.add(etiqInicioE);
        etiqInicioE.setBounds(500, 132, 40, 17);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bimprimir;
    private gnu.chu.controles.CPanel Pbotones;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CTextField etiqInicioE;
    private gnu.chu.controles.CTextField fechaE;
    private gnu.chu.controles.CTextField numEtiquE;
    private gnu.chu.controles.CTextField pro_nombE;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean next() throws JRException {
        if (numHoja>0)
            numeroEtiq++;
        numHoja++;
        return numHoja<=numEtiquE.getValorInt();            
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
       
        try
        {
            String campo = jrf.getName().toLowerCase();
            switch (campo)
            {
                case "fecha":
                    return fechaE.getDate();
                case "pro_nomb":
                    return  pro_nombE.getText();
                case "numero":
                    return numeroEtiq;
            }
            return null;
        } catch (ParseException ex)
        {
            throw new JRException("Error al buscar Fecha: "+ex.getMessage());
        }
    }
      
}
