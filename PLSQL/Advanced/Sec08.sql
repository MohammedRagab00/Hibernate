SELECT
    *
FROM
    EMPLOYEES
WHERE
    JOB_ID = 'SA_REP'
    AND DEPARTMENT_ID IS NULL;

CREATE OR REPLACE TRIGGER DELETE_EMP_TRG BEFORE
    DELETE ON EMPLOYEES
BEGIN
    IF NOT (TO_CHAR(SYSDATE(), 'DY') IN ('SAT', 'SUN')
    OR TO_CHAR(SYSDATE(), 'HH24:MI') NOT BETWEEN '08:00'
    AND '18:00') THEN
        RAISE_APPLICATION_ERROR(-20500, 'You may delete from EMPLOYEES table only during business hours.');
    END IF;
END;
/

DELETE FROM EMPLOYEES
WHERE
    JOB_ID = 'SA_REP'
    AND DEPARTMENT_ID IS NULL;

SELECT
    *
FROM
    EMPLOYEES;