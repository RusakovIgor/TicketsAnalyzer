# 🎫 Ticket Analyzer

Java-приложение для анализа авиабилетов с расчетом статистики по времени полета и ценам.

## 📊 Результаты анализа примерного набора данных

### Минимальное время полета между Владивостоком и Тель-Авивом:
| Авиакомпания | Время полета    |
|-------------|-----------------|
| **SU**      | 6 часов 0 минут |
| **S7**      | 6 часов 30 минут |
| **TK**      | 5 часов 50 минут |
| **BA**      | 8 часов 5 минут |

### Статистика по ценам на билеты:
- **Средняя цена**: 13 960,00 рублей
- **Медианная цена**: 13 500,00 рублей  
- **Разница между средней и медианной ценой**: 460,00 рублей

## ✨ Возможности

- 📊 Анализ времени полета между городами
- 💰 Расчет статистики по ценам (среднее, медиана)
- 🎯 Фильтрация билетов по направлению (VVO → TLV)
- ⏰ Поддержка различных форматов времени
- 🛡️ Надежная обработка ошибок и валидация данных
- 📁 Работа с JSON форматом

## 🚀 Быстрый старт

### Предварительные требования
- Java 11 или выше
- Maven 3.6+ (опционально)

### Установка
```bash
# Клонирование репозитория
git clone https://github.com/ваш_username/ticket-analyzer.git
cd ticket-analyzer

### Запуск через maven
# Компиляция проекта
mvn compile

# Запуск приложения
mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="tickets.json"

###Запуск через java
# 1. Скачать JSON библиотеку (если нет Maven)
curl -O https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar

# 2. Компиляция
javac -cp ".:json-20231013.jar" src/main/java/TicketAnalyzer.java

# 3. Запуск с указанием пути к файлу
java -cp ".:json-20231013.jar" TicketAnalyzer tickets.json

# Или с абсолютным путем
java -cp ".:json-20231013.jar" TicketAnalyzer /полный/путь/к/tickets.json

# Для Mac/Linux (используйте : вместо ;)
java -cp ".:json-20231013.jar" TicketAnalyzer tickets.json

# Если уже компилировали через Maven
java -cp "target/classes:json-20231013.jar" TicketAnalyzer tickets.json

# Или с зависимостями Maven
java -cp "target/classes:$(find ~/.m2/repository -name '*.jar' | grep json | head -1)" TicketAnalyzer tickets.json

Формат входных данных
Файл tickets.json должен содержать данные в следующем формате:

json
{
  "tickets": [
    {
      "origin": "VVO",
      "origin_name": "Владивосток",
      "destination": "TLV", 
      "destination_name": "Тель-Авив",
      "departure_date": "12.05.18",
      "departure_time": "16:20",
      "arrival_date": "12.05.18",
      "arrival_time": "22:10",
      "carrier": "TK",
      "stops": 3,
      "price": 12400
    }
  ]
}

Автор
Русаков Игорь - Java Developer


