-- Setencias para llevar acumulados correctos en Albaranes de venta.
update v_albavec set avc_kilos = (select sum( avl_canti)
from v_albavel as l,v_articulo as ar where
   v_albavec.emp_codi = l.emp_codi
              and v_albavec.avc_ano = l.avc_ano
              and v_albavec.avc_serie = l.avc_serie
              and v_albavec.avc_nume = l.avc_nume
	      and l.pro_codi= ar.pro_codi and ar.pro_tiplot='V'),
              avc_unid = (select sum( avl_unid)
from v_albavel as l,v_articulo as ar where
   v_albavec.emp_codi = l.emp_codi
              and v_albavec.avc_ano = l.avc_ano
              and v_albavec.avc_serie = l.avc_serie
              and v_albavec.avc_nume = l.avc_nume
	      and l.pro_codi= ar.pro_codi and ar.pro_tiplot='V'),
              avc_unid = )
             where avc_fecalb >= current_date -90 
              and
              exists (select * from v_albavel as l,v_articulo as ar where v_albavec.avc_nume =l.avc_nume and v_albavec.avc_serie=l.avc_serie and v_albavec.emp_codi = l.emp_codi
and v_albavec.avc_ano = l.avc_ano
and l.pro_codi= ar.pro_codi and ar.pro_tiplot='V');

update v_albavec set avc_basimp = "getBI_albven"(EMP_CODI,avc_ano,avc_serie,avc_nume)  WHERE  avc_fecalb >= current_date -90;
update v_albavec set avc_impalb = "getImp_albven"(EMP_CODI,avc_ano,avc_serie,avc_nume) WHERE  avc_fecalb >= current_date -90;

