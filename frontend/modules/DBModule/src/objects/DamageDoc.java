package objects;

import java.util.EnumMap;

/**
 * This class maps files of proof to damages.
 */
public class DamageDoc extends DataObject {

    public enum Field implements DataField {

	DAMAGE(Integer.class),
        FILE(Integer.class, true, false);

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
     * @param damage
     * @param file
     */
    public DamageDoc(Integer damage, Integer file) {
        super(new EnumMap<>(Field.class));
        data.put(Field.FILE, file);
        data.put(Field.DAMAGE, damage);
    }
    
    

    /**
     *
     * @return A document concerning the damage.
     */
    public Integer getFile() {
        return (Integer) data.get(Field.FILE);
    }

    /**
     *
     * @return the damage(case) this document is about?
     */
    public Integer getDamage() {
        return (Integer) data.get(Field.DAMAGE);
    }
    
}
