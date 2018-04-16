package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import tier1.station1.Station1;
import tier1.station2.Station2;
import tier2.BusinessServer;
import tier3.DataServer;

public class Test {

	public static void main(String[] args) throws MalformedURLException, NotBoundException {
		
		DataServer.main(null);
		BusinessServer.main(null);
		Station1.main(null);
		Station2.main(null);
	}

}
