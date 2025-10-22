#!/bin/sh


# Cargamos el fichero de propiedades que nos dice el entorno
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties

# Cargamos properties
cd /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/bin/
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/config/solvencia2_$ENTORNO.properties
PROJECT_HOME=/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2

# specify the Coherence installation directory
SCRIPT_PATH="${BASH_SOURCE[0]}";
if([ -h "${SCRIPT_PATH}" ]) then
    while([ -h "${SCRIPT_PATH}" ]) do SCRIPT_PATH=`readlink "${SCRIPT_PATH}"`; done
fi
pushd . > /dev/null
cd `dirname ${SCRIPT_PATH}` > /dev/null
SCRIPT_PATH=`pwd`
COHERENCE_HOME=`dirname $SCRIPT_PATH`;
popd  > /dev/null

if [ ! -f ${COHERENCE_HOME}/bin/arranque-solvencia2.sh ]; then
    echo "arranque-solvencia2.sh: must be run from the Coherence installation directory." >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1 
    exit
fi


# Arrancamos todos los nodos de la instancia
NUM_CPU=$(nproc)
COHERENCE_NODES_CPU=$(($(($NUM_CPU - $((NUM_CPU / 8)))) / 2))
echo $COHERENCE_NODES_CPU
COHERENCE_NODES=$COHERENCE_NODES_CPU
COHERENCE_NODES=8

n=0
while [ $n -lt $COHERENCE_NODES ]; do
    # Use a sleep interval between request rates RECOMENDACION DE AMAZON
    sleep 1
	echo "Arrancando nodo coherence "$n
    nohup ${COHERENCE_HOME}/bin/solvencia2-cache-server.sh -startid $n >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log  2>&1 &
    n=$(( $n + 1 ))
done


# Arrancamos nodo de monitorizacion
nohup ${COHERENCE_HOME}/bin/arranque-mbean-server-solvencia2.sh >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1 &
