package objects;

import java.util.EnumMap;

/**
 * This class keeps track of the attendance of a user to a certain infosession.
 * The present boolean indicates the presence of a person, when false the person
 * couldn't make it.
 */
public class Inscription extends DataObject {

    public enum Field implements DataField {

        ID(Integer.class, true, false),
	SESSION(InfoSession.class), 
	USER(User.class),
	PRESENT(Boolean.class);

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
     * @param session The relevant infosession.
     * @param user The person.
     * @param present Whether the person was in attendance.
     */
    public Inscription(Integer id, InfoSession session, User user, Boolean present) {

        super(new EnumMap<>(Field.class));
        data.put(Field.ID, id);
        data.put(Field.SESSION, session);
        data.put(Field.USER, user);
        data.put(Field.PRESENT, present);

    }

    
    
    /**
     *
     * @return the id of the privilege
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     * @return The relevant infosession the user attends
     */
    public InfoSession getSession() {
        return (InfoSession) data.get(Field.SESSION);
    }

    /**
     * @return The user that is attendint the infosession.
     */
    public User getUser() {
        return (User) data.get(Field.USER);
    }

    /**
     * @return Was the user present?
     */
    public Boolean getPresent() {
        return (Boolean) data.get(Field.PRESENT);
    }

    /**
     *
     * @param present Did the user attend?
     */
    public void setPresent(Boolean present) {
        data.put(Field.PRESENT, present);
    }
    
}
