package common;

import java.io.Serializable;

public class CarPart implements Serializable{
	private static final long serialVersionUID = 1L;
	private String registrationNumber;
	private Car car;
	private double weight;
	private String type;
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

}
