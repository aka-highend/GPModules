package com.fachrur_122.rxdabinmodule.Model;

import android.util.Log;

import com.fachrur_122.rxdabinmodule.APIManage;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by fachrur_122 on 17/05/2016.
 *
 */
public class PlacesDetail {

    @Expose
    Result result;

    public Result getResult() { return result; }

    public String getName() {
        if (result != null) {
            return result.getName();
        } else {
            return "";
        }
    }

    public String getPhotoUrl() {
        if (result != null) {
            return result.getPhotoUrl();
        } else {
            return "";
        }
    }

    public String getFormattedAddress() {
        if (result != null) {
            return result.getFormatted_address();
        } else {
            return "";
        }
    }

    public class Result {
        public class Photo {
            @Expose
            String photo_reference;
        }

        @Expose
        String name;

        @Expose
        List<Photo> photos;

        @Expose
        String formatted_address;

        public String getName() { return name; }

        public String getPhotoUrl() {
            if (photos != null && photos.size() > 0) {
                String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?key="
                        + APIManage.API_KEY + "&photoreference="
                        + photos.get(0).photo_reference
                        + "&maxwidth=300&maxheight=300";
                Log.d("PlacesDetail", "Photo Url for " + getName() + "was" + photoUrl);

                return photoUrl;
            } else {
                Log.d("PlacesDetail", "Photo Url was null in " + getName());
                return "";
            }
        }

        public String getFormatted_address() {
            return formatted_address;
        }
    }
}
