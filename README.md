#  Учет космических кораблей
Приложение учета космических кораблей в далеком будущем (в 3019 году). 
Поддерживаются следующие возможности:
1. получать список всех существующих кораблей;
2. создавать новый корабль;
3. редактировать характеристики существующего корабля;
4. удалять корабль;
5. получать корабль по id;
6. получать отфильтрованный список кораблей в соответствии с переданными фильтрами;
7. получать количество кораблей, которые соответствуют фильтрам.

![img.png](img.png)

# REST API
## Get ships list
|    |    |
|--- |--- |
| URL | /ships |
| Method | GET |
| URL Params | Optional: <br/> name=String<br/> planet=String<br/> shipType=ShipType<br/> after=Long<br/> before=Long<br/>isUsed=Boolean<br/>minSpeed=Double<br/>maxSpeed=Double<br/>minCrewSize=Integer<br/>maxCrewSize=Integer<br/>minRating=Double<br/>maxRating=Double<br/>order=ShipOrder<br/>pageNumber=Integer<br/>pageSize=Integer |
| Data Params | None |
| Success Response | Code: 200 OK<br/>Content: [<br/>{<br/>“id”:[Long],<br/>“name”:[String],<br/>“planet”:[String],<br/>“shipType”:[ShipType],<br/>“prodDate”:[Long],<br/>“isUsed”:[Boolean],<br/>“speed”:[Double],<br/>“crewSize”:[Integer],<br/>“rating”:[Double]<br/>},<br/>…<br/>] |
| Notes | Поиск по полям name и planet происходить по частичному соответствию. Например, если в БД есть корабль с именем «Левиафан», а параметр name задан как «иа» - такой корабль должен отображаться в результатах (Левиафан).<br/>pageNumber – параметр, который отвечает за номер отображаемой страницы при использовании пейджинга. Нумерация начинается с нуля<br/>pageSize – параметр, который отвечает за количество результатов на одной странице при пейджинге |

## Get ships count
|    |    |
|--- |--- |
| URL | /ships/count |
| Method | GET |
| URL Params | Optional:<br/>name=String<br/>planet=String<br/>shipType=ShipType<br/>after=Long<br/>before=Long<br/>isUsed=Boolean<br/>minSpeed=Double<br/>maxSpeed=Double<br/>minCrewSize=Integer<br/>maxCrewSize=Integer<br/>minRating=Double<br/>maxRating=Double |
| Data Params | None |
| Success Response | Code: 200 OK<br/>Content: Integer |
| Notes |  |

## Create ship
|    |    |
|--- |--- |
| URL | /ships |
| Method | POST |
| URL Params | None |
| Data Params | {<br/>“name”:[String],<br/>“planet”:[String],<br/>“shipType”:[ShipType],<br/>“prodDate”:[Long],<br/>“isUsed”:[Boolean], --optional, default=false<br/>“speed”:[Double],<br/>“crewSize”:[Integer]<br/>} |
| Success Response | Code: 200 OK<br/>Content: {<br/>“id”:[Long],<br/>“name”:[String],<br/>“planet”:[String],<br/>“shipType”:[ShipType],<br/>“prodDate”:[Long],<br/>“isUsed”:[Boolean],<br/>“speed”:[Double],<br/>“crewSize”:[Integer],<br/>“rating”:[Double]<br/>} |
| Notes | Мы не можем создать корабль, если:<br/>- указаны не все параметры из Data Params (кроме isUsed);<br/>- длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);<br/>- значение параметра “name” или “planet” пустая строка;<br/>- скорость или размер команды находятся вне заданных пределов;<br/>- “prodDate”:[Long] < 0;<br/>- год производства находятся вне заданных пределов.<br/>В случае всего вышеперечисленного ответ с ошибкой и кодом 400. |

## Get ship
|    |    |
|--- |--- |
| URL | /ships/{id} |
| Method | GET |
| URL Params | id |
| Data Params | None |
| Success Response | Code: 200 OK<br/>Content: {<br/>“id”:[Long],<br/>“name”:[String],<br/>“planet”:[String],<br/>“shipType”:[ShipType],<br/>“prodDate”:[Long],<br/>“isUsed”:[Boolean],<br/>“speed”:[Double],<br/>“crewSize”:[Integer],<br/>“rating”:[Double]<br/>} |
| Notes | Если корабль не найден в БД, ответ с ошибкой и кодом 404.<br/>Если значение id не валидное, ответ с ошибкой и кодом 400. |

## Update ship
|    |    |
|--- |--- |
| URL | /ships/{id} |
| Method | POST |
| URL Params | id |
| Data Params | {<br/>“name”:[String], --optional<br/>“planet”:[String], --optional<br/>“shipType”:[ShipType], --optional<br/>“prodDate”:[Long], --optional<br/>“isUsed”:[Boolean], --optional<br/>“speed”:[Double], --optional<br/>“crewSize”:[Integer] --optional<br/>} |
| Success Response | Code: 200 OK<br/>Content: {<br/>“id”:[Long],<br/>“name”:[String],<br/>“planet”:[String],<br/>“shipType”:[ShipType],<br/>“prodDate”:[Long],<br/>“isUsed”:[Boolean],<br/>“speed”:[Double],<br/>“crewSize”:[Integer],<br/>“rating”:[Double]<br/>} |
| Notes | Обновляются только те поля, которые не null.<br/>Если корабль не найден в БД, ответ с ошибкой и кодом  404.<br/>Если значение id не валидное, ответ с ошибкой и кодом  400. |

## Delete ship
|    |    |
|--- |--- |
| URL | /ships/{id} |
| Method | DELETE |
| URL Params | id |
| Data Params | None |
| Success Response | Code: 200 OK |
| Notes | Если корабль не найден в БД, ответ с ошибкой и кодом  404.<br/>Если значение id не валидное, ответ с ошибкой и кодом  400. |
