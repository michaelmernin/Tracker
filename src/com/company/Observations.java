package com.company;

/**
 * Created by michaelmernin on 12/8/16.
 */
public class Observations {

    int id;
    int bloodSugarLevel;
    String energyLevel;
    String mealEaten;
    String notes;

    boolean currentDate;

    public Observations(boolean currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(boolean currentDate) {
        this.currentDate = currentDate;
    }

    public Observations(int id, int bloodSugarLevel, String energyLevel, String mealEaten, String notes) {
        this.id = id;
        this.bloodSugarLevel = bloodSugarLevel;
        this.energyLevel = energyLevel;
        this.mealEaten = mealEaten;
        this.notes = notes;
    }

    public Observations() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBloodSugarLevel() {
        return bloodSugarLevel;
    }

    public void setBloodSugarLevel(int bloodSugarLevel) {
        this.bloodSugarLevel = bloodSugarLevel;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getMealEaten() {
        return mealEaten;
    }

    public void setMealEaten(String mealEaten) {
        this.mealEaten = mealEaten;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
