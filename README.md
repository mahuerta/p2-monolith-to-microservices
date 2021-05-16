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

Está preparada la parte de k8s aunque sin probar (Sólo la idea de la estructura de los archivos necesarios)

Para probar en local (tarda un montón en arrancar las BBDD):
> docker run -p 3306:3306 --name library-monolith-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest

> docker run -p 3307:3306 --name userms-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest






Configuración de minikube con ingress con host:
> minikube start --network-plugin=cni --memory=8192 --driver=virtualbox

> export MINIKUBE_IP=$(minikube ip)

> echo $MINIKUBE_IP www.library.com | sudo tee --append /etc/hosts >/dev/null

> cat /etc/hosts

> minikube addons enable ingress

> kubectl apply -f ./k8s

Acceso a las peticiones:
http://www.library.com/



TODO + INFO:
- (HECHO) Hace falta que el microservicio de usuarios haga una petición al monolito para obtener los comentarios de un usuario (validación al borrar)

- (HECHO) Los comentarios de un usuario se encuentran en el UserController.

- (HECHO) El método de obtener los comentarios no debe estar en el microservicio, se mantiene en el monolito.

- (HECHO) Hay una variable de entorno en el monolito que te permite ver si está configurado o no el microservicio (si se va a usar o no).

- (HECHO) En función de si lo está o no, las peticiones que necesiten alguna información de usuarios (algo más que su id) deberán modificarse para obtenerse del microservicio esa información.

- (HECHO) Hay una variable de entorno en el microservicio que te da la URL de conexión con el monolito a través de una petición REST.


TODO:

1. (HECHO) Cambiado a no usar una FK.
Hay añadida en el monolito una nueva columna en la base de datos para representar la relación usuario-comentario.
Hay que cambiarla. En vez de tener en Comment:

    @ManyToOne
    private User user;

    @Column(name = "user_ms_id")
    private Long userMSId;


Deberíamos tener:

    @Column(name = "user_id")
    private Long userId;

Hay que cambiar el código para que funcione con eso y permita devolver la información del usuario (no vale con que devuelva siempre el id, hay que adaptarse a la solución ya existente)



2. (PLANTEADO EL INGRESS) 
Problema: El router del (ingress) hay que configurarlo con alguna regla, ya no vale que las peticiones /users vayan al microservicio porque /users/{userId}/comments estará en el monolito.