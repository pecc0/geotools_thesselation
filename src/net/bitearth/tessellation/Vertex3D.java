package net.bitearth.tessellation;

public class Vertex3D {
	private double x, y, z;

	public Vertex3D() {
		
	}
	
	public Vertex3D(Vertex3D other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public Vertex3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Vertex3D add(Vertex3D other) {
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}

	public double getLength() {
		return Math.sqrt(getLengthSq());
	}
	
	public double getLengthSq() {
		return x*x + y*y + z*z; 
	}
	
	public Vertex3D normalize() {
		double len = getLengthSq();
		if (len == 0) {
			return this;
		}
		len = MathUtils.invSqrt(len);

		x = x * len;
		y = y * len;
		z = z * len;
		return this;
	}
	
	public void setLength(double len) {
		normalize();
		x = x * len;
		y = y * len;
		z = z * len;
	}
}
