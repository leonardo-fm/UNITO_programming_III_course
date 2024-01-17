@echo off
cls 

:: Build
echo Building shared library... 
start cmd /k "cd SharedModel & mvn clean install & exit"

echo Building client... 
start cmd /k "cd MailClient & mvn clean install & exit"

echo Building server... 
start cmd /k "cd MailServer & mvn clean install & exit"

exit