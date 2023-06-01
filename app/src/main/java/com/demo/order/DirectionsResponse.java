package com.demo.order;

import com.google.gson.Gson;

import java.util.List;

public class DirectionsResponse {
    public String status;
    public List<Route> routes;

    public static DirectionsResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DirectionsResponse.class);
    }

    public class Route {
        public List<Leg> legs;
        public OverviewPolyline overview_polyline;
    }

    public class Leg {
        public Distance distance;
        public Duration duration;
        public List<Step> steps;
    }

    public class Distance {
        public String text;
        public int value;
    }

    public class Duration {
        public String text;
        public int value;
    }

    public class Step {
        public String html_instructions;
        public Distance distance;
        public Duration duration;
    }

    public class OverviewPolyline {
        public String points;
    }
}
