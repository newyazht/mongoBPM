package org.jbpm.persistence.mongodb.rule.memory;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.persistence.mongodb.rule.EmbeddedFactHandle;
import org.jbpm.persistence.mongodb.rule.memory.EmbeddedAccumulateNodeMemory.Context;

public class EmbeddedFromNodeMemory extends EmbeddedNodeMemory {
	public static class Context {
		private final List<EmbeddedFactHandle> handleList = new ArrayList<EmbeddedFactHandle>();
		private final List<Integer> tupleFactHandleList = new ArrayList<Integer>();
		
		public List<EmbeddedFactHandle> getHandleList() {
			return handleList;
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
	public EmbeddedFromNodeMemory(int nodeId) {
		super(nodeId);
	}
	
	public List<Context> getContexts() {
		return contexts;
	}
	
	public Context addContext() {
		Context context = new Context();
		contexts.add(context);
		return context;
	}
}
