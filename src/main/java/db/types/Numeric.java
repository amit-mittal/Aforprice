package db.types;

public abstract class Numeric implements ISQLType {

	@Override
	public String getRHValue() {
		return toString();
	}

}
