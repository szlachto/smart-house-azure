package com.mycompany.app;

public class Measurements {
	private float E;
	private CmpVal Ich;
	private CmpVal Kch;
	private CmpVal Pch;
	private CmpVal Temp;
	private CmpVal Uch;

	public Measurements(float e, CmpVal ich, CmpVal kch, CmpVal pch, CmpVal temp, CmpVal uch) {
		
		setE(e);
		setIch(ich);
		setKch(kch);
		setPch(pch);
		setTemp(temp);
		setUch(uch);
	}

	public float getE() {
		return E;
	}

	public void setE(float e) {
		E = e;
	}

	public CmpVal getIch() {
		return Ich;
	}

	public void setIch(CmpVal ich) {
		Ich = ich;
	}

	public CmpVal getKch() {
		return Kch;
	}

	public void setKch(CmpVal kch) {
		Kch = kch;
	}

	public CmpVal getPch() {
		return Pch;
	}

	public void setPch(CmpVal pch) {
		Pch = pch;
	}

	public CmpVal getTemp() {
		return Temp;
	}

	public void setTemp(CmpVal temp) {
		Temp = temp;
	}

	public CmpVal getUch() {
		return Uch;
	}

	public void setUch(CmpVal uch) {
		Uch = uch;
	}
}
