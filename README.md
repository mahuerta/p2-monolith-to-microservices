# p2-monolith-to-microservices

<h1 align="center">Practice 2. Monolith to Microservices 👨🏻‍💻 </h1>

<p align="center">
  <a href="/docs" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="#" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
</p>

Proyecto para migrar una aplicación monolítica a microservicios.

## Authors

👤 **JuanCBM**: Juan Carlos Blázquez Muñoz

* Github: [@JuanCBM](https://github.com/JuanCBM)

👤 **mahuerta**: Miguel Ángel Huerta Rodríguez

* Github: [@mahuerta](https://github.com/mahuerta)

# Sobre la ejecución de la aplicación

Generación de los jars:
> node install.js

Publicación de las imágenes (Están los Dockerfiles):
> sh publish-docker-images.sh

Configuración de minikube con ingress con host:
> minikube start --network-plugin=cni --memory=8192 --driver=virtualbox

> export MINIKUBE_IP=$(minikube ip)

> echo $MINIKUBE_IP www.library.com | sudo tee --append /etc/hosts >/dev/null

> cat /etc/hosts

> minikube addons enable ingress


NOTA:
Para probar en local (tarda un montón en arrancar las BBDD):
> docker run -p 3306:3306 --name library-monolith-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest

> docker run -p 3307:3306 --name userms-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest

## Run mysql
Paso común para lanzar la aplicación con monolito o con monolito y microservicio.

```sh
kubectl apply -f k8s/mysql-db.yaml
```

## Run monolith only
Hay que asegurarse que la variable `USE_USER-MS` esta a `false` en el archivo monolith.yaml
```sh
kubectl apply -f k8s/monolith.yaml
kubectl apply -f k8s/ingresses/ingress-monolith.yaml
```

### Obtenemos la lista de usuarios
Adjuntamos un proyecto postman en la raíz del proyecto para poder realizar las pruebas de las peticiones.

| verb | url                                 |
|------|-------------------------------------|
| GET | http://www.library.com/api/v1/users/ |

Respuesta:
```
[
    {
        "id": 1,
        "nick": "user1",
        "email": "user1@email.es"
    },
    {
        "id": 2,
        "nick": "user2",
        "email": "user2@email.es"
    }
]
```

## Run monolith + user service
Asegurarse que la variable `USE_USER-MS` esta a true en el archivo monolith.yaml

Debemos eliminar el otro ingress si lo tenemos aplicado:
```sh
 kubectl delete -f k8s/ingresses/ingress-monolith.yaml
```

Aplicamos los cambios en el monolito, levantamos el microservicio y aplicamos el nuevo ingress:
```sh
kubectl apply -f k8s/userms.yaml
kubectl apply -f k8s/monolith.yaml
kubectl apply -f k8s/ingresses/ingress-with-microservice.yaml
```

### Obtenemos la lista de usuarios

| verb | url                                 |
|------|-------------------------------------|
| GET | http://www.library.com/api/v1/users/ |

Respuesta:
```
[
    {
        "id": 1,
        "nick": "user1",
        "email": "user1@email-from-ms.es"
    },
    {
        "id": 2,
        "nick": "user2",
        "email": "user2@email-from-ms.es"
    }
]
```

Hemos dado de alta dos usuarios con diferente correo.

NOTA: Acceso a las peticiones:
http://www.library.com/

### Cambios realizados:
- Hace falta que el microservicio de usuarios haga una petición al monolito para obtener los comentarios de un usuario (validación al borrar).

- Los comentarios de un usuario se encuentran en el UserController, no se extraen al microservicio.

- Hay una variable de entorno en el monolito que te permite ver si está configurado o no el microservicio (si se va a usar o no).

- En función de si lo está o no, las peticiones que necesiten alguna información de usuarios (algo más que su id) deberán modificarse para obtenerse del microservicio esa información.

- Hay una variable de entorno en el microservicio que te da la URL de conexión con el monolito a través de una petición REST.

- Cambiado a no usar una FK en Commentarios y Usuarios.
En vez de tener en Comment:

```
@ManyToOne
private User user;
```

Ahora tenemos:
```
@Column(name = "user_id")
private Long userId;
```

- Hay que cambiar el código para que funcione con eso y permita devolver la información del usuario (no vale con que devuelva siempre el id, hay que adaptarse a la solución ya existente)

- El router del (ingress) hay que configurarlo con alguna regla, ya no vale que las peticiones /users vayan al microservicio porque /users/{userId}/comments estará en el monolito.