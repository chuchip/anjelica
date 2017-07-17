-- Function: fn_actstk()

-- DROP FUNCTION fn_actstk();

CREATE OR REPLACE FUNCTION fn_actstk()
  RETURNS trigger AS
$BODY$   
  DECLARE   
  STKNEW RECORD;
  STKOLD RECORD;
  acpFecpro DATE;
  acpNucrot CHAR(30);
  acpPainac char(2);
  mvtFeccad DATE;
  acpPaisac char(2);
  acpFecsac DATE;
  matCodi int;
  sdeCodi int;
  acpEngpai char(2);  
  camCodi char(2);
  kilos float;
  unid int;
  BEGIN
        kilos=0;
        unid=0;      
--RAISE  NOTICE 'oPERACION %',TG_OP;	
        if TG_OP =  'INSERT' or TG_OP =  'UPDATE' then 
            select * INTO STKNEW FROM  anjelica.stockpart where 
                pro_codi= NEW.pro_codi and
                eje_nume= NEW.pro_ejelot  and 
                pro_serie = NEW.pro_serlot and
                pro_nupar = NEW.pro_numlot and
                pro_numind  = NEW.pro_indlot and
                alm_codi = NEW.alm_codi;                
            if STKNEW is  null then
--		RAISE  NOTICE 'NO EXISTIA STOCK-PARTIDAS ';	
            -- No existe ese individuo en stock-partidas
                kilos=NEW.mvt_canti;
                unid=NEW.mvt_unid;
                if NEW.mvt_tipo = 'S' then
                        kilos=kilos*-1;
                        unid= unid * -1;
                end if;
		select cam_codi INTO  camCodi FROM  anjelica.v_articulo where 
			pro_codi= NEW.pro_codi;

		acpFecpro=null;
		acpNucrot=null;
		acpPainac=null;
		mvtFeccad=NEW.mvt_feccad;
		acpPaisac=null;
		acpFecsac=null;
		matCodi=null;
		sdeCodi=null;
		acpEngpai=null;
		--RAISE  NOTICE 'Tipo documento %',NEW.mvt_tipdoc;
		if NEW.mvt_tipdoc = 'C' then					
			select acp_fecpro,acp_nucrot,acp_painac,acp_paisac,acp_fecsac,mat_codi,sde_codi,acp_engpai 
			into acpFecpro,acpNucrot,acpPainac,acpPaisac,acpFecsac,matCodi,sdeCodi,acpEngpai 
				from anjelica.v_albcompar where 
				pro_codi=NEW.pro_codi -- Codigo de producto.
				and NEW.pro_ejelot=acc_ano
				and NEW.pro_serlot=acc_serie
				and NEW.pro_numlot=acc_nume
				and NEW.pro_indlot=acp_numind;
			-- RAISE NOTICE 'buscando registro en compras. Fec.Cad %',mvtFeccad;
		end if;
		if NEW.mvt_tipdoc = 'd' then					 
			select deo_fecpro,deo_fecsac
			into acpFecpro,acpFecsac
				from anjelica.v_despori where 						
				 NEW.mvt_ejedoc=eje_nume
				 and NEW.mvt_numdoc=deo_codi;
			--RAISE NOTICE 'buscando registro en Despieces. Doc: % - % Fec.Cad %',NEW.mvt_ejedoc,NEW.mvt_numdoc,mvtFeccad;
		end if;
                insert into anjelica.stockpart (eje_nume,
                    emp_codi,pro_serie, stp_tiplot,pro_nupar,                    
                    pro_codi,pro_numind,alm_codi,
                    stp_unini,stp_unact,
                    stp_feccre,
					stp_kilini,stp_kilact,
					prv_codi,stp_feccad,cam_codi,
					stp_fecpro ,	 -- Fecha Produccion.
					stp_nucrot, -- Numeo Crotal
					stp_painac ,		 -- Pais de Nacimiento
					stp_engpai ,		-- Pais de engorde
					stp_paisac ,		-- Pais de Sacrificio
					stp_fecsac ,	-- Fecha Sacrificio					
					mat_codi ,		-- Matadero
					sde_codi )		-- Sala despiece
                 values
                 (
                        NEW.pro_ejelot, 
						NEW.mvt_empcod,NEW.pro_serlot,'P', NEW.pro_numlot,
                        NEW.pro_codi,NEW.pro_indlot,NEW.alm_codi,
						unid,unid,
						current_timestamp,
                        kilos,kilos,
                        NEW.mvt_cliprv,mvtFeccad,camCodi,
						acpFecpro,acpNucrot,acpPainac,acpEngpai,acpPaisac,acpFecsac,matCodi,sdeCodi);
            else
                if NEW.mvt_tipo='E' then
                   kilos=STKNEW.stp_kilact+NEW.mvt_canti;
                   unid=STKNEW.stp_unact+NEW.mvt_unid;
                else
                   kilos=STKNEW.stp_kilact-NEW.mvt_canti;
                   unid=STKNEW.stp_unact-NEW.mvt_unid;
                end if;
                UPDATE anjelica.stockpart set stp_kilact= kilos,
                        stp_unact=unid,
			prv_codi = NEW.mvt_cliprv,
                        stp_fefici = current_timestamp 
			WHERE   pro_codi = NEW.pro_codi and 
			 eje_nume=NEW.pro_ejelot  and 
                         pro_codi= NEW.pro_codi and
                         pro_serie = NEW.pro_serlot and
                         pro_nupar = NEW.pro_numlot and
                         pro_numind  = NEW.pro_indlot AND
                         alm_codi = NEW.alm_codi;
                 --RAISE NOTICE  'Saliendo de fn_acstk - 0 % ', NEW;                
            end if;
        end if;
        if TG_OP = 'UPDATE' or TG_OP = 'DELETE' then
            select * INTO STKOLD FROM  anjelica.stockpart where 
                pro_codi= OLD.pro_codi and
                eje_nume= OLD.pro_ejelot  and 
                pro_serie = OLD.pro_serlot and
                pro_nupar = OLD.pro_numlot and
                pro_numind  = OLD.pro_indlot and
                alm_codi = OLD.alm_codi;     
	    if STKOLD is null then
			RAISE EXCEPTION 'No encontrado Apunte stock anterior Almacen:% Art: % Individuo:% % %-%',OLD.alm_codi,
			OLD.pro_codi,OLD.pro_ejelot,OLD.pro_serlot,OLD.pro_numlot,OLD.pro_indlot;
		return null;
	    end if;
            if OLD.mvt_tipo='E' then
                 kilos=STKOLD.stp_kilact-OLD.mvt_canti;
                 unid=STKOLD.stp_unact-OLD.mvt_unid;
            else
                 kilos=STKOLD.stp_kilact+OLD.mvt_canti;
                 unid=STKOLD.stp_unact+OLD.mvt_unid;
            end if;
            UPDATE anjelica.stockpart set stp_kilact= kilos,
                   stp_unact=unid,
                   stp_fefici = current_timestamp 
            WHERE  pro_codi = OLD.pro_codi and
				   eje_nume=OLD.pro_ejelot  and 
                   pro_serie = OLD.pro_serlot and
                   pro_nupar = OLD.pro_numlot and
                   pro_numind  = OLD.pro_indlot AND
                   alm_codi = OLD.alm_codi;
             --RAISE NOTICE  'Saliendo de fn_acstk1 % ',OLD;
             if TG_OP = 'UPDATE' then
				RETURN NEW;
			 end if;
			 RETURN OLD;
        end if;
        
	return NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fn_actstk()
  OWNER TO anjelica;
