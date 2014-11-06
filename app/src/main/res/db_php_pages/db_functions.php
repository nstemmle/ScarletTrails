<?php
 
class db_functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'db_connect.php';
        // connecting to database
        $this->db = new db_connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    public function storeUser($first_name, $last_name, $email, $dob, $username, $password) {
        $result = mysql_query("INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL, DOB, USERNAME, PASSWORD) 
                                         VALUES('$first_name', '$last_name', '$email', '$dob', '$username', '$password')");
        // check for successful store
        if ($result) {
            // get user details 
            $user_id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM USER WHERE USER_ID = $user_id");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByUsernameAndPassword($username, $password) {
        $result = mysql_query("SELECT * FROM USER WHERE USERNAME = '$username'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $encrypted_password = $result['PASSWORD'];
            // check for password equality
            if ($encrypted_password == $password) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($username, $email) {
        $result = mysql_query("SELECT USERNAME, EMAIL FROM USER WHERE USERNAME = '$username' OR EMAIL = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Get trail by trail identification
     */
    public function getTrailById($searchKey) {
        $result = mysql_query("SELECT * FROM VW_TRAIL_COLLECTION WHERE TRAIL_ID = '$searchKey'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {

            $response["trailList"] = array();
 
            while ($row = mysql_fetch_array($result)) {

               $trail = array();
               $trail["trail_id"] = $row["TRAIL_ID"];
               $trail["name"] = $row["NAME"];
               $trail["distance"] = $row["DISTANCE"];
               $trail["elevation"] = $row["ELEVATION"];
               $trail["duration"] = $row["DURATION"];
               $trail["difficulty"] = $row["DIFFICULTY"];
               $trail["gear"] = $row["GEAR"];
               $trail["conditions"] = $row["CONDITIONS"];
               $trail["pet_friendly"] = $row["PET_FRIENDLY"];
               $trail["rating"] = $row["RATING"];
               $trail["location_id"] = $row["LOCATION_ID"];
               $trail["x"] = $row["X"];
               $trail["y"] = $row["Y"];
               $trail["zipcode"] = $row["ZIPCODE"];
               $trail["city"] = $row["CITY"];
               $trail["state"] = $row["STATE"];
               $trail["country"] = $row["COUNTRY"];  

               array_push($response["trailList"], $trail);
            }
            return $response;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Get trail by zipcode or city or name
     */
    public function getTrailByZipcodeOrCityOrName ($searchKey) {
        $result = mysql_query("SELECT * FROM VW_TRAIL_COLLECTION WHERE ZIPCODE LIKE '%$searchKey%' 
                                                                    OR CITY    LIKE '%$searchKey%' 
                                                                    OR NAME    LIKE '%$searchKey%'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {

            $response["trailList"] = array();
 
            while ($row = mysql_fetch_array($result)) {

               $trail = array();
               $trail["trail_id"] = $row["TRAIL_ID"];
               $trail["name"] = $row["NAME"];
               $trail["distance"] = $row["DISTANCE"];
               $trail["elevation"] = $row["ELEVATION"];
               $trail["duration"] = $row["DURATION"];
               $trail["difficulty"] = $row["DIFFICULTY"];
               $trail["gear"] = $row["GEAR"];
               $trail["conditions"] = $row["CONDITIONS"];
               $trail["pet_friendly"] = $row["PET_FRIENDLY"];
               $trail["rating"] = $row["RATING"];
               $trail["location_id"] = $row["LOCATION_ID"];
               $trail["x"] = $row["X"];
               $trail["y"] = $row["Y"];
               $trail["zipcode"] = $row["ZIPCODE"];
               $trail["city"] = $row["CITY"];
               $trail["state"] = $row["STATE"];
               $trail["country"] = $row["COUNTRY"];  

               array_push($response["trailList"], $trail);
            }
            return $response;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Get all the trails
     */
    public function getAllTrails () {
        $result = mysql_query("SELECT * FROM VW_TRAIL_COLLECTION") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            $response["trailList"] = array();
 
            while ($row = mysql_fetch_array($result)) {

               $trail = array();
               $trail["trail_id"] = $row["TRAIL_ID"];
               $trail["name"] = $row["NAME"];
               $trail["distance"] = $row["DISTANCE"];
               $trail["elevation"] = $row["ELEVATION"];
               $trail["duration"] = $row["DURATION"];
               $trail["difficulty"] = $row["DIFFICULTY"];
               $trail["gear"] = $row["GEAR"];
               $trail["conditions"] = $row["CONDITIONS"];
               $trail["pet_friendly"] = $row["PET_FRIENDLY"];
               $trail["rating"] = $row["RATING"];
               $trail["location_id"] = $row["LOCATION_ID"];
               $trail["x"] = $row["X"];
               $trail["y"] = $row["Y"];
               $trail["zipcode"] = $row["ZIPCODE"];
               $trail["city"] = $row["CITY"];
               $trail["state"] = $row["STATE"];
               $trail["country"] = $row["COUNTRY"];  

               array_push($response["trailList"], $trail);
            }
            return $response;
        } else {
            // user not existed
            return false;
        }
    }
}
 
?>