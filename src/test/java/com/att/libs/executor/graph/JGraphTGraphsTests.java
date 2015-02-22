package com.att.libs.executor.graph;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;

import com.att.libs.executor.graph.impl.TaskNode;

public class JGraphTGraphsTests {

	@Test
	public void testDirectedGraph() {
		//create a directed graph
		DirectedGraph<TaskNode, DefaultEdge> graph = new DefaultDirectedGraph<TaskNode, DefaultEdge>(DefaultEdge.class);
		
		TaskNode n1 = new TaskNode();
		n1.setName("Node 1");
		n1.setIndex(1);
		n1.setOrder(1);
		TaskNode n2 = new TaskNode();
		n2.setName("Node 2");
		n2.setIndex(2);
		n2.setOrder(2);
		TaskNode n3 = new TaskNode();
		n3.setName("Node 3");
		n3.setIndex(3);
		n3.setOrder(3);
		
		graph.addVertex(n1);
		graph.addVertex(n2);
		graph.addVertex(n3);
		
		graph.addEdge(n1, n2);
		graph.addEdge(n2, n3);
		
		System.out.println(graph.toString());
		
		TopologicalOrderIterator<TaskNode, DefaultEdge> iter = new TopologicalOrderIterator<TaskNode, DefaultEdge>(graph);
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
	
	@Test
	public void testDirectedMultiGraph() {
		//create a directed graph
		DirectedMultigraph<TaskNode, DefaultEdge> graph = new DirectedMultigraph<TaskNode, DefaultEdge>(DefaultEdge.class);
		
		TaskNode n1 = createNode(1);
		TaskNode n2 = createNode(2);
		TaskNode n3 = createNode(3);
		TaskNode n4 = createNode(4);
		TaskNode n5 = createNode(5);
		TaskNode n6 = createNode(6);
		TaskNode n7 = createNode(7);
		TaskNode n8 = createNode(8);
		TaskNode n9 = createNode(9);
		
		graph.addVertex(n1);
		graph.addVertex(n2);
		graph.addVertex(n3);
		graph.addVertex(n4);
		graph.addVertex(n5);
		graph.addVertex(n6);
		graph.addVertex(n7);
		graph.addVertex(n8);
		graph.addVertex(n9);
		
		graph.addEdge(n1, n2);
		graph.addEdge(n1, n3);
		graph.addEdge(n2, n4);
		graph.addEdge(n3, n4);
		graph.addEdge(n4, n5);
		graph.addEdge(n5, n6);
		graph.addEdge(n6, n7);
		graph.addEdge(n6, n8);
		graph.addEdge(n7, n9);
		graph.addEdge(n8, n9);
		
		System.out.println(graph.toString());
		
		BreadthFirstIterator<TaskNode, DefaultEdge> breadthIter = new BreadthFirstIterator<TaskNode, DefaultEdge>(graph);
		
		System.out.println("breadth first iterator");
		while(breadthIter.hasNext()){
			TaskNode n = breadthIter.next();
			System.out.println("+++++++++++++++");
			System.out.println(n + "\n -> Edges " + graph.edgesOf(n) + "\n -> Incoming Edges " + graph.incomingEdgesOf(n));
			Set<DefaultEdge> incomingEdges = graph.incomingEdgesOf(n);
			Iterator<DefaultEdge> iter = incomingEdges.iterator();
			while(iter.hasNext()){
				DefaultEdge ie = iter.next();
				System.out.println("Incoming edge " + ie + ", source " + graph.getEdgeSource(ie));
			}
		}
	}

	private TaskNode createNode(int i){
		TaskNode node = new TaskNode();
		node.setName("Node " + i);
		return node;
	}
}
