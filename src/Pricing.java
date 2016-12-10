import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by klimenkov on 10.12.2016.
 */
public class Pricing {
    private static final double REF_PER_KEY = 28.0;

    public static double parseUsd(String usdString) {
        try {
            return NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .parse(usdString)
                    .doubleValue();
        } catch ( ParseException e ) {
            return 0.0;
        }
    }

    public static double parseKeysRefs(String keysRefsString) {
        String[] priceStrings = keysRefsString.split(", ");
        double refPrice = 0.0;

        for ( String priceString : priceStrings ) {
            String[] tokens = priceString.split(" ");
            String valueString = tokens[0];
            String currency = tokens[1];
            double value = Double.parseDouble(valueString);

            switch ( currency ) {
                case "ref":
                    refPrice += value / REF_PER_KEY;
                    break;
                case "key":
                case "keys":
                    refPrice += value;
                    break;
            }
        }

        return refPrice;
    }

    public static double addComission(double price) {
        return Math.floor(price * 87.1 + 0.4) / 100;
    }
}
