@echo off
set COHERENCE_HOME=C:\Oracle\Middleware_12c\coherence
set SOURCE_BASE=F:\MCVPSIT\trunk\modelo

REM Entidades Modeo
call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesBel-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesBel-pof-config.xml -packages es.mapfre.solvencia.dominio.conversionesBel -startTypeId 3000 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesRossp-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesRossp-pof-config.xml -packages es.mapfre.solvencia.dominio.conversionesRossp -startTypeId 2000 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-maestro-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-maestro-pof-config.xml -packages es.mapfre.solvencia.dominio.maestro -startTypeId 1000 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-parametrizacionGeneral-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-parametrizacionGeneral-pof-config.xml -packages es.mapfre.solvencia.dominio.parametrizacionGeneral -startTypeId 4000 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-salidaCalculo-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-salidaCalculo-pof-config.xml -packages es.mapfre.solvencia.dominio.salidaCalculo -startTypeId 5000 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-formulacion-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-formulacion-pof-config.xml -packages es.mapfre.solvencia.dominio.formulacion -startTypeId 6000 -root %SOURCE_BASE%\target\classes

REM Claves Entidades
call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesBel-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesBel-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.conversionesBel -startTypeId 3500 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesRossp-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-conversionesRossp-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.conversionesRossp -startTypeId 2500 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-maestro-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-maestro-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.maestro -startTypeId 1500 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-parametrizacionGeneral-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-parametrizacionGeneral-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.parametrizacionGeneral -startTypeId 4500 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-salidaCalculo-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-salidaCalculo-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.salidaCalculo -startTypeId 5500 -root %SOURCE_BASE%\target\classes

call %COHERENCE_HOME%\bin\pof-config-gen.cmd -out %SOURCE_BASE%\src\main\resources\pof\solvencia-formulacion-keys-pof-config.xml -config %SOURCE_BASE%\src\main\resources\pof\solvencia-formulacion-keys-pof-config.xml -packages es.mapfre.solvencia.coherence.keys.formulacion -startTypeId 6500 -root %SOURCE_BASE%\target\classes

