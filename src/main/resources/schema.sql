
CREATE TABLE time_slots
(
    time_slot_id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    time_slot_date       DATE                   NOT NULL,
    start_time           TIME  					NOT NULL,
    end_time             TIME  					NOT NULL,
    is_keynote_time_slot BOOLEAN default false  NOT NULL
);

CREATE TABLE sessions
(
    session_id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_name        VARCHAR(80)   NOT NULL,
    session_description VARCHAR(1024) NOT NULL,
    session_length      INTEGER       NOT NULL
);

CREATE TABLE session_schedule
(
    schedule_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    time_slot_id BIGINT     NOT NULL REFERENCES time_slots (time_slot_id),
    session_id   BIGINT     NOT NULL REFERENCES sessions (session_id),
    room         VARCHAR(30) NOT NULL,
    FOREIGN KEY (time_slot_id) REFERENCES time_slots(time_slot_id),
    FOREIGN KEY (session_id) REFERENCES sessions(session_id)
);



CREATE TABLE speakers
(
    speaker_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name    VARCHAR(30)   NOT NULL,
    last_name     VARCHAR(30)   NOT NULL,
    title         VARCHAR(40)   NOT NULL,
    company       VARCHAR(50)   NOT NULL,
    speaker_bio   VARCHAR(2000) NOT NULL,
    speaker_photo BLOB   		NULL
);

CREATE TABLE session_speakers
(
    session_id BIGINT NOT NULL REFERENCES sessions (session_id),
    speaker_id BIGINT NOT NULL REFERENCES speakers (speaker_id),
    PRIMARY KEY (session_id, speaker_id),
    FOREIGN KEY (session_id) REFERENCES sessions(session_id),
    FOREIGN KEY (speaker_id) REFERENCES speakers(speaker_id)
);

CREATE INDEX idx_time_slot_id ON session_schedule(time_slot_id);
CREATE INDEX idx_session_id ON session_schedule(session_id);

