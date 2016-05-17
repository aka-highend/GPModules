package com.fachrur_122.rxdabinmodule.Model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by fachrur_122 on 17/05/2016.
 *
 */
public class PlacesResult {
    @Expose
    public List<Prediction> predictions;

    @Expose
    String status;
}
