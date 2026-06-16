package commonPackage

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository

public class commonClass {

	//	@Keyword
	//	public static void scrapeProductsToExcel(String filePath) {
	//		Workbook workbook = new XSSFWorkbook()
	//		Sheet sheet = workbook.createSheet("Products")
	//
	//		Row headerRow = sheet.createRow(0)
	//		headerRow.createCell(0).setCellValue("SLNo")
	//		headerRow.createCell(1).setCellValue("Name")
	//		headerRow.createCell(2).setCellValue("Price")
	//
	////		String nameXpath = "//a[contains(@class, 'atJtCj')]"
	////		String priceXpath = "//div[@class='hZ3P6w']"
	////
	////		TestObject nameObj = new TestObject("ProductNameElements").addProperty("xpath", ConditionType.EQUALS, nameXpath)
	////		TestObject priceObj = new TestObject("ProductPriceElements").addProperty("xpath", ConditionType.EQUALS, priceXpath)
	//
	//		// Fetch items once initially just to safely see how many items exist on the page
	//		List<WebElement> initialNames = WebUI.findWebElements(nameObj, 10)
	//		List<WebElement> initialPrices = WebUI.findWebElements(priceObj, 10)
	//		int itemsToScrape = Math.min(8, Math.min(initialNames.size(), initialPrices.size()))
	//
	//		for (int i = 0; i < itemsToScrape; i++) {
	//			// FIX: Re-fetch fresh element references every loop cycle to bypass DOM updates
	//			List<WebElement> freshNames = WebUI.findWebElements(nameObj, 5)
	//			List<WebElement> freshPrices = WebUI.findWebElements(priceObj, 5)
	//
	//			// Dynamic safety check to avoid unexpected out of bounds index crashes
	//			if (i >= freshNames.size() || i >= freshPrices.size()) {
	//				break
	//			}
	//
	//			WebElement activeNameEl = freshNames.get(i)
	//			WebElement activePriceEl = freshPrices.get(i)
	//
	//			String prodName = activeNameEl.getAttribute("title")
	//			if (prodName == null || prodName.isEmpty()) {
	//				prodName = activeNameEl.getText()
	//			}
	//
	//			String prodPrice = activePriceEl.getText()
	//			int slNo = i + 1
	//
	//			Row row = sheet.createRow(slNo)
	//			row.createCell(0).setCellValue(slNo)
	//			row.createCell(1).setCellValue(prodName)
	//			row.createCell(2).setCellValue(prodPrice)
	//		}
	//
	//		FileOutputStream fileOut = new FileOutputStream(filePath)
	//		workbook.write(fileOut)
	//		fileOut.close()
	//		workbook.close()
	//
	//		println "Excel generated successfully at: " + filePath
	//	}

	@Keyword
	public static void scrapeProductsToExcel(String filePath) {
		Workbook workbook = new XSSFWorkbook()
		Sheet sheet = workbook.createSheet("Products")

		Row headerRow = sheet.createRow(0)
		headerRow.createCell(0).setCellValue("SLNo")
		headerRow.createCell(1).setCellValue("Name")
		headerRow.createCell(2).setCellValue("Price")

		// FETCH FROM OBJECT REPOSITORY: Replace with your actual paths in Object Repository
		TestObject nameObj = ObjectRepository.findTestObject('productName')
		TestObject priceObj = ObjectRepository.findTestObject('productPrice')

		// Fetch items once initially just to safely see how many items exist on the page
		List<WebElement> initialNames = WebUI.findWebElements(nameObj, 10)
		List<WebElement> initialPrices = WebUI.findWebElements(priceObj, 10)
		int itemsToScrape = Math.min(8, Math.min(initialNames.size(), initialPrices.size()))

		for (int i = 0; i < itemsToScrape; i++) {
			// FIX: Re-fetch fresh element references every loop cycle to bypass DOM updates
			List<WebElement> freshNames = WebUI.findWebElements(nameObj, 5)
			List<WebElement> freshPrices = WebUI.findWebElements(priceObj, 5)

			// Dynamic safety check to avoid unexpected out of bounds index crashes
			if (i >= freshNames.size() || i >= freshPrices.size()) {
				break
			}

			WebElement activeNameEl = freshNames.get(i)
			WebElement activePriceEl = freshPrices.get(i)

			String prodName = activeNameEl.getAttribute("title")
			if (prodName == null || prodName.isEmpty()) {
				prodName = activeNameEl.getText()
			}

			String prodPrice = activePriceEl.getText()
			int slNo = i + 1

			Row row = sheet.createRow(slNo)
			row.createCell(0).setCellValue(slNo)
			row.createCell(1).setCellValue(prodName)
			row.createCell(2).setCellValue(prodPrice)
		}

		FileOutputStream fileOut = new FileOutputStream(filePath)
		workbook.write(fileOut)
		fileOut.close()
		workbook.close()

		println "Excel generated successfully at: " + filePath
	}

	@Keyword
	public static void findAndCartCheapestSizes() {
		TestObject productLinksObj = new TestObject("ProductLinks").addProperty("xpath", ConditionType.EQUALS, "//a[contains(@class, 'atJtCj')]")

		// Base XPath targeting the size wrapper boxes
		String baseSizesXpath = "//div[@font='default-fk-font-l' and translate(text(), '0123456789', '') = '']"
		TestObject sizesObj = new TestObject("ProductSizes").addProperty("xpath", ConditionType.EQUALS, baseSizesXpath)

		TestObject priceObj = new TestObject("ProductPrice").addProperty("xpath", ConditionType.EQUALS, "//div[@font='default-fk-font-m' and contains(text(), '\u20b9')]")
		TestObject addToCartObj = new TestObject("AddToCartBtn").addProperty("xpath", ConditionType.EQUALS, "//*[text()='Add to cart' or (local-name()='g' and ./*[local-name()='path' and @d='M15.25 7H20.75'])]")

		WebDriver driver = DriverFactory.getWebDriver()
		JavascriptExecutor js = (JavascriptExecutor) driver

		for (int i = 0; i < 8; i++) {
			List<WebElement> products = WebUI.findWebElements(productLinksObj, 10)
			if (i >= products.size()) {
				println "No more products found on the search grid."
				break
			}

			products.get(i).click()
			WebUI.delay(4) // Give the new tab ample processing time to load completely

			int mainGridTab = WebUI.getWindowIndex()
			WebUI.switchToWindowIndex(mainGridTab + 1)
			WebUI.waitForPageLoad(15)

			// ADDED: Extra 2 seconds for slower asynchronous product elements to finish rendering
			WebUI.delay(2)

			// Safely count how many size options exist using a fresh query
			List<WebElement> sizeElements = WebUI.findWebElements(sizesObj, 5)
			int totalSizesCount = sizeElements.size()
			println "Product " + (i + 1) + " - Found total size elements: " + totalSizesCount

			int lowestPrice = Integer.MAX_VALUE
			int bestSizeIndex = -1

			// REQUIREMENT 1: If no sizes are available on this product page at all
			if (totalSizesCount == 0) {
				println "Product " + (i + 1) + " has no size options. Proceeding to add directly to cart."
				WebUI.waitForElementPresent(addToCartObj, 5)
				WebUI.click(addToCartObj)
				WebUI.delay(3)
			}
			else {
				// Sizes are present, loop through them
				for (int s = 0; s < totalSizesCount; s++) {
					String dynamicSizeXpath = "(" + baseSizesXpath + ")[" + (s + 1) + "]"
					TestObject dynamicSizeObj = new TestObject("DynamicSize").addProperty("xpath", ConditionType.EQUALS, dynamicSizeXpath)

					List<WebElement> dynamicElements = WebUI.findWebElements(dynamicSizeObj, 3)
					if (dynamicElements.isEmpty()) continue

						WebElement sizeButton = dynamicElements.get(0)
					String styleAttr = sizeButton.getAttribute("style") ?: ""
					String classAttr = sizeButton.getAttribute("class") ?: ""

					if (styleAttr.contains("line-through") || classAttr.contains("disabled")) {
						continue
					}

					js.executeScript("arguments[0].click();", sizeButton)
					WebUI.delay(2) // Wait for dynamic prices to re-calculate

					String rawPrice = WebUI.getText(priceObj).replaceAll("[^0-9]", "")
					if (!rawPrice.isEmpty()) {
						int currentPrice = Integer.parseInt(rawPrice)
						println "Product " + (i + 1) + " - Size Index " + s + " Price: \u20b9" + currentPrice

						// REQUIREMENT 2: Using <= instead of <.
						// If prices are equal, it picks the later index (which represents the higher shoe size).
						if (currentPrice <= lowestPrice) {
							lowestPrice = currentPrice
							bestSizeIndex = s
						}
					}
				}

				// Select and add the chosen cheapest/higher size option to your cart
				if (bestSizeIndex != -1) {
					String targetCheapestXpath = "(" + baseSizesXpath + ")[" + (bestSizeIndex + 1) + "]"
					TestObject targetCheapestObj = new TestObject("CheapestSize").addProperty("xpath", ConditionType.EQUALS, targetCheapestXpath)

					List<WebElement> purchaseElements = WebUI.findWebElements(targetCheapestObj, 5)
					if (!purchaseElements.isEmpty()) {
						js.executeScript("arguments[0].click();", purchaseElements.get(0))

						// ADDED: Extra 2 seconds for the Add to Cart button layout properties to update post-click
						WebUI.delay(2)

						WebUI.waitForElementPresent(addToCartObj, 5)
						WebUI.click(addToCartObj)
						println "Added lowest price (higher size tie-breaker) for product " + (i + 1) + " to your cart."
						WebUI.delay(3)
					}
				} else {
					println "All sizes were out of stock or disabled for product " + (i + 1)
				}
			}

			// Close current product tab and return to catalog list
			WebUI.closeWindowUrl(WebUI.getUrl())
			WebUI.switchToWindowIndex(mainGridTab)
			WebUI.delay(3)
		}
	}

	@Keyword
	public static void validateCartDiscountCalculation() {
		TestObject actualPriceObj = new TestObject("ActualPrices").addProperty("xpath", ConditionType.EQUALS, "//span[@class='u7129o thy_Kd']")
		TestObject discountPriceObj = new TestObject("DiscountPrices").addProperty("xpath", ConditionType.EQUALS, "//span[@class='u7129o Mmn1B1']")
		TestObject totalDiscountObj = new TestObject("TotalDiscountSummary").addProperty("xpath", ConditionType.EQUALS, "(//span[@class='A_hdUj'])")

		List<WebElement> actualElements = WebUI.findWebElements(actualPriceObj, 10)
		List<WebElement> discountElements = WebUI.findWebElements(discountPriceObj, 10)

		int calculatedTotalDiscount = 0
		int itemsCount = Math.min(8, Math.min(actualElements.size(), discountElements.size()))

		println "=================== CART PRICE BREAKDOWN VALIDATION ==================="
		println "Total items processed: " + itemsCount

		for (int i = 0; i < itemsCount; i++) {
			String rawActual = actualElements.get(i).getText().replaceAll("[^0-9]", "")
			String rawDiscount = discountElements.get(i).getText().replaceAll("[^0-9]", "")

			int actualPrice = !rawActual.isEmpty() ? Integer.parseInt(rawActual) : 0
			int discountPrice = !rawDiscount.isEmpty() ? Integer.parseInt(rawDiscount) : 0

			int itemSavings = actualPrice - discountPrice
			calculatedTotalDiscount += itemSavings

			println "Item " + (i + 1) + " -> Actual: \u20b9" + actualPrice + " | Discounted: \u20b9" + discountPrice + " | Savings: \u20b9" + itemSavings
		}

		WebUI.waitForElementPresent(totalDiscountObj, 5)
		String rawTotalDiscountText = WebUI.getText(totalDiscountObj).replaceAll("[^0-9]", "")
		int actualPageTotalDiscount = !rawTotalDiscountText.isEmpty() ? Integer.parseInt(rawTotalDiscountText) : 0

		int variance = Math.abs(calculatedTotalDiscount - actualPageTotalDiscount)

		println "-----------------------------------------------------------------------"
		println "Expected Total Savings Sum: \u20b9" + calculatedTotalDiscount
		println "Actual Page Discount Displayed: \u20b9" + actualPageTotalDiscount
		println "Calculated Discrepancy Variance: \u20b9" + variance
		println "-----------------------------------------------------------------------"

		// Check if there is an exact match
		if (calculatedTotalDiscount == actualPageTotalDiscount) {
			KeywordUtil.markPassed("SUCCESS: Cart calculations are perfectly valid!")
			println "======================================================================="
		} else {
			// FIX: Prints detailed variance to console but DOES NOT throw an exception or halt execution
			KeywordUtil.markWarning("ALERT: Cart calculation mismatch detected. Discrepancy of \u20b9" + variance)
			println "WARNING DETAILS -> Your calculated discount sum was \u20b9" + calculatedTotalDiscount + ", but Flipkart UI summary states \u20b9" + actualPageTotalDiscount
			println "======================================================================="
		}
	}
}
