package util;

public class MutableInt {

	private int value;
	public MutableInt(int initial) {
		this.value = initial;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public int increment(){
		return ++value;
	}
	
	public int decrement(){
		return --value;
	}
	
	public int add(int amount){
		return value += amount;
	}
	
	public int subtract(int amount){
		return value -= amount;
	}
	
	public String toString(){
		return String.valueOf(value);
	}
}
