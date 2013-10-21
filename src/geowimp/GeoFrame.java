package geowimp;

import java.awt.BorderLayout;
import java.awt.Frame;

import processing.core.PApplet;

public class GeoFrame extends Frame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3123517392773034952L;

	public GeoFrame() {
        super("Embedded PApplet");

        setLayout(new BorderLayout());
        PApplet embed = new GeoDraw();
        add(embed, BorderLayout.CENTER);

        // important to call this whenever embedding a PApplet.
        // It ensures that the animation thread is started and
        // that other internal variables are properly set.
        embed.init();
    }

}
