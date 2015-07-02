package net.bitearth.tessellation;

public class Triangle {
	public final static int MAX_DIVISION_LEVEL = 14; 
	
	//same for north/south pole but in south pole the index has 3rd bit set (which must be removed before using this)
	//next traingle is those at edge 2 in northern and edge 1 in southern
	public final static int[] OCTAHEDRON_NEXT_TRIANGLES = new int[]{1, 3, 0, 2};
	
	//next traingle is those at edge 1. At edge 0 is the other hemisphere
	public final static int[] OCTAHEDRON_PREV_TRIANGLES = new int[]{2, 0, 3, 1};
	
	public static long getNeighbor(long triangleAddress, int level, int edgeIndex) {
		NeighborInPlaneResult res = new NeighborInPlaneResult();
		getNeightborInPlane(triangleAddress, level, edgeIndex, res);
		if (res.rotate == -1) {
			long result = res.address & 0b111;
			for (int l = 1; l <= level; l++) {
				result |= ((((res.address >> (l * 2 + 1)) & 0b11) + 1) % 3) << (l * 2 + 1);
			}
			return result;
		} else if (res.rotate == 1) {
			long result = res.address & 0b111;
			for (int l = 1; l <= level; l++) {
				result |= ((((res.address >> (l * 2 + 1)) & 0b11) + 3 - 1) % 3) << (l * 2 + 1);
			}
			return result;
		}
		return res.address;
	}
	
	
	//TODO this can be optimized in a single long
	private static class NeighborInPlaneResult {
		long address;
		int rotate; //rotate the result to get the actual triangle. -1=ccw, 1=cw, 0=no rotation 
	}
	
	private static void getNeightborInPlane(long triangleAddress, int level, int edgeIndex, NeighborInPlaneResult result) {
		assert (triangleAddress & (-1 << (level * 2 + 3))) == 0;
		assert(level >= 0);
		assert(edgeIndex >= 0 && edgeIndex <= 2);
		
		if (level <= 0) {
			//octahedron triangle
			if (edgeIndex == 0) {
				//only change the hemisphere
				result.address = triangleAddress ^ 0b100;
			} else {
				//can be written better but will not be clear
				if ((triangleAddress & 0b100) == 0) {
					//northern hemisphere
					if (edgeIndex == 1) {
						//edge 1 - go to prev triangle and set that the result must be rotated ccw
						result.address = OCTAHEDRON_PREV_TRIANGLES[(int) triangleAddress];
						result.rotate = -1;
					} else {
						//edge 2 - go to next triangle and set that the result must be rotated clockwise
						result.address = OCTAHEDRON_NEXT_TRIANGLES[(int) triangleAddress];
						result.rotate = 1;
					}
				} else {
					//southern
					if (edgeIndex == 1) {
						//edge 1 - go to next triangle and set that the result must be rotated clockwise
						result.address = OCTAHEDRON_NEXT_TRIANGLES[(int)(triangleAddress & 0b11)] | 0b100;
						result.rotate = -1;
					} else {
						//edge 2 - go to previous triangle and set that the result must be rotated ccw
						result.address = OCTAHEDRON_PREV_TRIANGLES[(int)(triangleAddress & 0b11)] | 0b100;
						result.rotate = 1;
					}
				}
			}
		} else {
			long parent = getParent(triangleAddress, level);
			
			int triangleType = getTriangleType(triangleAddress, level);
			if (triangleType == 0b11) {
				//triangle # 3 is surrounded by the other parts of the parent
				//to edge 0 corresponds triangle 2 from the parent, 1->0, 2->1
				result.address = parent | (((edgeIndex + 2) % 3L) << (level * 2 + 1));
			} else {
				//triangle type 2 has triangle type 3 from the same parent at edge 0
				//type 1 at edge 2
				//type 0 at edge 1
				if ((triangleType + 1) % 3 == edgeIndex) {
					result.address = parent | (0b11L << (level * 2 + 1));
				} else {
					//find the neighbor at same edge of the parent
					getNeightborInPlane(parent, level - 1, edgeIndex, result);
					
					//now inside the parent neighbor find the triangle that is next to ours. It is either with
					//edgeindex or with edgeindex + 1 depending on whether ours is with edge index or not
					if (triangleType == edgeIndex) {
						result.address |= (((edgeIndex + 1) % 3L) << (level * 2 + 1));
					} else {
						result.address |= (edgeIndex << (level * 2 + 1));
					}
				}
			}
		}
		
	}

	public static int getTriangleType(long triangleAddress, int level) {
		return (int) ((triangleAddress >>> (level * 2 + 1)) & 0b11L);
	}

	public static long getParent(long triangleAddress, int level) {
		return (0xffffffffffffffffL >>> (64 - (level * 2 + 1))) & triangleAddress;
	}
	
	public static boolean isUpside(long address, int level) {
		//start with check if root is in northern or southern hemisphere
		boolean result = (address & 0b100) == 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			if ((address & (0b11 << (1 + lvl * 2))) == 0b11) {
				//every time we meet 11 switch = change the direction
				result = !result;
			}
		}
		
		return result;
	}
	/*
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Triangle) {
			Triangle other = (Triangle) obj;
			return other.level == this.level && other.address == this.address;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		//will collide with triangles from other levels, yet they should not be hashed together anyways.
		return (int) address;
	}*/
	/*
	public Triangle[] split() {
		Triangle[] result = new Triangle[4];
		
		Vertex3D[] edgeCenters = new Vertex3D[3];
		
		for (int i = 0; i < 3; i++) {
			edgeCenters[i] = new Vertex3D(v(i));
			edgeCenters[i].add(v( (i+1) % 3 ));
			edgeCenters[i].normalize(); //assume radius=1
		}
		
		
		return result;
	}*/
	
}
