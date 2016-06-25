package com.mycompany.app;

public class Smpv extends FoggerData {
	private int SMPV_num;
	private int String_num;	
	private Measurements measurements;


	public Smpv(int sMPV_num, int string_num, Measurements measurements) {
		SMPV_num = sMPV_num;
		String_num = string_num;
		this.measurements = measurements;
	}

	public int getSMPV_Num() {
		return SMPV_num;
	}

	public void setSMPV_Num(int sMPV_Num) {
		SMPV_num = sMPV_Num;
	}

	public int getString_num() {
		return String_num;
	}

	public void setString_num(int string_num) {
		String_num = string_num;
	}

	public Measurements getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Measurements measurements) {
		this.measurements = measurements;
	}


}
