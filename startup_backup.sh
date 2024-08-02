set -e
database_name="thevibgyor"
database="mongodb"
database_username="admin"
database_password="adminpassword"
auth_db="admin"
collections_to_be_backed_up=("users" "user_roles" "sub_menu" "menu")
cd docker
container_id=$(docker ps -qf name="mongodb")
if [ -z "$container_id" ]; then
  echo "No container found with the name 'mongodb'. Exiting."
else
  for collection_name in "${collections_to_be_backed_up[@]}"; do
    docker exec "$container_id" mongodump --db "$database_name" --collection "$collection_name" --out /data/dump  --username "$database_username" --password "$database_password" --authenticationDatabase "$auth_db"
    echo "Data backed up successfully for collection "$collection_name
  done
fi
podman-compose down -v
podman-compose up -d --force-recreate
container_id=$(docker ps -qf name="mongodb")
if [ -z "$container_id" ]; then
  echo "No container found with the name 'mongodb'. Exiting."
else
  for collection_name in "${collections_to_be_backed_up[@]}"; do
    if [ -f "./backup/"$database_name"/"$collection_name".bson" ]; then
      docker exec "$container_id" mongorestore --db "$database_name" --collection "$collection_name" /data/dump/"$database_name"/"$collection_name".bson  --username "$database_username" --password "$database_password" --authenticationDatabase "$auth_db"
      echo "Data restored successfully for collection "$collection_name
    fi
  done
fi
cd ..