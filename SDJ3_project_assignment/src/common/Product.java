package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	private int registrationNumber;
	private String productType;
	private HashSet<Pallet> palletReferences;
	private ArrayList<CarPart> parts;
	public Product(int registrationNumber, String productType) {
		super();
		this.registrationNumber = registrationNumber;
		this.productType = productType;
		this.palletReferences = new HashSet<>();
		this.parts = new ArrayList<>();
	}
	public HashSet<Pallet> getPalletReferences() {
		return palletReferences;
	}
	public void setPalletReferences(HashSet<Pallet> palletReferences) {
		this.palletReferences = palletReferences;
	}
	public ArrayList<CarPart> getParts() {
		return parts;
	}
	public void setParts(ArrayList<CarPart> parts) {
		this.parts = parts;
	}
	public int getRegistrationNumber() {
		return registrationNumber;
	}
	public String getProductType() {
		return productType; 
	}
	
}
