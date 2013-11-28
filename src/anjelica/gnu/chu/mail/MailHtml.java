package gnu.chu.mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
import javax.mail.Address.*;
import java.util.Date;
import javax.mail.internet.*;

/**
 *
 * <p>T�tulo: MailHtml </p>
 * <p>Descripci�n: Clase Generica para Enviar correos Electronicos (E-MAIL) con partes
 * HTML</p>
 * <p>Copyright: Copyright (c) 2005
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 *
 */
public class MailHtml
{
  public static String mailhost = "mail.anjelica.es";
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
   * @param from Quien envia el Correo.
   * @param emailFrom Direccion correo usuario que envia el correo
   *
   */
  public MailHtml(String from, String emailFrom) {
         this(false, from, emailFrom);
  }
  /**
   * Constructor
   *
   * @param debugon  boolean True visualiza el proceso de enviar Email
   * @param from Usuario que envia el Correo.
   * @param  emailFrom Direccion correo usuario que envia el correo
   */
  public MailHtml(boolean debugon, String from, String emailFrom) {
         setFrom(from);
         setEmailFrom(emailFrom);
         debug=debugon;
         props=System.getProperties();
   		   props.put("mail.smtp.host", mailhost);
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
   * A�ade un fichero ascii al EMail
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
         sb.append("<HTML>\n"+
                   "<HEAD>\n"+
   	               "<TITLE>\n"+
         	         titulo + "\n"+
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
   * A�ade un fichero ascii al EMail
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
  public static void main(String[] args) {
  	String sb="<H1><font size=-1> Hola Caracola</H1>" + "\n";

      MailHtml sm= new MailHtml(true, "Angel", "aapella@virtualcom.es");
     try {
       sm.setTexto(new StringBuffer("pepe"));
       sm.setHtml(sb,null);
       sm.send("aapella@virtualcom.es","prueba de correo");
     } catch (Exception k) {
       k.printStackTrace();
     }
  }

}
