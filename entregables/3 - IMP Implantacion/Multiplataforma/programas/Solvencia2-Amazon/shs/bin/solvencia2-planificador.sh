#!/bin/sh

#  Cargamos el fichero que nos dice el entorno 
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties

# Cargamos properties
PROJECT_HOME=/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2
. $PROJECT_HOME/config/solvencia2_$ENTORNO.properties
HOSTNAME=$(hostname)
IP_LOCALHOST=$(/sbin/ip a | grep eth1 | grep inet | awk '{print $2}' | awk -F / '{print $1}')

# Este script arranca el planificador

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

# specify the JVM heap size
MEMORY=1536m

if [ ! -f ${COHERENCE_HOME}/bin/solvencia2-planificador.sh ]; then
  echo "solvencia2-planificador.sh: must be run from the Coherence installation directory." >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log  2>&1 
  exit
fi

if [ -f $JAVA_HOME/bin/java ]; then
  JAVAEXEC=$JAVA_HOME/bin/java
else
  JAVAEXEC=java
fi

if [[ $1 == '-jmx' ]]; then
    JMXPROPERTIES="-Dtangosol.coherence.management=all -Dtangosol.coherence.management.remote=true"
    shift
fi

if [[ $1 == '-startid' ]]; then
    shift
    START_ID=$1
    shift
fi

if [[ $1 == '-feccierre' ]]; then
    shift
    FEC_CIERRE=$1
    shift
fi
LOCAL_PORT=$(($INIT_PORT + $START_ID))

COH_CONF="-Dtangosol.coherence.override=$COHERENCE_OVERRIDE_FILE -Dtangosol.coherence.cacheconfig=$COHERENCE_CONFIG_FILE -Dtangosol.pof.config=$POF_CONFIG_FILE -Dtangosol.coherence.distributed.localstorage=false -Dtangosol.coherence.mode=prod -Dsolvencia.coherence.distributed.associated.partition.strategy=$PARTITIONING_STRATEGY"

COH_MEMBER_CONF="-Dtangosol.coherence.localhost=$IP_LOCALHOST -Dtangosol.coherence.localport=$LOCAL_PORT -Dtangosol.coherence.machine=$HOSTNAME -Dtangosol.coherence.member=planificador-$START_ID"

AWS_CONF="-Dtangosol.coherence.ec2addressprovider.propertyfile=$AWS_PROPERTIES_FILE -Dtangosol.coherence.ec2addressprovider.port=$INIT_PORT -Dtangosol.coherence.ec2addressprovider.region=$AWS_REGION -Dtangosol.coherence.ec2tagaddressprovider.tagname=$AWS_INSTANCE_ROLE_TAG -Dtangosol.coherence.ec2tagaddressprovider.tagvalue=$AWS_INSTANCE_ROLE_VALUE -Dtangosol.coherence.ec2tagaddressprovider.interface.tagname=$AWS_NIC_TAG -Dtangosol.coherence.ec2tagaddressprovider.interface.tagvalue=$AWS_NIC_VALUE"

SOLVENCIA_CONF="-Dsolvencia.fecha.cierre=$FEC_CIERRE -Dsolvencia.ruta.base=$RUTA_BASE -Dsolvencia.cache.config.files="

PERFORMANCE_CONF="-XX:+AggressiveOpts -Dtangosol.coherence.distributed.backupcount=0 -Djdk.map.althashing.threshold=512"

MONITOR_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_PATH/$(hostname)-planificador-memorydump-$(date +"%Y-%m-%d").hprof"

JAVA_OPTS="$MONITOR_OPTS -Xms$MEMORY -Xmx$MEMORY -Dentorno.ejecucion=$ENTORNO $PROXY_CONFIG $JMXPROPERTIES $COH_CONF $COH_MEMBER_CONF $AWS_CONF $SOLVENCIA_CONF $PERFORMANCE_CONF -Dlogs.path=$LOG_PATH "

echo "PROCESO=$FEC_CIERRE" > /u01/arranqueNodos/SOLVENCIA.start
echo "DESCRIPCION=" >> /u01/arranqueNodos/SOLVENCIA.start

$JAVAEXEC -server -showversion $JAVA_OPTS -cp "$PROJECT_HOME/config:$PROJECT_HOME/lib/*:$PROJECT_HOME/optional/*:$COHERENCE_HOME/lib/coherence.jar" es.mapfre.solvencia.planificador.AppGridPlanificador $1 >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1
