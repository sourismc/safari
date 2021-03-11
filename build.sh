#!/bin/bash

"/mnt/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 211.6305.21/plugins/maven/lib/maven3/bin/mvn" -Dmaven.ext.class.path=/mnt/c/Program\ Files/JetBrains/IntelliJ\ IDEA\ Community\ Edition\ 211.6305.21/plugins/maven/lib/maven-event-listener.jar install && scp target/safari.jar aytos.lan:/srv/mc/nukkit/plugins/safari.jar && echo "Build & Ship OK"
