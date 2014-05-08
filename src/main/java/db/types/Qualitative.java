package db.types;

public abstract class Qualitative implements ISQLType {

	@Override
	public String getRHValue() {
		return "'" + toString() + "'";
	}

}
