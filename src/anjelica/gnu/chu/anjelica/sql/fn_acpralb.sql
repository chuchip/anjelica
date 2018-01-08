-- Function: fn_acpralb()

-- DROP FUNCTION fn_acpralb();

CREATE OR REPLACE FUNCTION fn_acpralb()
  RETURNS trigger AS
$BODY$   
  DECLARE   
  nRows int;
  BEGIN
  -- RAISE NOTICE  'en fn_acpralb %', TG_TABLE_NAME;
        if TG_TABLE_NAME = 'v_albavel' then
           update anjelica.mvtosalm set mvt_prec = NEW.avl_prven where
        mvt_empcod =OLD.emp_codi and
        mvt_ejedoc=OLD.avc_ano and
        mvt_numdoc=OLD.avc_nume and
        mvt_serdoc=OLD.avc_serie and
mvt_tipdoc='V' AND
        mvt_lindoc=OLD.avl_numlin;
        end if; 
        if TG_TABLE_NAME = 'v_albacol' then
           update anjelica.mvtosalm set mvt_prec = NEW.acl_prcom,
                alm_codi = NEW.alm_codi where
                alm_codi = OLD.alm_codi AND
			mvt_empcod =OLD.emp_codi and
        mvt_ejedoc=OLD.acc_ano and
        mvt_numdoc=OLD.acc_nume and
        mvt_serdoc=OLD.acc_serie and
        mvt_lindoc=OLD.acl_nulin
        AND mvt_tipdoc='C';
        end if;
        if TG_TABLE_NAME = 'v_albacoc' then
           update anjelica.mvtosalm set mvt_cliprv = NEW.prv_codi,
mvt_time=NEW.acc_fecrec,
mvt_fecdoc=NEW.acc_fecrec
            where mvt_ejedoc=OLD.acc_ano and
        mvt_numdoc=OLD.acc_nume and
        mvt_serdoc=OLD.acc_serie and
        mvt_tipdoc='C';        
        end if;
 if TG_TABLE_NAME = 'v_albavec' then
           update anjelica.mvtosalm set mvt_cliprv = NEW.cli_codi,
mvt_fecdoc=NEW.avc_fecalb
            where mvt_empcod =OLD.emp_codi and
mvt_ejedoc=OLD.avc_ano and
        mvt_numdoc=OLD.avc_nume and
        mvt_serdoc=OLD.avc_serie and
        mvt_tipdoc='V';        
        end if;
        if TG_TABLE_NAME = 'desporig' then
--   RAISE NOTICE  'en fn_acpralb % % %' , NEW.prv_codi,OLD.eje_nume,OLD.deo_codi ;
           update anjelica.mvtosalm set mvt_cliprv = NEW.prv_codi
             where mvt_ejedoc=OLD.eje_nume and
        mvt_numdoc= OLD.deo_codi and
        (mvt_tipdoc='D' OR MVT_TIPdoc='d');
   GET DIAGNOSTICS nRows = ROW_COUNT;
   if nRows = 0 then
RAISE EXCEPTION 'No encontrado Mvto. de Despiece % % ',OLD.eje_nume,OLD.deo_codi;
return null;
   end if;
        end if;

return NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fn_acpralb()
  OWNER TO anjelica;
