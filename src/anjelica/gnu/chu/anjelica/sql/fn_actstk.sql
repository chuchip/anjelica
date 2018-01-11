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
  matCodi char(15);
  sdeCodi char(15);
  acpEngpai char(2);  
  mvtCliprv int;
  camCodi char(2);
  kilos float;
  unid int;
  BEGIN
       

RAISE  NOTICE 'oPERACION %',TG_OP;	
        if TG_OP =  'INSERT' or TG_OP =  'UPDATE' then 
		kilos=0;
		unid=0;      
		acpFecpro=null;
		acpFecsac=null;
		acpNucrot=null;
		acpPainac=null;
		mvtFeccad=NEW.mvt_feccad;
		acpPaisac=null;
		matCodi=null;
		sdeCodi=null;
		acpEngpai=null;
		mvtCliprv=NEW.mvt_cliprv;
            if NEW.mvt_tipdoc = 'd' then					 
                select deo_fecpro,deo_fecsac,prv_codi
                into acpFecpro,acpFecsac,mvtCliprv
                        from anjelica.v_despori where 						
                         NEW.mvt_ejedoc=eje_nume
                         and NEW.mvt_numdoc=deo_codi;
			--RAISE NOTICE 'buscando registro en Despieces. Doc: % - % Fec.Cad %',NEW.mvt_ejedoc,NEW.mvt_numdoc,mvtFeccad;
            end if;
            if NEW.mvt_tipdoc = 'C' then					
                select prv_codi,acp_fecpro,acp_nucrot,acp_painac,acp_paisac,acp_fecsac,acp_matad,acp_saldes,acp_engpai,acp_feccad
                    into mvtCliprv,acpFecpro,acpNucrot,acpPainac,acpPaisac,acpFecsac,matCodi,sdeCodi,acpEngpai,mvtFeccad
                        from anjelica.v_compras where 
                        pro_codi=NEW.pro_codi -- Codigo de producto.
                        and NEW.pro_ejelot=acc_ano
                        and NEW.pro_serlot=acc_serie
                        and NEW.pro_numlot=acc_nume
                        and NEW.pro_indlot=acp_numind;
			-- RAISE NOTICE 'buscando registro en compras. Fec.Cad %',mvtFeccad;
	    end if;
            select * INTO STKNEW FROM  anjelica.stockpart where 
                pro_codi= NEW.pro_codi and
                eje_nume= NEW.pro_ejelot  and 
                pro_serie = NEW.pro_serlot and
                pro_nupar = NEW.pro_numlot and
                pro_numind  = NEW.pro_indlot and
                alm_codi = NEW.alm_codi;                
            if STKNEW is  null then
		RAISE  NOTICE 'NO EXISTIA REGISTRO EN STOCK-PARTIDAS ';	
            -- No existe ese individuo en stock-partidas
                kilos=NEW.mvt_canti;
                unid=NEW.mvt_unid;
                if NEW.mvt_tipo = 'S' then
                    kilos=kilos*-1;
                    unid= unid * -1;
                end if;
		select cam_codi INTO  camCodi FROM  anjelica.v_articulo where 
			pro_codi= NEW.pro_codi;

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
                    stp_matad ,		-- Matadero
                    stp_saldes )		-- Sala despiece
                 values  (
                    NEW.pro_ejelot, 
                    NEW.mvt_empcod,NEW.pro_serlot,'P', NEW.pro_numlot,
                    NEW.pro_codi,NEW.pro_indlot,NEW.alm_codi,
                    unid,unid,
                    current_timestamp,
                    kilos,kilos,
                    mvtCliprv,mvtFeccad,camCodi,
                    acpFecpro,acpNucrot,acpPainac,acpEngpai,acpPaisac,acpFecsac,
                    matCodi,sdeCodi);
            else
	        RAISE  NOTICE 'EXISTIA REGISTRO EN STOCK-PARTIDAS ';	
               if NEW.mvt_tipdoc = 'V' or NEW.mvt_tipdoc = 'R' or NEW.mvt_tipdoc = 'D' or NEW.mvt_tipdoc = 'd'  then
                if NEW.mvt_tipdoc != 'd' then
                    mvtCliprv=STKNEW.prv_codi;
                    acpFecpro=STKNEW.stp_fecpro;
                    acpFecsac=STKNEW.stp_fecsac;
                    mvtFeccad=STKNEW.stp_feccad;
                end if;
                acpNucrot=STKNEW.stp_nucrot;
                acpPainac=STKNEW.stp_painac;
                acpPaisac=STKNEW.stp_paisac;               
                matCodi=STKNEW.stp_matad;
                sdeCodi=STKNEW.stp_saldes;
                acpEngpai=STKNEW.stp_engpai;		
               end if;
		kilos=STKNEW.stp_kilact;
		unid=STKNEW.stp_unact;
               if  TG_OP =  'UPDATE' then
                if OLD.mvt_tipo='E' then                 
                   kilos=STKNEW.stp_kilact-OLD.mvt_canti;
                   unid=STKNEW.stp_unact-OLD.mvt_unid;                  
                else  
                   kilos=STKNEW.stp_kilact+OLD.mvt_canti;
                   unid=STKNEW.stp_unact+OLD.mvt_unid;
                end if;
	      end if;
-- Actualizacion de Registro Stock.		
                if NEW.mvt_tipo='E' then                 
                   kilos=kilos+NEW.mvt_canti;
                   unid=unid+NEW.mvt_unid;                  
                else  
                   kilos=kilos-NEW.mvt_canti;
                   unid=unid-NEW.mvt_unid;
                end if;
 RAISE  NOTICE 'update  % cantidad % kilos %',STKNEW.stp_kilact,NEW.mvt_canti,kilos	;
                UPDATE anjelica.stockpart set stp_kilact= kilos,
                        stp_unact=unid,
			prv_codi = mvtCliprv,
			stp_feccad=mvtFeccad,
                        stp_fecpro=acpFecpro,
                        stp_fecsac=acpFecsac,
                        stp_nucrot=acpNucrot,
                        stp_painac=acpPainac,
                        stp_paisac=acpPaisac,
                        stp_matad=matCodi,
                        stp_saldes=sdeCodi,
                        stp_engpai=acpEngpai,
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
        if TG_OP = 'DELETE' then
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
