package com.mycompany.app;

public class FoggerEntity {

	private String entity;
	private FoggerData fields;
	private Tags tags;

	public FoggerEntity(String entity, FoggerData fields, Tags tags) {
		super();
		this.entity = entity;
		this.fields = fields;
		this.tags = tags;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public FoggerData getFields() {
		return fields;
	}

	public void setFields(FoggerData fields) {
		this.fields = fields;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	

	


}




