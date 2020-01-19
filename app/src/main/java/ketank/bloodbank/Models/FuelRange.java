package ketank.bloodbank.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FuelRange {

    @SerializedName("val")
    @Expose
    private String fuelVal;

    @SerializedName("cfl")
    @Expose
    private String cfl;

    public String getFuelVal() {
        return fuelVal;
    }

    public void setFuelVal(String fuelVal) {
        this.fuelVal = fuelVal;
    }

    public String getCfl() {
        return cfl;
    }

    public void setCfl(String cfl) {
        this.cfl = cfl;
    }
}
