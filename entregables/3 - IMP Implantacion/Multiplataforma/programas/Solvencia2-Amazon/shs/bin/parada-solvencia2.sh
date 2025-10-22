#!/bin/bash

####################################################################################
#                                                                                  #
#                  SCRIPT DE PARADA PARA ORACLE COHERENCE                          #
#                                                                                  #
####################################################################################



for i in `ps -ef | grep oracle | grep -e solvencia -e cache | grep -v grep | grep -v parada | awk '{print $2}'` 
    do
        kill -TERM $i
        if [ $? -ne 0 ]; then 
            kill -9 $i
        fi
    done
