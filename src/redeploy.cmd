@echo off

set PROJECT_NAME=%1

kubectl rollout restart deployment %PROJECT_NAME%
