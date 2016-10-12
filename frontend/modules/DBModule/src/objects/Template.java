package objects;

import java.util.EnumMap;

/**
 * A template Blob. I.e. a mail, notification, rapport ...
 */
public class Template extends DataObject {

    public enum Field implements DataField {

	ID(Integer.class, true, false),
	NAME(String.class), 
	CONTENT(String.class),
        REMOVABLE(Boolean.class),
	EDIT(Long.class, false, true);

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
     * @param id the id assigned by the db
     * @param name the name of the template
     * @param content the content of the template
     * @param edit the last time this template was edited
     */
    public Template(Integer id, String name, String content, Boolean removable, Long edit) {
        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.NAME, name);
        data.put(Field.EDIT, edit);
        data.put(Field.REMOVABLE, removable);
        data.put(Field.CONTENT, content);
    }
    
    public Boolean getRemovable() {
        return (Boolean) data.get(Field.REMOVABLE);
    }
    
    public void setRemovable(Boolean removable) {
        data.put(Field.REMOVABLE, removable);
    }

    /**
     *
     * @return the id generated by the db
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the last time this template was edited
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
    }

    /**
     *
     * @return the name of this template
     */
    public String getName() {
        return (String) data.get(Field.NAME);
    }

    /**
     *
     * @return the content of this template
     */
    public String getContent() {
        return (String) data.get(Field.CONTENT);
    }

    /**
     *
     * @param content the content of this template
     */
    public void setContent(String content) {
        data.put(Field.CONTENT, content);
    }

    /**
     *
     * @param name the name of this template
     */
    public void setName(String name) {
        data.put(Field.NAME, name);
    }

}