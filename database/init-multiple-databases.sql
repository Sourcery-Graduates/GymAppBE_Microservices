CREATE DATABASE authentication;
CREATE DATABASE userprofile;
CREATE DATABASE workout;

GRANT ALL PRIVILEGES ON DATABASE authentication TO gym_app_user;
GRANT ALL PRIVILEGES ON DATABASE userprofile TO gym_app_user;
GRANT ALL PRIVILEGES ON DATABASE workout TO gym_app_user;
