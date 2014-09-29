package gnu.chu.comm;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.comm.*;
import gnu.io.*;

public class cajanegra extends ventana implements SerialPortEventListener
{
  CommPortIdentifier portId;
  SerialPort serialPort;
  InputStream inputStream;
  OutputStream outputStream;

  CPanel Pprnc = new CPanel();
  CPanel cPanel1 = new CPanel();
  CLabel cLabel1 = new CLabel();
  CComboBox puertoE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CTextField velocidadE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel3 = new CLabel();
  CComboBox bitsDatosE = new CComboBox();
  CLabel cLabel4 = new CLabel();
  CComboBox bitsStopE = new CComboBox();
  CLabel cLabel5 = new CLabel();
  CComboBox paridadE = new CComboBox();
  CLabel cLabel6 = new CLabel();
  CButton BaceParam = new CButton();
  CComboBox flowControlE = new CComboBox();
  JTextArea texsalE = new JTextArea();
  JScrollPane jScrollPane1 = new JScrollPane();
  CButton Benviar = new CButton();
  CLabel cLabel7 = new CLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea texEntHexE = new JTextArea();
  CLabel cLabel8 = new CLabel();
  JScrollPane jScrollPane3 = new JScrollPane();
  JTextArea texEntStrE = new JTextArea();
  CButton Blimpiar = new CButton();
  CCheckBox inHexE = new CCheckBox();

  public cajanegra(EntornoUsuario eu, Principal p)
{
  this(eu,p,null);
}
public cajanegra(EntornoUsuario eu, Principal p,Hashtable ht)
{
  EU = eu;
  vl = p.panel1;
  jf = p;
  eje = true;

  try
  {

    setTitulo("Caja Negra ");

    if (jf.gestor.apuntar(this))
      jbInit();
    else
      setErrorInit(true);
  }
  catch (Exception e)
  {
   ErrorInit(e);
  }
}

public cajanegra(gnu.chu.anjelica.menu p, EntornoUsuario eu)
{
  this(p,eu,null);
}
public cajanegra(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
{
  EU = eu;
  vl = p.getLayeredPane();
  eje = false;

  try
  {
      setTitulo(" Caja Negra ");

    jf=null;
    jbInit();
  }
  catch (Exception e)
  {
   ErrorInit(e);
  }
}

private void jbInit() throws Exception
{
  iniciar(578, 428);
  Pprnc.setLayout(null);

  statusBar= new StatusBar(this);
    cPanel1.setBorder(BorderFactory.createEtchedBorder());
    cPanel1.setBounds(new Rectangle(4, 0, 550, 54));
    cPanel1.setLayout(null);
    cLabel1.setText("Puerto");
    cLabel1.setBounds(new Rectangle(6, 4, 38, 18));
    cLabel2.setText("Velocidad");
    cLabel2.setBounds(new Rectangle(156, 3, 63, 18));
    velocidadE.setText("9600");
    velocidadE.setBounds(new Rectangle(221, 4, 57, 18));
    cLabel3.setOpaque(false);
    cLabel3.setToolTipText("");
    cLabel3.setVerifyInputWhenFocusTarget(true);
    cLabel3.setText("Bits Datos");
    cLabel3.setBounds(new Rectangle(284, 6, 60, 15));
    cLabel4.setText("Bits Stop");
    cLabel4.setBounds(new Rectangle(417, 9, 76, 18));
    cLabel5.setText("Paridad");
    cLabel5.setBounds(new Rectangle(6, 25, 50, 18));
    cLabel6.setText("Flow Control");
    cLabel6.setBounds(new Rectangle(161, 27, 78, 18));
    BaceParam.setBounds(new Rectangle(405, 27, 113, 19));
    BaceParam.setText("Aceptar");
    Benviar.setBounds(new Rectangle(457, 133, 93, 31));
    Benviar.setText("Enviar");
    cLabel7.setBackground(Color.orange);
    cLabel7.setOpaque(true);
    cLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel7.setText("Salida (del Ordenador)");
    cLabel7.setBounds(new Rectangle(3, 55, 159, 19));

    cLabel8.setText("Entrada (del Ordenador)");
    cLabel8.setBounds(new Rectangle(4, 197, 159, 19));
    cLabel8.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel8.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel8.setOpaque(true);
    cLabel8.setBackground(Color.orange);

    Blimpiar.setBounds(new Rectangle(276, 189, 102, 27));
    Blimpiar.setText("Limpiar");
    inHexE.setText("Hexadecimal");
    inHexE.setBounds(new Rectangle(458, 85, 93, 19));
    flowControlE.setBounds(new Rectangle(236, 29, 116, 18));
    paridadE.setBounds(new Rectangle(63, 26, 85, 16));
    bitsStopE.setBounds(new Rectangle(470, 7, 71, 18));
    bitsDatosE.setBounds(new Rectangle(350, 6, 60, 18));
    puertoE.setBounds(new Rectangle(47, 3, 101, 19));
    jScrollPane3.setBounds(new Rectangle(5, 307, 447, 56));
    jScrollPane2.setBounds(new Rectangle(4, 220, 445, 80));
    jScrollPane1.setBounds(new Rectangle(3, 78, 448, 112));
    this.getContentPane().add(Pprnc,  BorderLayout.CENTER);
        this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    Pprnc.add(cPanel1, null);
    cPanel1.add(cLabel1, null);
    cPanel1.add(puertoE, null);
    cPanel1.add(cLabel2, null);
    cPanel1.add(velocidadE, null);
    cPanel1.add(cLabel3, null);
    cPanel1.add(bitsDatosE, null);
    cPanel1.add(cLabel4, null);
    cPanel1.add(bitsStopE, null);
    cPanel1.add(cLabel5, null);
    cPanel1.add(paridadE, null);
    cPanel1.add(cLabel6, null);
    cPanel1.add(BaceParam, null);
    cPanel1.add(flowControlE, null);
    Pprnc.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(texsalE, null);
    Pprnc.add(cLabel7, null);
    Pprnc.add(jScrollPane2, null);
    Pprnc.add(cLabel8, null);
    Pprnc.add(jScrollPane3, null);
    jScrollPane2.getViewport().add(texEntHexE, null);
    jScrollPane3.getViewport().add(texEntStrE, null);
    Pprnc.add(Benviar, null);
    Pprnc.add(inHexE, null);
    Pprnc.add(Blimpiar, null);
}
  public void iniciarVentana()
  {
    Enumeration en =CommPortIdentifier.getPortIdentifiers();
    String puerto;
    while (en.hasMoreElements())
    {
      portId = (CommPortIdentifier)  en.nextElement();
      puerto=portId.getName();
      puertoE.addItem(puerto,puerto);
    }
    bitsDatosE.addItem(""+SerialPort.DATABITS_5,""+SerialPort.DATABITS_5);
    bitsDatosE.addItem(""+SerialPort.DATABITS_6,""+SerialPort.DATABITS_6);
    bitsDatosE.addItem(""+SerialPort.DATABITS_7,""+SerialPort.DATABITS_7);
    bitsDatosE.addItem(""+SerialPort.DATABITS_8,""+SerialPort.DATABITS_8);
    bitsStopE.addItem("1",""+SerialPort.STOPBITS_1);
    bitsStopE.addItem("1.5",""+SerialPort.STOPBITS_1);
    bitsStopE.addItem("2",""+SerialPort.STOPBITS_2);
    paridadE.addItem("Impar",""+SerialPort.PARITY_EVEN);
    paridadE.addItem("Mark",""+SerialPort.PARITY_MARK);
    paridadE.addItem("Ninguna",""+SerialPort.PARITY_NONE);
    paridadE.addItem("Par",""+SerialPort.PARITY_ODD);
    paridadE.addItem("Espacio",""+SerialPort.PARITY_SPACE);
    flowControlE.addItem("Ninguno",""+SerialPort.FLOWCONTROL_NONE);
    flowControlE.addItem("RSTCTS_IN",""+SerialPort.FLOWCONTROL_RTSCTS_IN);
    flowControlE.addItem("RSTCTS_OUT",""+SerialPort.FLOWCONTROL_RTSCTS_OUT);
    flowControlE.addItem("RSTCTS",""+(SerialPort.FLOWCONTROL_RTSCTS_OUT | SerialPort.FLOWCONTROL_RTSCTS_OUT));
    flowControlE.addItem("XONOFF_IN",""+SerialPort.FLOWCONTROL_XONXOFF_IN);
    flowControlE.addItem("XONOFF_OUT",""+SerialPort.FLOWCONTROL_XONXOFF_OUT);
    flowControlE.addItem("XONOFF",""+(SerialPort.FLOWCONTROL_XONXOFF_OUT | SerialPort.FLOWCONTROL_XONXOFF_IN));

    activarEventos();
  }

  void activarEventos()
  {
    BaceParam.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        BaceParam_actionPerformed(e);
      }
    });
    Benviar.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Benviar_actionPerformed(e);
      }
    });
    Blimpiar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Blimpiar_actionPerformed(e);
      }
    });

  }

  void BaceParam_actionPerformed(ActionEvent e)
  {
  try
  {
    if (inputStream != null)
    {
      inputStream.close();
      outputStream.close();
      serialPort.close();
    }
    portId = CommPortIdentifier.getPortIdentifier(puertoE.getValor());
    serialPort = (SerialPort) portId.open("gnu.chu.comm.cajanegra", 2000);
    inputStream = serialPort.getInputStream();
    outputStream = serialPort.getOutputStream();
    serialPort.notifyOnDataAvailable(true);
    serialPort.setFlowControlMode(Integer.parseInt(flowControlE.getValor()));

    serialPort.setSerialPortParams(velocidadE.getValorInt(),
                                   Integer.parseInt(bitsDatosE.getValor()),
                                   Integer.parseInt(bitsStopE.getValor()),
                                   Integer.parseInt(paridadE.getValor()));

    serialPort.setFlowControlMode(Integer.parseInt(flowControlE.getValor()));
    serialPort.addEventListener(this);
  } catch (Exception k)
  {
    Error("Error al Establecer comunicacion",k);
  }
  }

  public void serialEvent(SerialPortEvent event) {
       switch(event.getEventType()) {
       case SerialPortEvent.BI:
       case SerialPortEvent.OE:
       case SerialPortEvent.FE:
       case SerialPortEvent.PE:
       case SerialPortEvent.CD:
       case SerialPortEvent.CTS:
       case SerialPortEvent.DSR:
       case SerialPortEvent.RI:
       case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
           break;
       case SerialPortEvent.DATA_AVAILABLE:
           int numBytes;
           byte[] readBuffer = new byte[20];
           try {
               while (inputStream.available() > 0) {
                   numBytes = inputStream.read(readBuffer);
                   texEntHexE.append(Formatear.StrToHex(new String(readBuffer,0,numBytes)));
                   texEntStrE.append(new String(readBuffer,0,numBytes));
               }
             } catch (IOException k)
             {
               Error("Error al escribir", k);
             }
           break;
       }
   }

  void Benviar_actionPerformed(ActionEvent e) {
    try {
      byte[] b;

      if (inHexE.isSelected())
        b=Formatear.HexToBytes(texsalE.getText());
      else
        b=texsalE.getText().getBytes();
      outputStream.write(b);
    } catch (Exception k)
    {
      Error("Error al escribir",k);
    }
  }

  void Blimpiar_actionPerformed(ActionEvent e) {
    texEntStrE.setText("");
    texEntHexE.setText("");
  }

  public void matar(boolean cerrarConexion)
  {
    try
    {
      if (inputStream != null)
      {
        inputStream.close();
        outputStream.close();
        serialPort.close();
      }
    }
    catch (Exception k)
    {
      ErrorInit(k);
    }
    super.matar(cerrarConexion);
  }

}


