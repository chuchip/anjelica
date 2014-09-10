
CREATE OR REPLACE FUNCTION anjelica."fn_mvtoalm"()
  RETURNS TRIGGER AS $grabar$  
  DECLARE   
  almCodi int;
  almOrig int;
  almFinal int;
  nRows int;
  cuantos int;
  tipoMvto char(1);
  mvtPrven numeric;
  mvtFecdoc date;
  mvtFecmvt timestamp;
  mvtCliprv int;
  ajuRegmvt int;
  mvtFeccad date;
  BEGIN

-- Albaranes de Venta
   select aju_regmvt into ajuRegmvt from anjelica.ajustedb;
	if not found then
		RAISE EXCEPTION 'NO encontrado Ajustes DB';
	end if;
	if ajuRegmvt = 0 then
		if TG_OP = 'UPDATE' or TG_OP  ='INSERT' then
		  RETURN NEW;
		else
		  return OLD;
		end if;        	
	end if;
	if TG_TABLE_NAME = 'v_albvenpar' then
	   if TG_OP =  'INSERT' OR TG_OP = 'UPDATE' then
	     select l.avl_prven,c.avc_fecalb,l.avl_fecalt,alm_codori,
	          alm_coddes, alm_codi,c.cli_codi
		 into mvtPrven,mvtFecdoc,mvtFecmvt,almOrig,almFinal,almCodi,mvtCliprv
	         from anjelica.v_albavel as l,anjelica.v_albavec as c where 
			c.emp_codi=l.emp_codi and
			c.avc_ano=l.avc_ano and
			c.avc_nume=l.avc_nume and
			c.avc_serie=l.avc_serie and
                        l.avl_numlin = NEW.avl_numlin and
			c.emp_codi=NEW.emp_codi and
			c.avc_ano=NEW.avc_ano and
			c.avc_nume=NEW.avc_nume and
			c.avc_serie=NEW.avc_serie;
		if not found then
			RAISE EXCEPTION 'NO encontrada cabecera o linea de albaran venta';
		end if;
	   end if;
	   if TG_OP =  'INSERT' then
		if  NEW.avc_serie = 'X' THEN
		 -- Traspaso entre Almacenes.
			 INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
				mvt_tipo , mvt_tipdoc ,
				alm_codi, 
				mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
				mvt_serdoc , mvt_numdoc ,mvt_lindoc,
				pro_codi  , 
				pro_ejelot ,pro_serlot, 
				pro_numlot ,pro_indlot ,
				mvt_canti, mvt_unid , mvt_prec,mvt_cliprv )
			values 		
			(
			TG_OP,
			current_timestamp,
			'E','V',
			almFinal,
			mvtFecdoc,
			NEW.emp_codi,
			NEW.avc_ano,
			NEW.avc_serie,
			NEW.avc_nume,
			NEW.avl_numlin,
			NEW.pro_codi,
			NEW.avp_ejelot,
			NEW.avp_serlot,
			NEW.avp_numpar,
			NEW.avp_numind,
			NEW.avp_canti,
			NEW.avp_numuni,
			mvtPrven,
                        mvtCliprv
			);
			almCodi=almOrig;
		end if;
		INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
			mvt_tipo , mvt_tipdoc ,
			alm_codi, 
			mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
			mvt_serdoc , mvt_numdoc ,mvt_lindoc,
			pro_codi  , 
			pro_ejelot ,pro_serlot, 
			pro_numlot ,pro_indlot ,
			mvt_canti, mvt_unid , mvt_prec,mvt_cliprv )
		values 		
		(
		TG_OP,
		current_timestamp,
		'S','V',
		almCodi,
		mvtFecdoc,
		NEW.emp_codi,
		NEW.avc_ano,
		NEW.avc_serie,
		NEW.avc_nume,
		NEW.avl_numlin,
		NEW.pro_codi,
		NEW.avp_ejelot,
		NEW.avp_serlot,
		NEW.avp_numpar,
		NEW.avp_numind,
		NEW.avp_canti,
		NEW.avp_numuni,
		mvtPrven,
                mvtCliprv
		);
		return NEW;
	    end if;
	    if TG_OP =  'UPDATE' then
		-- RAISE NOTICE 'En update Albaran venta';
		update anjelica.mvtosalm set mvt_oper= TG_OP,
			alm_codi=almCodi,
		        mvt_time=mvtFecdoc,
		        mvt_empcod =NEW.emp_codi,
		        mvt_ejedoc=NEW.avc_ano,
		        mvt_numdoc=NEW.avc_nume,
		        mvt_serdoc=NEW.avc_serie,
			pro_codi  = NEW.pro_codi,
			pro_ejelot =NEW.avp_ejelot,
			pro_serlot = NEW.avp_serlot,
			pro_numlot =NEW.avp_numpar,
			pro_indlot =NEW.avp_numind,
			mvt_canti=NEW.avp_canti,
			mvt_unid =NEW.avp_numuni,
			mvt_prec = mvtPrven ,
                        mvt_cliprv = mvtCliprv
                WHERE   mvt_tipdoc='V' and			
		        mvt_empcod =OLD.emp_codi and
		        mvt_ejedoc=OLD.avc_ano and
		        mvt_numdoc=OLD.avc_nume and
		        mvt_serdoc=OLD.avc_serie and
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.avp_ejelot AND
			pro_serlot = OLD.avp_serlot AND
			pro_numlot =OLD.avp_numpar AND
			pro_indlot =OLD.avp_numind and 
			mvt_lindoc = OLD.avl_numlin and
			mvt_canti = OLD.avp_canti;
		GET DIAGNOSTICS nRows = ROW_COUNT;
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a modificar. Alb. Venta';
			RETURN null;
		end if;
    	if nRows > 1 then
			RAISE EXCEPTION '(Update)Encontrado mas de un mvto para este individuo de venta';
			return null;
		end if;

		return NEW;
	    end if;
	    if TG_OP =  'DELETE' then		  
	       -- RAISE NOTICE  'borrando mvto de alb. venta';		      
	       DELETE FROM anjelica.mvtosalm  where
			mvt_tipdoc='V' and			
		        mvt_empcod =OLD.emp_codi and
		        mvt_ejedoc=OLD.avc_ano and
		        mvt_numdoc=OLD.avc_nume and
		        mvt_serdoc=OLD.avc_serie and
		        pro_codi  = OLD.pro_codi AND			
			pro_ejelot =OLD.avp_ejelot AND
			pro_serlot = OLD.avp_serlot AND
			pro_numlot =OLD.avp_numpar AND
			pro_indlot =OLD.avp_numind and 
			mvt_lindoc = OLD.avl_numlin and
			mvt_canti = OLD.avp_canti;
		GET DIAGNOSTICS nRows = ROW_COUNT;		
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a Borrar. Alb. Venta';
			return null;
		end if;
		if nRows > 1 AND  OLD.avc_serie != 'X' then
		   RAISE EXCEPTION '(Borrar)Encontrado mas de un mvto para este individuo. Alb: % % % % Producto: % Ind: % % %-%',
                            OLD.emp_codi,OLD.avc_ano,OLD.avc_nume,OLD.avc_serie,
                            OLD.pro_codi,OLD.avp_ejelot,OLD.avp_serlot, OLD.avp_numpar,OLD.avp_numind; 
			RAISE EXCEPTION '(Borrar)Encontrado mas de un mvto para este individuo de venta';
			return null;
		end if;
		-- raise NOTICE 'Mvto. Borrado';
		return OLD;
	    end if;
	end if;
-- Albaranes de compra
	if TG_TABLE_NAME = 'v_albcompar' then
	   if TG_OP =  'INSERT' then
	       select l.acl_prcom,c.acc_fecrec,l.alm_codi,prv_codi
			into mvtPrven,mvtFecdoc,almCodi,mvtCliprv
                 from anjelica.v_albacol as l,anjelica.v_albacoc as c
                 where  c.emp_codi=l.emp_codi and
                       c.acc_ano=l.acc_ano and
			c.acc_nume=l.acc_nume and
			c.acc_serie=l.acc_serie and
                        l.acl_nulin=NEW.acl_nulin and
			c.emp_codi=NEW.emp_codi and
			c.acc_ano=NEW.acc_ano and
			c.acc_nume=NEW.acc_nume and
			c.acc_serie=NEW.acc_serie;
		if not found then
			RAISE EXCEPTION 'NO encontrada cabecera o linea de albaran compra';
		end if;
	   end if;
	   if   TG_OP = 'UPDATE' then 
	       select l.acl_prcom,c.acc_fecrec,l.alm_codi,prv_codi
			into mvtPrven,mvtFecdoc,almCodi,mvtCliprv
                 from anjelica.v_albacol as l,anjelica.v_albacoc as c
                 where  c.emp_codi=l.emp_codi and
                       c.acc_ano=l.acc_ano and
			c.acc_nume=l.acc_nume and
			c.acc_serie=l.acc_serie and
                        l.acl_nulin=OLD.acl_nulin and
			c.emp_codi=OLD.emp_codi and
			c.acc_ano=OLD.acc_ano and
			c.acc_nume=OLD.acc_nume and
			c.acc_serie=OLD.acc_serie;
		if not found then
			RAISE EXCEPTION 'NO encontrada cabecera o linea de albaran compra (OLD)';
		end if;
	   end if;
	   if TG_OP =  'INSERT' then			
		INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
			mvt_tipo , mvt_tipdoc ,
			alm_codi,
			mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
			mvt_serdoc , mvt_numdoc ,mvt_lindoc,
			pro_codi  , 
			pro_ejelot ,pro_serlot, 
			pro_numlot ,pro_indlot ,
			mvt_canti, mvt_unid , mvt_prec,mvt_cliprv,mvt_feccad )
		values 		
		(
		TG_OP,
		mvtFecdoc,
--		current_timestamp,
		'E','C',almCodi,mvtFecdoc,
		NEW.emp_codi,NEW.acc_ano,NEW.acc_serie,
		NEW.acc_nume,	NEW.acl_nulin,	NEW.pro_codi,
		NEW.acc_ano,	NEW.acc_serie,	NEW.acc_nume,
		NEW.acp_numind,	NEW.acp_canti,	NEW.acp_canind,
		mvtPrven,  mvtCliprv,   NEW.acp_feccad);
		return NEW;
	    end if;
	    if TG_OP =  'UPDATE' then
		-- RAISE NOTICE 'En update de compras';
		update anjelica.mvtosalm set mvt_oper= TG_OP,
			alm_codi = almCodi,
			pro_codi  = NEW.pro_codi,
			pro_indlot =NEW.acp_numind,
			mvt_canti=NEW.acp_canti,
			mvt_unid =NEW.acp_canind,
			mvt_prec = mvtPrven,
                        mvt_cliprv=mvtCliprv,
                        mvt_feccad= NEW.acp_feccad,
			mvt_lindoc=NEW.acl_nulin
                where	alm_codi = almCodi and
			    mvt_tipdoc='C' and
		        mvt_empcod =OLD.emp_codi and
		        mvt_ejedoc=OLD.acc_ano and
		        mvt_numdoc=OLD.acc_nume and
		        mvt_serdoc= OLD.acc_serie and
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.acc_ano AND
			pro_serlot = OLD.acc_serie AND
			pro_numlot =OLD.acc_nume AND
			pro_indlot =OLD.acp_numind;
		GET DIAGNOSTICS nRows = ROW_COUNT;
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a modificar. Alb. Compra';
			RETURN null;
		end if;
		return NEW;
	    end if;
	    if TG_OP =  'DELETE' then	
	       -- RAISE NOTICE  'borrando mvto';
	       select l.alm_codi
		 into almCodi
                 from anjelica.v_albacol as l
                   where  emp_codi=OLD.emp_codi and
			acc_ano=OLD.acc_ano and
			acc_nume=OLD.acc_nume and
			acc_serie=OLD.acc_serie;
		if not found then
			RAISE EXCEPTION 'NO encontrada lineas de albaran compra  % % % %',
				OLD.emp_codi,OLD.acc_ano,OLD.acc_serie,OLD.acc_nume;
		end if;	
	       DELETE FROM anjelica.mvtosalm  where
			mvt_tipdoc='C' and
			alm_codi = almCodi and
		        mvt_empcod =OLD.emp_codi and
		        mvt_ejedoc=OLD.acc_ano and
		        mvt_numdoc=OLD.acc_nume and
		        mvt_serdoc=OLD.acc_serie and
		        pro_codi  = OLD.pro_codi AND			
			pro_indlot =OLD.acp_numind;
		GET DIAGNOSTICS nRows = ROW_COUNT;	
		RAISE warning 'cucu % ',nRows;	
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a Borrar Almacen % Alb. Compra % % % % Producto: % Ind: %',
                            almCodi,
                            OLD.emp_codi,OLD.acc_ano,OLD.acc_serie,OLD.acc_nume, 
			    OLD.pro_codi,
                            OLD.acp_numind;
			return null;
		end if;		
		return OLD;
	    end if;
	end if;
---  Despieces Entradas de almacen
	if TG_TABLE_NAME = 'v_despfin' then
	    if TG_OP =  'INSERT' OR TG_OP = 'UPDATE' then
	       select deo_fecha,prv_codi into mvtFecdoc,mvtCliprv from anjelica.desporig where 
			eje_nume=NEW.eje_nume and
			deo_codi=NEW.deo_codi;
		if not found then
			RAISE EXCEPTION 'NO encontrada cabecera Despiece Entrada';
		end if;
	   end if;
	   if TG_OP =  'INSERT' then			
		INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
			mvt_tipo , mvt_tipdoc , 
			alm_codi,
			mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
			mvt_serdoc , mvt_numdoc ,mvt_lindoc,
			pro_codi  , 
			pro_ejelot ,pro_serlot, 
			pro_numlot ,pro_indlot ,
			mvt_canti, mvt_unid , mvt_prec,
                        mvt_cliprv,mvt_feccad )
		values 		
		(
		TG_OP,
		current_timestamp,
		'E','d',
		NEW.alm_codi,
		mvtFecdoc,
		NEW.emp_codi,
		NEW.eje_nume,
		'D',
		NEW.deo_codi,
		NEW.def_orden,
		NEW.pro_codi,
		NEW.def_ejelot,
		NEW.def_serlot,
		NEW.pro_lote,
		NEW.pro_numind,
		NEW.def_kilos,
		NEW.def_numpie,
		NEW.def_prcost,
                mvtCliprv, NEW.def_feccad
		);
		return NEW;
	    end if;
	    if TG_OP =  'UPDATE' then
		-- RAISE NOTICE 'En update despieces Entrada (v_despfin)';
		update anjelica.mvtosalm set mvt_oper= TG_OP,
		        mvt_time=NEW.def_tiempo,
			pro_codi  = NEW.pro_codi,
			pro_ejelot =NEW.def_ejelot,
			pro_serlot = NEW.def_serlot,
			pro_numlot =NEW.pro_lote,
			pro_indlot =NEW.pro_numind,
			mvt_canti=NEW.def_kilos,
			mvt_unid =NEW.def_numpie,
			mvt_prec = NEW.def_prcost,
                        mvt_cliprv=mvtCliprv,
                        mvt_feccad= NEW.def_feccad
                where	mvt_tipdoc='d' and		
			alm_codi = OLD.alm_codi AND     
		        mvt_ejedoc=OLD.eje_nume and
		        mvt_numdoc=OLD.deo_codi and		       
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.def_ejelot AND
			pro_serlot = OLD.def_serlot AND
			pro_numlot =OLD.pro_lote AND
			pro_indlot =OLD.pro_numind;
		GET DIAGNOSTICS nRows = ROW_COUNT;
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado Mvto a modificar. Desp. Entrada';
			RETURN null;
		end if;
		return NEW;
	    end if;
	    if TG_OP =  'DELETE' then	
	       -- RAISE NOTICE  'borrando mvto de Desp.Entrada';		      
	       DELETE FROM anjelica.mvtosalm  where
			mvt_tipdoc='d' and     
			alm_codi = OLD.alm_codi AND     
		        mvt_ejedoc=OLD.eje_nume and
		        mvt_numdoc=OLD.deo_codi and		       
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.def_ejelot AND
			pro_serlot = OLD.def_serlot AND
			pro_numlot =OLD.pro_lote AND
			pro_indlot =OLD.pro_numind;
		GET DIAGNOSTICS nRows = ROW_COUNT;		
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a Borrar.Desp. Entrada';
			return null;
		end if;
		-- raise NOTICE 'Mvto. Borrado';
		return OLD;
	    end if;
	end if;	
---  Despieces Salidas d almacen
	if TG_TABLE_NAME = 'desorilin' then
	    if TG_OP =  'INSERT' OR TG_OP = 'UPDATE' then
	       select deo_fecha,deo_almori,prv_codi
                 into mvtFecdoc,almCodi,mvtCliprv
		 from anjelica.desporig where 
			eje_nume=NEW.eje_nume and
			deo_codi=NEW.deo_codi;
		if not found then
			RAISE EXCEPTION 'NO encontrada cabecera de Despiece Salida';
		end if;
	   end if;
	   if TG_OP =  'INSERT' then			
		INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
			mvt_tipo , mvt_tipdoc , 
			alm_codi,
			mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
			mvt_serdoc , mvt_numdoc ,mvt_lindoc,
			pro_codi  , 
			pro_ejelot ,pro_serlot, 
			pro_numlot ,pro_indlot ,
			mvt_canti, mvt_unid , mvt_prec,mvt_cliprv )
		values 		
		(
		TG_OP,
		current_timestamp,
		'S','D',
		almCodi,
		mvtFecdoc,
		1,
		NEW.eje_nume,
		'D',
		NEW.deo_codi,
		NEW.del_numlin,
		NEW.pro_codi,
		NEW.deo_ejelot,
		NEW.deo_serlot,
		NEW.pro_lote,
		NEW.pro_numind,
		NEW.deo_kilos,
		1,
		NEW.deo_prcost,
                mvtCliprv
		);
		return NEW;
	    end if;
	    if TG_OP =  'UPDATE' then
		-- RAISE NOTICE 'En update despieces Salidas (desorilin)';
		update anjelica.mvtosalm set mvt_oper= TG_OP,
		        mvt_time= NEW.deo_tiempo,
			pro_codi  = NEW.pro_codi,
			pro_ejelot =NEW.deo_ejelot,
			pro_serlot = NEW.deo_serlot,
			pro_numlot =NEW.pro_lote,
			pro_indlot =NEW.pro_numind,
			mvt_canti=NEW.deo_kilos,
			mvt_cliprv=mvtCliprv,
			mvt_prec = NEW.deo_prcost
                where 	mvt_tipdoc='D' and		
		        mvt_ejedoc=OLD.eje_nume and
		        mvt_numdoc=OLD.deo_codi and		       
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.deo_ejelot AND
			pro_serlot = OLD.deo_serlot AND
			pro_numlot =OLD.pro_lote AND
			pro_indlot =OLD.pro_numind AND
			MVT_CANTI = OLD.deo_kilos;
		GET DIAGNOSTICS nRows = ROW_COUNT;
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado Mvto a modificar. Desp.Salida Lote:% % Prod: % Indiv:%  Kilos: %',OLD.eje_nume,
				OLD.deo_codi,OLD.pro_codi,OLD.pro_numind,OLD.deo_kilos;
			RETURN null;
		end if;
		return NEW;
	    end if;
	    if TG_OP =  'DELETE' then	
	       -- RAISE NOTICE  'borrando mvto de Desp.Salida';		      
	       DELETE FROM anjelica.mvtosalm  where
			mvt_tipdoc='D' and	
		        mvt_ejedoc=OLD.eje_nume and
		        mvt_numdoc=OLD.deo_codi and		       
		        pro_codi  = OLD.pro_codi AND
			pro_ejelot =OLD.deo_ejelot AND
			pro_serlot = OLD.deo_serlot AND
			pro_numlot =OLD.pro_lote AND
			pro_indlot =OLD.pro_numind and
			MVT_CANTI = OLD.deo_kilos;
		GET DIAGNOSTICS nRows = ROW_COUNT;		
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a Borrar.Desp. Salida % % %',almCodi,OLD.eje_nume,OLD.deo_codi;
			return null;
		end if;
		-- raise NOTICE 'Mvto. Borrado';
		return OLD;
	    end if;
	end if;	
-- Mvtos de Regularizacion
	if TG_TABLE_NAME = 'v_regstock' then
	   if TG_OP =  'INSERT' or TG_OP='UPDATE' then
	        SELECT tir_afestk into tipoMvto FROM anjelica.v_motregu  WHERE 
			tir_codi = NEW.tir_codi;
		if not found then
			RAISE EXCEPTION 'NO encontrado tipo Mvto %',NEW.tir_codi;
		end if;
                if tipoMvto='=' then
			return NEW; -- Ignoro apuntes de Inventario
                end if;
		if tipoMvto='+' then
		    tipoMvto='E';
        else			
            tipoMvto='S';
		end if;
	   end if;
	   if TG_OP =  'INSERT' then		
		INSERT INTO anjelica.mvtosalm (mvt_oper,mvt_time,
			mvt_tipo , mvt_tipdoc , 
			alm_codi,
			mvt_fecdoc,mvt_empcod , mvt_ejedoc , 
			mvt_serdoc , mvt_numdoc ,mvt_lindoc,
			pro_codi  , 
			pro_ejelot ,pro_serlot, 
			pro_numlot ,pro_indlot ,
			mvt_canti, mvt_unid , mvt_prec,mvt_cliprv )
		values 		
		(
		TG_OP,
		current_timestamp,
		tipoMvto,'R',
		NEW.alm_codi,
		NEW.rgs_fecha,
		NEW.emp_codi,
		NEW.eje_nume,
		'R',
		NEW.rgs_nume,
		1, -- mvt_lindoc
		NEW.pro_codi,
		NEW.eje_nume,
		NEW.pro_serie,
		NEW.pro_nupar,
		NEW.pro_numind,
		NEW.rgs_kilos,
		NEW.rgs_canti,
		NEW.rgs_prregu,
                NEW.rgs_cliprv
		);
		return NEW;
	    end if;
	    if TG_OP =  'UPDATE' then
		-- RAISE NOTICE 'En update Regularizaciones ';
		update anjelica.mvtosalm set mvt_oper= TG_OP,
			mvt_numdoc = NEW.rgs_nume,
			mvt_tipo=tipoMvto,
		        mvt_time=NEW.rgs_fecha,
			pro_codi  = NEW.pro_codi,
			pro_ejelot =NEW.eje_nume,
			pro_serlot = NEW.pro_serie,
			pro_numlot =NEW.pro_nupar,
			pro_indlot =NEW.pro_numind,
			mvt_canti=NEW.rgs_kilos,
			mvt_unid =NEW.rgs_canti,
			mvt_prec = NEW.rgs_prregu,
                        mvt_cliprv = NEW.rgs_cliprv
                where	mvt_tipdoc='R' and					
		        mvt_numdoc=OLD.rgs_nume;		      
		GET DIAGNOSTICS nRows = ROW_COUNT;
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado Mvto a modificar. para Regul.';
			RETURN null;
		end if;
		return NEW;
	    end if;
	    if TG_OP =  'DELETE' then	
	       -- RAISE NOTICE  'borrando mvto de Regularizaciones';	
               SELECT tir_afestk into tipoMvto FROM anjelica.v_motregu  WHERE 
			tir_codi = OLD.tir_codi;
		if not found then
			RAISE EXCEPTION 'NO encontrado tipo Mvto %',OLD.tir_codi;
		end if;
                if tipoMvto='=' then
			return OLD; -- Ignoro apuntes de Inventario
                end if;     
	       DELETE FROM anjelica.mvtosalm  where	
			mvt_tipdoc='R' and					
		    mvt_numdoc=OLD.rgs_nume;	
		GET DIAGNOSTICS nRows = ROW_COUNT;		
		if nRows = 0 then
			RAISE EXCEPTION 'No encontrado mvto a Borrar. Regularizacion % ',OLD.rgs_nume;
			return null;
		end if;
		-- raise NOTICE 'Mvto. Borrado';
		return OLD;
	    end if;
	end if;
	return null;
END;
$grabar$ LANGUAGE 'plpgsql';
