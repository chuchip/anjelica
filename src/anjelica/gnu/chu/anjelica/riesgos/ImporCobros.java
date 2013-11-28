package gnu.chu.anjelica.riesgos;

/*
 *<p>Titulo: ImporCobros </p>
 * <p>Descripción: Esta clase busca en la base de datos SQLServer de Anasinf
 * por cobros realizados en el ultimo mes y actualiza las tablas de Anjelica
 * tocando la tabla  v_facvec</p>
 *
 *
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
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImporCobros {
  boolean incEmpresaBorrar=false;
  DatosTabla dtCon1;
  conexion ct;
  DatosTabla dtMS,dtMS1;
  conexion ctMS;
  EntornoUsuario EU;
  Statement stUp1;
  int nFraAct=0;
  
  public ImporCobros(String passwd) throws Exception
  {
      
      EU=gnu.chu.Menu.LoginDB.cargaEntornoUsu();
      EU.usu_nomb="SuperUsuario";
      EU.usuario="cpuente";
      EU.password=passwd;
      EU.email="cpuente@misl.es";
      ct=new conexion(EU);

      dtCon1=new DatosTabla(ct);
     
      stUp1=ct.createStatement();
      EU.usuario="anjelica";
      EU.password=passwd;
      EU.driverDB="com.microsoft.sqlserver.jdbc.SQLServerDriver";
      EU.catalog="BCONTA01";
      EU.addressDB="jdbc:sqlserver://w2003:1433";
      ctMS=new conexion(EU);
      dtMS=new DatosTabla(ctMS);

      
      dtMS.setCerrarCursor(false);
      dtMS1=new DatosTabla(ctMS);

  }
  void trataFacturas() throws SQLException
    {
      // Cojo todas las facturas pendientes de cobro.
      String s="SELECT * FROM v_facvec where fvc_cobrad = 0 ";
      if (!dtCon1.select(s))
          System.err.println("No encontradas facturas sin cobrar");
      String empresa;
      String empresaB;
      String factura;
      double impCobr;
      int fvcCobrad;
      do
      {
        fvcCobrad=0; // No pagado
        if (dtCon1.getInt("emp_codi")==1)
        {
            empresa="VP";
            empresaB="VPE";
        }
        else
        {
            empresa="VPE";
            empresaB="VP";
        }
        factura=dtCon1.getString("fvc_serie")+
                Formatear.format(dtCon1.getInt("fvc_nume"), "999999");
        s="SELECT * FROM efectos where (empresa =' "+empresa+"'"+
                (incEmpresaBorrar? "  or empresa !='"+empresaB+"'":"")+
                ") AND ANO = '"+dtCon1.getInt("fvc_ano")+"'"+
                " AND NUM_EFEC= '"+factura+"'";
        if (dtMS.select(s))
        { // Encontrados en efectos pagados parcialmente
            fvcCobrad=0; // No esta Totalmente Pagado
            impCobr=dtMS.getDouble("cob_parcial",true)+dtMS.getDouble("CTA_GTOS_DEV",true);
            actFra(fvcCobrad,impCobr);
            continue;
        }
        s="SELECT * FROM historicos_efectos where (empresa ='"+empresa+"' "+
                (incEmpresaBorrar? "  or empresa !='"+empresaB+"'":"")+
                ") and ANO = '"+dtCon1.getInt("fvc_ano")+"'"+
                " and NUM_EFEC= '"+factura+"'"+
                " AND TEST_SIT != 3";
        if (dtMS.select(s))
        { // Encontrados en efectos pagados parcialmente
            fvcCobrad=-1; //  Esta totalmente PAGADO
            impCobr=dtMS.getDouble("nominal");
            actFra(fvcCobrad,impCobr);
            continue;
        }
        // Buscando en efectos Agrupados
        s="select * from efectos_agrupados where empresa_O ='"+empresa+"' and "+
                " NUM_EFEC_O = '"+factura+"'";
        if (dtMS.select(s))
        { // Encontrados en efectos pagados parcialmente
            s="SELECT * FROM historicos_efectos where empresa ='"+dtMS.getString("empresa_D")+"'"+
                " AND  year(fec_fra) = "+dtCon1.getInt("fvc_ano")+
                " AND NUM_EFEC= '"+dtMS.getString("NUM_EFEC_D") +"'"+
                " AND TEST_SIT != 3";
            if (dtMS1.select(s))
            {
                fvcCobrad=-1; // Totalmente Pagado
                impCobr=dtCon1.getDouble("fvc_sumtot");
                actFra(fvcCobrad,impCobr);
                continue;
            }
            else
            {
                s="SELECT * FROM efectos where empresa ='"+dtMS.getString("empresa_D")+"'"+
                "  AND  year(fec_fra) = "+dtCon1.getInt("fvc_ano")+
                " AND NUM_EFEC= '"+dtMS.getString("NUM_EFEC_D") +"'";
                if (dtMS1.select(s))
                {
                 fvcCobrad=0; // No esta Totalmente Pagado
                 impCobr=dtMS1.getDouble("cob_parcial")+dtMS1.getDouble("CTA_GTOS_DEV");
                 actFra(fvcCobrad,impCobr);
                 continue;
                }
            }

        }
      } while (dtCon1.next());
      ct.commit();
      System.out.println("Facturas actualizadas: "+nFraAct);
  }

  private void actFra(int fvcCobrad,double impCobr ) throws SQLException
  {
   String s;
    if (fvcCobrad==0)
    {
       if (impCobr==dtCon1.getDouble("fvc_sumtot"))
       {
           fvcCobrad=-1;
       }
       else
       {
        if (dtCon1.getDouble("fvc_impcob")==impCobr)
           return;
       }
    }
    s="UPDATE v_facvec set fvc_cobrad = "+fvcCobrad+
              ", fvc_impcob = "+impCobr+
              " where emp_codi = "+dtCon1.getInt("emp_codi")+
              " and fvc_ano = "+dtCon1.getInt("fvc_ano")+
              " and fvc_serie ='"+dtCon1.getString("fvc_serie")+"'"+
              " and fvc_nume ="+dtCon1.getInt("fvc_nume");
      System.out.println(s);
      nFraAct++;
      stUp1.executeUpdate(s);
  }

  public void setIncluirEmpresa(boolean incEmpresa)
  {
      System.out.println("Incluir Empresa B: "+incEmpresa);
      this.incEmpresaBorrar=incEmpresa;
  }
  public static void main(String[] args)
  {
    try
    {
      if (args.length==0)
            System.err.println("Falta parametro con la contraseña");

      ImporCobros impCobros = new ImporCobros(args[0]);
      if (args.length>1)
          impCobros.setIncluirEmpresa(Boolean.valueOf(args[1]));
      impCobros.trataFacturas();
    }
    catch (Throwable ex)
    {
      Logger.getLogger(ImporCobros.class.getName()).log(Level.SEVERE, null, ex);
      System.exit(1);
    }

  }
}
