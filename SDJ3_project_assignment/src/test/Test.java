package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class Test {

	public static void main(String[] args)
			throws MalformedURLException, NotBoundException, InterruptedException {
		tier3.Main.main(null);
		tier2.businessserver.Main.main(null);
		tier2.webserver.Main.main(null);
		tier1.carregistrationclient.Main.main(null);
		tier1.dismantlingstationclient.Main.main(null);
		tier1.productpackagingclient.Main.main(null);
	}

}
