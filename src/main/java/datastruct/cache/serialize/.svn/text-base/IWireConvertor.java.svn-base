package datastruct.cache.serialize;

import datastruct.cache.ICacheRecord;

import java.util.Map;

/**
 * Created by Anurag on 3/16/14.
 */
public interface IWireConvertor<V extends ICacheRecord> {

    ICacheRecord unmarshall(String prefix, Map<String, String> fields);
    /**
     * Serialize the entry to a map. If there is an object with the following state
     * a=1
     * b=2
     * c={x:=1, y :=2}
     * d = Date("24Oct2014")
     * it should be serialized to the following map
     * {a:=1, b:=2, c.x:=1, c.y:=2, d:="24Oct2014"}
     * @return
     */
    public Map<String, String> marshall(V record);
}
