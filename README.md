# Anleitung

## Docker Container für Datenbank erstellen:

- Container Name: demo-mssql
- Persistence-Volume Name: demo-mssql-data
- Passwort: webshop_password0

# Persistence Volume erstellen:
```shell script
docker volume create demo-mssql-data
```

## Docker Container erstellen:
```shell script
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=webshop_password0' -p 1433:1433 --name demo-mssql -v demo-mssql-data:/var/opt/mssql -d mcr.microsoft.com/mssql/server:2022-latest
```

## Docker Container läuft?
```shell script
docker ps
docker ps -a
```
## Falls Container nicht richtig läuft 
- stoppen und löschen:
    ```shell script
    docker stop demo-mssql
    docker rm demo-mssql
    docker logs demo-mssql
    ```

## Datenbank im Backend im Container erzeugen:
```shell script
docker exec -it demo-mssql /opt/mssql-tools18/bin/sqlcmd -S tcp:localhost,1433 -U sa -P webshop_password0 -Q "CREATE DATABASE shop;" -C
```

# Backend starten  ([siehe](./shop-backend/README.md))
```shell script
cd shop/shop-backend
./mvnw quarkus:dev
```

# Frontend starten ([siehe](./shop-frontend/README.md))
```shell script
cd shop/shop-frontend
```
- Abhängigkeiten installieren:
```shell script
yarn
```
- starten:
```shell script
yarn dev
```

# Skripte ausführen 
(Demodaten in Datenbank erzeugen, alternativ im Frontend manuell erfassen)
- unter Linux:
```shell script
cd shop/scripts
./create_books.sh
./create_series.sh
```

- unter Windows:
```shell script
.\scripts\create_books.ps1
```

# Frontend in Web-Browser:
http://localhost:3000

# Backend in Browser erreichbar:
- API: http://localhost:8080/api/v1
- Swagger: http://localhost:8080/api/swagger-ui
- DevUI: http://localhost:8080/dev-ui

