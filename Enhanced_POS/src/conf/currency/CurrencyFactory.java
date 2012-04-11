package conf.currency;

public class CurrencyFactory {
	public enum Country { HK, US };
	public static Currency createCurrency(Country _country) {
		if (_country == Country.HK) {
			return new HKCurrency();
		} else if (_country == Country.US) {
			return new USCurrency();
		}
		//default currency
		return new HKCurrency();
	}

}
