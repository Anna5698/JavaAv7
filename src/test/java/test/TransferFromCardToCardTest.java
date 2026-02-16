package test;

import com.codeborne.selenide.Selenide;
import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.PersonalAccountPage;

import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransferFromCardToCardTest {

    @BeforeEach
    void setUpAll() {
        var user = getAuthUser();
        var verificationCode = DataHelper.getverificationCode(user);

        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.loginUser(user);
        var personalAcoountPage = verificationPage.verificationUser(verificationCode);
        reverseTransaction();
    }

    private void reverseTransaction() {
        var personalAcoountPage = new PersonalAccountPage();
        int currentBalanceFirstCard = personalAcoountPage.getBalanceCard(getFirstCardInfo().getCardId());

        String numberFirstCardWhere = personalAcoountPage.getNumberCard(getFirstCardInfo().getCardId());
        String numberSecondCardWhere = personalAcoountPage.getNumberCard(getSecondCardInfo().getCardId());

        if (currentBalanceFirstCard != 10_000){
            int sum = currentBalanceFirstCard - 10_000;
            if (sum > 0) {
                var replenishCardPage = personalAcoountPage.getReplenishCard(getSecondCardInfo().getCardId());
                var transfer = replenishCardPage.getMoneyTransfer(String.valueOf(sum),getFirstCardInfo().getNumber(), numberSecondCardWhere);
            } else {
                var replenishCardPage = personalAcoountPage.getReplenishCard(getFirstCardInfo().getCardId());
                var transfer = replenishCardPage.getMoneyTransfer(String.valueOf(-sum),getSecondCardInfo().getNumber(), numberFirstCardWhere);
            }
        }
        clearTransactionForms();
    }

    private void clearTransactionForms() {
        var personalAccountPage = new PersonalAccountPage();
        var replenishCardPage = personalAccountPage.getReplenishCard(getFirstCardInfo().getCardId());
        replenishCardPage.clearField();
        replenishCardPage.buttonCancel();
        new PersonalAccountPage();
    }

    @Test
    @DisplayName("Успешный перевод с карты 0001 на карту 0002")
    public void shouldTransferFromTheFirstCardToTheSecond() {

        var personalAcoountPage = new PersonalAccountPage();

        int balanceFirstCard = personalAcoountPage.getBalanceCard(getFirstCardInfo().getCardId());
        int balanceSecondCard = personalAcoountPage.getBalanceCard(getSecondCardInfo().getCardId());
        String numberFirstCardWhere = personalAcoountPage.getNumberCard(getFirstCardInfo().getCardId());

        var replenishCardPage =  personalAcoountPage.getReplenishCard(getFirstCardInfo().getCardId());
        var transfer = replenishCardPage.getMoneyTransfer("1000",getSecondCardInfo().getNumber(), numberFirstCardWhere);

        int balanceFirstCardAfterReplenishment = personalAcoountPage.getBalanceCard(getFirstCardInfo().getCardId());
        int balanceSecondCardAfterReplenishment = personalAcoountPage.getBalanceCard(getSecondCardInfo().getCardId());
        assertEquals(balanceFirstCard + 1000, balanceFirstCardAfterReplenishment);
        assertEquals(balanceSecondCard - 1000, balanceSecondCardAfterReplenishment);
    }

    @Test
    @DisplayName("Перевод суммы больше баланса на карте: ожидаем сообщение об ошибке")
    public void shouldShowErrorWhenTransferAmountExceedsBalance() {
        var personalAcoountPage = new PersonalAccountPage();

        String numberSecondCardWhere = personalAcoountPage.getNumberCard(getSecondCardInfo().getCardId());
        var replenishCardPage =  personalAcoountPage.getReplenishCard(getSecondCardInfo().getCardId());

        var transfer = replenishCardPage.getMoneyTransfer("12000",getFirstCardInfo().getNumber(), numberSecondCardWhere);
        replenishCardPage.getErrorMsg("Ошибка!");
    }

    @Test
    @DisplayName("Перевод суммы на не существующую карту: ожидаем сообщение об ошибке")
    public void shouldShowErrorWhenTransferToANonExistentCard() {
        var personalAcoountPage = new PersonalAccountPage();

        String numberSecondCardWhere = personalAcoountPage.getNumberCard(getSecondCardInfo().getCardId());
        var replenishCardPage =  personalAcoountPage.getReplenishCard(getSecondCardInfo().getCardId());

        var transfer = replenishCardPage.getMoneyTransfer("2000", String.valueOf(RandomCardInfo.generateCardInfo("ru").getNumber()), numberSecondCardWhere);
        replenishCardPage.getErrorMsg("Ошибка!");
    }

    @Test
    @DisplayName("Перевод c карты 0001 на карту 0001: ожидаем сообщение об ошибке")
    public void shouldShowErrorWhenTransferToTheSameCard() {
        var personalAcoountPage = new PersonalAccountPage();

        String numberFirstCardWhere = personalAcoountPage.getNumberCard(getFirstCardInfo().getCardId());
        var replenishCardPage =  personalAcoountPage.getReplenishCard(getFirstCardInfo().getCardId());

        var transfer = replenishCardPage.getMoneyTransfer("2000",getFirstCardInfo().getNumber(), numberFirstCardWhere);
        replenishCardPage.getErrorMsg("Ошибка!");
    }

}