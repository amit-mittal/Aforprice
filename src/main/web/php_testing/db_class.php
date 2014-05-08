<?php
class DBQueryException extends Exception {};
class DBConnectionException extends Exception {};

class db_class {
	var $host;
	var $user;
	var $pas;
	var $database; 
	var $connection;
	var $result;
	
	function __construct($host, $user, $pas, $database) {
		$this->host = $host;
		$this->user = $user;
		$this->pas = $pas;
		$this->database = $database;
	}
	
	function connect() {
		$this->connection = new mysqli($this->host, $this->user, $this->pas, $this->database);
		if($this->connection->connect_errno)
			throw new DBConnectionException("couldn't connect to the database : ".$this->connection->connect_errno." - ".$this->connection->connect_error);
		else
			return true;
	}
	
	function makeQuery($query) {
		$this->result = $this->connection->query($query);
		if($this->connection->errno)
			throw new DBQueryException("query was not succesful : ".$this->connection->errno." - ".$this->connection->error);
		else
			return true;
	}
	
	function getRow($assoc = false) {
		if($assoc) {
			if($row = $this->result->fetch_assoc())
				return $row;
		}
		else if($row = $this->result->fetch_row())
			return $row;
		
		$this->result->free();
		return NULL;
	}
	
	function closeResult() {
		$this->result->free();
	}
	
	function __destruct() {
		$this->connection->close();
	}
};
?>