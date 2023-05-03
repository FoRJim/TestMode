package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

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
        var notRegisteredUser = generateUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void showLoggedInUserWithWrongPassword() {
        var registeredUser = generateRegisteredUser("active");
        var notRegisteredPassword = generateUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredPassword.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void showBlockedUser() {
        var blockedUser = generateRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    void loginWithWrongUsername() {
        var invalidLogin = generateUser("active");
        var registeredPassword = generateRegisteredUser ("active");
        $("[data-test-id=login] input").setValue(invalidLogin.getLogin());
        $("[data-test-id=password] input").setValue(registeredPassword.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    void removedExtraCharactersInTheLogin() {
        var invalidLogin = generateRegisteredUser("active");
        $("[data-test-id=login] input").setValue(invalidLogin.getLogin()).sendKeys( Keys.INSERT + "4");
        $("[data-test-id=password] input").setValue(invalidLogin.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}