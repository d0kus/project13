CREATE TABLE portals (
                         id INT PRIMARY KEY,
                         portal_name TEXT NOT NULL,
                         url TEXT NOT NULL,
                         users_active INT NOT NULL,
                         working BOOLEAN NOT NULL
);

CREATE TABLE joblistings (
                             id INT PRIMARY KEY,
                             portal_id INT NOT NULL REFERENCES portals(id),
                             job_title TEXT NOT NULL,
                             company TEXT NOT NULL,
                             sphere TEXT NOT NULL,
                             active BOOLEAN NOT NULL
);