package net.bitearth.tessellation;

public class Triangle {
	public final static int MAX_DIVISION_LEVEL = 14; 
	
	private final long neighbour0, neighbour1, neighbour2; //The neighbor triangles within the corresponding level
	
	public Triangle(long neighbour0, long neighbour1, long neighbour2) {
		super();
		this.neighbour0 = neighbour0;
		this.neighbour1 = neighbour1;
		this.neighbour2 = neighbour2;
	}

	/**
	 * 
	 * @param index 0..2
	 * @return
	 */
	public long getNeighbour(int index) {
		switch (index) {
		case 0:
			return neighbour0;
		case 1:
			return neighbour1;
		case 2:
			return neighbour2;
		}
		return -1;
	}
/*
	public Vertex3D v(int index) {
		switch (index) {
		case 0:
			return v0;
		case 1:
			return v1;
		case 2:
			return v2;
		}
		return null;
	}
	*/
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
