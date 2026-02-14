package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Should transfer money from first card to second")
    void shouldTransferMoneyFromFirstToSecond() {
        var firstCardId = dashboardPage.getCardIdByNumber(DataHelper.getFirstCardNumber());
        var secondCardId = dashboardPage.getCardIdByNumber(DataHelper.getSecondCardNumber());

        var firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardId);

        var amount = DataHelper.getValidAmount(firstCardBalance);

        var transferPage = dashboardPage.selectCardForTransfer(secondCardId);
        dashboardPage = transferPage.transferMoney(DataHelper.getFirstCardNumber(), String.valueOf(amount));

        var firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalance - amount, firstCardBalanceAfter);
        assertEquals(secondCardBalance + amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should transfer money from second card to first")
    void shouldTransferMoneyFromSecondToFirst() {
        var firstCardId = dashboardPage.getCardIdByNumber(DataHelper.getFirstCardNumber());
        var secondCardId = dashboardPage.getCardIdByNumber(DataHelper.getSecondCardNumber());

        var firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardId);

        var amount = DataHelper.getValidAmount(secondCardBalance);

        var transferPage = dashboardPage.selectCardForTransfer(firstCardId);
        dashboardPage = transferPage.transferMoney(DataHelper.getSecondCardNumber(), String.valueOf(amount));

        var firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalance + amount, firstCardBalanceAfter);
        assertEquals(secondCardBalance - amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should not transfer amount more than balance")
    void shouldNotTransferMoreThanBalance() {
        var firstCardId = dashboardPage.getCardIdByNumber(DataHelper.getFirstCardNumber());
        var secondCardId = dashboardPage.getCardIdByNumber(DataHelper.getSecondCardNumber());

        var firstCardBalance = dashboardPage.getCardBalance(firstCardId);

        var amount = firstCardBalance + 1000;

        var transferPage = dashboardPage.selectCardForTransfer(secondCardId);
        transferPage.transferMoneyInvalid(DataHelper.getFirstCardNumber(), String.valueOf(amount));

        var firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalance, firstCardBalanceAfter);
    }
}
