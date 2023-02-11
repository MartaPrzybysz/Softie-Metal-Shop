import com.beust.ah.A;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MetalShop {
    static WebDriver driver = new ChromeDriver();
    String password = "Marta12345";
    String username = "Marta122022";
    String firstname = "Jan";
    String lastname = "Kowalski";
    String address = "test1234@gmail.com";

    @BeforeAll
    public static void setUp() {
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
    }

    @AfterAll
    public static void closeBrowser() {
        driver.quit();
    }

    @BeforeEach
    public void goToHomePage() {
        driver.findElement(By.linkText("Sklep")).click();
    }

    @Test
    public void emptyLogin() {
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();
        String error = driver.findElement(By.cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: Nazwa użytkownika jest wymagana.", error);
    }

    @Test
    public void emptyPassword() {
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.name("login")).click();
        String error = driver.findElement(By.cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: pole hasła jest puste.", error);
    }

    @Test
    public void registerSuccess() {
        driver.findElement(By.linkText("register")).click();
        Faker faker = new Faker();
        String registerUsername = faker.name().username();
        String email = registerUsername + faker.random().nextInt(12354) + "@gmail.com";
        driver.findElement(By.cssSelector("#user_login")).sendKeys(registerUsername);
        driver.findElement(By.cssSelector("#user_email")).sendKeys(email);
        driver.findElement(By.cssSelector("#user_pass")).sendKeys(password);
        driver.findElement(By.cssSelector("#user_confirm_password")).sendKeys(password);
        driver.findElement(By.cssSelector(".ur-submit-button")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user-registration-message")));
        WebElement error = driver.findElement(By.cssSelector(".user-registration-message"));
        Assertions.assertEquals("User successfully registered.", error.getText());
    }

    @Test
    public void goToContactPage() {
        driver.findElement(By.linkText("Kontakt")).click();
        Assertions.assertEquals("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/" +
                "kontakt/", driver.getCurrentUrl());
    }

    @Test
    public void goBackToHomePage() {
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.linkText("Strona główna")).click();
        Assertions.assertEquals("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/",
                driver.getCurrentUrl());
    }

    @Test
    public void sendMessage() {
        driver.findElement(By.linkText("Kontakt")).click();
        driver.findElement(By.name("your-name")).sendKeys(firstname + " " + lastname);
        driver.findElement(By.name("your-email")).sendKeys(address);
        driver.findElement(By.cssSelector(".wpcf7-submit")).submit();
        WebElement response = driver.findElement(By.cssSelector(".wpcf7-response-output"));
        Assertions.assertTrue(response.isEnabled());
    }


    @Test
    public void addProductToCart() {
        driver.findElement(By.cssSelector(".cart-contents")).click();
        WebElement info = driver.findElement(By.cssSelector(".cart-empty"));
        Assertions.assertEquals("Twój koszyk aktualnie jest pusty.", info.getText());
        driver.findElement(By.linkText("Wróć do sklepu")).click();
        driver.findElement(By.cssSelector(".post-28")).click();
        driver.findElement(By.cssSelector(".single_add_to_cart_button")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement price = driver.findElement(By.cssSelector(".amount"));
        Assertions.assertEquals("3000,00 zł", price.getText());
    }

    @Test
    public void deleteProductFromCart() {
        driver.findElement(By.cssSelector(".post-11")).click();
        driver.findElement(By.cssSelector(".single_add_to_cart_button")).click();
        driver.findElement(By.linkText("Zobacz koszyk")).click();
        driver.findElement(By.cssSelector(".remove")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        Assertions.assertEquals("Usunięto: „Złota moneta 5g - UK 1980”. Cofnij?",
                driver.findElement(By.cssSelector(".woocommerce-message")).getText());
    }

    @Test
    public void isLogoDisplayed() {
    WebElement logo = driver.findElement(By.linkText("Softie Metal Shop"));
    WebElement search = driver.findElement(By.cssSelector(".search-field"));
    Assertions.assertTrue(logo.isDisplayed() && search.isDisplayed());
    driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/moje-konto/");
    WebElement logo1 = driver.findElement(By.linkText("Softie Metal Shop"));
    WebElement search1 = driver.findElement(By.cssSelector(".search-field"));
    Assertions.assertTrue(logo1.isDisplayed() && search1.isDisplayed());
    }

}




