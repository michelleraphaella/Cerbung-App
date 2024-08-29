<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		$error = array('result' => 'ERROR', 'msg' => 'Failed to connect DB');
		echo json_encode($error);
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

	$sql = "DELETE FROM user_requests_cerbung WHERE username=? AND idcerbung=?";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("si", $username, $idcerbung);
	$stmt->execute();

	echo json_encode(array('result'=> 'OK'));
?>