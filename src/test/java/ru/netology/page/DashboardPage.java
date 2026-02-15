package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

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
}