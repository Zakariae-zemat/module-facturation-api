# Module de Facturation – Java / Spring Boot

Mini-API REST de facturation pour une plateforme de gestion de projets. Permet la gestion des clients, la création de factures avec calculs automatiques, et l'export JSON. Réalisé dans le cadre d'un test technique.

---

## 🚀 Fonctionnalités

* ✅ Gestion des clients (CRUD)
* ✅ Création de factures avec lignes (description, quantite, prix HT, TVA)
* ✅ Calcul automatique des totaux : HT / TVA / TTC
* ✅ Export JSON complet d'une facture
* ✅ Recherche de factures par client ou date
* ✅ Authentification HTTP Basic (Spring Security)
* ✅ Documentation Swagger interactive
* ✅ Tests unitaires des services métiers

---

## 🛠 Technologies

* Java 17
* Spring Boot 3.5.0
* Spring Data JPA (Hibernate)
* Base de données H2 en mémoire
* Spring Security (auth en mémoire)
* Swagger (Springdoc OpenAPI)
* Maven
* JUnit 5 + Mockito

---

## ▶️ Lancer le projet

### 1. Cloner le dépôt

```bash
git clone https://github.com/TON_USER/module-facturation-api.git
cd module-facturation-api
```

### 2. Lancer avec Maven

```bash
./mvnw spring-boot:run
```

> 💡 Sous Windows : `mvnw.cmd spring-boot:run`

### 3. Accéder aux outils

* API : [http://localhost:8080](http://localhost:8080)
* Swagger UI : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* Console H2 : [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

  * JDBC : `jdbc:h2:mem:billingdb`
  * Utilisateur : `sa`
  * Mot de passe : *(laisser vide)*

---

## 🔐 Authentification

Tous les endpoints sont protégés par HTTP Basic.

| Utilisateur | Mot de passe |
| ----------- | ------------ |
| `admin`     | `password`   |

---

## 📍 Principaux Endpoints REST

### 👤 Clients

| Méthode | Endpoint        | Description         |
| ------- | --------------- | ------------------- |
| GET     | `/clients`      | Liste des clients   |
| POST    | `/clients`      | Créer un client     |
| GET     | `/clients/{id}` | Détails d'un client |

### 📄 Factures

| Méthode | Endpoint                | Description                     |
| ------- | ----------------------- | ------------------------------- |
| GET     | `/factures`             | Liste des factures              |
| POST    | `/factures`             | Créer une facture               |
| GET     | `/factures/{id}`        | Voir une facture                |
| GET     | `/factures/{id}/export` | Export JSON complet             |
| GET     | `/factures/search`      | Rechercher par clientId ou date |

---

## 🔬 Exemple JSON : Création de facture

```json
{
  "client": { "id": 1 },
  "lignes": [
    {
      "description": "Service A",
      "quantite": 2,
      "prixUnitaireHT": 100.0,
      "tauxTVA": "TVA_20"
    },
    {
      "description": "Service B",
      "quantite": 1,
      "prixUnitaireHT": 200.0,
      "tauxTVA": "TVA_10"
    }
  ]
}
```

---

## 🔮 Tests unitaires

Lancer les tests avec :

```bash
./mvnw test
```

Tests fournis :

* `FactureServiceTest`
* `ClientServiceTest`

---

## 📁 Structure du projet

```
src/main/java/com/example/billing/
├── controller     # Web API controllers
├── service        # Business logic
├── repository     # JPA interfaces
├── entities          # Entities (Client, Facture...)
├── dto            # Export JSON DTOs
├── config         # Spring Security config
```

---

## ⏳ Temps estimé : 4 heures

Code propre, testé, documenté, réalisé sans assistance chatGPT ou un outil AI.

---

## 📅 Réalisé par: 

Conçu et réalisé par **Zemat** pour une mission backend Spring Boot.

---

Merci pour votre lecture et bon test ! ✨
