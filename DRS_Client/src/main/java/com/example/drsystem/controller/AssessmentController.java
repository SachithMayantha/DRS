package com.example.drsystem.controller;

public class AssessmentController {

    public static int assessDisaster(String type,String locationType,String severity) {
        int priority = 0;

        // Assess severity
        switch (severity) {
            case "Critical":
                priority += 5;
                break;
            case "Severe":
                priority += 4;
                break;
            case "High":
                priority += 3;
                break;
            case "Moderate":
                priority += 2;
                break;
            case "Low":
                priority += 1;
                break;
        }

        // Assess location - for example, a disaster in a densely populated area might be given a higher priority
        if (locationType.equals("Urban Area")) {
            priority += 3;
        } else if (locationType.equals("Industrial Zone")) {
            priority += 4;
        } else {
            priority += 2;
        }

        // Assess the type of disaster
        switch (type) {
            case "Fire":
                priority += 4;
                break;
            case "Flood":
                priority += 3;
                break;
            case "Earthquake":
                priority += 5;
                break;
            case "Tornado":
                priority += 4;
                break;
            case "Hurricane":
                priority += 5;
                break;
        }

        return priority; // Higher priority numbers indicate more urgent disasters
    }

}
