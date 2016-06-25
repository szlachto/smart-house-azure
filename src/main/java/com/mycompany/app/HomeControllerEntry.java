package com.mycompany.app;

import java.util.Date;

public class HomeControllerEntry {

    private Date timestamp;
    private Integer temperature;
    private Integer powerConsumption;
    private boolean peoplePresent = true;

    public HomeControllerEntry(final Integer temperature, final Integer powerConsumption, final boolean peoplePresent) {
        this.timestamp = new Date();
        this.temperature = temperature;
        this.powerConsumption = powerConsumption;
        this.peoplePresent = peoplePresent;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the temperature
     */
    public Integer getTemperature() {
        return this.temperature;
    }

    /**
     * @param temperature
     *            the temperature to set
     */
    public void setTemperature(final Integer temperature) {
        this.temperature = temperature;
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
