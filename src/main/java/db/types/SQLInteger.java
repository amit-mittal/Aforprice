package db.types;

public class SQLInteger extends Numeric {

	private final int value;
	public SQLInteger(int value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.value);
	}

}
