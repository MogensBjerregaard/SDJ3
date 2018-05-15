package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import tier1.carregistrationclient.Station1;
import tier1.dismantlingstationclient.Station2;
import tier1.productpackagingclient.Station3;
import tier2.businessserver.BusinessServer;

public class Test {

	public static void main(String[] args) throws MalformedURLException, NotBoundException, InterruptedException {

		tier3.Main.main(null);
		BusinessServer.main(null);
//		Webserver.main(null);
		Station1.main(null);
		Station2.main(null);
		Station3.main(null);
	}

}
