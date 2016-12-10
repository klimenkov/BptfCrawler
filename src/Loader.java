import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by klimenkov on 10.12.2016.
 */
public class Loader {
    public static List<String> getAllUrls() {
        String spreadsheetUrl = "https://backpack.tf/spreadsheet";
        String bptfUrl = "https://backpack.tf";

        Document doc = tryLoad(spreadsheetUrl);
        Elements nodes = doc.select("a.qlink");

        return nodes.stream()
                .map(node -> bptfUrl + node.attr("href"))
                .collect(Collectors.toList());
    }

    public static Document tryLoad(String url) {
        boolean loadSuccess = false;
        Document document = null;

        while (!loadSuccess) {
            try {
                document = Jsoup.connect(url).userAgent("Chrome").maxBodySize(0).get();
                loadSuccess = true;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return document;
    }
}
