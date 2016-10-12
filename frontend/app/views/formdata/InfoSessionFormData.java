package views.formdata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import exceptions.DataAccessException;
import models.InfoSessionsModel;
import models.RoleModel;
import models.UserModel;

import objects.Address;
import objects.Inscription;
import objects.User;

import play.data.validation.ValidationError;
import utils.TimeUtils;

import static play.mvc.Controller.session;

/**
 * Backing class for the InfoSession data form.
 * Requirements:
 * <ul>
 * <li> All fields are public,
 * <li> All fields are of type String or List[String].
 * <li> A public no-arg constructor.
 * <li> A validate() method that returns null or a List[ValidationError].
 * </ul>
 */
public class InfoSessionFormData {
    public String id = "";
    public String addressStreet = "";
    public String addressNumber = "";
    public String addressZip = "";
    public String addressPlace = "";
    public String addressBus = "";
    public String date = "";
    public String time = "";
    public String places = "";
    public String owners = "false";
    public String inscribed = "";
    public List<String> inscriptionIds = new ArrayList<String>();
    public List<String> inscriptionNames = new ArrayList<String>();
    public List<String> inscriptions = new ArrayList<String>();
    public String remove = "";
    public String inscriptionOnly = "";
    public String inscriptionsDisabled = "";

    /** Required for form instantiation. */
    public InfoSessionFormData() {}

    /** Creates form data from the various properties of an InfoSession object */
    public InfoSessionFormData(int id, Address address, long date, int places, boolean owners, List<Inscription> inscriptions, boolean inscribed, boolean inscriptionsDisabled) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        this.id = String.valueOf(id);
        this.addressStreet = address.getStreet();
        this.addressNumber = String.valueOf(address.getNumber());
        this.addressZip = address.getPlace().getZip();
        this.addressPlace = address.getPlace().getName();
        this.addressBus = address.getBus() == null ? "" : address.getBus();
        this.date = TimeUtils.getFormattedDate("dd/MM/yyyy", date);
        this.time = TimeUtils.getFormattedDate("HH:mm", date);
        this.places = String.valueOf(places);
        this.owners = String.valueOf(owners);
        this.inscribed = String.valueOf(inscribed);
        this.inscriptionsDisabled = String.valueOf(inscriptionsDisabled);
        for(Inscription inscription : inscriptions) {
            this.inscriptionIds.add(String.valueOf(inscription.getUser().getId()));
            this.inscriptionNames.add(inscription.getUser().getName() + " " + inscription.getUser().getSurname());
            this.inscriptions.add(String.valueOf(inscription.getPresent()));
        }
    }
    /**
    * Validates Form<InfoSessionFormData>.
    * Called automatically in the controller by bindFromRequest().
    *
    * @return Null if valid, or a List[ValidationError] if problems found.
    */
    public List<ValidationError> validate() throws DataAccessException {
        List<ValidationError> errors = new ArrayList<>();
        User user = UserModel.getCurrentUser();
        if((RoleModel.hasPermission(RoleModel.Permission.EDIT_INFOSESSIONS))&&(!inscriptionOnly.equals("inscriptionOnly"))) {
            if(!remove.equals("removeSession")) {
                try {
                    TimeUtils.stringToLong("dd/MM/yyyy HH:mm",date + " " + time);
                } catch(Exception e) {
                    // Eigenlijk NumberFormatException of ParseException, maar zo testen we ook direct of het niet null is enzo!
                    errors.add(new ValidationError("date", "De opgegeven datum en het bijhorende tijdstip zijn ongeldig."));
                }
                if (addressStreet == null || addressStreet.length() == 0) {
                    errors.add(new ValidationError("addressStreet", "Gelieve een straatnaam in te vullen."));
                }
                try {
                    if (addressNumber == null || addressNumber.length() == 0 || (Integer.parseInt(addressNumber)<1)) throw new Exception();
                } catch(Exception e) {
                    errors.add(new ValidationError("addressNumber", "Gelieve een correct huisnummer in te vullen."));
                }
                try {
                    int zip = Integer.parseInt(addressZip);
                    if(zip < 1000 || zip > 9999) errors.add(new ValidationError("addressZip", "De postcode is ongeldig."));
                } catch(Exception e) {
                    // Eigenlijk NumberFormatException, maar zo testen we ook direct of het niet null is enzo!
                    errors.add(new ValidationError("addressZip", "De opgegeven postcode is ongeldig."));
                }
                if(addressBus != null) {
                    if (addressBus.length() > 3) errors.add(new ValidationError("addressBus", "De bus mag niet langer zijn dan 3 karakters."));
                }
                if (addressPlace == null || addressPlace.length() == 0) {
                    errors.add(new ValidationError("addressPlace", "Gelieve een plaatsnaam in te vullen."));
                }
                try {
                    for(String id: inscriptions) {
                        if(UserModel.getUserById(Integer.parseInt(id)).equals(null)) throw new Exception();
                    }
                } catch(Exception e) {
                    errors.add(new ValidationError("inscriptions", "Er is iets misgegaan met de aanwezigheidslijst. Gelieve de pagina te vernieuwen."));
                }
                try {
                    int min_places = 0;
                    if(!id.equals(null) && !id.equals("")) min_places = InfoSessionsModel.getInscriptionCount(InfoSessionsModel.getInfoSessionById(Integer.parseInt(id)));
                    if(inscribed.equals("editInscription")) {
                        if(id.equals(null) || id.equals("")) {
                            min_places += 1;
                        } else {
                            if(InfoSessionsModel.getUserInscription(user,InfoSessionsModel.getInfoSessionById(Integer.parseInt(id)))==null){
                                min_places += 1;
                            }
                        }
                    }
                    if(Integer.parseInt(places)<min_places) throw new Exception();
                } catch(Exception e) {
                    errors.add(new ValidationError("places", "Het opgegeven aantal plaatsen is ongeldig."));
                }
                if((!owners.equals("false")) && (!owners.equals("true"))) errors.add(new ValidationError("owners", "Er iets misgegaan achter de schermen. Gelieve de pagina te vernieuwen."));
            }
        }
        if((!inscribed.equals("editInscription")) && (!inscribed.equals("") && (!inscribed.equals(null)))) errors.add(new ValidationError("inscribed", "Er iets misgegaan met de inschrijving."));

        if(errors.size() > 0) return errors;
        return null;
    }
}
