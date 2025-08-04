@echo off
docker start postgres-recebedor
docker start postgres-pagador
docker start postgres-central
echo Containers iniciados!
pause
