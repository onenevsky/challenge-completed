package ApiTests;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;;
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

        given()
                .body(requestBody)
                .when()
                .post(APIUrl + "/clients")
                .then()
                .contentType(ContentType.JSON)
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
        given().header("X-Session-Id", "f9c09c68-b0be-48d6-9812-1565798f45f5").
                when().
                get(APIUrl + "/hello")
                .then().contentType(ContentType.JSON)
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