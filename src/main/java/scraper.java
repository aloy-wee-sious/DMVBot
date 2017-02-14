import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by aloy on 2/13/17.
 */
public class scraper {

    public static void main(String[] args) {
        WebDriver driver;
        Wait<WebDriver> wait;

        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 3000);
        driver.get("https://www.dmv.ca.gov/wasapp/foa/clear.do?goTo=officeVisit&localeName=en");
        Select officId = new Select(driver.findElement(By.className("officeId")));
        officId.selectByValue("593");

        WebElement webElement = driver.findElement(By.name("numberItems"));
        String e = webElement.getText();
    }
}
