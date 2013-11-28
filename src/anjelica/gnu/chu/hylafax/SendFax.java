
package gnu.chu.hylafax;

import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import gnu.hylafax.HylaFAXClient;
import gnu.hylafax.HylaFAXClientProtocol;
import gnu.hylafax.Job;
import gnu.inet.ftp.ServerResponseException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * <p>Título:  SendFax</p>
 * <p>Descripcion: Utilidades para enviar faxes. Clase que se encargara de
 *  hacer el trabajo sucio a la hora de enviar fasex.
 *   </p>
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class SendFax
{

    EntornoUsuario EU;
    DatosTabla dt;
    int cliCodi;

    public SendFax(EntornoUsuario eu, DatosTabla dt) {
        EU = eu;
        this.dt = dt;
    }

    public void setCliente(int cli_codi) {
        this.cliCodi = cli_codi;
    }

    /**
     *
     * @param tipoDoc
     * @param empCodi
     * @param ejeNume
     * @param serDoc
     * @param numDoc
     * @param numFax
     * @param fichAdjunto
     * @return
     * @throws java.lang.Exception
     */
    public void sendFax(String tipoDoc, int empCodi, int ejeNume,
                        String serDoc, int numDoc, String numFax, String fichAdjunto) throws Exception {
        HylaFAXClient c = new HylaFAXClient();
        FileInputStream inStr = new FileInputStream(fichAdjunto);
//   c.setDebug(false);	// enable debug messages
        String servFax = getServFax();
        if (servFax == null) {
            mensajes.mensajeAviso("Servidor de FAX no Definido");
            return;
        }

        Job job = sendfax1(c, servFax, inStr, numFax);
        long idJob=job.getId();
//

//          long idJob=11; // PARA PRUEBAS
        dt.addNew("albvefax");
        dt.setDato("cli_codi", cliCodi);
        dt.setDato("avc_ano", ejeNume);
        dt.setDato("emp_codi", empCodi);
        dt.setDato("avc_serie", serDoc);
        dt.setDato("avc_nume", numDoc);
        dt.setDato("usu_nomb", EU.usuario);
        dt.setDato("avf_numfax", numFax);
        dt.setDato("avf_jobid", idJob);
        dt.setDato("avf_estad", "-");
        dt.setDato("avf_fecha", Formatear.getFechaAct("dd-MM-yyyy"), "dd-MM-yyyy");
        dt.setDato("avf_hora", Formatear.getFechaAct("hh.mm"));
        dt.setDato("avf_contro", "S");
        dt.setDato("avf_tipdoc", tipoDoc);
      

        dt.update();
        dt.commit();

    }

    /**
     * Devuelve el servidor de fax. segun esta establecido en el fichero, config.properties.
     * 
     * @return Servidor de Fax
     */
    public static String getServFax() {
        String raiz = System.getProperty("raiz");
        if (raiz == null) {
            raiz = "gnu.chu";
        }

        ResourceBundle param = ResourceBundle.getBundle(raiz + ".config",
             Locale.getDefault());
        try {
            return param.getString("SERVFAX");
        } catch (Exception k) {
            return null;
        }
    }

    private Job sendfax1(HylaFAXClient c, String servFax, FileInputStream inStr, String numFax) throws ServerResponseException, IOException {
        c.open(servFax);
        if (c.user("anjelica")) {
            c.pass("anjelica");
        }
        c.noop(); // for the heck of it
        c.tzone(HylaFAXClientProtocol.TZONE_LOCAL);
        Job job = c.createJob(); // start a new job
        String killtime = "000259";
        int maxdials = 12;
        int maxtries = 3; // -t
        String remote_filename = c.putTemporary(inStr);
        // set job properties
        job.setFromUser(EU.usuario);
        job.setKilltime(killtime);
        job.setMaximumDials(maxdials);
        job.setMaximumTries(maxtries);
        job.setDialstring(numFax);
        job.setNotifyType(HylaFAXClientProtocol.NOTIFY_NONE);
        job.setNotifyAddress(EU.email);
        job.addDocument(remote_filename);
        c.submit(job); // submit the job to the scheduler
        inStr.close();
        c.quit();
        return job;
    }
}
