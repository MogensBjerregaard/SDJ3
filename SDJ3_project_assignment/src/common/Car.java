package common;

import java.io.Serializable;

public class Car implements Serializable{
	private static final long serialVersionUID = 1L;
	private String chassisNumber;
	private double weight;
	private String model;
	private boolean isDismantled;

	public Car(String chassisNumber, double weight, String model) {
		super();
		this.chassisNumber = chassisNumber;
		this.weight = weight;
		this.model = model;
		this.isDismantled = false;
	}

	public Car(String chassisNumber, double weight, String model, boolean isDismantled) {
		super();
		this.chassisNumber = chassisNumber;
		this.weight = weight;
		this.model = model;
		this.isDismantled = isDismantled;
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
	public boolean isDismantled() {
		return isDismantled;
	}
	public void setAsDismantled() {
		isDismantled = true;
	}

	@Override
	public String toString() {
		return "chassisnumber=" + chassisNumber
				+ ", weight=" + weight
				+ ", model=" + model;
	}
}
