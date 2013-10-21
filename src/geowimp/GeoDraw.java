package geowimp;


import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import processing.core.PApplet;
import java.util.ArrayList;

import peasy.*;





public class GeoDraw extends PApplet {
	
	static int maxnum;
	static int minnum;
	static int maxsize;
	static int minsize;
	

	
	/**
	 * This program reads and stuff
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	StreamingData sd;
	ArrayList<GeoStreamEntry> entries;
	String textLine;
	PeasyCam cam;
	String artist;

	

	
	@Override
	public void setup() {
		//Set maxsize and minsize of marker
		 minsize = 2;
		 maxsize = 15;
		 maxnum = 0;
		 minnum = Integer.MAX_VALUE;
		
		
		 artist = "";
		 InputCollector ic = new InputCollector(this);
		 ic.run();
		 size(1000, 1000);
		 map = new UnfoldingMap(this, new OpenStreetMap.OpenStreetMapProvider());
		 background(0);
		 entries = new ArrayList<GeoStreamEntry>();
		 smooth();

	}
	
	@Override
	public void draw() {
		map.draw();
		map.zoomAndPanTo(new Location(60.5f, 9.2f), 7);
		
		for(GeoStreamEntry en:entries) {
			Location newloc = new Location(en.lat, en.lng);;
			SimplePointMarker newmark = new SimplePointMarker(newloc);
			newmark.setColor(color(0, 191, 255, 100));
			newmark.setStrokeWeight(find_size(en.num));
			map.addMarkers(newmark, newmark);
		}

		
		

		
	}
	
	
	//Method equivalent to the Arduino map 	
	int map(int x, int in_min, int in_max, int out_min, int out_max) {
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	int find_size(int num){
		if(num == 0) return 0;
		if(num < 10) return 1;
		if(num < 25) return 3;
		if(num < 50) return 6;
		if(num < 100) return 12;
		if(num < 500) return 20;
		if(num > 500) return 50;
		return 0;
	}
	
	public void load() {
		 System.out.println(artist);
		 sd = new StreamingData();
		 String sid = sd.GetSIDFromName(artist);
		 System.out.println(sid);
		 entries = sd.getGeoStreams(sid);
	}
	
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	
}
