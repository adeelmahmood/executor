package com.att.libs.executor.graph;


/**
 * Node class provides a skeleton for Node classes
 * Subclasses should implement methods to provide
 * concrete functionality of the Node
 * 
 * @author aq728y
 *
 */
public abstract class Node {

	protected String name;

	protected NodeStatus status = NodeStatus.NOT_PROCESSED;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeStatus getStatus() {
		return status;
	}

	public void setStatus(NodeStatus status) {
		this.status = status;
	}
	
	public static enum NodeStatus {

		NOT_PROCESSED("Not Processed"),

		INITIALIZING("Initializing"),
		
		PROCESSING("Processing"),
		
		FAILED("Failed"),
		
		PROCESSED("Processed");
		
		private String status;
		
		NodeStatus(String status){
			this.status = status;
		}
		
		public String getStatus(){
			return status;
		}
	}

}