package com.riantservices.riant.models;

import java.util.HashMap;
import java.util.List;

public class DistanceDirectionTime {
public float distance;
public List<List<HashMap<String, String>>> routes;
public float time;

public DistanceDirectionTime(float distance, List<List<HashMap<String, String>>> routes, float time){
    this.distance = distance;
    this.routes = routes;
    this.time = time;
}
}