package at.ac.fhwn.lib;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class LocationClientService {

    private static final String REQUEST_METHOD_SET_LOCATION = "/setLocation";
    private static final String REQUEST_METHOD_GET_LOCATION = "/getLocation";
    private static final String REQUEST_METHOD_GET_LOCATIONS = "/getLocations";
    private static final String REQUEST_METHOD_GET_ALL_LOCATIONS = "/getAllLocations";
    private static final String BASE_URL = "http://10.0.0.182:8080";
    private final int id;

    public LocationClientService(int id) {
        this.id = id;
    }

    public String get(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            return getResponseString(connection.getInputStream());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getResponseString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuffer stringBuffer = new StringBuffer();
        while (scanner.hasNextLine()){
            stringBuffer.append(scanner.nextLine());
        }
        return stringBuffer.toString();
    }

    public SaePoint sendLocation(SaePoint saePoint) {

        String params = String.format("id=%s&lat=%s&lon=%s&time=%s&satNr=%s&fixQ=%s",
                id,
                saePoint.getLatitude(),
                saePoint.getLongitude(),
                saePoint.getTime(),
                saePoint.getNrOfSatellites(),
                saePoint.getFixQuality()
        );

        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + REQUEST_METHOD_SET_LOCATION + "?" + params).openConnection();

            if( connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                System.out.println("Error occured: " + getResponseString(connection.getErrorStream()));
                throw new SaeException("Error occured: " + getResponseString(connection.getErrorStream()));
            }
            return new ObjectMapper().readValue(getResponseString(connection.getInputStream()), SaePoint.class);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public SaePoint getLocation(int index) {
        String params = String.format("id=%s&index=%s",
                id,
                index
        );

        try {
            return new ObjectMapper().readValue(new URL(BASE_URL + REQUEST_METHOD_GET_LOCATION + "?" + params),SaePoint.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<SaePoint> getLocations(){
        String json = "{\"latitude\":48.232526416666666,\"longitude\":14.7140093,\"" +
                "time\":\"140221.00\",\"nrOfSatellites\":12,\"fixQuality\":1}";

        String params = String.format("id=%s",
                id
        );
        try {
        URL url = new URL(BASE_URL + REQUEST_METHOD_GET_LOCATIONS + "?" + params);

            return new ObjectMapper().readValue(url,
                    new TypeReference<List<SaePoint>>(){});
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        String test = "[{\"latitude\":1.0,\"longitude\":2.0,\"time\":\"test\",\"nrOfSatellites\":19,\"fixQuality\":1},{\"latitude\":1.0,\"longitude\":2.0,\"time\":\"test\",\"nrOfSatellites\":19,\"fixQuality\":1},{\"latitude\":1.0,\"longitude\":2.0,\"time\":\"test\",\"nrOfSatellites\":19,\"fixQuality\":1}]";
//
//        TimerTask repeatedTask = new TimerTask() {
//            public void run() {
//                System.out.println("Task performed on " + new Date());
//            }
//        };
//        Timer timer = new Timer("Timer");
//
//        long delay = 1000L;
//        long period = 1000L * 60L * 60L * 24L;
//        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    public Hashtable<Integer, List<SaePoint>> getAllLocations(){
        try {
            URL url = new URL(BASE_URL + REQUEST_METHOD_GET_ALL_LOCATIONS );

            return new ObjectMapper().readValue(url,
                    new TypeReference<Hashtable<Integer, List<SaePoint>>>(){});
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
