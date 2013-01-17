package org.jpc.examples.osm.model.gsonconverters;

/**
 * GeoJson constants used by the gson converters
 * @author sergioc
 *
 */
public class GeoJsonConstants {
	
	//GeoJson ATTRIBUTES NAMES
	public static final String TYPE = "type";
	public static final String CRS = "crs";
	public static final String BBOX = "bbox";
	
	//FEATURE ATTRIBUTES NAMES
	public static final String ID = "id";
	public static final String GEOMETRY = "geometry";
	public static final String PROPERTIES = "properties";
	
	//FEATURE COLLECTION ATTRIBUTES NAMES
	public static final String FEATURES = "features";
	
	//GEOMETRY ATTRIBUTES NAMES
	public static final String COORDINATES = "coordinates";
	
	
	//FEATURE TYPES
	public static final String FEATURE = "Feature";
	public static final String FEATURE_COLLECTION = "FeatureCollection";
	
	//GEOMETRY TYPES
	public static final String POINT = "Point";
	public static final String MULTI_POINT = "MultiPoint";
	public static final String LINE_STRING = "LineString";
	public static final String MULTI_LINE_STRING = "MultiLineString";
	public static final String POLYGON = "Polygon";
	public static final String MULTI_POLYGON = "MultiPolygon";
	public static final String GEOMETRY_COLLECTION = "GeometryCollection";

}
