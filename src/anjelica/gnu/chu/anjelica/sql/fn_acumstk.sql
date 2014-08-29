-- Function: anjelica.fn_acumstk()

-- DROP FUNCTION anjelica.fn_acumstk();

CREATE OR REPLACE FUNCTION anjelica.fn_acumstk()
  RETURNS trigger AS
$BODY$   
  DECLARE   
  STKNEW RECORD;
  STKOLD RECORD;
  kilos float;
  unid int;
  ajuRegacu int;
  swUpd int;
  BEGIN
	    swUpd=0;
        kilos=0;
        unid=0;      
        select aju_regacu into ajuRegacu from anjelica.ajustedb;
	if not found then
		RAISE EXCEPTION 'NO encontrado Ajustes DB';
	end if;
	if ajuRegacu = 0 then
		if TG_OP = 'UPDATE' or TG_OP  ='INSERT' then
		  RETURN NEW;
		else
		  return OLD;
		end if;        	
	end if;
        if TG_OP =  'INSERT' or TG_OP =  'UPDATE' then 
	    --RAISE NOTICE  'En Act. Stk Serie % ',NEW.pro_serie ;
	    if NEW.pro_serie = 'S' then
			return NEW;
	    end if;
            select * INTO STKNEW FROM  anjelica.actstkpart where 
		        pro_codi = NEW.pro_codi and                
                alm_codi = NEW.alm_codi;                
            if STKNEW is  null then
                kilos=NEW.stp_kilact;
                unid=NEW.stp_unact;               
                insert into anjelica.actstkpart (                  
                    pro_codi,alm_codi,
                    stp_unact,
                    stp_feccre,
		    stp_kilact    ) 
                 values
                 (    NEW.pro_codi,NEW.alm_codi,
		      unid,
		      current_date,
                      kilos);
		-- RAISE NOTICE  'Insert. Stk con serie S articulo Nuevo % Kilos: % ',NEW.pro_codi,kilos;		         
            else
                kilos=STKNEW.stp_kilact+NEW.stp_kilact;
                unid=STKNEW.stp_unact+NEW.stp_unact;
                if TG_OP =  'UPDATE' then
			if NEW.pro_codi = OLD.pro_codi and NEW.alm_codi=OLD.alm_codi then
				kilos=kilos-OLD.stp_kilact;
				unid=unid-OLD.stp_unact;
				swUpd=1;
			end if;
                end if;
                UPDATE anjelica.actstkpart set stp_kilact= kilos,
                        stp_unact=unid,
                        stp_fefici = current_date 
                WHERE pro_codi = NEW.pro_codi and
                         alm_codi = NEW.alm_codi;
  	        select sum(stp_unact),sum(stp_kilact) into unid,kilos  from anjelica.actstkpart
 			where pro_codi = NEW.pro_codi;
	        UPDATE anjelica.v_articulo set pro_stkuni=unid,pro_stock=kilos
			where pro_codi=NEW.pro_codi;   
                         
                 -- RAISE NOTICE  'Act. Stk con serie S articulo Nuevo % Kilos: % upd % ',NEW.pro_codi,kilos,swUpd;		         
            end if;
        end if;
        if (TG_OP = 'UPDATE' or TG_OP = 'DELETE') and swUpd = 0 then
	    --RAISE NOTICE  'En Act. Stk olD Serie % ',OLD.pro_serie ;
	   if OLD.pro_serie = 'S' then
		return OLD;
	    end if;
            select * INTO STKOLD FROM  anjelica.actstkpart where 
		pro_codi = OLD.pro_codi and             
                alm_codi = OLD.alm_codi;     
	    if STKOLD is null then
		RAISE EXCEPTION 'No encontrado Apunte stock anterior Almacen:% Articulo:% ',OLD.alm_codi,
			OLD.pro_codi;
		return null;
	    end if;             
            kilos=STKOLD.stp_kilact-OLD.stp_kilact;
            unid=STKOLD.stp_unact-OLD.stp_unact;
            UPDATE anjelica.actstkpart set stp_kilact= kilos,
                   stp_unact=unid,
                   stp_fefici = current_date 
            WHERE  pro_codi = OLD.pro_codi  and
                   alm_codi = OLD.alm_codi;
	    -- RAISE NOTICE  'Act. Stk con serie S articulo viejo % Kilos: % ',OLD.pro_codi,kilos;	
	    select sum(stp_unact),sum(stp_kilact) into unid,kilos  from anjelica.actstkpart
			where pro_codi = OLD.pro_codi ;
	    UPDATE anjelica.v_articulo set pro_stkuni=unid,pro_stock=kilos
			where pro_codi=OLD.pro_codi;   
        end if;
	
	if TG_OP = 'UPDATE' or TG_OP  ='INSERT' then
              RETURN NEW;
         else
	      return OLD;
	 end if;        	
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION anjelica.fn_acumstk()
  OWNER TO anjelica;
