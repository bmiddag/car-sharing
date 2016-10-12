package objects;

import java.util.EnumMap;

/**
 * A user with his/her data.
 */
public class User extends DataObject {

    public enum Field implements DataField {
	
	ID(Integer.class, true, false),
	ROLE(Role.class), 
	TITLE(String.class), 
	NAME(String.class), 
	SURNAME(String.class), 
	MAIL(String.class),
        MAILVERIFIED(Boolean.class),
	PASS(String.class), 
	PHONE(String.class), 
	GUARANTEE(Integer.class), 
	PAST(String.class), 
	ZONE(Zone.class), 
	ADDRESS(Address.class), 
	DOMICILE(Address.class), 
	LICENSE(License.class), 
	IDCARD(IdCard.class),
        PHOTO(Integer.class),
        APPROVED(Boolean.class),
	TIME(Long.class, false, true), 
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
     * @param role the role this user has in the system
     * @param title the title by which to address this user
     * @param name the user's first name
     * @param surname the user's last name
     * @param mail the user's e-mail address
     * @param login the user's login name
     * @param pass the user's password (hashed and salted)
     * @param phone the user's telephone number
     * @param guarantee the amount of money the user paid as guarantee
     * @param past a short description of the user's past
     * @param zone the zone to which the user belongs
     * @param address the user's address
     * @param domicile the user's domicile address
     * @param license the users driver's license
     * @param idcard the user's id card
     * @param time the time at which this user was added to the system
     * @param edit the last time this user was edited
     */
    public User(Integer id, Role role, String title, String name, String surname, String mail, Boolean mailverified,
            String pass, String phone, Integer guarantee, String past,
            Zone zone, Address address, Address domicile, License license,
            IdCard idcard, Integer photo, Boolean approved, Long time, Long edit) {

        super(new EnumMap<>(Field.class));
        
        data.put(Field.ID, id);
        data.put(Field.ROLE, role);
        data.put(Field.NAME, name);
        data.put(Field.SURNAME, surname);
        data.put(Field.TITLE, title);
        data.put(Field.MAIL, mail);
        data.put(Field.MAILVERIFIED, mailverified);
        data.put(Field.PASS, pass);
        data.put(Field.PHONE, phone);
        data.put(Field.PAST, past);
        data.put(Field.GUARANTEE, guarantee);
        data.put(Field.ZONE, zone);
        data.put(Field.ADDRESS, address);
        data.put(Field.DOMICILE, domicile);
        data.put(Field.LICENSE, license);
        data.put(Field.IDCARD, idcard);
        data.put(Field.PHOTO, photo);
        data.put(Field.APPROVED, approved);
        data.put(Field.TIME, time);
        data.put(Field.EDIT, edit);

    }
    
    public Integer getPhoto() {
        return (Integer) data.get(Field.PHOTO);
    }
    
    public Boolean getMailVerified() {
        return (Boolean) data.get(Field.MAILVERIFIED);
    }
    
    public void setMailVerified() {
        data.put(Field.MAILVERIFIED, true);
    }
    
    public Boolean getApproved() {
        return (Boolean) data.get(Field.APPROVED);
    }
    
    public void setApproved(Boolean approved) {
        data.put(Field.APPROVED, approved);
    }
    
    /**
     *
     * @return the id assigned to the user by the db
     */
    public Integer getId() {
        return (Integer) data.get(Field.ID);
    }

    /**
     *
     * @return the role of the user
     */
    public Role getRole() {
        return (Role) data.get(Field.ROLE);
    }


    /**
     *
     * @return the first name of the user
     */
    public String getName() {
        return (String) data.get(Field.NAME);
    }

    /**
     *
     * @return the last name of the user
     */
    public String getSurname() {
        return (String) data.get(Field.SURNAME);
    }

    /**
     *
     * @return the e-mail address of the user
     */
    public String getMail() {
        return (String) data.get(Field.MAIL);
    }

    /**
     *
     * @return the password (hashed and salted)
     */
    public String getPass() {
        return (String) data.get(Field.PASS);
    }

    /**
     *
     * @return the user's telephone number
     */
    public String getPhone() {
        return (String) data.get(Field.PHONE);
    }

    /**
     *
     * @return the amount of guarantee paid
     */
    public Integer getGuarantee() {
        return (Integer) data.get(Field.GUARANTEE);
    }

    /**
     *
     * @return a description of the user's past
     */
    public String getPast() {
        return (String) data.get(Field.PAST);
    }

    /**
     *
     * @return the user's address
     */
    public Integer getAddressID() {
        Address address = (Address) data.get(Field.ADDRESS);
        return address == null ? null : address.getId();
    }
    /**
     *
     * @return the user's address
     */
    public Integer getDomicileID() {
        Address domicile = (Address) data.get(Field.DOMICILE);
        return domicile == null ? null : domicile.getId();
    }

    /**
     *
     * @return the user's address
     */
    public Address getAddress() {
        return (Address) data.get(Field.ADDRESS);
    }

    /**
     *
     * @return the zone to which the user belongs
     */
    public Zone getZone() {
        return (Zone) data.get(Field.ZONE);
    }

    /**
     *
     * @return the user's address
     */
    public Address getDomicile() {
        return (Address) data.get(Field.DOMICILE);
    }

    /**
     *
     * @return the driver's license of the user
     */
    public License getLicense() {
        return (License) data.get(Field.LICENSE);
    }

    /**
     *
     * @return the user's id card
     */
    public IdCard getIdCard() {
        return (IdCard) data.get(Field.IDCARD);
    }

    /**
     *
     * @return the time at which the user was added to the system
     */
    public Long getTime() {
        return (Long) data.get(Field.TIME);
    }

    /**
     *
     * @return the time at which this user was last edited
     */
    public Long getEdit() {
        return (Long) data.get(Field.EDIT);
    }

    /**
     *
     * @return the title by which the user should be addressed
     */
    public String getTitle() {
        return (String) data.get(Field.TITLE);
    }
    
    public void setPhoto(Integer file) {
        data.put(Field.PHOTO, file);
    }

    /**
     *
     * @param title the title by which the user should be addressed
     */
    public void setTitle(String title) {
        data.put(Field.TITLE, title);
    }

    /**
     *
     * @param role the role of the user
     */
    public void setRole(Role role) {
        data.put(Field.ROLE, role);
    }


    /**
     *
     * @param name the user's first name
     */
    public void setName(String name) {
        data.put(Field.NAME, name);
    }

    /**
     *
     * @param surname the user's last name
     */
    public void setSurname(String surname) {
        data.put(Field.SURNAME, surname);
    }

    /**
     *
     * @param mail the user's e-mail address
     */
    public void setMail(String mail) {
        data.put(Field.MAIL, mail);
    }

    /**
     *
     * @param pass the user's password (hashed and salted)
     */
    public void setPass(String pass) {
        data.put(Field.PASS, pass);
    }

    /**
     *
     * @param phone the telephone number of the user
     */
    public void setPhone(String phone) {
        data.put(Field.PHONE, phone);
    }

    /**
     *
     * @param guarantee the amount of money the user paid as guarantee
     */
    public void setGuarantee(Integer guarantee) {
        data.put(Field.GUARANTEE, guarantee);
    }

    /**
     *
     * @param past the user's past (short description)
     */
    public void setPast(String past) {
        data.put(Field.PAST, past);
    }

    /**
     *
     * @param domicile the user's domicile address
     *//*
    public void setDomicile(Integer domicile) {
        data.put(Field.DOMICILE, domicile);
    }*/
    
    /**
     *
     * @param address the user's address
     *//*
    public void setAddress(Integer address) {
        data.put(Field.ADDRESS, address);
    }*/
    
    /**
     *
     * @param address the user's address
     */
    public void setAddress(Address address) {
        data.put(Field.ADDRESS, address);
    }

    /**
     *
     * @param domicile the user's domicile address
     */
    public void setDomicile(Address domicile) {
        data.put(Field.DOMICILE, domicile);
    }

    /**
     *
     * @param zone the zone to which the user belongs
     */
    public void setZone(Zone zone) {
        data.put(Field.ZONE, zone);
    }

    /**
     *
     * @param license the user's driver's license
     */
    public void setLicense(License license) {
        data.put(Field.LICENSE, license);
    }

    /**
     *
     * @param idcard the id card of the user
     */
    public void setIdCard(IdCard idcard) {
        data.put(Field.IDCARD, idcard);
    }

}
