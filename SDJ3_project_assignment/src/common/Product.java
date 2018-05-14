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
		this.registrationNumber = registrationNumber;
		this.productType = productType;
		this.palletReferences = new HashSet<>();
		this.parts = new ArrayList<>();
	}
	public HashSet<Pallet> getPalletReferences() {
		return palletReferences;
	}
	public void addPalletReference(Pallet pallet) {
		palletReferences.add(pallet);
	}
	public void setPalletReferences(HashSet<Pallet> palletReferences) {
		this.palletReferences = palletReferences;
	}
	public ArrayList<CarPart> getParts() {
		return parts;
	}
	public void addPart(CarPart part) {
		parts.add(part);
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
	public String toString() {
		return "class=" + this.getClass().getSimpleName() +
				", registrationNumber=" + registrationNumber +
				", productType=" + productType +
				", numberOfParts=" + parts.size() +
				", numberOfPalletReferences=" + palletReferences.size();
	}
}
