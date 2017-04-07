-- create user anjelica password 'anjelica' createdb createuser;
-- drop database anjelica; -- Borra la DB por si ya existia
-- set session authorization anjelica;
-- create database anjelica with ENCODING='LATIN1';
-- \c anjelica -- Comentar esta linea se se utiliza pgadmin3
-- 
-- Tabla donde se guardan las configuraciones del grid
--
-- drop table gridajuste;
create schema anjelica;
create table anjelica.prov_espana(
 cop_codi varchar(2) not null,
 cop_nombre varchar(30) not NULL,
 constraint ix_prov_espana primary  key (cop_codi)
);
insert into prov_espana values(1,'Álava (Vitoria)');
insert into prov_espana values(2,'Albacete');
insert into prov_espana values(3,'Alicante');
insert into prov_espana values(4,'Almería');
insert into prov_espana values(5,'Ávila');
insert into prov_espana values(6,'Badajoz');
insert into prov_espana values(7,'Baleares (Palma de Mallorca)');
insert into prov_espana values(8,'Barcelona');
insert into prov_espana values(9,'Burgos');
insert into prov_espana values(10,'Cáceres');
insert into prov_espana values(11,'Cádiz');
insert into prov_espana values(12,'Castellón');
insert into prov_espana values(13,'Ciudad Real');
insert into prov_espana values(14,'Córdoba');
insert into prov_espana values(15,'Coruña');
insert into prov_espana values(16,'Cuenca');
insert into prov_espana values(17,'Gerona');
insert into prov_espana values(18,'Granada');
insert into prov_espana values(19,'Guadalajara');
insert into prov_espana values(20,'Guipúzcoa (San Sebastián)');
insert into prov_espana values(21,'Huelva');
insert into prov_espana values(22,'Huesca');
insert into prov_espana values(23,'Jaén');
insert into prov_espana values(24,'León');
insert into prov_espana values(25,'Lérida');
insert into prov_espana values(26,'La Rioja (Logroño)');
insert into prov_espana values(27,'Lugo');
insert into prov_espana values(28,'Madrid');
insert into prov_espana values(29,'Málaga');
insert into prov_espana values(30,'Murcia');
insert into prov_espana values(31,'Navarra (Pamplona)');
insert into prov_espana values(32,'Orense');
insert into prov_espana values(33,'Asturias (Oviedo)');
insert into prov_espana values(34,'Palencia');
insert into prov_espana values(35,'Las Palmas');
insert into prov_espana values(36,'Pontevedra');
insert into prov_espana values(37,'Salamanca');
insert into prov_espana values(38,'Santa Cruz de Tenerife');
insert into prov_espana values(39,'Cantabria (Santander)');
insert into prov_espana values(40,'Segovia');
insert into prov_espana values(41,'Sevilla');
insert into prov_espana values(42,'Soria');
insert into prov_espana values(43,'Tarragona');
insert into prov_espana values(44,'Teruel');
insert into prov_espana values(45,'Toledo');
insert into prov_espana values(46,'Valencia');
insert into prov_espana values(47,'Valladolid');
insert into prov_espana values(48,'Vizcaya (Bilbao)');
insert into prov_espana values(49,'Zamora');
insert into prov_espana values(50,'Zaragoza');
insert into prov_espana values(51,'Ceuta');
insert into prov_espana values(52,'Melilla');

CREATE TABLE anjelica.gridajuste (
    emp_codi integer NOT NULL,     -- Empresa a la que pert. el usuario
    usu_nomb varchar(15) NOT NULL, -- Usuario que guarda la conf.
    gra_nomb varchar(80) NOT NULL, -- Nombre del Grid.
    grd_colum integer NOT NULL,    -- Numero de Columna
    grd_anccol integer NOT NULL,   -- Anchura de Columna
    grd_poscol integer NOT NULL    -- Posicion de la Columnma
);
--
-- Tabla Usuarios
--
-- drop table usuarios;
create table anjelica.usuarios
(
	usu_nomb varchar(15) not null, -- Login del usuario
	emp_codi int not null, -- Empresa en la que trabaja el Usuario
	eje_nume int not null, -- Ejercicio de Trabajo
	usu_nomco varchar(100) not null, -- Nombre Completo
	usu_email varchar(100),
	usu_puejpa char(1) not null, -- Puede ejecutar Programas s/menu
	usu_admdb char(1) not null, -- Es administrador de la BD
	usu_activ char(1) not null, -- Activo (S/N)
	usu_rese1 char(1), 	    -- Reservado 1
	usu_previ char(1),	    -- Previsualizar Listados (S/N)
	usu_diapri char(1),	    -- Mostrar dialogo Imprimir
	sbe_codi smallint not null, -- Subempresa. Si es 0 tendra acceso a TODAS
    usu_pass varchar(80),       -- Contraseña Usuario (en SHA-1)
    usu_look varchar(3),        -- Look And Feel Usuario 
	usu_clanum smallint not null default 0, -- Clave numerica de usuario (sin encriptar)
	constraint ix_usuarios primary  key (usu_nomb)
);
insert into  usuarios VALUES('anjelica',1,2008,'Administrador de BD','anjelica@localhost.com','S','S','S',null,'N','N',1,0);
alter table usuarios add constraint ix_passnum unique(usu_clanum);
--
-- Tablas con posiciones de los programas en la pantalla
--
-- drop table xypantalla;
create table anjelica.xypantalla
(
	emp_codi int not null,
	usu_nomb varchar(30) not null,
	xyp_prog varchar(100) not null,
	xyp_posx int not null,
	xyp_posy int not null,
	xyp_with int not null,
	xyp_heig int not null
);
--
-- Tabla de Menus
--
-- drop table menus;
create table anjelica.menus
(
	mnu_usua varchar(15) not null, -- Usuario
	mnu_acro varchar(30) not null, -- Acronimo
	mnu_padr varchar(30) not null, -- Padre
	mnu_nuli int not null,	       -- Numero Linea
	mnu_defi varchar(60) not null, -- Definicion
	mnu_tipo char(1) not null,     -- Tipo (Padre o Hijo)
	mnu_prog varchar(100) not null,-- Programa a Ejecutar
	mnu_icon varchar(50),	       -- Icono
	mnu_args varchar(100)          -- Argumentos a Pasar
);

--
-- Tabla Maestra de Articulos
--
-- drop table v_articulo;
create table anjelica.v_articulo
(
pro_codi int not null,	-- Codigo Producto
pro_nomb varchar(50),	-- Nombre de Producto
pro_nomcor varchar(30),	-- Nombre Producto Corto
fam_codi int,		-- Familia a la que pertenece
pro_univen char(1) not null default 'P',	-- Unidad de Venta (Piezas/Cajas)
pro_deunve varchar(15), -- Descripcion Unidades de Venta (NO SE USA)
sbe_codi int not null,	-- SubEmpresa
pro_codeti int,		-- Codigo de Etiqueta. (0 por defecto. -1 Sin etiqueta)
pro_numcro int not null,    -- No Crotales iguales (0, por defecto = ilimitados)
pro_diacom int,		-- Dias de Consumo
pro_offein date,	-- Oferta desde Fecha  -- NO UTILIZADO
pro_offefi date,	-- Oferta hasta fecha -- NO UTILIZADO
pro_tipiva int,		-- Tipo de IVA
pro_costkmi int,	-- Llevar Control de Stock Minimo. (-1 si, 0. No)
pro_codart varchar(12) not null, -- Codigo de Articulo Venta
pro_tarexp int, 	-- Lleva Taras Expedicion? NO UTLIZADO
pro_coinen int,		-- Control Ind. Envase -- NO UTILIZADO
pro_unicom varchar(2),  -- Unidades de Compra (Kg/Ud)
pro_feulco date,        -- Fecha Ult. Compra
pro_prvulco int, 	-- Proveedor  Ult. Compra
pro_fulcon date, 	-- Fecha Ult. Coste
pro_tiplot varchar(1) not null,	-- Tipo Lote. (C -> Comentario No Suma. c-> Coment Suma,
                                --  V-> Vendible, 'D' Desecho)
pro_coexis varchar(1) not null default 'S',  -- Control Existencias (S/N)
pro_coinst int default -1 not null,	-- Control Individuos en Stock.
emp_codi int,		-- Empresa del Producto (DEPRECATED)
pro_conmax smallint,	-- Llevar Control de Maximos
pro_almcom int,		-- Almacen Compra -- NO USADO
pro_almven int,		-- Almacen Venta -- NO USADO
pro_disc1 varchar(2),	-- Discriminador 1 (Incluir Lista)
pro_disc2 varchar(2),	-- Discriminador 2 (Otros)
pro_disc3 varchar(2),	-- Discriminador 3 (SIN USO)
pro_disc4 varchar(2),	-- Discriminador 4 (Mayor/Calle)
pro_oblfsa smallint not null,	-- Obligatorio Fecha Sacrificio (0->NO)
pro_artcon int, 	-- Articulo Congelado 0=No
pro_unicaj int default 1 not null,-- Unidades por Caja (NO USUADO)
pro_cajpal int default 0 not null,	-- Cajas por Palet
pro_kguni float default 0 not null,	-- Kilos por Unidad
pro_kgcaj float default 0 not null,	-- Kilos por Caja/Unidad
pro_stock float default 0 not null,	-- Kilos actuales (Stock)
pro_kgmin float default 0 not null,	-- Kg. Minimos en Stock
pro_kgmax float default 0 not null,	-- Kg. Maximos en Stock
pro_stkuni float default 0 not null,	-- Unidades Actuales (Stock)
pro_activ int default -1 not null, -- Activo? 0 No.
pro_cadcong int default 23 not null, -- Meses de caducidad congelado
cam_codi  char(2) not null,  -- Camara (SE BUSCA EN discriminador dis_tipo='AC')
pro_envvac int default 0 not null, -- Envasado Vacio: 0 NO
pro_cosinc float default 0 not null, -- Costo a Incrementar (por Kilo)
pro_mancos int default 0 not null, -- Mantener costo para despieces 0 NO. Si -1
pro_fecalt date not null,    -- Fecha Alta
pro_feulmo date,             -- Fecha Ultima Modificacion
usu_nomb varchar(15) not null,-- Usuario que hizo la ult. modific. o alta
cat_codi int not null default 1, -- Categoria Articulo
cal_codi int not null default 1, -- Calibres
env_codi int not null default 0, -- Tipo de Envase
pro_indtco int not null default 1, -- Incluye en dto. Comercial (0: No)
pro_codequ int,		-- Producto equivalente.
constraint ix_articulo primary key(pro_codi)
);
create index ix_articul1 on v_articulo(pro_codart);
--
-- Tabla de Categorias
--
create table anjelica.categorias_art
(
	cat_codi int not null, -- Codigo Categoria
	cat_nomb varchar(8) not null, -- Nombre Categoria
	cat_descr varchar(25) not null, -- Descripción Categoria
	constraint ix_categorias_art primary key  (cat_codi)
);
insert into anjelica.categorias_art values(1,'I','Primera');
alter table anjelica.v_articulo add constraint cat_profk
   foreign key (cat_codi) references anjelica.categorias_art(cat_codi) DEFERRABLE INITIALLY DEFERRED;
grant select on  anjelica.categorias_art to public;
--
-- Tabla de Categorias
--
create table anjelica.calibres_art
(
	cal_codi int not null, -- Codigo Categoria
	cal_nomb varchar(12) not null, -- Nombre calibre
	cal_desr varchar(25) not null, -- Descripción calibre
	constraint ix_calibres_art primary key (cal_codi)
);
insert into anjelica.calibres_art values(1,'67-82 mm','1G');
alter table anjelica.v_articulo add constraint cal_profk
   foreign key (cal_codi) references anjelica.calibres_art(cal_codi) DEFERRABLE INITIALLY DEFERRED;
grant select on  anjelica.calibres_art to public;   
--
-- Tabla  de Lotes
--
-- drop table lotes;
create table anjelica.lotes
(
    pro_codi int not null,     -- Articulo
	eje_nume int not null,	   -- Ejercicio
	pro_serie char(1) not null,-- Serie
 	pro_numpar int not null,   -- Partida
    lot_costo float not null,  -- Precio costo
    lot_feulen date not null,  -- Fecha Ult. Entrada
  constraint ix_lotes primary key(pro_codi,eje_nume ,pro_serie,pro_numpar)
);
--
-- Tabla de Clientes
---
-- drop table clientes;
create table anjelica.clientes
(
cli_codi int not null,
cli_nomb varchar(40), -- Nombre
cli_nomco varchar(50), -- Nombre Comercial
cli_direc varchar(50), -- Direccion
cli_pobl varchar(30), -- Poblaccion
cli_codpo varchar(8), -- Cod. Postal
cli_telef varchar(25), -- Telefono
cli_fax varchar(15), -- Fax
cli_nif varchar(30), -- NIF
cli_percon varchar(30), -- Persona Contacto
cli_telcon varchar(15), -- Telefono Contacto
cli_nomen varchar(50), -- Nombre Envio
cli_diree varchar(50), -- Direccion Entrega
cli_poble varchar(30), -- Poblacion Entrega
cli_codpoe varchar(8), -- Cod. Postal Envio
cli_telefe varchar(15), -- Telefono Envio
cli_faxe varchar(15),	 -- Fax de Envio
cli_horenv varchar(50), -- Horario de Reparto
cli_comenv varchar(80), -- Comentario Reparto
emp_codi int default 1 not null, -- Empresa
cli_plzent int, 	-- Plazo de Entrega
tar_codi int,		-- Tarifa
cli_codfa int not null,	-- Cod.  Cliente al que  Facturar
cli_tipfac varchar(1),	-- Tipo Facturacion
fpa_codi int,		-- Forma Pago
cli_dipa1 int,		-- Dia de Pago 1
cli_dipa2 int,		-- Dia de Pago 2
ban_codi int,		-- Codigo de Banco
cli_baofic int,		-- Oficina de Banco
cli_badico int,		-- Digito Control de Banco
cli_bacuen float,	-- Cuenta de Banco
cli_bareme int,		-- Banco para remesas NO SE UTILIZA
cli_vaccom date,	-- Inicio de Vacaciones
cli_vacfin date,	-- Final de Vacaciones
cli_zonrep varchar(2),	-- Zona/Represent.
cli_zoncre varchar(2),	-- Zona Credito
cli_activ varchar(2) default 'S' not null,	-- Activo (S/N)
cli_giro varchar(2),	-- Pago por GIRO (S/N)
cli_libiva varchar(2),	-- Libro de IVA
cli_carte varchar(2),	-- Cartera.
cli_diario varchar(2),	--  Diario Contable
cli_sefacb varchar(1),	-- Serie Fact. en Contab.
cli_dtopp decimal(5,2),	-- DTO Pronto Pago
cli_comis decimal(5,2), --  Comision
cli_dtootr decimal(5,2), -- Dto Otros.
cli_albval int,		 -- Listar Albaranes Valorados (0 por Prog. 1 Si,2 No)
cli_recequ smallint,	-- Recargo Equivalencia
cli_agralb int,		-- Agrupar Albaranes
cli_comen varchar(255),	-- Comentario
cli_riesg float,	-- Riesgo Asignado a Cliente
pai_codi int not null ,		-- Pais del Cliente
cue_codi varchar(12),	-- Cuenta Contable
cli_exeiva smallint,	-- Exento de IVA
cli_tipiva VARCHAR(2), -- Tipo de IVA
cli_poriva varchar(2), -- Porcentaje de IVA
cli_tipdoc varchar(2), -- Tipo de Documento
cli_sitfac varchar(2), -- Situacion de Facturas
cli_orgofi smallint, 	-- Organismo Oficial
cli_coimiv varchar(2), -- Codigo Impuesto de IVA ??
div_codi   int, 	-- Divisa
cli_pdtoco float, -- % Dto Comercial.
cli_prapel float, -- % Rappel
rut_codi varchar(2), -- Ruta del cliente
cli_precfi int default 0 not null, --  Poner precio Final por representante.
cli_fecalt date not null default current_date,	-- Fecha Alta del Cliente
cli_feulmo date, 		-- Fecha Ult. Modificacion
cli_disc1 char(2), -- Discriminador1
cli_disc2 char(2), -- Discriminador 2
cli_disc3 char(2), -- Discriminador 3
cli_disc4 char(2), -- Discriminador 4
cli_gener smallint not null default 0, -- Cliente Generico (0 = NO)
sbe_codi smallint not null default 1,	-- Subempresa
cli_intern smallint not null default 0,	-- Cliente Interno
eti_codi smallint not null default 0,	-- Etiqueta Pers. Alb.Venta (0=ninguna)
zon_codi char(2),       -- Zona
rep_codi char(2),       -- Representante
cli_feulve date,        -- Fecha Ult. Venta
cli_feulco date,        -- Fecha Ult. Contacto
cli_estcon char(1) default 'N',     -- Estado de Contacto (No contacto,Ausente,Contacto, Llamar)
cli_email1 char(60),     -- Correo Electronico Comercial (Tarifas)
cli_email2 char(60),     -- Correo Electronico Administr. (Facturas/Alb.)
cli_servir smallint default -1 not null, --  Puede servir?. 0 NO . 1 Si. 2: No! (no podra cargar pedidos ni alb.)
constraint ix_vcliente primary key(cli_codi)
);
create or replace view anjelica.v_cliente as select *,cli_codrut as cli_carte,cli_codrut as cli_valor from anjelica.clientes;
grant select on anjelica.v_cliente to PUBLIC;
--
-- Tabla de Cambios en Tabla Clientes
---
drop table cliencamb;
create table anjelica.cliencamb
(
cli_codi int not null,
cli_nomb varchar(40), -- Nombre
cli_nomco varchar(50), -- Nombre Comercial
cli_direc varchar(50), -- Direccion
cli_pobl varchar(30), -- Poblaccion
cli_codpo varchar(8), -- Cod. Postal
cli_telef varchar(25), -- Telefono
cli_fax varchar(15), -- Fax
cli_nif varchar(15), -- NIF
cli_percon varchar(30), -- Persona Contacto
cli_telcon varchar(15), -- Telefono Contacto
cli_nomen varchar(50), -- Nombre Envio
cli_diree varchar(50), -- Direccion Entrega
cli_poble varchar(30), -- Poblacion Entrega
cli_codpoe varchar(8), -- Cod. Postal Envio
cli_telefe varchar(15), -- Telefono Envio
cli_Faxe varchar(15), -- Fax de Envio
cli_horenv varchar(50), -- Horario de Envio
cli_comenv varchar(80), -- Comentario Envio
emp_codi int, -- Empresa
cli_plzent int, -- Plazo de Entrega
tar_codi int, -- Tarifa
cli_codfa int, -- Cod.  Cliente al que  Facturar
cli_tipfac varchar(1), -- Tipo Facturacion
fpa_codi int, -- Forma Pago
cli_dipa1 int, -- Dia de Pago 1
cli_dipa2 int, -- Dia de Pago 2
ban_codi int, -- Codigo de Banco
cli_baofic int, -- Oficiona de Banco
cli_badico int, -- Digito Control de Banco
cli_bacuen float, -- Cuenta de Banco
cli_bareme int, -- Banco para remesas
cli_vaccom date, -- Inicio de Vacaciones
cli_vacfin date, -- Final de Vacaciones
cli_zonrep varchar(2), -- Zona/Represent.
cli_zoncre varchar(2), -- Zona Credito
cli_activ varchar(2), -- Activo (S/N)
cli_giro varchar(2), -- Pago por GIRO (S/N)
cli_libiva varchar(2), -- Libro de IVA
cli_carte varchar(2), -- Cartera.
cli_diario varchar(2), --  Diario Contable
cli_sefacb varchar(1), -- Serie Fact. en Contab.
cli_dtopp decimal(5,2), -- DTO Pronto Pago
cli_comis decimal(5,2), --  Comision
cli_dtootr decimal(5,2), -- Dto Otros.
cli_albval int, -- Listar Albaranes Valorados (0 por Prog. 1 Si,2 No)
cli_recequ smallint, -- Recargo Equivalencia
cli_agralb int, -- Agrupar Albaranes
cli_comen varchar(255), -- Comentario
cli_riesg float, -- Riesgo Asignado a Cliente
pai_codi int,	 -- Pais del Cliente
cue_codi varchar(12), -- Cuenta Contable
cli_exeiva smallint, -- Exento de IVA
cli_tipiva VARCHAR(2), -- Tipo de IVA
cli_poriva varchar(2), -- Porcentaje de IVA
cli_tipdoc varchar(2), -- Tipo de Documento
cli_sitfac varchar(2), -- Situacion de Facturas
cli_orgofi smallint, -- Organismo Oficial ??
cli_coimiv varchar(2), -- Codigo Impuesto de IVA ??
div_codi   int, 	-- Divisa
cli_pdtoco float, -- % Dto Comercial.
cli_prapel float, -- % Rappel
rut_codi varchar(2), -- ruta
cli_precfi float, --  Indica si se deben Revisar Precios (0: No)
cli_fecalt date,	-- Fecha Alta del Cliente
cli_feulmo date, 		-- Fecha Ult. Modificacion
cli_disc1 char(2), -- Discriminador1
cli_disc2 char(2), -- Discriminador 2
cli_disc3 char(2), -- Discriminador 3
cli_disc4 char(2), -- Discriminador 4
cli_gener smallint not null,-- Cliente Generico (0 = NO)
sbe_codi smallint , -- SubEmpresa
cli_intern smallint not null,	-- Cliente Interno
eti_codi smallint not null,	-- Etiqueta Personalizada Alb.Venta
zon_codi char(2),       -- Zona
rep_codi char(2),       -- Representante
cli_feulve date,        -- Fecha Ult. Venta
cli_feulco date,        -- Fecha Ult. Contacto
cli_estcon char(1) default 'N',     -- Estado de Contacto (No contacto,Ausente,Contacto, Llamar)
cli_email1 char(60),     -- Correo Electronico Comercial (Tarifas)
cli_email2 char(60),     -- Correo Electronico Administr. (Facturas/Alb.)
usu_nom varchar(15) not null, -- Usuario que realiza el Cambio
clc_fecha date not null, -- Fecha de Cambio
clc_hora decimal(5,2) not null, -- Hora de Cambio
clc_comen varchar(100) -- Comentario sobre el Cambio
cli_servir smallint default -1 not null, --  Puede servir?. 0 NO 
);
create index ix_cliencamb on cliencamb(cli_codi,clc_fecha,clc_hora);
--
-- Tabla Cabeceras de Albaranes de Ventas
--
-- drop table v_albavec;
create table anjelica.v_albavec
(
avc_id serial not null,
avc_ano int not null,
emp_codi int not null,
avc_serie char(1) not null,
avc_nume int not null,
cli_codi int not null,
avc_clinom varchar(40),	 -- Nombre de Cliente
avc_cofra int,
avc_fecalb date,	 -- Fecha de Albaran
usu_nomb varchar(20),
avc_tipfac varchar(1),
cli_ruta varchar(2) not null,		-- Ruta de Cliente
cli_codfa int,		-- Cliente de la Factura
fvc_ano int,		-- Año de La Factua
fvc_serie char(1),      -- Serie de Fra.
fvc_nume int,		-- Numero de La Factura
avc_cerra smallint,	-- Si no esta cerrado NO se facturara y se sumara al stock
avc_impres smallint, 	-- Albaran Impreso. (0 = No.  bit 1 = Impreso. bit 2 = Modif. Cant. 4 Modif. Precios)
fvc_trasp smallint,
avc_fecemi date,
-- avc_dtosbi smallint,	-- Dto s/Base Imponible (Si/No) NO SE USA
sbe_codi smallint,	-- SubEmpresa
avc_desrec varchar(15),
alm_codori int,		-- Almacen Origen (Para Alb. de Traspaso)
alm_coddes int,		-- Almacen Destino (Para Alb. de Traspaso)
avc_confo smallint,	-- Conforme (0 NO se puede Facturar -1  SI se puede facturar )
avc_preiva smallint,
avc_cobrad smallint,	-- Cobrado (S -1/No 0)
avc_obser varchar(255), -- Observaciones
avc_fecrca date,
avc_almori int,		-- Almacen Origen (de donde se saca el stock)
avc_tarimp char(1),	-- Tarjeton IMPRESO (NO USADO)
avc_aux1 varchar(50),
avc_aux2 varchar(50),
avc_aux3 varchar(50),
avc_impre2 float,
avc_basimp float not null,    -- Base Imponible  (Imp.Lin - Dtos)
avc_imcob2 float,
avc_kilos float not null,       -- Suma de Kilos.
div_codi int,		-- Divisa
avc_unid int not null, -- Suma de Unidades
avc_tottas float,
avc_totta2 float,
avc_apltas smallint,
avc_revpre smallint default 0 not null, -- Albaran Revisar Precios.0 No
avc_recarg float,
avc_imprec float,
avc_impalb float,	-- Importe Albaran (NETO Inc. Impuestos)
avc_impcob float,	-- Importe Cobrado
avc_impuv float,
avc_cucomi int,     -- -1 Desbloqueado. 0 Normal.
avc_valora int not null default 0,		-- 0 NO valorado -1 Si valorado. 2 Pend. Valorar
avc_dtopp decimal(5,2),	-- Dto. Pronto Pago
avc_dtocom decimal(5,2),-- Dto Comercial
avc_dtootr decimal(5,2), -- Dto Otros - En Euros. 
avc_recfin decimal(5,2),
avc_tipalb varchar(1),
avc_rcaedi varchar(3),
avc_nalsab varchar(15),
avc_ncarg char(17),
avc_nrelen varchar(17),
avc_repres varchar(2),
avc_depos char(1) default 'N' NOT NULL, -- 'N' Normal, 'D' Deposito

constraint ix_albavec primary key (emp_codi,avc_ano,avc_nume,avc_serie)
);
create index albavec1 on v_albavec (avc_fecalb,cli_codi);
create index albavec2 on v_albavec (cli_codi,avc_fecalb);
---
-- Historico cabecera de albaranes de venta
-- 
create table hisalcave
(
avc_id int,
avc_ano int not null,
emp_codi int not null,
avc_serie char(1) not null,
avc_nume int not null,
cli_codi int not null,
avc_clinom varchar(40),	 -- Nombre de Cliente
avc_cofra int,
avc_fecalb date,	 -- Fecha de Albaran
usu_nomb varchar(20),
avc_tipfac varchar(1),
cli_ruta varchar(2),		-- Ruta de Cliente
cli_codfa int,		-- Cliente de la Factura
fvc_ano int,		-- Año de La Factua
fvc_serie char(1),      -- Serie de Fra.
fvc_nume int,		-- Numero de La Factura
avc_cerra smallint,	-- Si no esta cerrado NO se facturara y se sumara al stock
avc_impres smallint, 	-- Albaran Impreso. (0 = No.  bit 1 = Impreso. bit 2 = Modif. Cant. 4 Modif. Precios)
fvc_trasp smallint,
avc_fecemi date,
-- avc_dtosbi smallint,	-- Dto s/Base Imponible (Si/No) NO SE USA
sbe_codi smallint,	-- SubEmpresa
avc_desrec varchar(15),
alm_codori int,		-- Almacen Origen (Para Alb. de Traspaso)
alm_coddes int,		-- Almacen Destino (Para Alb. de Traspaso)
avc_confo smallint,	-- Conforme (0 NO se puede Facturar -1  SI se puede facturar )
avc_preiva smallint,
avc_cobrad smallint,	-- Cobrado (S -1/No 0)
avc_obser varchar(255), -- Observaciones
avc_fecrca date,
avc_almori int,		-- Almacen Origen (de donde se saca el stock)
avc_tarimp char(1),	-- Tarjeton IMPRESO (NO USADO)
avc_aux1 varchar(50),
avc_aux2 varchar(50),
avc_aux3 varchar(50),
avc_impre2 float,
avc_basimp float not null,    -- Base Imponible  (Imp.Lin - Dtos)
avc_imcob2 float,
avc_kilos float,       -- Suma de Kilos.
div_codi int,		-- Divisa
avp_unid int,
avc_tottas float,
avc_totta2 float,
avc_apltas smallint,
avc_revpre smallint default 0 not null, -- Albaran Revisar Precios.0 No
avc_recarg float,
avc_imprec float,
avc_impalb float,	-- Importe Albaran (NETO Inc. Impuestos)
avc_impcob float,	-- Importe Cobrado
avc_impuv float,
avc_cucomi int,
avc_valora int,		-- 0 NO valorado -1 Si valorado
avc_dtopp decimal(5,2),	-- Dto. Pronto Pago
avc_dtocom decimal(5,2),-- Dto Comercial
avc_dtootr decimal(5,2), -- Dto Otros
avc_recfin decimal(5,2),
avc_tipalb varchar(1),
avc_rcaedi varchar(3),
avc_nalsab varchar(15),
avc_ncarg char(17),
avc_nrelen varchar(17),
avc_repres varchar(2),
avc_depos char(1) default 'N' NOT NULL, -- 'N' Normal, 'D' Deposito
 his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
 his_fecha timestamp not null, -- Fecha de Cambio
 his_coment varchar(100), -- Comentario sobre el Cambio
 his_rowid int not null,
 constraint ix_hisalcave primary key (his_rowid)
);
--
-- Palets usados en albaranes de venta
--
-- drop table anjelica.paletventa
create table anjelica.paletventa
(
	avc_id int not null, -- Identificador Numero Albaran
	pav_nume int not null, -- Numero de Palet
	pav_kilos float not null, -- Kilos
	constraint ix_paletventa primary key (avc_id,pav_nume)
);
--
-- Cabecera de  de Albaranes servidos en deposito
--
create table anjelica.albvenserc
(
    avs_nume serial,
    avs_fecha date not null default current_date, -- Fecha de entrega
    avc_ano int not null, -- Año del Albaran al que esta ligado
    emp_codi int not null,--  Empresa del Albaran al que esta ligado
    avc_serie char(1) not null, -- Serie del Albaran al que esta ligado
    avc_nume int not null, -- Numero del Albaran al que esta ligado
    cli_codi int not null,
    constraint ix_albserc primary key(avs_nume),
    CONSTRAINT con1 CHECK ( avc_nume >0)
);
--
-- Servido de un albaran de deposito
--
create view anjelica.v_albdepserv as select sa.*,ca.avc_fecalb,ca.avc_id,cl.cli_nomb,cl.cli_nomco
 from albvenserc as sa, v_albavec as ca,
clientes as cl
where ca.avc_ano=sa.avc_ano
and  ca.emp_codi = sa.emp_codi
and  ca.avc_serie= sa.avc_serie
and  ca.avc_nume= sa.avc_nume
and sa.cli_codi = cl.cli_codi;
grant select on anjelica.v_albdepserv to public;

--
-- Lineas de  de Albaranes servidos en deposito
--
create table anjelica.albvenserl
(
    avs_nume int not null,
    avs_numlin int not null,
    pro_codi int not null,
    pro_nomb varchar(50),	-- Descripcion del Articulo
    avs_canti float not null,	-- Cantidad de Linea
    avs_unid int not null,      -- Unidades
    constraint ix_albvenserl primary key (avs_nume,avs_numlin)
) ;
--
-- Individuos de Albaranes servidos en deposito
--
create table anjelica.albvenseri
(
    avs_nume int not null,
    avs_numlin int not null,  -- Numero de Linea de Albaran
    avi_numlin int not null,  -- Numero de Linea en individuos
    avs_ejelot int,           -- Ejercicio Lote
    avs_emplot int,	      -- Empresa Lote -- deprecated.
    avs_serlot char(1),       -- Serie de Lote
    avs_numpar int,           -- Numero de Partida (Lote)
    avs_numind int,           --  Numero de Individuo
    avs_numuni float,         -- Numero de Unidades
    avs_canti decimal(9,3),   -- Kilos
    constraint ix_albvenseri primary key (avs_nume,avs_numlin,avi_numlin )
);
create view anjelica.v_proservdep as select c.*,l.avs_numlin,l.pro_codi,l.pro_nomb,l.avs_canti as avl_canti,l.avs_unid,
i.avi_numlin,avs_ejelot,avs_emplot,avs_serlot,avs_numpar,avs_numind,avs_numuni,i.avs_canti from albvenserc as c,albvenserl as l, albvenseri as i 
where c.avs_nume = l.avs_nume and c.avs_nume = i.avs_nume and l.avs_numlin = i.avs_numlin;
grant select on anjelica.v_proservdep to public;
--
-- Pendiente de servir de un albaran de deposito
--
drop view anjelica.v_propenddep;
create view anjelica.v_propenddep as select ca.avc_id,avc_fecalb,
ca.pro_codi,avp_ejelot,avp_serlot,avp_numpar,avp_numind
 from v_albventa_detalle as ca 
where not exists (select * from v_albvenserv as sa where ca.emp_codi = sa.emp_codi
and  ca.avc_serie= sa.avc_serie
and  ca.avc_nume= sa.avc_nume
and ca.avc_ano=sa.avc_ano
and ca.pro_codi= ca.pro_codi
and sa.avs_serlot=ca.avp_serlot
and sa.avs_numpar=ca.avp_numpar and sa.avs_numind=ca.avp_numind and sa.avs_ejelot=ca.avp_ejelot);
grant select on anjelica.v_propenddep to public;
-- Tabla Lineas de Albaranes de Ventas
---
-- drop table v_albavel;
create table anjelica.v_albavel
(
avc_ano int not null,       -- Año del Albaran
emp_codi int not null,      -- Empresa del Albaran
avc_serie char(1) not null, -- Serie del Albaran
avc_nume int not null,      -- Numero del Albaran
avl_numlin int not null,      -- Nº Linea de ALbaran
pvc_nume int,               -- Numero de Pedido
pro_codi int,               -- Codigo de Producto
avl_tipdes char(1),         -- Tipo de Descuento. Siempre '%'
fvl_numlin int,             -- Nº Linea de Factura
alm_codi int,               -- Almacen de Producto (Siempre albavec.alm_codori)
pro_nomb varchar(50),	    -- Descripcion del Articulo
avl_numues varchar(15),
avl_fecmue date,
avl_fecrli date,
avl_numcaj smallint not null default 0, -- Numero de Caja
avl_numpal int not null default 0, -- Numero Pale
avl_coment varchar(50),      -- Comentario
aux_2 varchar(50),
aux_3 varchar(50),
precioventa2 float,	-- SIEMPRE A 0
descuento2 float,	-- SIEMPRE A 0
preciobase2 float,	-- SIEMPRE A 0
ptsincremento2 float,
preciotarifa2 float,
precioclientearticulo2 float,
preciopvp2 float,
tiporecargotasa char(1), -- Siempre %recargotasa float,	-- Siempre 0
recargotasa2 float,	-- Siempre 0
avl_canti float,	-- Cantidad de Linea
avl_prven float,	-- precio de Venta
avl_dtolin float,	-- Dto en Linea (En Euros)
avl_prbase float,       -- Precio Final de Linea ((prven - dtolin) - ((prven-dtolin)*(dtocom+dtopp))
avl_ptsinc float,
tar_preci float,	-- precio de tarifa
avl_unid int,		-- Unidades
avl_pincre float,       --
avl_pcomi float,
avl_profer float not null,       -- Precio Pedido
avl_prclar float,
avl_prepvp float,       -- Precio puesto por el tecnico
avl_poreo float,
avl_canbru float,		-- Peso bruto
avl_anorec int,
avl_serere varchar(1),
numrecepcion int,
avc_cerra int,		-- Cerrado (0 NO, -1 SI)
codproveedor int,
serierecepcion varchar(1),
avl_fecalt timestamp default  current_timestamp, -- Fecha Alta linea de albaran
constraint ix_albavel primary key (avc_ano,emp_codi,avc_nume,avc_serie,avl_numlin)
);
create index ix_albavel2 on anjelica.v_albavel (pro_codi,avc_cerra);
create index ix_albavel3 on anjelica.v_albavel (avl_fecalt);

drop view anjelica.v_albventa;
create view anjelica.v_albventa as select c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,avc_dtocom,
cli_codi,avc_clinom,avc_fecalb, usu_nomb,avc_tipfac, cli_codfa,
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
-- Tabla Historico Lineas de Albaranes de Ventas
---
-- drop table hisallive;
create table anjelica.hisallive
(
avc_ano int not null,       -- Año del Albaran
emp_codi int not null,      -- Empresa del Albaran
avc_serie char(1) not null, -- Serie del Albaran
avc_nume int not null,      -- Numero del Albaran
avl_numlin int not null,      -- Nº Linea de ALbaran
pvc_nume int,               -- Numero de Pedido
pro_codi int,               -- Codigo de Producto
avl_tipdes char(1),         -- Tipo de Descuento. Siempre '%'
fvl_numlin int,             -- Nº Linea de Factura
alm_codi int,               -- Almacen de Producto (Siempre albavec.alm_codori)
pro_nomb varchar(50),	    -- Descripcion del Articulo
avl_numcaj smallint not null default 0, -- Numero de Caja
avl_numpal int not null default 0, -- Numero de Pale
avl_numues varchar(15),
avl_fecmue date,
avl_fecrli date,
turno int,
avl_coment varchar(50),      -- Comentario
aux_2 varchar(50),
aux_3 varchar(50),
precioventa2 float,	-- SIEMPRE A 0
descuento2 float,	-- SIEMPRE A 0
preciobase2 float,	-- SIEMPRE A 0
ptsincremento2 float,
preciotarifa2 float,
precioclientearticulo2 float,
preciopvp2 float,
tiporecargotasa char(1), -- Siempre %
recargotasa float,	-- Siempre 0
recargotasa2 float,	-- Siempre 0
avl_canti float,	-- Cantidad de Linea
avl_prven float,	-- precio de Venta
avl_dtolin float,	-- Dto en Linea (En Euros)
avl_prbase float,       -- Precio Final de Linea ((prven - dtolin) - ((prven-dtolin)*(dtocom+dtopp))
avl_ptsinc float,
tar_preci float,	-- precio de tarifa
avl_unid int,		-- Unidades
avl_pincre float,       --
avl_pcomi float,
avl_profer float,       -- Precio Pedido
avl_prclar float,
avl_prepvp float,       -- Precio puesto por el tecnico
avl_poreo float,
avl_canbru float,
avl_anorec int,
avl_serere varchar(1),
numrecepcion int,
avc_cerra int,		-- Cerrado (0 NO, -1 SI)
codproveedor int,
serierecepcion varchar(1),
avl_fecalt timestamp, -- Fecha Alta linea de albaran
his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
 his_fecha timestamp not null, -- Fecha de Cambio
 his_coment varchar(100), -- Comentario sobre el Cambio
 his_rowid int not null
);
create index ix_hisallive on hisallive (his_rowid);
--
-- Desglose de lineas Albaranes de Venta
--
-- drop table v_albvenpar;
create table anjelica.v_albvenpar
(
avc_ano int not null,		-- Año de Albaran
emp_codi int not null,		-- Empresa de Albaran
avc_serie char(1) not null,	-- Serie de Albaran
avc_nume int not null,		-- Numero de Albaran
avl_numlin int not null,	-- Numero Linea de Albaran
avp_numlin int not null,	-- Numero Linea de Partida
pro_codi int,			-- Producto
avp_canori decimal(9,3) not null,		-- Kilos Originales (los leidos en la bascula sin destares)
avp_ejelot int,			-- Ejercicio Lote
avp_emplot int,			-- Empresa Lote
avp_serlot char(1), 	-- Serie de Lote
avp_numpar int,			-- Numero de Partida (Lote)
avp_numind int,			--  Numero de Individuo
avp_numuni float,		-- Numero de Unidades
avp_canti decimal(9,3) not null,  -- Kilos a facturar (netos)
avp_canbru decimal(9,3) not null, -- Cantidad brutos (Kilos)
constraint ix_albvenpar primary key  (avc_ano,emp_codi,avc_nume,avc_serie,avl_numlin,avp_numlin)
);
DROP VIEW anjelica.v_albventa_detalle ;
CREATE OR REPLACE VIEW v_albventa_detalle AS 
 SELECT c.emp_codi,    c.avc_ano,    c.avc_serie,    c.avc_nume,
	c.avc_id,    c.cli_codi,    c.avc_clinom,    c.avc_fecalb,
    c.usu_nomb,    c.avc_tipfac,    c.cli_codfa,    c.fvc_ano,
    c.fvc_nume,    c.avc_cerra,    c.avc_impres,    c.avc_fecemi,
    c.sbe_codi,    c.avc_cobrad,    c.avc_obser,    c.avc_fecrca,
    c.avc_basimp,    c.avc_kilos,    c.avc_unid,    c.div_codi,
    c.avc_impalb,    c.avc_impcob,    c.avc_dtopp,    c.avc_dtootr,
    c.avc_valora,    c.fvc_serie,    c.avc_depos,    l.avl_numlin,
    l.pro_codi,    l.avl_numpal,    l.avl_numcaj,    l.pro_nomb,
    l.avl_canti,    l.avl_prven,    l.avl_prbase,    l.tar_preci,
    l.avl_unid,    l.avl_canbru,    l.avl_fecalt,    l.fvl_numlin,
    l.avl_fecrli,    c.alm_codori,    c.alm_coddes,    p.pro_codi AS avp_procod,
    p.avp_numlin,    p.avp_ejelot,    p.avp_emplot,    p.avp_serlot,
    p.avp_numpar,    p.avp_numind,    p.avp_numuni,    p.avp_canti,
    p.avp_canbru,    p.avp_canori
   FROM v_albavel l,
    v_albavec c,
    v_albvenpar p
  WHERE c.emp_codi = l.emp_codi AND c.avc_ano = l.avc_ano AND c.avc_serie = l.avc_serie
  AND c.avc_nume = l.avc_nume AND c.emp_codi = p.emp_codi AND c.avc_ano = p.avc_ano
  AND c.avc_serie = p.avc_serie AND c.avc_nume = p.avc_nume AND l.avl_numlin = p.avl_numlin;
GRANT SELECT ON TABLE v_albventa_detalle TO public;

grant select on anjelica.v_albventa_detalle to public;

create index ix_albvenpa1 on v_albvenpar (avp_ejelot,avp_serlot,avp_numpar,avp_numind);

--
-- Historico Partidas de Albaranes de Venta
--
-- drop table hisalpave;
create table anjelica.hisalpave
(
avc_ano int not null,		-- Año de Albaran
emp_codi int not null,		-- Empresa de Albaran
avc_serie char(1) not null,	-- Serie de Albaran
avc_nume int not null,		-- Numero de Albaran
avl_numlin int not null,	-- Numero Linea de Albaran
avp_numlin int not null,	-- Numero Linea de Partida
pro_codi int,			-- Producto
avp_canori decimal(9,3) not null,		-- Cantidad Original
avp_ejelot int,			-- Ejercicio Lote
avp_emplot int,			-- Empresa Lote
avp_serlot char(1), 		-- Serie de Lote
avp_numpar int,			-- Numero de Partida (Lote)
avp_numind int,			--  Numero de Individuo
avp_numuni float,		-- Numero de Unidades
avp_canti decimal(9,3),         -- Kilos
avp_canbru decimal(9,3),  -- Cantidad brutos (Kilos)
his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
 his_fecha timestamp not null, -- Fecha de Cambio
 his_coment varchar(100), -- Comentario sobre el Cambio
 his_rowid int not null
);
create index ix_hisalpave on hisalpave  (his_rowid);
-- DROP VIEW  anjelica.v_halbventa_detalle;
create view anjelica.v_halbventa_detalle as 
select c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,cli_codi,avc_clinom,avc_fecalb, usu_nomb,avc_tipfac, cli_codfa,
fvc_ano,fvc_nume,c.avc_cerra,avc_impres,avc_fecemi,sbe_codi,avc_cobrad,avc_obser,avc_fecrca,
avc_basimp,avc_kilos,avc_unid,div_codi,avc_impalb,avc_impcob,avc_dtopp,avc_dtootr,avc_valora,fvc_serie,c.his_rowid,
avc_depos,l.avl_numlin,l.pro_codi,avl_numpal,pro_nomb,avl_canti,avl_prven,avl_prbase,tar_preci,avl_unid,
avl_canbru,avl_fecalt,fvl_numlin,avl_fecrli,alm_codori,alm_coddes,
avp_numlin,avp_ejelot,avp_emplot,avp_serlot,avp_numpar,avp_numind,avp_numuni,avp_canti,avp_canbru,avp_canori
from hisalcave as c, hisallive as l, hisalpave as p 
where c.his_rowid=l.his_rowid and l.his_rowid=p.his_rowid
and l.avl_numlin=p.avl_numlin;
grant select on anjelica.v_halbventa_detalle to public;
--
-- Tabla Productos de Residiuos en albaran de ventas(MER,GRASA,etc)
--
create table anjelica.albvenres
(
avc_ano int not null,		-- Año de Albaran
emp_codi int not null,		-- Empresa de Albaran
avc_serie char(1) not null,	-- Serie de Albaran
avc_nume int not null,		-- Numero de Albaran
avr_numlin int not null,	-- Numero Linea de Albaran
pro_codi int not null,          -- Producto
avr_canti decimal(9,3) not null,-- Kilos
avr_fecalt timestamp default  current_timestamp, -- Fecha Alta linea
constraint ix_albvenres primary key  (avc_ano,emp_codi,avc_nume,avc_serie,avr_numlin)
);
--
-- Historico de Albaranes de ventas
--
create table anjelica.hisalbavec
(
    avc_ano int not null,
    emp_codi int not null,
    avc_serie char(1) not null,
    avc_nume int not null,
    cli_codi int not null,
    avc_clinom varchar(40),
    avc_cofra int,
    avc_fecalb date,
    usu_nomb varchar(20),
    avc_tipfac varchar(1),
    cli_ruta varchar(2),
    cli_codfa int,
    fvc_ano int,
    fvc_serie char(1),
    fvc_nume int,
    avc_cerra smallint,
    avc_impres smallint,
    fvc_trasp smallint,
    avc_fecemi date,
    sbe_codi smallint,
    avc_desrec varchar(15),
    alm_codori int,
    alm_coddes int,
    avc_confo smallint,
    avc_preiva smallint,
    avc_cobrad smallint,
    avc_obser varchar(255),
    avc_fecrca date,
    avc_almori int,
    avc_tarimp char(1),
    avc_aux1 varchar(50),
    avc_aux2 varchar(50),
    avc_aux3 varchar(50),
    avc_impre2 float,
    avc_basimp float not null,
    avc_imcob2 float,
    avc_impuv2 float,
    div_codi int,
    avc_unid int,
    avc_tottas float,
    avc_totta2 float,
    avc_apltas smallint,
    avc_alrepr smallint default 0 not null,
    avc_recarg float,
    avc_imprec float,
    avc_impalb float,
    avc_impcob float,
    avc_impuv float,
    avc_cucomi int,
    avc_valora int not null default 0, -- 0: No Valorado. 1 Valorado. 2. Pend. Valorar
    avc_dtopp decimal(5,2),
    avc_dtocom decimal(5,2),
    avc_dtootr decimal(5,2),
    avc_recfin decimal(5,2),
    avc_tipalb varchar(1),
    avc_rcaedi varchar(3),
    avc_nalsab varchar(15),
    avc_ncarg char(17),
    avc_nrelen varchar(17),
    avc_depos char(1) default 'N' NOT NULL, -- (N)o es alb. Deposito, (D)Deposito
constraint ix_halbvec primary key (emp_codi,avc_ano,avc_nume,avc_serie)
);
create table anjelica.hisalbavel
(
    avc_ano int not null,
    emp_codi int not null,
    avc_serie char(1) not null,
    avc_nume int not null,
    avl_numlin int not null,
    pvc_nume int,
    pro_codi int,
    avl_tipdes char(1),
    fvl_numlin int,
    alm_codi int,
    pro_nomb varchar(50),
    avl_numues varchar(15),
    avl_fecmue date,
    avl_fecrli date,
    turno int,
    avl_coment varchar(50),
    aux_2 varchar(50),
    aux_3 varchar(50),
    precioventa2 float,
    descuento2 float,
    preciobase2 float,
    ptsincremento2 float,
    preciotarifa2 float,
    precioclientearticulo2 float,
    preciopvp2 float,
    tiporecargotasa char(1),
    recargotasa float,
    recargotasa2 float,
    avl_canti float,
    avl_prven float,
    avl_dtolin float,
    avl_prbase float,
    avl_ptsinc float,
    tar_preci float,
    avl_unid int,
    avl_pincre float,
    avl_pcomi float,
    avl_profer float,
    avl_prclar float,
    avl_prepvp float,
    avl_poreo float,
    avl_canbru float,
    avl_anorec int,
    avl_serere varchar(1),
    numrecepcion int,
    avc_cerra int,
    codproveedor int,
    serierecepcion varchar(1),
    avl_fecalt date default  current_date,
 constraint ix_halbvel primary key (avc_ano,emp_codi,avc_nume,avc_serie,avl_numlin)
);
create table anjelica.hisalbvenpar
(
    avc_ano int not null,
    emp_codi int not null,
    avc_serie char(1) not null,
    avc_nume int not null,
    avl_numlin int not null,
    avp_numlin int not null,
    pro_codi int,
    avp_tiplot char(1),
    avp_ejelot int,
    avp_emplot int,
    avp_serlot char(1),
    avp_numpar int,
    avp_numind int,
    avp_numuni float,
    avp_canti decimal(9,3),
constraint ix_halbvepar primary key  (avc_ano,emp_codi,avc_nume,avc_serie,avl_numlin,avp_numlin)
);
--
-- Cabeceras Albaranes de Compras
--
-- drop table v_albacoc;
create table anjelica.v_albacoc
(
acc_ano int not null,		-- Ejerc. Albaran
emp_codi int not null,		-- Empresa de Albaran
acc_serie char(1) not null,	-- Serie de Albaran
acc_nume int not null,		-- Numero de Albaran
prv_codi int,			-- Codigo de Proveedor
acc_fecrec date,		-- Fecha de Recepcion
usu_nomb varchar(20),
acc_copvfa int,
fcc_ano int,			-- Ejerc. de Fra.
fcc_nume int,			-- Numero de Fra.
acc_obser varchar(255),
acc_portes char(1) not null,	-- Portes Pagados/Debidos
acc_impokg float,		-- Importe de Porte/Kg.
acc_imcokg float not null default 0, 		-- Importe de COmisiones / Kg
frt_ejerc int not null,		-- Ejercicio Fra. de transp.
frt_nume int not null,		-- Fra. de Transportista
acc_cerra int not null,		-- Albaran Cerrado (0 NO -1 Si)
acc_totfra int not null,	-- Totalmente Facturado (0 No)
sbe_codi smallint,
avc_ano smallint,		-- Año del Albaran (0 Si no tiene)
avc_nume smallint,		-- Numero del Albaran (La serie deber ser Y)
apc_nume int,			-- Numero de Alb. de Proveedor
acc_id serial not null, -- Identificador Albaran 
constraint ix_albacoc primary key (acc_ano,emp_codi,acc_serie,acc_nume)
);
create index ix_albacoc2 on v_albacoc(emp_codi,prv_codi);
--
-- Tabla Historico Cabecera Albaranes de compras
-- drop table v_hisalcaco;
create table anjelica.hisalcaco
(
acc_ano int not null,		-- Ejerc. Albaran
emp_codi int not null,		-- Empresa de Albaran
acc_serie char(1) not null,	-- Serie de Albaran
acc_nume int not null,		-- Numero de Albaran
prv_codi int,			-- Codigo de Proveedor
acc_fecrec date,		-- Fecha de Recepcion
usu_nomb varchar(20),
acc_copvfa int,
fcc_ano int,			-- Ejerc. de Fra.
fcc_nume int,			-- Numero de Fra.
acc_obser varchar(255),
acc_portes char(1) not null,	-- Portes Pagados/Debidos
acc_impokg float,		-- Importe de Porte/Kg.
acc_imcokg float not null default 0, 		-- Importe de COmisiones / Kg
frt_ejerc int not null,		-- Ejercicio Fra. de transp.
frt_nume int not null,		-- Fra. de Transportista
acc_cerra int not null,		-- Albaran Cerrado (0 NO -1 Si)
acc_totfra int not null,	-- Totalmente Facturado (0 No)
sbe_codi smallint,
avc_ano smallint,		-- Año del Albaran (0 Si no tiene)
avc_nume smallint,		-- Numero del Albaran (La serie deber ser Y)
apc_nume int,			-- Numero de Alb. de Proveedor
acc_id int,
his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
his_fecha timestamp not null, -- Fecha de Cambio
his_coment varchar(100), -- Comentario sobre el Cambio
his_rowid int not null,
constraint ix_hisalcaco primary key (his_rowid)
);
---
-- Lineas Albaranes de Compras
---
-- drop table v_albacol;
create table anjelica.v_albacol
(
acc_ano int not null,		-- Año Albaran
emp_codi int not null,		-- Empresa Albaran
acc_serie char(1),		-- Serie Albaran
acc_nume int not null,		-- Numero de Albaran
acl_nulin int not null,		-- Numero de Linea
pcc_nume int,			-- Nº Pedido
pro_codi int,			-- Codigo Producto
pro_nomart varchar(50),
acl_numcaj int,			--- Numero de Unidades
acl_tipdes char(1),		-- Tipo descuento (Siempre %. NO USADO)
acl_porpag smallint,		-- Porte pagado (0 NO,-1: Si)
alm_codi int,			-- Almacen.
acl_canti decimal(9,3),		-- Cantidad Recibida
acl_kgrec decimal(9,3),		-- Kg. de Recorte
acl_prcom decimal(9,3),		-- Precio de Compra
acl_canfac float,		-- Cantidad YA Facturada
acl_comen varchar(50),
acc_cerra smallint,		-- Albaran Cerrado (0 = NO)
acl_totfra smallint not null,	-- Linea Totalmente Fact. (0 = No)
acl_prstk decimal(9,3) not null, -- Precio Para Inventarios
acl_dtopp float not null default 0, -- Dto Pronto Pago
constraint ix_albacol  primary key (acc_ano,emp_codi,acc_serie,acc_nume,acl_nulin)
);
--
-- Historico lineas de albaranes de compras
--
create table31 anjelica.hisallico
(
acc_ano int not null,		-- Año Albaran
emp_codi int not null,		-- Empresa Albaran
acc_serie char(1),		-- Serie Albaran
acc_nume int not null,		-- Numero de Albaran
acl_nulin int not null,		-- Numero de Linea
pcc_nume int,			-- Nº Pedido
pro_codi int,			-- Codigo Producto
pro_nomart varchar(50),
acl_numcaj int,			--- Numero de Unidades
acl_tipdes char(1),		-- Tipo descuento (%)
acl_porpag smallint,	-- Porte pagado (0 NO,-1: Si)
alm_codi int,			-- Almacen.
acl_canti decimal(9,3),		-- Cantidad Recibida
acl_kgrec decimal(9,3),		-- Kg. de Recorte
acl_prcom decimal(9,3),		-- Precio de Compra. 
acl_canfac float,		-- Cantidad YA Facturada
acl_comen varchar(50),
acc_cerra smallint,		-- Albaran Cerrado (0 = NO)
acl_totfra smallint not null,	-- Linea Totalmente Fact. (0 = No)
acl_prstk decimal(9,3) not null, -- Precio Para Inventarios
acl_dtopp float not null default 0, -- Dto PP. Normalmente es el mismo de cabecera
his_rowid int not null
);
 create index ix_hisallico on hisallico   (his_rowid,acl_nulin);
--
-- Partidas de los Albaranes de Compras. Detalle de los individuos
-- Su equivalente en  historico es hisalpaco
--
-- drop table v_albcompar;
create table anjelica.v_albcompar
(
   acc_ano int not null,	-- Ejerc. del Alb. de Compra
   emp_codi int not null,	-- Empresa del Alb. de Compra
   acc_serie char(1) not null,	-- Serie del Alb. de Compra
   acc_nume int not null,	-- Numero del Alb. de Compra
   acp_numlin int, 		-- Numero de Linea de la Partida
   acp_claani int,		-- Clase de Animal  (NO USADO)
   acp_numind int,		-- Numero de Ind.
   pcc_nume int ,		-- Numero de Pedido (NO USADO)
   guiasanitarianumero varchar(30), -- NO USADO
   guiasanitariaserie varchar(10),  -- NO USADO
   acp_fecpro date,                 -- Fecha Produccion.
   guiasanitariancrotal varchar(30),-- NO USADO
   dre_nume int ,                  -- Numero de Datos Registro 
   dib varchar(50),                 -- NO USADO
   acp_nucrot varchar(30),	-- Numero de Crotal
   acp_painac int,		-- Pais de Nacimiento
   dibnacimientocomunidad int,	-- NO USADO
   dibnacimientoprovincia int,  -- NO USADO
   acp_feccad date,		-- Fecha Caducidad
   dibsexo int,			-- NO USADO
   dibraza int,			-- NO USADO
   ncrotal2 varchar(30),	-- NO USADO
   ncrotal3 varchar(30),	-- NO USADO
   acp_paisac int,		-- Pais de Sacrificio
   sacrificiocomunidad int,	-- NO USADO
   sacrificioprovincia int,	-- NO USADO
   acp_fecsac date,		-- Fecha de Sacrificio
   tiposacrificio int,		-- NO USADO
   certificadornombre varchar(50),	-- NO USADO
   certificadornumero varchar(15),	-- NO USADO
   numerocorral varchar(30),		-- NO USADO
   ganadero int,			-- NO USADO
   observaciones varchar(255),
   opcional1 varchar(15),		-- NO USADO
   opcional2 varchar(15),		-- NO USADO
   sacrificado smallint,		-- NO USADO
   clasificacion char(1),		-- NO USADO
   gradograsa int,		-- NO USADO
   categoria char(1),		-- NO USADO
   pro_codi int,		-- Codigo de Producto
   acp_clasi varchar(10),   -- Clasificación
   tipo_c2 varchar(10),		-- NO USADO
   tipo_c3 varchar(10),		-- NO USADO
   tipo_c4 varchar(10),		-- NO USADO
   tipo_c5 varchar(10),		-- NO USADO
   caracteristica1 char(1),		-- NO USADO
   caracteristica2 char(1),		-- NO USADO
   caracteristica3 char(1),		-- NO USADO
   caracteristica4 char(1),		-- NO USADO
   caracteristica5 char(1),		-- NO USADO
   ordensacrificio char(1),		-- NO USADO
   precinto1 int,		-- NO USADO
   precinto2 int,		-- NO USADO
   precinto3 int ,		-- NO USADO
   precinto4 int ,		-- NO USADO
   acp_canind int ,		-- Cant.de Individuos (normalmente 1)
   mat_codi int,		-- Codigo de Matadero
   acl_nulin int not null,	-- Numero de Linea del Albaran
   traspasado smallint,		-- NO USADO
   sde_codi int,		-- Codigo Sala Despiece
   aux_1 varchar(50),		-- NO USADO
   aux_2 varchar(50),		-- NO USADO
   aux_3 varchar(50),		-- NO USADO
   precio float,		-- NO USADO
   precio2 float,		-- NO USADO
   acp_engpai int,		-- Pais de Engorde
   acp_canti float,		-- Kilos
   pesomediacanal1 float,		-- NO USADO
   pesomediacanal2 float,		-- NO USADO
   pesocueros float,		-- NO USADO
constraint ix_albcompar primary key(acc_ano,emp_codi,acc_serie,acc_nume,acl_nulin,acp_numlin)
);
-- create index ix_albcompar on v_albcompar (acc_ano,emp_codi,acc_serie,acc_nume,acp_numlin);
 create index ix_albcompar2 on v_albcompar (pro_codi,acc_ano,acc_serie,acp_numind);

-- drop view anjelica.v_compras;
 create or replace view anjelica.v_compras as 
select c.acc_ano, c.emp_codi,c.acc_serie, c.acc_nume, c.prv_codi, c.acc_fecrec, c.fcc_ano, c.fcc_nume,c.acc_portes,c.frt_ejerc,c.frt_nume,c.acc_cerra,c.sbe_codi,
l.acl_nulin,l.pro_codi,l.pro_nomart, acl_numcaj,l.acl_Canti,l.acl_prcom,l.acl_canfac,acl_kgrec,l.acl_comen, l.acl_dtopp,l.alm_codi,
i.acp_numlin,i.acp_numind,i.acp_canti,i.acp_canind,i.acp_feccad,i.acp_fecsac,i.acp_fecpro,i.acp_nucrot,i.acp_clasi,i.acp_painac,i.sde_codi,
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

create or replace view v_albacom as 
select c.acc_ano, c.emp_codi,c.acc_serie, c.acc_nume, c.prv_codi, c.acc_fecrec, c.fcc_ano, c.fcc_nume,c.acc_portes,c.frt_ejerc,c.frt_nume,c.acc_cerra,
l.acl_nulin,l.pro_codi,l.pro_nomart, acl_numcaj,l.acl_canti,l.acl_prcom,l.acl_canfac,acl_kgrec,l.acl_comen, l.acl_dtopp,l.alm_codi
from anjelica.v_albacoc as c,anjelica.v_albacol as l, anjelica
where c.acc_ano=l.acc_ano
and c.emp_codi=l.emp_codi
and c.acc_serie=l.acc_serie
and c.acc_nume=l.acc_nume
--order by l.acl_nulin,i.acp_numlin;
--
-- Historico Individuos albaranes de compras
--
create table anjelica.hisalpaco
(
   acc_ano int not null,	-- Ejerc. del Alb. de Compra
   emp_codi int not null,	-- Empresa del Alb. de Compra
   acc_serie char(1) not null,	-- Serie del Alb. de Compra
   acc_nume int not null,	-- Numero del Alb. de Compra
   acp_numlin int, 		-- Numero de Linea de la Partida
   acp_claani int,		-- Clase de Animal  (NO USADO)
   acp_numind int,		-- Numero de Ind.
   pcc_nume int ,		-- Numero de Pedido (NO USADO)
   acp_nucrot varchar(30),	-- Numero de Crotal
   acp_painac int,		-- Pais de Nacimiento
   acp_feccad date,		-- Fecha Caducidad
   acp_paisac int,		-- Pais de Sacrificio
   acp_fecsac date,		-- Fecha de Sacrificio
   acp_fecpro date,             -- Fecha de Produccion
   observaciones varchar(255),
   clasificacion char(1),       -- NO USADO
   pro_codi int,		-- Codigo de Producto
   acp_canind int ,		-- Cant.de Individuos
   mat_codi int,		-- Codigo de Matadero
   acl_nulin int,		-- Numero de Linea del Albaran
   sde_codi int,		-- Codigo Sala Despiece
   acp_engpai int,		-- Pais de Engorde
   acp_canti float,		-- Kilos
   his_rowid int not null
);
 create index ix_hisalpaco on hisalpaco   (his_rowid,acl_nulin);
--
-- Proveedores
--
-- drop table v_proveedo;
create table anjelica.v_proveedo
(
prv_codi int not null,
prv_nomb varchar(40),		 -- Nombre
prv_nomco varchar(50),		 -- Nombre comercial
prv_direc varchar(40),
prv_pobl varchar(30),
prv_codpo int,
pai_codi int, 			-- Pais del Proveedor
prv_telef varchar(15),
prv_fax varchar(15),
prv_nif varchar(20),		-- NIF
prv_percon varchar(30),		-- Persona de Contacto
prv_telcon varchar(15),		 -- Telefono de Contacto
prv_nombre varchar(40),		 -- Nombre para Recogida
prv_direre varchar(40),		-- Direccion de Recogida
prv_poblre varchar(30),	 	-- Poblacion de Recogida
prv_copore int,			-- Cod. Postal Recogida
prv_telere varchar(15),		-- Telefono de Recogida
prv_faxre  varchar(15),		-- Fax de Recogida
emp_codi int,			-- N/Cod. Empresa
prv_plzent int,			-- Plazo de Entrega (dias)
tar_codi int,			-- Tarifa
prv_codfac int,			-- Proveedor al que facturar
prv_tipfac char(1),		-- Tipo Fact. (Diario, Sem.,Quinc.,Mensual)
fpa_codi int,			-- Forma de Pago.
prv_dipa1 int,			-- Dia de Pago 1
prv_dipa2 int,			-- Dia de Pago 2
prv_inivac date,		-- Inicio de Vacaciones
prv_finvac date,		-- Final de Vacaciones
prv_recequ smallint,		-- Recargo Equivalencia
cue_codi varchar(12),		-- Cuenta Contable
prv_exeiva smallint,		-- Exento de IVA
prv_irpf smallint default 0 NOT NULL,	-- Tiene IRPF?
prv_rpfsbi smallint default 0 not null,	--  IRPF s/Base Imponible?
prv_activ varchar(2),		-- Discriminador 1 Activo (S/N)
prv_disc2 varchar(2),		-- Discriminador 2
prv_disc3 varchar(2),		-- Discriminador 3
prv_disc4 varchar(2),		-- Discriminador 4
prv_libiva char(2),		-- Libro de IVA
prv_sumdes smallint,		-- Suministra desglosado ?
prv_observ varchar(255),	-- Observaciones
prv_carter char(2),		-- Cartera
prv_diario char(2),		-- Diario
prv_sefact char(1),		-- Serie Fact. de Contabilidad
prv_tipiva char(2),		-- Tipo de IVA
prv_poriva char(2),		-- Porcentaje de IVA
prv_tipdoc char(2),		-- Tipo Documento
prv_sitfac char(2),		-- Situacion Facturas
prv_orgofi smallint,		-- Organismo Oficial
prv_aplore smallint,		-- Aplicar Oreo
prv_nexplo varchar(15),		-- Numero de Explotacion
prv_nurgsa varchar(12),		-- Numero de Registro Sanitario
div_codi int,			-- Codigo de  Divisa
prv_coimiv varchar(2),		-- Codigo Impuesto de IVA
prv_dtocom float,		-- Dto. Comercial
prv_dtopp float,		-- Dto. Pronto Pago
prv_dtorap float,		-- Dto de Rappel
prv_fecalt date,		-- Fecha de Alta.
prv_feulmo date,		-- Fecha Ult. Modific.
prv_intern smallint default 0 not null, 	-- Proveedor Interno ?
prv_poloin smallint,	-- Posicion inicial Lote en cod.Barras interno prv
prv_polofi smallint,	-- Posicion final Lote en cod.Barras interno prv
prv_popein smallint,	-- Posicion inicial peso en cod.Barras interno prv
prv_popefi smallint,	-- Posicion final peso en cod.Barras interno prv
constraint ix_proveedo primary key(prv_codi)
);
--
-- Tabla generica de Discriminadores
--
-- Hay 3 tipos 'base' de discriminador. Cliente, Articulo y Proveedor.
-- Luego para cada tipo se busca en la tabla configuracion, los tipos
-- segun los campos  cfg_dispr1-4 cfg_discl1-4 y cfg_dispv1-4
-- Ademas de esos discriminadores,en el programa pddiscrim, estan puestos a pelo 
-- los siguientes: 
--  Repres/Zona ->"R",    Credito -> "C,  Zona -> "z", Representante > "r", 'U' rutas.
--  Camara -> "C"
-- Estos discriminadores tienen sus correspondientes vistas (v_zonas, v_reprzona,v_rutas...,v_camaras)
-- drop table v_discrim;
create table anjelica.v_discrim
(
emp_codi int not null,		-- Empresa
dis_tipo char(2) not null,      -- C?: Clientes A?: Articulos, 'P?' Proveedor
dis_codi char(2),               -- Codigos para ese tipo
dis_nomb varchar(50),           -- Nombre
constraint ix_discrim primary key(emp_codi,dis_tipo,dis_codi)
);
--
-- Formas de Pago
--
-- drop table v_forpago;
create table anjelica.v_forpago
(
fpa_codi int not null,	-- Codigo de Forma de pago
fpa_nomb varchar(50),	-- Nombre
fpa_dia1 int not null,  -- Primer dia de pago
fpa_dia2 int not null,	-- Segundo dia de pago
fpa_dia3 int not null,	-- Tercer dia de pago
fpa_esgir int,		-- Es giro? -1 NO, otro SI
primary key (fpa_codi)
);
--
-- Facturas de Ventas
--
-- drop  table v_facvec;
create table anjelica.v_facvec
(
fvc_id serial not null, -- Numero Identificador
fvc_ano int not null,		-- Ejercicio de Fra.
emp_codi int not null,		-- Empresa
fvc_serie char(1) not null default '1', -- Serie Factura
fvc_nume int not null,		-- Numero de Factura
cli_codi int,			-- Codigo de Cliente
fvc_clinom varchar(40),		-- Nombre de Cliente
fvc_fecfra date,		-- Fecha de Factura
fvc_cuacom int,			-- Cuadro Comisiones (NO UTILIZADO)
fvc_dtosbi smallint,		-- DTO sobre Base Imp 0 -> No
fvc_desrec varchar(15),		-- Descripcion Recargo
fvc_sainen int,			-- Saldo Inicial de Envases (NO USADO)
fvc_trasp smallint,	 	-- Traspasada a Contabilidad ?
fvc_impres smallint,	  	-- Impresa ?
fvc_sumlin float,		-- Importe Suma de Lineas
fvc_sumtot decimal(8,2), 	-- Importe Total (Lo q se pagara, vamos)
fvc_impiva decimal(8,2),		-- Importe de IVA
fvc_imprec decimal(8,2),		-- Importe de Rec.Equi,.
fvc_cobrad smallint,		-- Cobrado Totalmente (-1 Si,0 No)
fvc_cobtra smallint,	  	-- Cobro Traspasado ? (NO SE USA)
div_codi int,			-- Divisa
fvc_impcob decimal(8,2),	-- Importe Cobrado
fvc_basimp decimal(8,2),	-- Importe Base Imponible
fvc_dtopp  decimal(4,2),	-- Porcentaje de Dto.PP
fvc_dtocom decimal(5,2),	-- NO USADO
fvc_dtootr decimal(5,2),	-- NO USADO
fvc_poriva decimal(5,2),	-- Porcentaje de IVA
fvc_porreq decimal(5,2),	-- Porcentaje de Rec.Equi.
fvc_recfin decimal(5,2),	-- Rec. Financiero
fvc_tipfac varchar(1),		-- Tipo de Factura
fvc_modif  varchar(1),		-- Modificado/Automatico
fpa_codi  int,			-- Forma de Pago
constraint ix_facvec primary  key (fvc_ano,emp_codi,fvc_serie,fvc_nume)
);
--
-- Tabla Lineas de facturas
--
drop table v_facvel;
create table anjelica.v_facvel
(
 eje_nume int NOT NULL,
 emp_codi int NOT NULL,
 fvc_serie char(1) not null default '1', -- Serie Factura
 fvc_nume int NOT NULL,
 fvl_numlin int NOT NULL,
 fvl_tipdes CHAR(1) ,		-- Tipo de Descuento. Siempre (%)
 pro_codi INT ,			-- Codigo de Producto
 fvl_nompro varchar(50),	-- Descripcion del Producto
 avc_nume INT	,		-- Numero de Albaran
 avc_ano INT,			-- Año del Albaran
 avc_serie CHAR(1),		-- Serie del Albaran
 avc_fecalb DATE,		-- Fecha del Albaran
 fvl_prve2 FLOAT,
 fvl_dto2 FLOAT,
 fvl_tireta CHAR(1), 	-- Tipo Recargo TASA. Siempre "%"
 fvl_rectas FLOAT,		-- Recargo Tasa. Siempre 0
 fvl_reta2 FLOAT,		-- Recargo Tasa2
 fvl_canti FLOAT,
 fvl_dto FLOAT,
 fvl_prven FLOAT,
 constraint ix_facvel primary key (eje_nume,emp_codi,fvc_serie,fvc_nume,fvl_numlin)
);
create index ix_facve1 on v_facvel(emp_codi,avc_ano,avc_serie,avc_nume);
--
-- Facturas de ventas. Comentario
create table anjelica.facvecom
(
 eje_nume int NOT NULL,
 emp_codi int NOT NULL,
 fvc_serie char(1) not null , -- Serie Factura
 fvc_nume int NOT NULL,
 fco_numlin int NOT NULL, -- Numero de Linea
 fco_tipo char(1) not null, -- Tipo C (Cabecera) P (Pie)
 fco_coment varchar(76) not null -- Linea comentario
);
create index ix_facvecom on facvecom  (eje_nume,emp_codi,fvc_serie,fvc_nume);
--
--
-- Zonas
---
-- drop table v_zona;
-- create table anjelica.v_zona
-- (
-- zon_codi int not null,
-- zon_nomb varchar(30)
-- );
-- create unique index ix_zona on v_zona(zon_codi);
--
-- Familias de Productos
---
-- drop table v_famipro;
create table anjelica.v_famipro
(
emp_codi int not null,		-- Empresa (DEPRECATED)
fpr_codi int not null,		-- Codigo de Familia del producto
fpr_nomb varchar(30),		-- Name of Family
agr_codi int,			    -- Code of Group (DEPRECATED)
fpr_ctacom varchar(12),		-- Cuenta compras (SIN USAR)
fpr_ctaven varchar(12),		-- Cuenta ventas  (SIN USAR)
constraint ix_famipro primary key(fpr_codi)
);
---
--- Agrupaciones de productos
---
-- drop table v_agupro;
create table anjelica.v_agupro
(
agr_codi int not null,		-- Code of group
agp_nomb varchar(30),		-- Name of Group
agp_color varchar(20),		-- Colour of group
emp_codi int not null,          -- Code of Company (DEPRECATED)
constraint ix_agrupo primary key(agr_codi)
);
--
-- Tabla con los diferentes grupos a los q pertenece una familia.
--
create table anjelica.grufampro
(
agr_codi int not null, -- Grupo
fpr_codi int not null -- Familia
);
create index ix_grufampr1 on anjelica.grufampro (fpr_codi);
create index ix_grufampr2 on anjelica.grufampro (agr_codi);
--
-- Tabla con la manera de presentar los grupos.
--
-- drop index grufamvis;
create table anjelica.grufamvis
(
    gfv_codi int not null,
    gfv_orden int not null, -- Orden para presentar
    agr_codi int not null,  -- Grupo
    gfv_padre int not null  -- Padre de la familia. 0=Raiz
);

create table anjelica.desporig
(
eje_nume int not null,   -- Ejercicio de Despiece
deo_codi int not null,   -- Numero de despiece
tid_codi int,            -- Tipo de despiece
deo_fecha date not null,	-- Fecha del despiece
usu_nomb varchar(20),   -- Usuario que genero el despiece (LOGIN)
deo_almori int,		-- Almacen de Origen
deo_almdes int,		-- Almacen destino
deo_ejloge int,         -- Ejercicio de Lote Generado
deo_seloge char(1),     -- Serie de Lote Generado
deo_nuloge int,         -- Numero de Lote generado
deo_cerra smallint default -1,     -- Cerrado (0: No)
deo_lotnue smallint,    -- Lote Nuevo (0: No, -1 SI)
deo_numdes int not null default 0,       -- Numero Grupo de despiece
deo_feccad date,        -- Fecha Caducidad
deo_fecpro date,        -- Fecha Producion
deo_incval char(1) not null default 'N',    -- Marcado como produccion (S/N)
deo_valor char(1) not null default 'N',     -- Valorado (S/N)
deo_block char(1) default 'N',  -- Abierto en tactil(S). Cerrado (N) Bloqueado (B) ?
prv_codi int not null,   -- Proveedor
deo_desnue char(1) default 'N' not null,   -- Despiece Nuestro (S/N)
deo_numuni  int  not null default 0,   -- Unidades a meter como origen (Solo tactil)
cli_codi int, -- Cliente para el que se hace el despiece
deo_fecval timestamp,    -- Fecha de Valoracion.
deo_usuval varchar(20),  -- Usuario q hizo valoración
constraint ix_despori primary key(eje_nume,deo_codi)
);
-- insert into desporig select d.eje_nume,deo_codi,tid_codi,deo_fecha,usu_nomb,deo_almori,deo_almdes,deo_ejloge,deo_seloge,deo_nuloge,deo_cerra,0,deo_numdes,g.grd_fecval,g.grd_fecpro,
-- g.grd_incval, grd_valor, grd_block,prv_codi,grd_desnue,grd_unid,grd_fecval,grd_usuval from v_desporig as d,grupdesp as g  where d.eje_nume=2011 and d.eje_nume=g.eje_nume
-- and d.deo_numdes=g.grd_nume and d.emp_codi=g.emp_codi;
--
-- Historico Origen despieces (cabecera)
--
-- drop table deorcahis;
create table anjelica.deorcahis
(
    eje_nume int not null,   -- Ejercicio de Despiece
    deo_codi int not null,   -- Numero de despiece
    tid_codi int,            -- Tipo de despiece
    deo_fecha date,		-- Fecha del despiece
    usu_nomb varchar(20),   -- Usuario que genero el despiece (LOGIN)
    deo_almori int,		-- Almacen de Origen
    deo_almdes int,		-- Almacen destino
    deo_ejloge int,         -- Ejercicio de Lote Generado
    deo_seloge char(1),     -- Serie de Lote Generado
    deo_nuloge int,         -- Numero de Lote generado
    deo_cerra smallint,     -- Cerrado (0: No)
    deo_lotnue smallint,    -- Lote Nuevo (0: No, -1 SI)
    deo_numdes int,         -- Numero Grupo de despiece
    deo_feccad date,        -- Fecha Caducidad
    deo_fecpro date,        -- Fecha Producion
    deo_incval char(1) not null default 'N',    -- Marcado para Valorar (S/N)
    deo_valor char(1) not null default 'N',     -- Valorado (S/N)
    deo_block char(1) default 'N',  -- Abierto en tactil(S). Cerrado (N) Bloqueado (B) ?
    prv_codi int not null,   -- Proveedor
    deo_desnue char(1) default 'N' not null,   -- Despiece Nuestro (S/N)
    deo_numuni int,           -- Numero de Unidades
	cli_codi int, -- Codigo cliente
    deo_fecval timestamp,    -- Fecha de Valoracion.
    deo_usuval varchar(20),  -- Usuario q hizo valoración
    his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
    his_fecha timestamp not null, -- Fecha de Cambio
    his_coment varchar(100), -- Comentario sobre el Cambio
    his_rowid int not null,
    constraint ix_deorcahis primary key (his_rowid)
);
--
-- Lineas despiece origen
--
-- drop table desorilin;
create table anjelica.desorilin
(
eje_nume int not null,   -- Ejercicio de Despiece
deo_codi int not null,   -- Numero de despiece
del_numlin int not null default 1, -- Numero de Linea
pro_codi int,            -- Producto
deo_ejelot int,          -- Ejercicio de Lote
deo_serlot char(1),      -- Serie de Lote
pro_lote int,           -- Numero de Lote
pro_numind int,	        -- Numero de Individuo
deo_prcost float,       -- Precio costo
deo_kilos float,        -- Kilos
deo_preusu float not null default 0,  -- Costo de Usuario (antes de regularizar costos)
deo_tiempo timestamp default current_timestamp, -- Dia y Hora en que se registro la
constraint ix_desorili primary key(eje_nume,deo_codi,del_numlin)
);
create index ix_desorilin1 on anjelica.desorilin(deo_tiempo);
--
-- Cabecera de despiece (Salidas de almacen)
--
create table anjelica.desorihis
(
eje_nume int not null,   -- Ejercicio de Despiece
deo_codi int not null,   -- Numero de despiece
tid_codi int,            -- Tipo de despiece
pro_codi int,            -- Producto
deo_ejelot int,          -- Ejercicio de Lote
deo_emplot int,          -- Empresa de Lote
deo_serlot char(1),      -- Serie de Lote
pro_lote int,           -- Numero de Lote
pro_numind int,		-- Numero de Individuo
deo_numdes int,		-- Numero de Despiece (Grupo)
deo_fecha date,		-- Fecha del despiece
usu_nomb varchar(20),   -- Usuario que genero el despiece
deo_almori int,		-- Almacen de Origen
deo_almdes int,		-- Almacen destino
deo_ejloge int,         -- Ejercicio de Lote Generado
deo_emloge int,         -- Empres Lote generado
deo_seloge char(1),     -- Serie de Lote Generado
deo_nuloge int,         -- Numero de Lote generado
deo_cerra smallint,     -- Cerrado (0: No)
deo_prcost float,       -- Precio costo
deo_kilos float,        -- Kilos
deo_fecalt date default  current_date,
deo_prusu float not null default 0,  -- Costo de Usuario (antes de regularizar costos)
 his_rowid int not null
);
-- insert into desorilin select d.eje_nume,deo_codi,1, pro_codi, deo_ejelot,deo_serlot,pro_lote,pro_numind,deo_prcost,deo_kilos,deo_preusu from v_desporig as d
-- where d.eje_nume=2011 ;
-- drop view v_despori;
--
-- Tabla historico Origen despieces (Lineas)
-- Su equivalente es desorilin
-- drop table deorlihis;
create table anjelica.deorlihis
(
eje_nume int not null,   -- Ejercicio de Despiece
deo_codi int not null,   -- Numero de despiece
del_numlin int not null, -- Numero de Linea
pro_codi int,            -- Producto
deo_ejelot int,          -- Ejercicio de Lote
deo_serlot char(1),      -- Serie de Lote
pro_lote int,           -- Numero de Lote
pro_numind int,		-- Numero de Individuo
deo_prcost float,       -- Precio costo
deo_kilos float,        -- Kilos
deo_preusu float,
deo_tiempo timestamp default current_timestamp,
his_rowid int not null
);
-- 
-- Lineas de despiece (Producto salida). Entrada a almacen
-- 
-- drop table v_despfin;
create table anjelica.v_despfin
(
eje_nume int not null,	-- Ejercicio del Despiece
emp_codi int not null,	-- Empresa  (Deprecated)
deo_codi int not null,	-- Numero de Desp.
def_orden int not null, -- Numero Orden (Para desp. Agrupados)
pro_codi int,		-- Producto
def_ejelot int,		-- Ejercicio del Lote Entrada
def_emplot int,		-- Empresa del Lote Entrada @deprecated
def_serlot char(1),	-- Serie del Lote Entrada
pro_lote int,		-- Numero de Lote Entrada
pro_numind int,		-- Numero de Individuo
def_numpie int,		-- Numero de Piezas
def_tippes char(1),	-- Tipos de Despiece
def_numdes int,  	-- Grupo de despiece (une con grupdesp)
alm_codi int,		-- Almacen
usu_nomb varchar(20),	-- usuario que genera el desp.
def_unicaj smallint not null, -- Numero de Articulos en caja.
def_cerra smallint not null, -- Cerrado
def_prcost float,	-- Precio de Costo
def_kilos float,	-- Kilos
def_feccad date,	-- Fecha. Caducidad
def_preusu float not null default 0,     -- Precio de Costo introducido por el usuario
def_tiempo timestamp default  current_timestamp, -- Fecha de Movimiento.
def_tippro char(1) not null default 'N' -- Tipo Produccion
);
create index  ix_despfin  on anjelica.v_despfin (eje_nume,deo_codi,def_orden);
create index ix_despfin1 on anjelica.v_despfin(pro_codi, def_ejelot , def_emplot , def_serlot , pro_lote , pro_numind);
create index ix_despfin2 on anjelica.v_despfin(def_tiempo);
--
-- Historico Lineas (entrada a almacen) de despiece (Cambios en v_despfin)
---
create table anjelica.desfinhis
(
eje_nume int not null,	-- Ejercicio del Despiece
emp_codi int not null,	-- Empresa
deo_codi int not null,	-- Numero de Desp.
def_orden int not null, -- Numero Orden (Para desp. Agrupados)
pro_codi int,		-- Producto
def_ejelot int,		-- Ejercicio del Lote Entrada
def_emplot int,		-- Empresa del Lote Entrada
def_serlot char(1),	-- Serie del Lote Entrada
pro_lote int,		-- Numero de Lote Entrada
pro_numind int,		-- Numero de Individuo
def_numpie int,		-- Numero de Piezas
def_tippes char(1),	-- Tipos de Despiece
def_numdes int,  	-- Grupo de despiece (une con grupdesp)
alm_codi int,		-- Almacen
usu_nomb varchar(20),	-- usuario que genera el desp.
def_unicaj smallint not null, -- Numero de Articulos en caja (no se usa).
def_cerra smallint not null, -- Cerrado
def_prcost float,	-- Precio de Costo
def_kilos float,	-- Kilos
def_feccad date,	-- Fecha. Caducidad
def_preusu float not null default 0,     -- Precio de Costo introducido por el usuario
def_tippro char(1) not null default 'N' ,
def_tiempo date default  current_date,
 his_rowid int not null
);
--
-- Motivos de Regularizacion
---
-- drop table v_motregu;
create table anjelica.motregu
(
    tir_codi int not null,		-- Codigo de la Regularizacion
    tir_nomb varchar(50),           -- Descripcion de la Regularizacion
    tir_afestk varchar(1) not null,	-- Como afecta al Stock
                                    -- (+ Suma, - Resta, = Inventario, * No Afecta)
    tir_tipo varchar(2) default '--',-- Tipo Regul. Definido en clase pdmotregu (VS: Vert. Sala,
                                           VC: Vert.Cliente, VP: Vert. Proveedor,MC: Merma Cliente)
  constraint ix_motregu primary key (tir_codi)
);
grant select on anjelica.v_motregu to public;
insert into anjelica.motregu values(5,'NO AFECTA STOCK','*','');
create view anjelica.v_motregu as select * from anjelica.motregu;
--
-- Motivos de Regularizacion FIJOS (Codigo < 20)
--
INSERT INTO v_motregu VALUES (1, 'VERTEDERO SALA', '-', '');
INSERT INTO v_motregu VALUES (2, 'RECLAMACION PROVEEDOR', '-', 'VP');
INSERT INTO v_motregu VALUES (3, 'REGULARIZACIÓN POSITIVA DE EXISTENCIAS', '+', '');
INSERT INTO v_motregu VALUES (4, 'DIFERENCIA INVENTARIO', '+', '');
INSERT INTO v_motregu VALUES (6, 'INVENTARIO', '=', '');
INSERT INTO v_motregu VALUES (12, 'VERTEDERO CLIENTE', '-', 'VC');
INSERT INTO v_motregu VALUES (13, 'VERTEDERO PROVEEDOR', '-', 'VP');
INSERT INTO v_motregu VALUES (15, 'VERTEDERO  CLIENTE CALLE', '-', 'VC');
INSERT INTO v_motregu VALUES (16, 'VERTEDERO SALA CALLE', '-', '');
INSERT INTO v_motregu VALUES (19, 'VERTEDERO CONGELADORA', '-', '');
--
-- Datos de despices (Lineas).
-- En esta tabla se guardan los productos generados en los
-- despieces.
--
---
--- Maestro tipo de Despieces (cabecera)
--- OBSOLETA --- NO SE USA
---
-- create table anjelica.v_despcab
-- (
-- tid_codi int not null,
-- tid_nomb varchar(50),
-- tid_tipo varchar(1),
-- pro_codi int,
-- tid_numdes int,
-- tid_feulca date,
-- tid_impgas smallint,
-- tid_perema smallint,
-- tid_tipcla char(1),
-- tid_ndagr int,
-- tid_usaeti smallint,
-- tid_salaut smallint,
-- tid_comen varchar(255),
-- tid_vepebr smallint,
-- tid_peso float,
-- tid_perepr float,
-- tid_pereac float,
-- tid_prrepr float,
-- tid_prreac float,
-- constraint ix_despcab primary key(tid_codi)
-- );
---
-- Tipo de Despieces (Lineas)
--- OBSOLETA --- NO SE USA
---
-- drop table v_desplin;
-- create table anjelica.v_desplin
-- (
 -- tid_codi int not null,
 -- dpl_nume int not null,
 -- pro_codi int,
 -- dpl_numpie int,
 -- dpl_tippes char(1),
 -- dpl_numdes int,
 -- dpl_resmaq varchar(64),
 -- dpl_prbase boolean,
 -- dpl_pesest float,
 -- dpl_kgtot float,
 -- dpl_perepr float,
 -- dpl_pereac float,
 -- dpl_prrepr float,
 -- dpl_prreac float,
 -- dpl_impcos float,
 -- dpl_cotrpr float,
 -- dpl_cotrac float,
 -- dpl_tolinf float,
 -- dpl_tolsup float,
 -- dpl_factor float,
 -- constraint ix_desplin primary key (tid_codi,dpl_nume)
-- );
--
-- Tabla de Precios Pre-Valorados para despieces
--
-- drop table desproval;
create table anjelica.desproval
(
    emp_codi int not null, -- Deprecated
    eje_nume int not null,
    dpv_nusem int not null, -- Numero de Semana
    pro_codi int not null,
    dpv_preci decimal(6,2) not null, -- Precio Final
	dpv_preori decimal(6,2) not null -- Precio original 
	dpv_preval decimal(6,2) not null -- Precio Valoracion
);
create index ix_desproval on desproval(eje_nume,dpv_nusem);
--
-- Datos Grupos de Despieces
--
-- drop table grupdesp;
create table anjelica.grupdesp
(
	emp_codi int not null, -- Deprecated
	eje_nume int not null,
	grd_nume int not null, -- Une con el deo_numdes en desporig
	grd_serie char(1) not null,
	grd_kilo decimal(8,2) not null, -- Kilos
	grd_unid int not null,          -- Unidades
	grd_prmeco decimal(8,2) not null, -- Precio Medio de Compra
	grd_incval char(1) not null,      -- Marcado para Valorar (S/N)
	grd_valor char(1) not null, -- Valorado (S/N)
	grd_block char(1) default 'N',  -- Abierto en tactil(S). Cerrado (N) Bloqueado (B) ?
	grd_feccad date, -- Fecha de Caducidad
	prv_codi int not null,
	grd_desnue char(1), -- Despiece Nuestro (S/N)  
    grd_fecpro date, -- Fecha de Produccion
    grd_fecha date default current_date, -- Fecha del grupo.
    constraint ix_grupdesp primary key (eje_nume,grd_nume)
);
--
--
--
-- Historico grupos de Grupos de despieces
--
create table anjelica.grudeshis
(
	eje_nume int not null,
	grd_nume int not null, -- Une con el deo_codi en desporig
	grd_serie char(1) not null,
	grd_kilo decimal(8,2) not null, -- Kilos
	grd_unid int not null,          -- Unidades
	grd_prmeco decimal(8,2) not null, -- Precio Medio de Compra
	grd_incval char(1) not null,      -- Marcado para Valorar (S/N)
	grd_valor char(1) not null, -- Valorado (S/N)
	grd_block char(1) default 'N',  -- Abierto (S) . Cerrado (N) Bloqueado (B) ?
	grd_feccad date, -- Fecha de Caducidad
	prv_codi int not null,
	grd_desnue char(1), -- Despiece Nuestro (S/N)
        grd_fecval timestamp,      -- Fecha de Valoracion.
        grd_usuval varchar(20), -- Usuario q hizo valoración
        grd_fecpro date, -- Fecha de Produccion
        his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
        his_fecha timestamp not null, -- Fecha de Cambio
        his_comen varchar(100), -- Comentario sobre el Cambio
        his_rowid int not null
);
--
-- Tipos de Tarifa
-- drop table tipotari;
create table anjelica.tipotari
(
tar_codi int not null,		   -- Codigo de Tarifa
tar_nomb varchar(45) not null, -- Descripcion de la tarifa
tar_tipo char(1) not null,     -- 'N' No poner Precios Aut. 
							   --  'S" Poner precios Aut.
tar_codori int not null default 0,    -- Tarifa Original (0: No depende de nadie)
tar_incpre float not null default 0,   -- Se Incrementara precio sobre tar_codori (Cant. Fija, no en porcentaje)
constraint ix_tipotari primary key (tar_codi)
);
---
-- Tablas de Tarifas
---
-- drop table tarifa;
create table anjelica.tarifa
(
tar_fecini date not null,
tar_fecfin date not null,
tar_codi int not null, -- Codigo de Tarifa
tar_linea int not null, -- Linea de tarifa
pro_codart varchar(15) not null, -- Codigo de Articulo
pro_nomb VARCHAR(50) not null, -- Descripcion del Articulo
tar_preci decimal(10,2),
tar_comrep float default 0 not null,   -- Comision Representantes
tar_comen varchar(150),
tar_grupo smallint not null, -- Grupo de familia  (solo se usa cuando pro_codart='')
tar_tipo smallint not null  -- Tipo Animal (solo se usa cuando pro_codart='X')
);
create  index ix_tari1 on anjelica.tarifa(tar_fecini,tar_fecfin,tar_codi,pro_codart);
---
-- Tarifas para clientes
---
-- drop table taricli;
create table anjelica.taricli
(
tar_fecini date not null,
tar_fecfin date,	-- 
cli_codi int not null, -- Codigo de Tarifa
tar_linea int not null, -- Linea de tarifa
pro_codart varchar(15) not null, -- Codigo de Articulo
pro_nomb VARCHAR(50) not null, -- Descripcion del Articulo
tar_preci decimal(10,2),
tar_comrep float default 0 not null,   -- Comision Representantes
tar_comen varchar(150),
constraint ix_taricli primary key (tar_fecini,cli_codi,pro_codart)
);
grant all on  taricli to public;
---
--- Maestro de  Almacenes
---
-- drop table v_almacen;
create table anjelica.almacen
(
alm_codi int not null,		-- Codigo de Almacen
alm_nomb varchar(50),		-- Nombre de Almacen
alm_tipo varchar(1) default 'I' not null ,-- (I)nterno (E)xterno
alm_direc varchar(40),		-- Direccion
alm_pobl varchar(40),		-- Poblacion
alm_codpos int,			-- Codigo Postal
alm_telef varchar(15),		-- Telefono
alm_fax varchar(15),		-- Fax
alm_respo varchar(30),		-- Responsable
emp_codi smallint not null,	-- Empresa
sbe_codi smallint not null,	-- Subempresa a la que pertenece el Almacen
alm_feulin date,			-- Fecha Ultimo Inventario.
constraint ix_almacen primary key (emp_codi,alm_codi)
);
create view v_almacen as select * from almacen ;
grant select on  v_almacen to public;
--
-- Tabla de Empresas
--
--drop table empresa;
create table anjelica.empresa
(
 emp_codi int not null,  -- Codigo de empresa
 emp_nomb varchar(40),	 -- Nombre de Empresa
 emp_dire varchar(40),	 -- Direccion Empresa
 emp_pobl varchar(30),	 -- Poblacion Empresa
 emp_codpo varchar(15),	    	 -- Codigo Postal
 emp_telef varchar(15),	 -- Telefono
 emp_fax varchar(15),	 -- FAX
 emp_nif varchar(12),	 -- NIF
 emp_empaso int,	     -- Empresa Asociada (SIN USAR)
 emp_loclcc int not null default 9,    -- Longitud cuenta contable cliente
 emp_codint varchar(15), -- Codigo Interno (SIN USAR)
 emp_nurgsa varchar(12), -- Codigo Numero Registro Sanitario
 emp_nomsoc varchar(30), -- Nombre Social
 pai_codi int,	    	 -- Pais por defecto
 emp_orgcon varchar(15), -- Organismo de Control.
 emp_cercal varchar(15), -- Certificacion Calidad
 emp_labcal varchar(10), -- Etiqueta Calidad
 emp_obsfra varchar(255),--Observaciones Factura
 emp_obsalb varchar(255),--Observaciones Albaran
 emp_vetnom varchar(50), --Nombre Veterinario
 emp_vetnum varchar(15), --Numero Veterinario
 emp_numexp varchar(15), --Numero Explotacion
 emp_codcom int,         -- Comunidad de Empresa
 emp_codpvi int,    	 -- Provincia de Empresa
 emp_divimp int,         -- Divisa de Importacion
 emp_divexp int,         -- Divisa de Exportación
 emp_desspr varchar(50), -- Destino Subproductos
 emp_codedi varchar(17), -- Codigo EDI
 emp_regmer varchar(70), -- Registro Mercantil
 emp_dirweb varchar(100), -- Direccion web de la empresa 
 primary key (emp_codi)
);
INSERT INTO anjelica.empresa VALUES (1, 'Anjelica, S.L.', 'C/ SOFTWARE LIBRE', 'LOGRO�O', 26006, '555-12345', '555-54321', 'Z12345', 91, NULL, '', '12345/LO', '0', 11, '', '', '', '', 'S.L. FOR PRESIDENT', '', '', '', 16, 26, 1, 1, '', '', '');
CREATE or replace VIEW anjelica.v_empresa as select emp_codi,emp_nomb ,	 -- Nombre de Empresa
 emp_dire,	 -- Direccion Empresa
 emp_pobl,	 -- Poblacion Empresa
 emp_codpo ,	    	 -- Codigo Postal
 emp_telef ,	 -- Telefono
 emp_fax ,	 -- FAX
 emp_nif ,	 -- NIF
 emp_loclcc -- Longitud campo cliente en contablidad
 emp_nurgsa , -- Codigo Numero Registro Sanitario
 emp_nomsoc , -- Nombre Social
 pai_codi ,	    	 -- Pais por defecto
 emp_orgcon , -- Organismo de Control.
 emp_cercal , -- Certificacion Calidad
 emp_labcal , -- Etiqueta Calidad
 emp_obsfra ,--Observaciones Factura
 emp_obsalb ,--Observaciones Albaran
 emp_vetnom , --Nombre Veterinario
 emp_vetnum , --Numero Veterinario
 emp_numexp , --Numero Explotacion
 emp_codcom ,         -- Comunidad de Empresa
 emp_codpvi ,    	 -- Provincia de Empresa
 emp_divimp ,         -- Divisa de Importacion
 emp_divexp ,         -- Divisa de Exportación
 emp_desspr , -- Destino Subproductos
 emp_codedi , -- Codigo EDI
 emp_regmer , -- Registro Mercantil
 emp_dirweb , -- Direccion web de la empresa 
 cop_nombre as emp_nomprv
 from anjelica.empresa left join   anjelica.prov_espana on substring(emp_codpo from 1 for 2)  = cop_codi;
 grant select on anjelica.v_empresa to public;
--
-- Numeraciones
--
-- drop table v_numerac;
create table anjelica.v_numerac
(
 emp_codi int not null,
 eje_nume int not null,
num_serieA int, -- Numero Albaran Ventas de Serie A
num_serieB int, -- Numero Albaran Ventas de serie B
num_serieC int, -- Numero Albaran Ventas de serie C
num_serieD int, -- Numero Albaran Ventas de serie D
num_serieX int, -- Numero Albaran Ventas de serie X
num_pedid int,	-- Numero Pedido de Ventas
num_factur int, -- Numero Fact. de Ventas serie A
num_factub int not null default 0, -- Numero Fact. de Ventas serie B
num_factuc int not null default 0, -- Numero Fact. de Ventas serie C
num_factud int not null default 0, -- Numero Fact. de Ventas serie D
num_secomA int, -- Numero Alb. Compras de Seria 'A'
num_secomB int, -- Numero Alb. Compras de Seria 'B'
num_secomC int, -- Numero Alb. Compras de Seria 'C'
num_secomD int, -- Numero Alb. Compras de Seria 'D'
num_faccom int, -- Factura de Compras
num_pedcom int, -- No Pedido Compras.
num_remesa int, -- Numero de Remesa
num_recibo int,
num_despi int,  -- Numero Despiece
num_serieY int,  -- Numero Albaran Ventas Serie Y (Trasp. Entre SubEmpresas)
num_secomY int , -- Numero Alb. Compras Serie Y
num_prec2 int,
num_prec3 int,
num_prec4 int,
num_prec5 int,
num_iddesp int,
num_indiv2 int,
num_indiv3 int,
num_indiv4 int,
num_lotdes int, -- No UTILIZADO
num_divis int,
num_sefral smallint not null default 0, -- Serie de fra Ventas = Ser. Alb.Ventas (0=NO)
constraint ix_numerac primary key(emp_codi,eje_nume)
);
--
-- Tablas de Cobros
--
-- drop table v_cobros;
create table anjelica.v_cobros
(
 cob_ano int, -- Ejercicio del Cobro (NO de la Fra o Alb.)
 emp_codi int, -- Emp. de la Fra/Alb
 cob_serie char(1), -- Serie Alb (Z si es Fra)
 alb_nume int, -- Albaran
 cob_anofac int, -- Año de la Fra./Albaran
 fvc_serie char, -- Serie de Factura
 fac_nume int, -- Factura
 tpc_codi int, -- Tipo de Cobro
 rem_ejerc int, -- Ejercicio de Remesa
 rem_codi int, -- Numero de Remesa que genero el cobro
 usu_nomb varchar(20),
 cob_feccob date,
 cob_horcob time,
 cob_obser varchar(30),
 cob_trasp int,
 cob_fecvto date,
 cob_impor float,
 cob_albar char(7)  -- Serie Alb,Nume. Alb. (X005100)
);
create index ix_cobro on v_cobros (emp_codi,cob_ano,cob_serie,alb_nume);
create index ix_cobro1 on v_cobros (emp_codi,cob_anofac,fac_nume);
---
-- Tipos de Cobro
--
--drop table v_cobtipo;
create table anjelica.v_cobtipo
(
	tpc_codi int not null, -- Codigo de Tipo Cobro
	tpc_nomb varchar(40),  -- Descripcion
	tpc_comi float,        -- No se usa
	tpc_cuecon varchar(12),-- No se usa
	tpc_giro char(1),		-- Es TIPO giro (S/N)
 primary key (tpc_codi)
);
--
-- Tipos de IVA
--
-- drop tables tiposiva;
create table anjelica.tiposiva
(
tii_codi int not null, -- Tipo de IVA
tii_ctaiva varchar(12), -- Cuenta de IVA
tii_ctaree varchar(12), -- Cuenta de Rec. Equival.
tii_ctairv varchar(12), -- Cuenta de IRPF Ventas
tii_ctaivc varchar(12), -- Cuenta de IVA Compras
tii_ctarec varchar(12), -- Cuenta Rec. Equ. Compras
tii_ctairc varchar(12), -- Cuenta IRPF Compras
tii_iva float, -- Porcentaje de IVA
tii_rec float, -- Porcentaje de Rec. Equ.
tii_irpf float, -- Porcentaje de IRPF
tii_fecini date not null, -- Fecha Inicio Validez
tii_fecfin date not null, -- Fecha Final Validez (Inclusive)
constraint ix_tiposiva primary key(tii_codi,tii_fecini,tii_fecfin)
);
--
-- Tabla relacion Detalles albaran Compra con Productos de venta
-- Utilizado para Albaranes serie Y.
-- @deprecated
--
create table anjelica.v_albcopave
(
   acc_ano int not null,	-- Ejerc. del Alb. de Compra
   emp_codi int not null,	-- Empresa del Alb. de Compra
   acc_serie char(1) not null,	-- Serie del Alb. de Compra
   acc_nume int not null,	-- Numero del Alb. de Compra
   acl_nulin int not null,	-- Numero Linea Albaran
   acp_numlin int, 		-- Numero de Linea del Detalle
   pro_codi int not null,	-- Producto de Albaran Venta
   avp_ejelot int,		-- Ejercicio Lote
   avp_serlot char(1), 		-- Serie de Lote
   avp_numpar int,		-- Numero de Partida (Lote)
   avp_numind int,		--  Numero de Individuo
constraint ix_albcopave primary key(acc_ano,emp_codi,acc_serie,acc_nume,acl_nulin,acp_numlin)
);
--
-- Facturas llevadas a las rutas
--
-- drop table factruta;
create table anjelica.factruta
(
cor_fecha date not null,
usu_nomb varchar(15) not null,
rut_codi varchar(2) not null, -- Modif. para que cargue rutas, en vez de zonas
cor_orden int not null,
fvc_ano int not null,
emp_codi int not null,
fvc_serie char not null,
fvc_nume int not null,
fvc_sumtot decimal(8,2) not null,
fvc_imppen decimal(8,2) not null,
fvc_impcob decimal(8,2) not null,
cor_tipcob char(1) not null, -- Tipo Cobro (Talon,Efectivo,Pagare)
cor_fecvto date, --
cor_coment varchar(255),
cor_intcob char(1) NOT NULL, -- Introducido en cobros (S/N)
cor_totcob char(1) not null -- Totalmente cobrado (S/N)
);
--
-- Tabla Cabecera Albaranes de ruta.
--
drop view anjelica.v_albruta;
drop view anjelica.v_cobruta;
drop table anjelica.albrutacab;
create table anjelica.albrutacab
(
	alr_nume serial not null, -- ID
	rut_codi char(2) not null, -- Ruta
	usu_nomb varchar(15) not null, -- Usuario o transportista
	alr_fecha date not null, -- Fecha de Registro
	alr_fecsal TIMESTAMP not null, -- Fecha Salida Ruta.
	alr_fecreg TIMESTAMP , -- Fecha Regreso ruta
	veh_codi int, -- Vehiculo en el que se hace el transporte 
	alr_vekmin int, -- km. Iniciales
	alr_vekmfi int, -- km. Finales
	alr_impgas float not null default 0, -- Importe gasolina
	alr_coment varchar(255), -- Comentarios sobre ruta.	
	alr_cerrad smallint not null default 0 -- Cerrada (0=NO)
 );
 create index ix_albrutacab on  anjelica.albrutacab(alr_fecha);
 --
-- Tabla Cabecera Lineas de ruta.
--
 drop view anjelica.v_albruta;
 drop table anjelica.albrutalin;
create table anjelica.albrutalin
(
	alr_nume int not null, -- ID
	alr_orden int not null, -- Orden de carga
	avc_id int not null,  -- ID. Albaran
	alr_bultos smallint not null, -- Bultos
	alr_palets smallint not null default 1, -- Palets
	alr_kilos float not null, -- Kilos de Albaran
	alr_unid int not null, -- Unidades de Albaran
	alr_horrep varchar(50), -- horario reparto
	alr_comrep varchar(80), -- Comentario Reparto
	cli_nomen varchar(50) not null, -- Nombre Cliente 
	cli_diree varchar(100) not null, -- Direccion entrega
	cli_poble varchar(50) not null, -- Poblacion entrega
	cli_codpoe varchar(8), -- Cod. Postal Envio
	alr_repet smallint not null default 0, -- Repetido
	constraint ix_albrutalin primary key (alr_nume,alr_orden)
);
create index ix_albrutali1 on anjelica.albrutalin(avc_id);
create or replace view v_albruta as select c.*,l.alr_orden,l.avc_id,alr_bultos,alr_palets,
alr_unid,alr_kilos,alr_horrep,alr_comrep,cli_nomen,cli_diree,cli_poble,cli_codpoe,alr_repet,
al.emp_codi,al.avc_ano,al.avc_serie,al.avc_nume,al.cli_codi,al.avc_clinom,al.avc_kilos,
al.avc_unid 
from anjelica.albrutacab as c, anjelica.albrutalin as l,anjelica.v_albavec as al 
where c.alr_nume=l.alr_nume and al.avc_id = l.avc_id;
grant select on v_albruta to public;

--
--
--
drop view anjelica.v_cobruta;
drop table anjelica.cobrosruta;
create table anjelica.cobrosruta
(
	alr_nume int not null, -- ID
	cru_orden int not null, -- Orden en que se metieron las lineas
	avc_id int,  -- ID. Albaran 
	fvc_id int,  -- ID. Factura	
	cru_impdoc decimal(8,2) not null,	-- Importe a cobrar
	cru_impcob decimal(8,2),   -- Importe Cobrado
	cru_tipcob char(1), --  Tipo Cobro (Talon,Efectivo,Pagare)
	cru_fecvto date, -- Vto (para talones/pagares)
	cru_coment varchar(255), -- Comentario
	cru_intcob smallint NOT NULL default 0, -- Introducido en cobros (0=No)
        cru_totcob smallint not null default 0, -- Totalmente cobrado (O=NO)
	constraint ix_cobrosRuta primary key (alr_nume,cru_orden)
);

create or replace view anjelica.v_cobruta as select c.*,l.cru_orden,l.fvc_id,cru_impdoc as fvc_imppen,
l.cru_impcob,
fr.emp_codi,fr.fvc_ano,fr.fvc_serie,fr.fvc_nume,fr.cli_codi,fr.fvc_clinom,fvc_fecfra,fvc_sumtot
from albrutacab as c, cobrosruta as l,v_facvec as fr
where c.alr_nume=l.alr_nume and fr.fvc_id = l.fvc_id;
grant select on v_cobruta to public;
---
-- Tabla vehiculos
---
create table vehiculos
(
	veh_codi int not null, -- Codigo de Vehiculo
	veh_nomb varchar(40) not null, -- Nombre de vehiculo
	veh_marca varchar(25), -- Marca
	veh_modelo varchar(25), -- Modelo
	veh_matri varchar(15), -- Matricula
	veh_anocom int, -- Año de Compra.
	veh_kilome int, -- Kilometros actuales	
	constraint ix_vehiculos primary key (veh_codi)
);
grant select on vehiculos to public;
insert into vehiculos values(99,'PROPIO','','','',0,0);
--
-- MATADEROS
--
-- drop table v_matadero;
create table anjelica.v_matadero
(
 mat_codi int not null,		-- Codigo de Matadero
 mat_nomb VARCHAR(50),		-- Nombre de Matadero
 mat_direc  VARCHAR(50),	-- Direccion
 mat_pobl VARCHAR(50),		-- Poblaccion
 mat_provi VARCHAR(50),		-- Provincia
 mat_codpos VARCHAR(15),	-- Codigo postal
 mat_telef VARCHAR(15), 	-- Telefono
 mat_fax  VARCHAR(15),		-- Fax
 mat_nif VARCHAR(20),		-- NIF
 mat_nuexpl VARCHAR(15),	-- Numero de Explotacion
 mat_nrgsa  VARCHAR(12),	-- Num. Reg. Sanitario
 mat_comen  VARCHAR(50),	-- Observaciones
 pai_codi int,			-- Pais
 mat_codcom int,		-- Cod. Comunidad  (NO USADO)
 mat_codprov int,		-- Cod. Provincia  (NO USADO)
 mat_orgcon  VARCHAR(15),	-- Organismo Control
 mat_cercal  VARCHAR(15),	-- Certificacion Calidad (NO USADO)
 mat_etical  VARCHAR(15)	-- Etiqueta de Calidad (NO USADO)
);
create unique index ix_matadero on v_matadero(mat_codi);
--
-- Salas de Despiece
--
-- drop table v_saladesp;
create table anjelica.v_saladesp
(
 sde_codi int not null, -- Codigo de Sala de Desp.
 sde_nomb varchar(50),
 sde_direc varchar(50),
 sde_pobl  varchar(50),
 sde_provi varchar(50) ,
 sde_codpos varchar(15),
 sde_telef varchar(15),
 sde_fax  varchar(15),
 sde_nif  varchar(20),
 sde_nuexpl varchar(15),
 sde_nrgsa  varchar(12), -- Numero de Registro Sanitario
 sde_comen varchar(50),
 pai_codi int, -- Pais de Sala de Despiece
 sde_codcom int,
 sde_codprov int,
 sde_orgcon varchar(15),
 sde_cercal varchar(15),
 sde_etical varchar(10),
 constraint ix_saladesp primary KEY (sde_codi);
);

--
-- Paises
--
-- drop table v_paises;
create table anjelica.paises
(
 pai_codi int not null,		-- Codigo de Pais
 pai_inic VARCHAR(2) not null,  -- Iniciales de Pais
 pai_nomb VARCHAR(50) not null,	-- Nombre de Pais
 pai_nomcor VARCHAR(5) not null,  -- Nombre corto del Pais (Para trazabilidad)
 pai_estcro VARCHAR(30),	-- Estructura Crotal (NO USADO)
 pai_coisa INT, 		-- CODIGO ISO-AECOC (NO Usado)
 pai_activ smallint not null default -1,  -- Activo
 loc_codi varchar(5) not null default 'es_ES', -- Locale del pais
 primary key (pai_codi)
);
create view anjelica.v_paises as select * from paises ;
create unique index ix_paise1 on paises(pai_inic);

-- Tabla con Locales
create table anjelica.locales
(
loc_codi varchar(5) not null, -- Codigo de Locale.
loc_nomb varchar(30) not null, -- Nombre de Locale
primary key (loc_codi)
);
insert into anjelica.locales values('es_ES','Español (España)');
alter table anjelica.paises add constraint loc_codi foreign key (loc_codi)
    references anjelica.locales(loc_codi);	
-- drop table 	 anjelica.articulo_locale
create table anjelica.articulo_locale
(
pro_codi int not null, -- Codigo Producto
loc_codi varchar(5) not null, -- Codigo de Locale.
pro_nomloc varchar(50) not null, -- Nombre de Articulo
primary key (pro_codi,loc_codi)
);


---
-- Proveedor Sala Despiece
---
-- DROP TABLE v_prvsade;
create TABLE v_prvsade
(
prv_codi int not null,   --- Cod. Proveedor
sde_codi int not null	--- Codigo de Sala de Despieze
);
---
-- proveedor MATADEROS
--
create table anjelica.v_prvmata
(
 prv_codi int not null,	-- Cod. Proveedor
 mat_codi int not null	-- Codigo Matadero
);
--
-- Clasificar Lomos para entrada
--
-- drop table claslomos;
create table anjelica.claslomos
(
	prv_codi int not null default 0, -- Afecta a Proveedor (si es 0, afecta a todos)
	cll_codi int not null, -- Codigo de Clasificacion
	pro_codi int not null, -- Codigo de Producto
	cll_kilos float not null -- Kilos Minimos
);
--
-- TABLA TIPOS DESPIECE (CABECERA)
--
-- drop table tipodesp;
create table anjelica.tipodesp
(
	tid_codi int not null, -- Codigo de Despiece
	tid_nomb varchar(50) not null, -- Descripcion Despiece
	tid_activ int not null,   -- Activo: 2, Solo TACTIL:1 Inactivo: 0
	tid_agrup int not null default 0, -- Numero Max.Desp/Dia
	tid_fecalt date not null, -- Fecha Alta
	tid_feulmo date, -- Fecha Ultima Modificación
	usu_nomb varchar(15),     -- Usuario que hizo ultima modificación
	tid_usoequ smallint not null default -1, -- Usar Productos equivalentes
	tid_merven smallint not null default 0, -- Linea Mer Venta para AutoDespiece.
	primary key (tid_codi)
);
create index ix_tipodes1 on tipodesp (tid_nomb);
-- Este tipo de despiece limita al dia actual, el numero de despieces libres al
-- numero puesto en tid_agrup
insert into tipodesp values (9998,'DESPIECE LIBRE',0,5,'20110101',null,null);
--
-- Tabla despieces (Entradas)
---
-- drop table tipdesent;
create table anjelica.tipdesent
(
	tid_codi int not null, -- Codigo Despiece
	tde_nuli int not null, -- No de Linea
	pro_codi int not null,  -- Cod. Producto.
        primary key (tid_codi,tde_nuli)
);
---
-- TABLA TIPOS DESPIECE (Salidas)
---
-- drop table tipdessal;
create table anjelica.tipdessal
(
	tid_codi int not null, -- Codigo de Despiece
	pro_codi int not null, -- Codigo Producto
	tds_unid int not null, -- Nº Unidades a Generar por cada una de la cabecera
        tds_costo float not null, -- Costo deseado (en €)
	tds_grupo int not null -- Grupo del Producto
);
create index ix_tipdessal on tipdessal(tid_codi,pro_codi);
---
-- Articulos excluyentes. Usados para despieces
---
create table anjelica.artiexcl
(
  pro_codini int not null,
  pro_codfin int not null
);
---
-- Articulos Equivalentes.  Usados para despieces
---
create table anjelica.artiequiv
(
  pro_codini int not null,
  pro_codfin int not null
);
---
-- Articulos Equivalentes Congelados.  Usados para despieces
---
create table anjelica.artequcon
(
  pro_codini int not null, -- Codigo Producto en fresco.
  pro_codfin int not null  -- Codigo Producto en Congelado.
);
--
-- Tabla divisas
--
-- drop table v_divisa;
create table anjelica.v_divisa
(
	div_codi int not null,
	div_nomb varchar(50),	-- Nombre de Divisa
	div_nomab varchar(15),	-- Nombre Abreviado
	div_nudepr int, 	-- Numero decimales para precios
	div_nudeim int,		-- Numero de Decimales para Importes
	div_maspre varchar(30), -- Mascara de Precios
	div_masimp varchar(20),	-- Mascara de Importes
	div_color varchar(20),  -- Color de Presentacion
	div_codedi varchar(3),	-- Codigo EDI
	primary key(div_codi)
);
--
-- Cabecera Pedidos de Compras
-- drop table pedicoc;
CREATE TABLE pedicoc
(
  eje_nume int not null,
  emp_codi int NOT NULL,
  pcc_nume int not null,	-- N. Pedido
  prv_codi int not null,	-- Proveedor
  pcc_fecped date not null,	-- Fecha Pedido
  pcc_fecrec date,		-- Fecha entrega
  alm_codi int not null,	-- Almacen Recepcion
  pcc_estad varchar(1) not null,   -- Estado (Pendiente, Confirmado, preFact)
  pcc_estrec varchar(1) not null,-- Estado Recep. (Recep./Cancelado/Pend/Bloqueado)
  pcc_imppor float not null,	-- Importe de Portes por Kilos.
  pcc_comen varchar(255),       -- Comentario sobre el pedido
  acc_ano int,			-- Ejercicio de Albaran de Compra
  acc_serie varchar(1),		-- Serie del Albaran
  acc_nume int,			-- N. Albaran
  acc_cerra int,		-- Cerrado (0 NO -1 Si)
  pcc_subjec varchar(255),	-- Subject para imprimir en el pedido
  pcc_portes char(1),		-- Portes Pagados o Debidos
  sbe_codi smallint not null,   -- SubEmpresa
  pcc_feccon date,			-- Fecha Confirmacion
  pcc_feclis date, -- Fecha en que se listo
  constraint ix_pedicoc primary key(eje_nume,emp_codi,pcc_nume)
);
--
-- Lineas Pedidos de Compras
--
-- drop table pedicol;
CREATE TABLE pedicol(
	eje_nume int not null,
	emp_codi int NOT NULL ,
	pcc_nume int not null,		-- No. Pedido
	prv_codi int not null,		-- Codigo de Proveedor
	pcl_numli int not null,		-- No Linea Pedido
	pro_codi  int not null, 	-- Codigo de Producto.
	pro_nomb varchar(50) not null,	-- Descr. Producto
	pcl_feccad date not null,	-- Fecha de Caducidad
	pcl_nucape int not null,	-- No. de Cajas Pedidas
	pcl_cantpe float not null,	-- Cantidad (kgs) Pedida
	pcl_precpe float not null,	-- Precio pedido
	div_codi int not null,		-- Divisa
	pcl_nucaco int not null,	-- No. Cajas Confirmadas
	pcl_cantco float not null,	-- Cantidad Confirmada
	pcl_precco float not null,	-- Precio Compra Conf.
	pcl_nucafa int not null,	-- No. Cajas Fact.
	pcl_cantfa  float not null,	-- Cantidad Fact.
	pcl_precfa  float not null,	-- Precio Compra Fact.
	pcl_comen varchar(50),		-- Comentario sobre Linea Pedido
	constraint ix_pedicol primary key(eje_nume,emp_codi,pcc_nume,pcl_numli)
);
create or replace view v_pedico as select  c.eje_nume,c.emp_codi,c.pcc_nume, c.prv_codi,pcc_fecped ,
  pcc_fecrec ,  alm_codi ,  pcc_estad , pcc_estrec , pcc_imppor , pcc_comen,
  acc_ano , acc_serie ,  acc_nume , acc_cerra , pcc_subjec , pcc_portes , sbe_codi,
  pcl_numli ,pro_codi ,	pro_nomb ,pcl_feccad,pcl_nucape ,pcl_cantpe,pcl_precpe ,
  div_codi,	pcl_nucaco,	pcl_cantco,	pcl_precco,	pcl_nucafa,	pcl_cantfa,	pcl_precfa ,pcl_comen 
	from pedicoc as c, pedicol as l where
	c.eje_nume=l.eje_nume and
	c.emp_codi=l.emp_codi and
	c.pcc_nume=l.pcc_nume;

  
  
--
-- Facturas Compras Cabecera
--
-- drop table v_facaco;
create table anjelica.v_facaco
(
  eje_nume int not null,	-- Ejercicio
  emp_codi int not null,	-- Empresa
  fcc_nume int not null,	-- Numero de Factura
  prv_codi int not null,	-- Codigo de Proveedor
  fcc_fecfra date,		-- Fecha de Factura
  fcc_desrec varchar(15),	-- Descripcion Recargo
  fcc_sainen int,		-- Saldo Inicial de Envases
  fcc_trasp int,		-- Traspasada
  fcc_facimp int,		-- Impreso
  fcc_facprv varchar(12),	-- Numero Fact. del Proveedor
  fcc_fefapv date,	        -- Fecha de Fact. del Proveedor
  div_codi int,         	-- Divisa
  fpa_codi int,         	-- Forma de Pago
  fcc_sumli2 float,
  fcc_sumto2 float,
  fcc_sumim12 float,
  fcc_sumim22 float,
  fcc_sumim33 float,
  fcc_imiva12 float,
  fcc_imiva22 float,
  fcc_imiva32 float,
  fcc_imrec12 float,
  fcc_imrec22 float,
  fcc_imrec32 float,
  fcc_imirp12 float,
  fcc_imirp22 float,
  fcc_imirp32 float,
  fcc_imrec2 float,
  fcc_baim12 float,
  fcc_baim22 float,
  fcc_baim32 float,
  fcc_tottas float,
  fcc_totta2 float,
  fcc_apltas int not null,
  fcc_tainpr int not null,
  fcc_sumlin float,	-- Suma de Lineas
  fcc_sumtot float,	-- Importe Total
  fcc_impiv1 float,	-- Importe de IVA
  fcc_impiv2 float,
  fcc_impiv3 float,
  fcc_impre1 float,	-- Importe de Recargo de Equivalencia
  fcc_impre2 float,
  fcc_impre3 float,
  fcc_imirp1 float,	-- Importe de IRPF
  fcc_imirp2 float,
  fcC_imirp3 float,
  fcc_imprec float,
  fcc_basim1 float,	-- Importe de base Imponible
  fcc_basim2 float,
  fcc_basim3 float,
  fcc_dtopp float,	-- Dto por Pronto Pago
  fcc_dtocom float,	-- Dto Comercial
  fcc_piva1 float,	-- Porcentaje de IVA
  fcc_piva2 float,
  fcc_piva3 float,
  fcc_prec1 float,	-- Recargo Comercial
  fcc_prec2 float,
  fcc_prec3  float,
  fcc_pirpf1 float,	-- % de IRPF
  fcc_pirpf2 float,
  fcc_pirpf3 float,
  fcc_conpag char(1) not null,-- Conforme Pago (S/N)
  fcc_coment varchar(150),	-- Comentario
  sbe_codi smallint not null,
 constraint ix_facaco primary key (emp_codi,eje_nume,fcc_nume)
);
--
-- Tabla de grupos de facturas de compras
--
-- drop table grufaco
create table anjelica.grufaco
(
 gfc_orige char(1) not null,	-- Origen (Compra,Transportista)
 eje_nume1 int not null,
 emp_codi1 int not null,
 fcc_nume1 int not null,
 eje_nume2 int not null,
 emp_codi2 int not null,
 fcc_nume2 int not null
);
--
-- Lineas  de Facturas de Compras
--
-- drop table v_falico;
create table anjelica.v_falico
(
  eje_nume int not null,
  emp_codi int not null,
  fcc_nume int not null,
  fcl_numlin int not null, -- Numero de Linea de Fra
  fcl_tipdes char(1),	-- Tipo de Descuento
  pro_codi int ,	-- Codigo de Producto
  acc_nume int, 	-- Numero de Albaran
  acc_ano int,		-- Año de Albaran
  acc_serie char(1),	-- Serie de Albaran
  acc_fecalb date,	-- Fecha de Albaran
  fcl_prcom2 float,	-- Precio Compra2 (SIN USAR)
  fcl_dto2 float,
  acl_nulin int,	-- Numero Linea de Albaran
  fcl_tireta char(1),	-- Tipo Recargo Tasa (%)
  fcl_rectas float,	-- Recargo de Tasa
  acl_recta2 float,	-- Recargo de Tasa2
  fcl_canti float,	-- Cantidad
  fcl_prcom float,	-- Precio de Compra
  fcl_dto float not null default '0', 	-- %  Importe Dto
constraint ix_falico primary key (eje_nume,emp_codi,fcc_nume,fcl_numlin)
);
create index ix_falico1 on v_falico(emp_codi,acc_ano,acc_nume,acc_serie);
--
-- Tabla de Inventarios de depositos.
-- Esta tabla es llenada por el traspaso de inventarios, para anotar los productos que estaran en almacen,
-- pero en deposito.
--
create table anjelica.invdepos
(
pro_codi int not null,		-- Codigo Producto
ind_fecha date not null,	-- Fecha de Inventario
eje_nume int  ,			-- Ejercicio de Reg.
emp_codi int ,			-- Empresa del lote
pro_serie char(1),		-- Serie de Producto
pro_nupar int,			-- Numero Partida
pro_numind int,			-- Numero de Individuo
alm_codi int,			-- Almacen
ind_numuni int,         -- Numero de Unidades
ind_kilos float
);
create index ix_invdepos on invdepos(ind_fecha,pro_codi);
--
-- Tabla de Regularizaciones
--
-- drop table regalmacen;
create table anjelica.regalmacen
(
pro_codi int not null,		-- Codigo Producto
rgs_fecha timestamp not null,	-- Fecha y hora de Regularizacion
rgs_nume int not null, 		-- Numero de Regularizacion (Contador)
eje_nume int  ,			-- Ejercicio de Reg.
emp_codi int ,			-- Empresa del lote
pro_serie char(1),		-- Serie de Producto
pro_nupar int,			-- Numero Partida
pro_numind int,			-- Numero de Individuo
tir_codi int,			-- Tipo de Regularizacion
rgs_canti int, 			-- Cantidad de Individuos (No de Piezas)
alm_codi int,			-- Almacen
rgs_recprv smallint not null,	-- Tipo Recl. Prov.. 0 = NO es Reclamado
				-- 1 Pendiente, 2 Aceptado, 3 Rechazado
				-- 4 Reclamacion Pend.
sbe_codi  smallint not null,	-- SubEmpresa
rgs_partid smallint not null,	-- Parte
usu_nomb varchar(20),	-- Usuario que crea la Reg.
rgs_prmeco float,		-- Costo en la fecha Mvto. 
rgs_prulco float,		-- Precio Ultima Entrada.
rgs_prregu float,		-- Precio de Regularizacion
rgs_kilos float,		-- Kilos
rgs_clidev int not null,	-- Cliente q genero la Dev.
rgs_kilant float not null,	-- Kilos Antiguos (NO USADO)
rgs_trasp smallint not null default -1,   -- Traspasado (Indica si influye en Almacen)
rgs_numer varchar(8),		-- Numerador NO USADO
rgs_cliprv int,			-- Codigo Cliente o Proveedor
rgs_coment varchar(100),	-- Comentario
rgs_fecres date,		-- Fecha Resolucion
acc_ano    int,			-- Ejercicio de Alb. Compra
acc_serie  char(1),		-- Serie de Albaran Compra
acc_nume   int,			-- Numero Albaran de Compra.
constraint ix_regstock primary key (rgs_nume)
);
create index ix_regstock2 on regalmacen(pro_codi,rgs_fecha,tir_codi);
create index ix_regstock3 on regalmacen(emp_codi,pro_nupar,pro_serie,eje_nume);
create index ix_regstock4 on regalmacen(rgs_fecha);

create view anjelica.v_regstock as select r.*,  tir_nomb, tir_afestk,  tir_tipo 
from anjelica.regalmacen as r,anjelica.motregu as m 
    where r.tir_codi = m.tir_codi 
    and tir_afestk != '*' and rgs_trasp!=0;  
grant select on anjelica.v_regstock to public;
create view anjelica.v_inventar as select r.* 
from anjelica.regalmacen as r,anjelica.motregu as m 
    where r.tir_codi = m.tir_codi 
    and tir_afestk = '=';  
grant select on anjelica.v_inventar to public;

----
--- Historicos Mensajes
----
-- drop table histmens;
create table  anjelica.histmens
(
        him_codi serial,    -- Numero secuencial usado para razones del usuario
	usu_nomb varchar(20) not null,
	him_fecha date not null, -- Fecha creacion Mensaje.
	him_hora  decimal(4,2) not null, -- Fecha creacion Mensaje.
	men_codi char(2) not null,  -- Codigo de Mensaje
	men_nomb varchar(255) not null  -- Descr. Mensaje
);
create index ix_histmens on histmens(usu_nomb,him_fecha);
--
-- Razones para los mensajes.
-- En esta tabla se guardaran las razones dadas por un usuario para un mensaje
--
create table anjelica.razonmens
(
    him_codi int not null,          -- Codigo de mensaje
    rme_descr varchar(255) not null, -- Descripcion de la razon
    primary key(him_codi)
);
--
-- Tipos de Mensajes
--
-- drop table mensajes;
create table anjelica.mensajes
(
	men_codi char(2) not null,  -- Codigo de Mensaje
	men_nomb varchar(255) not null,  -- Nombre de Mensaje
        men_tipo char(1) not null default 'A', -- Tipo: 'A' Aviso, 'C' Critico, 'I' Informacion
	primary key(men_codi)
);
insert into mensajes values('MS','Sale del Menu %u','I');
insert into mensajes values('EM','Entro en Menu %u','I');
insert into mensajes values('CS','Creado Stock por %u Prod: %p Unid: %U Kilos: %k Lote: %l','C');
insert into mensajes values('MP','Matado proceso %p por %u','A');
insert into mensajes values('ME','Enviada incidencia: %s','C');
insert into mensajes values('LC','Mod. Lin. Compras No encontrado %s','C');
insert into mensajes values('V1','Modificado ALBARAN Ventas No %a con precios ya puestos por %u','I');
insert into mensajes values('V2','BORRADO ALBARAN Ventas No %a por %u','A');
insert into mensajes values('V3','(PDALBVE)Error al Anular stock %s','C');
insert into mensajes values('V4','Anulada ALTA de Alb. Ventas No %a','A');
insert into mensajes values('V5','Camb. Cabec. Albaran %s','I');
insert into mensajes values('V6','Puestos precios a 0 de Prod:  %p ALBARAN %a','I');
insert into mensajes values('V7','Modificado Alb. YA listado. ALBARAN %a','A');
insert into mensajes values('ER','Error al Guardar Mensaje con Cod: %c','C');
insert into mensajes values('C1','Modificado Alb. Compras No: %a con factura: %f','I');
insert into mensajes values('C2','BORRADO ALBARAN Compras No %a','A');
insert into mensajes values('C3','Modif. Fra. Compras No %f','A');
insert into mensajes values('C4','Anulada ALTA de Alb. Compras No: %a','A');
insert into mensajes values('C5','Borrado Frab. Compras No: %a','A');
insert into mensajes values('C6','Borrado Fra. Transp. No: %a','A');
insert into mensajes values('C7','Modif.  Fra. Transp. No: %a','A');
insert into mensajes values('C8','Camb. en compras Ref. %p de ind: %i con ventas','C');
insert into mensajes values('C9','Borrado Albaran Proveedor: %a','C');
insert into mensajes values('CA','Modificado Albaran Proveedor: %a','A');
insert into mensajes values('CB','Cambiada en compras ref. de prod: %p a prod: %x en alb: %a','A');
insert into mensajes values('CC','Modificado Albaran compra en periodo ya cerrado: %a','C');
insert into mensajes values('CD','Modificado Precio Albaran compra en periodo cerrado: %a . Producto: %p','C');
insert into mensajes values('D1','Cancelada ALTA Despiece (PDdesp) %a','A');
insert into mensajes values('D2','Cancelada ALTA Despiece (TACTIL) %a','A');
insert into mensajes values('D3','Baja Despiece (PDdesp) %a','C');
insert into mensajes values('D4','Baja Despiece (TACTIL) Lote: %a','C');
insert into mensajes values('D5','Cerrado Despiece Descuadrado (TACTIL) Lote: %a','A');
insert into mensajes values('D6','Modificado Despiece Valorado  Lote: %a','A');
insert into mensajes values('BL','Anulado Bloqueo en tabla %t Registro %r','C');
insert into mensajes values('F1','Factura Ventas %f Borrada','C');
insert into mensajes values('F2','Factura Ventas %f Modificada','A');
insert into mensajes values('I1','Sobrescribiendo Inventario en Fecha  %f','I');
insert into mensajes values('I2','Borrando registros Inventario de Fecha  %f','I');
insert into mensajes values('I3','Insertado Inventario Fecha  %f.  Almacen %a ','I');
insert into mensajes values('I4','Regenerado Stock Inventario Fecha %f  Almacen %a Camara %c','I');
insert into mensajes values('I5','Mant. Inventario Fecha  %f Almacen %a Producto %p','C');
insert into mensajes values('A1','Stock > 1 Individuo. Almac: %a Prod: %p Indiv: %i','A');
insert into mensajes values('A2','Sin reg. Stock. Almac: %a Prod: %p Indiv: %i','A');
insert into mensajes values('R1','Anulados cobros. Condiciones: %c','A');
insert into mensajes values('EA','Enviado Email a Cliente %c con Albaran: %a','I');
insert into mensajes values('EF','Enviado Email a Cliente %c con Factura: %a','I');
insert into mensajes values('P1','Modificado Pedido Venta con Albaran asignado. Pedido: %p Albaran: %a','I');
--
-- Tabla de Etiquetas  (para trazabilidad y compras)
--
-- drop table etiquetas;
create table anjelica.etiquetas
(
	emp_codi int not null, -- Empresa
	eti_codi int not null, -- Codigo de Etiqueta
	eti_nomb varchar(50) not null, -- Descripcion de etiqueta
	eti_logo varchar(100) not null,  -- Logotipo para la etiqueta
	eti_ficnom varchar(100) not null, -- Fichero conf. de la Etiqueta
	eti_defec char(1) not null,  -- Etiqueta Defecto (S/N)
	eti_nuetpa smallint not null default 1, -- Numero Etiquetas por Pagina
    eti_client smallint not null -- Etiqueta para cliente (0 Todos)
);
INSERT INTO etiquetas (emp_codi,eti_codi,eti_nomb,eti_logo,eti_ficnom,eti_defec,eti_client)
    VALUES (1,1,'ESTANDART','anjelica.png','etiqueta','S',0);

---
--- Tabla de Bloqueos
---
-- drop table bloqueos;
create table anjelica.bloqueos
(
	usu_nomb varchar(15) not null, -- Usuario
	blo_tty varchar(25) not null, -- TTY
	blo_fecha date not null, -- Fecha de Bloqueo
	blo_hora decimal(4,2) not null, -- Hora de Bloqueo
	blo_tabla char(30) not null, -- Tabla que bloquea
	blo_regis char(100) not null -- Registro que bloquea
);
--
-- Tabla de Clientes/Articulos
--
-- drop table v_cliart;
CREATE TABLE v_cliart
(
 cli_codi int not null,		-- Codigo de Cliente
 pro_codi int not null,		-- Codigo de Producto
 pro_nomb varchar(50),		-- Nombre de Producto para Alb/Fra
 cla_obsped varchar(50),	-- Observ. sobre Pedido
 cla_infeti smallint not null,	-- inform. sobre etiqueta (sin USAR)
 cla_codbar varchar(50),	-- Cod. Barras para el cliente
 cla_tipcb int ,		-- TIPO CB
 eti_codi  int,			-- Codigo Formato Etiqueta
 cla_ingred varchar(255),	-- Ingredientes
 cla_deset1 varchar(50),	-- Descrip. Campo 1 de Etiqueta
 cla_deset2 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset3 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset4 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset5 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset6 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset7 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset8 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset9 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_deset10 varchar(50),	-- Descrip  Campo 2 de Etiqueta
 cla_poseti int,		-- Posicion de etiqueta (SIN USO)
 cla_gireti int,		-- Giro de Etiqueta (SIN USO)
 cla_diacon int,		-- Dias de Consumo
 cla_serman char(1) not null,	-- Servir Manual (SIN USO)
 constraint ix_cliart  primary key(cli_codi,pro_codi)
);
----
--- Tabla Bancos
----
-- drop table v_banco;
create table anjelica.v_banco
(
	 ban_codi int not null ,	-- Codigo de banco
	 ban_nomb varchar(30),		-- Nombre de Banco
	 ban_ofici int,			-- Oficina de Banco
	 ban_digito int,		-- Digito de Banco
	 ban_cuenta int,		-- Numero de Cuenta
	 ban_cuetex varchar(15),	-- Cuenta como Texto (NO USADO)
	 cli_codi int not null,		-- Cliente (NO USADO)
	 primary key (ban_codi)
);
---
-- Tabla de Albaranes de Ventas enviados por fax
---
-- drop table albvefax;
create table anjelica.albvefax
(
	cli_codi int not null,           -- Codigo de Cliente
	avc_ano int not null,
	emp_codi int not null,
	avc_serie char(1) not null,
	avc_nume int not null,
	usu_nomb varchar(15) not null,   -- usuario que envio el fax
	avf_numfax varchar(15) not null, -- Numero de fax donde se envio
	avf_jobid int not null,          -- Numero de Trabajo del Fax
	avf_estad char(1) not null,      -- Estado del Fax (Sending,Done,Fallo,Cancel)
	avf_fecha date not null,         -- Fecha de Envio
	avf_hora float not null,         -- Hora de Envio
	avf_msgerr varchar(50),          -- Mensaje De error
	avf_contro char(1) not null,     -- Avisar en caso de Error
	avf_nomfic varchar(50),          -- Nombre de fichero enviado
	avf_desfic varchar(50),          -- Descripcion de Fichero
    avf_tipdoc char(1) not null  default 'A'   -- Tipo Documento (A:Albaran, F:Factura)
);
create unique index ix_albvefax on albvefax(avf_jobid);
--
-- Tabla de Recibos (Giros bancarios)
--
-- drop table v_recibo;
create table anjelica.v_recibo
(
	eje_nume int not null,		-- Ejercicio de la Fra
	emp_codi int not null,		-- Empresa de la Fra.
	fvc_serie char(1) not null default 1, -- Serie de la Fra.
	fvc_nume int not null,		-- Numero de la Fra.
	rec_nume int not null,		-- Numero de Recibo
	rec_fecvto date not null,	-- Fecha de Vto.
	rem_ejerc  int not null,	-- Ejercicio de la Remesa
	rem_codi   int not null,	-- Numero de Remesa (0=sin asignar)
	bat_codi   int not null,	-- Banco de la remesa (0=sin asignar)
	rec_emitid int not null,	-- Emitido disquete
	rec_remant int,
	rec_recagr int,
	rec_impor2 float,
	rec_import float,		-- Importe del recibo
	ban_codi int not null,		-- Banco
	rec_baofic int not null,	-- Oficina del Banco
	rec_badico int not null,	-- Digito de control
	rec_bacuen float not null,	-- Numero de Cuenta
 constraint ix_recibo primary key (eje_nume,emp_codi,fvc_serie,fvc_nume,rec_nume)
);
--
-- Bancos de Tesoreria
--
-- drop table bancteso;
create table anjelica.bancteso
(
 bat_codi int not null,			-- Codigo de Banco
 bat_nomb varchar(50) not null,		-- Nombre de banco
 bat_nomsuc varchar(50) not null,	-- Nombre de Sucursal
 bat_titcue varchar(50) not null,	-- Titular de la Cuenta
 bat_nif    varchar(9) not null,	-- NIF del Titular de la Cuenta
 bat_nifsuf int  not null,	        -- Sufijo del titular de la cuenta
 bat_cucoba int  not null,		-- Cuenta Banco (4 digitos)
 bat_cusuba int  not null,		-- Cuenta Sucursal Banco (4 digitos)
 bat_digico int  not null,		-- Digito Control (2 digitos)
 bat_numcue float not null,		-- Numero de Cuenta (10 digitos)
 bat_cuecon varchar(12)  not null,	-- Cuenta Contable
 bat_limcre float,			-- Limite de Credito
 bat_nudiim int,			-- N� Dias en Com. Impagados (NO USADO)
 primary key (bat_codi)
);
--
-- Fichero de Remesas
--
-- drop table remesas;
create table anjelica.remesas
(
 eje_nume int not null,			-- Ejercicio de Remesa
 rem_codi int not null,			-- Codigo de la Remesa
 rem_fecha date not null,		-- Fecha de la Remesa
 bat_codi int not null,			-- Banco de la Remesa
 rem_come varchar(100),			-- Comentario sobre la Remesa
 rem_import float not null,		-- Importe de la Remesa
 rem_numfra int not null,		-- N� Fras que tiene la remesa
 rem_fecpre date,			-- Fecha de Presentacion
 rem_fecade date,			-- Fecha de Adeudo
 rem_direc varchar(150),		-- Directorio de Adeudo
 rem_norma char(4),			-- Norma
 rem_fordos char(1),			-- Formato D.O.S (S/N)
 bat_nifsuf int,			-- Sufijo del Banco
 primary key (eje_nume,rem_codi)
);
--
-- Tabla de Provincias
--
-- drop table v_provincia;
create table anjelica.v_provincia
(
 pai_codi int not null, -- Codigo de Pais
 pvi_codi int not null, -- Codigo de Provincia
 pvi_nomb varchar(255), -- Nombre de Provincia
 primary key(pai_codi,pvi_codi)
);
--
-- Cabecera de transportistas
--
-- drop table transportista;
create table anjelica.transportista
(
 tra_codi int not null,         -- Codigo de Transportista
 tra_nomb varchar(40) NOT NULL, -- Nombre del Transportista
 tra_direc varchar(40),		-- Direccion del Transportista
 tra_pobl varchar(30), 		-- Poblacion del transportista
 tra_codpos int,		-- Cod. Postal
 tra_telef varchar(15),		-- Telefono
 tra_fax varchar(15),		-- Fax
 tra_nif varchar(20),		-- NIF
 tra_vehic varchar(30),		-- Vehiculo
 tra_matric varchar(10),	-- Matricula
 tra_tipcal int,		-- Tipo Calculo. (1 Fijo, 2 Variable)
 tra_prebas float,		-- Precio Base  (Para tip.Calculo 1)
 tra_impkil float,		-- Importe por Kilo  (Para tip.Calculo 1)
 tra_porseg float,		-- Porcentaje a incr. por seguro (Ambos Tipos)
 tra_porree float,		-- Porcent. a Incr. por reembolso. (Ambos Tipos)
 tra_aduori float,		-- Importe de Aduanas de Origen (Ambos Tipos Calculo)
 tra_adudes float,		-- Importe de Aduanas de Dest. (Ambos Tipos Calculo)
 tra_tipo char(1) not null default 'C', -- Tipo Transportista. 'C': Compras. 'V' Ventas.
 constraint ix_transport primary key(tra_codi)
);
create view v_transport as select * from transportista where tra_tipo='C'; -- Transportista Compras
create view v_tranpvent as select tra_codi,tra_nomb from transportista where tra_tipo='V'
UNION
select usu_nomb as tra_codi, usu_nomco as tra_nomb from usuarios where usu_activ='S'; -- Transportista Ventas
grant select on  v_transport to public;
grant select on  v_tranpvent to public;
---
-- Cabecera de Descargas de Camiones
---
-- drop table descamcab;
-- create table anjelica.descamcab
-- (
 -- emp_codi int not null,		-- Empresa que realizo el transp.
 -- eje_nume int not null, 	-- Ejercicio en que se realizo el transp.
 -- dcc_nume int not null,		-- Numero de Descarga
 -- dcc_fecini date,		-- Fecha en que descargo el camion
 -- dcc_horini decimal(4,2),       -- Hora en que llego el camion
 -- dcc_fecfin date,		-- Fecha Final
 -- dcc_hofin decimal(4,2),	-- Hora en que se fue el camion.
 -- tra_codi int not null,		-- Transportista
 -- dcc_comen varchar(255),	-- Comentario sobre el transporte
 -- constraint ix_descamcab primary key(emp_codi,eje_nume,dcc_nume)
-- );
---
-- Descargas de Camiones (Lineas)
---
-- drop table descamlin;
-- create table anjelica.descamlin
-- (
 -- emp_codi int not null,		-- Empresa que realizo el transp.
 -- eje_nume int not null, 	-- Ejercicio en que se realizo el transp.
 -- dcc_nume int not null,		-- Numero de Transporte
 -- dcl_numli int not null,	-- Numero de Linea
 -- acc_serie char(1) not null,	-- Serie del Albaran de Compras
 -- acc_nume int not null,		-- Numero Alb. Compras
 -- constraint ix_descamlin primary key(emp_codi,eje_nume,dcc_nume,dcl_numli)
-- );
---
--- Factura de Transportistas.
---
-- drop table fratraca;
create table anjelica.fratraca
(
 emp_codi int not null,		-- Empresa a la que se le fact.
 eje_nume int not null, 	-- Ejercicio de la Fact.
 frt_nume int not null,		-- Numero de Factura
 frt_fecha date not null,	-- Fecha de Fact.
 frt_nufrtr varchar(25),	-- Num. Fra del Transportista.
 frt_fefrtr date,		-- Fecha Fra. del proveedor.
 tra_codi int not null,		-- Transportista
 frt_trasp int not null,	-- Traspasado a Cont.?
 div_codi int not null,		-- Divisa
 fpa_codi int not null,		-- Forma de Pago
 frt_implin float not null,	-- Importe de Lineas
 frt_dtopp float not null,	-- % Dto. Pronto Pago
 frt_dtocom float not null,	-- % Dto. Comercial
 frt_basimp float not null,	-- Base Imponible
 frt_piva float not null,	-- % IVA
 frt_impiva float not null,	-- Imp. IVA
 frt_preequ float not null,	-- % Rec. Equivalencia
 frt_impree float not null,	-- Imp. Rec. Equivalencia
 frt_caltot char(1) not null,	-- Calc. Transp. S/total (S/N)
 tap_codi float not null,	-- Tarifa de Transp.
 frt_imptot float not null,	-- Imp. Total
 frt_prekil float not null,	-- Precio Por kilo del Transp.
 constraint ix_fratraca primary key(emp_codi,eje_nume,frt_nume)
);
--
-- Factura de Transportistas (LINEAS)
--
-- drop table fratrali;
create table anjelica.fratrali
(
 emp_codi int not null,		-- Empresa a la que se le fact.
 eje_nume int not null, 	-- Ejercicio de la Fact.
 frt_nume int not null,		-- Numero de Factura
 frt_numlin int not null,	-- Numero de Linea
 frt_alccam char not null,	-- Alb. Compra/Camion/Ninguno
 dcc_ejerc int not null, 	-- Ejercicio de la descarga o Albaran
 acc_serie char(1),		-- Serie del Albaran de Compras
 frt_numalb int not null,	-- Numero Alb. Compras o Descarga Camion
 dcl_numli int not null,	-- Numero de Linea Descarga Camion
 frl_canti float not null,	-- Cantidad A facturar
 tra_codi  int not null,	-- Transportista.
 tap_codi  int not null,	-- Tarifa de Portes.
 tap_impor float not null,	-- Importe de Transp.
 constraint ix_fratrali primary key (emp_codi,eje_nume,frt_nume,frt_numlin)
);
--
-- Tabla con Tarifas de Portes
--
-- drop table taripor;
create table anjelica.taripor
(
 tra_codi int not null,		-- Transportista
 tap_codi int not null,		-- Numero de Tarifa.
 tap_nomb varchar(30) not null,	--  Nombre de Tarifa
 tap_fecini date not null,	-- Feha Inicio
 tap_fecfin date not null,	-- Fecha Final
 tap_kilos  int not null,	-- Kilos Limite (0 Sin limite)
 tap_impor  float not null,	-- Importe
 tap_fijkil char(1) not null 	-- Imp. p/Kilo (K) o Fijo (F)
);
create index ix_taripor on taripor(tra_codi,tap_codi,tap_fecini);
--
-- Tabla COmisiones Representantes
--
create table anjelica.comision_represent
(
	avc_id int  not null, -- Numero Albaran
	cor_linea varchar(30) not null, -- LINEA
	cor_coment varchar(120) not null, -- Comentario
	constraint ix_comrep primary key (avc_id,cor_linea)
 );
grant all on anjelica.comision_represent to public;
--
-- Tabla Libro de Vtos.
--
-- drop table  librovto;
create table anjelica.librovto
(
 lbv_orige char(1) not null,		-- Origen (Compra,Transportista)
 lbv_copvtr int    not null,		-- Codigo Proveedor/Transportista
 lbv_nombre varchar(50) not null,	-- Nombre Proveedor/Transportista
 emp_codi int not null,			-- Empresa
 eje_nume int not null,			-- Ejercicio
 lbv_numfra int not null,		-- Numero de Fra.
 lbv_nume   int not null,		-- Numero de Pago
 lbv_fecfra date,			-- Fecha de Fra.
 lbv_fecvto date not null,		-- Fecha de Vto.
 lbv_impvto float not null,		-- Importe a Pagar.
 lbv_pagado char(1) not null,		-- Pagado Completo (Si/No)
 lbv_imppag float not null,		-- Importe Pagado
 lbv_comen varchar(255),		-- Comentario sobre Pago
 constraint ix_librovto primary key (lbv_orige,emp_codi,eje_nume,lbv_numfra,lbv_nume)
);
--
-- Tabla Pagos Realizados
--
-- drop table libpagcab;
create table anjelica.libpagcab
(
 emp_codi int not null,		-- Empresa
 lbp_nume int not null,		-- Numero Orden de Pago
 lbp_numlin int not null,	-- Numero Linea Orden de Pago
 lbp_fecpag date not null,	-- Fecha de Pago
 bat_codi int not null,		-- Banco de Tesoreria (De donde salen las pelas)
 lbp_tippag char(1) not null,	-- Tipo de Pago (Pagare, Cheque,
				-- Recibo, Transferencia,Extranjero)
 ban_codi int,			-- Codigo de Banco (Para Transferencias)
 lbp_baofic int,		-- Oficina de Banco
 lbp_badico int,		-- Digito Control de Banco
 lbp_bacuen float,		-- Cuenta de Banco
 lbp_numtal varchar(15),	-- Numero de Tal�n o Pagare
 lbp_facprv varchar(12), 	-- Num. Fra del Proveedor
 constraint ix_lipaca primary key (emp_codi,lbp_nume,lbp_numlin)
);
--
-- Tabla Detalles de Pagos
--
-- drop table libpagdet;
create table anjelica.libpagdet
(
 emp_codi int not null,		-- Codigo empresa
 lbp_nume int not null,		-- Numero Orden de Pago
 lbp_numlin int not null, 	-- Numero de Pago.
 lip_nulide int not null,	-- Numero Linea detalle
 lbv_orige char(1) not null,	-- Origen (Compra/Transp)
 eje_nume int not null,		-- Ejercicio
 lbv_numfra int not null,	-- Numero de Fra.
 lbv_nume int not null,		-- Numero de Vto.
 lip_import float not null,	-- Importe Pagado.
 lip_totpag char(1) not null,	-- Totalmente Pagado (S/N)
 constraint ix_libpagdet primary key (emp_codi,lbp_nume,lbp_numlin,lip_nulide)
);
create index ix_lipade2 on libpagdet(emp_codi,lbp_nume,lbp_numlin);
--
-- Tabla con Parametros de configuracion de la Aplicacion
--
-- drop table configuracion;
create table anjelica.configuracion
(
  emp_codi int not null,           -- Empresa
  cfg_almcom int not null,         -- Almacen de Compras por defecto.
  cfg_almven int not null,         -- Almacen de Ventas por defecto
  cfg_numdec int not null,         -- Numero de Decimales.
  cfg_desven int not null,         -- Pedir Desp. en albaranes de Venta
  cfg_tideve int,                  -- Tipo Despieces para Ventas
  emp_prvdes int,                  -- Proveedor p/def para desp.
  cfg_dispr1 varchar(15) not null, -- Nombre Discr. Producto 1
  cfg_dispr2 varchar(15) not null, -- Nombre Discr. Producto 2
  cfg_dispr3 varchar(15) not null, -- Nombre Discr. Producto 3
  cfg_dispr4 varchar(15) not null, -- Nombre Discr. Producto 4
  cfg_discl1 varchar(15) not null, -- Nombre Discr. Cliente 1
  cfg_discl2 varchar(15) not null, -- Nombre Discr. Cliente 2
  cfg_discl3 varchar(15) not null, -- Nombre Discr. Cliente 3
  cfg_discl4 varchar(15) not null, -- Nombre Discr. Cliente 4
  cfg_dispv1 varchar(15) not null, -- Nombre Discr. Proveedor 1
  cfg_dispv2 varchar(15) not null, -- Nombre Discr. Proveedor 2
  cfg_dispv3 varchar(15) not null, -- Nombre Discr. Proveedor 3
  cfg_dispv4 varchar(15) not null, -- Nombre Discr. Proveedor 4
  cfg_caejau char(1) not null,	   -- Cambio Ejerc. Automatico (S/N)
  cfg_lialgr char(1) not null,	   -- Listado Albaranes Compras Graficos (S/N)
  cfg_lifrgr char(1) not null,	   -- Listado Fras. Compras Grafico (S/N)
  cli_codi   int    not null,      -- Cliente para uso Interno (Traspaso almacenes)
  cfg_tipemp int    not null default 1, -- Tipo de Empresa (1. Carnica, 2. Plantación)
  cfg_palven int not null default 1, -- Usa Palets en Ventas. 
  cfg_tarini int not null default 2, -- Tarifa Inicial
  constraint ix_config primary key (emp_codi)
);
insert into configuracion values(1,1,1,2,1,1,9999,
'Inc.Lista', 'Otros','Camara','Mayor/Calle',
'Zon/Rep', 'Zona/Cred.','Activo','Giro',
'Activo', 'Discr1','Discr2','Discr3','S','N','N',9999);
create view v_config as select * from configuracion ;
grant select on  v_config to public;
--
-- Tabla con diferentes Parametros para la aplicación
--
--drop table parametros;
create table anjelica.parametros
(
    usu_nomb varchar(15) not null default '*', -- Si * aplica a todos los usuarios
    par_nomb varchar(15) not null,    -- Nombre parametro
    par_desc varchar(50) not null,    -- Descripción Parametro
    par_valor int not null,           -- Char (0: False,-1: True)
    constraint ix_parametros primary key (usu_nomb,par_nomb)
);
insert into parametros values('*','checktidcodi','Validar producto sobre tipo despiece', 1);
insert into parametros values('*','autollenardesp','Auto llenar despiece con los productos', 0);
insert into parametros values('*','cargaproequi','Carga Productos Equivalentes en Tactil', 0);
insert into parametros values('*','agrupalote','Permite Agrupar diferentes Lote en despieces', 0);
insert into parametros values('*','avisatidcodi','Solo Avisarde  productos no validos en despieces', 0);
insert into parametros values('*','verNegResStock','Ver Stocks en negativo en Cons. Resumen Stock', 0);
insert into parametros values('*','impAlbTexto','Impresion Albaranes Venta modo texto', 0);
insert into parametros values('*','impFraTexto','Impresion Facturas Venta modo texto', 0);
insert into parametros values('*','famProdReci','Familia de productos de reciclaje',99);
insert into parametros values('*','despPend','Permite dejar despieces pendientes',0);
--
-- Tabla con las diferentes camaras
-- por empresa
--
-- drop table camaras;
--create table anjelica.camaras
--(
 -- emp_codi int not null,	-- Empresa
--  cam_codi varchar(2) not null,	-- Numero de Camara
--  cam_nomb varchar(50),		-- Nombre de Camara
-- constraint ix_camaras primary key (emp_codi,cam_codi)
--);
--
-- Cabecera Control de Inventarios
--
-- drop table coninvcab;
create table anjelica.coninvcab
(
	emp_codi int not null, 		-- Empresa (Deprecated)
	cci_codi int not null,		-- Numero de Inventario
	usu_nomb varchar(15),		-- Usuario q. realizo el inventario
	cci_feccon date not null,	-- Fecha de Control
	cam_codi varchar(2) not null,	-- Codigo de Camara (Tabla v_camaras)
	alm_codi int not null, 	-- Almacen
    constraint ix_coninvcab primary key (emp_codi,cci_codi)
);
create index ix_coninvcab1 on coninvcab(cci_feccon,emp_codi);
--
-- Lineas Control de Inventarios
--
-- drop table coninvlin;
create table anjelica.coninvlin
(
    emp_codi int not null,			-- Empresa
    cci_codi int not null,			-- Numero de Inventario
    lci_nume int not null,			-- Numero de Linea
    prp_ano  int not null,      		-- Ejercicio del lote
    prp_empcod int not null,			-- Empresa del Lote
    prp_seri char(1) not null,			-- Serie del Lote
    prp_part int not null,			-- Partida
    pro_codi int not null,			-- Producto
    pro_nomb varchar(50),			-- Nombre de Producto
    prp_indi int not null,                      -- Individuo de Lote
    lci_peso decimal(6,2) not null,		-- Peso de inventario
    lci_kgsord decimal(6,2) not null,		--  Kgs. Según Ordenador
    lci_numind int not null,                -- Numeros de Piezas
    lci_regaut smallint not null default 0,	-- Registro Automatico (0=No)
    lci_coment varchar(45),			-- Comentario
	lci_causa varchar(30), 		-- Causa Probable Incidencia.	
	lci_numcaj smallint not null default 0,	-- Numero Caja
    lci_numpal varchar not null default '',	-- Numero Palet
	alm_codlin int not null, -- Almacen (Para List. Dif. Inventario)
  constraint ix_coninvlin primary key(cci_codi,lci_nume,emp_codi,)
);
create index ix_coninvl2 on coninvlin(pro_codi,prp_ano,prp_part,prp_seri,prp_indi);
--
-- Vista para unir cabecera y lineas de inventarios
--
create view anjelica.v_coninvent as
select c.emp_codi,c.cci_codi,c.usu_nomb,cci_feccon, cam_codi,alm_codi,lci_nume,prp_ano, prp_empcod, prp_seri, prp_part, pro_codi, pro_nomb,
prp_indi,lci_peso,lci_kgsord,lci_numind,lci_regaut,lci_coment, lci_causa,lci_numcaj,lci_numpal,alm_codlin from coninvcab as c, coninvlin as l where
c.emp_codi=c.emp_codi
and c.cci_codi=l.cci_codi;
grant select on  v_coninvent to public;
----
-- Tabla Cabecera Inventarios  piezas producción
--
--drop table anjelica.cinvproduc;
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
  constraint ix_linvproduc primary key(cip_codi,lip_numlin)
);
create index ix_linvproduc2 on linvproduc(pro_codi,prp_ano,prp_part,prp_seri,prp_indi);

create view anjelica.v_invproduc as
select c.cip_codi,c.usu_nomb,cip_fecinv, c.cam_codi,c.alm_codi,lip_numlin,prp_ano, prp_seri, prp_part, l.pro_codi, a.pro_nomb,
prp_indi,prp_peso,l.prv_codi,prv_nomb,prp_fecsac,lip_fecalt from cinvproduc as c, linvproduc as l left join v_articulo as a on l.pro_codi=a.pro_codi
left join v_proveedo as pv on l.prv_codi = pv.prv_codi where
 c.cip_codi=l.cip_codi;
grant select on  v_invproduc to public;
--
-- Vista para selecionar los diferentes tipos de camaras
create view anjelica.v_camaras as
select emp_codi,dis_codi as cam_codi ,dis_nomb as cam_nomb from v_discrim where dis_tipo='AC';
--
-- Precios para inventarios
--
-- drop table invprec;
create table anjelica.invprec
(
	cci_feccon date not null,	-- Fecha de Control
	pro_codi int not null,		-- Producto
	ipr_prec float not null,	-- Precio de Producto
constraint ix_invprec primary key (cci_feccon,pro_codi)
);
--
-- Cabecera de pedidos de Ventas
--
--
-- drop table anjelica.pedvenc;
create table anjelica.pedvenc;
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 cli_codi int not null,		-- Cliente
 pvc_clinom varchar(40),	-- Nombre cliente.
 alm_codi int not null,		-- Almacen
 pvc_fecped timestamp not null,	-- Fecha de Pedido
 pvc_fecent date not null,	-- Fecha de Entrega
 pvc_comen varchar(200),	-- Comentario s/cabec. pedido
 pvc_confir char(1) not null,	-- Pedido Confirmado (S/N/Cancelado)
 avc_ano int,			-- Ejercicio del Albaran
 avc_serie char(1) not null,	-- Serie del Albaran
 avc_nume int not null,		-- Numero de Albaran
 usu_nomb varchar(15) not null,	-- Usuario q. crea el pedido
 pvc_cerra int not null,	-- Albaran Cerrado (0 NO, -1 SI)
 pvc_nupecl varchar(20),	-- Numero pedido de Cliente
 pvc_impres char(1) not null,	-- Listado (S/N)
 pvc_incfra char(1) not null default 'N', -- Incluir Fra?
 pvc_comrep varchar(200),	    -- Comentario para Reparto
 pvc_depos char(1) not null default 'N', -- Deposito ?
 rut_codi varchar(2),			-- Ruta
 pvc_fecpre date,				-- Fecha Preparación
 constraint ix_pedvenc primary key(emp_codi,eje_nume,pvc_nume)
);
--
-- Tabla historico Pedidos de ventas.
--
create table anjelica.hispedvenc
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 cli_codi int not null,		-- Cliente
 pvc_clinom varchar(40),	-- Nombre cliente.
 alm_codi int not null,		-- Almacen
 pvc_fecped timestamp not null,	-- Fecha de Pedido
 pvc_fecent date not null,	-- Fecha de Entrega
 pvc_comen varchar(200),	-- Comentario s/cabec. pedido
 pvc_confir char(1) not null,	-- Pedido Confirmado (S/N/Cancelado)
 avc_ano int,			-- Ejercicio del Albaran
 avc_serie char(1) not null,	-- Serie del Albaran
 avc_nume int not null,		-- Numero de Albaran
 usu_nomb varchar(15) not null,	-- Usuario q. crea el pedido
 pvc_cerra int not null,	-- Albaran Cerrado (0 NO, -1 SI)
 pvc_nupecl varchar(20),	-- Numero pedido de Cliente
 pvc_impres char(1) not null,	-- Listado (S/N)
 his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
 his_fecha timestamp not null, -- Fecha de Cambio
 his_coment varchar(100), -- Comentario sobre el Cambio
 his_rowid int not null,
  constraint ix_hpedvenc primary key(his_rowid)
 );
 
---
--- Lineas de Pedidos de Ventas
---
-- drop table pedvenl;
create table anjelica.pedvenl
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 pvl_numlin int not null,	-- Numero de Linea
 pvl_kilos float not null,	-- Kilos o unidades de producto.
 pvl_unid float not null,   -- Unidades
 pvl_canti float not null default '0',  -- Cantidad Introducida.
 pvl_tipo char(1) not null default 'K', -- Tipo Unidad introducida. (K/P/C)
 pro_codi int not null,		-- Codigo de Producto
 pvl_comen varchar(100),	-- Comentario sobre el producto
 pvl_precio float not null,	-- Precio
 pvl_precon int not null,	-- Precio Confirmado (0 NO)
 prv_codi int,			-- Proveedor
 pvl_feccad date,		-- Fecha Caducidad
 pvl_fecped timestamp not null,	-- Fecha Creacion Linea Pedido
 pvl_fecmod timestamp,		-- Fecha Mod. Linea Pedido
 constraint ix_pedvenl primary key(emp_codi,eje_nume,pvc_nume,pvl_numlin)
);
create table anjelica.hispedvenl
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 pvl_numlin int not null,	-- Numero de Linea
 pvl_kilos float not null,	-- Kilos o unidades de producto.
 pvl_unid float not null,   -- Unidades
 pvl_canti float not null default '0',  -- Cantidad Introducida.
 pvl_tipo char(1) not null default 'K', -- Tipo Unidad introducida. (K/P/C)
 pro_codi int not null,		-- Codigo de Producto
 pvl_comen varchar(100),	-- Comentario sobre el producto
 pvl_precio float not null,	-- Precio
 pvl_precon int not null,	-- Precio Confirmado (0 NO)
 prv_codi int,			-- Proveedor
 pvl_feccad date,		-- Fecha Caducidad
 pvl_fecped timestamp not null,	-- Fecha Creacion Linea Pedido
 pvl_fecmod timestamp,		-- Fecha Mod. Linea Pedido
 his_rowid int not null,
 constraint ix_hpedvenl primary key(his_rowid,pvl_numlin)
);
 --
 -- Modificaciones sobre Pedidos Ventas
 --
create table anjelica.pedvenmod
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio de Pedido
 pvc_nume int not null,		-- Numero de Pedido
 pvl_numlin int not null,	-- Numero de Linea
 pvm_canti float not null,  -- Nueva Cantidad
 pvm_coment varchar(40),	-- Comentario
 usu_nomb char(15) not null, -- Usuario que realiza apunte.
 pvm_fecha  timestamp not null default CURRENT_TIMESTAMP, -- Fecha Alta/Modif.
 constraint ox_pedvenmod primary key(emp_codi,eje_nume,pvc_nume,pvl_numlin)
);
drop view anjelica.v_pedven;
create or replace view anjelica.v_pedven as select  c.emp_codi,c.eje_nume, c.pvc_nume , c.pvc_id,cli_codi , alm_codi, pvc_fecped,pvc_fecpre,
 rut_codi,pvc_fecent, pvc_comen , pvc_comrep, pvc_confir , avc_ano , avc_serie , avc_nume ,
 c.usu_nomb , pvc_cerra , pvc_nupecl , pvc_impres ,pvc_depos,
 l.pvl_numlin, pvl_kilos,pvl_canti,pvm_canti,pvm_coment,pvl_unid,pvl_tipo, l.pro_codi, ar.pro_nomb as pvl_nomart,ar.pro_codart,pve_nomb,
 pvl_comen, pvl_precio ,pvl_precon ,prv_codi,pvl_feccad, pvl_fecped, pvl_fecmod,m.usu_nomb as pvm_usunom,pvm_fecha  from 
 pedvenc as c, pedvenl as l  left join pedvenmod as m ON
 m.emp_codi=l.emp_codi
 and m.eje_nume= l.eje_nume and m.pvc_nume=l.pvc_nume
 and m.pvl_numlin=l.pvl_numlin, v_articulo as ar left join prodventa on pro_codart = pve_codi
 where c.emp_codi=l.emp_codi 
 and c.eje_nume=l.eje_nume and c.pvc_nume = l.pvc_nume 
 and l.pro_codi = ar.pro_codi;
grant select on anjelica.v_pedven to public;


--drop view anjelica.v_hispedven;
create or replace view anjelica.v_hispedven as select  c.his_rowid as his_rowid,c.emp_codi,c.eje_nume, c.pvc_nume , pvc_id, cli_codi , alm_codi, pvc_fecped,
 pvc_fecent, pvc_comen , pvc_confir , avc_ano , avc_serie , avc_nume ,
 c.usu_nomb , pvc_cerra , pvc_nupecl , pvc_impres ,
 l.pvl_numlin, pvl_kilos,pvl_canti,pvm_canti,pvm_coment,pvl_unid,pvl_tipo, pro_codi,
 pvl_comen, pvl_precio ,pvl_precon ,prv_codi,pvl_feccad, pvl_fecped, pvl_fecmod,m.usu_nomb as pvm_usunom,
 pvm_fecha  from 
 hispedvenc as c, hispedvenl as l  left join pedvenmod as m ON
 m.emp_codi=l.emp_codi
 and m.eje_nume= l.eje_nume and m.pvc_nume=l.pvc_nume
 and m.pvl_numlin=l.pvl_numlin
 where c.his_rowid=l.his_rowid ;
grant select on anjelica.v_hispedven to public;
--
-- Tabla Comentario de Pedidos de ventas.
-- 
--
-- drop table compedven;
create table anjelica.compedven
(
 cli_codi int not null,
 cpv_fecha date not null,
 cpv_come varchar(512)
);
create index  ix_compedven  on compedven (cli_codi,cpv_fecha);
--
-- Tabla Comentarios  sobre cliente y producto
-- Utilizado SOLO por programa gnu.chu.anjelica.consCliente
--
create table anjelica.comprocli
(
 cli_codi int not null,
 pro_codi int not null,
 cpc_come varchar(512)
);
--
-- Tabla de Tipos de Listados de Almacen (Cabecera)
--
create table anjelica.tilialca
(
 tla_codi int not null,		-- Tipo de Listado
 tla_nomb varchar(60) not null, -- Descripcion
 tla_nulipr int not null,	-- Numero Lineas por Producto.
 tla_diagfe int not null,	-- Dias a Agrupar Fechas Caducidad (0 Sin agrupar)
 tla_vekgca char(1) not null,	-- Ver Kilos o Unidades
 constraint ix_tilialca primary  key (tla_codi)
);
--
-- Tabla de Tipos de Listados de Almacen (Grupos)
--
create table anjelica.tilialgr
(
 tla_codi int not null,		-- Tipo Listado
 tla_orden int not null,	-- Orden
 pro_desc varchar(25) not null,	-- Cabecera del Producto
 constraint ix_tilialgr primary  key (tla_codi,tla_orden)
);
--
-- Tabla de Tipos de Listados de Almacen (Productos)
--
create table anjelica.tilialpr
(
 tla_codi int not null,		-- Tipo Listado
 tla_orden int not null,	-- Orden
 pro_codi int not null 		-- Codigo de Producto
);
--
-- Tablas de Ejercicios
--
create table anjelica.ejercicio
(
 emp_codi int not null,		-- Empresa
 eje_nume int not null,		-- Ejercicio
 eje_fecini date not null,	-- Fecha Inicial
 eje_fecfin date not null,	-- Fecha Final
 eje_cerrad int not null,	-- Cerrado (0 No, -1 Si)
 constraint ix_ejercicio primary  key (emp_codi,eje_nume)
);
--
-- Tabla de SubEmpresas (secciones)
--
-- drop table subempresa;
create table anjelica.subempresa
(
 emp_codi int not null,		-- Empresa
 sbe_codi smallint not null,	-- SubEmpresa
 sbe_nomb char(40) not null,	-- Descripción SubEmpresa
 sbe_tipo char(1) not null default 'C', -- (C)liente, (A)rticulo
 sbe_albped smallint not null default 0, -- Indica si los alb. Deben ir sobre pedido.
 
 constraint ix_subempr primary key (emp_codi,sbe_codi,sbe_tipo)
);
INSERT INTO SUBEMPRESA VALUES(1,1,'GENERAL','C',0);
INSERT INTO SUBEMPRESA VALUES(1,1,'ARTICULO','A',0);
--
-- Tabla relacion entre subempresas cliente y articulo
-- Si no hay registros para una subempresa de cliente, se entendera que esa subempresa
-- de cliente puede acceder a los productos de todas las secciones
--
create table anjelica.relsubempr
(
    rse_codcli int not null,    -- Codigo subempresa de cliente.
    rse_codart int not null    -- Codigo Subempresa de Articulo.
);
--
-- Tabla de Listados
--
-- drop table listados;
create table anjelica.listados
(
 emp_codi int not null,		-- Empresa (Si es 0 indica TODAS las empresas)
 lis_codi int not null,		-- Codigo Listado
 lis_nomb char(80) not null,    -- Descripcion Listado
 lis_file char(80) not null,    -- Fichero con Listado (sin incluir .jasper)
 lis_logo char(50), -- Si es null sera la Imagen logotipo.jpg
 constraint ix_listados primary  key (emp_codi,lis_codi)
);
insert into anjelica.listados values(0,1,'Listado Trazabilidad','listraza',null);
insert into anjelica.listados values(0,2,'Listado Facturas Ventas (Cabecera)','cabfrave',null);
insert into anjelica.listados values(0,3,'Listado Facturas Ventas (Lineas)','lifrave',null);
insert into anjelica.listados values(0,5,' Listado Facturas Comentarios (PIE) ','cpfrave',null);
insert into anjelica.listados values(0,6,'Listado Fact. Ventas PreImpr. (Cab)','cabfravepi',null);
insert into anjelica.listados values(0,7, 'Listado Albaran Ventas (Cabecera)','cabalbve',null);
insert into anjelica.listados values(0,8, 'Listado Albaran Ventas (Linea)','lialbve',null);
insert into anjelica.listados values(0,9, 'Listado Albaran Ventas Desgl.(Linea)','lialbvedep',null);
insert into anjelica.listados values(0,10, 'Listado Hojas Palets','etiqPalets',null);
insert into anjelica.listados values(0,11,'Listado Tarifas Precios','tarifa','vp25.jpg');
--
-- Tabla de Envases
--
-- drop table envases;
create table anjelica.envases
(
 env_codi smallint not null, -- Codigo de Envase
 env_nomb char(25) not null, -- Descripcion de la clave.
 env_unid smallint not null, --  Unidades Media de productos por envase
 env_kilmax real not null default 0, -- Kilos Maximos x Envase
 env_peso real not null,	--  Peso del envase
 constraint ix_envases primary key (env_codi)
);
--
-- Acesso a Empresas por parte de los usuarios
--
-- drop table accusuemp;
create table anjelica.accusuemp
(
usu_nomb varchar(15) not null, -- Usuario
emp_codi int not null, --  Empresa
constraint ix_accusuemp primary key (usu_nomb,emp_codi)
);
--
-- Acesso a SubEmpresas por parte de los usuarios
--
-- drop table accususbe;
create table anjelica.accususbe
(
usu_nomb varchar(15) not null, -- Usuario
emp_codi int not null, --  Empresa
sbe_codi smallint not null, -- SubEmpresa
constraint ix_accususbe primary key (usu_nomb,emp_codi,sbe_codi)
);

-- Ventas sobre tarifa

create table anjelica.ventarifa
(
emp_codi int not null,
eje_nume int not null,
avc_serie nchar(1) not null,
avc_nume int not null,
avl_numlin int not null,
avc_fecalb date not null,
cli_codi int  not null,
pro_codi int not null,
pro_nomb nchar(50) not null,
avl_kilven decimal(10,2) not null,
avl_prven decimal(10,2) not null,
vet_prtari decimal(10,2),
vet_compr char(1) not null,
vet_prtaaj decimal(10,2),
vet_comen nchar(100)
);

-- Configuracion por defecto para programa "chuchi.iberrioja.angulo.cointprtar"

create table anjelica.ventarimem
(
usu_codi varchar(8) not null,
vtm_feini date not null,
vtm_fefin date not null,
zon_codi nchar(2) not null
);
--
-- Tabla de Representantes
--
create table anjelica.represent
(
rep_codi varchar(4) not null,    -- Codigo de Representante
rep_nomb varchar(45) not null,   -- Nombre Representante
cue_codi varchar(15),            -- cuenta contable
rep_direc varchar(50),           -- Direccion
rep_pobl varchar(30),            -- Poblaccion
rep_codpo int,                   -- Cod. Postal
rep_telef varchar(15),           -- Telefono
rep_fax varchar(15),             -- Fax
rep_nif varchar(15),             -- NIF
rep_email varchar(25),           -- Email.
constraint ix_represent primary key (rep_codi)
);
--
-- Indica en que zonas puede trabajar un representante.
-- No permitira asignarle a un cliente una repres. si la zona no esta en esta tabla.
-- Si un representante no tiene ningun apunte en esta tabla, significara que
-- no esta limitado a unas zonas en particular.
--
create table anjelica.reprzona
(
rep_codi varchar(4) not null,    -- Codigo de Representante
zon_codi varchar(4) not null     -- Codigo de Zona
);
--
-- Indica en que Secciones (Subempresas) puede trabajar un representante.
-- No permitira asignarle a un cliente una repres. si la seccion no esta en esta tabla.
-- Si un representante no tiene ningun apunte en esta tabla, significara que
-- no esta limitado a una seccion en particular.
--
create table anjelica.reprsecion
(
rep_codi varchar(4) not null,    -- Codigo de Representante
sbe_codi int not null            -- Codigo de SubEmpresa (o sección)
);
--
-- Indica Tarifas puede usar un representante.
-- No permitira asignarle a un cliente una repres. si la tarifa no esta en esta tabla.
-- Si un representante no tiene ningun apunte en esta tabla, significara que
-- no esta limitado a una tarifa en particular.
--
create table anjelica.reprtari
(
rep_codi varchar(4) not null,    -- Codigo de Representante
tar_codi int not null            -- Codigo de Tarifa
);
create view anjelica.v_zonas as
select dis_codi as zon_codi,dis_nomb as zon_nomb  from v_discrim
where dis_tipo='Cz';
grant select on anjelica.v_zonas to public;
--
-- Vista para Representantes/Zona - segun discriminadores.
create view anjelica.v_reprzona as
select dis_codi as rep_codi,dis_nomb as rep_nomb  from v_discrim
where dis_tipo='CR';
grant select on anjelica.v_reprzona to public;
-- Vistas de Credito - Segun discriminadores
create view anjelica.v_credclien as
select dis_codi as crc_codi,dis_nomb as crc_nomb  from v_discrim
where dis_tipo='CC';
grant select on anjelica.v_credclien to public;
-- Vista de rutas - Segun discriminadores
create view anjelica.v_rutas as
select dis_codi as rut_codi,dis_nomb as rut_nomb  from v_discrim
where dis_tipo='CU';
grant select on anjelica.v_rutas to public;

--
-- Cabecera  albaranes de proveedores
--
-- drop table albprvca;
create table anjelica.albprvca
(
   emp_codi int not null,   -- Empresa
   apc_codi serial not null,   -- Codigo interno
   apc_fecha date not null, -- Fecha Albaran
   prv_codi int not null,   -- Proveedor
   apc_albprv varchar(25) not null, -- Numero Albaran de proveedor
   apc_coment varchar(100), -- Comentarios
   fcc_ano  int,            -- Año de Factura
   fcc_nume int,            -- Numero de Factura
   apc_trans char(1) not null, -- Trasnsportista (T)/Proveedor (P)
   apc_implin float,         -- Importe de Linea
   apc_albtra int,           -- Codigo de Albaran del transportista
   apc_estad char(1) not null,-- Estado (Abierto/Cerrado)
   constraint ix_albprvca primary key (emp_codi,apc_codi)
);
--
-- Lineas  albaranes de proveedores
--
create table anjelica.albprvli
(
   emp_codi int not null,   -- Empresa
   apc_codi int not null,   -- Codigo interno
   apl_numlin int not null, -- Numero de Linea
   pro_codi int not null,   -- Codigo de producto.
   pro_nomb varchar(40),    -- Nombre de producto
   apl_unid int not null,   -- Unidades
   apl_canti float not null, -- Cantidad
   apl_precio float not null,-- Precio
   constraint ix_albprvli primary key (emp_codi,apc_codi,apl_numlin)
);
--
-- Tabla calendarios
--
create table anjelica.calendario
(
	cal_ano int,  -- Año
	cal_mes int,  -- Mes
	cal_fecini date,  -- Fecha Inicial
	cal_fecfin date,  -- Fecha Final.
	constraint ix_calendario primary  key (cal_ano,cal_mes)
);
--insert into calendario values(2012,1,'20120101','20120128');
--  drop table histventas;
create table anjelica.histventas
(
    hve_fecini date not null, -- Fecha Inicial
    hve_fecfin date not null, -- Fecha Final
    hve_kilven float,        -- Kilos Venta (Total)
    hve_impven float,        -- Importe Ventas (Total)
    hve_impgan float         -- Importe ganancia
    hve_impven float,        -- Importe Ventas
    hve_kiveav float,        -- Kilos Venta Articulos Vendibles
    hve_imveav float,        -- Importe de Venta Articulos Vendibles
	div_codi int not null default 1, -- Divisa
);
-- alter table anjelica.v_stkpart rename to stkpart_old;
-- drop table anjelica.stockpart;
create table anjelica.stockpart
(
	eje_nume int,		-- Ejercicio
 	emp_codi int,		-- Empresa (Para permisos de acceso)
	pro_serie char(1),	-- Serie
 	stp_tiplot char(1),	-- Tipo Lote (P: Partida S: Acumulado,  B Bloqueado)
 	pro_nupar int,		-- Partida
	stk_block int not null default 0, --   Bloqueado. 0: No
	pro_codi int,		-- Producto
	pro_numind int,		-- Numero Individuo
	alm_codi int,		-- Almacen
	stp_unini int,		-- Unidades Iniciales (puestas al crear
				-- el registro)
 	stp_unact int,		-- Unidades Actuales
 	stp_feccre timestamp,	-- Fecha de Creacion
 	stp_fefici timestamp,	-- Fecha Ult. Actualizacion
 	stp_kilini float,	-- Kilos Iniciales
	stp_kilact float,	-- Kilos Actuales
	prv_codi int,		-- Proveedor
	stp_feccad date,	-- Fecha de Caducidad
	stp_numpal sallint, -- Numero Palet
	stp_numcaj smallint, -- Numero caja
	cam_codi varchar(2), -- Camara en que esta ubicada
	constraint ix_stockpart primary key (pro_codi,eje_nume,pro_serie,pro_nupar,pro_numind);
);
create index ix_stkpart1 on anjelica.stockpart(eje_nume,pro_serie,pro_nupar,pro_numind,pro_codi);
create index ix_stkpart2 on anjelica.stockpart(pro_codi,eje_nume,pro_serie,pro_nupar);

-- alter table anjelica.v_stkpart rename to stkpart_antigua
create view anjelica.v_stkpart as select * from anjelica.stockpart;
grant select on anjelica.stkpart to public;
--drop table anjelica.mvtosalm;
create table anjelica.mvtosalm
(	
    mvt_oper char(10) not null, --
    mvt_time timestamp not null default current_timestamp,	-- Fecha de mvto.
	mvt_tipo char(1) not null default 'S', -- Entrada o Salida
	mvt_tipdoc char(1) not null, -- C (Alb. Compra), V (Alb.Venta), R (Regulariz), Despiece Entrada a alm.(d), Desp. Salida (D)
    alm_codi int not null,             -- Almacen
	mvt_fecdoc date not null,	   -- Fecha del Documento
	mvt_empcod int not null default 1, -- Empresa del Documento.
	mvt_ejedoc int not null, -- Ejercicio del Documento.
	mvt_serdoc char(1) not null, -- Serie del Ejercicio.
	mvt_numdoc int not null,	 -- Numero del Documento.
	mvt_lindoc int not null, -- Linea del documento
	pro_codi   int not null, -- Codigo de producto.
	pro_ejelot int not null, -- Ejercicio del Lote
	pro_serlot char(1) not null, -- Serie del Lote
	pro_numlot int not null, -- Numero de Lote
	pro_indlot int not null, -- Individuo del Lote.
	mvt_canti float not null, -- Cantidad
	mvt_unid int not null,    -- Unidades
	mvt_prec float,		 -- Precio
    mvt_cliprv int,          -- Cliente / Proveedor.
    mvt_feccad date          -- Fecha Caducidad del Indiv.
);
CREATE INDEX ix_mvtalm1 on anjelica.mvtosalm(mvt_tipdoc,mvt_fecdoc,mvt_empcod,mvt_ejedoc,mvt_serdoc);
CREATE INDEX ix_mvtalm2 on anjelica.mvtosalm(pro_codi,pro_ejelot,pro_serlot,pro_numlot,pro_indlot,mvt_time);
CREATE INDEX ix_mvtalm3 on anjelica.mvtosalm(pro_codi,mvt_time);

drop table anjelica.ajustedb;
create table anjelica.ajustedb
(
    aju_regacu int not null, -- // Regenerar Acumulados (0 No). Usado por fn_acumstk
    aju_regmvt int not null,  -- // Crear mvtos. Almacen (Solo para temas de testeo)
    aju_delmvt int not null default 0,
    aju_debug int not null default 0
);
insert into anjelica.ajustedb values(1,1,0);
--
-- Tabla con Acumulados de Stock Partidas
--
CREATE TABLE anjelica.actstkpart
(
  pro_codi integer,
  alm_codi integer,
  stp_unact integer,
  stp_feccre date,
  stp_fefici date,
  stp_kilact double precision
)
WITH ( OIDS=FALSE );
-- Index: anjelica.ix_astkpart
-- DROP INDEX anjelica.ix_astkpart;
CREATE INDEX ix_astkpart ON anjelica.actstkpart   (pro_codi, alm_codi);

-- drop table anjelica.hislisstk;
CREATE TABLE anjelica.hislisstk
(
	hls_fecstk timestamp not null, -- Fecha Listado Stock
	alm_codi int not null, -- Almacen
	
	pro_codi int,	  	-- Producto
	pro_ejelot int not null, -- Ejerc. Lote
	pro_serlot char(1) not null,	-- Serie 	
 	pro_numlot int not null,		-- Lote
	pro_indlot int not null		-- Numero Individuo		
);
create index  ix_hislisstk on anjelica.hislisstk  (alm_codi,hls_fecstk);
--
-- Tabla de Partes. Cabecera
--
--drop view anjelica.v_partes;
-- DROP TABLE anjelica.partecab;
CREATE TABLE anjelica.partecab
(
	par_codi serial, -- Numero secuencial de parte
	emp_codi int not null, -- Empresa
	pac_fecalt timestamp not null default current_timestamp, -- Fecha Creacion de parte
	pac_usuinc  varchar(15)  not null, -- Usuario Genera Incidencia (Operario sala)
	pac_usupro   varchar(15) , -- Usuario Procesa (Gerencia)
	pac_usures   varchar(15) , -- Usuario Resuelve (Oficina)
	pac_fecinc date not null, -- Fecha Incidencia
	pac_fecpro date, -- Fecha Procesado
	pac_fecres date, -- Fecha Resuelto
	pac_estad int not null default 1, -- Estado del parte (0: Generada. 1. Proc.Devolucion
									  -- 2. Procesada. 3 Cerrada. 4 Cancelada)	
	pac_tipo char(1) not null, -- Tipo Parte:  (S)ala,(E)ntrada, (D)evolucion,Devol.(R)ecepionada , (N)o-recepción
	pac_coment varchar(150), -- Comentario
	pac_cliprv int,	-- Cliente/Proveedor	
	pac_docano int, -- Ejercicio Documento
	pac_docser char(1), -- Serie Alb. Venta
	pac_docnum int, -- Numero Alb. Venta
	constraint ix_partecab primary  key (par_codi)
);

-- drop table anjelica.parteabo;
--drop table  anjelica.partelin;
create table anjelica.partelin
(
	par_codi int not null, -- Codigo Parte	
	pal_codi serial not null, -- Codigo  para unir con reclamaciones
	par_linea int not null, -- Numero de Linea
	pro_codi int not null,	  	-- Producto
	pal_kilos float, -- Kilos
	pal_unidad int not null, -- Unidades/Cajas
	pro_ejelot int, -- Ejercicio Lote
	pro_serlot char(1),	-- Serie 	
 	pro_numlot int,		-- Lote
	pro_indlot int,	-- Numero Individuo		
	pro_feccad date, -- Fecha Caducidad
	pal_acsala char(1) default '-' check  (pal_acsala in ('V','R','C','-')), 
			-- Accion sugerida x sala. (V)ertedero/(R)eutilizar/(C)ongelar, (-)No Definida
	pal_comsal varchar(50), -- Comentario Sala sobre esta linea	
	pal_accion char(1) not null default '-' check  (pal_accion in ('A','V','R','C','-')), -- (A) Acept. Dev. (N) No acept. Dev.
											 -- (V)ertedero/(R)eutilizar/(C)ongelar,(-) No definida
	pal_coment varchar(50), -- Comentario sobre esta linea
	constraint ix_partelin primary  key (par_codi,par_linea)
);
alter table anjelica.partelin add constraint pal_procod foreign key (pro_codi)
    references anjelica.v_articulo(pro_codi);
--
-- Reclamaciones a proveedores. (Solicitud de abono)
--	
create table anjelica.reclamprv
(
    rpv_codi serial not null, -- Codigo de Reclamacion
	acc_id int not null, -- Albaran Proveedor.
	par_codi int, -- Codigo Parte Incidencia(puede ser nulo)
	pal_codi int, -- Linea de Parte Incidencia
	pro_codi int not null, -- Codigo de Producto
	rpv_nomart varchar(50), -- Nombre articulo 
	rpv_kilos float not null,  -- Kilos Solicitados
	rpv_kilori float not null,-- Kilos Originales
	rpv_precio float not null, -- Abono en precio
	rpv_precori float not null,-- Precio original
	rpv_kilace float not null, -- Kilos Aceptados
	rpv_preace float not null, -- Abono aceptado
	paa_coment varchar(100), -- Comentario sobre Reclamación
	rpv_estad smallint not null  default 0 -- Estado de Abono (0-> Pendiente,1 Realizado. 2 Rechazado)
)
--
--  Coomentarios sobre reclamaciones
---
create table anjelica.reclamprv_notas
(  
   rpv_codi int not null, -- Codigo de reclamación
   rpn_fecha timestamp not null default CURRENT_TIMESTAMP, -- Fecha 
   rpn_coment varchar(100) -- Comentario
);
--
-- tabla tipos cortes de productos
--
create table disproventa
(
	dpv_tipo int not null, -- Tipo Discriminador. 1. Codigo Corte. 2. Animal. 3 Origen. 4 Clasif.
	dpv_nume char(3) not null, -- Codigo
	dpv_nomb varchar(30) not null -- Nombre
);
grant select on anjelica.disproventa to public;
create view v_tipoanimal as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=2;
grant select on anjelica.v_tipoanimal to public;
create view v_tipocorte as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=1;
grant select on anjelica.v_tipocorte to public;
create view v_clasiprod as select dpv_nume,dpv_nomb from disproventa where dpv_tipo=4;
grant select on anjelica.v_clasiprod to public;

---
-- Tabla con Referencias productos de venta
-- Se codificara de la siguiente manera.
-- X -> Animal
-- X -> Origen. 
-- XXX -> Codigo Corte
-- XXX -> Clasificacion por peso. Terminara siempre con un + o -
-- XX -> Clasificacion por calidad (A,B, P:Pistola)
-- * -> Indica Congelado.
-- Ejemplo: VA01330+ -> Solomillo Vaca 3.0+
-- VG009 -> Lomo con hueso y Solomillo de Vaca Gallego
-- LI012* -> Redondo Ternera Leche Internacional Congelado
-- VA00925+P -> Lomo con Hueso y Sol. de vaca de 25+ de pistola
---
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
-- 
-- Productos de tarifa
--
create table prodtarifa
(
	pve_codi varchar(15) not null, -- Referencia producto Venta
	pro_codi int  not null         -- Codigo Producto	
);
grant select on  anjelica.prodtarifa to public;
--
-- Tabla para calculos tarifas
--
drop table calctarifa;
create table calctarifa
(
	eje_nume int not null,  -- Año
	cta_semana int not null, -- Numero semana	
	cta_linea int not null, -- Numero Linea
	pve_codi varchar(15) not null, -- Referencia producto Venta
	cta_kgexis float not null,
	cta_prmex float not null,
	cta_kgcomp float not null,
	cta_prcomp float not null,
	cta_kgprod float not null,
	cta_prprod float not null,
	cta_coscal float not null,-- Costo Calculado
	cta_costo float not null,
	cta_cosasi float not null,
	constraint ix_calctarifa primary  key (eje_nume,cta_semana,cta_linea)
);
grant all on calctarifa to public;
--
-- Tipos Tareas de Operarios
--
create table tipostareaope
(
    tto_codi smallint not null, -- Tarea Operario
    tto_nomb varchar(50) not null, -- Descripción Tarea Operario
    sbe_codi smallint default 0, -- Seccion de Empresa
    tto_activa smallint default -1, -- Activa ? (0:NO)
    constraint ix_tipotarope primary  key (tto_codi)
);
grant all on tipostareaope to public;
--
-- Tabla tareas Operarios
---
create table tareasoperarios
(
    usu_codi varchar(8) not null, -- Operario
    tao_fecha date not null,      -- Fecha
    tao_horini varchar(4) not null, -- Hora Inicio (Formato HHMM)
    tao_horfin varchar(4),          -- Hora Final
    tto_codi smallint not null,     -- Tipo Tarea
    tto_coment varchar(256),        -- Comentario
    constraint ix_tareasoper primary  key (usu_codi,tao_fecha,tao_horini)
);
create table preciosmedios
(
pro_codi int not null,
prm_costo float not null
);
--
-- Tabla ajustes costos productos
-- 
create table costoprod
(
	cap_nume int not null,				-- Identificador
	cap_linea smallint not null,		-- Linea en la carga
	cap_fecha date not null,			-- Fecha Variacion costo
	pro_codi int  not null,				-- Codigo Producto	
	sbe_codi int not null default 0,	-- Sección Empresa (0:todas)	
	cap_costo float not null,			-- Costo a Incrementar.	
	constraint ix_costoprod primary  key (cap_nume,cap_linea)
);
create index ix_costopro1 on  costoprod (cap_fecha,pro_codi);
-- select 'insert into prodtarifa values(''' || pro_codart || ''',' ||  pro_codi || ');' from v_articulo where '' || pro_codi!=pro_codart
--drop view v_partes;
create view anjelica.v_partes as select c.*,l.par_linea,l.pro_codi,pal_kilos,pal_unidad,
pro_ejelot,pro_serlot,pro_numlot,pro_indlot,pro_feccad,pal_acsala, pal_comsal,pal_accion,pal_coment from anjelica.partecab as c,anjelica.partelin as l where c.par_codi=l.par_codi;
grant select on anjelica.v_partes to public;
--
-- Tabla Tiempos preparacion pedidos/Despieces
-- 
create table tiempostarea
(
	usu_nomb varchar(15) not null,	    -- Usuario
	tit_tipdoc char(1) not null,		-- "Pedido","D" Despiece
	tit_id int not null,				-- Identificador documento
	tit_tiempo float not null, 			-- Tiempo Identificador	
	constraint ix_tiempostarea primary  key (usu_nomb,tit_tipdoc,tit_id)
);
grant all on anjelica.tiempostarea to public;
--drop view v_cliprv;
create view anjelica.v_cliprv as 
select 'E' as tipo, cli_codi as codigo, cli_nomb as nombre from anjelica.clientes 
union all
select 'C' AS tipo,prv_codi as codigo, prv_nomb as nombre from anjelica.v_proveedo;
grant select on anjelica.v_cliprv to public;
--- Constraints tipo Foreign Key
alter table anjelica.v_albavec add constraint avc_procl foreign key (cli_codi)
    references anjelica.clientes(cli_codi);
alter table anjelica.v_albavel add constraint avl_profk foreign key (pro_codi) 
    references anjelica.v_articulo(pro_codi);
	
alter table anjelica.v_articulo add constraint fam_profk
   foreign key (fam_codi) references anjelica.v_famipro(fpr_codi) DEFERRABLE INITIALLY DEFERRED;
   
alter table anjelica.v_famipro add constraint grp_codfk
   foreign key (emp_codi,agr_codi) references anjelica.v_agupro(emp_codi,agr_codi)  DEFERRABLE INITIALLY DEFERRED;
alter table anjelica.v_albacol add constraint avl_profk foreign key (pro_codi) references anjelica.v_articulo(pro_codi);
alter table anjelica.v_albcompar add constraint acp_profk foreign key (pro_codi) references anjelica.v_articulo(pro_codi);
alter table anjelica.v_albcompar add constraint acl_cabel foreign key (emp_codi,acc_ano,acc_serie,acc_nume,acl_nulin)
references anjelica.v_albacol(emp_codi,acc_ano,acc_serie,acc_nume,acl_nulin) DEFERRABLE INITIALLY DEFERRED;
alter table anjelica.v_albacol add constraint acc_cabec foreign key (emp_codi,acc_ano,acc_serie,acc_nume)
references anjelica.v_albacoc(emp_codi,acc_ano,acc_serie,acc_nume) DEFERRABLE INITIALLY DEFERRED;

alter table anjelica.v_albavel add constraint avl_cabec foreign key (emp_codi,avc_ano,avc_serie,avc_nume)
references anjelica.v_albavec(emp_codi,avc_ano,avc_serie,avc_nume) DEFERRABLE INITIALLY DEFERRED;
alter table anjelica.albvenserl add constraint avs_nume_l foreign key (avs_nume) references anjelica.albvenserc(avs_nume);
alter table anjelica.albvenseri add constraint avs_nume_i foreign key (avs_nume) references anjelica.albvenserc(avs_nume);
alter table anjelica.v_albvenpar add constraint avl_cabel foreign key (emp_codi,avc_ano,avc_serie,avc_nume,avl_numlin)
references anjelica.v_albavel(emp_codi,avc_ano,avc_serie,avc_nume,avl_numlin) DEFERRABLE INITIALLY DEFERRED;


create view anjelica.v_albvenserv as
SELECT a.alm_codori,a.alm_codfin,a.avc_fecha,a.sbe_codi,c.avs_nume,c.avs_fecha,c.avc_ano,
c.emp_codi,c.avc_serie,c.avc_nume,
c.cli_codi,l.avs_numlin,l.pro_codi,l.pro_nomb,l.avs_canti AS avl_canti,
l.avs_unid,i.avi_numlin,i.avs_ejelot,i.avs_emplot,i.avs_serlot,
i.avs_numpar,i.avs_numind,i.avs_numuni,i.avs_canti
FROM albvenserc c, albvenserl l, albvenseri i,v_albavec as a
WHERE l.avs_nume = c.avs_nume AND i.avs_nume = c.avs_nume
   AND l.avs_numlin = i.avs_numlin
   and c.emp_codi = a.emp_codi 
   AND c.avc_serie = a.avc_serie
   aND c.avc_nume = a.avc_nume 
   and c.avc_ano = a.avc_ano ;

--
-- Vista para mantener compatibilidad
--
create  view v_despori as select  1 as emp_codi, c.*, 1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from desporig as c, desorilin as l where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi;
create  view v_hisdespori as select  1 as emp_codi, c.*, 1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from deorcahis as c, deorlihis as l where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi and c.his_rowid=l.his_rowid;
create  view v_despsal as select c.eje_nume,c.deo_codi,c.deo_numdes,tid_codi,deo_fecha, deo_almori,deo_almdes,deo_ejloge,
deo_seloge,deo_nuloge,deo_incval,
l.def_orden, pro_codi, def_ejelot, def_emplot, def_serlot, pro_lote,pro_numind ,def_kilos,def_numpie,def_prcost,
def_feccad,def_preusu,def_tiempo,l.alm_codi
 from anjelica.desporig as c, anjelica.v_despfin as l where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi;
 
create  view v_despiece as select  1 as emp_codi, c.*, g.grd_serie,g.grd_kilo,grd_unid,grd_prmeco,grd_incval,
grd_valor,grd_block,grd_feccad,g.prv_codi as grd_prvcod,grd_desnue,grd_fecpro,grd_fecha,
1 as deo_emloge, 1 as deo_emplot ,
l.del_numlin, pro_codi, deo_ejelot,  deo_serlot, pro_lote,pro_numind , deo_prcost, deo_kilos , deo_preusu,deo_tiempo
 from desporig as c left join grupdesp as g on c.eje_nume=g.eje_nume and c.deo_numdes = g.grd_nume, 
 desorilin as l 
  where c.eje_nume=l.eje_nume
 and c.deo_codi= l.deo_codi;
-- Procedures

CREATE OR REPLACE FUNCTION anjelica."getBI_albven"("empCodi" integer, "ejeNume" integer, "avcSerie" character, "avcNume" integer)
  RETURNS numeric AS
$BODY$DECLARE
linalb RECORD;
implinT numeric;
implin numeric;
avcDtopp numeric;
avcDtocom numeric;
cliReceq numeric;
cliCodi integer;
basImp numeric;
dtos numeric;
impDtos numeric;

BEGIN
-- raise notice 'Empresa % ', "empCodi";
-- raise notice 'Ejercicio %',"ejeNume";
-- raise notice 'Serie: %',"avcSerie";
-- raise notice 'Numero: %',"avcNume";


 implinT :=0;
 FOR linalb IN
select *
from anjelica.v_albavel where emp_codi = "empCodi"
and avc_ano = "ejeNume"
and avc_nume = "avcNume"
and avc_serie = "avcSerie" LOOP
implin := linalb.avl_canti *  (linalb.avl_prven - linalb.avl_dtolin);
-- raise notice 'IMPORTE LINEA: %',implin;
select round(implin,2) into implin;
implinT := implinT + implin;
END LOOP;

IF implinT = 0 then
  return 0;
end if;
-- raise notice 'IMPORTE LINEAS: %',implinT;
SELECT c.cli_codi,avc_dtopp,avc_dtocom, cli_recequ
 into cliCodi,avcDtopp,avcDtocom,cliReceq
 FROM anjelica.v_albavec c, anjelica.clientes cl WHERE c.avc_ano = "ejeNume"
         and c.emp_codi = "empCodi"
         and c.avc_serie = "avcSerie"
        and c.avc_nume = "avcNume"
        and cl.cli_codi = c.cli_codi;
if not found then
  return 0;
end if;
dtos := avcDtopp+avcDtocom;
basImp := implinT;
if dtos > 0 then

  impDtos := (basImp * dtos / 100);
  select round(impDtos,2) into impDtos;
  -- raise notice 'Imp. Dtos: %',impDtos;
  basImp := basImp - impDtos;
end if;

return basImp;
END;$BODY$
  LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION anjelica."getImp_albven"("empCodi" integer, "ejeNume" integer, "avcSerie" character, "avcNume" integer)
  RETURNS numeric AS
$BODY$DECLARE
linalb RECORD;
implinT numeric;
implin numeric;
avcDtopp numeric;
avcDtocom numeric;
cliReceq numeric;
cliCodi integer;
basImp numeric;
dtos numeric;
impDtos numeric;
porcIva numeric;
porcReq numeric;
tipoIva integer;
impIva numeric;
impReq numeric;
cliExeIVA integer;
impAlb numeric;
avcFecAlb date;
BEGIN
-- raise notice 'Empresa % ', "empCodi";
-- raise notice 'Ejercicio %',"ejeNume";
-- raise notice 'Serie: %',"avcSerie";
-- raise notice 'Numero: %',"avcNume";


 implinT :=0;
 porcIva= 0;
 select  avc_fecalb into avcFecAlb from anjelica.v_albavec
    where  emp_codi = "empCodi"
         and avc_ano = "ejeNume"
         and avc_nume = "avcNume"
         and avc_serie = "avcSerie";
 if not found then
    return 0;
 end if;

 FOR linalb IN
    select  l.pro_codi,sum(l.avl_canti) as avl_canti,
         avl_prven-avl_dtolin as avl_prven,pro_tipiva FROM anjelica.V_ALBAVEL as l, 
		 anjelica.v_articulo as a 
         where l.emp_codi = "empCodi"
         and avc_ano = "ejeNume"
         and avc_nume = "avcNume"
         and avc_serie = "avcSerie"
         and l.pro_codi = a.pro_codi
         and l.avl_canti != 0
         group by l.pro_codi,avl_prven,avl_dtolin,pro_tipiva
 LOOP
implin := linalb.avl_canti *  linalb.avl_prven;
if porcIva = 0 then
 SELECT tii_iva,tii_rec into porcIva,porcReq FROM anjelica.tiposiva
   WHERE tii_codi =  linalb.pro_tipiva
    and  avcFecAlb between tii_fecini and tii_fecfin;
 if not found then
   RAISE EXCEPTION 'Tipo IVA: % NO ENCONTRADO en Alb:% % % %',tipoIva,"empCodi","ejeNume","avcSerie","avcNume";
   return 0;
 end if;
 -- raise notice 'PORCENT. IVA: %',porcIva;
end if;
-- raise notice 'IMPORTE LINEA: %',implin;

select round(implin,2) into implin;
implinT := implinT + implin;
END LOOP;

IF implinT = 0 then
  return 0;
end if;
-- raise notice 'IMPORTE LINEAS: %',implinT;
SELECT c.cli_codi,cli_exeiva,avc_dtopp,avc_dtocom, cli_recequ
 into cliCodi,cliExeIVA,avcDtopp,avcDtocom,cliReceq
 FROM anjelica.v_albavec c, anjelica.clientes cl WHERE c.avc_ano = "ejeNume"
         and c.emp_codi = "empCodi"
         and c.avc_serie = "avcSerie"
        and c.avc_nume = "avcNume"
        and cl.cli_codi = c.cli_codi;
if not found then
  return 0;
end if;
dtos := avcDtopp+avcDtocom;
basImp := implinT;
if dtos > 0 then
  impDtos := (basImp * dtos / 100);
  select round(impDtos,2) into impDtos;
  -- raise notice 'Imp. Dtos: %',impDtos;
  basImp := basImp - impDtos;
end if;
select round(basImp,2) into basImp;
-- raise notice 'Base Imponible: %',basImp;
impIva := 0;
impReq := 0;
-- raise notice 'Exento iva %',cliExeIVA;
if cliExeIVA = 0 and "empCodi" < 90 then
  impIva := basImp * porcIva / 100;
  select round(impIva,2) into impIva;

  -- raise notice 'Imp. IVA: %',impIva;

  if  cliReceq <> 0 then
     impReq = basImp * porcReq / 100;
     select round(impReq ,2) into impReq ;
  end if ;
-- raise notice 'Imp. REQ: %',impReq;
end if;
impAlb=basImp+impIva+impReq;
return impAlb;

END;$BODY$
  LANGUAGE 'plpgsql';

 -- Function: "insertRegStock"(integer, integer, character, integer, integer, integer)

-- DROP FUNCTION "insertRegStock"(integer, integer, character, integer, integer, integer);

CREATE OR REPLACE FUNCTION "insertRegStock"("empCodi" integer, "ejeNume" integer,
 "avcSerie" character, "avcNume" integer, tircodi integer, prvcodi integer)
  RETURNS numeric AS
$BODY$DECLARE
DECLARE    linalb RECORD;
nreg numeric;
nlin numeric;

BEGIN

 nlin:=0;
 select max(rgs_nume) into nreg from regalmacen;
 nreg :=nreg+1;
 FOR linalb IN
select pro_codi,avc_fecalb,
avp_ejelot,avp_serlot,avp_numpar,avp_numind,avp_canti,alm_codori,sbe_codi,usu_nomb,avl_prven
from v_albventa_detalle where emp_codi = "empCodi"
	and avc_ano = "ejeNume"
	and avc_nume = "avcNume"
	and avc_serie = "avcSerie" 
LOOP

 -- raise notice 'Canti: % ',linalb.avp_canti;
 -- raise notice 'Fecha: % ',linalb.avc_fecalb;
 -- raise notice 'Numero Registro %',nreg;
  
  INSERT INTO regalmacen (pro_codi,rgs_fecha,rgs_nume,eje_nume,emp_codi,pro_serie,
  pro_nupar,pro_numind,tir_codi, rgs_canti,alm_codi,
   rgs_recprv,sbe_codi,rgs_partid,usu_nomb,rgs_prebas,rgs_prmeco,rgs_prulco,rgs_prregu,rgs_kilos,
   rgs_clidev,rgs_kilant,rgs_trasp,rgs_numer,rgs_cliprv,rgs_coment,rgs_fecres,acc_ano,acc_serie,acc_nume) 
  values ( linalb.pro_codi,linalb.avc_fecalb,nreg,linalb.avp_ejelot,1,linalb.avp_serlot,
  linalb.avp_numpar,linalb.avp_numind,tirCodi,linalb.avp_canti,linalb.alm_codori,
  2,linalb.sbe_codi,0,linalb.usu_nomb,linalb.avl_prven,linalb.avl_prven,linalb.avl_prven, linalb.avl_prven,linalb.avp_canti,
  0,0,-1,'',prvCodi,'Automatico a traves de insertRegStock',null,0,'',0);
nreg :=nreg+1;
nlin :=nlin+1;
end loop;
delete from v_albvenpar where emp_codi = "empCodi"
	and avc_ano = "ejeNume"
	and avc_nume = "avcNume"
	and avc_serie = "avcSerie";
delete from v_albavel where emp_codi = "empCodi"
	and avc_ano = "ejeNume"
	and avc_nume = "avcNume"
	and avc_serie = "avcSerie";
delete from v_albavec where emp_codi = "empCodi"
	and avc_ano = "ejeNume"
	and avc_nume = "avcNume"
	and avc_serie = "avcSerie" ;

return nlin;

END;

$BODY$
  LANGUAGE plpgsql VOLATILE;


-- Ejemplo de utilizacion
-- select * from "insertRegStock"(1,2013,'A'::character,3596,13,1);
-- update v_albavec set avc_basimp = "getBI_albven"(EMP_CODI,avc_ano,avc_serie,avc_nume)
-- WHERE   AVC_ANO=2010 AND avc_fecalb > '20100601'


--- Utilidades
-- Select para comprobar si ha habido una venta antes de q haya stock.
--
select c.avc_fecalb,l.*  from v_albavec  as c,v_albvenpar as l where
c.avc_fecalb between '20101030'  and '20101130'
and c.avc_ano = l.avc_ano
and c.emp_codi = l.emp_codi
and c.avc_nume =l.avc_nume
and c.avc_serie = l.avc_serie
-- and l.pro_codi= 40811
and c.avc_fecalb < (select min(stp_feccre) from v_stkpart as s where s.emp_codi=l.avp_emplot
and s.eje_nume= l.avp_ejelot
and s.pro_serie =l.avp_serlot
and s.pro_nupar = l.avp_numpar
and s.pro_numind = l.avp_numind
and s.pro_codi = l.pro_codi)
--
-- Para dar permisos a todas las tablas a un usuario
--
SELECT 'GRANT SELECT ON ' || relname || ' TO xxx;'
FROM pg_class JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
WHERE nspname = 'public' AND relkind IN ('r', 'v');
  on anjelica.mvtosalm for each row execute procedure anjelica.fn_actstk();


create trigger albvenpar_insert AFTER insert  on anjelica.v_albvenpar for each row execute procedure anjelica.fn_mvtoalm();
create trigger albvenpar_update BEFORE UPDATE OR DELETE  on anjelica.v_albvenpar for each row execute procedure anjelica.fn_mvtoalm();
create trigger albavel_UPDATE AFTER UPDATE  on anjelica.v_albavel for each row   WHEN (OLD.avl_prven IS DISTINCT FROM NEW.avl_prven) execute procedure anjelica.fn_acpralb();
create trigger albavec_UPDATE AFTER UPDATE  on anjelica.v_albavec for each row  
 WHEN (OLD.cli_codi IS DISTINCT FROM NEW.cli_codi or OLD.avc_fecalb  IS DISTINCT FROM NEW.avc_fecalb ) 
 execute procedure anjelica.fn_acpralb();

create trigger albcompar_insert AFTER insert  on anjelica.v_albcompar for each row execute procedure anjelica.fn_mvtoalm();
create trigger albcompar_update BEFORE UPDATE OR DELETE  on anjelica.v_albcompar for each row execute procedure anjelica.fn_mvtoalm();
create trigger v_albacol_UPDATE AFTER UPDATE  on anjelica.v_albacol for each row  WHEN (OLD.acl_prcom IS DISTINCT FROM NEW.acl_prcom OR OLD.alm_codi IS DISTINCT FROM NEW.alm_codi) execute procedure anjelica.fn_acpralb();
create trigger v_albacoc_UPDATE AFTER UPDATE  on anjelica.v_albacoc for each row  WHEN (OLD.prv_codi IS DISTINCT FROM NEW.prv_codi or OLD.acc_fecrec  IS DISTINCT FROM NEW.acc_fecrec  ) execute procedure anjelica.fn_acpralb();

create trigger v_despfin_insert AFTER insert  on anjelica.v_despfin for each row execute procedure anjelica.fn_mvtoalm();
create trigger v_despfin_update BEFORE UPDATE OR DELETE  on anjelica.v_despfin for each row execute procedure anjelica.fn_mvtoalm();
create trigger desporig_update AFTER UPDATE on anjelica.desporig  for each row  WHEN (OLD.prv_codi IS DISTINCT FROM NEW.prv_codi )  execute procedure anjelica.fn_acpralb();


create trigger desorilin_insert AFTER insert  on anjelica.desorilin for each row execute procedure anjelica.fn_mvtoalm();
create trigger desorilin_update BEFORE UPDATE OR DELETE  on anjelica.desorilin for each row execute procedure anjelica.fn_mvtoalm();

create trigger regstock_insert AFTER insert  on anjelica.regalmacen for each row execute procedure anjelica.fn_mvtoalm();
create trigger regstock_update BEFORE UPDATE OR DELETE  on anjelica.regalmacen for each row execute procedure anjelica.fn_mvtoalm();

create trigger mvtosalm_insert BEFORE insert  on anjelica.mvtosalm for each row execute procedure anjelica.fn_actstk();
create trigger mvtosalm_update BEFORE UPDATE OR DELETE  on anjelica.mvtosalm for each row execute procedure anjelica.fn_actstk();

create trigger stkpart_insert BEFORE insert OR UPDATE  on anjelica.stockpart for each row   WHEN (NEW.pro_serie !='S' ) execute procedure anjelica.fn_acumstk();
create trigger stkpart_delete BEFORE  DELETE  on anjelica.stockpart for each row WHEN (OLD.pro_serie !='S' ) execute procedure anjelica.fn_acumstk();
