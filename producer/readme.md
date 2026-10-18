# COLLABORATORI
- Nicole Girardi
- Samuele Querio
- AI: Gemini

# DESCRIZIONE
Il producer e una REST API per la gestione di un catalogo di prodotti. Espone
operazioni CRUD utilizzate dal servizio consumer.

# TECNOLOGIE
Java 21, Spring Boot, Spring Web MVC, Spring Data JPA, Hibernate, MySQL,
Spring Security e Maven.

# CONFIGURAZIONE SQL
se avviato in locale il db è inizializzato ogni volta all'avvio di spring, resettando i prodotti sempre allo stato iniziale,
mentre quando è avviato in un container docker viene inizializzato automaticamente tramite init.sql e mantiene la persistenza dei dati tra un avvio e l'altro
il container espone la porta 3305

# CONFIGURAZIONE PRODUCER
espone API REST su porta 8081
accesso al db, inizializzazione dei dati e sicurezza sono configurabili tramite variabili d'ambiente, i valori di default permettono il testing in locale

# ISTRUZIONI DI AVVIO
Per avviare l'intero progetto, dalla cartella principale eseguire:

```bash
docker compose up
```

Per avviare solo il producer in locale, assicurarsi che MySQL sia disponibile,
poi eseguire dalla cartella `producer`:

```bash
./mvnw spring-boot:run
```

Su Windows usare `mvnw.cmd spring-boot:run`.

# API
Base URL: `http://localhost:8081/api/prodotti`

| Metodo | Endpoint | Descrizione |
|---|---|---|
| GET | `/api/prodotti` | Restituisce tutti i prodotti; supporta `nome`, `categoria`, `sort` e `direction` come parametri di query. |
| GET | `/api/prodotti/{id}` | Restituisce un prodotto tramite ID. |
| POST | `/api/prodotti` | Crea un prodotto. |
| PUT | `/api/prodotti/{id}` | Modifica un prodotto esistente. |
| DELETE | `/api/prodotti/{id}` | Elimina un prodotto. |

GET `/api/prodotti` permette di filtrare per `nome` e `categoria`, fare il sorting su ogni attributo del prodotto
```text
/api/prodotti?nome=[nome]&categoria=[categoria]&sort=[nome/descrizione/prezzo/categoria/quantita/datacreazione]&direction=[asc/desc]
```

Per `POST`, `PUT` e `DELETE` e necessario inviare l'header:

```text
X-API-KEY: <valore-configurato-in-SPRING_SECURITY_API_KEY>
```

Esempio di body per `POST` e `PUT`:

```json
{
	"nome": "Laptop Pro 15",
	"descrizione": "Notebook professionale",
	"prezzo": 1299.90,
	"categoria": "Informatica",
	"quantita": 15
}
```

# FUNZIONALITA' AGGIUNTIVE
Autenticazione stateless tramite API key. Le richieste `GET` sono pubbliche,
mentre le operazioni di creazione, modifica ed eliminazione richiedono una
API key valida nell'header `X-API-KEY`. E disponibile anche l'endpoint di
health check `/actuator/health` usato da Docker Compose.
