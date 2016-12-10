import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by klimenkov on 10.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        List<String> urls = Loader.getAllUrls();

        PriorityQueue<BptfPage> sellingSet = new PriorityQueue<>(new SellingComparator());
        PriorityQueue<BptfPage> buyingSet = new PriorityQueue<>(new BuyingComparator());

        for (String url : urls) {
            BptfPage page = new BptfPage(url);
            System.out.println(url);

            if (page.keyPriceFromSelling() != 0.0) {
                sellingSet.offer(page);
            }
            if (page.keyPriceFromBuying() != 0.0) {
                buyingSet.offer(page);
            }
        }

        try (PrintWriter sellstxt = new PrintWriter("Selling Set.html");
             PrintWriter buystxt = new PrintWriter("Buying Set.html")) {
            sellstxt.println(toHtmlTable(sellingSet));
            buystxt.println(toHtmlTable(buyingSet));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String toHtmlTable(PriorityQueue<BptfPage> pages) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        sb.append("th, td { padding: 2px; text-align: left; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table style=\"width:100%\" border=\"1\">");
        sb.append("<tr>");
        sb.append("<th>Name</th>");
        sb.append("<th>Quality</th>");
        sb.append("<th>SCM Price, $</th>");
        sb.append("<th>BPTF Price, Key</th>");
        sb.append("<th>Top Sell Price, Key</th>");
        sb.append("<th>Top Buy Price, Key</th>");
        sb.append("<th>Key Price From Selling</th>");
        sb.append("<th>Key Price From Buying</th>");
        sb.append("</tr>");
        while (!pages.isEmpty()) {
            sb.append(pages.poll().toHtmlTr());
        }
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}

class SellingComparator implements Comparator<BptfPage> {

    @Override
    public int compare(BptfPage p1, BptfPage p2) {
        return Double.compare(p2.keyPriceFromSelling(), p1.keyPriceFromSelling());
    }
}

class BuyingComparator implements Comparator<BptfPage> {

    @Override
    public int compare(BptfPage p1, BptfPage p2) {
        return Double.compare(p1.keyPriceFromBuying(), p2.keyPriceFromBuying());
    }
}