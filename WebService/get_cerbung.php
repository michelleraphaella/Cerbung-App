<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "native_160821016", "ubaya", "native_160821016");

	if ($c->connect_errno) {
		echo json_encode(array('result' => 'ERROR', 'msg' => 'Failed to connect DB'));
		die();
	}

	$sql = "SELECT idcerbung, title, date_format(substr(tanggal, 1, 10), '%d/%m/%Y') as tanggal, jumlah_paragraf, jumlah_like, description, access, url, username, idgenre FROM cerbung";
	$result = $c->query($sql);

	$array = array();
	while ($obj = $result->fetch_object()) {
		$array[] = $obj;
	}

	echo json_encode(array('result'=> 'OK', 'data' => $array));
?>