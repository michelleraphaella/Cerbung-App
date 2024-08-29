<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		$error = array('result' => 'ERROR', 'msg' => 'Failed to connect DB');
		echo json_encode($error);
		die();
	}

	if (!isset($_POST['title'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['description'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['access'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['url'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['username'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}
	else if (!isset($_POST['idgenre'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	$title = $_POST['title'];
	$description = $_POST['description'];
	$access = $_POST['access'];
	$url = $_POST['url'];
	$username = $_POST['username'];
	$idgenre = (int) $_POST['idgenre'];

	$sql = "INSERT INTO cerbung(title, description, access, url, username, idgenre) VALUES(?,?,?,?,?,?)";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("sssssi", $title, $description, $access, $url, $username, $idgenre);
	$stmt->execute();
	$idcerbung = $stmt->insert_id;

	echo json_encode(array('result'=> 'OK', 'idcerbung'=>$idcerbung));
?>