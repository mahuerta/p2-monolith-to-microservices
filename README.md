# p2-monolith-to-microservices
Practice 2. Monolith to Microservices

Generación de los jars:
> node install.js

Publicación de las imágenes (Están los Dockerfiles):
> sh publish-docker-images.sh


Está preparada la parte de k8s aunque sin probar (Sólo la idea de la estructura de los archivos necesarios)


Para probar en local (tarda un montón en arrancar las BBDD):
> docker run -p 3306:3306 --name library-monolith-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest

> docker run -p 3307:3306 --name userms-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest


TODO + INFO:
- Hace falta que el microservicio de usuarios haga una petición al monolito para obtener los comentarios de un usuario (validación al borrar)
- Los comentarios de un usuario se encuentran en el UserController.

- El método de obtener los comentarios no debe estar en el microservicio, se mantiene en el monolito.

- Problema: El router del (ingress) hay que configurarlo con alguna regla, ya no vale que las peticiones /users vayan al microservicio porque /users/{userId}/comments estará en el monolito.


- Hay una variable de entorno en el monolito que te permite ver si está configurado o no el microservicio (si se va a usar o no).
- En función de si lo está o no, las peticiones que necesiten alguna información de usuarios (algo más que su id) deberán modificarse para obtenerse del microservicio esa información.

- Hay una variable de entorno en el microservicio que te da la URL de conexión con el monolito a través de una petición REST.
