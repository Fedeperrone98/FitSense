DROP DATABASE IF EXISTS iot_fitsense_db;
CREATE DATABASE IF NOT EXISTS iot_fitsense_db;

USE iot_fitsense_db;

CREATE TABLE IF NOT EXISTS configuration (
    area_id int(11) NOT NULL,
    node_id int(11) NOT NULL,
    address varchar(100) NOT NULL,
    PRIMARY KEY (area_id, node_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS area (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    name_area varchar(100) NOT NULL,
    max_presence int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO area (id, name_area, max_presence) VALUES
(1, "weight room", 40),
(2, "functional room", 30),
(3, "swimming pool", 20);


CREATE TABLE IF NOT EXISTS measurement_temperature (
    area_id int(11) NOT NULL,
    node_id int(11) NOT NULL,
    m_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    m_value int(11) NOT NULL,
    PRIMARY KEY(area_id, node_id, m_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS measurement_humidity (
    area_id int(11) NOT NULL,
    node_id int(11) NOT NULL,
    m_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    m_value int(11) NOT NULL,
    PRIMARY KEY(area_id, node_id, m_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS measurement_presence (
    area_id int(11) NOT NULL,
    node_id int(11) NOT NULL,
    m_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    m_value int(11) NOT NULL,
    PRIMARY KEY(area_id, node_id, m_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
