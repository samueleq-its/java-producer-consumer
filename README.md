# Esercitazione – Spring Boot REST API Producer / Consumer

---

## 👥 Collaboratori e AI

### Team di Sviluppo
| Ruolo | Nome |
| :--- | :--- |
| **Sviluppatore** | Daniel Salamone |
| **Sviluppatore** | Mattia Viada |
| **Sviluppatore** | Samuele Querio |
| **Sviluppatore** | Nicole Girardi |

### Supporto e Strumenti AI
- **Claude:** 10%
- **DeepSeek:** 30%
- **Sviluppo manuale e studio personale:** 60%

---

## 📝 Descrizione del Progetto

Il sistema è composto da due applicazioni **Spring Boot** indipendenti che comunicano tramite **REST API**, con persistenza su database **MySQL**:

1. **Producer (`product-api`, porta `8081`):** espone le REST API per il CRUD dei prodotti e gestisce la persistenza su MySQL tramite Spring Data JPA. Le operazioni di scrittura (`POST`, `PUT`, `DELETE`) sono protette da una **API Key** gestita con Spring Security.
2. **Consumer (`consumer`, porta `8082`):** interfaccia web Thymeleaf che ottiene i dati esclusivamente interrogando le REST API del Producer tramite `RestClient`, senza mai accedere al database. Il Consumer è in sola lettura (elenco e dettaglio prodotti).

Il progetto è completamente containerizzato: un singolo `docker compose up` avvia database, Producer e Consumer.

### Architettura del Sistema

```
Browser
  │
  ▼
Consumer :8082 ──HTTP/REST──▶ Producer :8081 ──JDBC/JPA──▶ MySQL :3306
```

### Struttura del Repository

```
.
├── compose.yaml
├── init.sql
├── .dockerignore
├── consumer/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/
│       ├── java/salamone/viada/consumer/
│       │   ├── ConsumerApplication.java
│       │   ├── client/ProdottoApiClient.java
│       │   ├── config/RestClientConfig.java
│       │   ├── controller/ProdottoWebController.java
│       │   ├── dto/ProdottoDTO.java
│       │   └── exception/{ApiNonDisponibileException,ProdottoNonTrovatoException}.java
│       └── resources/
│           ├── application.properties
│           ├── static/css/style.css
│           └── templates/{prodotti,dettaglio,error}.html
│
└── producer/
    ├── Dockerfile
    ├── dockerfile.db          (Dockerfile alternativo per MySQL, non usato da compose.yaml)
    ├── pom.xml
    ├── readme.md              (documentazione specifica del Producer)
    ├── TODO.txt
    └── src/main/
        ├── java/com/girque/
        │   ├── ProducerApplication.java
        │   ├── auth/{SecurityConfig,AuthFilter,ApiKeyAuthentication}.java
        │   ├── config/WebConfig.java
        │   ├── controller/ProdottoController.java
        │   ├── dto/{ProdottoDTO,ProdottoRequestDTO,FiltroDTO}.java
        │   ├── entity/Prodotto.java
        │   ├── exception/{GlobalExceptionHandler,ProdottoNotFoundException}.java
        │   ├── repos/ProdottoRepository.java
        │   └── services/{ProdottoService,ProdottoServiceImpl}.java
        └── resources/
            ├── application.properties
            ├── data.sql
            └── META-INF/additional-spring-configuration-metadata.json
```

---

## 🛠 Tecnologie Utilizzate

| Ambito | Tecnologie / Librerie |
| :--- | :--- |
| **Linguaggio** | Java 21 |
| **Framework Base** | Spring Boot 4.1.1 |
| **Web & REST** | Spring Web MVC |
| **Persistenza & DB** | Spring Data JPA, Hibernate, MySQL (immagine Docker `mysql:9.7`), MySQL Connector/J |
| **Sicurezza Producer** | Spring Security (autenticazione stateless via API Key) |
| **Health check** | Spring Boot Actuator (`/actuator/health` su Producer e Consumer) |
| **HTTP Client (Consumer)** | Spring `RestClient` |
| **Template Engine** | Thymeleaf |
| **Utility** | Lombok (usato nel Consumer) |
| **Build System** | Apache Maven (wrapper `mvnw` incluso) |
| **Containerizzazione** | Docker, Docker Compose (build multi-stage) |

---

## 📌 Requisiti Necessari

### Esecuzione in Locale
- **JDK 21**
- **Maven:** si usa il wrapper incluso (`./mvnw`, su Windows `mvnw.cmd`)
- **MySQL** attivo su `localhost:3306` con credenziali `root` / `root` (modificabili tramite variabili d'ambiente, vedi sotto)

### Esecuzione in Docker
- Docker Engine / Docker Desktop con Docker Compose

---

## 🗄 Database e Inizializzazione Dati

Il comportamento cambia a seconda dell'ambiente.

### Ambiente Locale
Serve solo che esista il database vuoto:

```sql
CREATE DATABASE IF NOT EXISTS esercitazione_api
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Con la configurazione di default (`spring.jpa.hibernate.ddl-auto=create`) Hibernate **ricrea la tabella `prodotti` ad ogni avvio** e `data.sql` ricarica i 20 prodotti di prova. Di conseguenza in locale i dati vengono **sempre riportati allo stato iniziale** a ogni riavvio del Producer.

### Ambiente Docker
- Il container MySQL esegue `init.sql` (montato in `/docker-entrypoint-initdb.d`) **solo al primo avvio**, quando il volume è vuoto: crea database e tabella e inserisce i 20 prodotti di prova.
- Il Producer parte con `SPRING_JPA_HIBERNATE_DDL_AUTO=update` e `SPRING_SQL_INIT_MODE=never`: non ricrea lo schema e non ricarica `data.sql`.
- I dati sono **persistenti** tra un riavvio e l'altro grazie al volume Docker `mysqldb`. Per ripartire da zero: `docker compose down -v`.
- Il MySQL del container è esposto sulla porta **3305** dell'host (internamente resta 3306), così non entra in conflitto con un eventuale MySQL locale sulla 3306.

---

## ⚙️ Configurazione

Tutte le impostazioni sensibili all'ambiente sono variabili d'ambiente con un valore di default adatto al test in locale: nessuna modifica al codice è necessaria per passare da locale a Docker.

### Producer – `producer/src/main/resources/application.properties`

| Variabile d'ambiente | Default | Descrizione |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/esercitazione_api` | URL JDBC |
| `SPRING_DATASOURCE_USERNAME` | `root` | Utente DB |
| `SPRING_DATASOURCE_PASSWORD` | `root` | Password DB |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `create` | Strategia schema Hibernate (in Docker: `update`) |
| `SPRING_SQL_INIT_MODE` | `always` | Esecuzione di `data.sql` (in Docker: `never`) |
| `SPRING_SECURITY_ENABLE` | `true` | Abilita/disabilita la protezione delle scritture (`security.enabled`) |
| `SPRING_SECURITY_API_KEY` | `12345678` | API Key attesa (`security.api-key`) |

Altre impostazioni: `server.port=8081`, `spring.application.name=product-api`, `spring.jpa.defer-datasource-initialization=true` (esegue `data.sql` dopo la creazione delle tabelle), `spring.sql.init.continue-on-error=true`.

### Consumer – `consumer/src/main/resources/application.properties`

| Variabile d'ambiente | Default | Descrizione |
| :--- | :--- | :--- |
| `API_BASE_URL` | `http://localhost:8081/api` | URL base delle API del Producer |
| `API_KEY` | `12345678` | API Key inviata nell'header `X-API-KEY` |

Altre impostazioni: `server.port=8082`, `spring.application.name=consumer`, `spring.thymeleaf.cache=false`.

> **Integrazione degli ambienti:** in Docker, `compose.yaml` imposta `API_BASE_URL=http://producer:8081/api` (il nome del servizio compose fa da hostname sulla rete interna) e la stessa API Key su Producer e Consumer.

> **Nota di sicurezza:** `compose.yaml` contiene un'API Key di esempio (UUID) scritta in chiaro, e le credenziali DB `root`/`root`. Vanno bene per l'esercitazione, ma in un contesto reale devono stare in un file `.env` o in un secret manager.

### Architettura interna del Producer

**Controller** ──▶ **Service** (interfaccia + implementazione) ──▶ **Repository** ──▶ **Entity** ──▶ **MySQL**

Filtri e ordinamento sono applicati nel `ProdottoServiceImpl`: carica i prodotti con `findAll()` e applica `FiltroDTO` (che funge sia da predicato di filtro sia da `Comparator`) con gli stream Java.

---

## 🔐 Autenticazione (Producer)

Le letture sono pubbliche, le scritture richiedono una API Key valida.

| Metodo HTTP | Endpoint | Accesso |
| :--- | :--- | :--- |
| `GET` | `/api/prodotti` | Pubblico |
| `GET` | `/api/prodotti/{id}` | Pubblico |
| `GET` | `/actuator/health` | Pubblico (usato dagli healthcheck Docker) |
| `POST` | `/api/prodotti` | **API Key richiesta** |
| `PUT` | `/api/prodotti/{id}` | **API Key richiesta** |
| `DELETE` | `/api/prodotti/{id}` | **API Key richiesta** |

- **Header:** `X-API-KEY: <valore>`
- **Implementazione:** `AuthFilter` (filtro `OncePerRequestFilter`, sessione stateless, CSRF disabilitato) confronta l'header con il valore di `security.api-key`; `SecurityConfig` richiede l'autenticazione per `POST`/`PUT`/`DELETE` e lascia tutto il resto pubblico.
- **Disattivazione:** impostando `SPRING_SECURITY_ENABLE=false` la protezione viene disabilitata (utile per test rapidi).
- Il Consumer invia l'header `X-API-KEY` in ogni richiesta, anche se oggi esegue solo letture.

Esempio:

```bash
curl -X POST http://localhost:8081/api/prodotti \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: 12345678" \
  -d '{"nome":"Laptop Pro 15","descrizione":"Notebook professionale","prezzo":1299.90,"categoria":"Informatica","quantita":15}'
```

---

## 🚀 Istruzioni per l'Avvio

### 1. Avvio tramite Docker Compose (consigliato)

Dalla root del progetto:

```bash
docker compose up --build
docker ps
```

Container creati:

| Container | Servizio | Porta host |
| :--- | :--- | :--- |
| `mysqldb` | MySQL | `3305` |
| `producer` | Producer API | `8081` |
| `consumer` | Consumer web | `8082` |

Ordine di avvio garantito dagli healthcheck: `mysqldb` (mysqladmin ping) → `producer` (`/actuator/health`) → `consumer` (`/actuator/health`). Producer e Consumer hanno `restart: on-failure`, il database `unless-stopped`.

Comandi utili:

```bash
docker compose up -d           # avvio in background
docker compose stop            # ferma i container senza rimuoverli
docker compose down            # ferma e rimuove container e rete
docker compose down -v         # rimuove anche il volume dei dati MySQL
docker compose logs -f         # log in tempo reale
```

### 2. Avvio in Locale

Prerequisiti: MySQL su `localhost:3306` con il database `esercitazione_api` già creato (vedi sopra). Aprire due terminali.

**Terminale 1 – Producer:**
```bash
cd producer
./mvnw spring-boot:run
```
Endpoint: `http://localhost:8081/api/prodotti`

**Terminale 2 – Consumer** (dopo che il Producer è partito):
```bash
cd consumer
./mvnw spring-boot:run
```
Interfaccia web: `http://localhost:8082/prodotti`

Su Windows usare `mvnw.cmd` al posto di `./mvnw`.

> Se si usa `./mvnw package` (o `clean package`) nel Producer, il test `ProducerApplicationTests` carica l'intero contesto Spring e quindi **richiede MySQL attivo**; in alternativa aggiungere `-DskipTests` (come fanno i Dockerfile).

---

## 📡 REST API Disponibili (Producer)

**Base URL:** `http://localhost:8081/api/prodotti`

### Operazioni CRUD

| Metodo | Endpoint | Risposta | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/prodotti` | `200 OK` | Pubblica |
| `GET` | `/api/prodotti/{id}` | `200 OK` / `404 Not Found` | Pubblica |
| `POST` | `/api/prodotti` | `201 Created` | API Key |
| `PUT` | `/api/prodotti/{id}` | `200 OK` / `404 Not Found` | API Key |
| `DELETE` | `/api/prodotti/{id}` | `200 OK` (corpo vuoto) | API Key |

Note:
- `POST` e `PUT` accettano un `ProdottoRequestDTO` (`nome`, `descrizione`, `prezzo`, `categoria`, `quantita`); `id` e `dataCreazione` sono assegnati dal server (in `PUT` la data di creazione originale viene mantenuta).
- `DELETE` non verifica l'esistenza del prodotto: restituisce `200 OK` anche se l'id non esiste (nessun `404`).
- Un body JSON malformato produce `400 Bad Request`.

### Filtri, Ricerca e Ordinamento

I parametri di query di `GET /api/prodotti` sono mappati su `FiltroDTO`:

| Parametro | Valori | Comportamento |
| :--- | :--- | :--- |
| `nome` | testo | Ricerca *contiene*, case-insensitive, sul nome |
| `categoria` | testo | Ricerca *contiene*, case-insensitive, sulla categoria |
| `sort` | `id`, `nome`, `descrizione`, `prezzo`, `categoria`, `quantita`, `datacreazione` | Campo di ordinamento. Se omesso, nessun ordinamento (ordine del DB) |
| `direction` | `asc` (default), `desc` | Direzione; ha effetto solo se `sort` è presente |

Esempi:

| Endpoint | Descrizione |
| :--- | :--- |
| `/api/prodotti?categoria=Informatica` | Prodotti della categoria Informatica |
| `/api/prodotti?nome=laptop` | Ricerca per nome |
| `/api/prodotti?sort=prezzo` | Per prezzo crescente |
| `/api/prodotti?sort=prezzo&direction=desc` | Per prezzo decrescente |
| `/api/prodotti?categoria=Accessori&sort=prezzo&direction=asc` | Filtro + ordinamento |

### Gestione Errori Centralizzata

`GlobalExceptionHandler` (`@RestControllerAdvice`) restituisce un JSON standard per `ProdottoNotFoundException` (404) e per i body non leggibili (400):

```json
{
  "status": 404,
  "message": "Prodotto non trovato",
  "timestamp": "2026-10-21T09:57:00"
}
```

---

## 💻 Pagine e Routing (Consumer)

| URL | Descrizione |
| :--- | :--- |
| `http://localhost:8082/prodotti` | Tabella prodotti con ricerca per nome, filtro categoria e ordinamento (prezzo / nome, crescente / decrescente) |
| `http://localhost:8082/prodotti/{id}` | Dettaglio del prodotto (categoria, prezzo, quantità, data di creazione, descrizione) |

La root `/` non è mappata: usare direttamente `/prodotti`. Se l'id richiesto non esiste si viene riportati all'elenco con un messaggio d'errore; se il Producer non è raggiungibile viene mostrato un messaggio nell'elenco (o la pagina `error.html` dal dettaglio).

L'elenco delle categorie nel filtro è definito nel `ProdottoWebController` (Informatica, Accessori, Audio, Storage, Mobile, Wearable, Networking, Ufficio).

---

## ✨ Funzionalità Implementate

- **Spring Security con API Key:** lettura pubblica, scrittura protetta tramite header `X-API-KEY`; protezione disattivabile con `SPRING_SECURITY_ENABLE=false`.
- **Pattern DTO:** le entity JPA non vengono mai esposte. Il Producer usa `ProdottoDTO` (risposte), `ProdottoRequestDTO` (input `POST`/`PUT`) e `FiltroDTO` (filtri e ordinamento); il Consumer ha un proprio `ProdottoDTO`.
- **Service layer:** `ProdottoService` (interfaccia) e `ProdottoServiceImpl`.
- **Gestione errori centralizzata** con `@RestControllerAdvice` lato Producer; eccezioni dedicate lato Consumer (`ApiNonDisponibileException`, `ProdottoNonTrovatoException`).
- **Configurazione tramite variabili d'ambiente** con default per l'esecuzione locale.
- **Health check Actuator** su entrambe le applicazioni, usato da Docker Compose per ordinare l'avvio dei servizi.
- **Docker:** Dockerfile multi-stage per Producer e Consumer (build Maven + runtime JDK 21 con `curl` per gli healthcheck), `compose.yaml` con tre servizi, volume persistente per MySQL e inizializzazione dati con `init.sql`.
- **UX del Consumer con AJAX:**
  - aggiornamento automatico della tabella: digitazione con **debounce di 400 ms**, applicazione immediata sulle tendine, `Invio` per aggiornare subito;
  - ricaricamento parziale di tabella, chip dei filtri attivi e messaggi d'errore senza refresh della pagina (l'input di ricerca non viene toccato, quindi mantiene focus e cursore);
  - `AbortController` per annullare le richieste obsolete durante la digitazione rapida;
  - `history.replaceState` per mantenere l'URL sincronizzato con i filtri (condivisibile / bookmarkabile);
  - chip "Filtri attivi" con link "Rimuovi filtri", overlay di caricamento con spinner.
- **Interfaccia curata:** CSS custom responsive, badge colorati per categoria, icone SVG inline.

---

## 🔭 Possibili Sviluppi

- Pubblicazione delle immagini su Docker Hub (non ancora fatta: `compose.yaml` fa la `build` dai sorgenti).
- Filtri e ordinamento eseguiti direttamente nel repository/database invece che in memoria.
- `404` su `DELETE` di un id inesistente e header `Location` nella risposta `201` del `POST`.
- Segreti (API Key, password DB) fuori da `compose.yaml`, in un file `.env`.
