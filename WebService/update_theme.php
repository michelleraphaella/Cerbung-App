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
	else if (!isset($_POST['theme'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	$username = $_POST['username'];
	$theme = (int) $_POST['theme'];

	$sql = "UPDATE user SET theme=?
			WHERE username=?";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("is", $theme, $username);
	$stmt->execute();

	echo json_encode(array('result' => 'OK'));
?>