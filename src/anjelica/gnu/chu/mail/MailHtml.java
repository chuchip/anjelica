package gnu.chu.mail;

import gnu.chu.utilidades.EntornoUsuario;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
import java.util.Date;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 *
 * <p>Título: MailHtml </p>
 * <p>Descripción: Clase Generica para Enviar correos Electronicos (E-MAIL) con partes
 * HTML</p>
 * <p>Copyright: Copyright (c) 2005-2015
 * Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
public class MailHtml
{ 
  String emailCC=null;
  String from;
  String emailFrom;
  private boolean debug=false;
 
  Properties props;
  private MimeMessage msg; 
  private Session session;
  
  // Utilizado para guardar los mensajes o los attachments
  private Multipart mp = new MimeMultipart();

  /**
   *
   * /**
   * Constructor
   *
   * @param from Nombre de quien envia el Correo. 
   * @param emailFrom Direccion correo usuario que envia el correo
   *
   */
  public MailHtml(String from, String emailFrom) {
         this(false, from, emailFrom,null);
  }
  /**
   * Constructor
   *
   * @param debugon  boolean True visualiza el proceso de enviar Email
   * @param from Nombre del Usuario que envia el Correo.
   * @param  emailFrom Direccion correo usuario que envia el correo
   * @param mailHost servidor de correo.
   */
  public MailHtml(boolean debugon, String from, String emailFrom,String mailHost) {
          if (mailHost==null)
           mailHost=gnu.chu.Menu.LoginDB.getMailHost();
          
         this.from=from;
         this.emailFrom=emailFrom;
         debug=debugon;
         props=System.getProperties();
   		 props.put("mail.smtp.host", mailHost);
         session = Session.getDefaultInstance(props, null);
         session.setDebug(debugon);
         msg = new MimeMessage(session);
  }
 
  public void setFrom(String fr) {
    from=fr;
  }
  public void setEmailFrom(String emfr) {
    emailFrom=emfr;
  }

    public String getEmailCC() {
        return emailCC;
    }

    public void setEmailCC(String emailCC) {
        this.emailCC = emailCC;
    }

  
  
  /**
   * Envia un Correo Rapido a una direccion
   * @param to Direccion e-mail donde va dirigido el Correo.
   * @param subject De que va el correo.
   * @param mens  Mensaje a poner en el correo.
   * @throws Exception si hay algun error
   */
  public  void send(String to, String subject,String mens) throws Exception {
    if (from != null)
  		msg.setFrom(new InternetAddress(emailFrom,from));
	  else
	    msg.setFrom();

    msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
    msg.setSubject(subject);
    if (mens!=null)
      msg.setText(mens);
    msg.setSentDate(new Date());
//    msg.setHeader("Content-Transfer-Encoding","7bit") ;


    msg.setContent(mp);
    Transport.send(msg);
  }
  /**
   * Envia un correo con el mensaje enviado con el setText
   *
   * @param to Direccion e-mail donde va dirigido el Correo.
   * @param subject De que va el correo.
   * @throws Exception si hay algun error
   *
   */
  public void send(String to, String subject) throws Exception {
    send(to,subject,null);
  }

  /**
   * Añade un fichero ascii al EMail
   * @param mensaje String
   * @param nombFichero String
   * @throws Exception
   */
  public void setHtml(String mensaje, String nombFichero) throws Exception {
         setHtml("Nuevo Documento", mensaje, nombFichero);
  }
  /**
   * Compone el mensaje HTML
   * @param titulo String
   * @param mensaje String
   * @param nombFichero String
   * @throws Exception
   */
  public void setHtml(String titulo, String mensaje, String nombFichero) throws Exception {
         StringBuffer sb=new StringBuffer();
         sb.append("<HTML>\n" + "<HEAD>\n" + "<TITLE>\n").append(titulo).append("\n"+
    	             "</TITLE>\n"+
                   "</HEAD>\n"+
                   "<BODY>\n");
         sb.append(mensaje);
         sb.append(" </BODY>\n"+"</HTML>\n");
         setHtml(sb, nombFichero);
  }
  public void setHtml(StringBuffer sb, String nombFichero) throws Exception
  {
    MimeBodyPart b = new MimeBodyPart();
    b.setContent(sb.toString(), "text/html");

//    MimeBodyPart b = new MimeBodyPart();
    // Si el nombFichero no es nulo crea un Attachment
    if (nombFichero != null)
      b.setFileName(nombFichero);
    mp.addBodyPart(b);
  }

  public void setTexto(String mensaje,String nombFichero)  throws Exception
  {
    MimeBodyPart b = new MimeBodyPart();
    if (nombFichero != null)
      b.setFileName(nombFichero);
    b.setContent(mensaje,"text/plain");
    mp.addBodyPart(b);
  }
  public void setTexto(StringBuffer mensaje)  throws Exception {
    setTexto(mensaje.toString(),null);
  }

  /**
   * Añade un fichero ascii al EMail
   * @param mensaje InputStream
   * @param nombFichero String
   * @throws Exception
   */
  public void setInputStream(InputStream mensaje, String nombFichero) throws Exception {
         MimeBodyPart b = new MimeBodyPart(mensaje);

         b.setFileName(nombFichero);

         // a�ade el texto o fichero al cuerpo del fichero
         mp.addBodyPart(b);
  }
 
  public void enviarFichero(String to,String texto,String subject,File fichero, String nombFichero) throws MessagingException, UnsupportedEncodingException 
  {    

        if (from != null)
         msg.setFrom(new InternetAddress(emailFrom,from));
        else
         msg.setFrom();

        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
        if (emailCC!=null)
         msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(emailCC, false));
        msg.setSubject(subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(texto);
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);
        
        // Part two is attachment
        messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(fichero);
        messageBodyPart.setDataHandler(new DataHandler(source));
        
        messageBodyPart.setFileName(nombFichero);
       
//        if (nombFichero.endsWith("pdf"))
//            messageBodyPart.setContent("application/pdf");
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        msg.setContent(multipart );

        // Send message
        Transport.send(msg);
        System.out.println("Sent message successfully....");
  }
  

}
