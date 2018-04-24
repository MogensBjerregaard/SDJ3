/**
 * WebServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.7  Built on : Nov 20, 2017 (11:41:20 GMT)
 */
package tier2.webserver.skeleton;

import tier2.webserver.GetProductsResponse;
import tier2.webserver.WebserverController;

/**
 *  WebServiceSkeleton java skeleton for the axisService
 */
public class WebServiceSkeleton implements WebServiceSkeletonInterface {
	private WebserverController controller;
	public WebServiceSkeleton() {
		super();
		controller = WebserverController.getInstance();
	}
    public tier2.webserver.GetProductsResponse getProducts(
    	
        tier2.webserver.GetProducts getProducts0) {
    	String chassisNumber = getProducts0.getArgs0();
    	common.xsd.Product[] matchingProducts = controller.getMatchingProducts(chassisNumber);
	
        GetProductsResponse response = new GetProductsResponse();
        response.set_return(matchingProducts);        
        return response;
    }

}
