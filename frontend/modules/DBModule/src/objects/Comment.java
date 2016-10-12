package objects;

import java.util.EnumMap;

/**
 * A comment to a Damage case.
 */
public class Comment extends DataObject {

    public enum Field implements DataField {
	
	ID(Integer.class, true, false),
	USER(Integer.class), 
	DAMAGE(Damage.class), 
	CONTENT(String.class),
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

    /**
     *
     * @param id Assigned by the DB
     * @param user The user commenting
     * @param damage The damage this comment is about
     * @param content The actual body of the comment
     * @param time TIme this comment was place
     */
    public Comment(Integer id, Integer user, Damage damage,
            String content, Long time) {

        super(new EnumMap<>(Field.class));

        data.put(Field.ID, id);
        data.put(Field.USER, user);
        data.put(Field.DAMAGE, damage);
        data.put(Field.CONTENT, content);
        data.put(Field.TIME, time);
        
    }
    
    

    /**
     * @return the id assigned by the DB
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return the user commenting
     */
    public Integer getUser() {
        return (Integer) data.get(Field.USER);
    }

    /**
     * @return the damage
     */
    public Damage getDamage() {
        return (Damage) data.get(Field.DAMAGE);
    }

    /**
     * @return the content
     */
    public String getContent() {
        return (String) data.get(Field.CONTENT);
    }

    /**
     * @param content the body of the comment
     */
    public void setContent(String content) {
        data.put(Field.CONTENT, content);
    }

    /**
     * @return the time this comment was placed
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }
}
