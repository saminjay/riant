package com.riantservices.riant.models;

import java.util.HashMap;
import java.util.List;

public class DistanceDirection {
public float distance;
public List<List<HashMap<String, String>>> routes;

public DistanceDirection(float distance, List<List<HashMap<String, String>>> routes){
    this.distance = distance;
    this.routes = routes;
}
}