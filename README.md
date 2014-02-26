crawler-mongo
=============

1. Устанавливаем git:
	sudo apt-get install git
2. Клонируем репозиторий: 
	git clone https://github.com/alex-icez/crawler-mongo
3. Устанавливаем Java: 
	sudo apt-get install openjdk-6-jdk
4. Устанавливаем автоматический сборщик проектов maven:
	sudo apt-get install maven2
5. Пересобираем роботов:
	mvn clean install
6. Устанавливаем mongodb:
	sudo apt-get install mongodb
7. Запускаем mongodb:
	sudo service mongodb start
8. Запускаем роботов:
	java -jar target/crawler-0.0.1.jar

Пункты 4-5 можно пропустить.
