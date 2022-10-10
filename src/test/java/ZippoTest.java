import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.TestInstance;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoTest {


    @Test
    public void Test(){

                 given()
                    // hazirlik ilemleri yapilan bolum(token,send body,parametreler)
                .when()
                         // link i ve metodu veriyoruz
                .then()
                 //assertion ve verileri ele alma extract
                ;

    }
    @Test
    public void statusCodeTest(){


        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()       // log.all() butun responsu gosterir
                .body("country",equalTo("United States")) // body.country == Unites States
                        .statusCode(200) //status kontrolu


        ;

    }
    @Test
    public void bodyJsonPathTest(){


        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()       // log.all() butun responsu gosterir
                .body("places[0].state",equalTo("California")) // body.country == Unites States
                .statusCode(200) //status kontrolu


        ;

    }
    @Test
    public void bodyJsonPathTest1(){


        given()


                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()       // log.all() butun responsu gosterir
                .body("places.'place name'",hasItem("Çaputçu Köyü")) // body.country == Unites States
                .statusCode(200) //status kontrolu

        ;

    }
    @Test
    public void bodyArrayHasSizeTest(){


        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places",hasSize(1))
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))

                .statusCode(200)


        ;

    }
    @Test
    public void pathParamTest(){


        given()
                .pathParam("Country","us")
                .pathParam("zipCode","90210")
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{Country}/{zipCode}")

                .then()
                .log().body()
                .statusCode(200)


        ;

    }
    @Test
    public void pathParamTest2(){
// 90210 dan 90250 kadar test sonuçlarında places in size nın hepsinde 1 gediğini test ediniz.

        for (int i = 90210; i <=90213 ; i++) {

                given()
                .pathParam("Country","us")
                .pathParam("zipCode",i)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{Country}/{zipCode}")

                .then()
                .log().body()
                        .body("places",hasSize(1))
                .statusCode(200)


        ;}

    }
    @Test
    public void queryParamTest(){



        given()
                .param("page",1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page",equalTo(1))
                .statusCode(200)


        ;

    }
    @Test
    public void queryParamTest1(){

        for (int pageNo =1; pageNo <=10 ; pageNo++) {


            given()
                    .param("page",pageNo)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page",equalTo(pageNo))
                    .statusCode(200)


            ;}

    }
    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void setup(){

        // RestAssured kendi statik degiskeni tanimli deger ataniyor
        baseURI="https://gorest.co.in/public/v1";
        requestSpecs=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();
        responseSpecs=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

    }
    @Test
    public void requestResponseSpecification(){

        given()
                .param("page",1)
                .spec(requestSpecs)
                .when()
                .get("/users")

                .then()

                .body("meta.pagination.page",equalTo(1))
                .spec(responseSpecs)


        ;

    }

    //Json extract
    @Test
    public void extractingJsonPath(){

        String placeNAme=
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'")
                // extract metodu ile given ile baslayan satir bir deger dondurur hale geldi , en ssonra extract olmali
                ;

        System.out.println("placeNAme = " + placeNAme);
    }
    @Test
    public void extractingJsonPathInt(){
                int limit=
                given()


                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")

                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");


    }
    @Test
    public void extractingJsonPathInt2(){
        int id=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data[2].id")
                ;
        System.out.println("id = " + id);

    }
    @Test
    public void extractingJsonPathInt3(){
        List<Integer> idler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id")// data daki butun idleri liste seklinde verir
                ;
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(3045));
    }
    @Test
    public void extractingJsonPathString2(){
        List<String> nameler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name")// data daki butun idleri liste seklinde verir
                ;
        System.out.println("nameler = " + nameler);

    }
    @Test
    public void extractingJsonPathStringlist2(){
        Response response=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response() // butun bady allindi
                ;


        List<Integer> idler=response.path("data.id");
        List<String> nameler=response.path("data.name");
        int limit=response.path("meta.pagination.limit");
        System.out.println("idler = " + idler);
        System.out.println("nameler = " + nameler);
        System.out.println("limit = " + limit);

    }
    @Test
    public void extractingJsonPOJO(){

        Location yer=
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .extract().as(Location.class);//location sablonu

        ;

        System.out.println("yer.="+yer);
        System.out.println("yer.getCountry() = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " + yer.getPlaces().get(0).getPlacename());

    }
}
