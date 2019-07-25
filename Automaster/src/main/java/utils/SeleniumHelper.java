package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumHelper {
	
public static WebDriver driver;
	
	public static void openURL(String url) {
		driver.get(url);
	}
	
	public static String getCurrentURL() {
		  return driver.getCurrentUrl();  
	}
	
	public static void enterTextUsingXpath(String xpath,String text ) {
		WebElement textBox = driver.findElement(By.xpath(xpath));
		textBox.clear();
		textBox.sendKeys(text);
	}
	
	public static void enterTextUsingId(String elementId, String text) {
		WebElement textBox = driver.findElement(By.id(elementId));
		textBox.clear();
		textBox.sendKeys(text);
	}
	
	
	public static void clickButtonUsingXpath(String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
	}
	
	public static void clickButtonUsingId(String elementId) {
		WebElement element = driver.findElement(By.id(elementId));
		element.click();
	}
	
	public static void closeCurrentTab() {
		
	}
	
	public static void closeWindow() {
		driver.close();
	}
	
	public static void waitUntillElementIsVisible(String elementId) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(elementId)));
	}

}
