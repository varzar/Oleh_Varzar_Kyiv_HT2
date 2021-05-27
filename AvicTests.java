package avic;
//import org.testing.annotations.BeforeMethode;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.ENTER;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class AvicTests {

    WebDriver driver;

    @BeforeTest
    public  void  setUp(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testsSetUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/ua");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchQueryPocketBook(){
        driver.findElement(xpath("//input[@id='input_search']")).clear();
        driver.findElement(xpath("//input[@id='input_search']")).sendKeys("PocketBook 628", ENTER);
        Assert.assertTrue(driver.getCurrentUrl().contains("query=PocketBook"));
    }



    @Test(priority = 2)
    public void checkElementsAmountOnSearchPagePocketBook() {
        driver.findElement(xpath("//input[@id='input_search']")).sendKeys("PocketBook 628", ENTER);//вводим в поиск PocketBook 628
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);//неявное ожидание 30 сек
        List<WebElement> elementsList = driver.findElements(xpath("//div[contains(text(),'PocketBook 628')]"));//собрали элементы поиска в лист
        int actualElementsSize = elementsList.size();//узнали количество элементов в листе
        assertEquals(actualElementsSize, 2);//сравнили количество элементов актуальное с тем которое ожидаем
    }



    @Test(priority = 3)
    public void checkThatSearchResultsContainsSearchWordPocketBook() {
        driver.findElement(xpath("//input[@id='input_search']")).sendKeys("PocketBook 628", ENTER);//вводим в поиск PocketBook 628
        List<WebElement> elementList = driver.findElements(xpath("//div[contains(text(),'PocketBook 628')]"));//собрали элементы поиска в лист
        for (WebElement webElement : elementList) { //прошлись циклом и проверили что каждый элемент листа содержит текст PocketBook 628
            assertTrue(webElement.getText().contains("PocketBook 628"));
        }
    }



    @Test(priority = 4)
    public void checkAddToCartPocketBook() {
        driver.findElement(xpath("//span[@class='sidebar-item']")).click();//каталог товаров
        driver.findElement(xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'noutbuki-i-aksessuaryi')]")).click();//Ноутбуки та планшети
        driver.findElement(xpath("//div[@class='brand-box__title']/a[contains(text(),'Електронні книги')]")).click();//Електронні книги
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));//wait for page loading
        driver.findElement(xpath("//a[@class='prod-cart__buy'][contains(@data-ecomm-cart,'Black (PB628-P-CIS)')]")).click();//add to cart PocketBook 628
        WebDriverWait wait = new WebDriverWait(driver, 30);//ждем пока не отобразится попап с товаром добавленным в корзину
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(xpath("//div[@class='btns-cart-holder']//a[contains(@class,'btn--orange')]")).click();//продолжить покупки
        String actualProductsCountInCart =
                driver.findElement(xpath("//div[contains(@class,'header-bottom__cart')]//div[contains(@class,'cart_count')]")).getText();//получили 1 которая в корзине (один продукт)
        assertEquals(actualProductsCountInCart, "1");
    }

    @AfterMethod
    public void tearDown(){
        driver.close();
    }
}
