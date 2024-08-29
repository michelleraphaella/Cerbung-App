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
	else if (!isset($_POST['idcerbung'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	$username = $_POST['username'];
	$idcerbung = (int) $_POST['idcerbung'];

	$sql = "SELECT username, idcerbung FROM user_likes_cerbung WHERE username=? AND idcerbung=?";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("si", $username, $idcerbung);
	$stmt->execute();

	$result = $stmt->get_result();
	
	$array = array();

	while ($obj = $result->fetch_object()) {
	    $array[] = $obj;
	}

	if (empty($array)) {
    	echo json_encode(array('result' => 'ERROR', 'msg' => 'No data found'));
	} else {
    	echo json_encode(array('result' => 'OK', 'data' => $array));
	}
?>