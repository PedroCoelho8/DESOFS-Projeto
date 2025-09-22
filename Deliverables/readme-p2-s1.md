# Project: Phase 2: Sprint 1

Domain:
- User: Pedro Alves
- Payment/PaymentDocument: Pedro Coelho
- BankAccount: Leonardo Gomes

Service + Controller + API:
- User: login -> Pedro Alves
- PaymentCreate -> Leonardo Gomes
- PaymentSend -> Pedro Coelho
- PaymentReceive -> Pedro Coelho

## Changes to be made

The API elements (UserAPI and UserController) should be depreciated, since our used programming language(Java 17) has 3 really useful annotations:

- **@Repository**: data access layer; enables better integration with Spring and JPA.
- **@Service**: business logic layer; simple middle-man of the other 2.
- **@RestController**: RESTful controller layer; used for API calls reception and control the input and output information.

These are a must-have because:

- **LOC(Lines Of Code)**: the easier a system is to read, the easier it is to maintain.
- **Layered architecture pattern**: even without a class named API, this follows a pattern already mentioned in the last sprint.

**TO DO**: change the architecture itself;

Also there a few more ways to also not have those surrounding catch blocks:

- **@ControllerAdvice**: class to go when an exception is thrown.
- **@ExceptionHandler**: method to run whenever a given Exception type is thrown.

## User

### Domain

The User Domain has all the information specified in the previous domain model:

- id (UUID): Unique identifier automatically generated for each user.

- name (Username): User's name, encapsulated in a value class (Username) which probably contains validations.

- email: User's email address, also encapsulated to ensure a valid format.

- password (Password): User's password, stored securely (probably encrypted).

- roles (List<Role>): List of user roles/roles, such as ADMIN, USER, GUEST, which control permissions on the system.

- phoneNumber: User's phone number, encapsulated for validation and formatting.

- nif (NIF): User's NIF, also encapsulated to ensure validity.

### Service

The User Service is currently composed by many methods to fulfill the specifications described in the previous development

- createUser: used for registration. Has to verify if there is already a user with the given email;
- updateUser: used for profile updating. Has to verify the existance of the user in the database;
- getUserByEmail: used for login. Has to verify the hashed password to the inserted password after hashing the last one.

### Factory

Following the Factory pattern, the User Factory is used for the User Creation.

Although it is quite simple, it leads to an easier maintenance further in development.

### Controller

The User Controller consists of the 2 needed parts for now:

- login: used to validate if a user is in the system. It also creates and gives as a response the user and its token for persistence and session management.
- register: used to create the user given a body.

### Repository

The Repository consists of all the elements defined in the service. It makes everything it does, but implements a SimpleJPARepository to facilitate the invocation of methods and be sure the user is either inserted, gotten or updated consistently.

---

## Payment

### Domain

The Payment entity represents the payment made between two bank accounts, linked to authenticated users, controlling the life cycle of a payment. The key elements of the Payment domain are:

- id (UUID): Unique identifier of the payment.

- paymentDocuments (Map<PaymentDocument, Double>): Map that associates payment documents with values; a payment can involve several documents.

- state (PaymentState): Payment state (e.g. PENDING, PAYED, etc.), which allows you to manage the payment flow.

- senderBankAccount (BankAccount): Bank account of the user sending the payment.

- receiverBankAccount (BankAccount): Bank account of the user receiving the payment.

- dtCreated, dtUpdated (LocalDateTime): Dates on which the payment was created and updated.

- createdBy, updatedBy (User): Reference to the user who created or updated the payment.

### Service

The Payment Service is responsible for managing the lifecycle of a payment between registered bank accounts, based on user permissions and status validations. The main operations include:

- create: used to create a payment between two accounts. Checks the existence of the accounts involved and the authenticated user;
- send: used to confirm that the sender has sent the payment. Checks that the payment is in PENDING status and that the authenticated user is the owner of the sending account;
- receive: used to confirm that the recipient has received the payment. Checks that the payment is in PAYED status and that the authenticated user is the owner of the recipient account.

### Factory

PaymentFactory is a utility class responsible for creating the Payment object from data transferred via PaymentDTO. It centralizes the Payment's construction logic, including the association of payment documents (PaymentDocument), the sender and recipient bank accounts, the payment's initial status and the user who created the payment. Encapsulating this process in the factory promotes a more modular design and a clear separation of responsibilities, making it easier to maintain and reuse the code.

### Controller

The Payment Controller consists of the 3 needed parts for now:

- createPayment: used to create a payment document given a body;
- sendPayment: used to send a payment document to a receiver, given a body;
- receivePayment: used to receive a payment document, given a body.

### Repository

PaymentRepository is the layer responsible for persisting and retrieving Payment objects in the database. It extends the standard JpaRepository interface for CRUD operations and implements custom methods for querying payments by filtering by state, by the user who created the sender and recipient bank accounts, and by the user who created the payment. These methods make it easier to find the relevant payments for different user contexts, ensuring efficient management of payment data.

---

## Bank Account

### Domain

The BankAccount entity represents a bank account registered in the system, associated with a user. The main elements are:

- id (Integer): Unique identifier of the bank account.

- iban (IBAN): IBAN code of the account, encapsulated in a Value Object to ensure validity.

- swiftBic (SwiftBic): SWIFT/BIC code, another Value Object.

- nifUser (TIN): TIN of the user who owns the account (Value Object with validation).

- accountType (AccountType): Type of bank account (current account, savings account, etc.), defined by enum.

- createdBy, updatedBy (User): User who created or updated the account.

- dtCreated, dtUpdated (LocalDateTime): Date of creation and update.

### Service

The Bank Account Service is responsible for managing the life cycle of a bank account, ensuring the creation, updating, consultation and removal of accounts, as well as associating them with registered users. The main operations include:

- create: used to register a new bank account associated with the authenticated user, validating the data received;
- findAll: lists all the bank accounts registered in the system;
- findById: queries a specific bank account by its identifier, ensuring that the account exists;
- update: allows the data of an existing bank account to be updated, verifying the existence of the account and the user making the change;
- delete: deletes a bank account from the system, after validating its existence.

### Factory

Factory is a class that is responsible for creating instances of the BankAccount entity from external data, in this case from a DTO (BankAccountDTO), which usually comes from the presentation layer or API.

This separates the entity creation logic from the rest of the code, centralizing it at a single point, which is good for keeping the code clean, reusable and easy to maintain.

### Controller

This controller is responsible for exposing REST endpoints to manipulate BankAccount resources through basic CRUD operations:

- Create account (POST /bankaccounts)

- List all (GET /bankaccounts)

- Query by id (GET /bankaccounts/{id})

- Update (PUT /bankaccounts/{id})

- Delete (DELETE /bankaccounts/{id})

External communication is done using DTOs (BankAccountDTO) and conversions between entity and DTO are done by BankAccountMapper. The business logic is encapsulated in IBankAccountService.

### Repository

IBankAccountRepository is the interface responsible for interacting with the database for operations related to the BankAccount entity. It extends the JpaRepository, which already has a standard set of functionalities for creating, reading, updating and deleting (CRUD) database records simply and automatically.

In addition to these basic operations, the repository defines specific methods for searching for bank accounts based on more specific properties, such as the IBAN value, the associated user's TIN, and also for checking whether accounts with certain data already exist

In addition to the naming convention-based methods, there is a custom query to return only the IBANs of the accounts created by a given user, which can be useful for more optimized queries.

In short, the repository acts as an abstraction layer that facilitates access to and manipulation of BankAccount data in the database, while keeping the rest of the application decoupled from the data access implementation.

---

## Security considerations

There are many factors to be considered and that are well-defined in the developed ASVS

- token generation: the session's token lasts for one hour and enables the user who requests with the token to make more requests. If the request is made and there was a frontend, the session token should be persisted throughout the remaining pages.

- bcrypt usage: when developing logins and registrations, an encriptation system must be developed. With this in mind, the library bcrypt was used to encrypt and comparison of passwords for login.

- role dependency: the roles are defined to limit the usage of certain APIs, considering that for example a sender entity cannot confirm the reception of a payment.
