import jdk.nashorn.internal.runtime.regexp.joni.Option;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Created by aloy on 2/13/17.
 */
public class scraper {
    static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {

        Wait<WebDriver> wait;
        System.setProperty("webdriver.gecko.driver", "C:/Users/Aloy/Desktop/geckodriver.exe");
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile myprofile = profile.getProfile("Dev");

        driver = new FirefoxDriver();
        //driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 500);
        Thread.sleep(3000);
        driver.get("https://www.dmv.ca.gov/wasapp/foa/clear.do?goTo=officeVisit&localeName=en");
        WebElement e = (driver.findElement(By.name("officeId")));

        Select officeId = new Select(e);
        selectValueInDropDown("504");


        driver.findElement(By.name("numberItems")).click();
        driver.findElement(By.name("taskDLO")).click();
        driver.findElement(By.name("firstName")).sendKeys("WEILI ALOYSIUS");
        driver.findElement(By.name("lastName")).sendKeys("WANG");
        driver.findElement(By.name("telArea")).sendKeys("510");
        driver.findElement(By.name("telPrefix")).sendKeys("283");
        driver.findElement(By.name("telSuffix")).sendKeys("3821");
        driver.findElement(By.xpath("//*[@id=\"app_content\"]/form/table/tbody/tr/td[1]/input[2]")).click();

        Thread.sleep(3000);

        WebElement w = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[2]"));
        System.out.println(w.getText());

        w = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[3]"));
        System.out.print(w.getText());

    }

    public static void selectValueInDropDown(String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsCmd = "document.getElementsByName('officeId')[0].value='" + value + "'";
        js.executeScript(jsCmd);
    }
}
