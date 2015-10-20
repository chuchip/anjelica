package gnu.chu.logs;

/*
 *<p>Titulo: EnviaMensajes </p>
 * <p>Descripción: Esta clase saca por la salida estandard los mensajes del tipo mandados
 *  como parametro  y cuya fecha sea superior a la actual menos los dias mandados como parametro</p>
 *  Recibe parametros:
 *  - Usuario
 *  - Contraseña
 *  - Tipos de Mensaje (Normalmente 'C')
 *  - Antiguedad en dias de los mensajes
 * <p>Copyright: Copyright (c) 2005-2010
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 */
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.escribe;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;


public class EnviaMensajes {
  DatosTabla dtCon1,dtStat;
  conexion ct;
  DatosTabla dtMS,dtMS1;
  conexion ctMS;
  EntornoUsuario EU;
  Statement stUp1;


 public  EnviaMensajes(String usuario, String passwd,String tipoMen, String dias) throws Exception
 {
      EU=gnu.chu.Menu.LoginDB.cargaEntornoUsu();
      EU.usu_nomb="SuperUsuario";
      EU.usuario=usuario;
      EU.password=passwd;
      EU.email="cpuente@vacunospuente.es";
      ct=new conexion(EU);
      dtCon1=new DatosTabla(ct);
      dtStat=new DatosTabla(ct);
      String s="SELECT * FROM histmens as h, mensajes as m where men_tipo='"+ tipoMen+"'"+
              " AND him_fecha >= current_date -"+dias+
              " and h.men_codi=m.men_codi "+
              " order by him_fecha desc ,him_hora desc,men_tipo desc";
      if (dtCon1.select(s))
      {
           System.out.println("----------------------------------");
           System.out.println("----- HISTORICO DE MENSAJES  -----");
           System.out.println("----------------------------------");
          do
          {
            System.out.println(" Fecha: "+dtCon1.getFecha("him_fecha","dd-MM-yy")+":"+
                  " Hora: "+dtCon1.getString("him_hora")+":"+
                  " Usuario:"+ dtCon1.getString("usu_nomb")+":"+
                  " Mensaje: "+dtCon1.getString("men_nomb")
                  );
         } while (dtCon1.next());
      }
      /**
       * Busco clientes nuevos o modificados
       */
      s="SELECT * FROM clientes where cli_fecalt >= current_date -"+dias+
              " OR cli_feulmo >= current_date -"+dias+
              " order by cli_feulmo desc ,cli_fecalt desc ";
       if (dtCon1.select(s))
      {
           System.out.println("----------------------------------");
           System.out.println("--  CLIENTES CON MODIFICACIONES --");
           System.out.println("----------------------------------");
          do
          {
            System.out.println("Cliente: "+dtCon1.getInt("cli_codi")+" - "+
                    dtCon1.getString("cli_nomb")+" Modificado en fecha: "+
                    dtCon1.getFecha("cli_feulmo","dd-MM-yy")+" Creado en Fecha: "+
                    dtCon1.getFecha("cli_fecalt","dd-MM-yy"));
          } while (dtCon1.next());
      }
      /**
       * Busco Productos nuevos o modificados
       */
      s="SELECT * FROM v_articulo where pro_fecalt >= current_date -"+dias+
              " OR pro_feulmo >= current_date -"+dias+
               " order by pro_feulmo desc ,pro_fecalt desc ";
       if (dtCon1.select(s))
      {
           System.out.println("----------------------------------");
           System.out.println("-- ARTICULOS CON MODIFICACIONES --");
           System.out.println("----------------------------------");
          do
          {
            System.out.println("Articulo: "+dtCon1.getInt("pro_codi")+" - "+
                    dtCon1.getString("pro_nomb")+" Modificado en fecha: "+
                    dtCon1.getFecha("pro_feulmo","dd-MM-yy")+" Creado en Fecha: "+
                    dtCon1.getFecha("pro_fecalt","dd-MM-yy"));
          } while (dtCon1.next());
      }
       /**
        * Busco inconsistencias en albaranes de ventas
        * 
        */
       Hashtable alb= pdalbara.checkAlbaran(Integer.parseInt(dias),dtCon1,dtStat);
       if (alb!=null)
       {
            System.out.println("----------------------------------------");
            System.out.println("-- ALBARANES VENTAS CON INCONSISTENCIAS --");
            System.out.println("------------------------------------------");
            Enumeration <String> kAlb=alb.keys();
            String albaran;
            while (kAlb.hasMoreElements())
            {
                albaran =  kAlb.nextElement();
                 System.out.println("Albaran: "+albaran+ " ERROR: "+alb.get(albaran));
            }
       }
        /**
        * Busco inconsistencias en albaranes de ventas
        * 
        */
        System.out.println("----------------------------------------");
        System.out.println("--  VENTAS MINORITAS DE PRODUCTOS DE MAYOR --");
        System.out.println("------------------------------------------");
        s="select avc_ano,avc_serie,avc_nume,avc_fecalb,"+
            " c.cli_codi,c.cli_nomb,v.pro_codi,v.pro_nomb,v.avl_canti"+
            " from v_albventa as v, v_cliente as c where c.cli_codi = v.cli_codi "+
            " and v.avc_fecalb between current_date -7 and current_date "+
            " and v.pro_codi between 10000 and 59999  "+
            " and v.avc_serie!='X' "+
            " and c.sbe_codi=2 ";
        if (dtCon1.select(s))
        {
           do 
           {
              System.out.println("----------------------------------------");
              System.out.println("--  VENTAS MINORITAS DE PRODUCTOS DE MAYOR --");
              System.out.println("------------------------------------------");
               System.out.println("Alb: "+dtCon1.getInt("avc_ano")+
                   dtCon1.getString("avc_serie")+dtCon1.getInt("avc_nume")+" De fecha: "+
                   dtCon1.getFecha("avc_fecalb","dd-MM-yyyy")+
                   " Cliente: "+dtCon1.getInt("cli_codi")+" -> "+dtCon1.getString("cli_nomb")+
                   " Articulo: "+dtCon1.getInt("pro_codi")+" -> "+dtCon1.getString("pro_nomb")+
                   " Kg:"+dtCon1.getDouble("avl_canti"));
           } while (dtCon1.select(s));
        } 
       
 }

 public static void main(String[] args)
  {
    try
    {
      if (args.length!=4)
      {
            System.err.println("Se requieren 4 parametros: Usuario, Contraseña,Tipo Mensaje (C/A/I), Dias Antiguedad");
            System.exit(1);
        }
      new EnviaMensajes(args[0],args[1],args[2],args[3]);
    }
    catch (Throwable ex)
    {
      String msgStack;
      escribe out = new escribe(System.out);
      ex.printStackTrace(out);
      msgStack = out.getMessage();

      ex.printStackTrace();
      System.exit(1);
    }

  }
}
