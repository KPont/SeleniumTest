package test;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarsTest1 {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ######################*/
 /*## Change this, according to your own OS and location of driver(s) ##*/
 /*############################################################*/
//    System.setProperty("webdriver.gecko.driver", "C:\\diverse\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "E:\\Kasper\\School\\chromedriver.exe");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    //Verify that page is loaded and all expected data are visible
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    //Verify the filter functionality 
    public void test2() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(2));
    }

    @Test
    //Verify there are 5 rows after the filter is cleared
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(" ");
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    //Test to verify that it can sort correctly by year. 
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("h_year"));
        element.click();
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        List<WebElement> data = rows.get(0).findElements(By.tagName("td"));
        System.out.println(data.get(0).getText());
        Assert.assertThat(rows.get(0).findElements(By.tagName("td")).get(0).getText(), is("938"));
        Assert.assertThat(rows.get(rows.size() - 1).findElements(By.tagName("td")).get(0).getText(), is("940"));
    }

    @Test
    //Testing the "Edit" feature for car with ID 938
    public void test5() throws Exception {
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        List<WebElement> data = rows.get(0).findElements(By.tagName("td"));
        data.get(7).findElement(By.linkText("Edit")).click();

        WebElement d = driver.findElement(By.id("description"));
        d.clear();
        d.sendKeys("Cool car");
        driver.findElement(By.id("save")).click();
        Assert.assertThat(data.get(5).getText(), is("Cool car"));
    }
    
    @Test
    //Testing that a message will display "All fields are required" after clicking New Car and then Save Car
    public void test6() throws Exception {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("save")).click();
        
        Assert.assertThat(driver.findElement(By.id("submiterr")).getText(), is("All fields are required"));
    }
    
    @Test
    //Testing that a new car can be created
    public void test7() throws Exception {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-5-5");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");
        driver.findElement(By.id("save")).click();
        
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        
        Assert.assertThat(rows.size(), is(6));
    }


}
