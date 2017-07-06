package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 *
 * @author Andrei Veshtard
 *         Date: July 4, 2017
 */
public class EarthquakeCityMap extends PApplet {

    // We will use member variables, instead of local variables, to store the data
    // that the setUp and draw methods will need to access (as well as other methods)
    // You will use many of these variables, but the only one you should need to add
    // code to modify is countryQuakes, where you will store the number of earthquakes
    // per country.

    // You can ignore this.  It's to get rid of eclipse warnings
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFILINE, change the value of this variable to true
    private static final boolean offline = false;

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";


    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

    // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";

    // The map
    private UnfoldingMap map;

    // Markers for each city
    private List<Marker> cityMarkers;
    // Markers for each earthquake
    private List<Marker> quakeMarkers;

    // A List of country markers
    private List<Marker> countryMarkers;

    @SuppressWarnings("Duplicates")
    public void setup() {
        // (1) Initializing canvas and map tiles
        size(900, 700, OPENGL);
        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
        } else {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }
        MapUtils.createDefaultEventDispatcher(this, map);

        // FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
        // one of the lines below.  This will work whether you are online or offline
        //earthquakesURL = "test1.atom";
        //earthquakesURL = "test2.atom";

        // WHEN TAKING THIS QUIZ: Uncomment the next line
        //earthquakesURL = "quiz1.atom";


        // (2) Reading in earthquake data and geometric properties
        //     STEP 1: load country features and markers
        final List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryMarkers = MapUtils.createSimpleMarkers(countries);

        //     STEP 2: read in city data
        final List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
        cityMarkers = new ArrayList<Marker>();
        for (final Feature city : cities) {
            cityMarkers.add(new CityMarker(city));
        }

        //     STEP 3: read in earthquake RSS feed
        final List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
        quakeMarkers = new ArrayList<Marker>();

        for (final PointFeature feature : earthquakes) {
            //check if LandQuake
            if (isLand(feature)) {
                quakeMarkers.add(new LandQuakeMarker(feature));
            }
            // OceanQuakes
            else {
                quakeMarkers.add(new OceanQuakeMarker(feature));
            }
        }

        // could be used for debugging
        printQuakes();

        // (3) Add markers to map
        //     NOTE: Country markers are not added to the map.  They are used
        //           for their geometric properties
        map.addMarkers(quakeMarkers);
        map.addMarkers(cityMarkers);

    }  // End setup


    public void draw() {
        background(0);
        map.draw();
        addKey();

    }

    // helper method to draw key in GUI
    // TODO: Update this method as appropriate
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240);
        rect(25, 50, 150, 250);

        ellipse(60, 123, 10, 10);
        rect(55, 137, 10, 10);
        ellipse(60, 271.5f, 10, 10);
        line(55, 266.5f, 65, 276.5f);
        line(55, 276.5f, 65, 266.5f);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(12);
        text("Earthquake Key", 50, 75);

        text("City Marker", 75, 100);
        text("Land Quake", 75, 120);
        text("Ocean Quake", 75, 140);
        text("Size ~ Magnitude", 50, 160);

        text("Shallow", 75, 200);
        text("Intermediate", 75, 220);
        text("Deep", 75, 240);

        text("Past Hour", 75, 270);

        fill(255, 0, 0);
        triangle(55, 106.5f, 60, 96.5f, 65, 106.5f);
        ellipse(60, 241.5f, 10, 10);

        fill(255, 255, 0);
        ellipse(60, 201.5f, 10, 10);
        fill(0, 0, 255);
        ellipse(60, 221.5f, 10, 10);
    }


    // Checks whether this quake occurred on land.  If it did, it sets the
    // "country" property of its PointFeature to the country where it occurred
    // and returns true.  Notice that the helper method isInCountry will
    // set this "country" property already.  Otherwise it returns false.
    private boolean isLand(final PointFeature earthquake) {

        // IMPLEMENT THIS: loop over all countries to check if location is in any of them

        // TODO: Implement this method using the helper method isInCountry
        for (final Marker country : countryMarkers) {
            if (isInCountry(earthquake, country)) {
                return true;
            }
        }
        // not inside any country
        return false;
    }

    // prints countries with number of earthquakes
    // You will want to loop through the country markers or country features
    // (either will work) and then for each country, loop through
    // the quakes to count how many occurred in that country.
    // Recall that the country markers have a "name" property,
    // And LandQuakeMarkers have a "country" property set.
    private void printQuakes() {
        // TODO: Implement this method
        final Map<String, Integer> map = new HashMap<>();
        int countOceanQuakes = 0;

        for (final Marker marker : quakeMarkers) {
            if (marker instanceof LandQuakeMarker) {
                final String country = marker.getProperty("country").toString();

                map.put(country, map.containsKey(country) ? map.get(country) + 1 : 1);
            } else {
                countOceanQuakes++;
            }
        }

        for (final Map.Entry<String, Integer> pair : map.entrySet()) {
            System.out.println(pair.getKey() + ": " + pair.getValue());
        }

        System.out.println("Ocean: " + countOceanQuakes);
    }


    // helper method to test whether a given earthquake is in a given country
    // This will also add the country property to the properties of the earthquake
    // feature if it's in one of the countries.
    // You should not have to modify this code
    private boolean isInCountry(final PointFeature earthquake, final Marker country) {
        // getting location of feature
        final Location checkLoc = earthquake.getLocation();

        // some countries represented it as MultiMarker
        // looping over SimplePolygonMarkers which make them up to use isInsideByLoc
        // noinspection Duplicates
        if (country.getClass() == MultiMarker.class) {

            // looping over markers making up MultiMarker
            for (final Marker marker : ((MultiMarker) country).getMarkers()) {

                // checking if inside
                if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));

                    // return if is inside one
                    return true;
                }
            }
        }

        // check if inside country represented by SimplePolygonMarker
        else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));

            return true;
        }
        return false;
    }

}
