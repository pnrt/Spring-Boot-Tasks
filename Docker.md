# Spring Boot with Docker and Kubernetes (Kotlin + Gradle)

## **1. Setting Up a Spring Boot Application with Gradle**

### **Step 1: Create a Spring Boot Project**

- Go to [Spring Initializr](https://start.spring.io/)
- Select the following options:
    - **Project:** Gradle
    - **Language:** Kotlin
    - **Spring Boot Version:** Latest stable version
    - **Dependencies:** Spring Web (for building REST APIs)
- Click **Generate** and download the project.
- Extract the project and open it in an IDE (IntelliJ IDEA recommended).

### **Step 2: Create a Simple Controller (Optional for Testing)**

Inside `src/main/kotlin/com/example/demo/`, create a new file `HelloController.kt`:

```kotlin
package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloController {
    @GetMapping("/hello")
    fun sayHello(): String {
        return "Hello, World!"
    }
}
```

Run the application:

```sh
gradlew bootRun
```

Then visit `http://localhost:8080/api/hello` to check if it works.

---

## **2. Containerizing with Docker**

### **Step 1: Install Docker**

If you haven’t installed Docker:

- Download and install [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/)
- Enable **WSL 2 backend** for performance improvements
- Restart your system if required

### **Step 2: Create a `Dockerfile`**

In the root of your project, create a file named `Dockerfile` and add:

```dockerfile
# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle build output (JAR file) into the container
COPY build/libs/*.jar app.jar

# Expose the port used by the Spring Boot app
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Step 3: Build the Docker Image**

First, build the project JAR file:

```sh
gradlew clean build
```

Then, build the Docker image:

```sh
docker build -t my-spring-boot-app .
```

### **Step 4: Run the Docker Container**

```sh
docker run -p 8080:8080 my-spring-boot-app
```

Check if the application is running at `http://localhost:8080/api/hello`.

### **Step 5: Push the Image to Docker Hub (For Deployment Later)**

```sh
docker login
```

Tag and push the image:

```sh
docker tag my-spring-boot-app your-dockerhub-username/my-spring-boot-app

docker push your-dockerhub-username/my-spring-boot-app
```

---

## **3. Deploying to Kubernetes**

### **Step 1: Install Kubernetes Tools**

- Install [kubectl](https://kubernetes.io/docs/tasks/tools/)
- Install [Minikube](https://minikube.sigs.k8s.io/docs/start/) for local testing
- Start Minikube:
    
    ```sh
    minikube start
    ```
    

### **Step 2: Create a Kubernetes Deployment File (`deployment.yaml`)**

Create a new file named `deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-boot-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: spring-boot-app
  template:
    metadata:
      labels:
        app: spring-boot-app
    spec:
      containers:
        - name: spring-boot-app
          image: your-dockerhub-username/my-spring-boot-app
          ports:
            - containerPort: 8080
```

### **Step 3: Create a Kubernetes Service (`service.yaml`)**

Create another file named `service.yaml`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: spring-boot-service
spec:
  selector:
    app: spring-boot-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

### **Step 4: Apply Deployment & Service**

```sh
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

### **Step 5: Verify the Deployment**

```sh
kubectl get pods
kubectl get services
```

### **Step 6: Access the Application**

- If using Minikube:
    
    ```sh
    minikube service spring-boot-service
    ```
    
- If on a cloud provider, get the external IP of the LoadBalancer and access it in the browser.

---

## **4. Future Deployment on Ubuntu Linux**

### **Step 1: Install Required Software on Ubuntu**

```sh
sudo apt update && sudo apt install -y docker.io kubectl
```

### **Step 2: Run Docker Container on Ubuntu**

```sh
docker run -d -p 8080:8080 your-dockerhub-username/my-spring-boot-app
```

### **Step 3: Deploy Kubernetes on Ubuntu**

If using **MicroK8s** (lightweight Kubernetes for Ubuntu):

```sh
sudo snap install microk8s --classic
microk8s start
microk8s kubectl apply -f deployment.yaml
microk8s kubectl apply -f service.yaml
```

Check status:

```sh
microk8s kubectl get all
```

### **Step 4: Expose Service in Ubuntu**

```sh
kubectl port-forward svc/spring-boot-service 8080:80
```

Access via `http://localhost:8080/api/hello`

---

## **Summary**

✅ Created a **Spring Boot application** with Gradle and Kotlin  
✅ Built and ran a **Docker container** on Windows  
✅ Deployed the container to **Kubernetes (Minikube)** for testing  
✅ Prepared for **future deployment on Ubuntu Linux**

Let me know if you need any clarifications! 🚀