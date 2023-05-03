package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.generateRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.generateUser;


public class AuthorizationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void successfulLoginRegisteredUser() {
        var registeredUser = generateRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.text("  Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void getErrorIfNotRegisteredUser() {
        var NotRegisteredUser = generateUser("active");
        $("[data-test-id=login] input").setValue(NotRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(NotRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__title").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
    }

    @Test
    void showLoggedInUserWithWrongPassword() {
        var registeredUser = generateRegisteredUser("active");
        var NotRegisteredPassword = generateUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(NotRegisteredPassword.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__title").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
    }

    @Test
    void showBlockedUser() {
        var blockedUser = generateRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Пользователь заблокирован")).shouldBe(Condition.visible);
    }
}