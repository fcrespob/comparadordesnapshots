#!/bin/bash
########################################################################################################
#                                               MAPFRE                                                 #
########################################################################################################
#--Carga del fichero de propiedades

if [ -e /root/arranque.properties ]; then

source /root/arranque.properties
                      if [ $? -ne 0 ];then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error cargando el fichero arranque.properties"
                       exit 1
                      fi
else
   echo "$FECHA_HOST:ERROR-->No se ha encontrado el fichero arranque.properties"
   exit 1
 fi
#--Validacion de la existencia del directorio de logs
VAR=0
INTENTOS=0
MAX_INTENTOS=100
FECHA_HOST=$(/bin/date +"%d-%m-%y-%T")_$(/bin/hostname)

while [ $VAR != 1 ] && [ $INTENTOS -lt $MAX_INTENTOS ]; do
        if [ $(df -m | grep /logs | wc -l ) -eq 1 ]; then
        VAR=1
        else
        INTENTOS=$(echo $INTENTOS+1 | bc)
        sleep 5s
        fi
done

        if [ $INTENTOS = $MAX_INTENTOS ]; then
                exit 1
        fi


while [ $(cat $LOG_NAME | grep "Ha finalizado el script de actualizacion en el nodo maestro" | wc -l) -ne 1 ]; do
   mount -a
   sleep 10s
  done
  
  if [ $(cat $LOG_NAME | grep ERROR | wc -l) -eq 0 ]; then
    mount -a
      if [ -e /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin ]; then
        if [ -e /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh ]; then
            su - $SCRIPT_USER -c "/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh &"
                sleep 20s
                  if [ $(ps -ef | grep java | grep coherence | grep -v grep | grep -v arranque | wc -l) -gt 0 ]; then
                       echo "$FECHA_HOST:INFO-->Se ha iniciado coherence en " $HOSTNAME >> $LOG_NAME
                       exit 0
                  else
                       echo "$FECHA_HOST:ERROR-->Se ha ha producido un error en el arranque de coherence en " $HOSTNAME >> $LOG_NAME
                       exit 1
                  fi
         else
          echo "$FECHA_HOST:ERROR-->No se ha encontrado el script /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh" >> $LOG_NAME
         fi

       else
         echo "$FECHA_HOST:ERROR-->No se ha encontrado el directorio /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin" >> $LOG_NAME
       fi
	 else
    echo "$FECHA_HOST:ERROR-->Se ha producido un error en el arranque de coherence en el nodo maestro" >> $LOG_NAME
  fi
