package jdbc.util;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import static java.lang.System.exit;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import javax.sql.rowset.serial.SerialBlob;
import jdbc.JDBCDataAccessProvider;
import objects.*;

/**
 *
 * @author laurens
 */
public class GenerateMocks {
    
    private static DataAccessContext dac;
    static {
        try {
            dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        } catch (DataAccessException ex) {
            exit(1);
        }
    }
    
    public static final int NUMBER_OF_FILES = 20000-100;
    public static final int NUMBER_OF_USERS = 10000;
    public static final int NUMBER_OF_ZONES = 20;
    public static final int NUMBER_OF_ROLES = 4;
    public static final int NUMBER_OF_LICENSES = 10000;
    public static final int NUMBER_OF_IDCARDS = 5000;
    public static final int NUMBER_OF_CARS = 500;
    public static final int NUMBER_OF_INSCRIPTIONS = NUMBER_OF_CARS;
    public static final int NUMBER_OF_TEMPLATES = 100;
    public static final int NUMBER_OF_DAMAGES = 100;
    public static final int NUMBER_OF_INFOSESSIONS = 100;
    public static final int NUMBER_OF_INSURANCES = 500;
    public static final int NUMBER_OF_ADDRESSES = 10000;
    public static final int NUMBER_OF_PLACES = 2703;
    public static final int NUMBER_OF_ACTIONS = 100;
    public static final int NUMBER_OF_RESERVATIONS = 1000;
    public static final int NUMBER_OF_RIDES = 1000;
    public static final int NUMBER_OF_REFUELINGS = 600;

    static Random r = new Random();
    
    static String[] namepts = {"lau", "re", "ns", "wou", "ter", "ro", "bin", "ba", "rt", "st", "ijn", "ste", "ven", "gil", "les"};
    static String[] namepts2 = {"De ", "Grae", "ve", "Pin", "noo", " Ter", "mont ", "Pr", "aet", "Se", "gh", "ers", "Van ", "de", "wie", "le", "Mid", "dag", "De ", "Bli", "eck"};
    static String[] words = {"Sed", "ut", "perspiciatis", "unde", "omnis", "iste", "natus", "error", "sit", "voluptatem", "accusantium", "doloremque",
        "laudantium", "totam", "rem", "aperiam", "eaque", "ipsa", "quae", "ab", "illo", "inventore", "veritatis", "et", "quasi", "architecto", "beatae",
        "vitae", "dicta", "sunt", "explicabo.", "Nemo", "enim", "ipsam", "voluptatem", "quia", "voluptas", "sit", "aspernatur", "aut", "odit", "aut", "fugit",
        "sed", "quia", "consequuntur", "magni", "dolores", "eos", "qui", "ratione", "voluptatem", "sequi", "nesciunt.", "Neque", "porro", "quisquam", "est",
        "qui", "dolorem", "ipsum", "quia", "dolor", "sit", "amet", "consectetur", "adipisci", "velit", "sed", "quia", "non", "numquam", "eius", "modi",
        "tempora", "incidunt", "ut", "labore", "et", "dolore", "magnam", "aliquam", "quaerat", "voluptatem.", "Ut", "enim", "ad", "minima", "veniam", "quis",
        "nostrum", "exercitationem", "ullam", "corporis", "suscipit", "laboriosam", "nisi", "ut", "aliquid", "ex", "ea", "commodi", "consequatur?",
        "Quis", "autem", "vel", "eum", "iure", "reprehenderit", "qui", "in", "ea", "voluptate", "velit", "esse", "quam", "nihil", "molestiae", "consequatur",
        "vel", "illum", "qui", "dolorem", "eum", "fugiat", "quo", "voluptas", "nulla", "pariatur?", "At", "vero", "eos", "et", "accusamus", "et", "iusto", "odio",
        "dignissimos", "ducimus", "qui", "blanditiis", "praesentium", "voluptatum", "deleniti", "atque", "corrupti", "quos", "dolores", "et", "quas",
        "molestias", "excepturi", "sint", "occaecati", "cupiditate", "non", "provident", "similique", "sunt", "in", "culpa", "qui", "officia", "deserunt",
        "mollitia", "animi", "id", "est", "laborum", "et", "dolorum", "fuga.", "Et", "harum", "quidem", "rerum", "facilis", "est", "et", "expedita",
        "distinctio.", "Nam", "libero", "tempore", "cum", "soluta", "nobis", "est", "eligendi", "optio", "cumque", "nihil", "impedit", "quo", "minus",
        "id", "quod", "maxime", "placeat", "facere", "possimus", "omnis", "voluptas", "assumenda", "est", "omnis", "dolor", "repellendus.", "Temporibus",
        "autem", "quibusdam", "et", "aut", "officiis", "debitis", "aut", "rerum", "necessitatibus", "saepe", "eveniet", "ut", "et", "voluptates",
        "repudiandae", "sint", "et", "molestiae", "non", "recusandae.", "Itaque", "earum", "rerum", "hic", "tenetur", "a", "sapiente", "delectus", "ut",
        "aut", "reiciendis", "voluptatibus", "maiores", "alias", "consequatur", "aut", "perferendis", "doloribus", "asperiores", "repellat.", "Sed", "ut",
        "perspiciatis", "unde", "omnis", "iste", "natus", "error", "sit", "voluptatem", "accusantium", "doloremque", "laudantium", "totam", "rem",
        "aperiam", "eaque", "ipsa", "quae", "ab", "illo", "inventore", "veritatis", "et", "quasi", "architecto", "beatae", "vitae", "dicta", "sunt",
        "explicabo.", "Nemo", "enim", "ipsam", "voluptatem", "quia", "voluptas", "sit", "aspernatur", "aut", "odit", "aut", "fugit", "sed", "quia",
        "consequuntur", "magni", "dolores", "eos", "qui", "ratione", "voluptatem", "sequi", "nesciunt.", "Neque", "porro", "quisquam", "est", "qui",
        "dolorem", "ipsum", "quia", "dolor", "sit", "amet", "consectetur", "adipisci", "velit", "sed", "quia", "non", "numquam", "eius", "modi", "tempora",
        "incidunt", "ut", "labore", "et", "dolore", "magnam", "aliquam", "quaerat", "voluptatem.", "Ut", "enim", "ad", "minima", "veniam", "quis", "nostrum",
        "exercitationem", "ullam", "corporis", "suscipit", "laboriosam", "nisi", "ut", "aliquid", "ex", "ea", "commodi", "consequatur?", "Quis", "autem",
        "vel", "eum", "iure", "reprehenderit", "qui", "in", "ea", "voluptate", "velit", "esse", "quam", "nihil", "molestiae", "consequatur", "vel", "illum",
        "qui", "dolorem", "eum", "fugiat", "quo", "voluptas", "nulla", "pariatur?"};
    static String[] words2 = {"But", "I", "must", "explain", "to", "you", "how", "all", "this", "mistaken", "idea", "of", "denouncing", "pleasure",
        "and", "praising", "pain", "was", "born", "and", "I", "will", "give", "you", "a", "complete", "account", "of", "the", "system", "and", "expound",
        "the", "actual", "teachings", "of", "the", "great", "explorer", "of", "the", "truth", "the", "master-builder", "of", "human", "happiness.", "No",
        "one", "rejects", "dislikes", "or", "avoids", "pleasure", "itself", "because", "it", "is", "pleasure", "but", "because", "those", "who", "do",
        "not", "know", "how", "to", "pursue", "pleasure", "rationally", "encounter", "consequences", "that", "are", "extremely", "painful.", "Nor",
        "again", "is", "there", "anyone", "who", "loves", "or", "pursues", "or", "desires", "to", "obtain", "pain", "of", "itself", "because", "it", "is",
        "pain", "but", "because", "occasionally", "circumstances", "occur", "in", "which", "toil", "and", "pain", "can", "procure", "him", "some", "great",
        "pleasure.", "To", "take", "a", "trivial", "example", "which", "of", "us", "ever", "undertakes", "laborious", "physical", "exercise", "except", "to",
        "obtain", "some", "advantage", "from", "it?", "But", "who", "has", "any", "right", "to", "find", "fault", "with", "a", "man", "who", "chooses", "to",
        "enjoy", "a", "pleasure", "that", "has", "no", "annoying", "consequences", "or", "one", "who", "avoids", "a", "pain", "that", "produces", "no",
        "resultant", "pleasure?", "On", "the", "other", "hand", "we", "denounce", "with", "righteous", "indignation", "and", "dislike", "men", "who",
        "are", "so", "beguiled", "and", "demoralized", "by", "the", "charms", "of", "pleasure", "of", "the", "moment", "so", "blinded", "by", "desire",
        "that", "they", "cannot", "foresee", "the", "pain", "and", "trouble", "that", "are", "bound", "to", "ensue;", "and", "equal", "blame", "belongs",
        "to", "those", "who", "fail", "in", "their", "duty", "through", "weakness", "of", "will", "which", "is", "the", "same", "as", "saying", "through",
        "shrinking", "from", "toil", "and", "pain.", "These", "cases", "are", "perfectly", "simple", "and", "easy", "to", "distinguish.", "In", "a", "free",
        "hour", "when", "our", "power", "of", "choice", "is", "untrammelled", "and", "when", "nothing", "prevents", "our", "being", "able", "to", "do",
        "what", "we", "like", "best", "every", "pleasure", "is", "to", "be", "welcomed", "and", "every", "pain", "avoided.", "But", "in", "certain",
        "circumstances", "and", "owing", "to", "the", "claims", "of", "duty", "or", "the", "obligations", "of", "business", "it", "will", "frequently",
        "occur", "that", "pleasures", "have", "to", "be", "repudiated", "and", "annoyances", "accepted.", "The", "wise", "man", "therefore", "always",
        "holds", "in", "these", "matters", "to", "this", "principle", "of", "selection:", "he", "rejects", "pleasures", "to", "secure",
        "other", "greater", "pleasures", "or", "else", "he", "endures", "pains", "to", "avoid", "worse", "pains."};
    static String[] places = {"Eine",
        "Ename", "Heurne", "Leupegem", "Mater", "Melden", "Mullem", "Nederename", "Volkegem", "Welden", "Oudenaarde", "Huise", "Ouwegem", "Zingem", "Kruishoutem", "Nokere", "Wannegem-Lede", "Elsegem", "Moregem",
        "Ooike (Wortegem-Petegem)", "Petegem-aan-de-Schelde", "Wortegem", "Wortegem-Petegem", "Astene", "Bachte-Maria-Leerne", "Gottem", "Grammene", "Meigem", "Petegem-aan-de-Leie", "Sint-Martens-Leerne",
        "Vinkt", "Wontergem", "Zeveren", "Deinze", "Eke", "Nazareth", "Bottelare", "Lemberge", "Melsen", "Munte", "Schelderode", "Merelbeke", "Sint-Martens-Latem", "Deurle", "Zevergem",
        "De Pinte", "Hansbeke", "Landegem", "Merendree", "Poesele", "Vosselare", "Nevele", "Balegem", "Gijzenzele", "Landskouter", "Moortsele", "Scheldewindeke", "Oosterzele", "Machelen (O.-Vl.)",
        "Olsene", "Zulte", "Lotenhulle", "Poeke", "Aalter", "Bellem", "Asper", "Baaigem", "Dikkelvenne", "Semmerzake", "Vurste", "Gavere", "Eeklo", "Ursel", "Knesselare", "Lovendegem",
        "Vinderhoute", "Zomergem", "Oostwinkel", "Ronsele", "Ertvelde", "Kluizen", "Sleidinge", "Evergem", "Waarschoot", "Assenede", "Boekhoute", "Bassevelde", "Oosteeklo", "Kaprijke",
        "Lembeke", "Sint-Laureins", "Sint-Margriete", "Sint-Jan-in-Eremo", "Waterland-Oudeman", "Watervliet", "Maldegem", "Adegem", "Middelburg"};

    public static String[] titles = {"Mevr", "Dhr"};

    /*-------------------------------------------------------------------------*/
    
    public static String generateTitle() {
        return r.nextBoolean() ? "Mevr" : "Dhr"; /* Ladies first */
    }

    public static String generateCityName() {
        return generateString(20);
    }

    public static String generateBus() {
        switch (r.nextInt(100)) {
            case 0:
                return "a";
            case 1:
                return Character.toString((char) ('a' + (char) r.nextInt(26)));
            case 2:
                return Integer.toString(r.nextInt(1000));
            default:
                return null;
        }
    }

    public static String generateURL() {
        String url = "";
        for (int i = 0; i < r.nextInt(12) + 1; i++) {
            url += "/" + generateRandomWord();
        }
        return url;
    }

    public static String generateZip() {
        return Integer.toString(r.nextInt(100) + 9000);
    }

    public static String generateFirstName(int parts) {
        String name = "";
        parts = r.nextInt(parts) + 1;
        while (parts-- > 0) {
            name += namepts[r.nextInt(namepts.length)];
        }

        return name;
    }

    public static String generateLastName(int parts) {
        String name = "";
        parts = r.nextInt(parts) + 1;

        while (parts-- > 0) {
            name += namepts2[r.nextInt(namepts2.length)];
        }

        return name;
    }

    public static String generateRandomName(int parts) {
        String name = "";
        parts = r.nextInt(parts) + 1;
        while (parts-- > 0) {
            name += r.nextBoolean() ? namepts[r.nextInt(namepts.length)] : namepts2[r.nextInt(namepts2.length)];
        }
        return name;
    }

    public static String generateRandomPhone() {
        String num = "";
        for (int i = 0; i < 9; i++) {
            num += r.nextInt(10);
        }
        return "+32" + num;
    }

    public static String[] TLD = {".com", ".mil", ".be", ".nl", ".de", ".fr"};

    public static String generateMail() {
        return generateRandomName(10) + "@" + generateRandomName(5) + TLD[r.nextInt(TLD.length)];
    }
    
    public static Blob generateBlob(){
        byte[] bytes = new byte[r.nextInt(1000)+1];
        r.nextBytes(bytes);
        try {
            return new SerialBlob(bytes);
        } catch (SQLException ex) {
            return null;
        }
    }

    public static String generateTextMaybeEmpty(double chance, int words) {
        if (r.nextDouble() < chance) {
            return null;
        }
        return generateText(words);
    }

    public static String generateText(int words) {
        String text = "";
        while (words-- > 0) {
            text += " " + words2[r.nextInt(words2.length)];
        }
        return text;
    }

    public static Long generateDate() {
        try {
            //return Long.valueOf(r.nextInt(60 * 60 * 24 * 365));
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(""+r.nextInt(9999)+"-"+r.nextInt(12)+"-"+r.nextInt(28)+" "+r.nextInt(24)+":"+r.nextInt(60)+":"+r.nextInt(60)).getTime();
        } catch (ParseException ex) {
            return new Long(0);
        }
    }
    
    public static Timestamp generateTimestamp() {
        return new Timestamp((long)r.nextInt());
    }

    public static int generateInt() {
        return r.nextInt(10000);
    }
    
    public static long generateLong() {
        return r.nextLong();
    }

    public static float generateFloat() {
        return r.nextFloat();
    }

    public static float generateDouble() {
        return r.nextFloat();
    }

    public static String generateRandomWord() {
        return generateString(r.nextInt(15) + 1);
    }

    public static boolean generateBoolean() {
        return r.nextBoolean();
    }
    private static final String[] delims = {"", "-", " ", "_", "."};

    public static String generatePlate() {
        String delim = delims[r.nextInt(delims.length)];
        switch (r.nextInt() % 100) {
            case 0:
                return Integer.toString(r.nextInt(1000000)); // kentekens tussen 1899-1921 en 1926-1953
            case 3:
                return generateString(1) + delim + r.nextInt(10000);
            case 6:
                return r.nextInt(10000) + delim + generateString(1); // kentekens tussen 21-26
            case 7:
                return generateString(1) + delim + r.nextInt(1000) + delim + generateString(1);
            case 13:
                return generateString(3) + delim + r.nextInt(1000);
            case 14:
                return r.nextInt(1000) + delim + generateString(3);
            default:
                return r.nextInt(10) + delim + generateString(3) + delim + r.nextInt(1000);
        }
    }

    public static BigDecimal GenerateBigDecimal() {
        return new BigDecimal(Float.toString((r.nextFloat())));
    }
    
    public static float GenerateFloat() {
        return r.nextFloat();
    }

    public static String generateString(int i) {
        char[] result = new char[i];
        while (i-- != 0) {
            char base = r.nextBoolean() ? 'a' : 'A';
            result[i] = ((char) (base + r.nextInt(26)));
        }
        return String.valueOf(result);
    }
    
    /*-----------------------------------------------------------------------*/
    
    public static Address randomAddress() {
        try {
            DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
            dap.init("test", "root", "root");
            dac = dap.getDataAccessContext();

            return dac.getAddressDAO().getAddress(r.nextInt(NUMBER_OF_ADDRESSES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE ADDRESSES: " + ex.getMessage());
        }
    }

    public static User randomUser() {
        try {
            DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
            dap.init("test", "root", "root");
            dac = dap.getDataAccessContext();
            return dac.getUserDAO().getUser(r.nextInt(NUMBER_OF_USERS) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE USERS: " + ex.getMessage());
        }
    }
    
    public static Place randomPlace() {
        try {
            DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
            dap.init("test", "root", "root");
            dac = dap.getDataAccessContext();
            return dac.getPlaceDAO().getPlace(r.nextInt(NUMBER_OF_PLACES - 100) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE PLACES: " + ex.getMessage());
        }
    }
    
    public static Role RandomRole() {

        try {
            return dac.getRoleDAO().getRole(r.nextInt(NUMBER_OF_ROLES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE ADDRESSES: " + ex.getMessage());
        }
    }

    public static Address RandomAddress() {
        try {
            return dac.getAddressDAO().getAddress(r.nextInt(NUMBER_OF_ADDRESSES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE ADDRESSES: " + ex.getMessage());
        }
    }

    public static Zone RandomZone() {
        try {
            return dac.getZoneDAO().getZone(r.nextInt(NUMBER_OF_ZONES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE ZONE: " + ex.getMessage());
        }
    }

    public static License RandomLicense() {
        try {
            return dac.getLicenseDAO().getLicense(r.nextInt(NUMBER_OF_LICENSES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE LICENSE: " + ex.getMessage());
        }
    }

    public static IdCard RandomIdCard() {
        try {
            return dac.getIdCardDAO().getIdCard(r.nextInt(NUMBER_OF_IDCARDS) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE IDCARD: " + ex.getMessage());
        }
    }

    public static Car RandomCar() {
        try {
            return dac.getCarDAO().getCar(r.nextInt(NUMBER_OF_CARS-150)+1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE CAR: " + ex.getMessage());
        }
    }

    public static Ride RandomRide() {
        try {
            return dac.getRideDAO().getRide(r.nextInt(NUMBER_OF_RIDES-150)+1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE RIDE: " + ex.getMessage());
        }
    }

    public static File RandomFile() {
        try {
            return dac.getFileDAO().getFile(r.nextInt(NUMBER_OF_FILES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE FILE: " + ex.getMessage());
        }
    }

    public static Damage RandomDamage() {
        try {
            return dac.getDamageDAO().getDamage(r.nextInt(NUMBER_OF_DAMAGES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE IDCARD: " + ex.getMessage());
        }
    }

    public static InfoSession RandomInfosession() {
        try {
            return dac.getInfoSessionDAO().getInfoSession(r.nextInt(NUMBER_OF_INFOSESSIONS) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE INFOSESSION: " + ex.getMessage());
        }
    }

    public static Insurance RandomInsurance() {
        try {
            return dac.getInsuranceDAO().getInsurance(r.nextInt(NUMBER_OF_INSURANCES) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE INSURANCES: " + ex.getMessage());
        }
    }

    public static User RandomUser() {
        try {
            return dac.getUserDAO().getUser(r.nextInt(NUMBER_OF_USERS) + 1);
        } catch (DataAccessException ex) {
            throw new UnsupportedOperationException("COULDN'T RETRIEVE USER: " + ex.getMessage());
        }
    }
    
    /* -------------------------------------------------------------------------
    
    
    private static void genRefueling() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter writer = new PrintWriter("refuelings.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_REFUELINGS; i++) {
                writer.printf("%d,%d,%s,%s,%d,%s,%d,%s\n",
                        r.nextInt(NUMBER_OF_USERS) + 1,
                        r.nextInt(NUMBER_OF_CARS - 140) + 1,
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        r.nextInt(NUMBER_OF_FILES - 100) + 1,
                        r.nextBoolean() ? "Diesel" : "Super+ 98",
                        r.nextInt(2),
                        String.valueOf(r.nextDouble() * 500).replaceAll(",", "."));
            }
            System.out.println("refueling created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void genInsurances() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("insurances.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_INSURANCES; i++) {
                writer.printf("%d,%s,%s,%d,%d,2014-0%d-%d%d\n", r.nextInt(NUMBER_OF_CARS-150) + 1,generateString(16), generateRandomName(4),
                        r.nextInt(15),r.nextInt(2),r.nextInt(10)+1,r.nextInt(3),r.nextInt(7)+1 );
            }
            System.out.println("insurances created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }    
    }

    public GenerateMocks() throws DataAccessException {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        dap.init("test", "root", "root");
        dac = dap.getDataAccessContext();
    }
    
    private static void genLicenses() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("licenses.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_LICENSES; i++) {
                writer.printf("%d,%s\n", r.nextInt(NUMBER_OF_FILES - 100) + 1, generateString(10));
            }
            System.out.println("licenses created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    private static void genAction() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter writer = new PrintWriter("actions.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_ACTIONS; i++) {
                writer.printf("%s,%d,%s\n", new Date(generateDate()).toString(), r.nextInt(60 * 60 * 24 * 365), generateRandomWord());
            }
            System.out.println("actions created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateInfosession() {
        try {
            PrintWriter writer = new PrintWriter("infosessions.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_INFOSESSIONS; i++) {
                writer.printf("%d,%s,%d,%d\n", r.nextInt(NUMBER_OF_ADDRESSES), new Date(generateDate()).toString(), r.nextInt(2), r.nextInt(32000) + 1);
            }
            System.out.println("INFS created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genUsers() {
        try {
            //GenerateMocks GM = new GenerateMocks();
            PrintWriter writer = new PrintWriter("users.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_USERS; i++) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%d,%d\n",
                        r.nextInt(NUMBER_OF_ROLES) + 1,
                        generateFirstName(r.nextInt(7) + 1),
                        generateLastName(r.nextInt(9) + 1),
                        generateTitle(),
                        generateMail(),
                        generateString(64),
                        generateString(64),
                        generateString(64),
                        generateRandomPhone(),
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        generateTextMaybeEmpty(0.6, 250),
                        r.nextInt(NUMBER_OF_ADDRESSES) + 1,
                        r.nextInt(NUMBER_OF_ZONES) + 1,
                        r.nextInt(NUMBER_OF_ADDRESSES) + 1,
                        r.nextInt(NUMBER_OF_LICENSES) + 1,
                        r.nextInt(NUMBER_OF_IDCARDS) + 1);
            }
            System.out.println("Users created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void genCars() {
        try {
            //GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("cars.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_CARS; i++) {
                twriter.printf("%s,%s,%d,%d,%d,%d,%s,%s,%s,%d,%s,%d,%d,%d,%d,%s,%s,%s,%s\n",
                        generateRandomName(r.nextInt(8) + 1),
                        generatePlate(),
                        r.nextInt(NUMBER_OF_ADDRESSES) + 1,
                        r.nextInt(NUMBER_OF_ZONES) + 1,
                        r.nextInt(NUMBER_OF_INSCRIPTIONS) + 1,
                        r.nextInt(NUMBER_OF_USERS) + 1,
                        generateRandomWord(),
                        generateRandomWord(),
                        generateRandomWord(),
                        r.nextInt(40) + 1974,
                        generateTextMaybeEmpty(0.5, 100),
                        r.nextInt(8) + 1,
                        r.nextInt(12) + 1,
                        r.nextInt(2),
                        r.nextInt(2),
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        GenerateBigDecimal().toPlainString().replaceAll(",", "."),
                        GenerateBigDecimal().toPlainString().replaceAll(",", ".")
                );
            }
            System.out.println("cars created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genReservations() {
        try {
            //GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("reservations.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_RESERVATIONS; i++) {
                Date start = new Date(generateDate());
                Date end = new Date(generateDate());
                boolean switched = end.before(start);
                twriter.printf("%d,%d,%s,%s,%s\n", r.nextInt(NUMBER_OF_USERS) + 1, r.nextInt(NUMBER_OF_CARS - 140) + 1,
                        switched ? end : start, switched ? start : end, r.nextInt(2));
            }
            System.out.println("reservations created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateRides() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("rides.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_RIDES; i++) {
                Date start = new Date(generateDate());
                Date end = new Date(generateDate());
                boolean switched = end.before(start);
                twriter.printf("%d,%d,%s,%s,%d,%s,%s\n", r.nextInt(NUMBER_OF_USERS), r.nextInt(NUMBER_OF_CARS),
                        switched ? end : start, switched ? start : end, r.nextInt(2),
                        GenerateBigDecimal().toPlainString(), GenerateBigDecimal().toPlainString());
            }
            System.out.println("rides created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genFiles() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("files.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_FILES; i++) {
                twriter.printf("%s\n", generateURL());
            }
            System.out.println("files created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genTemplates() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("templates.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_TEMPLATES; i++) {
                twriter.printf("%s,%s\n", generateURL(), GenerateMocks.generateTextMaybeEmpty(0.1, 500));
            }
            System.out.println("templates created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genAdresses() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter twriter = new PrintWriter("addresses.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_ADDRESSES; i++) {
                twriter.printf("%s,%s,%s,%s\n", r.nextInt(NUMBER_OF_PLACES), GenerateMocks.generateRandomWord() + "straat", r.nextInt(1000), GenerateMocks.generateBus());
            }
            System.out.println("addresses created, fetch!");
            twriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void genDamage() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter writer = new PrintWriter("damages.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_DAMAGES; i++) {
                writer.printf("%d,%d,%d,%s,%s\n", r.nextInt(NUMBER_OF_CARS - 140) + 1, r.nextInt(NUMBER_OF_USERS) + 1,
                        r.nextInt(NUMBER_OF_RIDES), r.nextBoolean() ? "Afgehandeld" : "In behandeling", generateTextMaybeEmpty(0.2, 100));
            }
            System.out.println("damages created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void genRides() {
        try {
            GenerateMocks GM = new GenerateMocks();
            PrintWriter writer = new PrintWriter("rides.csv", "UTF-8");
            for (int i = 0; i < NUMBER_OF_RIDES; i++) {
                Date start = new Date(generateDate());
                Date end = new Date(generateDate());
                boolean switched = end.before(start);
                double startkm = r.nextDouble();
                double stopkm = r.nextDouble();
                boolean switchkm = startkm < stopkm;
                writer.printf("%d,%d,%s,%s,%d,%s,%s\n", r.nextInt(NUMBER_OF_USERS) + 1, r.nextInt(NUMBER_OF_CARS - 140) + 1, switched ? end.toString() : start.toString(),
                        switched ? start.toString() : end.toString(),
                        r.nextInt(2), switchkm ? String.valueOf(stopkm).replaceAll(",", ".") : String.valueOf(startkm).replaceAll(",", "."),
                        switchkm ? String.valueOf(stopkm).replaceAll(",", ".") : String.valueOf(startkm).replaceAll(",", "."));
            }
            System.out.println("actions created, fetch!");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException | DataAccessException ex) {
            Logger.getLogger(GenerateMocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ---------------------------------------------------------------------------*/

}
