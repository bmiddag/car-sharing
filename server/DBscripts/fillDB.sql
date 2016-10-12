Insert INTO Country (code, name) VALUES ( "BE", "België");
Insert INTO Country (code, name) VALUES ( "NL", "Nederland");
Insert INTO Country (code, name) VALUES ( "LU", "Luxemburg");
Insert INTO Country (code, name) VALUES ( "DE", "Duitsland");
Insert INTO Country (code, name) VALUES ( "FR", "Frankrijk");

LOAD DATA LOCAL INFILE './files.csv' INTO TABLE File
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(data) ;

LOAD DATA LOCAL INFILE './priceranges.csv' INTO TABLE PriceRange
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(min,max,price) ;

LOAD DATA LOCAL INFILE './actions.csv' INTO TABLE Action
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(start, period,name) ;

LOAD DATA LOCAL INFILE './templates.csv' INTO TABLE Template
FIELDS TERMINATED BY ';\n'
LINES TERMINATED BY '\n$$\n'
(name,content) ;

LOAD DATA LOCAL INFILE './zones.csv' INTO TABLE Zone
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(name) ;

LOAD DATA LOCAL INFILE './roles.csv' INTO TABLE Role
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(name) ;

LOAD DATA LOCAL INFILE './places.csv' INTO TABLE Place
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
(zip,name,country) ;

LOAD DATA LOCAL INFILE './addresses.csv' INTO TABLE Address
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
(place,street,number,bus) ;

LOAD DATA LOCAL INFILE './idcards.csv' INTO TABLE IdCard
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(file, number,register)  ;

LOAD DATA LOCAL INFILE './licenses.csv' INTO TABLE License
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(file, number) ;

LOAD DATA LOCAL INFILE './users.csv' INTO TABLE User
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
 (role, name, surname, title, mail, pass ,
  phone, guarantee, past  , address , domicile, mailverified,approved  );

LOAD DATA LOCAL INFILE './cars.csv' INTO TABLE Car
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
 ( plate, owner, name, address, chassis, make,  type,
 model, year, fuel, capacity, doors, airco, cdplayer, radio, mp3,
 smokefree, tow, large, pets, wastedisposal, automatic, kiddyseats, gps,
 reachability, description, approved, active);

#LOAD DATA LOCAL INFILE './cars.bak' INTO TABLE Car
#FIELDS TERMINATED BY ','
#LINES TERMINATED BY '\n'
# ( name, plate,  address,  zone,  inscription,  owner,  make,  type,
# model, year, fuel, description, doors, capacity, tow, gps, consumption,
# chassis, value, mileage, kmpy);

LOAD DATA LOCAL INFILE './infosessions.csv' INTO TABLE InfoSession
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(address,date, owners, places);

#LOAD DATA LOCAL INFILE './reservations.csv' INTO TABLE Reservation
#FIELDS TERMINATED BY ','
#LINES TERMINATED BY '\n'
#(user,car,begin,end,approved);

#LOAD DATA LOCAL INFILE './rides.csv' INTO TABLE Ride
#FIELDS TERMINATED BY ','
#LINES TERMINATED BY '\n'
#(user,car,begin,end,approved,startkm,stopkm);

LOAD DATA LOCAL INFILE './notifications.csv' INTO TABLE Notification
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(user,subject,content);

#LOAD DATA LOCAL INFILE './refuelings.csv' INTO TABLE Refueling
#FIELDS TERMINATED BY ','
#LINES TERMINATED BY '\n'
#(ride,litre,price,proof,type,approved,mileage);

LOAD DATA LOCAL INFILE './damages.csv' INTO TABLE Damage
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
(car,user,status,description);

LOAD DATA LOCAL INFILE './insurances.csv' INTO TABLE Insurance
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(car,number,company,bonusmalus,omnium,expiration);

LOAD DATA LOCAL INFILE './permissions.csv' INTO TABLE Permission
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(name);

LOAD DATA LOCAL INFILE './userpermissions.csv' INTO TABLE UserPermission
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(user,permission);

LOAD DATA LOCAL INFILE './reservationranges.csv' INTO TABLE ReservationRange
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
(car,day,begin,end);
