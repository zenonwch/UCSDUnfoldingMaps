package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Implements a visual marker for cities on an earthquake map
 *
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Andrei Veshtard
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {

    public static int TRI_SIZE = 5;  // The size of the triangle marker

    public CityMarker(final Location location) {
        super(location);
    }


    public CityMarker(final Feature city) {
        super(((PointFeature) city).getLocation(), city.getProperties());
        // Cities have properties: "name" (city name), "country" (country name)
        // and "population" (population, in millions)
    }


    /**
     * Implementation of method to draw marker on the map.
     */
    @Override
    public void drawMarker(final PGraphics pg, final float x, final float y) {
        // Save previous drawing style
        pg.pushStyle();

        // IMPLEMENT: drawing triangle for each city
        pg.fill(150, 30, 30);
        pg.triangle(x, y - TRI_SIZE, x - TRI_SIZE, y + TRI_SIZE, x + TRI_SIZE, y + TRI_SIZE);

        // Restore previous drawing style
        pg.popStyle();
    }

    /**
     * Show the title of the city if this marker is selected
     */
    @Override
    public void showTitle(final PGraphics pg, final float x, final float y) {
        // TODO: Implement this method
        final String text = getCity() + " (" + getCountry() + ")\nPop:" + getPopulation() + 'M';
        final float w = pg.textWidth(text);
        final float padding = 3;

        pg.pushStyle();
        pg.fill(240, 200);
        pg.rect(x + 2 * TRI_SIZE, y, w + 2 * padding, 36);
        pg.fill(20);
        pg.textSize(12);
        pg.textAlign(PConstants.LEFT, PConstants.TOP);
        pg.text(text, x + 2 * TRI_SIZE + padding, y);
        pg.popStyle();
    }


    /* Local getters for some city properties.
     */
    public String getCity() {
        return getStringProperty("name");
    }

    public String getCountry() {
        return getStringProperty("country");
    }

    public float getPopulation() {
        return Float.parseFloat(getStringProperty("population"));
    }
}
