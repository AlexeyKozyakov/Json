Simple kotlin Json parser and serializer.
Contains two modules: json and json-reflect. 
Json module provides methods to parse json of arbitrary structure to generic json representation and work with this representation, for example retrieving some data from json fields. It also provides methods to serialize json representation to string.
Json-reflect module built on top of Json module and allows to parse json to defined by user classes by using reflection under the hood.
It also allows to write arbitrary class data to json.
See usage in [json tests](https://github.com/AlexeyKozyakov/Json/tree/main/json/src/test/java/com/alexey/kozyakov/json) and [json-reflect tests](https://github.com/AlexeyKozyakov/Json/tree/main/json-reflect/src/test/java/com/alexey/kozyakov/json/reflect).
