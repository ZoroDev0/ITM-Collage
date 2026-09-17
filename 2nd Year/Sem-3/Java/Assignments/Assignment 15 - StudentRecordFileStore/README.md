# Student Record File Store — OOP + DSA Edition

**A Comprehensive Desktop Application Demonstrating Core Java, OOP Principles, and Fundamental Data Structures**

- **Student Name:** Sasanka Sekhar Kundu
- **Roll Number:** 150096725118
- **Technology Stack:** Java 21 (OpenJDK 21.0.10) + Java Swing Desktop GUI
- **Architecture:** Clean Layered Architecture (`model`, `interfaces`, `service`, `auth`, `exception`, `gui`)
- **Default Authentication:** Username: `admin` | Password: `admin123`

---

## 1. Project Overview & Design Philosophy

**Student Record File Store** is an academic desktop management system designed for university faculty and administrators. It demonstrates how core Java principles, Object-Oriented Programming (OOP) paradigms, and fundamental Data Structures & Algorithms (DSA) cooperate in a real-world software system without relying on external heavy enterprise frameworks or databases.

The graphical interface is built around a warm, human-designed palette:
- **Primary Dark:** `#291C0E` (Sidebar, primary buttons, headers)
- **Primary Brown:** `#6E473B` (Accent controls, focus outlines, icons)
- **Muted Brown:** `#A78D78` (Subtitles, metadata, secondary text)
- **Neutral:** `#BEB5A9` (Borders, card dividers, neutral buttons)
- **Cream:** `#E1D4C2` (Main surface, card backgrounds, input fields)

---

## 2. Academic Concepts Implementation Map

### A. Core Java Pillars
| Concept | Implementation Location | Concrete Functionality |
|---|---|---|
| **File Handling** | `service.FileHandler` | Persists and loads records to/from `data/students.txt` using `BufferedReader` and `BufferedWriter`. Handles missing files and recovers gracefully from corrupted/malformed records. |
| **ArrayList** | `service.StudentManager` | In-memory primary repository (`ArrayList<Student>`) providing $O(1)$ random access and dynamic list expansion. |
| **Exception Handling** | `exception.StudentException` | Custom checked exception hierarchy catching business rule violations, invalid marks ($<0$ or $>100$), negative semesters, empty strings, duplicate IDs, and disk I/O errors. |

### B. Object-Oriented Programming (OOP)
| Concept | Implementation Location | Concrete Functionality |
|---|---|---|
| **Abstraction** | `model.Person` | Abstract base class encapsulating common identity (`id`, `name`) and mandating implementation of `abstract String displayInfo()`. |
| **Inheritance** | `model.Student extends Person` | Inherits identity fields from `Person` and extends them with academic attributes (`course`, `semester`, `marks`). |
| **Encapsulation** | `model.Person`, `model.Student` | Private and protected fields accessed exclusively through validated public getters and setters. |
| **Polymorphism** | `gui.AboutPanel`, `service.StudentManager` | Runtime dynamic method dispatch via base references (`Person person = new Student(...)`) invoking `Student.displayInfo()`. |
| **Interfaces** | `interfaces.Validatable` | Formal contract declaring `void validate() throws StudentException`, implemented by `Student` before any write operation. |
| **Method Overriding** | `model.Student` | Overrides `displayInfo()` from `Person` and `toString()` from `Object`. |

### C. Data Structures & Algorithms (DSA)
| Concept | Implementation Location | Concrete Functionality |
|---|---|---|
| **Stack (LIFO)** | `service.UndoManager` | `Stack<Action>` storing previous states of student mutations. Powers the **Undo Last Action** feature (reverses ADD, UPDATE, and DELETE). |
| **Queue (FIFO)** | `service.OperationQueue` | `Queue<StudentOperation>` (`LinkedList`) maintaining pending mutations in First-In-First-Out order with live activity tracking. |
| **Searching (Linear)** | `service.SearchService` | Traversal search across Student ID, Name, and Course ($O(n)$). |
| **Searching (Binary)** | `service.SearchService` | $O(\log n)$ exact lookup algorithm (`binarySearchById`) on Student ID-sorted datasets. |
| **Sorting** | `service.SortService` | Custom `Comparator<Student>` implementations sorting records by Student ID, Name, Marks, or Semester in Ascending/Descending order. |

---

## 3. Package & Project Directory Structure

```
StudentRecordFileStore/
│
├── src/
│   ├── Main.java                          # Entry point launching LoginFrame
│   ├── ProjectVerifier.java               # 16-point automated test verification suite
│   │
│   ├── auth/
│   │   └── AuthenticationManager.java     # Credential authentication (admin / admin123)
│   │
│   ├── exception/
│   │   └── StudentException.java          # Custom domain exception class
│   │
│   ├── interfaces/
│   │   └── Validatable.java               # Contract for domain integrity validation
│   │
│   ├── model/
│   │   ├── Person.java                    # Abstract base entity (Abstraction, Encapsulation)
│   │   ├── Student.java                   # Extends Person implements Validatable
│   │   ├── Action.java                    # Snapshot entity for Undo Stack (LIFO)
│   │   └── StudentOperation.java          # Event wrapper for FIFO Queue
│   │
│   ├── service/
│   │   ├── StudentManager.java            # Primary ArrayList store + Undo/Queue coordinator
│   │   ├── FileHandler.java               # Java File I/O for data/students.txt
│   │   ├── SearchService.java             # Linear Search O(n) & Binary Search O(log n)
│   │   ├── SortService.java               # Custom Comparator sorting (ID, Name, Marks, Sem)
│   │   ├── UndoManager.java               # Stack<Action> implementation
│   │   └── OperationQueue.java            # Queue<StudentOperation> implementation
│   │
│   └── gui/
│       ├── UIConstants.java               # Shared warm palette, typography, and Java2D vector icons
│       ├── LoginFrame.java                # Authentication window with credentials hint
│       ├── MainFrame.java                 # Primary post-login JFrame with persistent sidebar & CardLayout
│       ├── DashboardPanel.java            # Overview only (KPIs, recent preview, quick shortcuts)
│       ├── AddStudentPanel.java           # Dedicated Add Student page with validation
│       ├── RecordsPanel.java              # Dedicated Records table with Sorting, Inline Edit/Delete, & Undo
│       ├── SearchPanel.java               # Dedicated Search page with Linear & Binary Search toggles
│       ├── FileOperationsPanel.java       # Dedicated File management page (Save, Reload, Status Console)
│       └── AboutPanel.java                # Dedicated Viva checklist and runtime polymorphism test
│
├── data/
│   └── students.txt                       # Flat-file store (101|Rahul Sharma|B.Tech CSE|2|85)
│
└── README.md                              # Comprehensive documentation and viva guide
```

---

## 4. Multi-Page Navigation Architecture

After signing in, the application uses **one single `MainFrame`** with a left-hand persistent sidebar and a `CardLayout` container:

1. **Dashboard:**
   - High-level overview only.
   - Shows KPI cards (Total Students, Records Saved, File Status, Operation Queue count).
   - Recent 5 students preview table.
   - Quick Action buttons routing to dedicated pages.
2. **Add Student:**
   - Dedicated data entry page with field icons and focus rings.
   - Full validation flow: input $\rightarrow$ `Validatable.validate()` $\rightarrow$ `Student` $\rightarrow$ `ArrayList` $\rightarrow$ `OperationQueue` $\rightarrow$ `UndoManager` $\rightarrow$ `FileHandler` $\rightarrow$ disk.
3. **View Records:**
   - Full student table with column centering and sage marks badges.
   - Sorting bar with field selector (`ID`, `Name`, `Marks`, `Semester`) and order toggle (`Ascending` / `Descending`).
   - **Undo Last Action Button:** Pops from `Stack<Action>` and reverses the latest ADD, UPDATE, or DELETE.
   - Inline micro action icons for quick Edit and Delete.
4. **Search:**
   - Search query input with algorithm selector:
     - `Linear Search (All Fields: ID, Name, Course)` — $O(n)$
     - `Binary Search (Exact Student ID)` — $O(\log n)$
   - Displays real-time nanosecond/microsecond execution metrics.
5. **File Operations:**
   - Storage metadata (target file, existence, byte size, active count).
   - "Save to File" and "Reload from File" controls.
   - Live activity console logging I/O operations.
6. **About & Viva Concepts:**
   - Author details: **Sasanka Sekhar Kundu (Roll: 150096725118)**.
   - Complete academic checklist for OOP, DSA, and Core Java.
   - Interactive **"Test Polymorphism"** button demonstrating dynamic dispatch live on screen.

---

## 5. How to Compile and Run

### Prerequisites
- Operating System: Windows / macOS / Linux
- Java Version: OpenJDK 21 or compatible JDK

### Step 1: Open PowerShell / Terminal
```powershell
cd d:\Mini-Projects\StudentRecordFileStore
```

### Step 2: Compile All Sources
```powershell
$files = (Get-ChildItem -Recurse -Filter *.java src).FullName; javac -d bin $files
```

### Step 3: Run the Application
```powershell
java -cp bin Main
```
*(Log in using default credentials: **`admin`** / **`admin123`**).*

### Step 4: Run the Automated 16-Test Verifier
```powershell
java -cp bin ProjectVerifier
```

---

## 6. Automated Test Verification Results

The test suite (`ProjectVerifier.java`) validates all 16 academic and functional specifications:

```
==================================================
  STUDENT RECORD FILE STORE - OOP + DSA VERIFIER
  Student: Sasanka Sekhar Kundu | Roll: 150096725118
==================================================

[TEST 1] 1. Authentication System (admin / admin123) ... PASSED
[TEST 2] 2. OOP Inheritance & Abstraction (Student extends Person) ... PASSED
[TEST 3] 3. OOP Interface Contract (Validatable.validate()) ... PASSED
[TEST 4] 4. OOP Polymorphism & Method Overriding (Person -> Student.displayInfo()) ... PASSED
[TEST 5] 5. File Handling: Auto-Creation of Directory & File ... PASSED
[TEST 6] 6. ArrayList: In-Memory Storage & File Persistence ... PASSED
[TEST 7] 7. Exception Handling: Duplicate ID (Throws StudentException) ... PASSED
[TEST 8] 8. Queue DSA: FIFO StudentOperation Processing ... PASSED
[TEST 9] 9. Searching DSA: Linear Search across ID/Name/Course ... PASSED
[TEST 10] 10. Searching DSA: Binary Search by Student ID O(log n) ... PASSED
[TEST 11] 11. Sorting DSA: Multi-Attribute Comparators (SortService) ... PASSED
[TEST 12] 12. Stack DSA: Undo ADD Action via UndoManager ... PASSED
[TEST 13] 13. Stack DSA: Undo UPDATE Action via UndoManager ... PASSED
[TEST 14] 14. Stack DSA: Undo DELETE Action via UndoManager ... PASSED
[TEST 15] 15. File Handling: Resilience Against Corrupt/Malformed Lines ... PASSED
[TEST 16] 16. Stack Exception Handling: Empty Undo Stack ... PASSED

==================================================
VERIFICATION SUMMARY: 16 / 16 TESTS PASSED
ALL OOP + DSA + CORE JAVA REQUIREMENTS VERIFIED!
==================================================
```

---

## 7. Viva Explanation Guide

During project evaluation, explain the concepts using this quick reference:

- **Where is Abstraction & Inheritance?**
  Explain `model/Person.java` (abstract class with `abstract String displayInfo()`) and `model/Student.java` (`Student extends Person`).
- **Where is Polymorphism?**
  Point to `gui/AboutPanel.java` or `Main.java` where `Person person = new Student(...)` is used, and show how calling `person.displayInfo()` dynamically resolves to `Student`'s overridden method.
- **Where is the Interface?**
  Point to `interfaces/Validatable.java` and explain how `Student implements Validatable`, validating data integrity before changes are committed.
- **Where is the Stack?**
  Point to `service/UndoManager.java` which maintains `Stack<Action>`. Show how performing an Add, Update, or Delete pushes an action, and clicking **Undo Last Action** pops and reverses the mutation.
- **Where is the Queue?**
  Point to `service/OperationQueue.java` which maintains `Queue<StudentOperation>` (`LinkedList`), processing transactions in FIFO order.
- **Where are Searching & Sorting?**
  Explain `service/SearchService.java` for $O(n)$ Linear Search and $O(\log n)$ Binary Search, and `service/SortService.java` for `Comparator<Student>` sorting.
- **Where are File Handling, ArrayList, and Exceptions?**
  Explain `service/FileHandler.java` for `BufferedReader`/`BufferedWriter`, `service/StudentManager.java` for `ArrayList<Student>`, and `exception/StudentException.java` for checked exception handling.
- **Where is Pagination implemented?**
  Explain how pagination is implemented cleanly across all data tables:
  1. `gui/RecordsPanel.java`: Full interactive pagination with page size selection (5, 8, 10, 15, 20), page range status, previous/next controls, and dynamic numbered buttons synchronized with inline edits, deletions, and Undo Stack.
  2. `gui/SearchPanel.java`: Paginated query result set with row slicing, search timer metrics, and custom page sizes.
  3. `gui/DashboardPanel.java`: Paginated recent student records table with page sizing and numbered navigation.
