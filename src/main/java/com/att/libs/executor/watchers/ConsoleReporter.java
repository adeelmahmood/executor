package com.att.libs.executor.watchers;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.libs.executor.graph.Graph;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.graph.Node.NodeStatus;
import com.att.libs.executor.utils.GraphUtils;

/**
 * ConsoleReporter watches graph events and reports
 * information on console
 * 
 * @author aq728y
 *
 */
public class ConsoleReporter extends AbstractGraphWatcher<Graph<Node>, Node> {
	
	private static final Logger log = LoggerFactory.getLogger(ConsoleReporter.class);
	
	private boolean showStats;
	
	private long start;
	private long end;
	
	@Override
	public void processingGraph(Graph<Node> graph) {
		log.debug("Graph " + graph.getName() + " is being processed");
		start = DateTime.now().getMillis();
	}
	
	@Override
	public void beforeNodeProcessing(Graph<Node> graph, Node node) {
		log.debug("Node " + node.getName() + " is in the queue");
	}
	
	@Override
	public void processingNode(Graph<Node> graph, Node node) {
		log.debug("Node " + node.getName() + " is being processed");
	}
	
	@Override
	public void nodeProcessingCompleted(Graph<Node> graph, Node node) {
		log.debug("Node " + node.getName() + " has been processed");
		if(isShowStats()){
			//get total nodes
			int total = graph.getNodes().size();
			int processed = GraphUtils.countNodesByStatus(graph, NodeStatus.PROCESSED);
			int remaining = GraphUtils.countNodesByStatus(graph, NodeStatus.NOT_PROCESSED);
			int failed = GraphUtils.countNodesByStatus(graph, NodeStatus.FAILED);
			long timeElapsed = ((DateTime.now().getMillis()-start)/1000);
			//show stats
			log.debug("\n-----------------------------------------------------------\n"
					+ "--		Total Graph Node(s) : " + total + "					\n"
					+ "--		Processed Node(s) : " + processed + "				\n"
					+ "--		Remaining Node(s) : " + remaining + "				\n" +
		(failed>0	? "--		Failed Node(s) : " + failed + "				\n" : "") 
					+ "--		Time Elapsed : " + timeElapsed + " sec 				\n"
					+ "-----------------------------------------------------------");
		}
	}
	
	@Override
	public void graphProcessingCompleted(Graph<Node> graph) {
		end = DateTime.now().getMillis();
		log.debug("Graph " + graph.getName() + " has been processed in " + (end-start)/1000 + " sec");
	}
	
	@Override
	public void graphProcessingFailed(Graph<Node> graph, Throwable e) {
		log.debug("Graph " + graph.getName() + " failed, status " + graph.getStatus() + ", error " + e.getMessage());
	}
	
	@Override
	public void nodeProcessingFailed(Graph<Node> graph, Node node, Throwable e) {
		log.debug("Node " + node.getName() + " failed, status " + node.getStatus() + ", error " + e.getMessage());
	}

	public boolean isShowStats() {
		return showStats;
	}

	public void setShowStats(boolean showStats) {
		this.showStats = showStats;
	}
}