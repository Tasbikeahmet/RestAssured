package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    @BeforeClass
    void Setup(){

        baseURI="https://gorest.co.in/public/v2/";
    }



int userID=0;

    @Test
    public void createUserObject(){
User newUser=new User();
newUser.setName(getRandomName());
newUser.setGender("male");
newUser.setEmail(getRandomEmail());
newUser.setStatus("active");



        userID=
                given()
                        .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id")
                ;
        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
        System.out.println("userID = " + userID);




    }
    @Test(dependsOnMethods ="createUserObject",priority = 1)
    public void updateUserObject(){
        Map<String,String>updateUser=new HashMap<>();
        updateUser.put("name","Ahmet");





                given()
                        .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                        .contentType(ContentType.JSON)
                        .body(updateUser)
                        .pathParam("userID",userID)
                        .when()

                        .put("users/{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)

                        .body("name",equalTo("Ahmet"))
                ;




    }
    @Test(dependsOnMethods ="createUserObject",priority = 2)
    public void getUserByID(){



        given()
                .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                .contentType(ContentType.JSON)

                .pathParam("userID",userID)
                .when()

                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)

                .body("id",equalTo(userID))
        ;




    }
    @Test(dependsOnMethods ="createUserObject",priority = 3)
    public void deleteUserById(){



        given()
                .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                .contentType(ContentType.JSON)

                .pathParam("userID",userID)
                .when()

                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(204)

        ;


    } @Test(dependsOnMethods ="deleteUserById")
    public void deleteUserByIdNegatif(){



        given()
                .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                .contentType(ContentType.JSON)

                .pathParam("userID",userID)
                .when()

                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;

    }
    @Test
    public void getUsers(){


        Response response=
        given()
                .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")


                .when()

                .get("users")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().response()


        ;
        // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
        int idUser3path=response.path("[2].id");
        int idUser3JsonPaht=response.jsonPath().getInt("[2].id");
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser3JsonPaht = " + idUser3JsonPaht);


        // TODO : Tüm gelen veriyi bir nesneye atınız  (google araştırması)


        User[] userPath=response.as(User[].class);
        System.out.println("userPath = " + userPath);
        List<User> userJsonPath=response.jsonPath().getList("",User.class);
        System.out.println("userJsonPath = " + userJsonPath);
    }
    @Test
    public void getUserByIDExtract(){
// TODO : GetUserByID testinde dönen user ı bir nesneye atınız.

User user=
        given()
                .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                .contentType(ContentType.JSON)

                .pathParam("userID",2307)
                .when()

                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                //.extract().as(User.class)
                .extract().jsonPath().getObject("",User.class)


        ;
        System.out.println("user = " + user);
    }
    @Test
    public void getUsersV1() {


        Response response =
                given()
                        .header("Authorization", "Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")


                        .when()

                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();
        //  response.as() //tum gelen response uygun nesnelerin yapilmasi gerekiyor
        List<User> dataUser=response.jsonPath().getList("data",User.class);//JSONPATH bir respon icindeki bir parcayi
                                                                                // nesneye donusturebiliriz
        System.out.println("dataUser = " + dataUser);
        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
    @Test(enabled = false)
    public void createUser(){
        int userID=
                given()
                        .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
        System.out.println("userID = " + userID);




    }
    public String getRandomName(){

        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomEmail(){

        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
    }
    @Test(enabled = false)
    public void createUserMap(){

        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",getRandomName());
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");

        int userID=
                given()
                        .header("Authorization","Bearer de4142163a02a6edcfc7d48f61f1cb9cb891eb4e7e9aab51ee4f39a78d35f81d")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
        System.out.println("userID = " + userID);

    }

}
