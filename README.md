# ⚡ Automation Exercise Test Automation Framework

<p align="center">
  <img src="https://automationexercise.com/static/images/home/logo.png" alt="Automation Exercise Logo" width="320"/>
</p>

<p align="center">
  <b>Enterprise-Grade SDET Automation Framework for <a href="https://automationexercise.com/">AutomationExercise.com</a></b><br>
  Built with <b>Java 21</b>, <b>Selenium WebDriver 4</b>, <b>TestNG</b>, <b>WebDriverManager</b>, <b>ExtentReports 5</b>, and <b>GitHub Actions CI/CD</b>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Selenium%20WebDriver-4.29.0-43B02A?style=for-the-badge&logo=selenium&logoColor=white" alt="Selenium 4" />
  <img src="https://img.shields.io/badge/TestNG-7.11.0-FF7F00?style=for-the-badge&logo=testng&logoColor=white" alt="TestNG" />
  <img src="https://img.shields.io/badge/Apache%20Maven-3.9.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/ExtentReports-5.1.2-007ACC?style=for-the-badge&logo=buffer&logoColor=white" alt="ExtentReports" />
  <img src="https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions" />
  <img src="https://img.shields.io/badge/License-MIT-brightgreen?style=for-the-badge" alt="License MIT" />
</p>

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Key Architectural Highlights & Interview Defensibility](#-key-architectural-highlights--interview-defensibility)
- [Design Patterns Implemented](#-design-patterns-implemented)
- [Repository Structure](#-repository-structure)
- [Test Coverage Matrix (All 26 Published Scenarios)](#-test-coverage-matrix-all-26-published-scenarios)
- [Prerequisites](#-prerequisites)
- [Local Setup & Execution](#-local-setup--execution)
- [Step-by-Step: How to Push to GitHub](#-step-by-step-how-to-push-to-github)
- [Step-by-Step: How to Deploy & View Reports via GitHub Actions & Pages](#-step-by-step-how-to-deploy--view-reports-via-github-actions--pages)
- [HTML Reporting & Failure Screenshots](#-html-reporting--failure-screenshots)
- [Author & Acknowledgments](#-author--acknowledgments)

---

## 🔍 Overview

This repository contains a full-featured, production-ready test automation framework designed as a showcase portfolio project for **Senior SDET / QA Automation Engineer** roles. It automates all **26 official practice test cases** published on [AutomationExercise.com](https://automationexercise.com/test_cases), covering end-to-end user authentication, catalog search, complex checkout flows, invoice downloads, cart calculations, and customer reviews.

The framework is strictly engineered with **industry best practices**: zero flaky waits (`Thread.sleep`), fluent Page Object navigation, automated failure screenshot capture, runtime directory resilience, and automatic AdSense overlay suppression.

---

## 🏛 Key Architectural Highlights & Interview Defensibility

When discussing this framework in technical interviews, highlight these engineering decisions:

### 1. 🛡 Automated Google AdSense & Vignette Ad Suppression
- **The Challenge:** [AutomationExercise.com](https://automationexercise.com/) injects full-screen Google AdSense vignette interstitial ads on link clicks, which can intercept Selenium clicks and cause `ElementClickInterceptedException` or route the URL to `#google_vignette`.
- **The Engineering Solution:** `DriverFactory` passes Chrome host resolver rules:
  ```java
  chromeOptions.addArguments("--host-resolver-rules=MAP pagead2.googlesyndication.com 127.0.0.1, MAP *.g.doubleclick.net 127.0.0.1, MAP *.googlesyndication.com 127.0.0.1");
  ```
  This suppresses ad network scripts at browser network level without needing external browser extensions or third-party proxies, ensuring 100% deterministic test execution.

### 2. ⏱ Strict Zero `Thread.sleep` Guarantee
- Pure explicit synchronization using `WebDriverWait` and `ExpectedConditions` encapsulated in [`WaitUtil`](src/main/java/utils/WaitUtil.java).
- Every wait strategy is conditioned on state (element visibility, clickability, invisibility, URL changes, or alert presence).

### 3. 📁 Runtime Directory Resilience
- Git does not track empty folders. Relying on `reports/` or `reports/screenshots/` existing after a fresh clone is an antipattern.
- [`ScreenshotUtil`](src/main/java/utils/ScreenshotUtil.java) and [`ExtentManager`](src/main/java/utils/ExtentManager.java) unconditionally execute `Files.createDirectories(...)` before any file write operation.
- `.gitkeep` files are committed in both `reports/extent/` and `reports/screenshots/` to ensure git permanence.

### 4. 🔄 Dynamic Test Data Generation & Lifecycle Cleanup
- Account registration scenarios dynamically generate timestamped emails (`qa_user_<timestamp>@testmail.com`) to prevent duplicate email conflicts.
- All registration tests clean up state by terminating with an account deletion step (`ACCOUNT DELETED!`), ensuring clean subsequent test runs.

### 5. 📸 Automated Failure Screenshots & Rich Reporting
- [`TestListener`](src/main/java/listeners/TestListener.java) implements TestNG's `ITestListener`.
- Upon any test failure, a full viewport screenshot is captured and automatically embedded directly alongside the exception stack trace in the Extent HTML report.

---

## 🎨 Design Patterns Implemented

| Design Pattern | Implementation | Purpose |
|---|---|---|
| **Page Object Model (POM)** | `pages.*` (9 Page Classes) | Decouples page locators and user interactions from test assertions. |
| **Factory Pattern** | `drivers.DriverFactory` | Centralizes creation and configuration of browser-specific drivers (Chrome, Firefox, Edge, Headless). |
| **Singleton Pattern** | `utils.ExtentManager` | Provides a single, thread-safe instance of `ExtentReports` across the entire test suite run. |
| **Observer Pattern** | `listeners.TestListener` | Listens to TestNG lifecycle events (`onTestStart`, `onTestFailure`, `onFinish`) to log execution status. |
| **ThreadLocal Storage** | `ExtentManager.TEST_CONTAINER` | Isolates `ExtentTest` instances per executing thread for reliable parallel execution. |
| **Data-Driven Configuration** | `utils.ConfigReader` | Loads `config.properties` with fallback precedence for CLI system properties (`-Dbrowser`, `-Dheadless`). |

---

## 📂 Repository Structure

```
automation-exercise-framework/
├── .github/
│   └── workflows/
│       └── run-tests.yml                 # GitHub Actions CI/CD (Tests + Artifacts + Pages Deploy)
├── reports/
│   ├── extent/
│   │   ├── .gitkeep                      # Git folder persistence
│   │   └── extent-report.html            # Rich Extent HTML report
│   └── screenshots/
│       ├── .gitkeep                      # Git folder persistence
│       └── *.png                         # Automated failure screenshots
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── base/
│   │       │   └── BaseTest.java         # Driver lifecycle, @BeforeMethod, @AfterMethod, @Listeners
│   │       ├── drivers/
│   │       │   └── DriverFactory.java    # Driver factory with WDM, headless, and ad blocking
│   │       ├── listeners/
│   │       │   └── TestListener.java     # TestNG listener for ExtentReports & failure screenshots
│   │       ├── pages/
│   │       │   ├── HomePage.java
│   │       │   ├── SignupLoginPage.java
│   │       │   ├── AccountInformationPage.java
│   │       │   ├── ProductsPage.java
│   │       │   ├── ProductDetailPage.java
│   │       │   ├── CartPage.java
│   │       │   ├── CheckoutPage.java
│   │       │   ├── PaymentPage.java
│   │       │   └── ContactUsPage.java
│   │       └── utils/
│   │           ├── ConfigReader.java     # Fail-fast property reader with system override
│   │           ├── WaitUtil.java         # Explicit wait wrapper (zero Thread.sleep)
│   │           ├── ScreenshotUtil.java   # Viewport screenshot capture & directory creator
│   │           └── ExtentManager.java    # ExtentReports 5 singleton manager
│   └── test/
│       ├── java/
│       │   └── tests/
│       │       ├── AccountTest.java      # TC01 - TC05
│       │       ├── ContactUsTest.java    # TC06
│       │       ├── NavigationTest.java   # TC07, TC25, TC26
│       │       ├── ProductsTest.java     # TC08, TC09, TC18, TC19, TC21, TC22
│       │       ├── SubscriptionTest.java # TC10, TC11
│       │       ├── CartTest.java         # TC12, TC13, TC17, TC20
│       │       └── CheckoutTest.java     # TC14, TC15, TC16, TC23, TC24
│       └── resources/
│           └── config.properties         # Default execution parameters
├── .gitignore                            # Excludes target/, IDE configs, keeps reports gitkeeps
├── mvnw                                  # Maven Wrapper (Linux / macOS)
├── mvnw.cmd                              # Maven Wrapper (Windows PowerShell / CMD)
├── pom.xml                               # Dependencies, plugins, Java 21 release config
├── README.md                             # Project documentation
└── testng.xml                            # Test suite runner & listener configuration
```

---

## 🧪 Test Coverage Matrix (All 26 Published Scenarios)

| Test Class | Test Case | Official Title | Verification Target |
|---|---|---|---|
| `AccountTest` | **TC01** | Register User | Full registration, profile completion, "ACCOUNT CREATED!", session check, account deletion |
| `AccountTest` | **TC02** | Login User with correct email and password | Valid credentials login, "Logged in as username", account deletion cleanup |
| `AccountTest` | **TC03** | Login User with incorrect email and password | Invalid credentials, verify "Your email or password is incorrect!" error |
| `AccountTest` | **TC04** | Logout User | Valid authentication, logout button click, redirection to `/login` verification |
| `AccountTest` | **TC05** | Register User with existing email | Duplicate email submission, verify "Email Address already exist!" error |
| `ContactUsTest` | **TC06** | Contact Us Form | Form input, file attachment, alert confirmation, success notice, return home |
| `NavigationTest` | **TC07** | Verify Test Cases Page | Header navigation click, landing URL `/test_cases` verification |
| `NavigationTest` | **TC25** | Verify Scroll Up using 'Arrow' button | Scroll to footer, verify "SUBSCRIPTION", click arrow, verify top banner |
| `NavigationTest` | **TC26** | Verify Scroll Up without 'Arrow' button | Scroll to footer, manual scroll to top, verify top hero banner |
| `ProductsTest` | **TC08** | Verify All Products and product detail page | Catalog navigation, product detail verification (name, category, price, stock, brand) |
| `ProductsTest` | **TC09** | Search Product | Search keyword query, verify "SEARCHED PRODUCTS", verify all result names match |
| `ProductsTest` | **TC18** | View Category Products | Sidebar category expansion (Women → Dress, Men → Tshirts), verify category titles |
| `ProductsTest` | **TC19** | View & Cart Brand Products | Sidebar brand filters (Polo, Madame), verify brand-specific catalog views |
| `ProductsTest` | **TC21** | Add review on product | Product detail review submission (name, email, text), verify thank-you notice |
| `ProductsTest` | **TC22** | Add to cart from Recommended items | Home recommended items carousel, add to cart, verify item in cart |
| `SubscriptionTest` | **TC10** | Verify Subscription in home page | Home footer email subscription, verify "You have been successfully subscribed!" |
| `SubscriptionTest` | **TC11** | Verify Subscription in Cart page | Cart footer email subscription, verify success message |
| `CartTest` | **TC12** | Add Products in Cart | Multi-item cart additions, verify prices, quantities, and totals |
| `CartTest` | **TC13** | Verify Product quantity in Cart | Custom quantity input (4 units), add to cart, verify exact quantity in cart |
| `CartTest` | **TC17** | Remove Products From Cart | Remove line item via "X" button, verify row deletion and cart count decrement |
| `CartTest` | **TC20** | Search Products and Verify Cart After Login | Search & add items as guest, login, verify items persist in authenticated cart |
| `CheckoutTest` | **TC14** | Place Order: Register while Checkout | Add items, proceed to checkout, register mid-flow, enter comments, pay, delete account |
| `CheckoutTest` | **TC15** | Place Order: Register before Checkout | Register account upfront, add items, checkout, place order, delete account |
| `CheckoutTest` | **TC16** | Place Order: Login before Checkout | Login upfront, add items, checkout, place order, delete account |
| `CheckoutTest` | **TC23** | Verify address details in checkout page | Validate that delivery and billing addresses at checkout match registration |
| `CheckoutTest` | **TC24** | Download Invoice after purchase order | Complete order, verify "Download Invoice" button, trigger invoice download, delete account |

---

## 💻 Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher ([Eclipse Temurin Recommended](https://adoptium.net/temurin/releases/?version=21))
- **Apache Maven**: Version 3.9+ (or simply use the included `./mvnw` / `.\mvnw.cmd` wrapper)
- **Web Browser**: Google Chrome (installed locally; driver binaries are handled automatically by WebDriverManager)
- **Git**: Installed for version control

Verify installed versions:
```powershell
java -version
mvn -version   # or .\mvnw.cmd -v
git --version
```

---

## 🏃 Local Setup & Execution

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/automation-exercise-framework.git
cd automation-exercise-framework
```

### 2. Configuration Settings (`config.properties`)
Located at `src/test/resources/config.properties`:
```properties
browser=chrome
headless=false
url=https://automationexercise.com/
explicitWaitSeconds=10
```

### 3. Execution Commands

#### Run Full Test Suite (TestNG Suite via Maven):
```powershell
mvn clean test
```

#### Run in Headless Mode (Fast, Ideal for Local Background & CI):
```powershell
mvn clean test -Dheadless=true
```

#### Run Using the Maven Wrapper (No system Maven required):
```powershell
.\mvnw.cmd clean test -Dheadless=true
```

#### Run a Specific Test Class:
```powershell
mvn test -Dtest=AccountTest -Dheadless=true
```

#### Run an Individual Test Method:
```powershell
mvn test -Dtest=AccountTest#testRegisterUser_TC01 -Dheadless=true
```

#### Run on a Different Browser (Firefox / Edge):
```powershell
mvn test -Dbrowser=firefox -Dheadless=true
```

---

## 📤 Step-by-Step: How to Push to GitHub

Follow these exact steps from PowerShell or your preferred terminal:

### Step 1: Open PowerShell in the Project Directory
```powershell
cd d:\Automation_Project\automation-exercise-framework
```

### Step 2: Initialize Git Repository
```powershell
git init
```

### Step 3: Stage All Files
*(The included `.gitignore` will automatically prevent `target/`, IDE folders, and temporary files from being staged)*
```powershell
git add .
```

Check the staged files:
```powershell
git status
```

### Step 4: Make Your Initial Commit
```powershell
git commit -m "feat: complete Java Selenium TestNG automation framework with 26 test cases"
```

### Step 5: Create a New Repository on GitHub
1. Navigate to [GitHub](https://github.com/) and log into your account.
2. In the top-right corner, click **+** → **New repository**.
3. Name your repository: `automation-exercise-framework` (or any preferred name).
4. Choose **Public** (recommended for portfolio showcase).
5. **Do NOT** initialize with a README, .gitignore, or license (we already have them).
6. Click **Create repository**.

### Step 6: Link Remote & Set Main Branch
Copy your GitHub repo URL and run:
```powershell
git branch -M main
git remote add origin https://github.com/<your-username>/automation-exercise-framework.git
```
*(Replace `<your-username>` with your GitHub handle)*

### Step 7: Push Code to GitHub
```powershell
git push -u origin main
```

---

## 🚀 Step-by-Step: How to Deploy & View Reports via GitHub Actions & Pages

The framework includes a fully automated CI/CD pipeline in [`.github/workflows/run-tests.yml`](.github/workflows/run-tests.yml).

### 1. Automated Execution on Every Push
Whenever you push to the `main` branch or create a pull request:
1. GitHub Actions spins up an `ubuntu-latest` runner.
2. Configures JDK 21 and caches Maven dependencies.
3. Automatically runs all tests in headless Chrome: `mvn clean test -Dheadless=true`.
4. Uploads the generated `reports/extent/` and `reports/screenshots/` as downloadable artifacts even if tests fail (`if: always()`).

### 2. How to Download Test Report Artifacts from GitHub
1. Open your repository on GitHub.
2. Click on the **Actions** tab at the top.
3. Click on the latest workflow run (e.g., *Run Selenium Tests*).
4. Scroll down to the **Artifacts** section at the bottom.
5. Click **`automation-exercise-test-reports`** to download the ZIP file containing `extent-report.html` and any captured failure screenshots.

### 3. Deploy Live Extent Report to GitHub Pages (Live Web URL)
To make your HTML test report accessible via a live public link without downloading ZIP files:

1. In your GitHub repository, go to **Settings** → **Pages** (in the left sidebar).
2. Under **Build and deployment** → **Source**, select:
   - **GitHub Actions**
3. That's it! On every subsequent push to `main`, the `deploy-report` job in `.github/workflows/run-tests.yml` will automatically publish the report.
4. Your live report will be available at:
   ```
   https://<your-username>.github.io/automation-exercise-framework/
   ```

---

## 📊 HTML Reporting & Failure Screenshots

- **Extent Report Location:** `reports/extent/extent-report.html`
- **Screenshots Location:** `reports/screenshots/*.png`

To view the report locally after any test execution:
```powershell
Start-Process "reports\extent\extent-report.html"
```

The report features:
- **Dashboard View:** Executive summary metrics, execution duration, test counts, and environment details (OS, Java version, browser, headless status).
- **Test Details View:** Step-by-step logs, timestamped events, and clickable failure screenshots.
- **Embedded Failure Evidence:** Screenshots captured on failure are directly visible inside each failed test node.

---

## 👤 Author & Acknowledgments

- **Developed as an SDET / QA Automation Portfolio Project**
- Target Application: [AutomationExercise.com](https://automationexercise.com/)
- Licensed under the [MIT License](LICENSE)
