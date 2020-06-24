package ApiTests;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;
public class Swagger {

    String APIUrl = "http://localhost:8080/challenge";

    @Test
    public void getListOfUserNamesFromDB() {

        given()
                .when().get(APIUrl + "/clients")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200).log().all()
                .body(notNullValue());
    }

    @Test
    public void checkSpecificUserInTheList() {
        Users expectedUser = new Users();
        expectedUser.setusername("Bob");
        expectedUser.setfullname("Carry");

        given()
                .when().get(APIUrl + "/clients")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200).log().all()
                .body("clients", hasItems(expectedUser.username, expectedUser.fullname));
    }

    @Test
    public void createNewClient() {

        String requestBody = "{\n" +
                "  \"fullName\": \"string\",\n" +
                "  \"username\": \"string\"\n" +
                "}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(APIUrl + "/clients")
                .then()
                //.contentType(ContentType.JSON)
                .statusCode(200).log().all()
                .body(notNullValue());
    }

    @Test
    public void checkHelloMessageForUnauthorizedUser() {
        given().
                when().
                get(APIUrl + "/hello")
                .then().contentType(ContentType.JSON)
                .statusCode(401).log().all()
                .body(notNullValue());

    }

    @Test
    public void checkHelloMessageForAuthorizedUser() {
        String requestBody = "{\"username\":\"string\"}";
        Response response = given().contentType(ContentType.JSON).body(requestBody).when().post(APIUrl + "/login");
        String session = response.getHeader("X-Session-Id");
        System.out.println(session);

        given().header("X-Session-Id", session).
                when().
                get(APIUrl + "/hello")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200).log().all()
                .body("resultCode", is("Ok"), "message", is("Hello, string!"));
    }

    @Test
    public void testClientSessionIdLogin() {

        String requestBody = "{\"username\":\"string\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(APIUrl + "/login")
                .then()
                .statusCode(200).log().all()
                .body(notNullValue());
    }

    @Test
    public void testClientLogOut() {

        String requestBody = "{\"username\":\"string\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(APIUrl + "/logout")
                .then()
                .statusCode(200).log().all()
                .body(notNullValue());
    }
}