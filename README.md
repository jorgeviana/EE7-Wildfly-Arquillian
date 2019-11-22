# Boilerplate

`mvn archetype:generate`

23: remote -> org.wildfly.archetype:wildfly-javaee7-webapp-ear-blank-archetype (An archetype that generates a starter Java EE 7 webapp project for JBoss Wildfly. The project is an EAR, with an EJB-JAR and WAR)

# Build

`mvn clean package`

# Docker

```
sudo docker run -p 8080:8080 -p 9990:9990 -it jboss/wildfly /bin/bash

$ /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
$ add-user.sh ...
                                               
sudo docker cp sample-ear/target/sample-ear.ear CONTAINER_ID:/opt/jboss/wildfly/standalone/deployments

```
or, but do not forget to `-b 0.0.0.0`


```
sudo docker run -p 8080:8080 -p 9990:9990 -it jboss/wildfly \
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

sudo docker cp sample-ear/target/sample-ear.ear CONTAINER_ID:/opt/jboss/wildfly/standalone/deployments
```

Then

```
curl localhost:8080/sample-web/rest/v1/heartbeat
curl localhost:8080/sample-web/rest/v1/heartbeat/hello
```


#### Docker reference
https://hub.docker.com/r/jboss/wildfly/
