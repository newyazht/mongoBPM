package org.jbpm.persistence.mongodb.rule.memory;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.persistence.mongodb.rule.EmbeddedFactHandle;

public class EmbeddedAccumulateNodeMemory extends EmbeddedNodeMemory {
	private static final long serialVersionUID = 1L;
	public static class Context implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private final EmbeddedFactHandle resultFactHandle;
		private final List<Integer> tupleFactHandleList = new ArrayList<Integer>();
		public Context(EmbeddedFactHandle resultFactHandle) {
			this.resultFactHandle = resultFactHandle;
		}
		
		public EmbeddedFactHandle getResultFactHandle() {
			return resultFactHandle;
		}
		
		public List<Integer> getTupleFactHandleList () {
			return tupleFactHandleList;
		}
		
		public int[] getTupleFactHandleArray() {
			int i = 0;
			int[] ret = new int[tupleFactHandleList.size()];
			for (int handleId: tupleFactHandleList) {
				ret[i++] = handleId;
			}
			return ret;
		}
		
		public void addTuple(int tupleFactHandle) {
			tupleFactHandleList.add(tupleFactHandle);
		}
	}
	private List<Context> contexts = new ArrayList<Context>();
	public EmbeddedAccumulateNodeMemory(int nodeId) {
		super(nodeId);
	}
	
	public List<Context> getContexts() {
		return contexts;
	}
	
	public Context addContext(EmbeddedFactHandle resultFactHandle) {
		Context context = new Context(resultFactHandle);
		contexts.add(context);
		return context;
	}
}
