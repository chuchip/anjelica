package gnu.chu.anjelica.listados;

import java.sql.*;
import gnu.chu.sql.*;
/*
 * Utilidades para listados
 * <p>Copyright: Copyright (c) 2005-2010</p>
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @author chuchiP
 * @version 1.1
 */
public abstract class utillista
{

    public  static String getNombList(int empCodi,int codList, DatosTabla dtStat) throws SQLException
    {
      String s;
      s="SELECT * FROM listados WHERE emp_codi = "+empCodi+
          " and lis_codi = "+codList;
      if (dtStat.select(s))
        return dtStat.getString("lis_file");
      s="SELECT * FROM listados WHERE emp_codi = 0"+ // Busco para empresa General
          " and lis_codi = "+codList;
      if (dtStat.select(s))
        return dtStat.getString("lis_file");
      throw new SQLException("NO encontrado codigo Listado: "+codList+ " en Empresa: "+empCodi);
    }

    

}
