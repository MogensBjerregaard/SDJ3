package common;

import java.io.Serializable;

public class CarPart implements Serializable{
	private static final long serialVersionUID = 1L;
	private String registrationNumber;
	private Car car;
	private double weight;
	private String type;
	private Pallet palletReference;

	public CarPart(String registrationNumber, Car car, String type, double weight) {
		super();
		this.registrationNumber = registrationNumber;
		this.car = car;
		this.type = type;
		this.weight = weight;
	}
	public String getRegistrationNumber() {
		return this.registrationNumber;
	}

	public Car getCar() {
		return this.car;
	}

	public double getWeight() {
		return this.weight;
	}

	public String getType() {
		return this.type;
	}

	public Pallet getPalletReference() {
		return palletReference;
	}

	public void setPalletReference(Pallet pallet) {
		palletReference = pallet;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CarPart)) {
			return false;
		}

		CarPart other = (CarPart) obj;
		return this.registrationNumber.equals(other.registrationNumber);
	}

	@Override
	public String toString() {
		return "class=" + this.getClass().getSimpleName() + ", " +
				"registrationNumber=" + registrationNumber + ", " +
				"type=" + type + ", " +
				"weight=" + weight + ", " +
				"carChassisNumber=" + getCar().getChassisNumber();
	}
}
