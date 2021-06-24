package tests;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import reqres_objects.*;
import utils.TestListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@Listeners(TestListener.class)
public class ReqResTest {

    /**
     * Post user test.
     * This method create and post user, then verify status code and 'name' and 'job' fields
     */
    @Test(description = "Create and post user, then verify status code and 'name' and 'job' fields")
    public void postUserTest() {
        User user = User.builder()
            .name("morpheus")
            .job("leader")
            .build();
        given()
            .body(user)
            .header("Content-Type", "application/json")
        .when()
            .post("https://reqres.in/api/users")
        .then()
            .log().all()
            .body("name", equalTo(user.getName()),
                "job", equalTo(user.getJob()))
            .statusCode(201);
    }

    /**
     * Gets list users test.
     * This method get list users, then verify status code and first user 'ID' field
     */
    @Test(description = "Get list users, then verify status code and first user 'ID' field")
    public void getListUsersTest() {
        String body =
        given()
        .when()
            .get("https://reqres.in/api/users?page=2")
        .then()
            .log().all()
            .statusCode(200)
            .extract().body().asString();
        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        System.out.println(usersList);
        Assert.assertEquals(usersList.getData().get(0).getId(), 7);
    }

    /**
     * Gets single user test.
     * This method get user, then verify status code and user 'first name' field
     */
    @Test(description = "Get user, then verify status code and user 'first name' field")
    public void getSingleUserTest() {
        String body =
        given()
        .when()
            .get("https://reqres.in/api/users/2")
        .then()
            .log().all()
            .statusCode(200)
            .extract().body().asString();
        SingleUser singleUser = new Gson().fromJson(body, SingleUser.class);
        Assert.assertEquals(singleUser.getData().getFirstName(), "Janet");
    }

    /**
     * Single user not found test.
     * This method try get single user, then verify status code and that user is missing
     */
    @Test(description = "Try get single user, then verify status code and that user is missing")
    public void singleUserNotFoundTest() {
        given()
        .when()
            .get("https://reqres.in/api/users/23")
        .then()
            .log().all()
            .body(equalTo("{}"))
            .statusCode(404);
    }

    /**
     * Gets list resource test.
     * This method get list resources, then verify status code and fourth resource 'year' field
     */
    @Test(description = "Get list resources, then verify status code and fourth resource 'year' field")
    public void getListResourceTest() {
        String body =
        given()
        .when()
            .get("https://reqres.in/api/unknown")
        .then()
            .log().all()
            .statusCode(200)
            .extract().body().asString();
        ResourceList resourceList = new Gson().fromJson(body, ResourceList.class);
        System.out.println(resourceList);
        Assert.assertEquals(resourceList.getData().get(3).getYear(), 2003);
    }

    /**
     * Gets single resource test.
     * This method get single resource, then verify status code and resource 'name' field
     */
    @Test(description = "Get single resource, then verify status code and resource 'name' field")
    public void getSingleResourceTest() {
        String body =
        given()
        .when()
            .get("https://reqres.in/api/unknown/2")
        .then()
            .log().all()
            .statusCode(200)
            .extract().body().asString();
        SingleResource singleResource = new Gson().fromJson(body, SingleResource.class);
        System.out.println(singleResource);
        Assert.assertEquals(singleResource.getData().getName(), "fuchsia rose");
    }

    /**
     * Single resource not found test.
     * This method try get single resource, then verify status code and that resource is missing
     */
    @Test(description = "Try get single resource, then verify status code and that resource is missing")
    public void singleResourceNotFoundTest() {
        given()
        .when()
            .get("https://reqres.in/api/unknown/23")
        .then()
            .log().all()
            .body(equalTo("{}"))
            .statusCode(404);
    }

    /**
     * Update user via put test.
     * This method create user and replace user data with new ones, then verify status code and new data
     */
    @Test(description = "Create user and replace user data with new ones, then verify status code and new data")
    public void updateUserViaPutTest() {
        User user = User.builder()
            .name("morpheuss")
            .job("leader")
            .build();
        given()
            .body(user)
            .header("Content-Type", "application/json")
        .when()
            .post("https://reqres.in/api/users/2")
        .then()
            .log().all();
        user = User.builder()
            .name("morpheus")
            .job("zion resident")
            .build();
        given()
            .body(user)
            .header("Content-Type", "application/json")
        .when()
            .put("https://reqres.in/api/users/2")
        .then()
            .log().all()
            .body("name", equalTo(user.getName()),
                "job", equalTo(user.getJob()))
            .statusCode(200);
    }

    /**
     * Update user via path test.
     * This method create user and replace user 'job' field with new one, then verify status code and user field
     */
    @Test(description = "Create user and replace user 'job' field with new one, then verify status code and user field")
    public void updateUserViaPathTest() {
        User user = User.builder()
            .name("morpheus")
            .job("leader")
            .build();
        given()
            .body(user)
            .header("Content-Type", "application/json")
        .when()
            .post("https://reqres.in/api/users/2")
        .then()
            .log().all();
        user = User.builder()
            .name(user.getName())
            .job("zion resident")
            .build();
        given()
            .body(user)
            .header("Content-Type", "application/json")
        .when()
            .patch("https://reqres.in/api/users/2")
        .then()
            .log().all()
            .body("name", equalTo(user.getName()),
                "job", equalTo(user.getJob()))
            .statusCode(200);
    }

    /**
     * Delete user.
     * This method delete user and verify status code and that api body is empty
     */
    @Test(description = "Delete user then verify status code and that api body is empty")
    public void deleteUser() {
        given()
        .when()
            .delete("https://reqres.in/api/users/2")
        .then()
            .log().all()
            .body(equalTo(""))
            .statusCode(204);
    }

    /**
     * User register successful test.
     * This method register user and chek status code and 'id' and 'token'
     */
    @Test(description = "Register user then verify status code and 'id' and 'token'")
    public void userRegisterSuccessfulTest() {
        Users users = Users.builder()
            .email("eve.holt@reqres.in")
            .password("pistol")
            .build();
        given()
        .when()
            .contentType(ContentType.JSON)
            .body(users)
            .post("https://reqres.in/api/register")
        .then()
            .log().all()
            .body("$", hasKey("id"),
                "$", hasKey("token"))
            .statusCode(200);
    }

    /**
     * User register unsuccessful test.
     * This method try register without password, then verify status code and error message
     */
    @Test(description = "Try register without password, then verify status code and error message")
    public void userRegisterUnsuccessfulTest() {
        Users users = Users.builder()
            .email("sydney@fife")
            .build();
        given()
        .when()
            .contentType(ContentType.JSON)
            .body(users)
            .post("https://reqres.in/api/register")
        .then()
            .log().all()
            .body("error", equalTo("Missing password"))
            .statusCode(400);
    }

    /**
     * Login successful test.
     * This method login, then verify status code and 'token'
     */
    @Test(description = "Login, then verify status code and 'token'")
    public void loginSuccessfulTest() {
        Users users = Users.builder()
            .email("eve.holt@reqres.in")
            .password("cityslicka")
            .build();
        given()
        .when()
            .contentType(ContentType.JSON)
            .body(users)
            .post("https://reqres.in/api/login")
        .then()
            .log().all()
            .body("$", hasKey("token"))
            .statusCode(200);
    }

    /**
     * Login unsuccessful test.
     * This method try login without password, then verify status code and error message
     */
    @Test(description = "Try login without password, then verify status code and error message")
    public void loginUnsuccessfulTest() {
        Users users = Users.builder()
            .email("peter@klaven")
            .build();
        given()
        .when()
            .contentType(ContentType.JSON)
            .body(users)
            .post("https://reqres.in/api/login")
        .then()
            .log().all()
            .body("error", equalTo("Missing password"))
            .statusCode(400);
    }

    /**
     * Gets delayed response test.
     * This method get users list with delay, then verify status code and third user 'email' field
     */
    @Test(description = "Get users list with delay, then verify status code and third user 'email' field")
    public void getDelayedResponseTest() {
        String body =
        given()
        .when()
            .get("https://reqres.in/api/users?delay=3")
        .then()
            .log().all()
            .statusCode(200)
            .extract().body().asString();
        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        Assert.assertEquals(usersList.getData().get(2).getEmail(), "emma.wong@reqres.in");
    }
}