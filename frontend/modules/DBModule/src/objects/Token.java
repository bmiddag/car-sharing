package objects;

import java.util.EnumMap;

/**
 *
 * @author laurens
 */
public class Token extends DataObject {

    public enum Field implements DataObject.DataField {

	USER(Integer.class),
        TOKEN(Long.class, true, false),
        TIME(Long.class, false, true);

	private final boolean stamp;
        private final boolean key;
        private final Class type;

        private Field(Class type, boolean key, boolean stamp) {
            this.type = type;
            this.key = key;
            this.stamp = stamp;
        }

        private Field(Class type) {
            this.type = type;
            this.key = false;
            this.stamp = false;
        }

        @Override
        public Class getType() {
            return type;
        }

        @Override
        public boolean isKey() {
            return key;
        }

        @Override
        public boolean isStamp() {
            return stamp;
        }

    }
    
    public Token(Integer uid, Long token, Long time){
        super(new EnumMap<>(Field.class));
        data.put(Field.USER, uid);
        data.put(Field.TOKEN, token);
        data.put(Field.TIME, time);
    }   

    /**
     * @return the uid
     */
    public Integer getUid() {
        return (Integer) data.get(Field.USER);
    }

    /**
     * @return the token
     */
    public Long getToken() {
        return (Long) data.get(Field.TOKEN);
    }   

    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }     
  
}
