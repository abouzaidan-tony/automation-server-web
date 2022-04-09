
CREATE TABLE account (
    id integer NOT NULL primary key AUTOINCREMENT,
    nickname varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    password varchar(100) NOT NULL,
    token varchar(15) NOT NULL,
    otp varchar(5) DEFAULT NULL,
    q1 int(11) NOT NULL,
    q2 int(11) NOT NULL,
    ans1 varchar(100) NOT NULL,
    ans2 varchar(100) NOT NULL
);

CREATE TABLE dev_account (
  id integer NOT NULL primary key AUTOINCREMENT,
  email varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  unity_invoice varchar(100) DEFAULT NULL,
  otp varchar(5) DEFAULT NULL,
  verified tinyint(1) NOT NULL,
  q1 int(11) NOT NULL,
  q2 int(11) NOT NULL,
  ans1 varchar(100) DEFAULT NULL,
  ans2 varchar(100) NOT NULL
);

CREATE TABLE account_session (
  id integer NOT NULL primary key AUTOINCREMENT,
  account_id int(11) NOT NULL,
  account_type varchar(1) NOT NULL,
  token varchar(46) NOT NULL,
  expiry_date date NOT NULL,
  application_token varchar(15) DEFAULT NULL
);

CREATE TABLE application (
  id integer NOT NULL primary key AUTOINCREMENT,
  dev_account_id int(11) NOT NULL,
  app_name varchar(50) NOT NULL,
  app_token varchar(15) NOT NULL
);

CREATE TABLE questions (
  id integer NOT NULL primary key AUTOINCREMENT,
  question varchar(200) NOT NULL
);

INSERT INTO questions VALUES(1,'What is the favorite road on which you most like to travel?');
INSERT INTO questions VALUES(2,'If you could be a character out of any novel, who would you be?');
INSERT INTO questions VALUES(3,'Who was your least favorite boss?');
INSERT INTO questions VALUES(4,'What is the name of your favorite relative not in the immediate family?');
INSERT INTO questions VALUES(5,'What was your childhood phone number?');
INSERT INTO questions VALUES(6,'What is the name of your least favorite teacher?');
INSERT INTO questions VALUES(7,'Where do you want to retire?');
INSERT INTO questions VALUES(8,'What is your dream car?');
INSERT INTO questions VALUES(9,'If you won a million dollars, what is the most extravagant purchase you would make?');
INSERT INTO questions VALUES(10,'In what town or city was your first full time job?');

CREATE TABLE user (
  id integer NOT NULL primary key AUTOINCREMENT,
  user_key varchar(5) NOT NULL,
  account_id int(11) NOT NULL,
  connected tinyint(1) NOT NULL,
  app_id int(11) NOT NULL
);

CREATE TABLE device (
  id integer NOT NULL primary key AUTOINCREMENT,
  device_key varchar(5) NOT NULL,
  account_id int(11) NOT NULL,
  connected tinyint(1) NOT NULL,
  app_id int(11) NOT NULL
);

CREATE TABLE subscriptions (
  id integer NOT NULL primary key AUTOINCREMENT,
  app_id int(11) NOT NULL,
  account_id int(11) NOT NULL
);

COMMIT;