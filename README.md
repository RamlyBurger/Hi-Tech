# Hi-Tech Commerce

<p align="center">
  <img src="src/main/resources/static/icon/logo2-highlighted.png" alt="Hi-Tech Company logo" width="280">
</p>

Hi-Tech Commerce is a Java MVC ecommerce system developed as the course assignment for AACS1483 Web Design and Development. It provides database-backed products, authentication, customer cart and checkout flows, order history, and admin management screens.

## Course Details

| Detail | Value |
| --- | --- |
| Course | AACS1483 Web Design and Development |
| Programme | DFT Y1S1 |
| Tutorial Class | Group 1 |
| School | TAR UMT |

## Stack

- Java 17
- Spring Boot 2.7.18
- Spring MVC and Thymeleaf
- Spring Security with BCrypt password storage
- Spring Data JPA and Hibernate
- Flyway database migrations
- PostgreSQL for Docker/local app runtime
- H2 for automated tests
- JUnit 5, MockMvc, Spring Security Test
- Docker Compose

## Features

- Public storefront pages for home, about, FAQ, feedback, events, membership, products, categories, and product details.
- Dynamic product catalog with 16 seeded products.
- Legacy route compatibility for pages such as `home.html`, `products.html`, `proDesktop.html`, and `singleprod1.html`.
- Customer registration and login.
- Role-based access for `CUSTOMER` and `ADMIN`.
- Session-based cart with quantity update and item removal.
- Simulated checkout that creates orders and decrements stock.
- Customer order history.
- Admin dashboard and CRUD-style management for products, stock, events, and promotions.
- Flyway schema and seed data for categories, products, users, events, and promotions.

## Default Accounts

| Role | Username | Password |
| --- | --- | --- |
| Customer | `customer` | `password` |
| Admin | `admin` | `password` |

## Run Locally With Maven

Start PostgreSQL first. The provided Docker Compose file can run both PostgreSQL and the app, but if you only want the database:

```powershell
docker compose up -d postgres
```

Run the app:

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

```text
http://localhost:8080
```

The app uses these defaults unless environment variables override them:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/hitech
SPRING_DATASOURCE_USERNAME=hitech
SPRING_DATASOURCE_PASSWORD=hitech
SERVER_PORT=8080
```

## Run With Docker Compose

Build and run the full stack:

```powershell
docker compose up --build
```

Then open:

```text
http://localhost:8080
```

Stop the stack:

```powershell
docker compose down
```

Remove the PostgreSQL volume if you want to reset seeded data:

```powershell
docker compose down -v
```

## Tests

Run the full test suite:

```powershell
.\mvnw.cmd test
```

The tests use the `test` profile and H2. Current coverage includes:

- Product seed/search repository behavior.
- Public storefront visibility.
- Login with seeded users.
- Admin route authorization.
- Customer cart to checkout to order-history flow.

## Main Routes

| Area | Routes |
| --- | --- |
| Storefront | `/`, `/products`, `/products/{category}`, `/products/item/{slug}` |
| Customer | `/cart`, `/checkout`, `/orders` |
| Auth | `/login`, `/register`, `/logout` |
| Admin | `/admin`, `/admin/products`, `/admin/events`, `/admin/promotions` |
| Legacy | `/home.html`, `/products.html`, `/proDesktop.html`, `/singleprod1.html` to `/singleprod16.html` |

## Project Structure

```text
Hi-Tech/
|-- pom.xml
|-- mvnw / mvnw.cmd
|-- Dockerfile
|-- docker-compose.yml
|-- src/main/java/com/hitech/commerce/
|   |-- cart/          # Session cart
|   |-- config/        # Spring Security config
|   |-- domain/        # JPA entities and enums
|   |-- repository/    # Spring Data repositories
|   |-- security/      # UserDetailsService
|   |-- service/       # Catalog, account, cart, order, admin services
|   `-- web/           # MVC controllers and form models
|-- src/main/resources/
|   |-- db/migration/  # Flyway schema and seed data
|   |-- static/        # CSS, JS, images, icons, fonts, video
|   `-- templates/     # Thymeleaf views
`-- src/test/java/     # Repository, security, and MVC flow tests
```

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).

## Notes

This project is the course assignment ecommerce prototype for AACS1483 Web Design and Development. Checkout uses simulated payment only; no real payment gateway is connected.
