# Flipkart Automation Project (Katalon Studio)

An automated web regression testing project built in **Katalon Studio** to handle dynamic e-commerce shopping workflows, data collection, complex cart decision-making matrices, and order discount validations on Flipkart.

---

## 🚀 Core Test Workflow & Logic

The automation engine executes a structured, multi-phase testing routine across the web platform:

### Phase 1: Search & Catalog Excel Archival
* **Product Search**: Launches the browser, navigates to Flipkart, and runs an asynchronous keyword query for `"shoes"`.
* **Data Extraction**: Identifies the primary search catalog result grid.
* **Excel Storage**: Extracts critical metadata for the **first 8 products** (Serial Number, Product Name, and Price Fields) and records them into an external Excel spreadsheet using data-driven integrations.

### Phase 2: Dynamic Pricing & Size Optimization (Tie-Breaker Matrix)
The script systematically opens each of the 8 archived products in a new browser tab to check size-based availability:
* **The Low-Price Rule**: Loops through every available size bubble option on the product details page. It selects and adds the **lowest-priced size** directly to the cart.
* **The High-Size Tie-Breaker Rule**: If all size variants for a product share the exact same price point, the script evaluates the indices and selects the **highest available shoe size** to add to the cart.
* **Fallback Strategy**: If a product has no visible size options, it skips selection and directly triggers the primary checkout target.

### Phase 3: Checkout Cart Discount Validations
Once all 8 optimized products are added to the checkout bucket, the script switches to the active Cart interface:
1. Loops through every individual product line item inside the cart.
2. Extracts and reads the specific **individual discount values** mapped to each item.
3. Automatically sums up all the individual discounts programmatically.
4. Grabs the final **Total Discount** figure displayed in the checkout summary sidebar panel.
5. Runs an automated structural assertion comparing the calculated sum against the page's total discount to validate data integrity.

---

## 🛠️ Automated Tech Stack
* **Automation Platform**: Katalon Studio Enterprise
* **Scripting Runtime**: Groovy / Java
* **Data Persistence Engine**: Apache POI (Excel Binding)
* **Locator Model**: Parameterized & Dynamic Test Objects (Centralized Repository)

---

## 📂 Repository Exclusion Rules (`.gitignore`)
To prevent machine-specific workspace data from polluting remote branches, ensure your `.gitignore` includes:
```text
bin/
libs/
.settings/
.classpath
.project
Reports/
Object Repository/.cache/
```
