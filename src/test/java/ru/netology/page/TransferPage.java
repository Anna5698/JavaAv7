package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public TransferPage() {
        amountField.shouldBe(Condition.visible);
    }

    public DashboardPage transferMoney(String fromCardNumber, String amount) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    public void transferMoneyInvalid(String fromCardNumber, String amount) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
        errorNotification.shouldBe(Condition.visible);
    }
}