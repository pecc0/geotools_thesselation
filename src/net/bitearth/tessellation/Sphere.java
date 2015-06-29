package net.bitearth.tessellation;

import java.util.HashMap;
import java.util.Map;

public class Sphere {
	public static long NORTH_POLE_ID = -1L;
	public static long SOUTH_POLE_ID = -2L;
	public static double RADIUS = 1;
	private LruCache<Long, Vertex3D> vertices;

	@SuppressWarnings("unchecked")
	private Map<Long, Triangle>[] triangles = (Map<Long, Triangle>[])new Map[10];
	
	public Sphere() {
		vertices = new LruCache<Long, Vertex3D>(1046527);
		
		//the octahedron vertices
		vertices.put(NORTH_POLE_ID, new Vertex3D(0, 0, RADIUS));
		vertices.put(SOUTH_POLE_ID, new Vertex3D(0, 0, -RADIUS));
		vertices.put(0b000L, new Vertex3D(-RADIUS, 0, 0));
		vertices.put(0b001L, new Vertex3D(0, -RADIUS, 0));
		vertices.put(0b010L, new Vertex3D(RADIUS, 0, 0));
		vertices.put(0b011L, new Vertex3D(0, RADIUS, 0));
		
		//the octahedron triangles
//		triangles[0] = new HashMap<Long, Triangle>(8);
//		triangles[0].put(0b000L, new Triangle(0b100L, 0b001L, 0b011L));
//		triangles[0].put(0b001L, new Triangle(0b101L, 0b010L, 0b000L));
//		triangles[0].put(0b010L, new Triangle(0b110L, 0b011L, 0b001L));
//		triangles[0].put(0b011L, new Triangle(0b111L, 0b000L, 0b010L));
//		triangles[0].put(0b100L, new Triangle(0b101L, 0b000L, 0b111L));
//		triangles[0].put(0b101L, new Triangle(0b110L, 0b001L, 0b100L));
//		triangles[0].put(0b110L, new Triangle(0b111L, 0b010L, 0b101L));
//		triangles[0].put(0b111L, new Triangle(0b100L, 0b011L, 0b110L));
		
		//undeletable triangles
		triangles[1] = new HashMap<Long, Triangle>(8 << 2);
		triangles[2] = new HashMap<Long, Triangle>(8 << 4);
		triangles[3] = new HashMap<Long, Triangle>(8 << 6);
		triangles[4] = new HashMap<Long, Triangle>(8 << 8);
		
		
	}
}
