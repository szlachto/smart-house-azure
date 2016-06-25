package com.mycompany.app;


public class HomeControllerEntry {

    private String timestamp;
    private Double internalTemperature;
    private Integer powerConsumption;
    private boolean peoplePresent = true;
    private Double externalTemperature;

    public HomeControllerEntry(final String timeStamp, final Double temperature, final Double extTemperature,
        final Integer powerConsumption, final boolean peoplePresent) {
        this.timestamp = timeStamp;
        this.internalTemperature = temperature;
        this.externalTemperature = extTemperature;
        this.powerConsumption = powerConsumption;
        this.peoplePresent = peoplePresent;
    }

    /**
     * @return the internalTemperature
     */
    public Double getInternalTemperature() {
        return this.internalTemperature;
    }

    /**
     * @param internalTemperature
     *            the internalTemperature to set
     */
    public void setInternalTemperature(final Double internalTemperature) {
        this.internalTemperature = internalTemperature;
    }

    /**
     * @return the externalTemperature
     */
    public Double getExternalTemperature() {
        return this.externalTemperature;
    }

    /**
     * @param externalTemperature
     *            the externalTemperature to set
     */
    public void setExternalTemperature(final Double externalTemperature) {
        this.externalTemperature = externalTemperature;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the temperature
     */
    public Double getTemperature() {
        return this.internalTemperature;
    }

    /**
     * @param temperature
     *            the temperature to set
     */
    public void setTemperature(final Double temperature) {
        this.internalTemperature = temperature;
    }

    /**
     * @return the powerConsumption
     */
    public Integer getPowerConsumption() {
        return this.powerConsumption;
    }

    /**
     * @param powerConsumption
     *            the powerConsumption to set
     */
    public void setPowerConsumption(final Integer powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    /**
     * @return the peoplePresent
     */
    public boolean isPeoplePresent() {
        return this.peoplePresent;
    }

    /**
     * @param peoplePresent
     *            the peoplePresent to set
     */
    public void setPeoplePresent(final boolean peoplePresent) {
        this.peoplePresent = peoplePresent;
    }


}
