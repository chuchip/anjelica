
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