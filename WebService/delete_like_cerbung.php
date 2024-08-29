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

	$c->begin_transaction();

	try{
		$sql = "DELETE FROM user_likes_cerbung WHERE username=? AND idcerbung=?";
		$stmt = $c->prepare($sql);
		$stmt->bind_param("si", $username, $idcerbung);
		$stmt->execute();

		$sql2 = "UPDATE cerbung SET jumlah_like = jumlah_like-1
					WHERE idcerbung=?";
		$stmt2 = $c->prepare($sql2);
		$stmt2->bind_param("i", $idcerbung);
		$stmt2->execute();

		$c->commit();

		echo json_encode(array('result'=> 'OK'));
	}
	catch (Exception $e) {
		$c->rollback();
		echo json_encode(array('result' => 'ERROR', 'msg' => $e->getMessage()));
	}
?>