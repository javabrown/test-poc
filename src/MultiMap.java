import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
 

public class MultiMap<K,V> {
  private Map<K, List<V>> _alteredMap = null;
  
  public MultiMap() {
    _alteredMap = new HashMap<K, List<V>>();
  }
  
  public void insertValue(K key, V value) {
    if (_alteredMap.get(key) != null) {
      List<V> existingValue = _alteredMap.get(key);
      existingValue.add(value);
      _alteredMap.put(key, existingValue);
    }
    else{
      ArrayList<V> newList = new ArrayList<V>();
      newList.add(value);
      _alteredMap.put(key, newList);
    }
  }
  
  public List<V> getValue(K key){
    return _alteredMap.get(key);
  }
  
  public Map<K, List<V>> getMap(){
    return new HashMap(_alteredMap);
  }
  
  public int size() {
      return _alteredMap.size();
  }
  
  public Set<K> keySet(){
    return _alteredMap.keySet();
  }
  /**
   * Break split One-to-Many relation between K and V, return all values
   * including duplicates entry
   * 
   * @return
   */
  public List<V> values() {
    List<V> output = new ArrayList<V>();
    for (K key : _alteredMap.keySet()) {
      List<V> value = _alteredMap.get(key);
      output.addAll(value);
    }

    return output;
  }
}
