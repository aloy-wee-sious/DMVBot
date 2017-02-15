import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.io.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by aloy on 2/13/17.
 */
public class Scraper {
    static WebDriver driver;
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print(scrape("C:/Users/Aloy/Desktop/geckodriver.exe", "599"));
        }


    public static String scrape (String driverPath, String DMVId) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver();
        Wait<WebDriver> wait;

        String result = new String();
        driver.get("https://www.dmv.ca.gov/wasapp/foa/clear.do?goTo=officeVisit&localeName=en");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(2000);

        WebElement element = (driver.findElement(By.name("officeId")));
        Select officeId = new Select(element);
        selectValueInDropDown(DMVId);


        driver.findElement(By.name("numberItems")).click();
        driver.findElement(By.name("taskDLO")).click();
        driver.findElement(By.name("firstName")).sendKeys("DMV");
        driver.findElement(By.name("lastName")).sendKeys("BOT");
        driver.findElement(By.name("telArea")).sendKeys("510");
        driver.findElement(By.name("telPrefix")).sendKeys("000");
        driver.findElement(By.name("telSuffix")).sendKeys("1111");
        driver.findElement(By.xpath("/*//*[@id=\"app_content\"]/form/table/tbody/tr/td[1]/input[2]")).click();

        Thread.sleep(2000);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //location
        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[2]/td/address/table/tbody/tr[1]/td[2]"));
        result = result+element.getText()+"\n";

        //address
        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[2]/td/address/table/tbody/tr[2]/td[2]"));
        result = result+element.getText()+"\n";

        //address line 2
        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[2]/td/address/table/tbody/tr[3]/td[2]"));
        result = result+element.getText()+"\n";

        //First appointment
        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[3]/td/p[1]"));
        result = result+element.getText()+"\n";

        //timing
        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[3]/td/p[2]"));
        result = result+element.getText()+"\n";

        driver.quit();

        return result;
    }

    private static void selectValueInDropDown(String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsCmd = "document.getElementsByName('officeId')[0].value='" + value + "'";
        js.executeScript(jsCmd);
    }
}
