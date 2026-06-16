import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration


WebUI.openBrowser(GlobalVariable.url)
WebUI.maximizeWindow()

WebUI.takeScreenshot()


WebUI.click(findTestObject("Object Repository/CrossIcon"))

WebUI.sendKeys(findTestObject("Object Repository/SearchBar"),ProductName)

WebUI.takeScreenshot()

WebUI.click(findTestObject("Object Repository/SearchIcon"))
WebUI.click(findTestObject("Object Repository/FiltterCheckBox",[Brand : ProductBrand]))

WebUI.takeScreenshot()

WebUI.delay(5)

String projectPath = RunConfiguration.getProjectDir()
String dynamicFilePath = projectPath + "/ProductList.xlsx"

CustomKeywords.'commonPackage.commonClass.scrapeProductsToExcel'(dynamicFilePath)

WebUI.takeScreenshot()




























// WebUI.click(findTestObject("null"))

WebUI.delay(5)

// Trigger the loop workflow up to 8 items
CustomKeywords.'commonPackage.commonClass.findAndCartCheapestSizes'()

WebUI.delay(5)
WebUI.click(findTestObject("Object Repository/Cart"))
WebUI.takeScreenshot()

WebUI.delay(5)
CustomKeywords.'commonPackage.commonClass.validateCartDiscountCalculation'()
WebUI.takeScreenshot()



