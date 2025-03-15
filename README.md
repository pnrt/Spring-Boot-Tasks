[Docker Image](https://hub.docker.com/r/pnrt/tasks-spring-boot-app)
### **Spring Boot with Kotlin - Microservices & CRUD Operations**

This guide covers the basics of microservices in **Spring Boot with Kotlin**, including **CRUD operations** with **PostgreSQL**, **CSV files**, and **normal text files**.

---

## **1. Setting Up Spring Boot with Kotlin for Microservices**

### **Step 1: Create a Spring Boot Kotlin Project**

Use [Spring Initializr](https://start.spring.io/) to generate a project with the following dependencies:  
✅ **Spring Web** (For REST APIs)  
✅ **Spring Boot Actuator** (For monitoring)  
✅ **Spring Data JPA** (For database interaction)  
✅ **PostgreSQL Driver** (For PostgreSQL support)  
✅ **Flyway Migration** (For database migrations)

---

### **Dependencies for Spring Boot Kotlin Microservices (CRUD with PostgreSQL, CSV, and Text Files)**

You need the following dependencies in your `build.gradle.kts` (Kotlin DSL) file to work with **Spring Boot Microservices**, **PostgreSQL**, **CSV processing**, and **file handling**.

---

### **📌 Essential Dependencies**

```kotlin
dependencies {
    // Spring Boot Web for REST API (GET, POST, PUT, DELETE)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Boot Data JPA for Database Access
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // PostgreSQL Driver
    implementation("org.postgresql:postgresql")

    // Jackson for JSON Processing
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin Reflection and Stdlib
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Lombok (Optional: For Reducing Boilerplate Code)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // CSV Processing Library (Apache Commons CSV)
    implementation("org.apache.commons:commons-csv:1.10.0")

    // Spring Boot DevTools (Optional: For Live Reload)
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

---

### **🔧 Explanation of Dependencies**

|Dependency|Purpose|
|---|---|
|`spring-boot-starter-web`|Enables building REST APIs (GET, POST, PUT, DELETE).|
|`spring-boot-starter-data-jpa`|Provides ORM (Object-Relational Mapping) for database operations.|
|`postgresql`|PostgreSQL JDBC driver for database connectivity.|
|`jackson-module-kotlin`|Enables JSON serialization and deserialization for Kotlin.|
|`kotlin-reflect` & `kotlin-stdlib-jdk8`|Kotlin-specific language features and reflection support.|
|`commons-csv`|Library for reading and writing CSV files.|
|`spring-boot-devtools`|Helps with automatic reloads during development.|
|`spring-boot-starter-test`|Provides JUnit and other testing tools for Spring Boot.|

---

### **📌 How to Add These Dependencies?**

1️⃣ Open your `build.gradle.kts` file.  
2️⃣ Add the dependencies under `dependencies {}`.  
3️⃣ Sync your project using:

```bash
./gradlew build
```

4️⃣ Run your Spring Boot app:

```bash
./gradlew bootRun
```

---

## **2. Defining a Microservice Structure**

A microservice typically contains:

- **Controller** (Handles API requests)
- **Service** (Business logic)
- **Repository** (Database interaction)
- **Entity** (Data model)

---

## **3. CRUD Operations with PostgreSQL**

### **Step 1: Configure PostgreSQL**

Add the database configuration in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: myuser
    password: mypassword
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
     
```
---

### **📌 Default Location:**

```bash
src
 ├── main
 │   ├── kotlin (or java)
 │   ├── resources
 │   │   ├── application.yml   👈 (Configuration File)
 │   │   ├── application.properties (Alternative)
 │   │   ├── static/
 │   │   ├── templates/
 │   │   ├── messages/
 │   │   ├── logback.xml
```

If it is missing, you can **create a new file** named **`application.yml`** inside the `resources` folder.

---

### **How to Use `application.yml`?**

#### **Example: PostgreSQL Database Configuration**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydatabase
    username: myuser
    password: mypassword
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

---

### **Alternative: `application.properties`**

If you're using **`application.properties`** instead of **YAML**, the same config would look like:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---
### **🛠️ Need More Help?**

Let me know if you need help **modifying, loading, or structuring** your `application.yml` file! 🚀
### **Step 2: Create an Entity**

Define a data model using **JPA**:

```kotlin
import jakarta.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    val description: String? = null
)
```

---

### **Step 3: Create a Repository**

Use **Spring Data JPA** to interact with the database:

```kotlin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long>
```

---

### **Step 4: Implement the Service Layer**

Service to handle business logic:

```kotlin
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> = taskRepository.findAll()

    fun getTaskById(id: Long): Task? = taskRepository.findById(id).orElse(null)

    fun createTask(task: Task): Task = taskRepository.save(task)

    fun updateTask(id: Long, updatedTask: Task): Task? {
        return if (taskRepository.existsById(id)) {
            taskRepository.save(updatedTask.copy(id = id))
        } else {
            null
        }
    }

    fun deleteTask(id: Long) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        }
    }
}
```

---

### **Step 5: Create the Controller**

Expose REST endpoints using `@RestController`:

```kotlin
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks() = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long) = taskService.getTaskById(id)

    @PostMapping
    fun createTask(@RequestBody task: Task) = taskService.createTask(task)

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody updatedTask: Task) =
        taskService.updateTask(id, updatedTask)

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long) = taskService.deleteTask(id)
}
```

---

## **4. Working with CSV Files in Spring Boot**

CSV files can be used for data import/export.

### **Step 1: Add Dependencies**

Include the **Apache Commons CSV** dependency in `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.apache.commons:commons-csv:1.10.0")
}
```

---

### **Step 2: Create a CSV Service to Read and Write Files**

```kotlin
import org.apache.commons.csv.*
import org.springframework.stereotype.Service
import java.io.*

@Service
class CsvService {

    fun readCsv(filePath: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val reader = FileReader(filePath)
        val records = CSVFormat.DEFAULT.withHeader("id", "title", "description")
            .withFirstRecordAsHeader().parse(reader)

        for (record in records) {
            tasks.add(Task(
                id = record["id"].toLong(),
                title = record["title"],
                description = record["description"]
            ))
        }
        reader.close()
        return tasks
    }

    fun writeCsv(tasks: List<Task>, filePath: String) {
        val writer = FileWriter(filePath)
        val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "title", "description"))

        tasks.forEach { task ->
            csvPrinter.printRecord(task.id, task.title, task.description)
        }
        csvPrinter.flush()
        csvPrinter.close()
    }
}
```

---

## **5. Working with Normal Text Files**

We can store data in plain text files.

### **Step 1: Create a Service to Read and Write Text Files**

```kotlin
import org.springframework.stereotype.Service
import java.io.File

@Service
class TextFileService {

    fun readTextFile(filePath: String): List<String> {
        return File(filePath).readLines()
    }

    fun writeTextFile(filePath: String, content: List<String>) {
        File(filePath).writeText(content.joinToString("\n"))
    }
}
```

---

## **6. Running the Application**

Start the Spring Boot application using:

```bash
./gradlew bootRun
```

Or, if using Maven:

```bash
./mvnw spring-boot:run
```

---

## **7. Testing API Endpoints with cURL**

### **Get All Tasks**

```bash
curl -X GET http://localhost:8080/tasks
```

### **Get Task by ID**

```bash
curl -X GET http://localhost:8080/tasks/1
```

### **Create a New Task**

```bash
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d '{"title":"New Task","description":"Task details"}'
```

### **Update a Task**

```bash
curl -X PUT http://localhost:8080/tasks/1 -H "Content-Type: application/json" -d '{"title":"Updated Task","description":"Updated details"}'
```

### **Delete a Task**

```bash
curl -X DELETE http://localhost:8080/tasks/1
```

---

## **8. Summary**

✅ **Microservices Setup** - Used Spring Boot & Kotlin  
✅ **CRUD Operations** - Implemented with PostgreSQL  
✅ **CSV Handling** - Read and write tasks from CSV files  
✅ **Text File Handling** - Read and write simple text files  
✅ **API Testing** - Used cURL commands

---

## **✅ JPA Native Query Approach (Easier than JDBC)**

If you still want to write **manual SQL queries**, but with JPA, you can use **@Query (Native Query)**.

### **1️⃣ Modify Your Repository**

```kotlin
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : CrudRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks", nativeQuery = true)
    fun findAllTasks(): List<Task>
}
```

💡 **Why use `nativeQuery = true`?**  
Because it lets you write **raw SQL** while still using JPA.

---

### **2️⃣ Convert Query Result to JSON & Save**

```kotlin
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.File

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun exportTasksToJson(): File {
        // 1️⃣ Query database using manual SQL (but in JPA)
        val tasks = taskRepository.findAllTasks()

        // 2️⃣ Convert result to JSON
        val json = ObjectMapper().writeValueAsString(tasks)

        // 3️⃣ Save JSON to a file
        val file = File("tasks.json")
        file.writeText(json)

        return file
    }
}
```

---

### **3️⃣ Controller to Serve JSON File**

```kotlin
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping("/export")
    fun exportTasks(): ResponseEntity<FileSystemResource> {
        val file = taskService.exportTasksToJson()

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.json")

        return ResponseEntity(FileSystemResource(file), headers, HttpStatus.OK)
    }
}
```

---

## **🔥 Why This is Better than JDBC**

✅ **Less Code** – No need for `RowMapper` or `JdbcTemplate`.  
✅ **Database Independence** – Works with any DB supported by JPA.  
✅ **Easier Transaction Handling** – JPA handles entity states automatically.  
✅ **Still Uses Raw SQL** – So you have **manual query control**.

---

### **📝 Example API Call**

```http
GET http://localhost:8080/tasks/export
```

📥 The **downloaded `tasks.json` file** will contain:

```json
[
    {
        "id": 1,
        "title": "Complete project",
        "completed": false
    },
    {
        "id": 2,
        "title": "Fix bugs",
        "completed": true
    }
]
```

---


## **✅ Using `if` Conditions in Query Parameters**

Modify your controller to check **custom conditions** before responding.

```kotlin
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController {

    @GetMapping
    fun getTaskById(
        @RequestParam id: Long?,
        @RequestParam auth: String?
    ): ResponseEntity<Map<String, Any>> {
        
        // 🚨 Condition: If `auth` is missing, return error response
        if (auth.isNullOrBlank()) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "Auth token is required!")
            )
        }

        // 🚨 Condition: If `id` is missing, return another response
        if (id == null) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "Task ID is required!")
            )
        }

        // 🚨 Condition: If `auth` is incorrect, deny access
        if (auth != "ABCD1234") {
            return ResponseEntity.status(403).body(
                mapOf("error" to "Invalid authentication token!")
            )
        }

        // ✅ If everything is okay, return a success JSON response
        val response = mapOf(
            "status" to "success",
            "taskId" to id,
            "message" to "Task details found!"
        )

        return ResponseEntity.ok(response)
    }
}
```

---

## **📝 How This Works?**

### **Example Requests & Responses**

#### **1️⃣ Missing `auth` Parameter**

```http
GET http://localhost:8080/tasks?id=1
```

🔹 **Response (400 Bad Request)**

```json
{
    "error": "Auth token is required!"
}
```

---

#### **2️⃣ Missing `id` Parameter**

```http
GET http://localhost:8080/tasks?auth=ABCD1234
```

🔹 **Response (400 Bad Request)**

```json
{
    "error": "Task ID is required!"
}
```

---

#### **3️⃣ Invalid `auth` Value**

```http
GET http://localhost:8080/tasks?id=1&auth=WRONGTOKEN
```

🔹 **Response (403 Forbidden)**

```json
{
    "error": "Invalid authentication token!"
}
```

---

#### **4️⃣ Valid Request**

```http
GET http://localhost:8080/tasks?id=1&auth=ABCD1234
```

🔹 **Response (200 OK)**

```json
{
    "status": "success",
    "taskId": 1,
    "message": "Task details found!"
}
```

---

## **🚀 Customizing JSON Responses**

If you want **nested JSON structures** or dynamic responses:

```kotlin
val response = mapOf(
    "status" to "success",
    "task" to mapOf(
        "id" to id,
        "title" to "Complete Kotlin Project",
        "dueDate" to "2025-03-15"
    )
)
```

🔹 **Example Response**

```json
{
    "status": "success",
    "task": {
        "id": 1,
        "title": "Complete Kotlin Project",
        "dueDate": "2025-03-15"
    }
}
```

---



```plaintext
?id=1&Auth=ABCD
```



---

## **✅ Handling Query Parameters in Spring Boot Kotlin**

In your **Controller**, you can extract individual query parameters like this:

```kotlin
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController {

    @GetMapping
    fun getTaskById(
        @RequestParam id: Long,         // Extracting "id" from URL
        @RequestParam auth: String      // Extracting "Auth" from URL
    ): String {
        return "Task ID: $id, Auth: $auth"
    }
}
```

### **Example API Call**

```plaintext
GET http://localhost:8080/tasks?id=1&Auth=ABCD
```

🔹 **Response:**

```json
"Task ID: 1, Auth: ABCD"
```

---

## **✅ Handling Optional Query Parameters**

If some parameters are **optional**, use `required = false` and a default value:

```kotlin
@GetMapping
fun getTaskById(
    @RequestParam(required = false, defaultValue = "0") id: Long,
    @RequestParam(required = false, defaultValue = "NoAuth") auth: String
): String {
    return "Task ID: $id, Auth: $auth"
}
```

Now, if `id` or `Auth` is missing, it will use default values.

---

## **✅ Using Query Parameters with a Repository (Database)**

If you're retrieving data from PostgreSQL using Spring Data JPA, modify your service layer:

### **Task Entity**

```kotlin
import jakarta.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    val description: String
)
```

---

### **Task Repository**

```kotlin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findById(id: Long): Task?
}
```

---

### **Task Controller with Database Query**

```kotlin
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskRepository: TaskRepository) {

    @GetMapping
    fun getTaskById(
        @RequestParam id: Long,
        @RequestParam auth: String
    ): Task? {
        println("Auth received: $auth") // For debugging
        return taskRepository.findById(id)
    }
}
```

Now, calling:

```plaintext
GET http://localhost:8080/tasks?id=1&Auth=ABCD
```

🔹 **Response (from Database):**

```json
{
    "id": 1,
    "title": "Sample Task",
    "description": "This is a test task."
}
```

---

## **🚀 Need More Help?**

Let me know if you want to extend it to **CSV file handling, text files, or authentication!** 🎯
