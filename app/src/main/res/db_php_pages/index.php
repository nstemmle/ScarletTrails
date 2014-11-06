<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 * check for POST request 
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'db_functions.php';
    $db = new db_functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $username = $_POST['username'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByUsernameAndPassword($username, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["user_id"] = $user["USER_ID"];
            $response["user"]["username"] = $user["USERNAME"];
            $response["user"]["first_name"] = $user["FIRST_NAME"];
            $response["user"]["last_name"] = $user["LAST_NAME"];
            $response["user"]["email"] = $user["EMAIL"];
            $response["user"]["dob"] = $user["DOB"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect username or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $first_name = $_POST['first_name'];
        $last_name = $_POST['last_name'];
        $email = $_POST['email'];
        $dob = $_POST['dob'];
        $username = $_POST['username'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isUserExisted($username, $email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed.";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($first_name, $last_name, $email, $dob, $username, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["user_id"] = $user["USER_ID"];
                $response["user"]["username"] = $user["USERNAME"];
                $response["user"]["first_name"] = $user["FIRST_NAME"];
                $response["user"]["last_name"] = $user["LAST_NAME"];
                $response["user"]["email"] = $user["EMAIL"];
                $response["user"]["dob"] = $user["DOB"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registration";
                echo json_encode($response);
            }
        }
    } else if ($tag == 'getTrail') {

        $searchKey = $_POST['searchKey'];
        $getByTag = $_POST['getByTag'];

        if(isset($searchKey) && $searchKey != '')
        {
           if($getByTag == 'getById') {
              $trail = $db->getTrailById($searchKey);
           }
           else if($getByTag == 'getByZipCityName') {
              $trail = $db->getTrailByZipcodeOrCityOrName($searchKey);
           }
           else if($getByTag == 'getAllTrails') {
              $trail = $db->getAllTrails();
           }
           else {
              echo "Invalid Request";
           }
        } 
        if ($trail != false) {
            // user found
            // echo json with success = 1
            $trail["success"] = 1;
            // echoing JSON response
            echo json_encode($trail);  
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Failed to return Trails.";
            echo json_encode($response);
        }
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>