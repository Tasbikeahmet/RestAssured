package Campus;
import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.BeforeClass;

public class CountryTest {
Cookies cookies;
    @BeforeClass
    public void loginCampus(){
        baseURI="https://demo.mersys.io/";

        //{"username": "richfield.edu","password": "Richfield2020!","rememberMe": "true"}
        Map<String,String> credential=new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");
cookies=
        given()
                .contentType(ContentType.JSON)
                .body(credential)
                .when()
                .post("auth/login")
                .then()
                //.log().all()
                .statusCode(200)
                .extract().response().getDetailedCookies()
                ;

    }
String countryID;
String countryName;
String countryCode;
    @Test
    public void creatCountry(){
        countryName=getRandomName();
        countryCode=getRandomcode();
        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);

        countryID=
given()
        .cookies(cookies)
        .contentType(ContentType.JSON)
        .body(country)
        .log().body()
        .when()
        .post("school-service/api/countries")

        .then()
        .log().body()
        .statusCode(201)
        .extract().jsonPath().getString("id")
        ;

    }
    @Test(dependsOnMethods = "creatCountry" )
    public void creatCountryNegatif(){

        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);


                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))

        ;

    }
    @Test(dependsOnMethods = "creatCountry" )
    public void updateCountry(){
        countryName=getRandomName();
        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(countryName))
                ;

    }
    @Test(dependsOnMethods = "updateCountry" )
    public void deleteCountryById(){



        given()
                .cookies(cookies)
                .pathParam("countryID",countryID)

                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .statusCode(200)

        ;

    }
    @Test(dependsOnMethods = "deleteCountryById" )
    public void deleteCountryByIdNegatif(){



        given()
                .cookies(cookies)
                .pathParam("countryID",countryID)

                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .statusCode(400)

        ;

    }
    @Test(dependsOnMethods = "deleteCountryById" )
    public void updateCountryNegatif(){
        countryName=getRandomName();
        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("school-service/api/countries")

                .then()

                .statusCode(400)
                .body("message",equalTo("Country not found"))

        ;

    }

public String getRandomName(){

        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
}

    public String getRandomcode(){

        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }
}
