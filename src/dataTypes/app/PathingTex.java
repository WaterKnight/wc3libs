package dataTypes.app;

public class PathingTex extends Wc3String {
	public PathingTex(String stringVal, String... aliases) {
		super(stringVal, aliases);
	}

	@Override
	public PathingTex decode(Object val) {
		return new PathingTex(val.toString());
	}

}