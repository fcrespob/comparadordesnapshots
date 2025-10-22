#!/bin/sh

# Carga del fichero que nos dice el entorno
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties

# Cargamos properties
PROJECT_HOME=/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2
. $PROJECT_HOME/config/solvencia2_$ENTORNO.properties

HOSTNAME=$(hostname)
IP_LOCALHOST=$(/sbin/ifconfig eth1 | grep "inet addr" |  awk -F ":" '{print $2'} | awk '{print $1'} )
# Comprobar si funciona con proxy
#EC2_AVAIL_ZONE=`curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone`
#EC2_REGION="`echo \"$EC2_AVAIL_ZONE\" | sed -e 's:\([0-9][0-9]*\)[a-z]*\$:\\1:'`"
EC2_REGION=us-east-1

# This will start a cache server

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
#MEMORY=16384m
# Subimos a 18GB para optimizar el consumo de memoria
#MEMORY=18432m

# Aprovechamos toda la memoria disponible del sistema
#TOTAL_MEM=$(free -m | awk 'FNR == 3 {print $4+$3}')
#AVAILABLE_MEM=$(($TOTAL_MEM/$((($COHERENCE_NODES+2)))))
#MEMORY=$AVAILABLE_MEM"m"

if [ ! -f ${COHERENCE_HOME}/bin/solvencia2-cache-server.sh ]; then
  echo "solvencia2-cache-server.sh: must be run from the Coherence installation directory." >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1 
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

# Calculo de numero optimo de motores
NUM_CPU=$(nproc)
COHERENCE_NODES_CPU=$(($(($NUM_CPU - $((NUM_CPU / 8)))) / 2))
COHERENCE_NODES=$COHERENCE_NODES_CPU
COHERENCE_NODES=8

# Número de nodos motor
MOTOR=$(($COHERENCE_NODES-$ENTREGABLES_NODES))
# Número de nodos entregables
ENTREGABLES=$ENTREGABLES_NODES

#Asignar la memoria al nodo
TOTAL_MEM=$(free -m | awk 'FNR == 3 {print $4+$3}')
AVAILABLE_MEM=$(($((2*$TOTAL_MEM/3))/$(($COHERENCE_NODES+$ENTREGABLES*$(($ENTREGABLES_MEM-1))))))

if [[ $START_ID -lt $MOTOR ]]; then
	MEMORY=$AVAILABLE_MEM"m"
	MEMBER_NAME=motor-$START_ID
else
	echo "ENTREGABLES"
	MEMORY=$(($ENTREGABLES_MEM*$AVAILABLE_MEM))"m"
	echo "MEMORIA:$MEMORY"
    MEMBER_NAME=entregables-$(($START_ID-$MOTOR))
fi

echo "Iniciando nodo: Member:$MEMBER_NAME con $MEMORY" >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1 

LOCAL_PORT=$((INIT_PORT + $START_ID))

COH_CONF="-Dtangosol.coherence.override=$COHERENCE_OVERRIDE_FILE -Dtangosol.coherence.cacheconfig=$COHERENCE_CONFIG_FILE -Dtangosol.pof.config=$POF_CONFIG_FILE -Dtangosol.coherence.mode=prod -Dsolvencia.coherence.distributed.associated.partition.strategy=$PARTITIONING_STRATEGY"

COH_MEMBER_CONF="-Dtangosol.coherence.localhost=$IP_LOCALHOST -Dtangosol.coherence.localport=$LOCAL_PORT -Dtangosol.coherence.site=$EC2_REGION -Dtangosol.coherence.rack=$HOSTNAME -Dtangosol.coherence.machine=$HOSTNAME -Dtangosol.coherence.member=$MEMBER_NAME -Dtangosol.coherence.cluster=$COHERENCE_CLUSTER_NAME"

AWS_CONF="-Dtangosol.coherence.ec2addressprovider.propertyfile=$AWS_PROPERTIES_FILE -Dtangosol.coherence.ec2addressprovider.port=$INIT_PORT -Dtangosol.coherence.ec2addressprovider.region=$AWS_REGION -Dtangosol.coherence.ec2tagaddressprovider.tagname=$AWS_INSTANCE_ROLE_TAG -Dtangosol.coherence.ec2tagaddressprovider.tagvalue=$AWS_INSTANCE_ROLE_VALUE -Dtangosol.coherence.ec2tagaddressprovider.interface.tagname=$AWS_NIC_TAG -Dtangosol.coherence.ec2tagaddressprovider.interface.tagvalue=$AWS_NIC_VALUE"

PERFORMANCE_CONF="-XX:+AggressiveOpts -Dtangosol.coherence.distributed.backupcount=0 -Djdk.map.althashing.threshold=512"

SOLVENCIA_CONF="-Dsolvencia.coherence.distributed.associated.thread.count=4"

JAVA_OPTS="-Xms$MEMORY -Xmx$MEMORY -Dentorno.ejecucion=$ENTORNO $PROXY_CONFIG $PERFORMANCE_CONF $JMXPROPERTIES $COH_CONF $COH_MEMBER_CONF $AWS_CONF $SOLVENCIA_CONF -Dlogs.path=$LOG_PATH"

$JAVAEXEC -server -showversion $JAVA_OPTS -cp "$PROJECT_HOME/config:$PROJECT_HOME/lib/*:$PROJECT_HOME/optional/*:$COHERENCE_HOME/lib/coherence.jar" es.mapfre.solvencia.motor.SolvenciaCacheServer $1
