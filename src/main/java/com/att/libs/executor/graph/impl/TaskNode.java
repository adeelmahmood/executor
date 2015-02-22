package com.att.libs.executor.graph.impl;

import java.util.Map;

import com.att.libs.executor.graph.AbstractNode;
import com.att.libs.executor.graph.Node;
import com.att.libs.executor.tasks.ExecutableTask;

/**
 * TaskNode represents a indexable {@link Node} that holds 
 * runnable tasks
 * 
 * @author aq728y
 *
 */
public class TaskNode extends AbstractNode{

	private int index;
	private int order;
	
	private Map<String, Object> data;
	
	private Class<? extends ExecutableTask> task;
	private Class<? extends ExecutableTask> pre;
	private Class<? extends ExecutableTask> post;
	
	public TaskNode() {
	}
	
	public TaskNode(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" [Node " + name + ", Status " + status.getStatus() + "]\n");
		buffer.append("  [Index " + index + ", Order " + order + "]\n");
		buffer.append("  [Task " + (task!=null?task.getCanonicalName():"") + "]\n");
		buffer.append("  [Pre " + (pre!=null?pre.getCanonicalName():"") + "]\n");
		buffer.append("  [Post " + (post!=null?post.getCanonicalName():"") + "]\n");
		buffer.append("  [Data " + data + "]\n\n");
		return buffer.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + index;
		result = prime * result + order;
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result + ((pre == null) ? 0 : pre.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskNode other = (TaskNode) obj;
		if (index != other.index)
			return false;
		if (order != other.order)
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		if (pre == null) {
			if (other.pre != null)
				return false;
		} else if (!pre.equals(other.pre))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Class<? extends ExecutableTask> getTask() {
		return task;
	}
	public void setTask(Class<? extends ExecutableTask> task) {
		this.task = task;
	}
	public Class<? extends ExecutableTask> getPre() {
		return pre;
	}
	public void setPre(Class<? extends ExecutableTask> pre) {
		this.pre = pre;
	}
	public Class<? extends ExecutableTask> getPost() {
		return post;
	}
	public void setPost(Class<? extends ExecutableTask> post) {
		this.post = post;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}