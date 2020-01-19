package ketank.bloodbank.Other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

import ketank.bloodbank.Models.FuelRange;

public class SharedPref {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FUEL_VAL = "Fuel_Range";

    public SharedPref() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFuelConsumption(Context context, List<FuelRange> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FUEL_VAL, jsonFavorites);

        editor.commit();
    }

//    public void addFuelConsumption(Context context, FuelRange fuelRange) {
//        List<FuelRange> favorites = getFavorites(context);
//        if (favorites == null)
//            favorites = new ArrayList<FuelRange>();
//        favorites.add(fuelRange);
//        saveFuelConsumption(context, favorites);
//    }

    public void removeFavorite(Context context, FuelRange fuelRange) {
        ArrayList<FuelRange> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(fuelRange);
            saveFuelConsumption(context, favorites);
        }
    }

    public ArrayList<FuelRange> getFavorites(Context context) {
        SharedPreferences settings;
        List<FuelRange> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FUEL_VAL)) {
            String jsonFavorites = settings.getString(FUEL_VAL, null);
            Gson gson = new Gson();
            FuelRange[] favoriteItems = gson.fromJson(jsonFavorites,
                    FuelRange[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<FuelRange>(favorites);
        } else
            return null;

        return (ArrayList<FuelRange>) favorites;
    }
}