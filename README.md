# FoodMood - Progetto ISPW

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/images/project_badges/sonarcloud-light.svg" alt="SonarQube Cloud">
  </a>
</p>

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=alert_status" alt="Quality Gate Status">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=ncloc" alt="Lines of Code">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=security_rating" alt="Security Rating">
  </a>
</p>

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=sqale_rating" alt="Maintainability Rating">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=software_quality_maintainability_issues" alt="Maintainability Issues">
  </a>
  &nbsp;
  <a href="https://sonarcloud.io/summary/new_code?id=montigabriele_Progetto-Ingegneria-del-Software">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=montigabriele_Progetto-Ingegneria-del-Software&metric=software_quality_security_issues" alt="Security Issues">
  </a>
</p>

---

FoodMood è un'applicazione sviluppata per il progetto di Ingegneria del Software e Progettazione Web, eseguibile sia in modalità **GUI** che in **CLI**.

L'idea è quella di assistere il cliente al ristorante nell'ordinazione, aiutandolo a scegliere i piatti in base ai suoi gusti, preferenze alimentari, budget, intolleranze, allergie e apporto calorico desiderato. Il sistema pone domande mirate all'utente e suggerisce i prodotti più adatti tra quelli disponibili nel menù del ristorante.

**Video YouTube:** https://youtu.be/Xwf9RPsDWK8 

---

## Avvio e configurazione iniziale (menu interattivo)

All'avvio, FoodMood guida l'utente tramite un menù testuale per selezionare:

1. **Interfaccia**
   - CLI → interfaccia a riga di comando
   - GUI → interfaccia grafica (JavaFX)

2. **Modalità di persistenza**
   - DEMO       → Nessun salvataggio, dati in memoria volatile
   - FILESYSTEM → Salvataggio su file CSV in `data/csv/`
   - FULL       → Salvataggio completo tramite database MySQL

## Modalità di esecuzione disponibili

Il progetto può essere avviato in **sei modalità**:

- `cli demo`
- `cli filesystem`
- `cli full`

- `gui demo`
- `gui filesystem`
- `gui full`

---

## Requisiti

- **Java JDK** 17 o superiore
- **Apache Maven** 3.6+
- **MySQL** 8.0+ *(solo per modalità FULL)*
- **JavaFX SDK** *(gestito automaticamente da Maven)*

---

## Esecuzione con Maven

### Avvio con menu interattivo

```bash
mvn compile exec:java -Dexec.mainClass="it.foodmood.Main"
```

oppure

```bash
mvn javafx:run
```

### Avvio diretto con parametri

#### Modalità GUI + Database completo

```bash
mvn clean compile exec:java -Dexec.args="gui full"
```

#### Modalità CLI + Database completo

```bash
mvn clean compile exec:java -Dexec.args="cli full"
```

#### Modalità GUI + Filesystem

```bash
mvn clean compile exec:java -Dexec.args="gui filesystem"
```

#### Altre combinazioni

```bash
mvn clean compile exec:java -Dexec.args="gui demo"
mvn clean compile exec:java -Dexec.args="cli demo"
mvn clean compile exec:java -Dexec.args="cli filesystem"
```

---

## Configurazione

### Selezione del ruolo utente

In `src/main/resources/application.properties` è possibile scegliere quale ruolo esegue il sistema:

```properties
# Gestione della modalità di esecuzione
user.mode = customer
# user.mode = waiter
# user.mode = manager
```

**Ruoli disponibili:**

- `customer` → Cliente del ristorante
- `waiter`   → Cameriere
- `manager`  → Titolare/gestore

### Configurazione Database (modalità FULL)

Modificare `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/foodmood
db.user=tuo_utente
db.password=tua_password
```

---

## Funzionalità per ruolo

### Cliente

#### GUI e CLI

- Login
- Accesso come ospite 
- Ordinazione tramite menù digitale classico
- Ordinazione tramite suggerimenti (raccomandazioni personalizzate basate su preferenze, allergie, budget, calorie)

### Cameriere

#### GUI (parziale) e CLI (non implementato)

- Visualizzazione sala
- Consultazione menù digitale
- *(Altri use case non ancora implementati)*

### Titolare

#### GUI e CLI 

- CRUD per la gestione di ingredienti con allergeni e macronutrienti
- CRUD per la gestione di piatti 
- Gestione sala: aggiunta e spostamento tavoli (implementata solo in GUI)

---

## Pulizia della compilazione

Per eliminare i file generati ed effettuare una compilazione pulita:

```bash
mvn clean
```

---

## Struttura dati

### Modalità DEMO

I dati rimangono in memoria volatile e vengono persi alla chiusura dell'applicazione.

### Modalità FILESYSTEM

I dati vengono salvati in file CSV nella cartella `data/csv/`:

- `ingredients.csv`
- `dishes.csv`
- `orders.csv`

### Modalità FULL

I dati vengono salvati in modo persistente nel database MySQL.

---