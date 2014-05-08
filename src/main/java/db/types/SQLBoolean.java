package db.types;

public class SQLBoolean extends Numeric {

	private final boolean value;
	public SQLBoolean(boolean b) {
		this.value = b;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.value);
	}

}
