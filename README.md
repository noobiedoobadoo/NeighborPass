# NeighborPass

Download the required files
- [docker-compose.yml](https://raw.githubusercontent.com/noobiedoobadoo/NeighborPass/main/run-on-docker/docker-compose.yml)
- [init-db.sql](https://raw.githubusercontent.com/noobiedoobadoo/NeighborPass/main/run-on-docker/init-db.sql)

Place them in a folder and run:
```bash
docker-compose -f docker-compose.yml up
```

Access the app at **localhost:8085** (or your configured port).

Stop the application with:
```bash
docker compose down -v
```
