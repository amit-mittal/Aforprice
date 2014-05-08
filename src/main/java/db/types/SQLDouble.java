package db.types;

public class SQLDouble extends Numeric {

	private final double d;
	public SQLDouble(double d) {
		this.d = d;
	}
	
	@Override
	public String toString(){
		return String.valueOf(d);
	}
}
