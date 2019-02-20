declare
    PI_S_SOFT_USER varchar2(30);
begin
    PI_S_SOFT_USER := '&&SOFT_USER';
    for C_CRIA_SINONIMO in (select OBJECT_NAME
                            from   All_Objects
                            where  UPPER(OWNER) = UPPER(PI_S_SOFT_USER)
                            and    OBJECT_NAME not in (select SYNONYM_NAME 
                                                       from   USER_SYNONYMS 
                                                       where  UPPER(TABLE_OWNER) = UPPER(PI_S_SOFT_USER))
							and    OBJECT_TYPE in ('FUNCTION','PACKAGE' , 'PROCEDURE', 'SEQUENCE' , 'TABLE' , 'VIEW')
							AND object_name <> ('PRC_COMPILA_INVALIDO')
							)loop
      begin
      --
         execute immediate 'Create synonym ' || C_CRIA_SINONIMO.OBJECT_NAME ||' for '
                           || PI_S_SOFT_USER || '.'|| C_CRIA_SINONIMO.OBJECT_NAME;
      exception when others
      then 
           null;
       end;    
  end loop; 
end;  
/