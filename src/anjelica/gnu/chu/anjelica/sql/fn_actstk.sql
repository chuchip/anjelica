CREATE OR REPLACE FUNCTION anjelica."fn_actstk"()
  RETURNS TRIGGER AS $grabar$   
  DECLARE   
  STKNEW RECORD;
  STKOLD RECORD;
  kilos float;
  unid int;
  BEGIN
        kilos=0;
        unid=0;      
		
        if TG_OP =  'INSERT' or TG_OP =  'UPDATE' then 
            select * INTO STKNEW FROM  anjelica.stockpart where 
                pro_codi= NEW.pro_codi and
                eje_nume= NEW.pro_ejelot  and 
                pro_serie = NEW.pro_serlot and
                pro_nupar = NEW.pro_numlot and
                pro_numind  = NEW.pro_indlot and
                alm_codi = NEW.alm_codi;                
            if STKNEW is  null then
                kilos=NEW.mvt_canti;
                unid=NEW.mvt_unid;
                if NEW.mvt_tipo='S' then
                        kilos=kilos*-1;
                        unid= unid * -1;
                end if;
                insert into anjelica.stockpart (eje_nume,
                    emp_codi,pro_serie, stp_tiplot,pro_nupar,                    
                    pro_codi,pro_numind,alm_codi,
                    stp_unini,stp_unact,
                    stp_feccre,
					stp_kilini,stp_kilact,
					prv_codi,stp_feccad) 
                 values
                 (
                        NEW.pro_ejelot, 
						NEW.mvt_empcod,NEW.pro_serlot,'P', NEW.pro_numlot,
                        NEW.pro_codi,NEW.pro_indlot,NEW.alm_codi,
						unid,unid,
						current_timestamp,
                        kilos,kilos,
                        NEW.mvt_cliprv,NEW.mvt_feccad);
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
$grabar$ LANGUAGE 'plpgsql';
