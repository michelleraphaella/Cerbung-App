<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		$error = array('result' => 'ERROR', 'msg' => 'Failed to connect DB');
		echo json_encode($error);
		die();
	}

	if (!isset($_POST['paragraf'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['idcerbung'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['username'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	$paragraf = $_POST['paragraf'];
	$idcerbung = (int) $_POST['idcerbung'];
	$username = $_POST['username'];

	$sql = "INSERT INTO paragraf(paragraf, idcerbung, username) VALUES(?,?,?)";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("sis", $paragraf, $idcerbung, $username);
	$stmt->execute();

	echo json_encode(array('result'=> 'OK'));
?>