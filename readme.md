# Struktúraváltó - Junior Vállalati Java Backend vizsgaremek - webshop

## Vízió

A vizsgaremek egy webshop backend rész funkcióit valósítja meg.

Az áruház több kategóriában is kínál termékeket. Ezek a kategóriák szabadon bővíthetők, módosíthatók, vagy törölhetők.
Lehetőség van az áruházba új terméket felvenni, törölni, vagy a termékek adatait igény szerint módisítani.
A felhasználók, hogy segítség a többi felhasználót, pontozhatják az egyes termékeket. A termékek átlagpontja jelenjen meg az adott termék lekérdezésekor.
Az áruházból csak regisztrált ügyfelek vásárolhatnak. Ezért szükséges az ügyfelek adatainak kezelése is.
Tudjon egy új ügyfél regisztrálni, legyen lehetősége a regisztráció során megadott adatait módosítani, illetve törölhesse is a regisztrációját.
Rendelés leadásakor, a megvásárolt mennyiséggel csökkenteni kell a raktárkészletet. Ha nincs a kívánt mennyiség raktáron, vissza kell utasítani a megrendelést!
Leadott rendelés utólag már nem módosítható.


Projekt neve: `webshop`.
Az adatbázis, és a felhasználó neve, jelszava: `webshop`.

## Sprint 1
### Funkcionális követelmények
A következőket kell karbantartani:
* termékcsoportokat (`ProductCategoryType`)
* termékeket (`Product`)
* ügyfelek (`Customer`)
* ügyfelekhez tartozó címeket (`Address`)
* Megrendelést (`Order`)

  Egy résztvevő akár több rendelést is leadhat.

#### Termékcsoport
* Elnevezés (nem üres, max. 100 karakter)
* Leírás (tetszőleges, de max 255 karakter)
  
  Lehet listázni, lekérdezni, létrehozni, mindkét attribútumot módosítani, törölni az `/api/products` végponton.

#### Termékek
* Név (nem üres, max. 100 karakter)
* Egységár (egész szám)
* Raktárkészlet (egész szám)
* Termékcsoport
* Pontozások (1-5 osztályzatok)
* Leírás (tetszőleges, de max 255 karakter)

  Lehet listázni (többféleképpen, attribútumai alapján is), lekérdezni, létrehozni, attribútumait módosítani, értékelni, törölni az `/api/products` végponton.

#### Címek
* Város neve (nem üres, max. 50 karakter)
* Irányító szám (nem üres, pontosan 4 számjegy)
* Utca, házszám (nem üres, max. 100 karakter)
* Megjegyzés (tetszőleges, de max 255 karakter)

  Lehet listázni, lekérdezni, létrehozni, attribútumait módosítani, törölni az `/api/addresses` végponton.

#### Ügyfelek
* Név (nem üres, max. 100 karakter)
* E-mail cím (nem üres, max. 100 karakter)
* Címek (szállítási, számlázási)
* Megjegyzés (tetszőleges, de max 255 karakter)

  Lehet listázni, lekérdezni, létrehozni, attribútumait módosítani, törölni az `/api/customers` végponton.

#### Megrendelés
* Felhasználó
* Termékek
* Megrendelés dátuma (tetszőleges)
* Megjegyzés (tetszőleges, de max 255 karakter)

  A résztvevőket is lehet felvenni, lekérdezni, listázni, módosítani és törölni
az `/api/orders` végponton.

### Nem-funkcionális követelmények
Klasszikus háromrétegű alkalmazás, MariaDB adatbázissal, Java Spring backenddel, REST webszolgáltatásokkal.

#### Követelmények tételesen:
* Hozz létre egy `README.md` fájlt, ami tartalmaz egy rövid leírást a projektről
* SQL adatbázis kezelő réteg megvalósítása Spring Data JPA-val (`Repository`)
* Flyway - a scriptek a funkciókkal együtt készüljenek, szóval ahogy bekerül az entitás, úgy kerüljön be egy
  plusz script is, ami a táblát létrehozza
* Üzleti logika réteg megvalósítása `@Service` osztályokkal
* Integrációs tesztek megléte (elég TestRestTemplate tesztek), legalább 80%-os tesztlefedettség
* Controller réteg megvalósítása, RESTful API implementálására. Az API végpontoknak a `/api` címen kell elérhetőeknek lenniük.
* Hibakezelés, validáció
* Swagger felület
* HTTP fájl a teszteléshez
* Dockerfile
* Új repository-ba kell dolgozni, melynek címe szabadon választható.
* Commitolni legalább entitásonként, és hozzá tartozó REST végpontonként

#### Feladat nagysága
* Legalább két tábla, 1-n kapcsolatban
* Legalább két SQL migráció
* Legalább két entitás
* Legalább két service
* Legalább két controller
* Minden HTTP metódusra legalább egy végpont (`GET`, `POST`, `PUT`, `DELETE`)