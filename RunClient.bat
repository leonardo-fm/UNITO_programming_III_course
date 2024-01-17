title Mail client

@echo off
cls 

:: Run
echo Starting client... 

cd MailClient
mvn clean javafx:run

exit