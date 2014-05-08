package datastruct;

import java.util.TreeSet;

public class FixedSizeTreeSet<E extends Comparable> extends TreeSet<E> {
	private int maxSize = Integer.MAX_VALUE;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FixedSizeTreeSet(int maxSize){
		super();
		this.maxSize = maxSize;
	}
	
	/* (non-Javadoc)
	 * @see java.util.TreeSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		if(this.size()==this.maxSize){
			E last = this.last();
			if(last.compareTo(e)>0)//last element is bigger then new element
				this.remove(last);
			else//new element is bigger then last element, no-op
				return true;				
		}
		return super.add(e);
	}

	private static class TestObject implements Comparable<TestObject>{
		Integer a;
		public TestObject(Integer a){
			this.a =a;
		}
		public Integer getValue(){
			return a;
		}
		
		@Override
		public int compareTo(TestObject o) {
			return a.compareTo(o.getValue());
		}
		
		public String toString(){
			return a.toString();
		}
	}

	public static void main(String[] args){
		FixedSizeTreeSet<TestObject> x = new FixedSizeTreeSet<>(5);
		TestObject x1 = new TestObject(1);
		TestObject x2 = new TestObject(2);
		TestObject x3 = new TestObject(3);
		TestObject x4 = new TestObject(4);
		TestObject x5 = new TestObject(5);
		TestObject x6= new TestObject(6);
		
		x.add(x1);
		x.add(x2);
		x.add(x3);
		x.add(x4);
		x.add(x6);
		System.out.println(x);
		x.add(x5);
		System.out.println(x);
		
	}
}
