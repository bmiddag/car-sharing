package objects;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import objects.DataObject.DataField;

public abstract class DataObject/*<E extends Enum<E> & DataField>*/ {
    
    public interface DataField {
        public String name();
        public Class getType();
        public boolean isKey();
        public boolean isStamp();
    }
    
    protected final Map<DataField, Object> data;
    
    protected DataObject(EnumMap data) {
        //this.fields = null;
        this.data = data;
    }
    
    public <T> T getData(DataField field) {
        Class<T> c = field.getType();
        return c.cast(data.get(field));
    }
    
    protected Map<DataField, Object> getDataMap() {
        return data;
    }
    
    @Override
    public boolean equals(Object obj) {        
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        for (DataField f : data.keySet()) {
            Object o1 = data.get(f);
            Object o2 = ((DataObject)obj).getDataMap().get(f);
            if (!f.isStamp() && !(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.data);
        return hash;
    }
    
}
