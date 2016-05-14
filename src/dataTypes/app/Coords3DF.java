package dataTypes.app;

public class Coords3DF {
	private Real _x;
	private Real _y;
	private Real _z;
	
	public Real getX() {
		return _x;
	}

	public Real getY() {
		return _y;
	}
	
	public Real getZ() {
		return _z;
	}
	
	public Coords3DF(float x, float y, float z) {
		_x = Real.valueOf(x);
		_y = Real.valueOf(y);
		_z = Real.valueOf(z);
	}

	public Coords3DF(Real x, Real y, Real z) {
		this(x.toFloat(), y.toFloat(), z.toFloat());
	}
}
