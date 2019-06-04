-- cambios del 21/5/2019
insert into anjelica.listados values(0,12, 'Listado Detalle Impuesto Alb. Ventas','lialbvers',null);
INSERT INTO LISTADOS (emp_codi,lis_codi,lis_nomb,lis_file,lis_logo) VALUES (0,13,'Listado Detalle Impuesto Fra. Ventas ','ivafrars',null);
insert into parametros values('*','permitemultiIva','Permite albaranes/fras con diferentes IVAS',0); 
create table albveniva
(
	avc_id int not null,
    avc_basimp float not null,
	avc_poriva float not null,
	avc_porreq float not null,
	avc_impiva float not null,
	avc_impreq float not null
);
create table fraveniva
(
	fvc_id int not null,
    fvc_basimp float not null,
	fvc_poriva float not null,
	fvc_porreq float not null,
	fvc_impiva float not null,
	fvc_impreq float not null
);
---
alter table tarifa alter tar_preci type float;
--
CREATE INDEX ix_mvtalm4 on anjelica.mvtosalm(mvt_tipdoc,mvt_empcod,mvt_ejedoc,mvt_serdoc,MVT_NUMDOC);
--
ALTER TABLE USUARIOS ADD usu_adlock smallint not null default 0;

insert into parametros values('cpuente','formatocosto','Formato para campos costo','---9.9999'); 
--
--
create view anjelica.v_tiporegu as
select dis_codi as tir_tipo,dis_nomb as tir_nomb  from v_discrim
where dis_tipo='Or';
grant select on anjelica.v_tiporegu to public;
-
insert into parametros values('*','solsinstock','Solucionar productos sin stock',0); 
---
alter table usuarios add usu_clanum smallint not null default 0; -- Password Corta de usuario (sin encriptar)
insert into parametros values('*','checkCodRep','Comprueba que el codigo Reparto este nulo',0);
insert into parametros values('*','checkCuenCont','Comprueba que la cuenta contable sea valida',0);
insert into parametros values('*','diasAlbVentaMod','Restrincion modificar Alb.Venta con mas de n dias',0);
insert into parametros values('*','errordiascad','Error si hay menos de n dias de cad.',-1); 

solsinstock
-- 
-- Cabecera Reservas productos para clientes
--
create table cabresprcli
(
	rpc_id serial not null, -- Numero  Reserva
	usu_nomb varchar(15) not null,
	rpc_fecha date not null, -- fecha traspaso	
	cli_codi int not null -- Cliente al que se le realiza la reserva	
);
grant  select,update,insert, delete on cabresprcli to public;
--
-- Lineas  Reservas productos para clientes
--
create table  linresprcli
(
	rpc_id int not null, -- Numero Traspaso
	rpc_numlin int not null, -- Numero Linea
	pro_codi int,			-- Producto	
	rpc_ejelot int,			-- Ejercicio Lote
	rpc_serlot char(1), 	-- Serie de Lote
	rpc_numpar int,			-- Numero de Partida (Lote)
	rpc_numind int,			--  Numero de Individuo
	rpc_numuni int,		-- Numero de Unidades
	rpc_canti float not null,  -- Kilos 
	rpc_fecalt timestamp default current_timestamp not null, -- Fecha/Hora traspaso
	 constraint ix_linresprcli primary key(rpc_id,rpc_numlin)
);
grant select,update,insert, delete  on linresprcli to public;
create view v_resprcli as 
select 	usu_nomb,
	rpc_fecha , -- fecha traspaso	
	cli_codi , -- Cliente
	l.* from cabresprcli  as c, linresprcli as l where c.rpc_id = l.rpc_id 
	order by rpc_numlin;
--
alter table v_articulo add pro_dimica smallint;
update v_articulo set pro_dimica = 14 where pro_artcon=0;
update v_Articulo set pro_dimica = 60 where pro_artcon!=0;
--
grant select on  anjelica.prodventa to public;
create table disproventa
(
	dpv_tipo int not null, -- Tipo Discriminador. 1. Codigo Corte. 2. Animal. 3 Origen. 4 Clasif.
	dpv_nume char(3) not null, -- Codigo
	dpv_nomb varchar(30) not null -- Nombre
);
create view v_tipoanimal as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=2;
grant select on anjelica.v_tipoanimal to public;
create view v_tipocorte as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=1;
grant select on anjelica.v_tipocorte to public;
create view v_clasiprod as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=4;
grant select on anjelica.v_clasiprod to public;
----
insert into parametros values('*','inchojatrans','Incluir Hoja transportista', 1);
INSERT INTO PARAMETROS VALUES('*','avisodiascad','Aviso dias Caducidad',0); -- Comprobar dias
---
-- tabla hoja trasnporte de Albaran Venta
--drop table albvenht;
create table albvenht
(
	avc_id int not null,-- Numero de Albaran	
	tra_codi int not null, -- Transportista
	avt_fectra date, -- Fecha transporte
	avt_portes char(1) not null, --  Portes (Debidos/Pagados)
	avt_kilos float not null,	 -- Kilos transportados
	avt_connom varchar(128), -- Nombre Conductor
	avt_condni varchar(25), -- Dni Conductor
	avt_matri1 varchar(25), -- Matricula 1
	avt_matri2 varchar(25), -- Matricula 2	
	constraint ix_albvenht primary key(avc_id)
);
grant all on albvenht to public;
--
ALTER TABLE stockpart DISABLE TRIGGER USER;
drop view v_stkpart;
alter table stockpart add stp_paisde  varchar(2);
create view anjelica.v_stkpart as select * from anjelica.stockpart;
grant select on anjelica.v_stkpart to public;
ALTER TABLE stockpart ENABLE TRIGGER USER;
create or replace view anjelica.v_compras as 
select c.acc_ano, c.emp_codi,c.acc_serie, c.acc_nume, c.prv_codi, c.acc_fecrec, c.fcc_ano, c.fcc_nume,c.acc_portes,c.frt_ejerc,c.frt_nume,c.acc_cerra,c.sbe_codi,
l.acl_nulin,l.pro_codi,l.pro_nomart, acl_numcaj,l.acl_Canti,l.acl_prcom,l.acl_canfac,acl_kgrec,l.acl_comen, l.acl_dtopp,l.alm_codi,
i.acp_numlin,i.acp_numind,i.acp_canti,i.acp_canind,i.acp_feccad,i.acp_fecsac,i.acp_fecpro,i.acp_nucrot,i.acp_clasi,i.acp_painac,i.sde_codi,
i.acp_paisac,i.acp_engpai,i.mat_codi,i.acp_matad,i.acp_saldes,i.acp_paisde
from anjelica.v_albacoc as c,anjelica.v_albacol as l, anjelica.v_albcompar as i
where c.acc_ano=l.acc_ano
and c.emp_codi=l.emp_codi
and c.acc_serie=l.acc_serie
and c.acc_nume=l.acc_nume
and c.acc_ano=i.acc_ano
and c.emp_codi=i.emp_codi
and c.acc_serie=i.acc_serie
and c.acc_nume=i.acc_nume
and l.acl_nulin=i.acl_nulin;
grant select on anjelica.v_compras to public;
-- stockpart
alter table v_albcompar add acp_paisde char(2);
alter table hisalpaco add acp_paisde character varying(2);
--
DROP TRIGGER albavel_UPDATE ON anjelica.v_albavel;
create trigger albavel_UPDATE AFTER UPDATE  on anjelica.v_albavel for each row   WHEN (OLD.avl_prven IS DISTINCT FROM NEW.avl_prven OR OLD.avl_prbase IS DISTINCT FROM NEW.avl_prbase ) execute procedure anjelica.fn_acpralb();
--
drop view v_despsal;
ALTER TABLE v_despfin DISABLE TRIGGER USER;
alter table v_despfin add def_blkcos smallint not null default 0; -- Bloqueado costo
CREATE OR REPLACE VIEW v_despsal AS 
 SELECT c.eje_nume,    c.deo_codi,    c.deo_numdes,    c.tid_codi,    c.deo_fecha,    c.deo_almori,    c.deo_almdes,    c.deo_ejloge,
    c.deo_seloge,    c.deo_nuloge,    c.deo_incval,    l.def_orden,    l.pro_codi,    l.def_ejelot,    l.def_emplot,
    l.def_serlot,    l.pro_lote,    l.pro_numind,    l.def_kilos,    l.def_numpie,    l.def_prcost,    l.def_unicaj,
    l.def_feccad,    l.def_preusu,    l.def_tiempo,    l.alm_codi, l.def_blkcos,   c.deo_desnue
   FROM desporig c,
    v_despfin l
  WHERE c.eje_nume = l.eje_nume AND c.deo_codi = l.deo_codi;
 ALTER TABLE v_despfin enable TRIGGER USER;
grant select on  anjelica.v_despsal to public;
---
create view v_tiempospedido as
select t.*,u.usu_nomco from tiempostarea as t left join usuarios as u on u.usu_nomb=t.usu_nomb
where t.tit_tipdoc = 'P';
grant select on v_tiempospedido  to public;
---
delete from mensajes;
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('MS','Sale del Menu %u','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('EM','Entro en Menu %u','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('CS','Creado Stock por %u Prod: %p Unid: %U Kilos: %k Lote: %l','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('MP','Matado proceso %p por %u','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('LC','Mod. Lin. Compras No encontrado %s','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V1','Modificado ALBARAN Ventas No %a con precios ya puestos por %u','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V2','BORRADO ALBARAN Ventas No %a por %u','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V3','(PDALBVE)Error al Anular stock %s','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V4','Anulada ALTA de Alb. Ventas No %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V5','Camb. Cabec. Albaran %s','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V6','Puestos precios a 0 de Prod:  %p ALBARAN %a','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('V7','Modificado Alb. YA listado. ALBARAN %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('ER','Error al Guardar Mensaje con Cod: %c','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C1','Modificado Alb. Compras No: %a con factura: %f','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C2','BORRADO ALBARAN Compras No %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C3','Modif. Fra. Compras No %f','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C4','Anulada ALTA de Alb. Compras No: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C5','Borrado Frab. Compras No: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C6','Borrado Fra. Transp. No: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C7','Modif.  Fra. Transp. No: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C8','Camb. en compras Ref. %p de ind: %i con ventas','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D1','Cancelada ALTA Despiece (PDdesp) %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D2','Cancelada ALTA Despiece (TACTIL) %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D3','Baja Despiece (PDdesp) %a','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D4','Baja Despiece (TACTIL) Lote: %a','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D5','Cerrado Despiece Descuadrado (TACTIL) Lote: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('BL','Anulado Bloqueo en tabla %t Registro %r','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('F1','Factura Ventas %f Borrada','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('F2','Factura Ventas %f Modificada','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('I1','Sobrescribiendo Inventario en Fecha  %f','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('I2','Borrando registros Inventario de Fecha  %f','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('I3','Insertado Inventario Fecha  %f.  Almacen %a ','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('I4','Regenerado Stock Inventario Fecha %f  Almacen %a Camara %c','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('I5','Mant. Inventario Fecha  %f Almacen %a Producto %p','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('R1','Anulados cobros. Condiciones: %c','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('C9','Borrado Albaran Proveedor: %a','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('CA','Modificado Albaran Proveedor: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('CB','Cambiada en compras ref. de prod: %p a prod: %x en alb: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('CC','Modificado Albaran Proveedor en periodo ya cerrado: %a','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('CD','Modificado Precio Albaran compra en periodo cerrado: %a . Producto: %p','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('ME','Enviada incidencia: %s','C');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('D6','Modificado Despiece Valorado  Lote: %a','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('A1','Stock > 1 Individuo. Almac: %a Prod: %p Indiv: %i','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('A2','Sin reg. Stock. Almac: %a Prod: %p Indiv: %i','A');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('EA','Enviado Email a Cliente %c con Albaran: %a','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('EF','Enviado Email a Cliente %c con Factura: %a','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('P1','Modificado Pedido Venta con Albaran asignado. Pedido: %p Albaran: %a','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AVL','Listado','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AVE','E-Mail','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AVF','Fax','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AV1','Forzado List.','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AV2','Anulado List.','I');
INSERT INTO mensajes (men_codi,men_nomb,men_tipo) VALUES ('AVP','Alb.Valorado','I');
---
create table registro
(
	reg_codi serial,
	reg_time timestamp not null default current_timestamp,
	usu_nomb varchar(15) not null,
	reg_numdoc int, -- Numero documento.
	men_codi varchar(3), -- Codigo mensaje
	reg_valor varchar(255)
);

alter table hisalcave rename avc_rcaedi to avc_repres; -- Representante
alter table taricli add tar_butapa int not null default 1; -- Buscar tarifa padre . 1 Si. 0 No
create table albventra
(
	avc_id int not null,-- Numero de Albaran
	avt_tipo char(1) not null, -- Tipo: 'Cajas','Bolsas','Palets','Fresco'
	avt_unid smallint not null,
	constraint ix_albvenbul primary key(avc_id,avt_tipo)
);
update clientes set cli_codrut='LO01'
CREATE OR REPLACE VIEW v_cliente AS 
 SELECT clientes.cli_codi, clientes.cli_nomb, clientes.cli_nomco, 
    clientes.cli_direc, clientes.cli_pobl, clientes.cli_codpo, 
    clientes.cli_telef, clientes.cli_fax, clientes.cli_nif, clientes.cli_percon, 
    clientes.cli_telcon, clientes.cli_nomen, clientes.cli_diree, 
    clientes.cli_poble, clientes.cli_codpoe, clientes.cli_telefe, 
    clientes.cli_faxe, clientes.emp_codi, clientes.cli_plzent, 
    clientes.tar_codi, clientes.cli_codfa, clientes.cli_tipfac, 
    clientes.fpa_codi, clientes.cli_dipa1, clientes.cli_dipa2, 
    clientes.ban_codi, clientes.cli_baofic, clientes.cli_badico, 
    clientes.cli_bacuen, clientes.cli_bareme, clientes.cli_vaccom, 
    clientes.cli_vacfin, clientes.cli_zonrep, clientes.cli_zoncre, 
    clientes.cli_activ, clientes.cli_giro, clientes.cli_libiva, 
    clientes.cli_codrut, clientes.cli_diario, clientes.cli_sefacb, 
    clientes.cli_dtopp, clientes.cli_comis, clientes.cli_dtootr, 
    clientes.cli_albval, clientes.cli_recequ, clientes.cli_agralb, 
    clientes.cli_comen, clientes.cli_riesg, clientes.pai_codi, 
    clientes.cue_codi, clientes.cli_exeiva, clientes.cli_tipiva, 
    clientes.cli_poriva, clientes.cli_tipdoc, clientes.cli_sitfac, 
    clientes.cli_orgofi, clientes.cli_coimiv, clientes.div_codi, 
    clientes.cli_pdtoco, clientes.cli_prapel, clientes.rut_codi, 
    clientes.cli_precfi, clientes.cli_fecalt, clientes.cli_feulmo, 
    clientes.cli_disc1, clientes.cli_disc2, clientes.cli_disc3, 
    clientes.cli_disc4, clientes.cli_gener, clientes.sbe_codi, 
    clientes.cli_intern, clientes.eti_codi, clientes.zon_codi, 
    clientes.rep_codi, clientes.cli_feulve, clientes.cli_feulco, 
    clientes.cli_estcon, clientes.cli_email1, clientes.cli_email2, 
    clientes.cli_horenv, clientes.cli_comenv, clientes.cli_servir, 
    clientes.cli_enalva, clientes.cli_ordrut, clientes.cli_codrut AS cli_carte, 
    clientes.cli_codrut AS cli_valor,cli_comped 
   FROM clientes;
   create view anjelica.v_albdepserv as select sa.*,ca.avc_fecalb,ca.avc_id,cl.cli_nomb,cl.cli_nomco
 from albvenserc as sa, v_albavec as ca,
clientes as cl
where ca.avc_ano=sa.avc_ano
and  ca.emp_codi = sa.emp_codi
and  ca.avc_serie= sa.avc_serie
and  ca.avc_nume= sa.avc_nume
and sa.cli_codi = cl.cli_codi;
grant select on anjelica.v_cliente to PUBLIC;
CREATE OR REPLACE view v_transventext as select * from transportista where tra_tipo='V'; 
grant select on  v_transventext to public;


create view v_subemprcliente AS
select emp_codi,sbe_codi,sbe_nomb from subempresa where sbe_tipo='C';
grant select on v_subemprcliente to public;
---
create or replace view v_albruta as select c.*,l.alr_orden,l.avc_id,alr_bultos,alr_palets,
alr_unid,alr_kilos,alr_horrep,alr_comrep,cli_nomen,cli_diree,cli_poble,cli_codpoe,alr_repet,
al.emp_codi,al.avc_ano,al.avc_serie,al.avc_nume,al.cli_codi,al.avc_clinom,al.avc_kilos,
al.avc_unid ,al.sbe_codi
from anjelica.albrutacab as c, anjelica.albrutalin as l,anjelica.v_albavec as al 
where c.alr_nume=l.alr_nume and al.avc_id = l.avc_id;
grant select on v_albruta to public;
--
alter table transportista  add tra_activ  char(1) default 'S' not null; -- Activo (S/N)
CREATE OR REPLACE VIEW v_tranpvent AS 
 SELECT 'T'::text || transportista.tra_codi AS tra_codi,
    transportista.tra_nomb, tra_activ as trp_activ
   FROM transportista
  WHERE transportista.tra_tipo = 'V'::bpchar
UNION
 SELECT usuarios.usu_nomb AS tra_codi,
    usuarios.usu_nomco AS tra_nomb,usu_activ as trp_activ
   FROM usuarios;

drop view v_stkpart;
alter table stockpart  alter stp_numpal type varchar(5);
create view anjelica.v_stkpart as select * from stockpart;
grant select on v_stkpart to public;
 --
alter table tipotari add tar_comfij float; -- Comision fija (si tiene)
alter table tipotari add tar_prmipe float; -- precio minimo permitido
--

ALTER TABLE stockpart DISABLE TRIGGER USER;
drop view v_stkpart;
alter table stockpart alter mat_codi type integer;
alter table stockpart alter mat_codi type integer;
alter table stockpart add  stp_matad  varchar(15);   -- Matadero
alter table stockpart add  stp_saldes varchar(15);		-- Sala despiece
create view anjelica.v_stkpart as select * from anjelica.stockpart;
grant select on anjelica.v_stkpart to public;
ALTER TABLE stockpart ENABLE TRIGGER USER;

alter table etiquetas add eti_activ smallint not null default -1;

drop view anjelica.v_compras;
alter table v_albcompar alter acp_clasi type varchar(20);
alter table HISALPACO alter acp_clasi type varchar(20);
alter table v_albcompar rename opcional1 to acp_matad; -- Matadero
alter table v_albcompar rename opcional2 to acp_saldes; -- Sala despiece
alter table v_albcompar alter acp_matad type varchar(15); -- Matadero
alter table v_albcompar alter acp_saldes type varchar(15); -- Matadero
alter table v_albcompar disable trigger user;

update v_albcompar set acp_matad = (select mat_nrgsa from v_matadero as m where v_albcompar.mat_codi=m.mat_codi) 
where exists (select mat_nrgsa from v_matadero as m where v_albcompar.mat_codi=m.mat_codi)
and acp_matad is null;
update v_albcompar set acp_saldes = (select sde_nrgsa from v_saladesp as m where v_albcompar.sde_codi=m.sde_codi) 
where exists (select sde_nrgsa from v_saladesp as m where v_albcompar.sde_codi=m.sde_codi) 
and acp_matad is null;
alter table v_albcompar enable trigger user;
drop view v_compras;
 create or replace view anjelica.v_compras as 
select c.acc_ano, c.emp_codi,c.acc_serie, c.acc_nume, c.prv_codi, c.acc_fecrec, c.fcc_ano, c.fcc_nume,c.acc_portes,c.frt_ejerc,c.frt_nume,c.acc_cerra,c.sbe_codi,
l.acl_nulin,l.pro_codi,l.pro_nomart, acl_numcaj,l.acl_Canti,l.acl_prcom,l.acl_canfac,acl_kgrec,l.acl_comen, l.acl_dtopp,l.alm_codi,
i.acp_numlin,i.acp_numind,i.acp_canti,i.acp_canind,i.acp_feccad,i.acp_fecsac,i.acp_fecpro,i.acp_nucrot,i.acp_clasi,i.acp_painac,i.sde_codi,i,acp_matad,i.acp_saldes,
i.acp_paisac,i.acp_engpai,i.mat_codi
from anjelica.v_albacoc as c,anjelica.v_albacol as l, anjelica.v_albcompar as i
where c.acc_ano=l.acc_ano
and c.emp_codi=l.emp_codi
and c.acc_serie=l.acc_serie
and c.acc_nume=l.acc_nume
and c.acc_ano=i.acc_ano
and c.emp_codi=i.emp_codi
and c.acc_serie=i.acc_serie
and c.acc_nume=i.acc_nume
and l.acl_nulin=i.acl_nulin;
grant select on anjelica.v_compras to public;

grant select on anjelica.v_albdepserv to public;
create view anjelica.v_cliprv as 
select 'E' as tipo, cli_codi as codigo, cli_nomb as nombre from anjelica.clientes 
union all
select 'C' AS tipo,prv_codi as codigo, prv_nomb as nombre from anjelica.v_proveedo;
grant select on anjelica.v_cliprv to public;

alter table anjelica.tipodesp add tid_asprfi smallint not null default 0; -- Asignar Producto Salida 
alter table anjelica.tipodesp add  tid_auclpe smallint not null default 0; -- Autoclasificar por peso.
alter table anjelica.tipdesent add tde_unid int not null  default 1;-- Numero Unidades
alter table anjelica.tipdessal add tds_proini int;
alter table anjelica.tipdessal add tds_pescla float ;

Alter table clientes add cli_ordrut smallint

create table anjelica.comision_represent
(
	avc_id int not null, -- Numero Albaran
	cor_linea varchar(30) not null, -- Lineas
	cor_coment varchar(120) not null, -- Comentario
	cor_comres varchar(120) not null, -- Respuesta Comentario
	constraint ix_comrep primary key (avc_id,cor_linea)
 );
grant all on anjelica.comision_represent to public;

--
create table anjelica.programasParam
(
	prf_host varchar(50), -- Host
	usu_nomb varchar(15), -- Si * aplica a todos los usuarios
    prf_id varchar(30) not null,    -- Nombre parametro    
    prf_valor varchar(15) not null --  Valor
);
grant all on anjelica.programasParam to public;
-- 
alter table taricli add tar_butapa int not null default 1; -- Buscar tarifa padre . 1 Si. 0 No

create table tiemposdoc
(
	tdo_id serial not null,			-- identificador registo
	tdo_host varchar(25) not null,	-- Nombre Host
	usu_nomb varchar(15) not null,	-- Usuario
	tdo_tipdoc char(1) not null,	-- 'P' Pedido Venta, "A" Albaran Venta,"D" Despiece, "C" Albaran Compra
	tdo_iddoc int not null,			-- Identificador documento
	tdo_inicio timestamp not null, 		-- Hora inicio
	tdo_final timestamp,				-- hora final
	tdo_coment varchar(150),			-- Comentario sobre tarea	
	constraint ix_tiemposdoc primary  key (usu_nomb,tdo_tipdoc,tdo_id)
);
grant all on anjelica.tiemposdoc to public;

alter table tipdesent add tde_grupo varchar(10);
--
-- Modificado tipo de campo grupo a productos salida de tipos de despiece. tipdesent
--
alter table tipdessal alter tds_grupo type varchar(10);

create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
alter table clientes add cli_enalva smallint not null default 0;
alter table cliencamb add cli_enalva smallint not null default 0;


alter table v_articulo add pro_encaja smallint; -- Encajado.
update v_articulo set pro_encaja=-1; 
--
alter table desproval add dpv_pretar decimal(6,2);
update desproval set dpv_pretar=0;
alter table desproval alter dpv_pretar set not null;
--
alter table mensajes alter men_codi type varchar(3);

--alter table coninvlin add lci_depos smallint not null default 0; -- Es deposito.
alter table coninvlin add lci_causa varchar(30);
create view anjelica.v_coninvent as
select c.emp_codi,c.cci_codi,c.usu_nomb,cci_feccon, cam_codi,alm_codi,lci_nume,prp_ano, prp_empcod, prp_seri, prp_part, pro_codi, pro_nomb,
prp_indi,lci_peso,lci_kgsord,lci_numind,lci_regaut,lci_coment, lci_causa,lci_numcaj,lci_numpal,l.alm_codlin,l.lci_depos from coninvcab as c, coninvlin as l where
c.emp_codi=c.emp_codi
and c.cci_codi=l.cci_codi;

grant select on  v_coninvent to public;

alter table desporig add deo_fecsac date;		-- Fecha Sacrificio
alter table deorcahis add deo_fecsac date;		-- Fecha Sacrificio
drop view v_despori;
create  view v_despori as select  1 as emp_codi, c.*, 1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from desporig as c, desorilin as l where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi;
grant select on  anjelica.v_despori to public;
drop view v_hisdespori;
create  view v_hisdespori as select  1 as emp_codi, c.*, 1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from deorcahis as c, deorlihis as l where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi and c.his_rowid=l.his_rowid;
 grant select on  anjelica.v_hisdespori to public;

drop view v_despsal;
CREATE OR REPLACE VIEW v_despsal AS 
 SELECT c.eje_nume,
    c.deo_codi,
    c.deo_numdes,
    c.tid_codi,
    c.deo_fecha,
    c.deo_almori,
    c.deo_almdes,
    c.deo_ejloge,
    c.deo_seloge,
    c.deo_nuloge,
    c.deo_incval,
    l.def_orden,
    l.pro_codi,
    l.def_ejelot,
    l.def_emplot,
    l.def_serlot,
    l.pro_lote,
    l.pro_numind,
    l.def_kilos,
    l.def_numpie,
    l.def_prcost,
    l.def_unicaj,
    l.def_feccad,
    l.def_preusu,
    l.def_tiempo,
    l.alm_codi,
    c.deo_desnue
   FROM desporig c,
    v_despfin l
  WHERE c.eje_nume = l.eje_nume AND c.deo_codi = l.deo_codi;
grant select on  anjelica.v_despsal to public;
drop view v_despiece;
create  view v_despiece as select  1 as emp_codi, c.*, g.grd_serie,g.grd_kilo,grd_unid,grd_prmeco,grd_incval,
grd_valor,grd_block,grd_feccad,g.prv_codi as grd_prvcod,grd_desnue,grd_fecpro,grd_fecha,
1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from desporig as c left join grupdesp as g on c.eje_nume=g.eje_nume and c.deo_numdes = g.grd_nume, 
 desorilin as l 
  where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi;
 grant select on  anjelica.v_despiece to public;

alter table pedicoc add tra_codi int;

alter table parametros alter par_valor  type varchar(100);
alter table parametros alter par_nomb  type varchar(30);
alter table tilialca add cam_codi varchar(2);

--
drop view anjelica.v_albventa;
create view anjelica.v_albventa as select c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,
cli_codi,avc_clinom,avc_fecalb, usu_nomb,avc_tipfac, cli_codfa,avc_revpre,
fvc_ano,fvc_nume,c.avc_cerra,avc_impres,avc_fecemi,sbe_codi,avc_cobrad,avc_obser,avc_fecrca,
avc_basimp,avc_kilos,avc_unid,div_codi,avc_impalb,avc_impcob,avc_dtopp,avc_dtootr,avc_valora,fvc_serie,
avc_depos,avl_numlin,pro_codi,avl_numpal,pro_nomb,avl_canti,avl_prven,avl_profer,avl_prbase,avc_repres,
tar_preci,avl_unid,
avl_canbru,avl_fecalt,fvl_numlin,avl_fecrli,alm_codori,alm_coddes,avl_dtolin 
from v_albavel as l, v_albavec as c 
where c.emp_codi=l.emp_codi and c.avc_ano=l.avc_ano and c.avc_serie=l.avc_serie 
and c.avc_nume=l.avc_nume;
grant select on anjelica.v_albventa to public;
--

drop view anjelica.v_albruta;
create or replace view v_albruta as select c.*,l.alr_orden,l.avc_id,alr_bultos,alr_palets,
alr_unid,alr_kilos,alr_horrep,alr_comrep,cli_nomen,cli_diree,cli_poble,cli_codpoe,alr_repet,
al.emp_codi,al.avc_ano,al.avc_serie,al.avc_nume,al.cli_codi,al.avc_clinom,al.avc_kilos,
al.avc_unid 
from anjelica.albrutacab as c, anjelica.albrutalin as l,anjelica.v_albavec as al 
where c.alr_nume=l.alr_nume and al.avc_id = l.avc_id;
grant select on v_albruta to public;
--


--alter table hisalpaco add column  acp_clasi varchar(10);
--alter table v_albcompar add column  acp_clasi varchar(10);
alter table tarifa alter pro_codart type varchar(15);
alter table v_articulo alter pro_codart type varchar(15);
--alter table anjelica.tipodesp add tid_merven smallint not null default 0 ;-- AutoMer
--
--
--

drop view anjelica.v_coninvent;
--alter table anjelica.coninvlin add lci_numcaj smallint not null default 0;	-- Numero Caja
create view anjelica.v_coninvent as
select c.emp_codi,c.cci_codi,c.usu_nomb,cci_feccon, cam_codi,alm_codi,lci_nume,prp_ano, prp_empcod, prp_seri, prp_part, pro_codi, pro_nomb,
prp_indi,lci_peso,lci_kgsord,lci_numind,lci_regaut,lci_coment,lci_numcaj,lci_numpal,alm_codlin from coninvcab as c, coninvlin as l where
c.emp_codi=c.emp_codi
and c.cci_codi=l.cci_codi;
--


update  empresa set emp_loclcc= 9;
alter table empresa alter emp_loclcc set not null;
drop view v_empresa;
CREATE OR REPLACE VIEW v_empresa AS 
 SELECT empresa.emp_codi,
    empresa.emp_nomb,
    empresa.emp_dire,
    empresa.emp_pobl,
    empresa.emp_codpo,
    empresa.emp_telef,
    empresa.emp_fax,
    empresa.emp_nif,
    empresa.emp_loclcc,
    empresa.emp_nurgsa,
    empresa.emp_nomsoc,
    empresa.pai_codi,
    empresa.emp_orgcon,
    empresa.emp_cercal,
    empresa.emp_labcal,
    empresa.emp_obsfra,
    empresa.emp_obsalb,
    empresa.emp_vetnom,
    empresa.emp_vetnum,
    empresa.emp_numexp,
    empresa.emp_codcom,
    empresa.emp_codpvi,
    empresa.emp_divimp,
    empresa.emp_divexp,
    empresa.emp_desspr,
    empresa.emp_codedi,
    empresa.emp_regmer,
    empresa.emp_dirweb,
    prov_espana.cop_nombre AS emp_nomprv
   FROM empresa
     LEFT JOIN prov_espana ON "substring"(empresa.emp_codpo::text, 1, 2) = prov_espana.cop_codi::text;
	 grant select on anjelica.v_empresa to public;
--

alter table pedvenc add rut_codi varchar(2);  
alter table hispedvenc add rut_codi varchar(2) ;
alter table pedvenc add pvc_fecpre date; 
alter table hispedvenc add pvc_fecpre date;
--
alter table  stockpart add cam_codi varchar(2);	 
--

alter table hisalpaco add opcional1  varchar(2);
alter table hisalpaco add opcional2   varchar(2);
alter table hisalpaco alter clasificacion  type varchar(2);
update hisalpaco set opcional1 = (select pai_inic from paises where pai_codi = hisalpaco.acp_paisac) where acp_paisac>0 ;
update hisalpaco set opcional2 = (select pai_inic from paises where pai_codi = hisalpaco.acp_painac) where acp_painac>0; 
update hisalpaco set clasificacion = (select pai_inic from paises where pai_codi = hisalpaco.acp_engpai) where acp_engpai>0 ;
alter table hisalpaco rename acp_paisac to acp_paisac1;
alter table hisalpaco rename acp_painac to acp_painac1;
alter table hisalpaco rename acp_engpai to acp_paieng1;
alter table hisalpaco rename opcional1 to acp_paisac;
alter table hisalpaco rename opcional2 to acp_painac;
alter table hisalpaco rename clasificacion to acp_engpai;
--

alter table v_albcompar alter opcional1  type varchar(2);
alter table v_albcompar alter opcional2  type varchar(2);
alter table v_albcompar alter clasificacion  type varchar(2);
drop trigger albcompar_update on anjelica.v_albcompar;
update v_albcompar set opcional1 = (select pai_inic from paises where pai_codi = v_albcompar.acp_paisac) where acp_paisac>0; --and acc_ano=2016;
update v_albcompar set opcional2 = (select pai_inic from paises where pai_codi = v_albcompar.acp_painac) where acp_painac>0; -- and acc_ano=2016;
update v_albcompar set clasificacion = (select pai_inic from paises where pai_codi = v_albcompar.acp_engpai) where acp_engpai>0; -- and acc_ano=2016;
alter table v_albcompar rename acp_paisac to acp_paisac1;
alter table v_albcompar rename acp_painac to acp_painac1;
alter table v_albcompar rename acp_engpai to acp_paieng1;
alter table v_albcompar rename opcional1 to acp_paisac;
alter table v_albcompar rename opcional2 to acp_painac;
alter table v_albcompar rename clasificacion to acp_engpai;
create trigger albcompar_update BEFORE UPDATE OR DELETE  on anjelica.v_albcompar for each row execute procedure anjelica.fn_mvtoalm();
--

alter table v_proveedo add prv_poloin smallint;	-- Posicion inicial Lote en cod.Barras interno prv
alter table v_proveedo add prv_polofi smallint;	-- Posicion final Lote en cod.Barras interno prv
alter table v_proveedo add prv_popein smallint;	-- Posicion inicial peso en cod.Barras interno prv
alter table v_proveedo add prv_popefi smallint;	-- Posicion final peso en cod.Barras interno prv
--

alter table  desproval add dpv_preori decimal(6,2);
update desproval set dpv_preori = 0;
alter table desproval alter dpv_preori set not null;
--

alter table v_albavec rename avc_rcaedi to avc_repres; -- Representante
update v_albavec set avc_repres = (select  rep_codi from clientes as c where c.cli_codi=v_albavec.cli_codi);
update v_albavec set avc_repres = 'MA' where avc_repres is null;
alter table v_albavec alter avc_repres  set not null; -- Representante
--




create view anjelica.v_albdepserv as select sa.*,ca.avc_fecalb,ca.avc_id,cl.cli_nomb,cl.cli_nomco
 from albvenserc as sa, v_albavec as ca,
clientes as cl
where ca.avc_ano=sa.avc_ano
and  ca.emp_codi = sa.emp_codi
and  ca.avc_serie= sa.avc_serie
and  ca.avc_nume= sa.avc_nume
and sa.cli_codi = cl.cli_codi;


--
--alter table v_albavec add avc_rcaedi varchar(2);
--

alter table clientes rename cli_carte  to cli_codrut; -- Codigo cliente en rutas.
alter table clientes alter cli_codrut type varchar(5); -- Codigo cliente en rutas.
alter table cliencamb rename cli_carte to cli_codrut; -- Codigo cliente en rutas.
alter table cliencamb alter cli_codrut type varchar(5); -- Codigo cliente en rutas.
update clientes set cli_codrut = rut_codi || cli_codrut;
drop view v_cliprv;
drop view v_cliente;
alter table clientes alter cli_nif type varchar(30);

--

alter table claslomos add prv_codi int not null default 0;
--
alter table  pedicoc add pcc_feccon date;			-- Fecha Confirmacion
alter table  pedicoc add  pcc_feclis date; -- Listado 
--

alter table  desproval add dpv_preval decimal(6,2);
update desproval set dpv_preval = 0;
alter table desproval alter dpv_preval set not null;
--
alter table clientes add cli_comped varchar(256); -- Comentarios preparación pedidos
alter table cliencamb add cli_comped varchar(256); -- Comentarios preparación pedidos
alter table clientes   add cli_servir smallint default 1 not null;
alter table cliencamb   add cli_servir smallint default 1 not null;
--
alter table v_despfin add def_tippro char(1) not null default 'N' ;
alter table desfinhis  add def_tippro char(1) not null default 'N' ;
--
alter table configuracion add cfg_tarini smallint not null default 2;
alter table tipotari add constraint  ix_tipotari unique(tar_codi);
--

alter table pedvenc add pvc_id serial;
alter table hispedvenc add pvc_id serial;
grant all on pedvenc_pvc_id_seq to public;
--

alter table v_albacoc add  acc_imcokg float not null default 0; 		-- Importe de Comisiones / Kg
alter table hisalcaco add  acc_imcokg float not null default 0; 		-- Importe de Comisiones / Kg
alter table v_albacoc add  acc_kilpor int ; 		-- Kg. de portes
alter table hisalcaco add  acc_kilpor int ;		-- Kg. de Portes.
update v_albacoc set acc_kilpor=0;
update hisalcaco set acc_kilpor=0;
--

alter table histventas add div_codi int not null default 1;
--
alter table  tarifa add tar_comrep float default 0 not null;   -- Comision Representantes
alter table  taricli add tar_comrep float default 0 not null;   -- Comision Representantes
--

create view anjelica.v_invproduc as
select c.cip_codi,c.usu_nomb,cip_fecinv, c.tid_codi,c.cam_codi,c.alm_codi,cip_coment,lip_numlin,prp_ano, prp_seri, prp_part, l.pro_codi, a.pro_nomb,
prp_indi,prp_peso,l.prv_codi,prv_nomb,prp_fecsac,prp_feccad,prp_fecpro,lip_fecalt,l.pai_codi from cinvproduc as c, linvproduc as l left join v_articulo as a on l.pro_codi=a.pro_codi
left join v_proveedo as pv on l.prv_codi = pv.prv_codi where
 c.cip_codi=l.cip_codi;
grant select on  v_invproduc to public;

--
alter table v_articulo add pro_codequ int ;
--
alter table stockpart add stp_fecpro date;	 -- Fecha Produccion.
alter table stockpart add stp_nucrot varchar(30); -- Numero Crotal
alter table stockpart add stp_painac char(2);	 -- Pais de Nacimiento
alter table stockpart add stp_engpai char(2) ;		-- Pais de engorde
alter table stockpart add stp_paisac char(2);		-- Pais de Sacrificio
alter table stockpart add stp_fecsac date;	-- Fecha Sacrificio
alter table stockpart add mat_codi int;		-- Matadero
alter table stockpart add sde_codi int;		-- Sala despiece
alter table stockpart add stp_traaut smallint default 1 not null;
--

create table anjelica.cinvproduc
(	
	cip_codi int not null,			-- Numero de Inventario
	usu_nomb varchar(15) not null,	-- Usuario q. realizo el inventario
	cip_fecinv date not null,		-- Fecha de Inventario
	cam_codi varchar(2) not null,	-- Codigo de Camara (Tabla v_camaras)
	alm_codi int not null, 			-- Almacen
	tid_codi int not null default 0,-- Tipo Despiece
	cip_coment varchar(100),			-- Comentario	
    constraint ix_cinvproduc primary key (cip_codi)
);
create index ix_cinvproduc1 on cinvproduc(cip_fecinv);
--
-- Lineas  Inventarios  piezas producción
--
-- drop table linvproduc;
create table anjelica.linvproduc
(   
    cip_codi int not null,			-- Numero de Inventario
    lip_numlin int not null,			-- Numero de Linea
    prp_ano  int not null,      		-- Ejercicio del lote
    prp_seri char(1) not null,			-- Serie del Lote
    prp_part int not null,			-- Partida
    pro_codi int not null,			-- Producto    
    prp_indi int not null,          -- Individuo de Lote	
    prp_peso decimal(6,2) not null,		-- Peso de inventario
	prv_codi int not null,			-- Prvoveedor
	prp_fecsac date not null,		-- Fecha Sacrificio
	lip_fecalt timestamp not  null default current_timestamp,
	pai_codi varchar(3),	
	prp_feccad date ,				-- Fecha Cad.
	prp_fecpro date ,				-- Fecha Produccion.
	vpc_id int,			-- Numero valoracion.
  constraint ix_linvproduc primary key(cip_codi,lip_numlin)
);
create index ix_linvproduc2 on linvproduc(pro_codi,prp_ano,prp_part,prp_seri,prp_indi);

---
alter table tipotari add tar_comrep float;				-- Comision Repres.
update tipotari set tar_comrep=0;
alter table tipotari alter tar_comrep set not null;			-- Comision Repres.
--

alter table tipotari add tar_corein float;				-- Comision Repres. Inferior
update tipotari set tar_corein=0;
alter table tipotari alter tar_corein set not null;			-- Comision Repres. Inferior
--

alter table v_matadero add pai_inic varchar(2);
alter table v_saladesp add pai_inic varchar(2);
update v_matadero set pai_inic = (select pai_inic from paises where pai_codi = v_matadero.pai_codi) where pai_codi>0; --and acc_ano=2016;
update v_saladesp set pai_inic = (select pai_inic from paises where pai_codi = v_saladesp.pai_codi) where pai_codi>0; --and acc_ano=2016;
alter table v_matadero drop pai_codi;
alter table v_saladesp  drop pai_codi;
--

alter table v_articulo add pro_kgmiun float; -- kg. Minimo Unidad.
alter table v_articulo add pro_kgmaun float; -- kg. Maximo Unidad.
alter table v_articulo add pro_cointa float; -- Costo a Incrementar en Tarifa

-- Incluida columna de dto. Linea

alter table mvtosalm add mvt_prenet float;
ALTER TABLE mvtosalm DISABLE TRIGGER USER;
update mvtosalm set mvt_prenet=mvt_prec;
update mvtosalm set mvt_prenet = mvt_prec  - mvt_prec * 
(select avc_dtopp+avc_dtocom FROM V_ALBAVEC AS V WHERE MVT_EMPCOD=EMP_CODI AND MVT_EJEDOC=AVC_ANO AND MVT_SERDOC=AVC_SERIE AND MVT_NUMDOC=AVC_NUME ) / 100
 where mvt_tipdoc='V' AND mvt_serdoc!='X' 
AND EXISTS(SELECT * FROM V_ALBAVEC AS V WHERE MVT_EMPCOD=EMP_CODI AND MVT_EJEDOC=AVC_ANO AND MVT_SERDOC=AVC_SERIE AND MVT_NUMDOC=AVC_NUME AND avc_dtopp+avc_dtocom>0);
ALTER TABLE mvtosalm enable TRIGGER USER;
-- UPDATE FN_MVTOALM y fn_actpralb ... 