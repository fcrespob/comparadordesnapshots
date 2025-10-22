#!/bin/sh

# Cargamos el fichero de propiedades que nos dice el entorno
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/entorno.properties

# Cargamos properties
. /u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2/config/solvencia2_$ENTORNO.properties
PROJECT_HOME=/u01/oracle/Oracle/Middleware/Oracle_Home/coherence/user_projects/solvencia2
HOSTNAME=$(hostname)
IP_LOCALHOST=$(ifconfig eth1 | grep "inet addr" |  awk -F ":" '{print $2'} | awk '{print $1'} )
if [ -z "$COHERENCE_JMX_HOST" ]; then
  IP_SERVICIO=$(/sbin/ifconfig eth0 | grep "inet addr" |  awk -F ":" '{print $2'} | awk '{print $1'} )
else
  IP_SERVICIO=$COHERENCE_JMX_HOST
fi

# Este script arranca el mbean server

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
MEMORY=384m

if [ ! -f ${COHERENCE_HOME}/bin/arranque-mbean-server-solvencia2.sh ]; then
  echo "arranque-mbean-server-solvencia2.sh: must be run from the Coherence installation directory." >> $LOG_PATH/$(hostname)-arranque-coherence-server-$(date +"%Y-%m-%d").log 2>&1 
  exit
fi

if [ -f $JAVA_HOME/bin/java ]; then
  JAVAEXEC=$JAVA_HOME/bin/java
else
  JAVAEXEC=java
fi

JMXPROPERTIES="-Dcom.sun.management.jmxremote -Dtangosol.coherence.management=all -Dtangosol.coherence.management.remote=true -Dtangosol.coherence.management.remote.host=$IP_SERVICIO -Dtangosol.coherence.management.remote.registryport=9000 -Dtangosol.coherence.management.remote.connectionport=3000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

JMXPROPERTIES="$JMXPROPERTIES -Djava.rmi.server.hostname=localhost"

LOCAL_PORT="8090"

COH_CONF="-Dtangosol.coherence.override=$COHERENCE_OVERRIDE_FILE -Dtangosol.coherence.cacheconfig=$COHERENCE_CONFIG_FILE -Dtangosol.pof.config=$POF_CONFIG_FILE -Dtangosol.coherence.distributed.localstorage=false -Dtangosol.coherence.mode=prod -Dsolvencia.coherence.distributed.associated.partition.strategy=$PARTITIONING_STRATEGY"

COH_MEMBER_CONF="-Dtangosol.coherence.localhost=$IP_LOCALHOST -Dtangosol.coherence.localport=$LOCAL_PORT -Dtangosol.coherence.site=$EC2_REGION -Dtangosol.coherence.rack=$HOSTNAME -Dtangosol.coherence.machine=$HOSTNAME -Dtangosol.coherence.member=mbean-server -Dtangosol.coherence.cluster=$COHERENCE_CLUSTER_NAME"

AWS_CONF="-Dtangosol.coherence.ec2addressprovider.propertyfile=$AWS_PROPERTIES_FILE -Dtangosol.coherence.ec2addressprovider.port=$INIT_PORT -Dtangosol.coherence.ec2addressprovider.region=$AWS_REGION -Dtangosol.coherence.ec2tagaddressprovider.tagname=$AWS_INSTANCE_ROLE_TAG -Dtangosol.coherence.ec2tagaddressprovider.tagvalue=$AWS_INSTANCE_ROLE_VALUE -Dtangosol.coherence.ec2tagaddressprovider.interface.tagname=$AWS_NIC_TAG -Dtangosol.coherence.ec2tagaddressprovider.interface.tagvalue=$AWS_NIC_VALUE"

SOLVENCIA_CONF="-Dsolvencia.fecha.cierre=$FEC_CIERRE -Dsolvencia.ruta.base=$RUTA_BASE -Dsolvencia.cache.config.files= -Dsolvencia.coherence.distributed.associated.thread.count=4"

JAVA_OPTS="-Xms$MEMORY -Xmx$MEMORY -Dentorno.ejecucion=$ENTORNO $PROXY_CONFIG $JMXPROPERTIES $COH_CONF $COH_MEMBER_CONF $AWS_CONF $SOLVENCIA_CONF -Dlogs.path=$LOG_PATH "

$JAVAEXEC -server -showversion $JAVA_OPTS -cp "$PROJECT_HOME/config:$PROJECT_HOME/lib/*:$PROJECT_HOME/optional/*:$COHERENCE_HOME/lib/coherence.jar" es.mapfre.solvencia.coherence.jmx.SolvenciaMBeanConnector -rmi $1
