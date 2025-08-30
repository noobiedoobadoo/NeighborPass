# NeighborPass


## Key Features
- **Visitor Entry Logging:** Admins log visitor details, entry/exit times, and status (waiting, entered, left, denied).

- **Real-Time Resident Approval:** Sends approval requests to residents via asynchronous notifications; residents can approve or deny entries.

- **Visitor History Management:** Users view categorized histories (current, past, denied); admins access any visitor's logs.

- **Phone Verification:** OTP-based verification using Twilio for secure registration.

- **Role-Based Access:** JWT-secured authentication with roles for residents, admins, and security personnel.

- **Compliance Checks:** Flags non-compliant visitors (e.g., missing exits) using boolean flags and consistency checks.

- **Asynchronous Processing**: Fault-tolerant messaging for logs and approvals.

- **Optional Features:** Cooldown periods for requests, pre-approvals, and automatic status updates (e.g., mark as past after 24 hours).


## Rough HLD
<img width="852" height="642" alt="DiagramFIN" src="https://github.com/user-attachments/assets/a9f1108d-9588-4a68-91f3-c9f068612373" />

## Class Mapping
<img width="636" height="605" alt="ClassMappingFIN" src="https://github.com/user-attachments/assets/51c3ef89-09d0-466b-b686-47f97d2f7c27" /> <br>
Entry - User & Entry-Visitor mappings with @OnDelete(set null) cascades, for Persistent logging

## Tech Stack
- **Core:** Java, Spring Boot (Microservices Architecture)

- **Database:** MySQL (for persistent storage and audit logs), Redis (for fast retrieval)

- **Messaging:** RabbitMQ (for real-time, asynchronous inter-service communication)

- **Security:** JWT (JSON Web Tokens) for authentication and authorization

- **External Services:** Twilio (for OTP generation and SMS notifications)

- **Deployment:** Docker Compose (for containerization and easy setup)

- **Other:** Feign Client (for service communication), API Gateway (for routing and filters)
  
## Quickstart with Docker
Download the required files
- [docker-compose.yml](https://raw.githubusercontent.com/noobiedoobadoo/NeighborPass/main/run-on-docker/docker-compose.yml)
- [init-db.sql](https://raw.githubusercontent.com/noobiedoobadoo/NeighborPass/main/run-on-docker/init-db.sql)

Place them in a folder and run:
```bash
docker-compose -f docker-compose.yml up
```

Access the API at **localhost:8085**.

Stop the application with:
```bash
docker compose down -v
```

## Core Workflows
- **Admin Logs Visitor**: POST to /admin with visitor details (contact, name, _purpose_).

- **Resident Approval:** System sends async request; resident responds via /entries/{id} .

- **View History:** GET /entries (user-specific or admin-view all).

- **Authentication:** POST /auth/login with credentials; returns JWT token for authorized requests.

All endpoints are secured and routed through the API Gateway.

## Challenges Overcome
- During development, I tackled several technical hurdles to ensure robustness:

- Implemented @OnDelete annotations and correct entity mappings for persistent logs (e.g., **AVIODING** cascading deletes between users and visitors).

- Handled visitor creation flow: First validate contact, then prompt for name if new (considered async implementation for better UX).

- Configured RabbitMQ for durable/non-durable queues, learning about exchanges, routing keys, and queue recreation for fault tolerance.

- Used boolean flags to identify non-compliant visitors (e.g., missing exits/invalid entry) while managing consistency across delete/create operations.

- Created separate authentication service with admin login and OTP workarounds (e.g., caller notifications not feasible due to API limits).

- Resolved consuming from the same queue twice by creating two queues with identical routing keys.

- Added exception handling and double-checks for user deletion scenarios.

- Mitigated stale JWT risks double checking in gatelog microservice (user deletion).

These challenges enhanced my understanding of microservices design, async systems, and secure data management.

## Functional Requirements
- Admins log visitors with entry/exit times and associate them with residents.

- Visitors have statuses: waiting, entered, left, denied.

- Real-time approval requests sent to residents for approval/denial.

- If approved, log entry; otherwise, deny access.

- Users view categorized visitor history (current/past/denied).

- Admins access any visitor's history.

- [Optional] Auto-mark as past if no exit after 24 hours.

- Assumptions: All entries/exits are logged accurately; visitors provide valid contacts.

- [Optional] Prevent duplicate entries with same contact; flag inconsistencies on entry/exit.

## Non-Functional Requirements
- Asynchronous, responsive logging of entries, exits, approvals, and denials.

- Scalable design to accommodate future features (e.g., modular microservices).

- Fault-tolerant communication to handle service failures.

## Future Scope
- Support multiple dependent users per household.

- Real-time chat between residents and admins regarding visitors.

- Pre-approval system based on visitor contact numbers.

- Cooldown periods for repeated requests.

- Advanced analytics for entry patterns.

## Development Notes (Original TODO List)
- Implement boolean flag for compliance checks (DONE).

- Configure consumer queues with PostConstruct initialization.

- Make approve/deny actions async (OPTIONAL).

- Centralize development in one IDE.

- Add API Gateway with Eureka for service discovery.

- Secure all endpoints with role-based authorization.

- Remove passwords/roles from downstream services.

- Minimize HTTP calls between services.

- Implement user deletion (admin).

- Add filters for entries by house/contact/date.

- Validate admin endpoints.

- Implement boolean flag features.

- Dockerize the application.

- Deploy with durable queues.

- Resolve Feign client localhost issues.

- Fix dual consumer setup.

- Resolve approve/deny bugs and add exception handling.

- Handle entries when admin/user is deleted.



