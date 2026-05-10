CREATE TABLE IF NOT EXISTS patient
(
    patient_id UUID PRIMARY KEY,
    first_name varchar(128) NOT NULL,
    last_name varchar(128) NOT NULL,
    gender varchar(16),
    weight double precision,
    height double precision ,
    email varchar(128) UNIQUE NOT NULL,
    phone_number varchar(128) UNIQUE NOT NULL ,
    date_of_birth DATE,
    address varchar(128),
    status varchar(32) NOT NULL,
    registered_date DATE NOT NULL
);

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address, status, registered_date)
SELECT 'f43d3b0c-fa24-43ba-be2f-543ef1036dbf',
       'Carl',
       'Johnson',
       'MALE',
       '102.0',
       '185.9',
       'cj@example.com',
       '+1 (213) 482-9174',
       '1966-08-11',
       'Grove Street 65',
       'ACTIVE',
       '2024-07-23'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = 'f43d3b0c-fa24-43ba-be2f-543ef1036dbf');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height,  email, phone_number, date_of_birth, address,status, registered_date)
SELECT '9c94c586-496b-4c2e-b3f5-b1db78d1c186',
       'Donald',
       'Trump',
       'MALE',
       '109.3',
       '189.3',
       'dtj@example.com',
       '+1 (312) 649-2850',
       '1946-06-14',
       'Washington DC',
       'ACTIVE',
       '2024-09-23'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '9c94c586-496b-4c2e-b3f5-b1db78d1c186');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height,  email, phone_number, date_of_birth, address,status, registered_date)
SELECT '7f89b453-ce96-42a3-ae64-d17d62b83b71',
       'Bruce',
       'Wayne',
       'MALE',
       '100.3',
       '188.2',
       'dark-knight@example.com',
       '+1 (415) 762-9381',
       '1960-03-19',
       'Gotham',
       'ACTIVE',
       '2009-11-23'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '7f89b453-ce96-42a3-ae64-d17d62b83b71');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '0de8b041-7834-4685-90c9-d1fa1221a232',
       'Walter',
       'White',
       'MALE',
       '75.3',
       '180.3',
       'i-am-the-danger@example.com',
       '+1 (646) 701-4386',
       '1958-09-11',
       'Albuquerque, 3828 Piermont Dr',
       'ACTIVE',
       '2010-03-29'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '0de8b041-7834-4685-90c9-d1fa1221a232');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '344d8ace-f1c0-43fc-9348-a3c72e3c1627',
       'Darren',
       'Watkins',
       'MALE',
       '65.3',
       '178.2',
       'ishowspeed@example.com',
       '+1 (202) 555-0199',
       '2005-01-21',
       'Montana, 3499 Labor Dr',
       'ACTIVE',
       '2019-04-30'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '344d8ace-f1c0-43fc-9348-a3c72e3c1627');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT 'cccb7d02-6c47-415d-aa79-57c9775b7abc',
       'Gabrielle',
       'Torres',
       'FEMALE',
       '65.8',
       '170.3',
       'torreslvbbc@example.com',
       '+1 (305) 823-1172',
       '1990-04-02',
       'Texas, 1903 Del-Toro Dr',
       'ACTIVE',
       '2019-05-03'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = 'cccb7d02-6c47-415d-aa79-57c9775b7abc');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT 'd4e4f2b7-7963-4ecf-8b50-9137b945bdaa',
       'Case',
       'Dylan',
       'MALE',
       '120.3',
       '188.4',
       'caseoh@example.com',
       '+1 (702) 916-4853',
       '1998-05-09',
       'Ostin, 1231 Baker Dr',
       'ACTIVE',
       '2023-04-27'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = 'd4e4f2b7-7963-4ecf-8b50-9137b945bdaa');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '44d11c3f-7486-44d1-8d1a-116f1b98ea01',
       'Linus',
       'Torvalds',
       'MALE',
       '80.3',
       '177.1',
       'linux4life@example.com',
       '+1 (617) 393-2078',
       '1969-12-28',
       'Oregon, 0223 Nimble Dr',
       'ACTIVE',
       '1993-02-23'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '44d11c3f-7486-44d1-8d1a-116f1b98ea01');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '9d8db8fd-1ccd-450f-ab1d-34c8e2178c5a',
       'Van',
       'DarkHolm',
       'MALE',
       '85.4',
       '180.8',
       'bondage@example.com',
       '+1 (469) 821-4506',
       '1972-10-24',
       'New Jersey, 1478 Pitch Dr',
       'ACTIVE',
       '2007-07-18'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '9d8db8fd-1ccd-450f-ab1d-34c8e2178c5a');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '2014b6c6-be98-4c64-b83d-4d4c8736b957',
       'Zack',
       'Rawr',
       'MALE',
       '67.0',
       '190.4',
       'asmongold@example.com',
       '+1 (818) 290-7734',
       '1990-04-20',
       'Texas, 983 Saint Gregory Dr',
       'ACTIVE',
       '2005-03-18'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '2014b6c6-be98-4c64-b83d-4d4c8736b957');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT 'bcfe825a-700b-488e-8955-50bd5887b9c3',
       'Nicholas',
       'Stewart',
       'MALE',
       '85.1',
       '175.9',
       'jynxzi@example.com',
       '+1 (503) 476-2319',
       '2001-09-26',
       'California, 92 Fobos Dr',
       'ACTIVE',
       '2024-10-12'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = 'bcfe825a-700b-488e-8955-50bd5887b9c3');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '084b400a-0832-4b7a-8016-bbbcc515baeb',
       'Gem',
       'Jewels',
       'FEMALE',
       '70.8',
       '174.2',
       'giyatt@example.com',
       '+1 (917) 650-1492',
       '1998-03-23',
       'California, 8843 El-Mios Dr',
       'ACTIVE',
       '2025-11-02'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '084b400a-0832-4b7a-8016-bbbcc515baeb');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '19307b9f-165c-4c6c-a983-e9834c08aacf',
       'Trevor',
       'Philips',
       'MALE',
       '77.1',
       '180.7',
       'lonelypsycho@example.com',
       '+1 (801) 449-9327',
       '1967-11-14',
       'North Dakota, 8384 Rubio Dr',
       'ACTIVE',
       '2012-06-04'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '19307b9f-165c-4c6c-a983-e9834c08aacf');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '78e03965-f588-4f83-977f-347a7c81fdfe',
       'Nolan',
       'Grayson',
       'MALE',
       '90.0',
       '190.9',
       'bigdaddy@example.com',
       '+1 (312) 585-4390',
       '1923-12-14',
       'Viltrum',
       'ACTIVE',
       '2022-07-25'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '78e03965-f588-4f83-977f-347a7c81fdfe');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '1a7430ef-4510-49fb-b1fb-d1d3d65b774b',
       'Jesse',
       'Pinkman',
       'MALE',
       '72.2',
       '175.5',
       'thedealer@example.com',
       '+1 (480) 725-3146',
       '1987-09-14',
       'Albuquerque, 322 16th St SW',
       'ACTIVE',
       '2008-02-13'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '1a7430ef-4510-49fb-b1fb-d1d3d65b774b');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '46286cae-717a-448b-a3f1-aa5ac2f29eba',
       'Emma',
       'Frost',
       'FEMALE',
       '65.3',
       '177.2',
       'whitequeen@example.com',
       '+1 (377) 794-3283',
       '1972-07-14',
       'New York, 24 90th St SW',
       'ACTIVE',
       '2003-02-26'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '46286cae-717a-448b-a3f1-aa5ac2f29eba');

INSERT INTO patient (patient_id, first_name, last_name, gender, weight, height, email, phone_number, date_of_birth, address,status, registered_date)
SELECT '09000e05-6a37-4b69-b516-381621575e4a',
       'Vitaly',
       'Tsal',
       'MALE',
       '78.2',
       '182.7',
       'velichaishy@example.com',
       '+1 (457) 709-3221',
       '1990-10-19',
       'Dubai, 321 74th St',
       'ACTIVE',
       '2008-08-30'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE patient_id = '09000e05-6a37-4b69-b516-381621575e4a');




