package data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StockConverter {
	private static Map<String,Integer> names = new HashMap<String,Integer>(40);
	private static Map<Integer,String> ids = new HashMap<Integer,String>(40);
		
	/**
	 * creates a fast one-to-one ratio between Strings and Integers
	 * @param stock, the stock name you would like to have a mapping to an integer
	 * @return the integer mapping
	 */
	public static int register(String stock) {
		if (names.containsKey(stock)) {
			return names.get(stock);
		} else {
			Integer index = new Integer(names.size());
			updateHash(stock, index);
			return index.intValue();
		}
	}

	/**
	 * update at the same time to guarantee 1 to 1 mapping
	 * @param stock, the name of the stock
	 * @param index, the integer
	 */
	private static void updateHash(String stock, Integer index) {
		names.put(stock, index);
		ids.put(index, stock);
	}
	
	public static int getId(String stock) {
		return names.get(stock).intValue();
	}

	public static Set<Integer> getStockIds() {
		return ids.keySet();
	}
	
	public static String getName(Integer id) {
		return ids.get(id);
	}
}
