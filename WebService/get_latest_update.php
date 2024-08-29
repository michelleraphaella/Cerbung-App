<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		echo json_encode(array('result' => 'ERROR', 'msg' => 'Failed to connect DB'));
		die();
	}

	if (!isset($_POST['idcerbung'])) {
		echo json_encode(array('result' => 'ERROR'));
		die();
	}

	//convert ke int
	$idcerbung = (int) $_POST['idcerbung'];

	$sql = "SELECT idparagraf, paragraf, idcerbung, username, max(date_format(substr(tanggal, 1, 10), '%d/%m/%Y')) AS tanggal
		FROM paragraf
		WHERE idcerbung=?
		GROUP BY idparagraf, paragraf, idcerbung, username
		ORDER BY tanggal DESC
		LIMIT 1";
	$stmt = $c->prepare($sql);
	$stmt->bind_param("i", $idcerbung);
	$stmt->execute();

	$result = $stmt->get_result();
	
	$array = array();

	while ($obj = $result->fetch_object()) {
	    $array[] = $obj;
	}

	echo json_encode(array('result'=> 'OK', 'data' => $array));
?>