cd docker
podman-compose down -v
podman-compose up -d --force-recreate
cd ..