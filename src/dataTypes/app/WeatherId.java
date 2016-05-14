package dataTypes.app;

import dataTypes.DataType;
import dataTypes.Stringable;
import misc.ObjId;

public class WeatherId extends ObjId {	
	protected WeatherId(String idString) {
		super(idString);
	}
	
	public static WeatherId valueOf(String id) {
		return new WeatherId(id.toString());
	}
	
	public static WeatherId valueOf(Stringable id) {
		return new WeatherId(id.toString());
	}
	
	@Override
	public WeatherId decode(Object val) {
		return valueOf(val.toString());
	}
}
