/******************************************
** ProdAttributeDetails.java Created @Jul 2, 2012 11:50:19 PM
** @author: Jayanta Hazra
**
******************************************/
package entities;

public class ProdAttributeDetails {
	private String internalName;
	private int type;
	public ProdAttributeDetails(String internalName, int type) {
		this.internalName = internalName;
		this.type = type;		
	}
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
