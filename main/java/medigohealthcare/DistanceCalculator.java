/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medigohealthcare;

import com.google.firebase.database.utilities.Pair;
import com.google.maps.GeoApiContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 20175707
 */
public class DistanceCalculator {
    
    public DistanceCalculator(){
        
    }
    
    public static JSONObject getJsonDistance(String start, String destination) throws IOException, InterruptedException{
        String startURI = start.replace(" ", "+");
        String destinationURI = destination.replace(" ", "+");
        String key = "YOUR GOOGLE MAPS KEY";
        String uri = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + startURI + "&destinations=" + destinationURI + "&key=" + key;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    
    public static int getDistanceInt(JSONObject json){
        JSONArray rows = json.getJSONArray("rows");
        JSONObject elementsArray = rows.getJSONObject(0);
        String elementsString = elementsArray.toString();
        String rep1 = elementsString.replace("[", "");
        String rep2 = rep1.replace("]", "");
        JSONObject elementsJson = new JSONObject(rep2);
        JSONObject elements = elementsJson.getJSONObject("elements");
        JSONObject distance = elements.getJSONObject("distance");
        int value = distance.getInt("value");
        return value;
    }
    
    public static Pair<String, Integer> giveClosestPharmacy(String patientAddress, HashMap<String, String> pharmaciesAddress) throws IOException, InterruptedException{
        HashMap<String, Integer> distances = new HashMap<>();
        
        Iterator it = pharmaciesAddress.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            JSONObject jsonDistance  = getJsonDistance(patientAddress, (String) pair.getValue());
            int distance = getDistanceInt(jsonDistance);
            distances.put((String) pair.getKey(), distance);
        }
    
        String closestPharm = null;
        int closestDistace = 2000000000;
        
        Iterator itD = distances.entrySet().iterator();
        while (itD.hasNext()) {
            Map.Entry pair = (Map.Entry)itD.next();
            if ((int)pair.getValue() <= closestDistace){
                closestDistace = (int) pair.getValue();
                closestPharm = (String) pair.getKey();
            }
        }
        Pair<String, Integer> pair = new Pair<>(closestPharm, closestDistace);
        
        return pair;
    }
    
    // Give delivery times
    public HashMap<String, Integer> deliveryDistances = new HashMap<>();
    public void deliveryTimes(String pharmacyAddress, HashMap<String, String> patientEmailAddress) throws IOException, InterruptedException{
        // Get closest patient
        Pair<String, Integer> closestPat = giveClosestPharmacy(pharmacyAddress, patientEmailAddress);
        // Add to the delivery map
        deliveryDistances.put(closestPat.getFirst(), closestPat.getSecond());
        // Remove from the list of patients
        String addressNextClosest = patientEmailAddress.get(closestPat.getFirst());
        patientEmailAddress.remove(closestPat.getFirst());
        // If not done move to the next closest patient
        if (!patientEmailAddress.isEmpty()){
            deliveryTimes(addressNextClosest, patientEmailAddress);
        }
    }
    

}
