@echo off
echo Starting Backend Server on port 8080...
start cmd /k "cd backend && gradlew bootRun"

echo Starting Frontend Server on port 3000...
start cmd /k "cd frontend && npx http-server -p 3000"

echo Servers are starting in separate windows!
echo Backend API will be available at http://localhost:8080/api
echo Frontend Website will be available at http://localhost:3000
