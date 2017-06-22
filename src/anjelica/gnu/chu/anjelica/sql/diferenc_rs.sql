alter table anjelica.albrutalin add cli_nomen varchar(50) ;-- Nombre Cliente 
alter table anjelica.albrutalin add	cli_diree varchar(100) ; -- Direccion entrega
alter table anjelica.albrutalin add cli_poble varchar(50) ; -- Poblacion entrega
alter table anjelica.albrutalin add	cli_codpoe varchar(8); -- Cod. Postal Envio
drop view anjelica.v_albruta;
create or replace view v_albruta as select c.*,l.alr_orden,l.avc_id,alr_bultos,alr_palets,
alr_unid,alr_kilos,alr_horrep,alr_comrep,cli_nomen,cli_diree,cli_poble,cli_codpoe,alr_repet,
al.emp_codi,al.avc_ano,al.avc_serie,al.avc_nume,al.cli_codi,al.avc_clinom,al.avc_kilos,
al.avc_unid 
from anjelica.albrutacab as c, anjelica.albrutalin as l,anjelica.v_albavec as al 
where c.alr_nume=l.alr_nume and al.avc_id = l.avc_id;
grant select on v_albruta to public;
--
create table prodventa
(
	pve_codi varchar(15) not null, -- Referencia producto Venta
	pve_nomb varchar(50) not null, -- Nombre
	pve_cong smallint default 0 not null, -- Congelado
	pve_corte smallint default 0 not null, -- Corte 
	pve_curac smallint default 0 not null, -- Curación
	constraint ix_prodventa primary  key (pve_codi)
);
grant select on  anjelica.prodventa to public;
--alter table hisalpaco add column  acp_clasi varchar(10);
--alter table v_albcompar add column  acp_clasi varchar(10);
alter table tarifa alter pro_codart type varchar(15);
alter table v_articulo alter pro_codart type varchar(15);
--alter table anjelica.tipodesp add tid_merven smallint not null default 0 ;-- AutoMer
--
alter table desporig add cli_codi int; -- Cliente para el que se hace el despiece
alter table deorcahis add cli_codi int; -- Cliente para el que se hace el despiece
--
alter table usuarios add usu_clanum smallint not null default 0; -- Password Corta de usuario (sin
--
drop view anjelica.v_coninvent;
alter table anjelica.coninvlin add lci_numcaj smallint not null default 0;	-- Numero Caja
create view anjelica.v_coninvent as
select c.emp_codi,c.cci_codi,c.usu_nomb,cci_feccon, cam_codi,alm_codi,lci_nume,prp_ano, prp_empcod, prp_seri, prp_part, pro_codi, pro_nomb,
prp_indi,lci_peso,lci_kgsord,lci_numind,lci_regaut,lci_coment,lci_numcaj,lci_numpal,alm_codlin from coninvcab as c, coninvlin as l where
c.emp_codi=c.emp_codi
and c.cci_codi=l.cci_codi;
--
alter table empresa rename emp_empcon to emp_loclcc;
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
drop view v_cliprv;
drop view v_cliente;
alter table clientes rename cli_carte  to cli_codrut; -- Codigo cliente en rutas.
alter table clientes alter cli_codrut type varchar(5); -- Codigo cliente en rutas.
alter table cliencamb rename cli_carte to cli_codrut; -- Codigo cliente en rutas.
alter table cliencamb alter cli_codrut type varchar(5); -- Codigo cliente en rutas.
update clientes set cli_codrut='' where cli_codrut = '1';
create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
create view anjelica.v_albdepserv as select sa.*,ca.avc_fecalb,ca.avc_id,cl.cli_nomb,cl.cli_nomco
 from albvenserc as sa, v_albavec as ca,
clientes as cl
where ca.avc_ano=sa.avc_ano
and  ca.emp_codi = sa.emp_codi
and  ca.avc_serie= sa.avc_serie
and  ca.avc_nume= sa.avc_nume
and sa.cli_codi = cl.cli_codi;
grant select on anjelica.v_albdepserv to public;
create view anjelica.v_cliprv as 
select 'E' as tipo, cli_codi as codigo, cli_nomb as nombre from anjelica.clientes 
union all
select 'C' AS tipo,prv_codi as codigo, prv_nomb as nombre from anjelica.v_proveedo;
grant select on anjelica.v_cliprv to public;
--
alter table v_albavec add avc_rcaedi varchar(2);
--
drop view v_cliente;
alter table clientes alter cli_codrut type varchar(5);
alter table cliencamb alter cli_codrut type varchar(5); -- Codigo cliente en rutas.
create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
update clientes set cli_codrut = rut_codi || cli_codrut where cli_codrut!='';
--
drop view anjelica.v_albventa;
create view anjelica.v_albventa as select c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,
cli_codi,avc_clinom,avc_fecalb, usu_nomb,avc_tipfac, cli_codfa,avc_revpre,
fvc_ano,fvc_nume,c.avc_cerra,avc_impres,avc_fecemi,sbe_codi,avc_cobrad,avc_obser,avc_fecrca,
avc_basimp,avc_kilos,avc_unid,div_codi,avc_impalb,avc_impcob,avc_dtopp,avc_dtootr,avc_valora,fvc_serie,
avc_depos,avl_numlin,pro_codi,avl_numpal,pro_nomb,avl_canti,avl_prven,avl_prbase,avc_repres,
tar_preci,avl_unid,
avl_canbru,avl_fecalt,fvl_numlin,avl_fecrli,alm_codori,alm_coddes,avl_dtolin 
from v_albavel as l, v_albavec as c 
where c.emp_codi=l.emp_codi and c.avc_ano=l.avc_ano and c.avc_serie=l.avc_serie 
and c.avc_nume=l.avc_nume;
grant select on anjelica.v_albventa to public;
--
drop view v_cliente;
alter table clientes alter cli_nif type varchar(30);
create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
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
drop view v_cliente;
alter table clientes   add cli_servir smallint default 1 not null;
create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
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
  constraint ix_linvproduc primary key(cip_codi,lip_numlin)
);
create index ix_linvproduc2 on linvproduc(pro_codi,prp_ano,prp_part,prp_seri,prp_indi);
create view anjelica.v_invproduc as
select c.cip_codi,c.usu_nomb,cip_fecinv, c.cam_codi,c.alm_codi,c.tid_codi,c.cip_coment,lip_numlin,prp_ano, prp_seri, prp_part, l.pro_codi, a.pro_nomb,
prp_indi,prp_peso,l.prv_codi,prv_nomb,prp_fecsac,pai_codi,lip_fecalt from cinvproduc as c, linvproduc as l left join v_articulo as a on l.pro_codi=a.pro_codi
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
ALTER TABLE linvproduc ADD pai_codi varchar(3); -- Pais
ALTER TABLE linvproduc ADD prp_fecpro date ; -- Fecha Prod.
--
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