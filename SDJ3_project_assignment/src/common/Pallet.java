package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Pallet implements Serializable{

	private static final long serialVersionUID = 1L;
	private int registrationNumber;
	private ArrayList<CarPart> parts;
	private String typeOfPart;
	private double maxWeight;

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

	public Pallet(String typeOfPart, double maxWeight) {
		super();
		this.registrationNumber = 0;
		this.parts = new ArrayList<>();
		this.typeOfPart = typeOfPart;
		this.maxWeight = maxWeight;
	}

	public void setRegistrationNumber(int registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public void addParts(CarPart part) {
		parts.add(part);
	}

	@Override
	public String toString() {
		if(typeOfPart.equals("Steeringwheels")) return "#"+registrationNumber+": StWheel "+parts.size()+"p";
		return "#"+registrationNumber+": "+typeOfPart.substring(0, typeOfPart.length()-1)+" "+parts.size()+"p";
	}
}
