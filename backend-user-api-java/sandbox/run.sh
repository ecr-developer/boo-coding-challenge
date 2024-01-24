# Create as docker networks
docker network create adm_boo_services

# Create as folders with permission
mkdir -m 777 .docker

docker compose -f services/docker-compose.yml up -d

echo "Inicializando os containers..."
sleep 20