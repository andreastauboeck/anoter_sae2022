package at.ac.fhwn.locationtransmitter;

import at.ac.fhwn.lib.SaePoint;

import java.io.*;
import java.util.Scanner;

public class NmeaReader {

    public SaePoint readMessage(String line) {
        String[] values = line.split(",");

        if (values[0].equals("$GNGGA")) {
            String time = values[1];
            double latitude = Double.parseDouble(values[2].substring(0,2)) +
                    Double.parseDouble(values[2].substring(2))/60;
            double longitude = Double.parseDouble(values[4].substring(0,3)) +
                    Double.parseDouble(values[4].substring(3))/60;
            int fixQuality = Integer.parseInt(values[6]);
            int nrOfSatellites = Integer.parseInt(values[7]);

            return new SaePoint(time,latitude,longitude, nrOfSatellites,fixQuality);
        }
        return null;
    }
}
