package com.patternity.documentation.graphic.layout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.patternity.core.metamodel.LayeredOccurrence;
import com.patternity.core.metamodel.OccurrenceTemplate;
import com.patternity.core.metamodel.Pattern;
import com.patternity.core.metamodel.PatternOccurrence;
import com.patternity.core.metamodel.Type;

public class LayeredOccurrenceTest {

	public final static Pattern BRIDGE = new Pattern() {

		public String getName() {
			return "Bridge";
		}

		public String toString() {
			return getName();
		}

	};

	@Test
	public void testScenarioBridge() throws Exception {
		final LayeredOccurrence occurrence = new LayeredOccurrence(BRIDGE);

		// meta (diagram) definitions
		final OccurrenceTemplate metaTemplate = occurrence.metaLayer();
		metaTemplate.addStructureFlow(new String[] { "CollaborationAbstractionHierarchy",
				"CollaborationImplementorHierarchy", "Delegation" }, "class-diagram;hide-multiplicity;hide-label");
		metaTemplate.addDynamics(new String[] { "Delegation", "Description" },
				"sequence-diagram;hide-multiplicity;hide-label");

		// shallow (relationships) definitions
		final OccurrenceTemplate shallowTemplate = occurrence.shallowLayer();
		shallowTemplate.addDelegationRelation("Delegation", "Abstraction", "Implementor");
		shallowTemplate.addInheritanceHierarchy("AbstractionHierarchy", "Abstraction");
		shallowTemplate.addInheritanceHierarchy("ImplementorHierarchy", "Implementor");
		shallowTemplate.addCollaborationRelation("CollaborationAbstractionHierarchy", "Self", "AbstractionHierarchy");
		shallowTemplate.addCollaborationRelation("CollaborationImplementorHierarchy", "Self", "ImplementorHierarchy");
		shallowTemplate
				.addTextTemplate("Description",
						"Decouple an abstraction ($Abstraction$) from its implementation so that the two can vary independently.");
		shallowTemplate.addTextTemplate("Category", "Structural");

		// deep (runtime declaration from annotated source code)
		final PatternOccurrence deep = occurrence.deepLayer();
		final Type abstraction = new Type("MyAbstraction");
		final Type implementor1 = new Type("MyImplementor1");
		final Type implementor2 = new Type("MyImplementor2");
		deep.add("Abstraction", abstraction);
		deep.add("Implementor", implementor1);
		deep.add("Implementor", implementor2);

		// other implicit types that exist...
		final Type[] abstractionSubtypes = new Type[] { new Type("ConcreteAbstraction1"),
				new Type("ConcreteAbstraction2") };
		final Type[] implementor1Subtypes = new Type[] { new Type("ConcreteImplementor1"),
				new Type("ConcreteImplementor1") };
		final Type[] implementor2Subtypes = new Type[] { new Type("ConcreteImplementor1b"),
				new Type("ConcreteImplementor1b") };
		final Map<Type, Type[]> subTypes = new HashMap<Type, Type[]>();
		subTypes.put(abstraction, abstractionSubtypes);
		subTypes.put(implementor1, implementor1Subtypes);
		subTypes.put(implementor2, implementor2Subtypes);

		// ---------------------------------
		// let's resolve

		// TODO REMOVE ASAP!!!!
		DiagramPrinter.subTypesHorribleHack = subTypes;

		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		DiagramPrinter.process(baseDir, occurrence);

	}

}
