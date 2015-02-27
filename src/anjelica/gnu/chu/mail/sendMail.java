package gnu.chu.mail;

/**
 *
 * <p>Título: sendMail </p>
 * <p>Descripción: Clase Generica para Enviar correos Electronicos (E-MAIL)</p>
 * <p>Copyright: Copyright (c) 2005
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
 * @author chuchiP
 *
 */

import javax.mail.*;
import javax.mail.internet.*;
import gnu.chu.utilidades.*;
import java.io.*;
import java.util.Properties;
import java.util.Date;

public class sendMail
{
  public static String mailer = "msgsend";
  private boolean debug=false;
  private EntornoUsuario EU;
  Properties props;
  private MimeMessage msg;
  private Session session;

  // Utilizado para guardar los mensajes o los attachments
  private Multipart mp = new MimeMultipart();

  public sendMail(boolean debugon,EntornoUsuario entUsu)
  {
    this(debugon,entUsu,null);
  }
  /**
   * Constructor
   *
   * @param debugon True visualiza el proceso de enviar Email
   * @param entUsu para recoger Datos del Usuario que envia el Email
   * @param mailHost Servidor de Correo saliente.
   */
  public sendMail(boolean debugon, EntornoUsuario entUsu, String mailHost)
  {
    try
    {
      if (mailHost==null)
        mailHost=gnu.chu.Menu.LoginDB.getMailHost();

      debug = debugon;
      EU = entUsu;
      props = System.getProperties();
      props.put("mail.smtp.host", mailHost);
      session = Session.getDefaultInstance(props, null);
      session.setDebug(debugon);
      msg = new MimeMessage(session);

    }
    catch (Exception j)
    {
      mensajes.mensajeAviso(j.getMessage());
    }
  }

  /**
   * Envia un Correo Rapido a una direccion
   * @param to Direccion e-mail donde va dirigido el Correo.
   * @param subject De que va el mensaje (Subject)
   * @param mens Mensaje a poner en el correo.
   * @throws Exception si hay algun error
   */
  public  void send(String to, String subject,String mens) throws Exception
  {
    String from=EU.usu_nomb;
    if (from != null)
      msg.setFrom(new InternetAddress(EU.email, from));
    else
      msg.setFrom();

    msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
    msg.setSubject(subject);
    msg.setText(mens);
    msg.setHeader("X-Mailer", mailer);
    msg.setSentDate(new Date());

    Transport.send(msg);
  }
  /**
   * Envia un correo con el mensaje enviado con el setText
   *
   * @param  to Direccion e-mail donde va dirigido el Correo.
   * @param subject De que va el correo.
   * @throws Exception si hay algun error
   *
   */
  public void send(String to, String subject) throws Exception
  {
    String from=EU.usu_nomb;

    if (from != null)
      msg.setFrom(new InternetAddress(EU.email, from));
    else
      msg.setFrom();

    msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
    msg.setSubject(subject);
    msg.setHeader("X-Mailer", mailer);
    msg.setSentDate(new Date());

    // asigna los mensajes o ficheros añadidos con el setText
//    msg.setContent(mp);

    // Envia el Correo
    Transport.send(msg);
  }
  /**
   * Añade Texto al Email
   * @param mensaje Mensaje a enviar
     * @throws java.lang.Exception
   */
  public void setText(String mensaje) throws Exception {
    setText(mensaje, null);
  }
  /**
   * Añade un fichero ascii al EMail
     * @param mensaje
     * @param nombFichero
     * @throws java.lang.Exception
   */
  public void setText(String mensaje, String nombFichero) throws Exception {
    MimeBodyPart b = new MimeBodyPart();
    // Si el nombFichero no es nulo crea un Attachment
    if (nombFichero != null) {
      b.setFileName(nombFichero);
      b.setText(mensaje, "us-ascii");
    } else
      b.setText(mensaje);

    // añde el texto o fichero al cuerpo del fichero
    mp.addBodyPart(b);
  }
  /**
   * Añade un fichero al correo.
   * @param f
   * @param content
   * @throws Exception 
   */
  public void addFile(File f,String content) throws Exception
  {      
    byte[] b=new byte[100];
    StringBuilder s=new StringBuilder();
    FileInputStream fIn=new FileInputStream(f);
    int nBytes;
    while ( (nBytes=fIn.read(b))>0)
    {
      s.append(new String(b, 0, nBytes));
    }
    fIn.close();
    Multipart multipart = new MimeMultipart();
    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent(s.toString(),content);
    messageBodyPart.setFileName(f.getName());
    multipart.addBodyPart(messageBodyPart);

    msg.setContent(multipart);
  }
  public void addFile(File f) throws Exception
  {
    addFile(f,"text/plain");
  }

  static public void enviaMailAviso(String msg, EntornoUsuario entUsu)
  {
    enviaMailAviso("AVISO DE APLICACION",msg,entUsu);
  }
  static public void enviaMailAviso(String subject,String msg, EntornoUsuario entUsu)
  {
    try
    {
      String dirMailAviso = gnu.chu.Menu.LoginDB.getDirMailAviso();
      if (dirMailAviso==null || dirMailAviso.trim().equals("") )
        return;
      sendMail mail = new sendMail(false, entUsu);
      mail.send(gnu.chu.Menu.LoginDB.getDirMailAviso(), "[ANJELICA] AVISO: "+subject, msg);
    }
    catch (Throwable j)
    {
      SystemOut.print(j);
    }
  }
  public static void enviaMailError(String s,escribe systemOut,EntornoUsuario EU ) {
     try
     {
       if (gnu.chu.Menu.LoginDB.getDirMailError()==null || gnu.chu.Menu.LoginDB.getDirMailError().trim().equals(""))
         return;
       sendMail mail = new sendMail(false, EU);
       mail.send(gnu.chu.Menu.LoginDB.getDirMailError(), "[ANJELICA] ERROR DE APLICACION",
                 s + "\n" + systemOut.getMessage());
     }
     catch (Throwable j)
     {
      SystemOut.print(j);
     }

   }

}
