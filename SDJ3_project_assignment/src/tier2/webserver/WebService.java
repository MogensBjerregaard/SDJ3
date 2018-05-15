package tier2.webserver;

import common.Product;

public interface WebService {
	Product[] getProducts(String chassisNumber);
}
