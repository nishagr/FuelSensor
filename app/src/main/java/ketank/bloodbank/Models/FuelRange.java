package ketank.bloodbank.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FuelRange {

    @SerializedName("val")
    @Expose
    private int fuelVal;

    @SerializedName("cfl")
    @Expose
    private int cfl;

    public int getFuelVal() {
        return fuelVal;
    }

    public void setFuelVal(int fuelVal) {
        this.fuelVal = fuelVal;
    }

    public int getCfl() {
        return cfl;
    }

    public void setCfl(int cfl) {
        this.cfl = cfl;
    }
}
