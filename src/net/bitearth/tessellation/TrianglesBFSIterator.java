package net.bitearth.tessellation;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TrianglesBFSIterator {

	private int level;
	private LinkedList<Long> queue = new LinkedList<Long>();
	private HashSet<Long> used = new HashSet<Long>();
	
	public TrianglesBFSIterator(long startAddress, int level) {
		this.level = level;
		this.queue.addLast(startAddress);
		used.add(startAddress);
	}
	
	public long next() {
		Long current = queue.removeFirst();
		if (current != null) {
			for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++) {
				long ngh = Triangle.getNeighbor(current, level, edgeIndex);
				if (!used.contains(ngh)) {
					used.add(ngh);
					queue.addLast(ngh);
				}
			}
			return current;
		}
		return 0;
	}

	public boolean hasNext() {
		return !queue.isEmpty();
	}
	
	
	public int getLevel() {
		return level;
	}
	
	public Set<Long> getUsed() {
		return Collections.unmodifiableSet(used);
	}
}
