package objects;

import java.util.EnumMap;

/**
 * A country with its code.
 */
public class Country extends DataObject {

    public enum Field implements DataField {

	CODE(String.class, true, false),
	NAME(String.class);

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

    };

    /**
     *
     * @param code Unique country code, ISO 3166-1 Either alpha 2 or 3.
     * @param name Country name
     */
    public Country(String code, String name) {
        super(new EnumMap<>(Field.class));
        data.put(Field.CODE, code);
        data.put(Field.NAME, name);
    }
    
    

    /**
     *
     * @return Country code.
     */
    public String getCode() {
        return (String) data.get(Field.CODE);
    }

    /**
     *
     * @return Country name.
     */
    public String getName() {
        return (String) data.get(Field.NAME);
    }

}
