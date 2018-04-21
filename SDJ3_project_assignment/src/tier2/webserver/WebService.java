package tier2.webserver;

import common.Product;

public interface WebService {
	public Product[] getProducts(String chassisNumber);
}
