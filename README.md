# Vitesse Candidate Management

Android application developed for the automotive company **Vitesse** to optimize candidate management by the Human Resources department.

## Objective

This application aims to provide Vitesse's Human Resources department with a modern and efficient tool for managing job applications. It helps centralize candidate information, track their progress, and facilitate communication.

## Key Features

*   **Candidate Management:**
    *   Create new candidate profiles.
    *   Modify existing candidate information.
    *   Delete candidates from the list.
*   **List and Visualization:**
    *   Clear and detailed display of each candidate's information (contact details, desired salary, notes, etc.).
    *   Ability to mark/unmark candidates as "favorites" for quick access or priority tracking.
*   **Quick Actions:**
    *   Initiate a phone call directly from the candidate's profile.
    *   Send an SMS.
    *   Compose an email.
*   **User Interface:**
    *   Intuitive and modern UI built with Jetpack Compose.
    *   Salary display in Euros with an indicative conversion to British Pounds.
    *   Personalized note-taking for each candidate.

## Technologies Used

*   **Language:** Kotlin
*   **UI:** Jetpack Compose
*   **Architecture:** MVVM
*   **Dependency Injection:** Koin
*   **Navigation:** Jetpack Navigation Component
*   **Local Database:** Room
*   **Image Loading:** Coil
*   **HTTP Client:** Ktor => Defined in a KMP shared module

## Running the project

1. **Clone the repository:**
Using `git clone https://github.com/leoarmand/vitesse.git`
2. **Run into Android Studio**
