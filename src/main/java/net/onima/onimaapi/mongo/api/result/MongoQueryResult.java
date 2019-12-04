package net.onima.onimaapi.mongo.api.result;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;

@SuppressWarnings("unchecked")
public class MongoQueryResult extends MongoResult {
	
	private Map<String, Object> values;
	
	public boolean hasValue(String value) {
		return values.containsKey(value);
	}

	public <T> T getValue(String value, Class<T> clazz) {
		return clazz.cast(values.get(value));
	}
	
	public Collection<Object> getValues() {
		return values.values();
	}
	
	public Map<String, Object> getMap() {
		return values;
	}
	
	public void setMap(Map<String, Object> values) {
		this.values = values;
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public void add(String column, Object value) {
		Preconditions.checkArgument(column != null && !column.isEmpty(), "Une colonne est obligé d'avoir un nom !");
		values.put(column, value);
	}
	
	public <K, V> Map<K, V> valueToMap(String value, Class<K> mapKey, Class<V> mapValue) {
		return (Map<K, V>) getValue(value, Map.class);
	}
	
	public <T> List<T> valueToList(String value, Class<T> listValue) {
		return (List<T>) getValue(value, List.class);
	}

}
