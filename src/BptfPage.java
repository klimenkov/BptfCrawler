import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Locale;

/**
 * Created by klimenkov on 10.12.2016.
 */
public class BptfPage {
    public String url;
    public String itemName;
    public String itemQuality;

    public double scmPriceUsd;
    public double bptfPrice;

    public double topSellPrice;
    public double topBuyPrice;

    public BptfPage(String url) {
        this.url = url;

        Document document = Loader.tryLoad(url);
        Element infoDiv = document.select("div[data-name]").get(0);

        itemName = infoDiv.attr("data-name");
        itemQuality = infoDiv.attr("data-q_name");

        scmPriceUsd = Pricing.parseUsd(infoDiv.attr("data-p_scm"));
        bptfPrice = Pricing.parseKeysRefs(infoDiv.attr("data-price") + " ref");

        Elements sellBuyDivs = document.select("div.col-md-6");
        Elements sellOrdersLis = sellBuyDivs.get(0).select("li[title]");
        Elements buyOrdersLis = sellBuyDivs.get(1).select("li[title]");

        if ( !sellOrdersLis.isEmpty() ) {
            Element topSellLi = sellOrdersLis.get(0);
            topSellPrice = Pricing.parseKeysRefs(topSellLi.attr("data-listing_price"));
        } else {
            topSellPrice = 0.0;
        }

        if ( !buyOrdersLis.isEmpty() ) {
            Element topBuyLi = buyOrdersLis.get(0);
            topBuyPrice = Pricing.parseKeysRefs(topBuyLi.attr("data-listing_price"));
        } else {
            topBuyPrice = 0.0;
        }
    }

    public double keyPriceFromSelling() {
        return topSellPrice != 0.0 ? Pricing.addComission(scmPriceUsd) / topSellPrice : 0.0;
    }

    public double keyPriceFromBuying() {
        double min = Math.min(bptfPrice, topBuyPrice);

        return min != 0.0 ? scmPriceUsd / min : 0.0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Item Name:\t\t\t");
        stringBuilder.append(itemName);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("Item Quality:\t\t");
        stringBuilder.append(itemQuality);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("SCM Price, $:\t\t");
        stringBuilder.append(scmPriceUsd);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("BPTF Price:\t\t\t");
        stringBuilder.append(bptfPrice);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("SellPriceString:\t");
        stringBuilder.append(topSellPrice);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("BuyPriceString:\t\t");
        stringBuilder.append(topBuyPrice);
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }

    public String toHtmlTr() {
        StringBuilder sb = new StringBuilder();

        sb.append("<tr>");
        sb.append("<td><a href=\"" + url + "\">" + itemName + "</a></td>");
        sb.append("<td>" + itemQuality + "</td>");
        sb.append("<td>" + scmPriceUsd + "</td>");
        sb.append("<td>" + bptfPrice + "</td>");
        sb.append("<td>" + topSellPrice + "</td>");
        sb.append("<td>" + topBuyPrice + "</td>");
        sb.append("<td>" + String.format(Locale.US, "%.3f", keyPriceFromSelling()) + "</td>");
        sb.append("<td>" + String.format(Locale.US, "%.3f", keyPriceFromBuying()) + "</td>");
        sb.append("</tr>");

        return sb.toString();
    }
}
