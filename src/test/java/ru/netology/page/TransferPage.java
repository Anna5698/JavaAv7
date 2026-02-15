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

    /**
     * Заполняет поля формы перевода и нажимает кнопку перевода
     * @param fromCardNumber номер карты отправителя
     * @param amount сумма перевода
     */
    private void fillAndSubmitTransfer(String fromCardNumber, String amount) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
    }

    /**
     * Выполняет успешный перевод денег
     * @param fromCardNumber номер карты отправителя
     * @param amount сумма перевода
     * @return страницу DashboardPage после успешного перевода
     */
    public DashboardPage transferMoney(String fromCardNumber, String amount) {
        fillAndSubmitTransfer(fromCardNumber, amount);
        return new DashboardPage();
    }

    /**
     * Выполняет перевод с ошибкой (например, сумма превышает баланс)
     * @param fromCardNumber номер карты отправителя
     * @param amount сумма перевода
     */
    public void transferMoneyInvalid(String fromCardNumber, String amount) {
        fillAndSubmitTransfer(fromCardNumber, amount);
        checkErrorNotification();
    }

    /**
     * Проверяет наличие и видимость уведомления об ошибке
     */
    public void checkErrorNotification() {
        errorNotification.shouldBe(Condition.visible);
        errorNotification.shouldNotBe(Condition.empty);
    }

    /**
     * Отменяет перевод и возвращается на главную страницу
     * @return страницу DashboardPage после отмены
     */
    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }
}