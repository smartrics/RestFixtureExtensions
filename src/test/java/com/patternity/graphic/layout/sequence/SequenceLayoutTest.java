package com.patternity.graphic.layout.sequence;

import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

import com.patternity.graphic.behavioral.Agent;
import com.patternity.graphic.behavioral.Chain;
import com.patternity.graphic.behavioral.Composite;
import com.patternity.graphic.behavioral.Event;
import com.patternity.graphic.behavioral.Message;
import com.patternity.graphic.behavioral.Note;
import com.patternity.graphic.behavioral.PingPong;
import com.patternity.graphic.dag.Node;
import com.patternity.util.TemplatedWriter;

public class SequenceLayoutTest {

	private File tempDir = new File(System.getProperty("java.io.tmpdir"));
	
	@Test
    public void testLayout() throws Exception {

        final Agent controller = new Agent("Controller", "controller", true);
        final Agent model = new Agent("Model", "model", true);
        final Agent view = new Agent("View", "view", true);

        final Node root = newNode(null, null);
        final Node a = newNode(model, "a");
        final Node b = newNode(model, "b");
        final Node c = newNode(view, "c");
        final Node d = newNode(view, "d");
        final Node e = newNode(model, "e");
        final Node f = newNode(controller, "f");
        final Node g = newNode(model, "g");

        final Node h = newNode(view, "h");

        root.add(a);

        a.add(b);

        b.add(c);
        b.add(new Node(new Note("this is an overall comment")));
        b.add(f);

        c.add(d);
        d.add(e);

        f.add(g);
        f.add(new Node(new Note(model, "this is a comment for f ")));

        root.add(h);

        final SequenceLayout layout = new SequenceLayout(16);
        final String s = layout.layout(root);

        final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, "SequenceLayoutTest.svg"), new File("template.svg"));
        writer.write(s, "viewBox=\"0 0 1000 1000\"");

        SnippetGenerator generator = new SnippetGenerator();
        final String snippet = generator.layout(a);
        System.out.println(snippet);

    }

    private Node newNode(Agent agent, final String name) {
        final String method = name == null ? null : name + "()";
        final Event message = new Message(agent, method);
        return new Node(message);
    }

    public void testCompositeSharedEffort() throws Exception {
        final Agent composite = new Agent("Composite", "composite", true);
        final Agent genericChild = new Agent("Component", "child", true);
        final Message genericEvent = new Message(genericChild, "doThat()");
        final String commentPattern = "For each member, {0}.{1}";
        final int n = 5;
        final Node compositeNode = new Node(new Message(composite, genericEvent.getMethod()));
        Composite.newComposite(genericEvent, commentPattern, n).append(compositeNode);

        final Node root = new Node(new Message(null, null));
        root.add(compositeNode);

        final SequenceLayout layout = new SequenceLayout(16);
        final String s = layout.layout(root);

        final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, "SequenceLayoutTest_composite.svg"), new File("template.svg"));
        writer.write(s, "viewBox=\"0 0 1000 1000\"");

    }

	@Test
    public void testExampleCompositeSEffort() throws Exception {
        final Agent composite = new Agent("Panel", "panel", true, new String[] { "Composite" });
        final Agent genericChild = new Agent("Widget", "widget", true);
        final Message genericEvent = new Message(genericChild, "draw()");
        final String commentPattern = "For [widget in panel], {0}.{1}";
        final int n = 4;
        final Node compositeNode = new Node(new Message(composite, genericEvent.getMethod()));
        Composite.newComposite(genericEvent, commentPattern, n).append(compositeNode);

        final Node root = new Node(new Message(null, null));
        root.add(compositeNode);

        final SequenceLayout layout = new SequenceLayout(16);
        final String s = layout.layout(root);

        final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, "SequenceLayoutTest_sharedeffort.svg"), new File("template.svg"));
        writer.write(s, "viewBox=\"0 0 1000 1000\"");

    }

	@Test
    public void testChainSelfAssignment() throws Exception {
        final Agent genericChild = new Agent("Component", "child", true);
        final Message genericEvent = new Message(genericChild, "doThat()");
        final String commentPattern = "For each successor, call its {1} until the request is processed";
        final int n = 4;
        final Node compositeNode = new Node(new Message(null, genericEvent.getMethod()));
        Chain.newChain(genericEvent, commentPattern, n).append(compositeNode);

        final Node root = new Node(new Message(null, null));
        root.add(compositeNode);

        final SequenceLayout layout = new SequenceLayout(16);
        final String s = layout.layout(root);

        final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, "SequenceLayoutTest_chain.svg"), new File("template.svg"));
        writer.write(s, "viewBox=\"0 0 1000 1000\"");

    }

	@Test
    public void testCallback() throws Exception {
        final Agent subject = new Agent("Model", "subject", true);
        final Agent observer = new Agent("View", "observer", true);

        final Message initialMessage = new Message(subject, "setSomething()");
        final Node initialNode = new Node(initialMessage);
        new PingPong(new Message(observer, "update(this)"), new Message(subject, "getData()"), 3).append(initialNode);

        final Node root = new Node(new Message(null, null));
        root.add(initialNode);

        final SequenceLayout layout = new SequenceLayout(16);
        final String s = layout.layout(root);

        final TemplatedWriter writer = new TemplatedWriter(new File(tempDir, "SequenceLayoutTest__callback.svg"), new File("template.svg"));
        writer.write(s, "viewBox=\"0 0 1000 1000\"");
    }

}
