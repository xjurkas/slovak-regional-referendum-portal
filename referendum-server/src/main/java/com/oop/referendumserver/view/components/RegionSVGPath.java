package com.oop.referendumserver.view.components;

import com.oop.referendumserver.db.model.Region;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;


/**
 * Custom SVG path representation for a specific region on the map.
 */
public class RegionSVGPath extends SVGPath {

    /**
     * Function to get region
     * @return region
     */
    public Region getRegion() {
        return region;
    }

    private final Region region;
    private final Color fillColor;
    private final Color hooverColor;

    /**
     * Constructs a new RegionSVGPath.
     * @param region     the region associated with this SVG path
     * @param content    the content of the SVG path
     * @param fillColor  the fill color of the SVG path
     * @param hooverColor the color of the SVG path when hovered over
     */
    public RegionSVGPath(Region region, String content, Color fillColor, Color hooverColor) {
        super();
        this.region = region;
        this.fillColor = fillColor;
        this.hooverColor = hooverColor;

        setContent(content);
        setFill(fillColor);
        setStroke(Color.BLACK);
        setStrokeWidth(0.5);
    }

    /**
     * Handles mouse events for the SVG path
     * @param event the mouse event
     */
    public void handleEvent(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            setFill(hooverColor);
        }

        if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            setFill(fillColor);
        }

    }

    /**
     * Gets the hexadecimal representation of the fill color
     * @return the hexadecimal representation of the fill color
     */
    public String getFillColorHex() {
        return toRGBCode(fillColor);
    }

    /**
     * Gets the hexadecimal representation of the hover color
     * @return the hexadecimal representation of the hover color
     */
    public String getHooverColorHex() {
        return toRGBCode(hooverColor);
    }

    /**
     * Converts a color to its hexadecimal representation
     * @param color the color to convert
     * @return the hexadecimal representation of the color
     */
    private static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
