package org.fis;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class EbayUITask {
    public static void main(String[] args) {

        // Set the path to the ChromeDriver executable - testing it with my chrome path
        System.setProperty("webdriver.chrome.driver", "/Users/rohithvasudevan/Downloads/chromedriver.exe");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // 1. Open browser
            driver.manage().window().maximize();
            System.out.println("Browser opened successfully.");

            // 2. Navigate to ebay.com
            driver.get("https://www.ebay.com");
            verifyPageLoaded(driver, "Electronics, Cars, Fashion, Collectibles & More | eBay");

            // 3. Search for 'book'
            WebElement searchBox = driver.findElement(By.xpath("//input[@id='gh-ac']")); // Locate search box
            searchBox.sendKeys("book"); // Enter 'book'
            WebElement searchButton = driver.findElement(By.xpath("//input[@id='gh-btn']")); // Locate search button
            searchButton.click();
            verifyPageLoaded(driver, "Book for sale | eBay");

            // 4. Click on the first book in the list
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement firstBook = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//ul[@class='srp-results srp-list clearfix']/li/div/div[2]/a)[1]")
            ));
            firstBook.click();

            // Switch to the new tab if it opens the book in a new one
            switchToNewTab(driver);


            // 5. In the item listing page, click on 'Add to cart'
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@id='atcBtn_btn_1']")
            ));
            addToCartButton.click();

            // 6. Verify the cart has been updated and displays the number of items
            WebElement cartCount = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//i[@id='gh-cart-n']")
            ));

            String itemCount = cartCount.getText();
            if (!itemCount.isEmpty() && Integer.parseInt(itemCount) > 0) {
                System.out.println("Test Passed: Cart updated with " + itemCount + " item(s)." );
            } else {
                System.out.println("Test Failed: Cart is not updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void verifyPageLoaded(WebDriver driver, String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean titleIsCorrect = wait.until(ExpectedConditions.titleContains(expectedTitle));
        if (titleIsCorrect) {
            System.out.println("Page loaded successfully: " + driver.getTitle());
        } else {
            System.out.println("Failed to load the page with title containing: " + expectedTitle);
        }
    }

    private static void switchToNewTab(WebDriver driver) {
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                System.out.println("Switched to the new tab.");
                break;
            }
        }
    }

}