<?php
if(isset($_REQUEST['type']) && $_REQUEST['type'] == "test"){
	$content = file('./playserver-test.log');
} else if(isset($_REQUEST['type']) && $_REQUEST['type'] == "prod"){
	$content = file('./playserver.log');
} else {
	$content = file('./playserver-dev.log');
}

if(!isset($_REQUEST['lines'])){
	foreach($content as $line)
	{
		echo $line.'<br>';
	}	
} else {
	$number = $_REQUEST['lines'];
	$lines = array_slice($content, (-1 * $number), $number, true);
	foreach($lines as $line)
	{
		echo $line.'<br>';
	}	
}
?>
