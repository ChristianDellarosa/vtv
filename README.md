## VTV PROJECT - Vehicle technical verification system


## Architecture

![Architecture Diagram](/documentation/architecture/architecture-diagram.png)

## Stack
- Java 17
- Spring Boot 3
- MongoDB
- RabbitMQ 3-12


## API Documentation

Se implemento el standard de Open API en cada uno de los microservicios por lo cual al ejecutar los microservicios en sus determinados puertos podrian visualizar la siguiente UI

* **Appointment**: http://localhost:8081/appointment-docs
* **Inspection**: http://localhost:8082/inspection-docs
* **Auth**: http://localhost:8083/auth-docs


## Setup

### Requisitos
 * Instancia de MongoDB latest
 * Instancia de RabbitMQ 3-12
 * Configurar Queue en RabbitMQ


### Instancia de MongoDB
Utilizando Docker podemos ejecutar el siguiente comando para levantar un contenedor de MongoDB:
 - user: user
 - password: password
 - port: 27017

```Docker
docker run --env=MONGO_INITDB_ROOT_USERNAME=user --env=MONGO_INITDB_ROOT_PASSWORD=password -p 27017:27017 -d mongo:latest
```
 

### Instancia de RabbitMQ
Utilizando docker podemos ejecutar el siguiente comando para levantar un contenedor de RabbitMQ en el cual ademas de el broker de mensajeria levantara una UI para administrar el mismo.

 - user: guest
 - password: guest
 - port: 5672
 - managment-url: http://localhost:15672/

```Docker
docker run -p 15672:15672 -p 5672:5672 -d rabbitmq:3.12-management
```

#### Para agregar una queue en RabbitMQ, iniciamos session en http://localhost:15672 y nos dirigimos a la siguiente seccion
![How-to-add-queue](/documentation/util/how-to-add-queue-on-rabbitmq.png)

## Configuracion de microservicios
En nuestro sistema tenemos tres microservicios los cuales son necesarios para el funcionamiento del sistema estos son:

* Auth - No requiere configuraciones adicionales
* Appointment
* Inspection

### Inspection
Se necesitan configurar las siguientes variables de entorno dentro del archivo application.yml

* Datasource MONGODB
* Datasource RabbitMQ
* Base URL en donde este levantado nuestro microservicio AUTH
* Nombre de la QUEUE que generamos

```YAML
data:
    mongodb:
      uri: 'mongodb://USER:PASSWORD@HOST:PORT/DATABASE-NAME?authSource=admin'

spring:
  rabbitmq:
    host: HOST
    port: PORT
    username: USERNAME
    password: PASSWORD

queues:
  appointment: 'QUEUE-NAME'

client:
  auth:
    base-url: 'BASE-URL-AUTH-MICROSERVICE'
```

### Appointment
Se necesitan configurar las siguientes variables de entorno dentro del archivo application.yml

* Datasource MONGODB
* Datasource RabbitMQ
* Base URL en donde este levantado nuestro microservicio AUTH
* Nombre de la QUEUE que generamos

```YAML
data:
    mongodb:
      uri: 'mongodb://USER:PASSWORD@HOST:PORT/DATABASE-NAME?authSource=admin'

spring:
  rabbitmq:
    host: HOST
    port: PORT
    username: USERNAME
    password: PASSWORD

producer:
  inspection:
    queue-name: 'QUEUE-NAME'

```


#### Configuraciones de Validaciones de Negocio Adicionales
* months-enable: Meses en los cuales el sistema esta habilitado para entregar turnos
* days-enable: Dias de la semana en los cuales el sistema esta habilitado para entregar turnos

* hours-enable: Horas del dia en los cuales el sistema esta habilitado para entregar turnos

* holiday-days: Dias en los cuales, el sistema no puede entregar turnos como por ejemplo dias no laborables

* per-hour: Cantidad de turnos por hora que el sistema puede agendar

**Ejemplo:**

```YAML
schedules:
  months-enable:
    - FEBRUARY
    - MARCH
  days-enable:
    - MONDAY
    - TUESDAY
    - WEDNESDAY
    - THURSDAY
  hours-enable:
    - 9
    - 14
    - 15
    - 16
    - 17
  per-hour: 3
  holiday-days:
    - '2023-10-21'
```

Con motivo de testear los casos de uso planteados, se generaron cuatro configuraciones como variables de entorno, las cuales plantean los 4 escenarios a testear

- Checkeo aprobado con 80 puntos o mas
- Checkeo aprobado pero observado (entre 40 y 80 puntos)
- Checkeo rechazado (menos de 40 puntos)
- Checkeo rechazado por que un paso de checkeo tiene menos de 5 puntos

**Solamente se podra activar un caso a la vez**

```YAML
check-test:
  is-approved-check-case: false
  is-observed-check-case: true
  is-rejected-check-case: false
  is-reject-by-less-than-five-points-check-case: false
```
### Run

Una vez configuradas las variables anteriormente mencionadas, y teniendo las instancias de MongoDB y RabbitMQ en funcionamiento, por ultimo restaria levantar los tres microservicios de nuestro sistema.

#### Run Appointment
Posicionados sobre el root del proyecto ejecutamos

```CMD
cd appointment-service
```

```CMD
./gradlew bootRun
```



#### Run Auth
Posicionados sobre el root del proyecto ejecutamos

```CMD
cd auth-service
```

```CMD
./gradlew bootRun
```

#### Run Inspection
Posicionados sobre el root del proyecto ejecutamos

```CMD
cd inspection-service
```
```CMD
./gradlew bootRun
```

### Run with DockerCompose
Utilizando docker-compose podriamos facilmente levantar todos los microservicios y los servicios de MongoDB y RabbitMQ de la siguiente forma

* Primero deberemos buildear **TODOS** los microservicios

#### Build Auth

```CMD
cd auth-service
```

```CMD
./gradlew build
```

#### Build Appointment
```CMD
cd appointment-service
```

```CMD
./gradlew build
```

#### Build Inspection

```CMD
cd inspection-service
```

```CMD
./gradlew build
```

Luego de buildear nuestros microservicios (Momentaneamente es un proceso manual, que a futuro se podria automatizar como un stage del docker file para levantar todo el ambiente de manera mas agil se esta trabajando en esto)

* Nos paramos en el root del proyecto y ejecutamos

```CMD
docker-compose up -d
```
Esto nos ejecutaria todos los servicios en conjunto con un contenedor de MongoDB y RabbitMQ

* Por ultimo deberemos ingresar manualmente (se esta trabajando para automatizarlo) en la web de administracion de rabbitMQ y generar la cola de mensajeria con nombre `cola1`
Luego Stopear el docker-compose y volver a iniciarlo y la aplicacion funcionará correctamente