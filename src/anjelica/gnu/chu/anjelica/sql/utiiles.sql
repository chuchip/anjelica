
--
select 'A' as tipo, usu_nomb ,avc_nume,avc_serie,cl.cli_codi,cl.cli_nomb,min(avl_fecalt) as fecmin, max(avl_fecalt) as fecmax from v_albventa as a, v_cliente as cl
where cl.cli_codi = a.cli_codi
and avl_fecalt::date='20160817'
group by usu_nomb,avc_nume,avc_serie,cl.cli_codi,cl.cli_nomb
union all
select 'D' as tipo,c.usu_nomb,deo_codi as avc_nume,'A' as avc_serie,c.tid_codi as cli_codi, tid_nomb as cli_nomb,
 min(deo_tiempo) as fecmin, max(deo_tiempo) as fecmax from v_despiece as c, tipodesp as t
where c.tid_codi=t.tid_codi and
deo_tiempo::date='20160817'
group by c.usu_nomb,avc_nume,avc_serie,cli_codi,cli_nomb
order by 2,7
--
-- Estudios de vap
--
select 'A' as tipo, usu_nomb ,avc_nume,avc_serie,cl.cli_codi,cl.rut_codi,cl.cli_nomb,min(avl_fecalt) as fecmin, max(avl_fecalt) as fecmax from v_albventa as a, v_cliente as cl
where cl.cli_codi = a.cli_codi
and cl.sbe_codi=2
and avl_fecalt::date='20170222'
group by usu_nomb,avc_nume,avc_serie,cl.rut_codi,cl.cli_codi,cl.cli_nomb
order by 2,7


 --
 -- Sacar clientes
 --
 select cl.cli_codi,cl.cli_nomb,cl.cli_pobl,max(avc_fecalb) from clientes as cl, v_albavec as a  where cl.rep_codi='SE' and cl.ZON_codi='RA' AND cl.CLI_aCTIV='S'
and avc_fecalb>='20160901' and cl.cli_codi=a.cli_codi
group by cl.cli_codi,cl.cli_nomb,cl.cli_pobl
order by cli_pobl
--
-- Poner como produccion los articulos de produccion, que esten en stock al -- final de la semana
--
update v_despfin set def_tippro='S'
where def_tippro='N'  and def_tiempo::date between '20170306' and '20170310'
and exists (select * from stockpart as s,v_articulo as a where 
s.pro_codi= a.pro_codi and
a.sbe_codi = 14 and
v_despfin.pro_codi= s.pro_codi and
v_despfin.def_ejelot= s.eje_nume and
v_despfin.def_serlot = s.pro_serie and
v_despfin.pro_lote= s.pro_nupar and
v_despfin.pro_numind=s.pro_numind and stp_unact=1)
--order by deo_codi
--
--  Sacar comisiones de representantes en un albaran
--
select PRO_CODI,AVL_PRVEN, AVL_PROFER,TAR_PRECI from v_albavel where emp_codi=1 and avc_ano=2016 and avc_nume=11361 and avc_serie='A'
--
-- Sacar precio entrada de  produccion en unas fechas
--
select sum(deo_kilos* deo_prcost)/ sum(deo_kilos) from v_despori where deo_incval='S' and deo_fecha between '20170306' and '20170312'
---
-- Sacar comisiones represnentas con ciertos comentarios
--
select  c.cli_codi,cl.cli_nomb,avc_nume,avc_fecalb,pro_codi,pro_nomb,avl_prven,tar_preci from v_albventa as c, v_cliente as cl where avc_fecalb>='20170310' 
and c.cli_codi = cl.cli_codi and  exists(select avc_id from comision_represent as co  where 
co.avc_id= c.avc_id and (cor_coment like '%encima%' or cor_coment like '%Encima&')) order by c.cli_codi,avc_nume

--
-- Insertar en inventario control sobre inventario produccion
--
 insert into coninvlin 
select  1 as emp_codi,6393 as cci_codi,(cip_codi*100)+ lip_numlin, prp_ano,1,prp_seri,prp_part,pro_codi,'' as pro_nomb,prp_indi,prp_peso,0 as lci_kgsord,1 as lci_numind,
0 as lci_regaut,'cip_codi=12' as lci_coment,'' as lci_numpal,1 as alm_codlin,1 as lci_numcaj,'' as lci_causa from linvproduc where cip_codi>=10
