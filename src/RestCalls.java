import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;



public class RestCalls {
    static List<String> id;

    @Test
    public void APICallMethod() {
        RestAssured.baseURI = "https://restool-sample-app.herokuapp.com/api/character";
        RestAssured.basePath = "?search=";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);
        System.out.println(response.asString());
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        JsonPath jPath = response.jsonPath();
        List<String> idObject = jPath.getJsonObject("id");

        id = jPath.getList("id");
        for (String idFind : idObject) {

            System.out.println(idFind);
        }
    }


    public void compareValues() throws IOException {
        File file = new File("E:\\SeleniumTest\\DataSheet.xls");
        FileInputStream stream = new FileInputStream(file);

        HSSFWorkbook workbook = new HSSFWorkbook(stream);
        HSSFSheet sheet1 = workbook.getSheet("Sheet1");
        List<String> listValue = new ArrayList<String>();

        for (int i = 0; i <= sheet1.getLastRowNum(); i++) {
            HSSFRow rowSheet2 = sheet1.getRow(i);
            if (rowSheet2.getRowNum() > 0) {
                HSSFCell cell = rowSheet2.getCell(0);
                String covertedValue = cell.getStringCellValue();
                listValue.add(covertedValue);
            }
        }

        for (int s = 0; s < listValue.size(); s++) {
            if (id.contains(listValue.get(s))) {
                System.out.println(listValue.get(s) + "  true");
            } else {
                System.out.println(listValue.get(s) + "   False");
            }
        }

    }

    @Test
    public void compareWithUI() {
        System.setProperty("webdriver.chrome.driver", "C:\\Jarfiles\\jarchrome\\chromedriver.exe");


        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://restool-sample-app.herokuapp.com/#/characters");
        driver.manage().window().maximize();

        List<WebElement> divAll = driver.findElements(By.xpath("//table[@class='pure-table pure-table-horizontal'] /tbody/ tr/ td[2]/div"));

        List<String> idValue = new ArrayList();

        for (int y = 0; y < divAll.size(); y++) {
            idValue.add(divAll.get(y).getText());
        }
        System.out.println(idValue);


        for (int s = 0; s < idValue.size(); s++) {
            if (id.contains(idValue.get(s))) {
                System.out.println(idValue.get(s) + "  true");
            } else {
                System.out.println(idValue.get(s) + "   False");
            }
        }


    }

    public void APIPostMethod() {
        RestAssured.baseURI = "https://restool-sample-app.herokuapp.com/api/character?search=";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "TestNew"); // Cast
        requestParams.put("thumbnail", "");
        requestParams.put("location", "Winterfell");
        requestParams.put("realName", "Test");
        requestParams.put("isAlive", "true");
        requestParams.put("id", "xx34546");
        request.body(requestParams.toString());
        Response response = request.post("https://restool-sample-app.herokuapp.com/api/character?search=");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "200");
        String successCode = response.jsonPath().get("SuccessCode");
        Assert.assertEquals("Correct Success code was returned", successCode, "OPERATION_SUCCESS");


    }



    public void getCalls() {
        RestAssured.baseURI = "http://restcountries.eu";
        String countryName = "India";
        Response response = given()
                .pathParam("country", countryName)
                .when()
                .get("/rest/v1/name/{country}")
                .then().extract().response();

        String verifyValue = response.asString();

        System.out.println(verifyValue);

        JsonPath jPath1 = response.jsonPath();
        List<String> jb = jPath1.getJsonObject("capital");

        System.out.println(jb);
        List<String> expecOut = new ArrayList<String>();
        expecOut.add("Diego Garcia");
        expecOut.add("New Delhi");

        for (int i = 0; i < expecOut.size(); i++) {
            Assert.assertTrue(jb.get(i).contains(expecOut.get(i)));
        }


    }


}
