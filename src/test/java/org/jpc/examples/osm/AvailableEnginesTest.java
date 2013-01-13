package org.jpc.examples.osm;

import org.jpc.util.PrologEngineManager;
import org.junit.Test;

import com.google.common.collect.Multiset;

public class AvailableEnginesTest {

	@Test
	public void testEngines() {
		PrologEngineManager manager = PrologEngineManager.getDefault();
		manager.register(PrologEngineManager.findConfigurations());
		Multiset<String> prologEngines = manager.groupByPrologEngine().keys();
		System.out.println(prologEngines);
	}
}
