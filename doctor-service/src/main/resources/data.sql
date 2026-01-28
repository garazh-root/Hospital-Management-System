CREATE TABLE IF NOT EXISTS doctor
(
    doctor_id UUID PRIMARY KEY,
    first_name varchar(32) NOT NULL,
    last_name varchar(32) NOT NULL,
    gender varchar(16) NOT NULL,
    email varchar(128) UNIQUE NOT NULL,
    phone_number varchar(64) NOT NULL,
    specialization text NOT NULL,
    rating numeric(2,1) NOT NULL DEFAULT 0.0,
    doctor_status varchar(32) NOT NULL
);

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'a0288931-e496-4211-9f09-0b9962256579',
       'Will',
       'Otkins',
       'MALE',
       'otkindr@example.com',
       '+1 (442) 803-9122',
       'Cardiologist',
       0.0,
       'ACTIVE'

WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'a0288931-e496-4211-9f09-0b9962256579');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '56a3f48c-bca3-4b60-ac48-6db4ed9382ad',
       'Lesley',
       'Grey',
       'FEMALE',
       'thelesley@example.com',
       '+1 (040) 431-9920',
       'Dermatologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '56a3f48c-bca3-4b60-ac48-6db4ed9382ad');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '9fd8827b-7bd9-49a0-b58b-85fdb11548ff',
       'Debra',
       'Winslow',
       'FEMALE',
       'debrrradr@example.com',
       '+1 (391) 671-9332',
       'Ophthalmologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '9fd8827b-7bd9-49a0-b58b-85fdb11548ff');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'c088b336-b7a1-4aca-a6be-e7d48ab3109f',
       'Harvey',
       'Duma',
       'MALE',
       'harveyjob@example.com',
       '+1 (201) 920-4352',
       'Dentist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'c088b336-b7a1-4aca-a6be-e7d48ab3109f');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'bffe3ab9-973c-4d04-9363-5f754ad228d6',
       'Emma',
       'Montgomery',
       'FEMALE',
       'emmabaddie@example.com',
       '+1 (328) 030-1392',
       'Neurologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'bffe3ab9-973c-4d04-9363-5f754ad228d6');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'f29b9a6f-628b-41a7-8816-758a6a3e67a2',
       'Michael',
       'De Veil',
       'MALE',
       'jobmail3michael@example.com',
       '+1 (320) 934-1220',
       'Orthopedic',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'f29b9a6f-628b-41a7-8816-758a6a3e67a2');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '657cd99a-9657-4cf8-983a-5447334c4d92',
       'Becky',
       'Norse',
       'FEMALE',
       'mailforbecky3@example.com',
       '+1 (442) 945-0390',
       'Gastroenterologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '657cd99a-9657-4cf8-983a-5447334c4d92');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '329ddbb2-8b50-4ecb-8455-0aeaed91216c',
       'Mitch',
       'Wilnowski',
       'MALE',
       'mitchfor@example.com',
       '+1 (040) 117-7437',
       'Surgeon',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '329ddbb2-8b50-4ecb-8455-0aeaed91216c');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'bd3eb373-dd02-4a82-a892-8559cff3f2bf',
       'Sarah',
       'Tober',
       'FEMALE',
       'lilsh@example.com',
       '+1 (040) 267-4409',
       'Pediatrician',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'bd3eb373-dd02-4a82-a892-8559cff3f2bf');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '7c49389c-5694-4dea-8cc3-2f2e2db4e1b6',
       'Jamal',
       'Novis',
       'MALE',
       'hood4life@example.com',
       '+1 (201) 071-9213',
       'Nephrologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '7c49389c-5694-4dea-8cc3-2f2e2db4e1b6');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'a9450e5a-5a4e-4c50-9a11-823ba37f7c33',
       'Chun',
       'Ki',
       'FEMALE',
       'yellowinblood@example.com',
       '+1 (399) 129-2925',
       'Oncologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'a9450e5a-5a4e-4c50-9a11-823ba37f7c33');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'b88c1bc0-4b7b-4cf9-836b-e6c176f2f1a5',
       'Kyle',
       'Norrington',
       'MALE',
       'gigaman@example.com',
       '+1 (440) 870-3625',
       'Oncologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'b88c1bc0-4b7b-4cf9-836b-e6c176f2f1a5');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '792b60bc-f394-4eb5-ad44-90fc9e142ca8',
       'Nora',
       'De Santa',
       'FEMALE',
       'bestdr@example.com',
       '+1 (440) 029-8885',
       'Pulmonologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '792b60bc-f394-4eb5-ad44-90fc9e142ca8');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'd7975181-a93b-4a81-85fe-64067c44ff02',
       'Evelynn',
       'Anzer',
       'FEMALE',
       'deutcshstill@example.com',
       '+1 (201) 855-0945',
       'Gynaecologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'd7975181-a93b-4a81-85fe-64067c44ff02');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '25f4e613-af3b-41f4-8f99-f4b619b642d1',
       'Mark',
       'Hollow',
       'MALE',
       'mrkforjobonly@example.com',
       '+1 (440) 447-0349',
       'Hepatologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '25f4e613-af3b-41f4-8f99-f4b619b642d1');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '4809e1a7-7dec-43b0-b5f2-d2eb303455b8',
       'Derek',
       'Whales',
       'MALE',
       'mrkfjfcjobonly@example.com',
       '+1 (440) 410-0559',
       'Hepatologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '4809e1a7-7dec-43b0-b5f2-d2eb303455b8');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '9943502e-0c8d-48e7-a610-f0c990c02b4e',
       'Courtney',
       'Bright',
       'FEMALE',
       'asbrightassun@example.com',
       '+1 (201) 202-4399',
       'Psychiatrist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '9943502e-0c8d-48e7-a610-f0c990c02b4e');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'df5e1e5a-857e-4d7a-83ad-66f211af9a61',
       'Brian',
       'Locks',
       'MALE',
       'pulmforjob@example.com',
       '+1 (440) 293-4342',
       'Pulmonologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'df5e1e5a-857e-4d7a-83ad-66f211af9a61');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT 'f8428687-779d-432e-908b-8926a267081a',
       'Agnieszka',
       'Biawa',
       'FEMALE',
       'theagnthebest@example.com',
       '+1 (399) 443-7229',
       'Dermatologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = 'f8428687-779d-432e-908b-8926a267081a');

INSERT INTO doctor (doctor_id, first_name, last_name, gender, email, phone_number, specialization, rating, doctor_status)
SELECT '048a27f9-08e1-4032-aac8-bdbd85f051c8',
       'Claude',
       'Fort',
       'OTHER',
       'thespecialone@example.com',
       '+1 (201) 310-7170',
       'Cardiologist',
       0.0,
       'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE doctor_id = '048a27f9-08e1-4032-aac8-bdbd85f051c8');

CREATE TABLE IF NOT EXISTS schedule_template
(
    id UUID PRIMARY KEY DEFAULT pg_catalog.gen_random_uuid(),
    doctor_id UUID NOT NULL REFERENCES doctor(doctor_id) ON DELETE CASCADE,
    day_of_week VARCHAR(16) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_start_time TIME NOT NULL,
    break_end_time TIME NOT NULL,
    slot_duration_of_minutes INTEGER NOT NULL DEFAULT (30),
    effective_from DATE,
    effective_to DATE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_time CHECK ( end_time > start_time ),
    CONSTRAINT valid_break CHECK ( break_end_time > start_time )
);

CREATE TABLE IF NOT EXISTS schedule_override
(
    id UUID PRIMARY KEY DEFAULT pg_catalog.gen_random_uuid(),
    doctor_id UUID NOT NULL REFERENCES doctor(doctor_id) ON DELETE CASCADE,
    date DATE NOT NULL,
    override_type VARCHAR(16) NOT NULL,
    start_time TIME,
    end_time TIME,
    slot_duration_of_minutes INTEGER,
    reason VARCHAR(128),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (doctor_id, date),
    CONSTRAINT valid_override_type CHECK (override_type IN ('UNAVAILABLE','CUSTOM_HOURS' ,'EMERGENCY'))
);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
     ('a0288931-e496-4211-9f09-0b9962256579', 'MONDAY', '09:00:00', '17:00:00', '13:00:00', '14:00:00',30, true),
     ('a0288931-e496-4211-9f09-0b9962256579', 'WEDNESDAY', '10:00:00', '16:00:00', '12:00:00', '13:00:00',30, true),
     ('a0288931-e496-4211-9f09-0b9962256579', 'FRIDAY', '09:00:00', '17:00:00', '13:00:00', '14:00:00',30, true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('56a3f48c-bca3-4b60-ac48-6db4ed9382ad', 'MONDAY', '09:00:00', '17:00:00', '13:00:00', '14:00:00',30, true),
    ('56a3f48c-bca3-4b60-ac48-6db4ed9382ad', 'TUESDAY', '09:00:00', '16:00:00', '12:00:00', '13:00:00',30, true),
    ('56a3f48c-bca3-4b60-ac48-6db4ed9382ad', 'THURSDAY', '08:30:00', '15:30:00', '12:00:00', '13:00:00',30, true),
    ('56a3f48c-bca3-4b60-ac48-6db4ed9382ad', 'FRIDAY', '09:30:00', '17:30:00', '13:30:00', '14:30:00',30, true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('9fd8827b-7bd9-49a0-b58b-85fdb11548ff', 'MONDAY', '09:00:00', '17:00:00', '13:00:00', '14:00:00',30, true),
    ('9fd8827b-7bd9-49a0-b58b-85fdb11548ff', 'WEDNESDAY', '09:00:00', '16:00:00', '12:00:00', '13:00:00',30, true),
    ('9fd8827b-7bd9-49a0-b58b-85fdb11548ff', 'THURSDAY', '10:00:00', '17:00:00', '13:00:00', '14:00:00',30, true),
    ('9fd8827b-7bd9-49a0-b58b-85fdb11548ff', 'FRIDAY', '08:30:00', '16:30:00', '12:30:00', '13:30:00',30, true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('c088b336-b7a1-4aca-a6be-e7d48ab3109f', 'TUESDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('c088b336-b7a1-4aca-a6be-e7d48ab3109f', 'WEDNESDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('c088b336-b7a1-4aca-a6be-e7d48ab3109f', 'THURSDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('c088b336-b7a1-4aca-a6be-e7d48ab3109f', 'FRIDAY', '11:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('bffe3ab9-973c-4d04-9363-5f754ad228d6', 'MONDAY', '08:00:00', '15:30:00', 30, '12:00:00', '13:00:00', true),
    ('bffe3ab9-973c-4d04-9363-5f754ad228d6', 'WEDNESDAY', '08:00:00', '15:30:00', 30, '12:00:00', '13:00:00', true),
    ('bffe3ab9-973c-4d04-9363-5f754ad228d6', 'FRIDAY', '08:00:00', '16:30:00', 30, '12:00:00', '13:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('f29b9a6f-628b-41a7-8816-758a6a3e67a2', 'MONDAY', '09:00:00', '16:00:00', 30, '12:00:00', '13:00:00', true),
    ('f29b9a6f-628b-41a7-8816-758a6a3e67a2', 'TUESDAY', '10:00:00', '17:00:00', 30, '14:00:00', '15:00:00', true),
    ('f29b9a6f-628b-41a7-8816-758a6a3e67a2', 'THURSDAY', '09:00:00', '16:00:00', 30, '13:00:00', '14:00:00', true),
    ('f29b9a6f-628b-41a7-8816-758a6a3e67a2', 'FRIDAY', '11:00:00', '16:00:00', 30, '14:00:00', '15:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('657cd99a-9657-4cf8-983a-5447334c4d92', 'TUESDAY', '09:20:00', '15:30:00', 30, '13:00:00', '14:00:00', true),
    ('657cd99a-9657-4cf8-983a-5447334c4d92', 'WEDNESDAY', '09:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('657cd99a-9657-4cf8-983a-5447334c4d92', 'THURSDAY', '11:00:00', '16:30:00', 30, '14:00:00', '15:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('329ddbb2-8b50-4ecb-8455-0aeaed91216c', 'MONDAY', '10:00:00', '16:30:00', 30, '14:00:00', '15:00:00', true),
    ('329ddbb2-8b50-4ecb-8455-0aeaed91216c', 'TUESDAY', '10:00:00', '16:30:00', 30, '14:00:00', '15:00:00', true),
    ('329ddbb2-8b50-4ecb-8455-0aeaed91216c', 'WEDNESDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('329ddbb2-8b50-4ecb-8455-0aeaed91216c', 'THURSDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('bd3eb373-dd02-4a82-a892-8559cff3f2bf', 'MONDAY', '09:00:00', '16:00:00', 30, '13:00:00', '14:00:00', true),
    ('bd3eb373-dd02-4a82-a892-8559cff3f2bf', 'TUESDAY', '09:00:00', '16:00:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('7c49389c-5694-4dea-8cc3-2f2e2db4e1b6', 'MONDAY', '09:00:00', '16:00:00', 30, '13:00:00', '14:00:00', true),
    ('7c49389c-5694-4dea-8cc3-2f2e2db4e1b6', 'TUESDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('7c49389c-5694-4dea-8cc3-2f2e2db4e1b6', 'WEDNESDAY', '09:00:00', '16:00:00', 30, '13:00:00', '14:00:00', true),
    ('7c49389c-5694-4dea-8cc3-2f2e2db4e1b6', 'FRIDAY', '11:00:00', '15:30:00', 30, '13:30:00', '14:30:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('a9450e5a-5a4e-4c50-9a11-823ba37f7c33', 'TUESDAY', '09:00:00', '15:30:00', 30, '13:00:00', '14:00:00', true),
    ('a9450e5a-5a4e-4c50-9a11-823ba37f7c33', 'FRIDAY', '11:00:00', '15:30:00', 30, '13:30:00', '14:30:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('b88c1bc0-4b7b-4cf9-836b-e6c176f2f1a5', 'MONDAY', '09:00:00', '15:30:00', 30, '13:00:00', '14:00:00', true),
    ('b88c1bc0-4b7b-4cf9-836b-e6c176f2f1a5', 'WEDNESDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('792b60bc-f394-4eb5-ad44-90fc9e142ca8', 'MONDAY', '10:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('792b60bc-f394-4eb5-ad44-90fc9e142ca8', 'WEDNESDAY', '10:00:00', '16:45:00', 30, '13:30:00', '14:30:00', true),
    ('792b60bc-f394-4eb5-ad44-90fc9e142ca8', 'FRIDAY', '12:00:00', '17:00:00', 30, '14:30:00', '15:30:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('d7975181-a93b-4a81-85fe-64067c44ff02', 'TUESDAY', '09:00:00', '16:00:00', 30, '13:30:00', '14:30:00', true),
    ('d7975181-a93b-4a81-85fe-64067c44ff02', 'WEDNESDAY', '12:00:00', '17:00:00', 30, '14:30:00', '15:30:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('25f4e613-af3b-41f4-8f99-f4b619b642d1', 'MONDAY', '09:45:00', '16:00:00', 30, '13:30:00', '14:30:00', true),
    ('25f4e613-af3b-41f4-8f99-f4b619b642d1', 'WEDNESDAY', '12:00:00', '16:20:00', 30, '13:00:00', '14:00:00', true),
    ('25f4e613-af3b-41f4-8f99-f4b619b642d1', 'FRIDAY', '09:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('4809e1a7-7dec-43b0-b5f2-d2eb303455b8', 'TUESDAY', '09:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true),
    ('4809e1a7-7dec-43b0-b5f2-d2eb303455b8', 'THURSDAY', '09:00:00', '16:30:00', 30, '13:00:00', '14:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('9943502e-0c8d-48e7-a610-f0c990c02b4e', 'MONDAY', '09:00:00', '17:00:00', 30, '13:00:00', '14:00:00', true),
    ('9943502e-0c8d-48e7-a610-f0c990c02b4e', 'WEDNESDAY', '09:00:00', '17:00:00', 30, '13:00:00', '14:00:00', true),
    ('9943502e-0c8d-48e7-a610-f0c990c02b4e', 'FRIDAY', '10:00:00', '14:00:00', 30, '12:00:00', '13:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('df5e1e5a-857e-4d7a-83ad-66f211af9a61', 'TUESDAY', '08:00:00', '16:00:00', 30, '12:00:00', '13:00:00', true),
    ('df5e1e5a-857e-4d7a-83ad-66f211af9a61', 'THURSDAY', '08:00:00', '14:00:00', 30, '12:00:00', '13:00:00', true),
    ('df5e1e5a-857e-4d7a-83ad-66f211af9a61', 'SATURDAY', '09:00:00', '13:00:00', 30, '12:00:00', '13:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('f8428687-779d-432e-908b-8926a267081a', 'WEDNESDAY', '09:00:00', '16:00:00', 30, '12:00:00', '13:00:00', true),
    ('f8428687-779d-432e-908b-8926a267081a', 'FRIDAY', '10:00:00', '15:00:00', 30, '12:00:00', '13:00:00', true);

INSERT INTO schedule_template (doctor_id, day_of_week, start_time, end_time, break_start_time, break_end_time, slot_duration_of_minutes, active)
VALUES
    ('048a27f9-08e1-4032-aac8-bdbd85f051c8', 'TUESDAY', '08:25:00', '16:00:00', 30, '12:00:00', '13:30:00', true),
    ('048a27f9-08e1-4032-aac8-bdbd85f051c8', 'WEDNESDAY', '12:00:00', '17:00:00', 30, '14:00:00', '15:00:00', true);
