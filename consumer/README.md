# Consumer – Spring Boot (`consumer`)

## Collaboratori e AI

| Ruolo | Nome |
| :--- | :--- |
| **Sviluppatore** | Daniel Salamone |
| **Sviluppatore** | Mattia Viada |

**Supporto AI:** Claude __%, DeepSeek __% (da completare), sviluppo manuale e studio personale __%

---

## Descrizione del Progetto

Il Consumer è un'applicazione **Spring Boot** con interfaccia web **Thymeleaf** (porta `8082`). Mostra il catalogo prodotti interrogando le REST API del Producer tramite `RestClient`, **senza mai accedere direttamente al database**. È in sola lettura: elenco prodotti e dettaglio.

```
Browser ──▶ Consumer :8082 ──HTTP/REST──▶ Producer :8081 ──▶ MySQL
```

---

## Tecnologie Utilizzate

Java 21 · Spring Boot 4.1.1 · Spring Web MVC · `RestClient` · Thymeleaf · Actuator (health check) · Lombok · Maven · Docker

---

## Requisiti Necessari

- **Locale:** JDK 21 e Maven (wrapper `./mvnw` incluso), più il **Producer in esecuzione** su `http://localhost:8081`
- **Docker:** Docker Engine / Docker Desktop con Docker Compose

---

## Configurazione MySQL

Il Consumer **non si connette a MySQL** e non ha alcuna configurazione database: i dati arrivano solo dal Producer. Il database (`esercitazione_api`) è gestito dal Producer, o dal container `mysqldb` in Docker tramite `init.sql`.

---

## Configurazione della Producer

Il Consumer si aspetta un Producer raggiungibile all'indirizzo configurato in `API_BASE_URL`. Le API in lettura sono pubbliche; l'header `X-API-KEY` viene comunque inviato in ogni richiesta, quindi `API_KEY` deve coincidere con la `SPRING_SECURITY_API_KEY` del Producer.

---

## Configurazione della Consumer

File: `consumer/src/main/resources/application.properties`

| Variabile d'ambiente | Default | Descrizione |
| :--- | :--- | :--- |
| `API_BASE_URL` | `http://localhost:8081/api` | URL base delle API del Producer |
| `API_KEY` | `12345678` | API Key inviata nell'header `X-API-KEY` |

Altre proprietà: `server.port=8082`, `spring.application.name=consumer`, `spring.thymeleaf.cache=false`.

In Docker, `compose.yaml` imposta `API_BASE_URL=http://producer:8081/api`: nessuna modifica al codice tra locale e container.

---

## Istruzioni per l'Avvio

**In locale** (con il Producer già avviato):
```bash
cd consumer
./mvnw spring-boot:run        # su Windows: mvnw.cmd spring-boot:run
```

**Con Docker Compose** (dalla root, avvia database, Producer e Consumer):
```bash
docker compose up --build
```

Interfaccia web: **http://localhost:8082/prodotti** (la root `/` non è mappata).

---

## REST API e Pagine

**API del Producer usate dal Consumer** (`ProdottoApiClient`):

| Metodo | Endpoint | Uso |
| :--- | :--- | :--- |
| `GET` | `/api/prodotti?nome=&categoria=&sort=&direction=` | Elenco con filtri e ordinamento |
| `GET` | `/api/prodotti/{id}` | Dettaglio prodotto |

**Pagine esposte dal Consumer:**

| URL | Descrizione |
| :--- | :--- |
| `/prodotti` | Tabella con ricerca per nome, filtro categoria e ordinamento (prezzo / nome, crescente / decrescente) |
| `/prodotti/{id}` | Dettaglio completo del prodotto |

---

## Funzionalità Aggiuntive

- **Ricerca AJAX:** tabella, filtri attivi e messaggi d'errore si aggiornano senza ricaricare la pagina (debounce 400 ms sulla digitazione, applicazione immediata sulle tendine).
- **`AbortController`** per annullare le richieste obsolete e **`history.replaceState`** per tenere l'URL condivisibile.
- **Chip dei filtri attivi** con "Rimuovi filtri" e overlay di caricamento con spinner.
- **Gestione errori:** messaggio dedicato se il Producer non è raggiungibile, redirect all'elenco se il prodotto non esiste, pagina `error.html`.
- **UI curata:** CSS custom responsive, badge colorati per categoria, icone SVG inline.
- **Health check** Actuator (`/actuator/health`) usato da Docker Compose.
- **Dockerfile multi-stage** (build Maven + runtime JDK 21).
