package ru.netology.Data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {}
    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String generateLoginRandom () {
        var login = faker.name().username();
        return login;
    }

    public static String generatePasswordRandom () {
        var password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto generateUser(String status) {
            var user = new RegistrationDto (generateLoginRandom(), generatePasswordRandom(), status);
            return user;
        }

        public static RegistrationDto generateRegisteredUser (String status){
            var registeredUser = generateUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
