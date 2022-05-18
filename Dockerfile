FROM openjdk:8
VOLUME /tmp
ADD ./target/RESTProductos-1.0.jar rest-productos.jar
ENTRYPOINT ["java","-jar","rest-productos.jar"]