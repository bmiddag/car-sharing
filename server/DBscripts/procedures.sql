
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