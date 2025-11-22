Monorepo de tres servicios Spring Boot (Clientes, Productos, Proveedores) con base de datos PostgreSQL y Nginx como proxy. Cada servicio expone un CRUD y se ejecuta en Docker.


Link video:

https://drive.google.com/file/d/1gMRUmyAm9_PnnzaX5OJ47L7aXamtuBHC/view?usp=sharing



Estructura
- app01 (Clientes)
- app02 (Productos)
- app03 (Proveedores)
- db/init (esquema SQL e inserción de 20 registros por tabla)
- nginx-loadbalancer (docker-compose del proxy y archivos de configuración en conf.d)

Despliegue
1. Crear red de Docker
   - `docker network create sd-net`
2. Base de datos (crea la base companydb, tablas y 20 filas por tabla)
   - `docker run -d --name db --network sd-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=companydb -v "$PWD/db/init:/docker-entrypoint-initdb.d" postgres:16-alpine`
3. Servicio app01 (Clientes)
   - `cd app01 && docker build -t app01 .`
   - `docker run -d --network sd-net -p 3001:8080 --name app01 app01`
4. Servicio app02 (Productos)
   - `cd ../app02 && docker build -t app02 .`
   - `docker run -d --network sd-net -p 3002:8080 --name app02 app02`
5. Servicio app03 (Proveedores)
   - `cd ../app03 && docker build -t app03 .`
   - `docker run -d --network sd-net -p 3003:8080 --name app03 app03`
6. Nginx como proxy
   - `cd ../nginx-loadbalancer && docker compose up -d`

Acceso directo (sin Nginx)
- Clientes: `http://127.0.0.1:3001/clientes`
- Productos: `http://127.0.0.1:3002/productos`
- Proveedores: `http://127.0.0.1:3003/proveedores`

Acceso vía Nginx 
- `http://127.0.0.1:9000/clientes`
- `http://127.0.0.1:9000/productos`
- `http://127.0.0.1:9000/proveedores`

Operaciones CRUD (curl)
- Clientes (app01, puerto 3001)
  - Listar: `curl -s http://127.0.0.1:3001/clientes`
  - Obtener por id: `curl -s http://127.0.0.1:3001/clientes/1`
  - Crear: `curl -i -X POST http://127.0.0.1:3001/clientes -H "Content-Type: application/json" -d '{"nombre":"Nuevo","email":"nuevo@ex.com","telefono":"555-9999"}'`
  - Actualizar: `curl -i -X PUT http://127.0.0.1:3001/clientes/1 -H "Content-Type: application/json" -d '{"nombre":"Editado","email":"editado@ex.com","telefono":"555-0000"}'`
  - Eliminar: `curl -i -X DELETE http://127.0.0.1:3001/clientes/1`
- Productos (app02, puerto 3002)
  - Listar: `curl -s http://127.0.0.1:3002/productos`
  - Obtener por id: `curl -s http://127.0.0.1:3002/productos/1`
  - Crear: `curl -i -X POST http://127.0.0.1:3002/productos -H "Content-Type: application/json" -d '{"nombre":"Teclado","precio":49.99,"stock":50}'`
  - Actualizar: `curl -i -X PUT http://127.0.0.1:3002/productos/1 -H "Content-Type: application/json" -d '{"nombre":"Teclado Pro","precio":59.99,"stock":40}'`
  - Eliminar: `curl -i -X DELETE http://127.0.0.1:3002/productos/1`
- Proveedores (app03, puerto 3003)
  - Listar: `curl -s http://127.0.0.1:3003/proveedores`
  - Obtener por id: `curl -s http://127.0.0.1:3003/proveedores/1`
  - Crear: `curl -i -X POST http://127.0.0.1:3003/proveedores -H "Content-Type: application/json" -d '{"nombre":"Proveedor X","email":"px@ex.com","telefono":"555-1111","direccion":"Av. 123"}'`
  - Actualizar: `curl -i -X PUT http://127.0.0.1:3003/proveedores/1 -H "Content-Type: application/json" -d '{"nombre":"Proveedor X2","email":"px2@ex.com","telefono":"555-2222","direccion":"Av. 456"}'`
  - Eliminar: `curl -i -X DELETE http://127.0.0.1:3003/proveedores/1`

Verificación de base de datos
- Listar tablas: `docker exec -it db psql -U postgres -d companydb -c "\\dt"`
- Contar filas: `docker exec -it db psql -U postgres -d companydb -c "SELECT count(*) FROM clientes;"`

Limpieza
- `docker rm -f app01 app02 app03 nginx-loadbalancer-nginx-1 db`
- `docker network rm sd-net`
- `docker system prune -a`



