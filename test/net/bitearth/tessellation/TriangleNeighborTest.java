package net.bitearth.tessellation;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TriangleNeighborTest {

	@Test
	public void testSameOctahedronTriangle() {
		Assert.assertEquals(0b1011010, Triangle.getNeighbor(0b1111010, 2, 0));
		
		Assert.assertEquals(0b0110010, Triangle.getNeighbor(0b0011010, 2, 0));
		
		Assert.assertEquals(0b1110010, Triangle.getNeighbor(0b1010010, 2, 0));
	}
	
	@Test
	public void testInterOctahedronTriangles() {
		Assert.assertEquals(0b0001101, Triangle.getNeighbor(0b0100001, 2, 0));
		
		Assert.assertEquals(0b0101101, Triangle.getNeighbor(0b0000001, 2, 0));
		
		Assert.assertEquals(0b0010010, Triangle.getNeighbor(0b0110001, 2, 1));
		
		Assert.assertEquals(0b1001000, Triangle.getNeighbor(0b1000001, 2, 2));
		
		Assert.assertEquals(0b0110110, Triangle.getNeighbor(0b0010101, 2, 2));
	}
}
