package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/**
 * Implements a visual marker for ocean earthquakes on an earthquake map
 *
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Andrei Veshtard
 */
public class OceanQuakeMarker extends EarthquakeMarker {

    public OceanQuakeMarker(final PointFeature quake) {
        super(quake);

        // setting field in earthquake marker
        isOnLand = false;
    }


    /**
     * Draw the earthquake as a square
     */
    @Override
    public void drawEarthquake(final PGraphics pg, final float x, final float y) {
        pg.rect(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}