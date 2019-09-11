import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostMethods {
    @Test
    public void PutMethod() {

        String APIUrl = "https://restool-sample-app.herokuapp.com/api/character?=search";

        //Initializing payload or API body
        String APIBody = "{\"name\": \"TestNew22\",\"location\": \"Winterfell\", \"isAlive\":  \"true\",\"id\":  \"xx3454634345\"}"; //e.g.- "{\"key1\":\"value1\",\"key2\":\"value2\"}"

        // Building request using requestSpecBuilder
        RequestSpecBuilder builder = new RequestSpecBuilder();


        //Setting API's body
        builder.setBody(APIBody);

        //Setting content type as application/json or application/xml
        builder.setContentType("application/json; charset=UTF-8");

        RequestSpecification requestSpec = builder.build();

        //Making post request with authentication, leave blank in case there are no credentials- basic("","")
        Response response = RestAssured.given().auth().preemptive().basic("", "")
                .spec(requestSpec).when().post(APIUrl);

        JSONObject JSONResponseBody = new JSONObject(response.body().asString());

        //Fetching the desired value of a parameter
        String result = JSONResponseBody.getString("id");

        //Asserting that result of Norway is Oslo
        Assert.assertEquals(result, "xx3454634345");

    }

}
