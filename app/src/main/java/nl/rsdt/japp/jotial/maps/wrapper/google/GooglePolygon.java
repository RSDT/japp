package nl.rsdt.japp.jotial.maps.wrapper.google;

import nl.rsdt.japp.jotial.maps.wrapper.IPolygon;

/**
 * Created by mattijn on 08/08/17.
 */

public class GooglePolygon implements IPolygon{
    private final com.google.android.gms.maps.model.Polygon googlePolygon;

    public GooglePolygon(com.google.android.gms.maps.model.Polygon polygon) {
        googlePolygon = polygon;
    }

    @Override
    public void remove() {
        googlePolygon.remove();
    }

    @Override
    public void setStrokeWidth(int strokeWidth) {
        googlePolygon.setStrokeWidth(strokeWidth);
    }

    public void setFillColor(int color) {
        googlePolygon.setFillColor(color);
    }

    @Override
    public void setVisible(boolean visible) {
        googlePolygon.setVisible(visible);
    }
}
