package common;

import java.io.Serializable;

public class Car implements Serializable{
	private static final long serialVersionUID = 1L;
	private String chassisNumber;
	private double weight;
	private String model;
	public Car(String chassisNumber, double weight, String model) {
		super();
		this.chassisNumber = chassisNumber;
		this.weight = weight;
		this.model = model;
	}
	public String getChassisNumber() {
		return this.chassisNumber;
	}
	public double getWeight() {
		return this.weight;
	}
	public String getModel() {
		return this.model;
	}

}
