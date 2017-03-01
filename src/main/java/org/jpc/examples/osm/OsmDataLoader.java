package org.jpc.examples.osm;

import static org.jpc.engine.prolog.PrologConstants.CONS_FUNCTOR;
import static org.jpc.engine.prolog.PrologConstants.NIL_SYMBOL;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jpc.engine.prolog.PrologEngine;
import org.jpc.examples.osm.model.jpcconverters.CoordinatesJpcConverter;
import org.jpc.examples.osm.model.jpcconverters.NodeJpcConverter;
import org.jpc.examples.osm.model.jpcconverters.WayJpcConverter;
import org.jpc.util.salt.CachedPrologEngineWriter;
import org.jpc.util.termprocessor.PrologAbstractWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OsmDataLoader {

	public static String DEFAULT_DATA_FILE = "org/jpc/examples/osm/brussels_center_filtered.osm";
	
	public static final String ID_ATTRIBUTE = "id"; //node id attribute
	public static final String NODE_ELEMENT = "node"; //node xml element
	public static final String LAT_NODE_ATTRIBUTE = "lat"; //latitude xml attribute 
	public static final String LON_NODE_ATTRIBUTE = "lon"; //longitude xml attribute 
	public static final String WAY_ELEMENT = "way"; //way xml element 
	public static final String NODE_REFERENCE_ELEMENT = "nd"; //a node reference element nested in a way element 
	public static final String REFERENCE_ATTRIBUTE = "ref"; //a reference attribute
	public static final String TAG_ELEMENT = "tag"; //tag xml element
	public static final String KEY_TAG_ATTRIBUTE = "k"; //key xml attribute
	public static final String VALUE_TAG_ATTRIBUTE = "v"; //key xml attribute
	
	private PrologEngine prologEngine;
	private PrologAbstractWriter writer;
	
	public OsmDataLoader(PrologEngine prologEngine) {
		this.prologEngine = prologEngine;
	}

	public void load(String fileName) {
		File file = new File(fileName);
		load(file);
	}
	
	public void loadDefault() {
		File resourceFile = new File(getClass().getClassLoader().getResource(DEFAULT_DATA_FILE).getFile());
		load(resourceFile);
	}

	public void load(File resourceFile) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
//		try(PrintStream ps = new PrintStream("brussels_center_filtered.lgt")) {
//			writer = new PrologStreamWriter(prologEngine, ps);
		try {
			//writer = new PrologEngineWriter(prologEngine);
			writer = new CachedPrologEngineWriter(prologEngine);
			writer.followingClauses();
			writer.startLogtalkObjectContext().startAtom("osm");
			
			final Set<String> names = new HashSet<>();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler saxHandler = new OsmSaxHandler();

			saxParser.parse(resourceFile, saxHandler);
//			for(String s:names) {
//				System.out.println(s);
//			}
			((CachedPrologEngineWriter)writer).close();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}
	}
	
	class OsmSaxHandler extends DefaultHandler {
		private int tagsCounter;
		private int nodeReferenceCounter;
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if(qName.equals(NODE_ELEMENT)) {
				writer.startCompound().startAtom(NodeJpcConverter.NODE_FUNCTOR_NAME); //to be closed later in the endElement method
				String id = attributes.getValue(ID_ATTRIBUTE);
				writer.startIntegerTerm(Long.parseLong(id));
				String lat = attributes.getValue(LAT_NODE_ATTRIBUTE);
				String lon = attributes.getValue(LON_NODE_ATTRIBUTE);
				writer.startCompound().startAtom(CoordinatesJpcConverter.COORDINATE_FUNCTOR_NAME).startFloatTerm(Double.parseDouble(lon)).startFloatTerm(Double.parseDouble(lat)).endCompound();
			} else if(qName.equals(WAY_ELEMENT)) {
				writer.startCompound().startAtom(WayJpcConverter.WAY_FUNCTOR_NAME); //to be closed later in the endElement method
				String id = attributes.getValue(ID_ATTRIBUTE);
				writer.startIntegerTerm(Long.parseLong(id));
			} else if(qName.equals(NODE_REFERENCE_ELEMENT)) {
				nodeReferenceCounter++;
				String ref = attributes.getValue(REFERENCE_ATTRIBUTE);
				writer.startCompound().startAtom(".");
				writer.startIntegerTerm(Long.parseLong(ref));
			} else if(qName.equals(TAG_ELEMENT)) {
				if(nodeReferenceCounter > 0) {
					writer.startAtom(NIL_SYMBOL);
					closeNodeReferences();
				}
				tagsCounter++;
				String key = attributes.getValue(KEY_TAG_ATTRIBUTE);
				String value = attributes.getValue(VALUE_TAG_ATTRIBUTE);
				writer.startCompound().startAtom(CONS_FUNCTOR); //to be closed later in the endElement method
				writer.startCompound().startAtom("-").startAtom(key).startAtom(value).endCompound();
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if(qName.equals(NODE_ELEMENT)) {
				writer.startAtom(NIL_SYMBOL); //an empty list for ending the list of tags expressed as cons
				if(tagsCounter>0)
					closeTags();
				writer.endCompound(); //closing the node
				
			} else if(qName.equals(WAY_ELEMENT)) {
				if(nodeReferenceCounter > 0) {
					writer.startAtom(NIL_SYMBOL);
					closeNodeReferences();
				}
				writer.startAtom(NIL_SYMBOL); //an empty list for ending the list of tags expressed as cons
				if(tagsCounter>0)
					closeTags();
				writer.endCompound(); //closing the way
			}
		}
		
		private void closeNodeReferences() {
			for(int i=0;i<nodeReferenceCounter;i++)
				writer.endCompound(); //closing each node reference
			nodeReferenceCounter = 0;
		}
		
		private void closeTags() {
			for(int i=0;i<tagsCounter;i++)
				writer.endCompound(); //closing each tag
			tagsCounter = 0;
		}
	}

}
