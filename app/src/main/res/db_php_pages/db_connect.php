<?php
 
class db_connect {
 
    // constructor
    function __construct() {
         
    }
 
    // destructor
    function __destruct() {
        // $this->close();
    }
 
    // Connecting to database
    public function connect() {
        require_once 'db_config.php';
        // connecting to mysql
        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD);
        // selecting database
        $db_found = mysql_select_db(DB_DATABASE);

        // return database handler
        return $con;
    }
 
    // Closing database connection
    public function close() {
        mysql_close();
    }
 
}
 
?>