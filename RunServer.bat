title Mail server

@echo off
cls

:: Run
echo Starting server... 

cd MailServer 
mvn clean javafx:run

exit