package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Pallet implements Serializable{

	private static final long serialVersionUID = 1L;
	private int registrationNumber;
	private ArrayList<CarPart> parts;
	private String typeOfPart;
	private double maxWeight;

	private final double DEFAULT_MAX_WEIGHT = 500;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getRegistrationNumber() {
		return registrationNumber;
	}

	public ArrayList<CarPart> getParts() {
		return parts;
	}

	public String getTypeOfPart() {
		return typeOfPart;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public Pallet(String typeOfPart) {
		this.registrationNumber = 0;
		this.parts = new ArrayList<>();
		this.typeOfPart = typeOfPart;
		this.maxWeight = DEFAULT_MAX_WEIGHT;
	}

	public Pallet(String typeOfPart, double maxWeight) {
		this.registrationNumber = 0;
		this.parts = new ArrayList<>();
		this.typeOfPart = typeOfPart;
		this.maxWeight = maxWeight;
	}

	public Pallet(int registrationNumber, String typeOfPart, double maxWeight) {
		this.registrationNumber = registrationNumber;
		this.parts = new ArrayList<>();
		this.typeOfPart = typeOfPart;
		this.maxWeight = maxWeight;
	}

	public void setRegistrationNumber(int registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public void addPart(CarPart part) throws IllegalArgumentException {
		int currentWeight = 0;
		for (CarPart carPart : parts) {
			currentWeight += carPart.getWeight();
		}
		if (currentWeight + part.getWeight() < maxWeight) {
			parts.add(part);
			part.setPalletReference(this);
		}
		else {
			throw new IllegalArgumentException("cannot add part to pallet - max weight exceeded");
		}
	}

	public CarPart getNextCarPart() {
		if (parts.size() > 0) {
			return parts.remove(0);
		}
		else {
			throw new IllegalStateException("Pallet is empty");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pallet)) {
			return false;
		}

		Pallet other = (Pallet) obj;
		return this.getRegistrationNumber() == other.getRegistrationNumber();
	}

	@Override
	public String toString() {
		return "class=" + this.getClass().getSimpleName() + ", " +
				"registrationNumber=" + registrationNumber + ", " +
				"typeOfParts=" + typeOfPart + ", " +
				"maxWeight=" + maxWeight + ", " +
				"unitsStored=" + getParts().size();
	}
}
