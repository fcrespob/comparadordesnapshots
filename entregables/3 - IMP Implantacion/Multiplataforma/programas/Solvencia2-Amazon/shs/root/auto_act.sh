#!/bin/bash
#
########################################################################################################
#                                               MAPFRE                                                 #
########################################################################################################
#
# Auto-actualizacion de componentes en instancias de Amazon

#--Variables
FECHA_HOST=$(/bin/date +"%d-%m-%y-%T")_$(/bin/hostname)

#--Carga del fichero que nos dice el entorno
if [ -e /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties ]; then
source /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties > /dev/null 2>&1

                      if [ $? -ne 0 ];then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error cargando el fichero que determina el entorno: entorno.properties"
                       exit 1
                      fi
else
   echo "$FECHA_HOST:ERROR-->No se ha encontrado el fichero entorno.properties"
   exit 1
fi
#--Carga del fichero de propiedades de la actualizacion

if [ -e /root/actualizacion_$ENTORNO.properties ]; then

source /root/actualizacion_$ENTORNO.properties > /dev/null 2>&1
                      if [ $? -ne 0 ];then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error cargando el fichero de propiedades de la actualizacion: actualizacion_$ENTORNO.properties"
                       exit 1
                      fi
else
   echo "$FECHA_HOST:ERROR-->No se ha encontrado el fichero actualizacion_$ENTORNO.properties"
   exit 1
fi

#--Validacion de la existencia del directorio de logs
VAR=0
INTENTOS=0
MAX_INTENTOS=100

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

if [ $(df -m | grep /logs | wc -l ) -eq 1 ]; then
 exportfs -o rw,fsid=1,no_root_squash,no_subtree_check *:/logs
                       if [ $? -ne 0 ];then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error exportando el directorio /logs via NFS"
                       exit 1
                      fi
fi

#--Creacion del fichero de log

echo \# $(/bin/date) > $LOG_NAME
echo $(/bin/hostname) >> $LOG_NAME
chown $USER_OWNER:$GROUP_OWNER $LOG_NAME

#--Verificaciones de la comunicacion con el entorno open

VAR=0
        while [ $VAR != 1 ]; do
                STATUS=$(ping -c 5 $DIR_IP_OPEN | grep ttl | wc -l)
                if [ $STATUS -gt 4 ]; then
                        echo "$FECHA_HOST:INFO-->Se ha establecido la comunicacion con el entorno Open" >> $LOG_NAME
                        VAR=1
                        mount -a
                fi
        done

#--Verificacion de contenido directorio de actualizacion

   if [ ! -e $ACT_DIR ]; then
        echo "$FECHA_HOST:ERROR-->No se ha encontrado el directorio $ACT_DIR" >> $LOG_NAME
        exit 1
   else

#--Si no existe el fichero de configuracion de la actualizacion se inicia coherence
     if [ ! -e $ACT_DIR/$CONFIG_FILE_NAME ]; then
              echo "$FECHA_HOST:INFO-->Ha finalizado el script de actualizacion en el nodo maestro:" $HOSTNAME >> $LOG_NAME
                              if [ -e /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh ]; then
                                       su - $SCRIPT_USER -c "/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh" &
                                       sleep 20s
                                        if [ $(ps -ef | grep java | grep coherence | grep -v grep | grep -v arranque | wc -l) -gt 0 ]; then
                                           echo "$FECHA_HOST:INFO-->Se ha iniciado coherence en $HOSTNAME no se han encontrado ficheros para actualizar"  >> $LOG_NAME #El arranque se realiza automaticamente por no existir fichero de actualizacion
                                           exit 0
                                        else
                                           echo "$FECHA_HOST:ERROR-->Se ha ha producido un error en el arranque de coherence en: " $HOSTNAME >> $LOG_NAME
                                           exit 1
                               fi
                              else
                                    echo "$FECHA_HOST:ERROR-->Se ha producido un error en el arranque de coherence en $HOSTNAME: No se ha encontrado el fichero /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh" >> $LOG_NAME
                                   exit 1
                                fi
     fi
  fi

#--Si existe el fichero, se valida su contenido



if [ -e $ACT_DIR/$CONFIG_FILE_NAME ]; then

 cat $ACT_DIR/$CONFIG_FILE_NAME | grep -v "#" | grep -v ^$ |  while read i
  do
                 ORIGEN=$(echo $i | awk '{print $1}')
                      if [ $? -ne 0 ] || [ $ORIGEN = "" ];then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error leyendo el fichero de configuracion, verifique el formato" >> $LOG_NAME
                      fi
                DESTINO=$(echo $i | awk '{print $2}')
                    if [ $? -ne 0 ]|| [ $DESTINO = "" ] ; then
                       echo "$FECHA_HOST:ERROR-->Se ha producido un error leyendo el fichero de configuracion, verifique el formato" >> $LOG_NAME
                     fi
                       if [ ! -e $ORIGEN ]; then
                        echo "$FECHA_HOST:ERROR-->No se encuentra la ruta $ORIGEN ">> $LOG_NAME
                       fi
done
fi

#
#--Inicio de la copia de ficheros

if [ $(cat $LOG_NAME | grep ERROR | grep -v grep | wc -l) -gt 0 ]; then
        exit 1
else

 cat $ACT_DIR/$CONFIG_FILE_NAME | grep -v "#" |grep -v '^$'|  while read i
 do
                  ORIGEN=$(echo $i | awk '{print $1}')
                  DESTINO=$(echo $i | awk '{print $2}')

      if [ ! -e $DESTINO ]; then
        mkdir -p $DESTINO
        chown $USER_OWNER:$GROUP_OWNER $DESTINO
      fi
      if [ $DESTINO = "/" ] || [ $DESTINO = "/etc" ] || [ $DESTINO = "/opt" ] || [ $DESTINO = "lib" ]||  [ $DESTINO = "/boot" ] || [ $DESTINO = "/u01" ] || [ $DESTINO = "/var" ] ||[ $DESTINO = "/home" ]|| [ $DESTINO = "/usr" ] ||[ $DESTINO = "/home" ] ||[ $DESTINO = "/tmp" ] ||[ $DESTINO =  "/proc" ] ||[ $DESTINO = "" ] ||[ $DESTINO = " " ] ||  [ $DESTINO = "/bib64" ] ||  [ $DESTINO = "/net" ] ||  [ $DESTINO = "/srv" ] ||  [ $DESTINO = "/sbin" ] ||  [ $DESTINO = "/sys" ]||  [ $DESTINO = "/selinux" ] ||  [ $DESTINO = "/srv" ]; then
         echo "$FECHA_HOST:ERROR-->Se ha producido un error no se puede borrar $DESTINO" >> $LOG_NAME
         exit 1
       else
         su - $USER_OWNER -c "rm -f $DESTINO/*"
                    if [ $? -ne 0 ]; then
                        echo "$FECHA_HOST:ERROR-->Se ha producido un error borrando el contenido de $DESTINO" >> $LOG_NAME
                        exit 1
                    else
                        echo "$FECHA_HOST:INFO-->Se ha borrado el destino $DESTINO" >> $LOG_NAME
                    fi
        cp -f $ORIGEN/* $DESTINO  > /dev/null 2>&1
        chown $USER_OWNER:$GROUP_OWNER $DESTINO/*
		
        if [ $(ls $DESTINO | grep ".sh" | grep -v grep | wc -l ) -gt 0 ]; then

			chmod 750 $DESTINO/*.sh
         fi

                          if [ $? -ne 0 ]; then
                            echo "$FECHA_HOST:ERROR-->Se ha producido un error copiando de $ORIGEN a $DESTINO" >> $LOG_NAME
                            exit 1
                          else
                             echo "$FECHA_HOST:INFO-->Se han copiado los ficheros en $DESTINO" >> $LOG_NAME
                          fi
                    fi
        done
fi

#--Verificacion del fichero de log de actualizacion  para el arranque de coherence

NEW_NAME=$(echo $CONFIG_FILE_NAME | awk -F '.' '{print $1}')_$(/bin/date +%H%M%S).txt

  if [ $(cat $LOG_NAME | grep ERROR | wc -l) -eq 0 ]; then # Si no se ha producido un error durante la actualizacion se inicia Coherence.

    su - $SCRIPT_USER -c "/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/arranque-solvencia2.sh" &
    sleep 20s
                     if [ $(ps -ef | grep java | grep coherence | grep -v grep | grep -v arranque | wc -l) -gt 0 ]; then
                       echo "$FECHA_HOST:INFO-->Se ha iniciado coherence en " $HOSTNAME "tras la actualizacion" >> $LOG_NAME
                         else
                            echo "$FECHA_HOST:ERROR-->Se ha ha producido un error en el arranque de coherence en " $HOSTNAME >> $LOG_NAME
                         fi
	   echo "#EJECUTADO" >> $ACT_DIR/$CONFIG_FILE_NAME
	   mv $ACT_DIR/$CONFIG_FILE_NAME $ACT_DIR/$NEW_NAME
       echo "$FECHA_HOST:INFO-->Ha finalizado el script de actualizacion en el nodo maestro" $HOSTNAME >> $LOG_NAME
    exit 0
  fi

