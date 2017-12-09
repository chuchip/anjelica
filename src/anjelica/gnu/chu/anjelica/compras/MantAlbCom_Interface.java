/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gnu.chu.anjelica.compras;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Interface para todos los tipos de Mantenimiento Albaranes de Compras
 * @author jpuente.ext
 */
public interface MantAlbCom_Interface
{  
   public void verDiferentesLotes() ;
   public void confGridDesglose() throws Exception;
   public int cambiaLinDesg0(int row) throws Exception;
   public String getLinGrDes();
   public void guardaUltValoresDesg();
   
   /**
   * 
   *  @param row int N. Linea Alb. Desglose para BD
   *  @param nLiAlDe int N. Linea Alb. Desglose para BD
   * @param nLiAlb int N. LINEA Alb. PARA BD
   * @param nInd int N. Individuo para BD
   * @throws SQLException error BD
   *
   */
   public  void guardaLinDes(int row,int nLiAlDe,int nLiAlb,int nInd) throws SQLException,NumberFormatException;
   public void cambioPrv(boolean forzarCambioPrv);
   public boolean actGridDes(int nLinAlb,int row,int nLinDes,int nInd,int nIndAnt, int nLiAlAnt) throws SQLException,java.text.ParseException;
    public void imprEtiq(String proCodi,int nLin,int nInd) throws SQLException,java.text.ParseException;
   public void copiaJtValorAnt();
   public void PADAddNew0();
   public ArrayList getDatosDesgl() throws SQLException;
}
