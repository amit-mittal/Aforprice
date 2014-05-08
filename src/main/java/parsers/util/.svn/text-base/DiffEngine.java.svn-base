/******************************************
** DiffEngine.java Created @Jun 24, 2012 1:13:04 PM
** @author: Jayanta Hazra
**
******************************************/
package parsers.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import entities.GenericProduct;

public class DiffEngine<Key, Value extends GenericProduct> {
	private Map<Key, Value> baseRecords;
	private Comparator<Value> comparator;
	private Map<Key, Value> updates;
	private Map<Key, Value> expires;
	private Map<Key, Value> inserts;
	private Map<Key, Value> noChange;
	private boolean expireOld;
	public DiffEngine(Map<Key, Value> baseRecords, Comparator<Value> comparator, boolean expireOld) {
		this.baseRecords = baseRecords;
		this.comparator = comparator;
		updates = new HashMap<Key, Value>();
		expires = new HashMap<Key, Value>();
		inserts = new HashMap<Key, Value>();
		noChange = new HashMap<Key, Value>();
		this.expireOld = expireOld;
	}
	
	public void process( Key key, Value value) {
		Value record = baseRecords.get(key);
		if( record == null) {
			inserts.put(key, value);
		} else {
			int v = comparator.compare(value, record); // <-1 0 >1 new record older/same/newer
			if( v == 0) {
				noChange.put(key, value);
			} else {
				if (v > 0) { // new value newer
					if (expireOld) {
						expires.put(key, record);
						inserts.put(key, value);
					} else
						updates.put(key, value);
				}
				else { //new value older
					//if(expireOld) {			
//						value.setThruDate(value.getFromDate());
//						inserts.put(key, value);
					//}
				}
			}
		}
		baseRecords.put(key, value);
	}
	public Map<Key, Value> getUpdates() {
		return updates;
	}
//	public void setUpdates(Map<Key, Value> updates) {
//		this.updates = updates;
//	}
	public Map<Key, Value> getInserts() {
		return inserts;
	}
	public Map<Key, Value> getExpires() {
		return expires;
	}

//	public void setInserts(Map<Key, Value> inserts) {
//		this.inserts = inserts;
//	}
	public Map<Key, Value> getNoChange() {
		return noChange;
	}
//	public void setNoChange(Map<Key, Value> noChange) {
//		this.noChange = noChange;
//	}
	public int getCacheSize() {
		return baseRecords.size();
	}
	public Map<String, Integer> clearState() {
		Map<String, Integer> state = new HashMap<String, Integer>();
		state.put("INSERT", inserts.size());
		state.put("UPDATE", updates.size());
		state.put("EXPIRE", expires.size());
		state.put("NO_CHANGE", noChange.size());
		state.put("TOTAL_CACHE", baseRecords.size());
		inserts.clear();
		updates.clear();
		expires.clear();
		noChange.clear();
		baseRecords.clear();
		return state;
	}
}
