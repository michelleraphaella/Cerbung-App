<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		echo json_encode(array('result' => 'ERROR', 'msg' => 'Failed to connect DB'));
		die();
	}

	if (!isset($_POST['username'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	//convert ke int
	$username = $_POST['username'];

	$sql = "SELECT max(date_format(substr(tanggal, 1, 10), '%d/%m/%Y')) as tanggal FROM paragraf 
		WHERE username=?";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("s", $username);
	$stmt->execute();

	$result = $stmt->get_result();
	
	$array = array();

	while ($obj = $result->fetch_object()) {
	    $array[] = $obj;
	}

	echo json_encode(array('result'=> 'OK', 'data' => $array));
?>