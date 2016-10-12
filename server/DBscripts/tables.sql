DROP TABLE IF EXISTS Token;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Notification;
DROP TABLE IF EXISTS Inscription;
DROP TABLE IF EXISTS InfoSession;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS DamageDoc;
DROP TABLE IF EXISTS Damage;
DROP TABLE IF EXISTS Refueling;
DROP TABLE IF EXISTS Ride;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Cost;
DROP TABLE IF EXISTS Paper;
DROP TABLE IF EXISTS Insurance;
DROP TABLE IF EXISTS Privilege;
DROP TABLE IF EXISTS ReservationRange;
DROP TABLE IF EXISTS Car;
DROP TABLE IF EXISTS CarPhoto;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS IdCard;
DROP TABLE IF EXISTS License;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS Place;
DROP TABLE IF EXISTS Country;
DROP TABLE IF EXISTS UserPermission;
DROP TABLE IF EXISTS Permission;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Zone;
DROP TABLE IF EXISTS Template;
DROP TABLE IF EXISTS Action;
DROP TABLE IF EXISTS PriceRange;
DROP TABLE IF EXISTS File;

CREATE TABLE File (
 id     INT UNSIGNED AUTO_INCREMENT,
 data    MEDIUMBLOB NOT NULL,
 filename VARCHAR(127),
 time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE PriceRange (
 id    INT UNSIGNED AUTO_INCREMENT,
 min    INT UNSIGNED NOT NULL,
 max    INT UNSIGNED NOT NULL,
 price   INT UNSIGNED NOT NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Action (
 id    INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(31) NOT NULL UNIQUE,
 start   BIGINT,
 period   BIGINT,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Template (
 id     INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(31) NOT NULL UNIQUE,
 removable BIT(1) DEFAULT 1,
 content    TEXT,
 edit TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Zone (
 id    INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(31) NOT NULL UNIQUE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Role (
 id    INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(31) NOT NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Permission (
 id    INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(31) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE UserPermission (
 id    INT UNSIGNED AUTO_INCREMENT,
 user   INT UNSIGNED NOT NULL,
permission INT UNSIGNED NOT NULL,
PRIMARY KEY (id));

CREATE TABLE Country (
 code VARCHAR(3) NOT NULL UNIQUE,
 name VARCHAR(63) NOT NULL UNIQUE,
PRIMARY KEY (code)) ENGINE = INNODB;

CREATE TABLE Place (
 id INT UNSIGNED AUTO_INCREMENT,
 name   VARCHAR(63) NOT NULL,
 zip  VARCHAR(9) NOT NULL,
 country VARCHAR(3) NOT NULL,
 FOREIGN KEY (country) REFERENCES Country(code) ON DELETE RESTRICT,
 PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Address (
 id    INT UNSIGNED AUTO_INCREMENT,
 place   INT UNSIGNED NOT NULL,
 street   VARCHAR(63) NOT NULL,
 number   INT UNSIGNED NOT NULL,
 bus    VARCHAR(3),
FOREIGN KEY (place) REFERENCES Place(id) ON DELETE RESTRICT,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE License (
 id INT UNSIGNED AUTO_INCREMENT,
 file   INT UNSIGNED NOT NULL,
 number   VARCHAR(15) NOT NULL UNIQUE,
FOREIGN KEY (file) REFERENCES File(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE IdCard (
 id INT UNSIGNED AUTO_INCREMENT,
 file   INT UNSIGNED NOT NULL,
 number   VARCHAR(15) NOT NULL UNIQUE,
 register  VARCHAR(15) NOT NULL UNIQUE,
FOREIGN KEY (file) REFERENCES File(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE User (
 id    INT UNSIGNED AUTO_INCREMENT,
 photo INT UNSIGNED,
 role    INT UNSIGNED NOT NULL,
 name   VARCHAR(64) NOT NULL,
 surname  VARCHAR(64) NOT NULL,
 title  VARCHAR(16),
 mail    VARCHAR(128) NOT NULL UNIQUE,
 pass   CHAR(198) NOT NULL,
 phone  VARCHAR(31),
 guarantee  INT UNSIGNED,
 past  TEXT,
 address   INT UNSIGNED,
 zone   INT UNSIGNED,
 domicile  INT UNSIGNED,
 license   INT UNSIGNED,
 idcard   INT UNSIGNED,
 mailverified BIT(1) DEFAULT 0,
 approved BIT(1) DEFAULT NULL,
 time   TIMESTAMP NOT NULL DEFAULT 0,
 edit  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (photo) REFERENCES File(id) ON DELETE SET NULL,
FOREIGN KEY (address) REFERENCES Address(id) ON DELETE SET NULL,
FOREIGN KEY (domicile) REFERENCES Address(id) ON DELETE SET NULL,
FOREIGN KEY (zone) REFERENCES Zone(id) ON DELETE SET NULL,
FOREIGN KEY (license) REFERENCES License(id) ON DELETE SET NULL,
FOREIGN KEY (idcard) REFERENCES IdCard(id) ON DELETE SET NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE CarPhoto (
 file   INT UNSIGNED NOT NULL,
 car   INT UNSIGNED NOT NULL,
FOREIGN KEY (file) REFERENCES File(id) ON DELETE CASCADE,
PRIMARY KEY (file)) ENGINE = INNODB;

CREATE TABLE Car (
 id      INT UNSIGNED AUTO_INCREMENT,
 name    VARCHAR(31) UNIQUE,
 plate   VARCHAR(15) UNIQUE,
 approved  BIT(1) DEFAULT NULL,
 active  BIT(1) DEFAULT 0,
 address     INT UNSIGNED,
 zone    INT UNSIGNED,
 inscription   INT UNSIGNED,
 owner   INT UNSIGNED NOT NULL,
 make    VARCHAR(31),
 type    VARCHAR(31),
 model   VARCHAR(31),
 year    INT,
 fuel    VARCHAR(31),
 doors    INT,
 capacity   INT,
 tow    BIT(1) DEFAULT 0,
 gps     BIT(1) DEFAULT 0, 
 airco   BIT(1) DEFAULT 0,
 kiddyseats     BIT(1) DEFAULT 0,
 automatic      BIT(1) DEFAULT 0, 
 large   BIT(1) DEFAULT 0,
 wastedisposal  BIT(1) DEFAULT 1,
 pets   BIT(1) DEFAULT 1, 
 smokefree     BIT(1) DEFAULT 0,
 cdplayer     BIT(1) DEFAULT 0,
 radio     BIT(1) DEFAULT 0,
 mp3     BIT(1) DEFAULT 0,
 consumption   DOUBLE,
 chassis    VARCHAR(31),
 value    INT UNSIGNED,
 mileage     DOUBLE,
 kmpy    DOUBLE, 
 reachability  TEXT,
 description  TEXT,
 time    TIMESTAMP NOT NULL DEFAULT 0,
 edit   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (address) REFERENCES Address(id) ON DELETE SET NULL,
FOREIGN KEY (owner) REFERENCES User(id) ON DELETE CASCADE,
FOREIGN KEY (zone) REFERENCES Zone(id) ON DELETE SET NULL,
FOREIGN KEY (inscription) REFERENCES File(id) ON DELETE SET NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE ReservationRange (
 id INT UNSIGNED AUTO_INCREMENT,
 car INT UNSIGNED NOT NULL,
 day BIT(3) NOT NULL,
 begin BIGINT NOT NULL,
 end BIGINT NOT NULL,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Privilege (
 id    INT UNSIGNED AUTO_INCREMENT,
 user   INT UNSIGNED NOT NULL,
 car    INT UNSIGNED NOT NULL,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE CASCADE,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Insurance (
 id    INT UNSIGNED AUTO_INCREMENT,
 car INT UNSIGNED NOT NULL,
 number   VARCHAR(31),
 company  VARCHAR(127),
 bonusmalus  INT,
 omnium   BIT(1),
 expiration  DATE,
 time   TIMESTAMP NOT NULL DEFAULT 0,
 edit TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Paper (
 file   INT UNSIGNED,
 insurance  INT UNSIGNED NOT NULL,
FOREIGN KEY (insurance) REFERENCES Insurance(id) ON DELETE CASCADE,
PRIMARY KEY (file)) ENGINE = INNODB;

CREATE TABLE Cost (
 id    INT UNSIGNED AUTO_INCREMENT,
 car    INT UNSIGNED NOT NULL,
 price    INT UNSIGNED NOT NULL,
 type   VARCHAR(63),
 proof    INT UNSIGNED UNIQUE,
 description TEXT,
 approved  BIT(1) DEFAULT 0,
 moment BIGINT NOT NULL,
 time   TIMESTAMP NOT NULL DEFAULT 0,
 edit TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
FOREIGN KEY (proof) REFERENCES File(id) ON DELETE SET NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Reservation (
 id INT UNSIGNED AUTO_INCREMENT,
 user   INT UNSIGNED,
 car    INT UNSIGNED,
 begin   BIGINT NOT NULL,
 end   BIGINT NOT NULL,
 approved  BIT(1),
 pending BIT(1) NOT NULL DEFAULT 1,
 time   TIMESTAMP NOT NULL DEFAULT 0,
 edit TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE CASCADE,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Ride (
 id   INT UNSIGNED AUTO_INCREMENT,
 user   INT UNSIGNED,
 car    INT UNSIGNED,
 begin   BIGINT NULL,
 end   BIGINT NULL,
 approved  BIT(1),
 pending   BIT(1) NOT NULL DEFAULT 1,
 startkm   DOUBLE,
 stopkm   DOUBLE,
 price INT,
 time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE CASCADE,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Refueling (
 id   INT UNSIGNED AUTO_INCREMENT,
 litre  DOUBLE,
 price   INT UNSIGNED NOT NULL,
 ride INT UNSIGNED,
 proof  INT UNSIGNED UNIQUE,
 type  VARCHAR(31),
 approved BIT(1),
 mileage DOUBLE,
 time   TIMESTAMP NOT NULL DEFAULT 0,
 edit  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (ride) REFERENCES Ride(id) ON DELETE CASCADE,
FOREIGN KEY (proof) REFERENCES File(id) ON DELETE SET NULL,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Damage (
 id    INT UNSIGNED AUTO_INCREMENT,
 car    INT UNSIGNED NOT NULL,
 user   INT UNSIGNED,
 status   VARCHAR(31) DEFAULT 'Onverwerkt',
 description TEXT,
 occurred BIGINT,
 time    TIMESTAMP NOT NULL DEFAULT 0,
 edit  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE SET NULL,
FOREIGN KEY (car) REFERENCES Car(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE DamageDoc (
 file   INT UNSIGNED,
 damage  INT UNSIGNED NOT NULL,
FOREIGN KEY (damage) REFERENCES Damage(id) ON DELETE CASCADE,
PRIMARY KEY (file)) ENGINE = INNODB;

CREATE TABLE Comment (
 id   INT UNSIGNED AUTO_INCREMENT,
 user  INT UNSIGNED,
 damage INT UNSIGNED,
 content  TEXT NOT NULL,
 time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE SET NULL,
FOREIGN KEY (damage) REFERENCES Damage(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE InfoSession (
 id   INT UNSIGNED AUTO_INCREMENT,
 address  INT UNSIGNED NOT NULL,
 date  BIGINT NOT NULL,
 owners BIT(1) NOT NULL DEFAULT 0,
 places  INT,
FOREIGN KEY (address) REFERENCES Address(id) ON DELETE RESTRICT,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Inscription (
 id    INT UNSIGNED AUTO_INCREMENT,
 session INT UNSIGNED NOT NULL,
 user  INT UNSIGNED NOT NULL,
 present BIT(1) DEFAULT 0,
 time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE CASCADE,
FOREIGN KEY (session) REFERENCES InfoSession(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Notification (
 id   INT UNSIGNED AUTO_INCREMENT,
 user  INT UNSIGNED,
 subject TINYTEXT,
 content  TEXT,
 seen BIT(1) NOT NULL DEFAULT 0,
 time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id) ON DELETE CASCADE,
PRIMARY KEY (id, user)) ENGINE = INNODB;

CREATE TABLE Invoice (
 id INT UNSIGNED AUTO_INCREMENT,
 user INT UNSIGNED,
 pdf INT UNSIGNED,
 excel INT UNSIGNED,
 totalCost INT UNSIGNED,
 till BIGINT,
 time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (pdf) REFERENCES File(id) ON DELETE CASCADE,
FOREIGN KEY (excel) REFERENCES File(id) ON DELETE CASCADE,
PRIMARY KEY (id)) ENGINE = INNODB;

CREATE TABLE Token (
  token BIGINT,
  user INT UNSIGNED, 
  time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user) REFERENCES User(id),
PRIMARY KEY (token)) ENGINE = INNODB;

CREATE TRIGGER user_time before insert ON User FOR EACH ROW SET new.time = now();
CREATE TRIGGER car_time before insert ON Car FOR EACH ROW SET new.time = now();
CREATE TRIGGER damage_time before insert ON Damage FOR EACH ROW SET new.time = now();
CREATE TRIGGER reservation_time before insert ON Reservation FOR EACH ROW SET new.time = now();
CREATE TRIGGER refueling_time before insert ON Refueling FOR EACH ROW SET new.time = now();
CREATE TRIGGER cost_time before insert ON Cost FOR EACH ROW SET new.time = now();
CREATE TRIGGER insurance_time before insert ON Insurance FOR EACH ROW SET new.time = now();

DROP EVENT IF EXISTS delete_stale_tokens;
CREATE EVENT delete_stale_tokens ON SCHEDULE EVERY 1 WEEK ON COMPLETION PRESERVE DO 
  DELETE FROM Token WHERE time < (NOW() - INTERVAL 7 DAY);

DROP EVENT IF EXISTS delete_disapproved_users;
CREATE EVENT delete_disapproved_users ON SCHEDULE EVERY 1 WEEK ON COMPLETION PRESERVE DO 
  DELETE FROM User WHERE approved = false AND edit < (NOW() - INTERVAL 1 MONTH);

DROP EVENT IF EXISTS delete_disapproved_cars;
CREATE EVENT delete_disapproved_cars ON SCHEDULE EVERY 1 WEEK ON COMPLETION PRESERVE DO 
  DELETE FROM Car WHERE approved = false AND edit < (NOW() - INTERVAL 1 MONTH);

DELIMITER //

#
# Procedure for searching if a car is available
#

DROP PROCEDURE IF EXISTS isAvailable //
CREATE PROCEDURE isAvailable ( in_min BIGINT, in_max BIGINT, in_duration BIGINT, in_car INT UNSIGNED, IN aliases TEXT, IN joins TEXT )
BEGIN
 
  DECLARE try_begin BIGINT DEFAULT in_min;
  DECLARE try_end BIGINT DEFAULT in_min + in_duration;
  DECLARE result BIGINT DEFAULT 0;
  DECLARE begin_time DATETIME;
  DECLARE end_time DATETIME;
  DECLARE later DATETIME;
  DECLARE time DATETIME;
  
  validate: LOOP

    IF try_end > in_max THEN LEAVE validate; END IF;

    IF in_duration < 12*60*60*1000 AND (begin_time < addtime(date(begin_time),'08:00:00') OR end_time > addtime(date(end_time),'20:00:00')) THEN ITERATE validate; END IF;

    # Check reservations

    SELECT max(end) INTO result FROM Reservation WHERE car = in_car AND end > try_begin AND begin < try_end;
    IF (result IS NOT NULL) THEN
      SET try_begin = result;
      SET try_end = try_begin + in_duration;
      ITERATE validate;
    END IF;

    # Check begin and end day

    SET begin_time = from_unixtime(try_begin/1000);
    SET end_time = from_unixtime(try_end/1000);

    IF date(begin_time) = date(end_time) THEN
      SELECT max(end) INTO result FROM ReservationRange WHERE car = in_car AND day = dayofweek(begin_time) AND end/1000 > time(begin_time) AND begin/1000 < time(end_time);
    ELSE
      SELECT max(end) INTO result FROM ((SELECT end FROM ReservationRange WHERE car = in_car AND day = dayofweek(begin_time) AND end/1000 > time(begin_time))
        UNION (SELECT end FROM ReservationRange WHERE car = in_car AND day = dayofweek(end_time) AND begin/1000 < time(end_time))) AS ends;
    END IF;

    IF (result IS NOT NULL) THEN
      SET try_begin = result;
      SET try_end = try_begin + in_duration;
      ITERATE validate;
    END IF;

    # Iterate over other days

    SET later = begin_time;

    days: LOOP

      SET later = adddate(later,1);
      IF date(later) >= date(end_time) THEN LEAVE days; END IF;

      SELECT max(end) INTO result FROM ReservationRange WHERE car = in_car AND day = dayofweek(later);
      IF (result IS NOT NULL) THEN
        SET try_begin = result;
        SET try_end = try_begin + in_duration;
        ITERATE validate;
      END IF;

    END LOOP days;

    # Return result

    SET @prepared = concat("SELECT ", try_begin, " AS try_begin, ", try_end, " AS try_end, ", aliases, " FROM ", joins, " WHERE Car_table.ID = ", in_car, ";");
    PREPARE stmt FROM @prepared;
    EXECUTE stmt;

    LEAVE validate;

    SET try_begin = try_begin + 1000*60*60;
    SET try_end = try_begin + in_duration;

  END LOOP validate;

END //

#
# Procedure for searching available cars
#

DROP PROCEDURE IF EXISTS getAvailableCars //
CREATE PROCEDURE getAvailableCars ( IN in_min BIGINT, IN in_max BIGINT, IN in_duration BIGINT, IN aliases TEXT, IN joins TEXT, IN filter TEXT )
BEGIN
 
  DECLARE car INT UNSIGNED;
  DECLARE finished INT DEFAULT 0;
  DECLARE result BIGINT DEFAULT 0;
  DECLARE car_cursor CURSOR FOR SELECT id FROM Car;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;

  SET @prepared = concat("SELECT group_concat(id) INTO @filtered FROM Car ", filter, ";");
  PREPARE stmt FROM @prepared;
  EXECUTE stmt; 

  OPEN car_cursor;
  search_cars: LOOP

    FETCH car_cursor INTO car;
    IF finished = 1 THEN LEAVE search_cars; END IF;

    SET result = find_in_set(car, @filtered);
    IF result = 0 OR result IS NULL THEN ITERATE search_cars; END IF;
    CALL isAvailable(in_min, in_max, in_duration, car, aliases, joins);

  END LOOP search_cars;
  CLOSE car_cursor;

END //

DELIMITER ;
