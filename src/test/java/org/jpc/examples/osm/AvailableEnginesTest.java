package org.jpc.examples.osm;

import org.jpc.util.LogicEngineManager;
import org.junit.Test;

import com.google.common.collect.Multiset;

public class AvailableEnginesTest {

	@Test
	public void testEngines() {
		LogicEngineManager manager = LogicEngineManager.getDefault();
		manager.register(LogicEngineManager.findConfigurations());
		Multiset<String> logicEngines = manager.groupByLogicEngine().keys();
		System.out.println(logicEngines);
	}
}
