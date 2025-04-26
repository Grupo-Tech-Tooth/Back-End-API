@echo off

echo ==== Mudando o nome da imagem Docker ====
docker tag back marcosfloriano/back:latest || (
  echo Falha ao renomear a imagem.
  exit /b 1
)

echo ==== Realizando push da imagem para o Docker Hub ====
docker push marcosfloriano/back:latest || (
  echo Falha ao realizar o push da imagem.
  exit /b 1
)

echo ==== Processo concluído com sucesso ====
pause