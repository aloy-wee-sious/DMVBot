import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;


/**
 * Created by aloy on 2/13/17.
 */
public class Scraper {
    static WebDriver driver;
/*    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print(scrapeDL("/home/aloy/Documents/Programming/geckodriver", "599"));
        }*/


    public static Appointment scrapeDL(String driverPath, String DMVId, String phone, String firstName, String lastName, Appointment oldAppointment) throws InterruptedException, ParseException {
        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver();
        Wait<WebDriver> wait;

        String result = new String();
        driver.get("https://www.dmv.ca.gov/wasapp/foa/clear.do?goTo=officeVisit&localeName=en");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(3000);

        WebElement element = (driver.findElement(By.name("officeId")));
        Select officeId = new Select(element);
        selectValueInDropDown(DMVId);


        driver.findElement(By.name("numberItems")).click();
        driver.findElement(By.name("taskDLO")).click();
        driver.findElement(By.name("firstName")).sendKeys(firstName);
        driver.findElement(By.name("lastName")).sendKeys(lastName);
        driver.findElement(By.name("telArea")).sendKeys(phone.substring(0,3));
        driver.findElement(By.name("telPrefix")).sendKeys(phone.substring(3,6));
        driver.findElement(By.name("telSuffix")).sendKeys(phone.substring(6,10));
        driver.findElement(By.xpath("/*//*[@id=\"app_content\"]/form/table/tbody/tr/td[1]/input[2]")).click();

        Thread.sleep(3000);
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
        String date = element.getText();
        result = result+element.getText()+"\n";

        Appointment newAppointment = new Appointment(result, date);

        if (oldAppointment == null || newAppointment.date.before(oldAppointment.date)) {
            driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[1]/tbody/tr[9]/td/p/input")).click();
            Thread.sleep(3000);
            element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[2]/tbody/tr/td[1]/form/input"));
            element.submit();
            Thread.sleep(3000);
        }

        driver.quit();

        return newAppointment;
    }

    public static Appointment scrapeBTW(String driverPath, String DMVId, String phone, String firstName, String lastName, Appointment oldAppointment, String DL, String DOB) throws InterruptedException, ParseException {

        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver();
        Wait<WebDriver> wait;

        String result = new String();

        driver.get("https://www.dmv.ca.gov/wasapp/foa/clear.do?goTo=driveTest&localeName=en");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(3000);

        WebElement element = (driver.findElement(By.name("officeId")));
        Select officeId = new Select(element);
        selectValueInDropDown(DMVId);

        driver.findElement(By.xpath("//*[@id=\"DT\"]")).click();
        driver.findElement(By.name("firstName")).sendKeys(firstName);
        driver.findElement(By.name("lastName")).sendKeys(lastName);
        driver.findElement(By.name("dlNumber")).sendKeys(DL);

        String[] split = DOB.split("/");

        driver.findElement(By.name("birthMonth")).sendKeys(split[0]);
        driver.findElement(By.name("birthDay")).sendKeys(split[1]);
        driver.findElement(By.name("birthYear")).sendKeys(split[2]);

        driver.findElement(By.name("telArea")).sendKeys(phone.substring(0,3));
        driver.findElement(By.name("telPrefix")).sendKeys(phone.substring(3,6));
        driver.findElement(By.name("telSuffix")).sendKeys(phone.substring(6,10));

        driver.findElement(By.xpath("//*[@id=\"app_content\"]/form/table/tbody/tr/td[1]/input[2]")).click();
        Thread.sleep(3000);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table/tbody/tr[1]/td[1]/address"));
        result = result+element.getText()+"\n";

        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table/tbody/tr[2]/td/p"));
        result = result+element.getText()+"\n";

        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table/tbody/tr[3]/td[1]/p"));
        result = result+element.getText()+"\n";

        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table/tbody/tr[3]/td[2]/form/p/input"));
        element.click();
        Thread.sleep(3000);

        element = driver.findElement(By.xpath("//*[@id=\"app_content\"]/table[2]/tbody/tr/td[1]/form/input"));
        element.submit();
        Thread.sleep(3000);

        driver.quit();
        return new Appointment(result);
    }

    private static void selectValueInDropDown(String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsCmd = "document.getElementsByName('officeId')[0].value='" + value + "'";
        js.executeScript(jsCmd);
    }
}
