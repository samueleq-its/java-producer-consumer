# Esercitazione – Spring Boot REST API Producer / Consumer

---

## 👥 Collaboratori e AI

### Team di Sviluppo
| Ruolo | Nome |
| :--- | :--- |
| **Sviluppatore** | Daniel Salamone |
| **Sviluppatore** | Viada |

### Supporto e Strumenti AI
- **Claude (Anthropic):** 40%
- **ChatGPT (OpenAI):** 30%
- **Sviluppo manuale e studio personale:** 30%

---

## 📝 Descrizione del Progetto

Il sistema è composto da due applicazioni **Spring Boot** indipendenti che comunicano tramite **REST API**, con persistenza su database **MySQL**:

1. **Producer Application (`product-api`, `:8081`):** Espone le REST API per il CRUD dei prodotti e gestisce la persistenza dei dati su MySQL tramite Spring Data JPA. Le operazioni di scrittura (POST, PUT, DELETE) sono protette tramite API Key gestita con Spring Security.
2. **Consumer Application (`product-client`, `:8082`):** Interfaccia web sviluppata con Thymeleaf che consuma i dati esclusivamente interrogando le REST API del Producer tramite `RestClient`, senza mai accedere direttamente al database.

Il progetto include il supporto completo a **Docker** e **Docker Compose** per l'orchestratore multi-container e la pubblicazione su **Docker Hub**.

### Architettura del Sistema

```
Browser
  │
  ▼
Consumer :8082 ──HTTP/REST──▶ Producer :8081 ──JDBC/JPA──▶ MySQL :3306
```

> **Nota:** In ambiente Docker, i tre servizi comunicano attraverso una rete dedicata (`bridge`). MySQL utilizza un volume persistente (`mysql-data`) per preservare i dati. Il Consumer è completamente disaccoppiato dal DB.

### Struttura del Repository

```
.
├── compose.yaml
├── init.sql
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
    ├── pom.xml
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
| **Persistenza & DB** | Spring Data JPA, Hibernate, MySQL 8 |
| **Sicurezza Producer** | Spring Security (Autenticazione basata su API Key) |
| **HTTP Client (Consumer)** | Spring `RestClient` |
| **Template Engine** | Thymeleaf |
| **Utility** | Lombok |
| **Build System** | Apache Maven |
| **Containerizzazione** | Docker, Docker Compose |

---

## 📌 Requisiti Necessari

### Esecuzione in Locale
- **Java Development Kit (JDK):** Versione 21
- **Build Tool:** Maven 3.9+ (oppure l'eseguibile incluso `./mvnw`)
- **Database:** MySQL 8 attivo su `localhost:3306`

### Esecuzione in Docker
- **Engine:** Docker Engine / Docker Desktop
- **Orchestratore:** Docker Compose v2+
- *(Opzionale)* Account Docker Hub per il push/pull delle immagini pre-built

---

## 🗄 Configurazione MySQL

Per l'esecuzione locale, creare il database eseguendo lo script seguente:

```sql
CREATE DATABASE IF NOT EXISTS esercitazione_api
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE esercitazione_api;

CREATE TABLE prodotti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    descrizione TEXT,
    prezzo DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    quantita INT NOT NULL DEFAULT 0,
    data_creazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

- **Ambiente Locale:** I 20 prodotti di prova vengono caricati automaticamente all'avvio dal file `producer/src/main/resources/data.sql`.
- **Ambiente Docker:** Il DB viene inizializzato tramite lo script `init.sql` e lo schema della tabella viene gestito da Hibernate via `spring.jpa.hibernate.ddl-auto=update`.

---

## ⚙️ Configurazione della Producer Application (`product-api`)

File: `producer/src/main/resources/application.properties`

```properties
spring.application.name=product-api
server.port=8081

# Connessione al Database
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/esercitazione_api}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root}

# Configurazione JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Sicurezza API Key
api.key=${API_KEY:default-secret-key}
```

> **Architettura interna Producer:**
> Il Producer è organizzato secondo un'architettura a livelli ben definita:
> **Controller** ──▶ **Service** (Interfaccia + Implementation) ──▶ **Repository** ──▶ **Entity** ──▶ **MySQL**

---

## ⚙️ Configurazione della Consumer Application (`product-client`)

File: `consumer/src/main/resources/application.properties`

```properties
spring.application.name=product-client
server.port=8082

# URL Base delle REST API del Producer
api.base-url=${PRODUCER_URL:http://localhost:8081}/api
api.key=${API_KEY:default-secret-key}

# Disabilitazione cache Thymeleaf per sviluppo
spring.thymeleaf.cache=false
```

> **Integrazione degli Ambienti:**
> - In **Locale**: `PRODUCER_URL` assume il valore di default `http://localhost:8081`.
> - In **Docker**: `PRODUCER_URL` viene sovrascritta con `http://producer:8081` mediante le variabili d'ambiente di Docker Compose, senza richiedere alcuna modifica al codice sorgente.

---

## 🔐 Autenticazione (Producer)

Le operazioni di lettura sulle API del Producer sono pubbliche, mentre le operazioni di scrittura richiedono un'autenticazione tramite **API Key**.

| Metodo HTTP | Endpoint | Accesso / Auth |
| :--- | :--- | :--- |
| `GET` | `/api/prodotti` | **Pubblica** |
| `GET` | `/api/prodotti/{id}` | **Pubblica** |
| `POST` | `/api/prodotti` | **API Key richiesta** |
| `PUT` | `/api/prodotti/{id}` | **API Key richiesta** |
| `DELETE` | `/api/prodotti/{id}` | **API Key richiesta** |

- **Header Custom:** `X-API-KEY: <valore>`
- **Configurazione:** Il valore atteso dell'API Key è configurabile sia tramite la proprietà `api.key` che tramite la variabile d'ambiente `API_KEY`.

---

## 🚀 Istruzioni per l'Avvio

### 1. Avvio in Locale

Aprire due terminali distinti per avviare i due microservizi.

**Terminale 1 — Producer Application:**
```bash
cd producer
./mvnw clean package
./mvnw spring-boot:run
```
*Endpoint Producer:* `http://localhost:8081/api/prodotti`

**Terminale 2 — Consumer Application:**
```bash
cd consumer
./mvnw clean package
./mvnw spring-boot:run
```
*Interfaccia Web Consumer:* `http://localhost:8082/prodotti`

---

### 2. Avvio tramite Docker Compose

Dalla root principale del progetto, eseguire:

```bash
# Build e avvio di tutti i container (DB, Producer, Consumer)
docker compose up --build

# Verificare lo stato dei container in esecuzione
docker ps
```

Container creati:
- `esercitazione-db` (Porta `3306`)
- `esercitazione-producer` (Porta `8081`)
- `esercitazione-consumer` (Porta `8082`)

**Comandi Docker utili:**
```bash
docker compose up -d           # Avvio in background (detached)
docker compose stop            # Arresta i container senza rimuoverli
docker compose down            # Arresta e rimuove container e reti
docker compose down -v         # Rimuove container, reti e anche il volume dei dati MySQL
docker compose logs -f         # Visualizza i log in tempo reale di tutti i servizi
```

---

## 📡 REST API Disponibili (Producer)

**Base URL:** `http://localhost:8081/api/prodotti`

### Operations CRUD

| Metodo | Endpoint | Codici HTTP Response | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/prodotti` | `200 OK` | Pubblica |
| `GET` | `/api/prodotti/{id}` | `200 OK` / `404 Not Found` | Pubblica |
| `POST` | `/api/prodotti` | `201 Created` | API Key |
| `PUT` | `/api/prodotti/{id}` | `200 OK` / `404 Not Found` | API Key |
| `DELETE` | `/api/prodotti/{id}` | `204 No Content` / `404 Not Found` | API Key |

### Filtri, Ricerca e Ordinamento

I parametri di richiesta vengono mappati nell'oggetto `FiltroDTO` gestito dal controller tramite `@RequestParam`:

| Endpoint | Descrizione |
| :--- | :--- |
| `/api/prodotti?categoria=Informatica` | Filtra i prodotti per categoria esatta |
| `/api/prodotti?nome=laptop` | Ricerca case-insensitive sul nome del prodotto |
| `/api/prodotti?sort=prezzo` | Ordina i prodotti per prezzo (crescente di default) |
| `/api/prodotti?sort=prezzo&direction=desc` | Ordina i prodotti per prezzo decrescente |
| `/api/prodotti?categoria=Accessori&sort=prezzo&direction=asc` | Combinazione di filtri e ordinamento |

### Gestione Errori Centralizzata

Le eccezioni vengono gestite da `@RestControllerAdvice` (`GlobalExceptionHandler`), che restituisce una struttura JSON standardizzata:

```json
{
  "status": 404,
  "message": "Prodotto non trovato",
  "timestamp": "2026-10-21T09:57:00"
}
```

---

## 💻 Pagine e Routing (Consumer)

| URL | Descrizione Interfaccia |
| :--- | :--- |
| `http://localhost:8082/prodotti` | Tabella dinamica prodotti con ricerca, filtro categoria e ordinamento |
| `http://localhost:8082/prodotti/{id}` | Vista di dettaglio completa del singolo prodotto |

---

## ✨ Funzionalità Aggiuntive Implementate

- **Protezione con Spring Security:** Autenticazione basata su API Key personalizzata per separare le operazioni pubbliche (lettura) da quelle protette (scrittura).
- **Pattern DTO (Data Transfer Object):** Separazione netta tra il dominio e l'API. Utilizzo di `ProdottoDTO`, `ProdottoRequestDTO` e `FiltroDTO` sia sul Producer che sul Consumer. Le entità del DB non vengono mai esposte all'esterno.
- **Disaccoppiamento tramite Service Layer:** Implementazione del pattern `ProdottoService` (interfaccia) e `ProdottoServiceImpl` (implementazione).
- **UX Reattiva e Fluidità AJAX (Client-side):**
  - **Auto-submit con Debounce:** Filtraggio automatico dei dati con un delay di 350ms sulla digitazione e applicazione immediata sulle sezioni `<select>`.
  - **Aggiornamento AJAX dinamico:** Ricaricamento parziale della tabella dei prodotti senza refresh della pagina, preservando lo stato del cursore e il focus dell'input.
  - **AbortController JS:** Cancellazione automatica delle richieste HTTP pendenti o obsolete durante la digitazione rapida.
  - **Sincronizzazione URL (`history.replaceState`):** Mantiene gli argomenti di ricerca e i filtri sempre sincronizzati con l'URL per consentire il bookmarking o la condivisione del link.
- **Interfaccia Utente Curata:** CSS custom responsive, badge visivi per le categorie, overlay di caricamento con spinner e icone SVG inline.
- **Robustezza ed Error Handling:** Gestione elegante delle disconnessioni: messaggio di errore dedicato nella UI Consumer qualora il Producer risulti irraggiungibile o spento.
- **Inizializzazione Dati Automatica:** Caricamento automatico dei dati di prova con `data.sql` in locale e `init.sql` su Docker.
- **Docker Integration Advanced:** File `compose.yaml` ottimizzato, gestione dei volumi per MySQL e immagini pubblicabili su Docker Hub (`producer-api`, `consumer-web`) per consentire il deploy rapido su ambienti server sprovvisti del codice sorgente.
