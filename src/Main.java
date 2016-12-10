/**
 * Created by klimenkov on 10.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        final String url = "http://backpack.tf/stats/Unique/Festive%20Revolver/Tradable/Craftable";
        BptfPage bptfPage = new BptfPage(url);

        System.out.println(bptfPage);
    }
}
