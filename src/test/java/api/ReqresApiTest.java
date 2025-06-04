package api;

import com.codeborne.selenide.As;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class ReqresApiTest {

    private final static String URL = "https://reqres.in";

    @Test
    public void listUserTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        List<UserPojo> users = given()
                .when()
                .get("/api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserPojo.class);

        users.forEach(x->Assert.assertTrue(x.getEmail().contains("reqres.in")));
    }

    @Test
    public void singleUserTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        UserPojo userPojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/users/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", UserPojo.class);

        Assert.assertEquals("Janet", userPojo.getFirst_name());
    }

    @Test
    public void singleUserNotFoundTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecUnique(404));
        UserPojo userPojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/users/23")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", UserPojo.class);
        
        Assert.assertNull(userPojo);
    }

    @Test
    public void listResourceTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        List<ResourcePojo> resourcePojos = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ResourcePojo.class);

        boolean response = IntStream.range(1 , resourcePojos.size() - 1)
                .allMatch(x -> resourcePojos.get(x).id <= resourcePojos.get(x + 1).id);
        Assert.assertTrue(response);
    }

    @Test
    public void singleResourceTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        ResourcePojo resourcePojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/unknown/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", ResourcePojo.class);

        Assert.assertEquals("#C74375" , resourcePojo.getColor());
    }

    @Test
    public void singleResourceNotFoundTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecUnique(404));
        ResourcePojo resourcePojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/unknown/23")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", ResourcePojo.class);

        Assert.assertNull(resourcePojo);
    }

    @Test
    public void createTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecUnique(201));
        AddCreatePojo addCreatePojo = new AddCreatePojo("morpheus", "leader");

        CreatePojo createPojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(addCreatePojo)
                .post("/api/users")
                .then().log().all()
                .extract().body().as(CreatePojo.class);

        LocalDate responseDate = ZonedDateTime.parse(createPojo.getCreatedAt()).toLocalDate();
        LocalDate today = LocalDate.now();

        Assert.assertEquals(today, responseDate);
    }

    @Test
    public void updatePutTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        NewUpdatePojo newUpdatePojo = new NewUpdatePojo("morpheus", "zion resident");

        UpdatePojo updatePojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(newUpdatePojo)
                .put("/api/users/2")
                .then().log().all()
                .extract().body().as(UpdatePojo.class);

        LocalDate responseDate = ZonedDateTime.parse(updatePojo.getUpdatedAt()).toLocalDate();
        LocalDate today = LocalDate.now();

        Assert.assertEquals(today, responseDate);
    }

    @Test
    public void updatePatchTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        NewUpdatePojo newUpdatePojo = new NewUpdatePojo("morpheus", "zion resident");

        UpdatePojo updatePojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(newUpdatePojo)
                .patch("/api/users/2")
                .then().log().all()
                .extract().body().as(UpdatePojo.class);

        LocalDate responseDate = ZonedDateTime.parse(updatePojo.getUpdatedAt()).toLocalDate();
        LocalDate today = LocalDate.now();

        Assert.assertEquals(today, responseDate);
    }

    @Test
    public void deleteTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecUnique(204));
        given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .delete("/api/users/2")
                .then().log().all();
    }

    @Test
    public void registerSuccessfulTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        NewRegisterSPojo newRegisterSPojo = new NewRegisterSPojo("eve.holt@reqres.in", "pistol");

        RegisterSPojo registerSPojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(newRegisterSPojo)
                .post("/api/register")
                .then().log().all()
                .extract().body().as(RegisterSPojo.class);

        Assert.assertEquals("4",registerSPojo.getId());
    }

    @Test
    public void registerUnSuccessfulTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecError400());
        NewRegisterSPojo newRegisterSPojo = new NewRegisterSPojo("eve.holt@reqres.in", "");

        RegisterUSPojo registerUSPojo = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(newRegisterSPojo)
                .post("/api/register")
                .then().log().all()
                .extract().body().as(RegisterUSPojo.class);

        Assert.assertEquals("Missing password", registerUSPojo.getError());
    }

    @Test
    public void loginSuccessfulTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecOK200());
        NewRegisterSPojo registerSPojo = new NewRegisterSPojo("eve.holt@reqres.in", "cityslicka");

        Map<String,String> response = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(registerSPojo)
                .post("/api/login")
                .then().log().all()
                .extract().body().jsonPath().getMap("$");

        Assert.assertEquals("QpwL5tke4Pnpja7X4", response.get("token"));
    }

    @Test
    public void loginUnSuccessfulTest(){
        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpecError400());

        Map<String,String> requestEmail = new HashMap<>();
        requestEmail.put("email", "peter@klaven");

        Map<String, String> errorResponse = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .body(requestEmail)
                .post("/api/login")
                .then().log().all()
                .extract().body().jsonPath().getMap("$");

        Assert.assertEquals("Missing password", errorResponse.get("error"));
    }

    @Test
    public void delayResponseTest(){
        List<UserPojo> users = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/users?delay=3")
                .then().log().all()
                .time(lessThan(4000L))
                .extract().body().jsonPath().getList("data", UserPojo.class);

        users.forEach(x->Assert.assertTrue(x.getEmail().contains("reqres.in")));
    }
}

