package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private String cardSelector = "[data-test-id='%s']";
    private String balanceSelector = "[data-test-id='%s'] .list__item div";

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public int getCardBalance(String cardId) {
        var balanceText = $(String.format(balanceSelector, cardId)).text();
        return extractBalance(balanceText);
    }

    private int extractBalance(String text) {
        var parts = text.split(":");
        if (parts.length > 1) {
            var balancePart = parts[1].trim();
            return Integer.parseInt(balancePart.replaceAll("[^0-9]", ""));
        }
        return 0;
    }

    public TransferPage selectCardForTransfer(String toCardId) {
        $(String.format(cardSelector, toCardId)).find("button").click();
        return new TransferPage();
    }

    public String getCardIdByNumber(String cardNumber) {
        if (cardNumber.equals(DataHelper.getFirstCardNumber())) {
            return "92df3f1c-a033-48e6-8390-206f6b1f56c0";
        } else if (cardNumber.equals(DataHelper.getSecondCardNumber())) {
            return "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
        }
        return "";
    }
}