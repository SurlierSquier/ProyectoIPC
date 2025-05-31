# 🧭 Proyecto de Carta de Navegación - IPC

<div align="center">
  <img src="images/Menu.png" alt="Logo" width="200"/>
  
  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
  [![JavaFX](https://img.shields.io/badge/JavaFX-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
  [![SceneBuilder](https://img.shields.io/badge/SceneBuilder-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://gluonhq.com/products/scene-builder/)
  [![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
  [![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)](https://netbeans.apache.org/)
</div>

## 📝 Descripción del Proyecto

Este proyecto fue desarrollado para la asignatura de Interfaces Persona Computador (IPC) y consiste en una aplicación de escritorio para la realización y gestión de problemas de carta de navegación. La aplicación está desarrollada utilizando Java con JavaFX y SceneBuilder, siguiendo los principios de diseño de interfaces de usuario.

## ✨ Características Principales

- 🔐 Sistema de autenticación de usuarios (login y registro)
- 📊 Interfaz intuitiva para la resolución de problemas de navegación
- 📈 Visualización de resultados y estadísticas
- 📚 Gestión de problemas y ejercicios
- 🎯 Menú de navegación principal

## 🏗️ Estructura del Proyecto

```
├── src/
│   ├── app/
│   │   ├── Main.java
│   │   ├── controllers/
│   │   │   ├── LoginViewController.java
│   │   │   ├── RegisterViewController.java
│   │   │   ├── MainViewController.java
│   │   │   ├── ProfileViewController.java
│   │   │   ├── AllProblemsController.java
│   │   │   ├── ProblemViewController.java
│   │   │   └── ResultsViewController.java
│   │   └── views/
│   │       ├── LoginView.fxml
│   │       ├── RegisterView.fxml
│   │       ├── MainView.fxml
│   │       ├── ProfileView.fxml
│   │       ├── AllProblems.fxml
│   │       ├── ProblemView.fxml
│   │       └── ResultsView.fxml
│   ├── resources/
│   └── utils/
├── lib/
└── test/
```

## 🖼️ Capturas de Pantalla

### 🔑 Pantalla de Login

<div align="center">
  <img src="images/Login.png" alt="Login" width="600"/>
</div>

### ✍️ Pantalla de Registro

<div align="center">
  <img src="images/Register.png" alt="Register" width="600"/>
</div>

### 🏠 Menú Principal

<div align="center">
  <img src="images/Menu.png" alt="Menu" width="600"/>
</div>

### 📋 Lista de Problemas

<div align="center">
  <img src="images/Problems.png" alt="Problems" width="600"/>
</div>

### 📝 Vista de Problema Individual

<div align="center">
  <img src="images/Problem.png" alt="Problem" width="600"/>
</div>

### 📊 Resultados

<div align="center">
  <img src="images/Results.png" alt="Results" width="600"/>
</div>

## 💻 Requisitos del Sistema

- Java JDK 8 o superior
- JavaFX
- NetBeans IDE (recomendado para desarrollo)

## 🚀 Instalación

1. Clonar el repositorio
2. Abrir el proyecto en NetBeans
3. Ejecutar el archivo build.xml para compilar el proyecto
4. Ejecutar la aplicación desde NetBeans

## 💾 Base de Datos

La aplicación utiliza una base de datos SQLite (data.db) para almacenar:

- Información de usuarios
- Problemas y ejercicios
- Resultados y estadísticas

## 👥 Contribución

Este proyecto fue desarrollado en parejas como parte de un trabajo académico para la asignatura de Interfaces Persona Computador.

---

<div align="center">
  <sub>Desarrollado para la asignatura de IPC</sub>
</div>
