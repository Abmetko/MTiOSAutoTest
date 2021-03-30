package core.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import static io.restassured.RestAssured.given;


public class APIClient {
    /**
     * browserstack API
     **/
    public static void passTestStatus(String sessionId, String status, String reason) {
        Response response = given().auth()
                .basic(PropertyLoader.getProperty("browserstack.login"), PropertyLoader.getProperty("browserstack.password"))
                .header("Content-Type", "application/json")
                .body("{\"status\":\"" + status + "\", \"reason\":\"" + reason + "\"}")
                .when()
                .put("https://api-cloud.browserstack.com/app-automate/sessions/" + sessionId + ".json");
        response.then()
                .statusCode(200);
    }

    public static String getApplicationData(String app_url) {
        Response response = given().auth()
                .basic(PropertyLoader.getProperty("browserstack.login"), PropertyLoader.getProperty("browserstack.password"))
                .when()
                .get("https://api-cloud.browserstack.com/app-automate/recent_apps");
        response.then().extract().response();
        JsonPath path = response.jsonPath();
        List<HashMap<String, Object>> data = path.getList("");
        for (HashMap<String, Object> singleObject : data) {
            if (singleObject.get("app_url").equals(app_url)) {
                return (String) singleObject.get("app_version");
            }
        }
        return null;
    }

    public static String getToken(){
        Response response = given()
                .header("Content-Type","application/json")
                .body("{\n" +
                        "\t\"email\": \"" + PropertyLoader.getProperty("user.email") + "\",\n" +
                        "\t\"password\": \"" + PropertyLoader.getProperty("user.password") + "\",\n" +
                        "\t\"iframe\": true,\n" +
                        "\t\"customerPlatformData\": {\n" +
                        "\t\t\"timeZone\": \"+03:00\",\n" +
                        "\t\t\"device\": \"iOS\"\n" +
                        "\t}\n" +
                        "}\n")
                .when()
                .post("https://globaltradeatf.crmarts.com/client-area/api/accounts/login");
        response.then().statusCode(200);
        return response.jsonPath().get("redirectUrl").toString().split("token=")[1];
    }

//    public static String getUserAccessToken(){
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body("{\"login\":7102210,\"token\":\"" + getToken() + "\"}")
//                .when()
//                .post("https://api-mobile-live-globaltradeatf.crmarts.com/fms/oauth/sso");
//        response.then().statusCode(200);
//        return response.jsonPath().get("access_token");
//    }

    public static String getUserAccessToken(){
        String body = String.format("grant_type=%s&username=%s&password=%s", "password", "7102210", "Qwerty1");
        Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("authorization","Basic ODg4OTk5MDE2OnF3ZXJ0eTE2==")
                .header("grant_type","password")
                .body(body)
                .when()
                .post("https://api-mobile-live-globaltradeatf.crmarts.com/fms/auth/oauth/token");
        response.then().statusCode(200);
        return response.jsonPath().get("access_token");
    }

    public static int getAssetLeverage(){
        Response response = given().auth().oauth2(getUserAccessToken())
                .header("Content-Type", "application/json")
                .body("{\"symbol\":\"" + PropertyLoader.getProperty("instrument.set") + "\"}")
                .when()
                .post("https://api-mobile-live-globaltradeatf.crmarts.com/fms/leverage");
        response.then().statusCode(200);
        return Math.round(response.jsonPath().get("leverage"));
    }

    public static void main(String[] args) {
//        System.out.println(getUserAccessToken());
        String volume = "0.09996";
        System.out.println(Double.parseDouble(volume));
    }
}

/*
[
  {
    "app_name": "InvestLite.ipa",
    "app_version": "1.52.0",
    "app_url": "bs://271928e3447813390b52a979734b524c9fdbd414",
    "app_id": "271928e3447813390b52a979734b524c9fdbd414",
    "uploaded_at": "2021-02-26 19:37:11 UTC"
  }
]
*/