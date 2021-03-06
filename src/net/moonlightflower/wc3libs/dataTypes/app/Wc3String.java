package net.moonlightflower.wc3libs.dataTypes.app;

import net.moonlightflower.wc3libs.dataTypes.DataType;
import net.moonlightflower.wc3libs.dataTypes.Stringable;

public class Wc3String extends DataType implements Stringable {
	public static String name() {
		return "String";
	}

	public static Wc3String getDefVal() {
		return null;
	}
	
	private String _val;
	
	public String getVal() {
		return _val;
	}

	@Override
	public String toString() {
		return getVal();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;

		if (!((other instanceof Wc3String) || (other instanceof String))) return false;

		return (hashCode() == other.hashCode());
	}
	
	@Override
	public Object toSLKVal() {
		return toString();
	}

	@Override
	public Object toTXTVal() {
		return toString();
	}
	
	public Wc3String() {
		
	}
	
	public Wc3String(String val, String... aliases) {
		_val = val;
	}
	
	public static Wc3String valueOf(Object val) {
		if (val == null) return null;
		
		return new Wc3String(val.toString());
	}

	@Override
	public Wc3String decode(Object val) {
		return valueOf(val);
	}
	
	public static Wc3String decodeStatic(Object val) {
		return valueOf(val);
	}
}